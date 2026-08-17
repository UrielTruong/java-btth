package gui;

import model.HoaDon;

import javax.swing.*;
import java.awt.*;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("HỆ THỐNG ĐẶT TRÀ SỮA");
        setSize(1000, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Danh sách hóa đơn đã lập, dùng chung giữa khu lập hóa đơn và khu quản lý hóa đơn
        List<HoaDon> dsHoaDon = new ArrayList<>();

        JTabbedPane tabs = new JTabbedPane();
        HoaDonPanel hoaDonPanel = new HoaDonPanel(dsHoaDon);
        BanPanel banPanel = new BanPanel();
        QuanLyHoaDonPanel quanLyHoaDonPanel = new QuanLyHoaDonPanel(dsHoaDon);

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
            }
        });

        add(tabs, BorderLayout.CENTER);

    }
}
