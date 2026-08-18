package gui;

import db.ConnectionFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.SQLException;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("HỆ THỐNG ĐẶT TRÀ SỮA");
        setSize(1000, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        kiemTraKetNoiCSDL();

        JTabbedPane tabs = new JTabbedPane();
        HoaDonPanel hoaDonPanel = new HoaDonPanel();
        BanPanel banPanel = new BanPanel();
        QuanLyHoaDonPanel quanLyHoaDonPanel = new QuanLyHoaDonPanel();

        tabs.addTab("Quản lý món", new MonPanel());
        tabs.addTab("Quản lý bàn", banPanel);
        tabs.addTab("Lập hóa đơn", hoaDonPanel);
        tabs.addTab("Quản lý hóa đơn", quanLyHoaDonPanel);

        int viTriTabHoaDon = tabs.indexOfComponent(hoaDonPanel);
        banPanel.setChonBanListener(ten -> {
            hoaDonPanel.chonBan(ten);
            tabs.setSelectedIndex(viTriTabHoaDon);
        });

        // Mỗi khi chuyển sang tab Quản lý hóa đơn thì nạp lại dữ liệu mới nhất
        tabs.addChangeListener(e -> {
            if (tabs.getSelectedComponent() == quanLyHoaDonPanel) {
                quanLyHoaDonPanel.lamMoiDuLieu();
            } else if (tabs.getSelectedComponent() == hoaDonPanel) {
                hoaDonPanel.lamMoiDanhSachMon();
            }
        });

        add(tabs, BorderLayout.CENTER);

    }

    // Kiểm tra kết nối MySQL 1 lần lúc khởi động để báo lỗi rõ ràng thay vì để từng panel tự báo rải rác
    private void kiemTraKetNoiCSDL() {
        try (Connection conn = ConnectionFactory.getConnection()) {
            // Kết nối thành công, không cần làm gì thêm
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                    "Không kết nối được MySQL:\n" + ex.getMessage(),
                    "Lỗi kết nối CSDL", JOptionPane.ERROR_MESSAGE);
        }
    }
}
