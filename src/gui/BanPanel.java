package gui;

import dao.BanDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class BanPanel extends JPanel {
    public static final String TRONG = "Trống";
    public static final String DANG_PHUC_VU = "Đang phục vụ";
    public static final String DA_DAT = "Đã đặt";

    private static final Color MAU_TRONG = new Color(152, 251, 152);
    private static final Color MAU_DANG_PHUC_VU = new Color(255, 127, 80);
    private static final Color MAU_DA_DAT = new Color(255, 215, 0);

    private final BanDAO banDAO = new BanDAO();

    // Phải khớp tên với các mục trong cbSoBan của HoaDonPanel
    private final String[] tenBan;
    private final JButton[] nutBan;
    private final String[] trangThaiBan;

    // Sự kiện báo cho MainFrame biết người dùng vừa chọn bàn nào
    public interface ChonBanListener {
        void onChonBan(String tenBan);
    }
    private ChonBanListener chonBanListener;

    public BanPanel() {
        Map<String, String> duLieuBan = taiDuLieuBanTuDB();
        tenBan = duLieuBan.keySet().toArray(new String[0]);
        nutBan = new JButton[tenBan.length];
        trangThaiBan = duLieuBan.values().toArray(new String[0]);

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelLuoi = new JPanel(new GridLayout(0, 3, 15, 15));
        panelLuoi.setBorder(BorderFactory.createTitledBorder("Sơ đồ bàn (click để lập hóa đơn, chuột phải để đổi trạng thái)"));

        for (int i = 0; i < tenBan.length; i++) {
            JButton btn = new JButton();
            btn.setPreferredSize(new Dimension(130, 90));
            btn.setFont(new Font("SansSerif", Font.BOLD, 14));
            btn.setOpaque(true);

            nutBan[i] = btn;
            capNhatGiaoDienNut(i);

            final int idx = i;
            btn.addActionListener(e -> xuLyClickBan(idx));
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) { moPopupNeuCan(e, idx); }

                @Override
                public void mouseReleased(MouseEvent e) { moPopupNeuCan(e, idx); }
            });

            panelLuoi.add(btn);
        }

        add(panelLuoi, BorderLayout.CENTER);
        add(taoChuThich(), BorderLayout.SOUTH);
    }

    private Map<String, String> taiDuLieuBanTuDB() {
        try {
            Map<String, String> ds = banDAO.layTatCa();
            if (!ds.isEmpty()) return ds;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách bàn từ CSDL: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        // CSDL rỗng hoặc lỗi kết nối -> dùng tạm 5 bàn mặc định để giao diện vẫn dùng được
        Map<String, String> macDinh = new LinkedHashMap<>();
        for (int i = 1; i <= 5; i++) {
            macDinh.put("Bàn " + i, TRONG);
        }
        return macDinh;
    }

    private JPanel taoChuThich() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        panel.add(taoOChuThich(MAU_TRONG, TRONG));
        panel.add(taoOChuThich(MAU_DANG_PHUC_VU, DANG_PHUC_VU));
        panel.add(taoOChuThich(MAU_DA_DAT, DA_DAT));
        return panel;
    }

    private JLabel taoOChuThich(Color mau, String nhan) {
        JLabel lbl = new JLabel("  " + nhan + "  ");
        lbl.setOpaque(true);
        lbl.setBackground(mau);
        lbl.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return lbl;
    }

    // Đổi màu nền + nhãn nút theo trạng thái hiện tại của bàn
    private void capNhatGiaoDienNut(int idx) {
        JButton btn = nutBan[idx];
        String trangThai = trangThaiBan[idx];

        btn.setText("<html><center>" + tenBan[idx] + "<br>" + trangThai + "</center></html>");

        switch (trangThai) {
            case DANG_PHUC_VU:
                btn.setBackground(MAU_DANG_PHUC_VU);
                break;
            case DA_DAT:
                btn.setBackground(MAU_DA_DAT);
                break;
            default:
                btn.setBackground(MAU_TRONG);
        }
    }

    // Click trái -> báo cho MainFrame mở tab Lập hóa đơn với bàn đã chọn
    private void xuLyClickBan(int idx) {
        if (chonBanListener != null) {
            chonBanListener.onChonBan(tenBan[idx]);
        }
    }

    // Click phải -> hiện menu cho phép đổi trạng thái bàn
    private void moPopupNeuCan(MouseEvent e, int idx) {
        if (!e.isPopupTrigger()) return;

        JPopupMenu popup = new JPopupMenu();
        for (String trangThai : new String[]{TRONG, DANG_PHUC_VU, DA_DAT}) {
            JMenuItem item = new JMenuItem(trangThai);
            item.addActionListener(ev -> {
                try {
                    banDAO.capNhatTrangThai(tenBan[idx], trangThai);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật trạng thái bàn trong CSDL: " + ex.getMessage(),
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                trangThaiBan[idx] = trangThai;
                capNhatGiaoDienNut(idx);
            });
            popup.add(item);
        }
        popup.show(e.getComponent(), e.getX(), e.getY());
    }

    public void setChonBanListener(ChonBanListener listener) {
        this.chonBanListener = listener;
    }
}
