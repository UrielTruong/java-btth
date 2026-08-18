package dao;

import db.ConnectionFactory;
import model.ChiTietHoaDon;
import model.HoaDon;
import model.Mon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HoaDonDAO {

    // Lưu 1 hóa đơn (kèm toàn bộ chi tiết) trong 1 transaction: hoặc lưu hết, hoặc không lưu gì cả
    public void luuHoaDon(HoaDon hd) throws SQLException {
        String sqlHD = "INSERT INTO hoa_don (ma_hoa_don, ngay_lap, ten_khach_hang, so_dt, so_ban) VALUES (?, ?, ?, ?, ?)";
        String sqlCT = "INSERT INTO chi_tiet_hoa_don (ma_hoa_don, ma_mon, so_luong, size, tien_topping) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement(sqlHD)) {
                    ps.setString(1, hd.getMaHoaDon());
                    ps.setString(2, hd.getNgayLap());
                    ps.setString(3, hd.getTenKhachHang());
                    ps.setString(4, hd.getSoDT());
                    ps.setString(5, hd.getSoBan());
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = conn.prepareStatement(sqlCT)) {
                    for (ChiTietHoaDon ct : hd.getChiTiet()) {
                        ps.setString(1, hd.getMaHoaDon());
                        ps.setString(2, ct.getMon().getMaMon());
                        ps.setInt(3, ct.getSoLuong());
                        ps.setString(4, ct.getSize());
                        ps.setDouble(5, ct.getTienTopping());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }

                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public List<HoaDon> layTatCa() throws SQLException {
        return timKiem(null);
    }

    // Lấy số thứ tự lớn nhất đã dùng trong mã hóa đơn dạng "HD001", "HD002"...
    // để lần lập hóa đơn kế tiếp (kể cả sau khi tắt/mở lại app) không bị trùng mã đã có trong CSDL
    public int laySoThuTuLonNhat() throws SQLException {
        String sql = "SELECT MAX(CAST(SUBSTRING(ma_hoa_don, 3) AS UNSIGNED)) AS so_lon_nhat " +
                "FROM hoa_don WHERE ma_hoa_don REGEXP '^HD[0-9]+$'";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("so_lon_nhat");
            }
            return 0;
        }
    }

    // Tìm theo Mã hóa đơn / Ngày / Khách hàng (khớp gần đúng, không phân biệt hoa thường); truyền null hoặc "" để lấy tất cả
    public List<HoaDon> timKiem(String tuKhoa) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT hd.ma_hoa_don, hd.ngay_lap, hd.ten_khach_hang, hd.so_dt, hd.so_ban, " +
                "       ct.so_luong, ct.size, ct.tien_topping, " +
                "       m.ma_mon, m.ten_mon, m.loai, m.don_gia " +
                "FROM hoa_don hd " +
                "JOIN chi_tiet_hoa_don ct ON ct.ma_hoa_don = hd.ma_hoa_don " +
                "JOIN mon m ON m.ma_mon = ct.ma_mon ");

        boolean coTimKiem = tuKhoa != null && !tuKhoa.trim().isEmpty();
        if (coTimKiem) {
            sql.append("WHERE LOWER(hd.ma_hoa_don) LIKE ? OR LOWER(hd.ngay_lap) LIKE ? OR LOWER(hd.ten_khach_hang) LIKE ? ");
        }
        sql.append("ORDER BY hd.ma_hoa_don");

        // Gom các dòng chi tiết (JOIN nên 1 hóa đơn có thể ra nhiều dòng) lại theo mã hóa đơn
        Map<String, HoaDon> gomTheoMaHD = new LinkedHashMap<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            if (coTimKiem) {
                String pattern = "%" + tuKhoa.trim().toLowerCase() + "%";
                ps.setString(1, pattern);
                ps.setString(2, pattern);
                ps.setString(3, pattern);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String maHD = rs.getString("ma_hoa_don");
                    HoaDon hd = gomTheoMaHD.get(maHD);
                    if (hd == null) {
                        hd = new HoaDon(rs.getString("ten_khach_hang"), rs.getString("so_dt"), rs.getString("so_ban"));
                        hd.setMaHoaDon(maHD);
                        hd.setNgayLap(rs.getString("ngay_lap"));
                        gomTheoMaHD.put(maHD, hd);
                    }

                    Mon mon = new Mon(rs.getString("ma_mon"), rs.getString("ten_mon"), rs.getString("loai"), rs.getDouble("don_gia"));
                    ChiTietHoaDon ct = new ChiTietHoaDon();
                    ct.setMon(mon);
                    ct.setSoLuong(rs.getInt("so_luong"));
                    ct.setSize(rs.getString("size"));
                    ct.setTienTopping(rs.getDouble("tien_topping"));
                    hd.themChiTiet(ct);
                }
            }
        }

        return new ArrayList<>(gomTheoMaHD.values());
    }
}
