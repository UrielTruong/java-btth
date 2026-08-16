package gui;

import model.ChiTietHoaDon;
import model.HoaDon;
import model.Mon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HoaDonPanel extends JPanel {
    private HoaDon hoaDon;
    private List<Mon> dsMon;
    private DecimalFormat df = new DecimalFormat("#,### VNĐ");
    private int maHDCount = 1;

    // Khu 1: Thông tin
    private JTextField txtMaHD, txtNgay, txtTenKhach, txtSoDT;
    private JComboBox<String> cbSoBan;

    // Khu 2: Chọn món
    private JComboBox<String> cbMon;
    private JRadioButton rdoM, rdoL;
    private ButtonGroup groupSize;
    private JComboBox<String> cbDuong, cbDa;
    private JCheckBox chkTranChau, chkPudding, chkThach, chkFlan;
    private JSpinner spnSoLuong;
    private JButton btnThemMon;

    // Khu 3: Bảng chi tiết
    private JTable tableChiTiet;
    private DefaultTableModel modelChiTiet;

    // Khu 4: Tổng kết & Thao tác
    private JLabel lblTamTinh, lblGiamGia, lblTongTien;
    private JButton btnThanhToan, btnXoaMon, btnHuyHoaDon;

    public HoaDonPanel() {
        this.hoaDon = new HoaDon();
        this.dsMon = khoiTaoDanhSachMon();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelTop = new JPanel(new GridLayout(1, 2, 10, 10));
        panelTop.add(taoKhuThongTin());
        panelTop.add(taoKhuChonMon());

        add(panelTop, BorderLayout.NORTH);
        add(taoKhuBangChiTiet(), BorderLayout.CENTER);
        add(taoKhuTongKet(), BorderLayout.SOUTH);

        ganSuKien();
        capNhatTongTien();
    }

    private List<Mon> khoiTaoDanhSachMon() {
        List<Mon> list = new ArrayList<>();
        list.add(new Mon("TS01", "Trà sữa Matcha", "Trà sữa", 35000));
        list.add(new Mon("TS02", "Trà sữa Trân châu", "Trà sữa", 30000));
        list.add(new Mon("TC01", "Trà đào cam sả", "Trà trái cây", 35000));
        return list;
    }

    private JPanel taoKhuThongTin() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Thông tin hóa đơn"));

        txtMaHD = new JTextField(String.format("HD%03d", maHDCount));
        txtMaHD.setEditable(false);

        txtNgay = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        txtNgay.setEditable(false);

        txtTenKhach = new JTextField();
        txtSoDT = new JTextField();
        cbSoBan = new JComboBox<>(new String[]{"Bàn 1", "Bàn 2", "Bàn 3", "Bàn 4", "Bàn 5", "Mang về"});

        panel.add(new JLabel("Mã hóa đơn:")); panel.add(txtMaHD);
        panel.add(new JLabel("Ngày lập:")); panel.add(txtNgay);
        panel.add(new JLabel("Tên khách:")); panel.add(txtTenKhach);
        panel.add(new JLabel("Số điện thoại:")); panel.add(txtSoDT);
        panel.add(new JLabel("Số bàn:")); panel.add(cbSoBan);

        return panel;
    }

    private JPanel taoKhuChonMon() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Chọn món & Tùy chọn"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 5, 3, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Món:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        cbMon = new JComboBox<>();
        for (Mon m : dsMon) {
            cbMon.addItem(m.getTenMon() + " (" + df.format(m.getDonGia()) + ")");
        }
        panel.add(cbMon, gbc);

        gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Size:"), gbc);

        rdoM = new JRadioButton("M", true);
        rdoL = new JRadioButton("L");
        groupSize = new ButtonGroup();
        groupSize.add(rdoM); groupSize.add(rdoL);

        JPanel pnlSize = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlSize.add(rdoM); pnlSize.add(rdoL);

        gbc.gridx = 1; panel.add(pnlSize, gbc);
        gbc.gridx = 2; panel.add(new JLabel("Số lượng:"), gbc);

        spnSoLuong = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        gbc.gridx = 3; panel.add(spnSoLuong, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Đường:"), gbc);
        String[] dsMuc = {"100%", "70%", "50%", "30%", "0%"};
        cbDuong = new JComboBox<>(dsMuc);
        gbc.gridx = 1; panel.add(cbDuong, gbc);

        gbc.gridx = 2; panel.add(new JLabel("Đá:"), gbc);
        cbDa = new JComboBox<>(dsMuc);
        gbc.gridx = 3; panel.add(cbDa, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Topping:"), gbc);

        chkTranChau = new JCheckBox("Trân châu (+5k)");
        chkPudding = new JCheckBox("Pudding (+7k)");
        chkThach = new JCheckBox("Thạch (+5k)");
        chkFlan = new JCheckBox("Flan (+10k)");

        JPanel pnlTopping = new JPanel(new GridLayout(2, 2, 2, 2));
        pnlTopping.add(chkTranChau); pnlTopping.add(chkPudding);
        pnlTopping.add(chkThach); pnlTopping.add(chkFlan);

        gbc.gridx = 1; gbc.gridwidth = 3; panel.add(pnlTopping, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 4;
        btnThemMon = new JButton("Thêm vào hóa đơn");
        panel.add(btnThemMon, gbc);

        return panel;
    }

    private JPanel taoKhuBangChiTiet() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Chi tiết hóa đơn"));

        String[] cols = {"Món", "SL", "Đơn giá", "Thành tiền"};
        modelChiTiet = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tableChiTiet = new JTable(modelChiTiet);
        panel.add(new JScrollPane(tableChiTiet), BorderLayout.CENTER);
        return panel;
    }

    private JPanel taoKhuTongKet() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Tổng kết & Thanh toán"));

        JPanel pnlTien = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 5));
        lblTamTinh = new JLabel("Tạm tính: 0 VNĐ");
        lblGiamGia = new JLabel("Giảm giá (0%): 0 VNĐ");
        lblTongTien = new JLabel("<html><b>Tổng tiền: <font color='red'>0 VNĐ</font></b></html>");

        lblTamTinh.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblGiamGia.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblTongTien.setFont(new Font("SansSerif", Font.BOLD, 16));

        pnlTien.add(lblTamTinh); pnlTien.add(lblGiamGia); pnlTien.add(lblTongTien);

        JPanel pnlNut = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        btnThanhToan = new JButton("Thanh toán");
        btnXoaMon = new JButton("Xóa món");
        btnHuyHoaDon = new JButton("Hủy hóa đơn");

        pnlNut.add(btnThanhToan); pnlNut.add(btnXoaMon); pnlNut.add(btnHuyHoaDon);

        panel.add(pnlTien, BorderLayout.WEST);
        panel.add(pnlNut, BorderLayout.EAST);

        return panel;
    }

    private void ganSuKien() {
        btnThemMon.addActionListener(e -> xuLyThemMon());
        btnXoaMon.addActionListener(e -> xuLyXoaMon());
        btnHuyHoaDon.addActionListener(e -> xuLyHuyHoaDon());
        btnThanhToan.addActionListener(e -> xuLyThanhToan());
    }

    private void capNhatTongTien() {
        double tamTinh = hoaDon.tinhTamTinh();
        int phanTramGiam = (tamTinh >= 100000) ? 5 : 0;
        double tienGiam = tamTinh * (phanTramGiam / 100.0);
        double tongTien = tamTinh - tienGiam;

        lblTamTinh.setText("Tạm tính: " + df.format(tamTinh));
        lblGiamGia.setText(String.format("Giảm giá (%d%%): %s", phanTramGiam, df.format(tienGiam)));
        lblTongTien.setText("<html><b>Tổng tiền: <font color='red'>" + df.format(tongTien) + "</font></b></html>");
    }

    private void xuLyThemMon() {
        int indexMon = cbMon.getSelectedIndex();
        if (indexMon < 0) return;

        Mon monChon = dsMon.get(indexMon);

        double tienTopping = 0;
        if (chkTranChau.isSelected()) tienTopping += 5000;
        if (chkPudding.isSelected()) tienTopping += 7000;
        if (chkThach.isSelected()) tienTopping += 5000;
        if (chkFlan.isSelected()) tienTopping += 10000;

        String size = rdoL.isSelected() ? "L" : "M";
        int soLuong = (int) spnSoLuong.getValue();

        ChiTietHoaDon ct = new ChiTietHoaDon();
        ct.setMon(monChon);
        ct.setSoLuong(soLuong);
        ct.setSize(size);
        ct.setTienTopping(tienTopping);

        hoaDon.themChiTiet(ct);

        capNhatBang();
        capNhatTongTien();
    }

    private void xuLyXoaMon() {
        int row = tableChiTiet.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn món cần xóa khỏi hóa đơn!");
            return;
        }

        hoaDon.getChiTiet().remove(row);
        capNhatBang();
        capNhatTongTien();
    }

    private void xuLyHuyHoaDon() {
        int chon = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn hủy toàn bộ hóa đơn?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (chon == JOptionPane.YES_OPTION) {
            lamMoiForm();
        }
    }

    private void xuLyThanhToan() {
        // 1. Kiểm tra validation
        String tenKhach = txtTenKhach.getText().trim();
        if (tenKhach.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên khách hàng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            txtTenKhach.requestFocus();
            return;
        }

        if (hoaDon.getChiTiet().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Hóa đơn chưa có món nào! Vui lòng chọn món.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Ghép chuỗi hóa đơn bằng StringBuilder
        double tamTinh = hoaDon.tinhTamTinh();
        int phanTramGiam = (tamTinh >= 100000) ? 5 : 0;
        double tienGiam = tamTinh * (phanTramGiam / 100.0);
        double tongTien = tamTinh - tienGiam;

        StringBuilder sb = new StringBuilder();
        sb.append("====================================================\n");
        sb.append("                 HÓA ĐƠN BÁN HÀNG                   \n");
        sb.append("====================================================\n");
        sb.append(String.format("Mã HD     : %s\n", txtMaHD.getText()));
        sb.append(String.format("Ngày lập  : %s\n", txtNgay.getText()));
        sb.append(String.format("Khách hàng: %s\n", tenKhach));
        sb.append(String.format("Số ĐT     : %s\n", txtSoDT.getText().trim()));
        sb.append(String.format("Vị trí    : %s\n", cbSoBan.getSelectedItem()));
        sb.append("----------------------------------------------------\n");
        sb.append(String.format("%-22s %-5s %18s\n", "Món", "SL", "Thành tiền"));
        sb.append("----------------------------------------------------\n");

        for (ChiTietHoaDon ct : hoaDon.getChiTiet()) {
            String tenHienThi = ct.getMon().getTenMon() + " (" + ct.getSize() + ")";
            sb.append(String.format("%-22s x%-4d %18s\n",
                    tenHienThi,
                    ct.getSoLuong(),
                    df.format(ct.tinhThanhTien())));
        }

        sb.append("----------------------------------------------------\n");
        sb.append(String.format("Tạm tính        : %29s\n", df.format(tamTinh)));
        sb.append(String.format("Giảm giá (%2d%%)  : %29s\n", phanTramGiam, df.format(tienGiam)));
        sb.append("----------------------------------------------------\n");
        sb.append(String.format("TỔNG TIỀN       : %29s\n", df.format(tongTien)));
        sb.append("====================================================\n");
        sb.append("           Cảm ơn quý khách & Hẹn gặp lại!          \n");

        // 3. Hiển thị bằng JTextArea + Font Monospaced để căn thẳng cột
        JTextArea ta = new JTextArea(sb.toString());
        ta.setFont(new Font("Monospaced", Font.PLAIN, 13));
        ta.setEditable(false);
        JOptionPane.showMessageDialog(this, ta, "Chi tiết hóa đơn", JOptionPane.INFORMATION_MESSAGE);

        // 4. Hỏi lập hóa đơn mới
        int chon = JOptionPane.showConfirmDialog(this,
                "Thanh toán thành công! Bạn có muốn lập hóa đơn mới không?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (chon == JOptionPane.YES_OPTION) {
            lamMoiForm();
        }
    }

    private void lamMoiForm() {
        hoaDon.getChiTiet().clear();
        txtTenKhach.setText("");
        txtSoDT.setText("");
        cbSoBan.setSelectedIndex(0);
        cbMon.setSelectedIndex(0);
        rdoM.setSelected(true);
        spnSoLuong.setValue(1);
        cbDuong.setSelectedIndex(0);
        cbDa.setSelectedIndex(0);
        chkTranChau.setSelected(false);
        chkPudding.setSelected(false);
        chkThach.setSelected(false);
        chkFlan.setSelected(false);

        maHDCount++;
        txtMaHD.setText(String.format("HD%03d", maHDCount));

        capNhatBang();
        capNhatTongTien();
    }

    private void capNhatBang() {
        modelChiTiet.setRowCount(0);
        for (ChiTietHoaDon ct : hoaDon.getChiTiet()) {
            String tenHienThi = ct.getMon().getTenMon() + " (" + ct.getSize() + ")";
            double donGiaMotLy = ct.getMon().getDonGia() + ct.getPhuThuSize() + ct.getTienTopping();

            modelChiTiet.addRow(new Object[]{
                    tenHienThi,
                    ct.getSoLuong(),
                    df.format(donGiaMotLy),
                    df.format(ct.tinhThanhTien())
            });
        }
    }
}