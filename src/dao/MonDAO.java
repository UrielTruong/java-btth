package dao;

import db.ConnectionFactory;
import model.Mon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MonDAO {

    public List<Mon> layTatCa() throws SQLException {
        String sql = "SELECT ma_mon, ten_mon, loai, don_gia FROM mon ORDER BY ma_mon";
        List<Mon> ds = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ds.add(new Mon(
                        rs.getString("ma_mon"),
                        rs.getString("ten_mon"),
                        rs.getString("loai"),
                        rs.getDouble("don_gia")
                ));
            }
        }
        return ds;
    }

    public void them(Mon m) throws SQLException {
        String sql = "INSERT INTO mon (ma_mon, ten_mon, loai, don_gia) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getMaMon());
            ps.setString(2, m.getTenMon());
            ps.setString(3, m.getLoai());
            ps.setDouble(4, m.getDonGia());
            ps.executeUpdate();
        }
    }

    // maCu: mã món trước khi sửa (dùng làm điều kiện WHERE vì mã món cũng có thể bị đổi)
    public void sua(String maCu, Mon m) throws SQLException {
        String sql = "UPDATE mon SET ma_mon = ?, ten_mon = ?, loai = ?, don_gia = ? WHERE ma_mon = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getMaMon());
            ps.setString(2, m.getTenMon());
            ps.setString(3, m.getLoai());
            ps.setDouble(4, m.getDonGia());
            ps.setString(5, maCu);
            ps.executeUpdate();
        }
    }

    public void xoa(String maMon) throws SQLException {
        String sql = "DELETE FROM mon WHERE ma_mon = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maMon);
            ps.executeUpdate();
        }
    }
}
