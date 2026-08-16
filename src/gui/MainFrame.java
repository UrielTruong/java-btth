package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.BorderLayout;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Mua tà tưa ún đi");
        setSize(1000, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Quản lý món", new MonPanel());
        tabs.addTab("Lập hóa đơn", new HoaDonPanel());
        add(tabs, BorderLayout.CENTER);

    }
}
