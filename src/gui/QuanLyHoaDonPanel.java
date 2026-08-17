package gui;

import model.ChiTietHoaDon;
import model.HoaDon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class QuanLyHoaDonPanel extends JPanel {
    private final List<HoaDon> dsHoaDon;
    // Danh sách hóa đơn đang hiển thị trên bảng, khớp theo thứ tự dòng để tra cứu khi click
    private final List<HoaDon> dsHienThi = new ArrayList<>();
    private final DecimalFormat df = new DecimalFormat("#,### VNĐ");

    private JTextField txtTimKiem;
    private JButton btnTim, btnLamMoi;
    private JTable table;
    private DefaultTableModel model;

    public QuanLyHoaDonPanel(List<HoaDon> dsHoaDon) {
        this.dsHoaDon = dsHoaDon;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(taoKhuTimKiem(), BorderLayout.NORTH);
        add(taoKhuBang(), BorderLayout.CENTER);

        loadTable();
    }

    private JPanel taoKhuTimKiem() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm (theo Mã hóa đơn / Ngày / Khách hàng)"));

        txtTimKiem = new JTextField(20);
        btnTim = new JButton("Tìm");
        btnLamMoi = new JButton("Làm mới");

        btnTim.addActionListener(e -> xuLyTimKiem());
        txtTimKiem.addActionListener(e -> xuLyTimKiem());
        btnLamMoi.addActionListener(e -> {
            txtTimKiem.setText("");
            loadTable();
        });

        panel.add(new JLabel("Tìm kiếm:"));
        panel.add(txtTimKiem);
        panel.add(btnTim);
        panel.add(btnLamMoi);

        return panel;
    }

    private JScrollPane taoKhuBang() {
        String[] cols = {"Mã HĐ", "Ngày", "Khách hàng", "Bàn", "Tổng tiền"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1 && row < dsHienThi.size()) {
                    xemChiTietHoaDon(dsHienThi.get(row));
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách hóa đơn"));
        return scrollPane;
    }

    // Gọi lại mỗi khi tab này được hiển thị để cập nhật các hóa đơn vừa thanh toán
    public void lamMoiDuLieu() {
        loadTable();
    }

    private void loadTable() {
        model.setRowCount(0);
        dsHienThi.clear();
        for (HoaDon hd : dsHoaDon) {
            themDong(hd);
        }
    }

    private void themDong(HoaDon hd) {
        dsHienThi.add(hd);
        model.addRow(new Object[]{
                hd.getMaHoaDon(),
                hd.getNgayLap(),
                hd.getTenKhachHang(),
                hd.getSoBan(),
                df.format(hd.tinhTongTien())
        });
    }

    private void xuLyTimKiem() {
        String tuKhoa = txtTimKiem.getText().trim().toLowerCase();

        if (tuKhoa.isEmpty()) {
            loadTable();
            return;
        }

        model.setRowCount(0);
        dsHienThi.clear();
        for (HoaDon hd : dsHoaDon) {
            String ma = hd.getMaHoaDon() == null ? "" : hd.getMaHoaDon().toLowerCase();
            String ngay = hd.getNgayLap() == null ? "" : hd.getNgayLap().toLowerCase();
            String khach = hd.getTenKhachHang() == null ? "" : hd.getTenKhachHang().toLowerCase();

            if (ma.contains(tuKhoa) || ngay.contains(tuKhoa) || khach.contains(tuKhoa)) {
                themDong(hd);
            }
        }
    }

    // Hiện modal xem lại chi tiết các món đã thanh toán của hóa đơn được click chọn
    private void xemChiTietHoaDon(HoaDon hd) {
        double tamTinh = hd.tinhTamTinh();
        double tienGiam = hd.tinhGiamGia(tamTinh);
        double tongTien = tamTinh - tienGiam;
        int phanTramGiam = tamTinh == 0 ? 0 : (int) Math.round(tienGiam * 100 / tamTinh);

        StringBuilder sb = new StringBuilder();
        sb.append("====================================================\n");
        sb.append("              CHI TIẾT HÓA ĐƠN ĐÃ THANH TOÁN         \n");
        sb.append("====================================================\n");
        sb.append(String.format("Mã HD     : %s\n", hd.getMaHoaDon()));
        sb.append(String.format("Ngày lập  : %s\n", hd.getNgayLap()));
        sb.append(String.format("Khách hàng: %s\n", hd.getTenKhachHang()));
        sb.append(String.format("Số ĐT     : %s\n", hd.getSoDT()));
        sb.append(String.format("Vị trí    : %s\n", hd.getSoBan()));
        sb.append("----------------------------------------------------\n");
        sb.append(String.format("%-22s %-5s %18s\n", "Món", "SL", "Thành tiền"));
        sb.append("----------------------------------------------------\n");

        for (ChiTietHoaDon ct : hd.getChiTiet()) {
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

        JTextArea ta = new JTextArea(sb.toString());
        ta.setFont(new Font("Monospaced", Font.PLAIN, 13));
        ta.setEditable(false);
        JOptionPane.showMessageDialog(this, ta,
                "Chi tiết hóa đơn " + hd.getMaHoaDon(), JOptionPane.INFORMATION_MESSAGE);
    }
}
