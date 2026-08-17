package dao;

import db.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class BanDAO {

    // Trả về danh sách bàn theo đúng thứ tự tên (LinkedHashMap giữ thứ tự chèn/ORDER BY)
    public Map<String, String> layTatCa() throws SQLException {
        String sql = "SELECT ten_ban, trang_thai FROM ban ORDER BY ten_ban";
        Map<String, String> ds = new LinkedHashMap<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ds.put(rs.getString("ten_ban"), rs.getString("trang_thai"));
            }
        }
        return ds;
    }

    public void capNhatTrangThai(String tenBan, String trangThai) throws SQLException {
        String sql = "UPDATE ban SET trang_thai = ? WHERE ten_ban = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            ps.setString(2, tenBan);
            ps.executeUpdate();
        }
    }
}
