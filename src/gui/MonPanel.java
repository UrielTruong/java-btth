package gui;

import model.Mon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MonPanel extends JPanel {
    private JTextField txtMaMon, txtTenMon, txtDonGia;
    private JComboBox<String> cbLoai;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;
    private JTextField txtTimKiem;
    private JButton btnTim;
    private JTable table;
    private DefaultTableModel model;

    // Dữ liệu gốc
    private List<Mon> dsMon = new ArrayList<>();

    public MonPanel() {
        setLayout(new BorderLayout(10, 10));

        // --- 1. KHU NHẬP LIỆU & NÚT BẤM (NORTH) ---
        JPanel panelTop = new JPanel(new BorderLayout(10, 10));
        panelTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelInput = new JPanel(new GridLayout(4, 2, 10, 10));
        txtMaMon = new JTextField();
        txtTenMon = new JTextField();
        txtDonGia = new JTextField();

        String[] dsLoai = {"Trà sữa", "Trà trái cây", "Cà phê", "Đồ ăn vặt", "Khác"};
        cbLoai = new JComboBox<>(dsLoai);

        panelInput.add(new JLabel("Mã món:"));
        panelInput.add(txtMaMon);
        panelInput.add(new JLabel("Tên món:"));
        panelInput.add(txtTenMon);
        panelInput.add(new JLabel("Loại:"));
        panelInput.add(cbLoai);
        panelInput.add(new JLabel("Đơn giá:"));
        panelInput.add(txtDonGia);

        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm mới");

        panelButtons.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        panelButtons.add(btnThem);
        panelButtons.add(btnSua);
        panelButtons.add(btnXoa);
        panelButtons.add(btnLamMoi);

        // --- Khu tìm kiếm (theo Mã món, Tên món hoặc Loại) ---
        JPanel panelSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        txtTimKiem = new JTextField(20);
        btnTim = new JButton("Tìm");
        panelSearch.add(new JLabel("Tìm kiếm:"));
        panelSearch.add(txtTimKiem);
        panelSearch.add(btnTim);


        panelTop.setLayout(new BorderLayout());
        panelTop.add(panelInput, BorderLayout.CENTER);
        panelTop.add(panelButtons, BorderLayout.SOUTH);
        panelTop.add(panelSearch, BorderLayout.NORTH);

        //add panelTop vao top cua main frame
        add(panelTop, BorderLayout.NORTH);

        // --- 2. BẢNG DỮ LIỆU (CENTER) ---
        String[] cols = {"Mã món", "Tên món", "Loại", "Đơn giá"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách món"));

        add(scrollPane, BorderLayout.CENTER);

        // --- 3. DỮ LIỆU MẪU & SỰ KIỆN ---
        khoiTaoDuLieuMau();
        loadTable();
        ganSuKien();
    }

    private void khoiTaoDuLieuMau() {
        dsMon.add(new Mon("TS01", "Trà sữa Matcha", "Trà sữa", 35000));
        dsMon.add(new Mon("TS02", "Trà sữa Trân châu", "Trà sữa", 30000));
        dsMon.add(new Mon("TC01", "Trà đào cam sả", "Trà trái cây", 35000));
    }

    // Đổ dữ liệu từ dsMon ra JTable
    private void loadTable() {
        model.setRowCount(0); // Xóa trắng dòng cũ
        for (Mon m : dsMon) {
            model.addRow(new Object[]{
                    m.getMaMon(),
                    m.getTenMon(),
                    m.getLoai(),
                    (long) m.getDonGia()
            });
        }
    }

    // Gán các sự kiện click nút và click dòng trên bảng
    private void ganSuKien() {
        // Click bảng -> Đưa dữ liệu dòng chọn lên form
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    Mon m = dsMon.get(row);
                    txtMaMon.setText(m.getMaMon());
                    txtTenMon.setText(m.getTenMon());
                    cbLoai.setSelectedItem(m.getLoai());
                    txtDonGia.setText(String.valueOf((long) m.getDonGia()));
                }
            }
        });

        // Nút Thêm
        btnThem.addActionListener(e -> xuLyThem());

        // Nút Sửa
        btnSua.addActionListener(e -> xuLySua());

        // Nút Xóa
        btnXoa.addActionListener(e -> xuLyXoa());

        // Nút Làm mới
        btnLamMoi.addActionListener(e -> xuLyLamMoi());

        // Nút Tìm & nhấn Enter trên ô tìm kiếm
        btnTim.addActionListener(e -> xuLyTimKiem());
        txtTimKiem.addActionListener(e -> xuLyTimKiem());
    }

    // Tìm kiếm theo Mã món, Tên món hoặc Loại (khớp gần đúng, không phân biệt hoa thường)
    private void xuLyTimKiem() {
        String tuKhoa = txtTimKiem.getText().trim().toLowerCase();

        if (tuKhoa.isEmpty()) {
            loadTable();
            return;
        }

        model.setRowCount(0);
        for (Mon m : dsMon) {
            if (m.getMaMon().toLowerCase().contains(tuKhoa)
                    || m.getTenMon().toLowerCase().contains(tuKhoa)
                    || m.getLoai().toLowerCase().contains(tuKhoa)) {
                model.addRow(new Object[]{
                        m.getMaMon(),
                        m.getTenMon(),
                        m.getLoai(),
                        (long) m.getDonGia()
                });
            }
        }
    }

    private void xuLyThem() {
        String ma = txtMaMon.getText().trim();
        String ten = txtTenMon.getText().trim();
        String loai = (String) cbLoai.getSelectedItem();
        String strGia = txtDonGia.getText().trim();

        // Validate rỗng
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã món không được để trống!");
            txtMaMon.requestFocus();
            return;
        }
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên món không được để trống!");
            txtTenMon.requestFocus();
            return;
        }

        // Validate đơn giá số dương
        double donGia;
        try {
            donGia = Double.parseDouble(strGia);
            if (donGia <= 0) {
                JOptionPane.showMessageDialog(this, "Đơn giá phải là số dương!");
                txtDonGia.requestFocus();
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Đơn giá phải là số hợp lệ!");
            txtDonGia.requestFocus();
            return;
        }

        // Validate trùng mã món
        for (Mon m : dsMon) {
            if (m.getMaMon().equalsIgnoreCase(ma)) {
                JOptionPane.showMessageDialog(this, "Mã món đã tồn tại!");
                txtMaMon.requestFocus();
                return;
            }
        }

        // Thêm vào list và cập nhật bảng
        dsMon.add(new Mon(ma, ten, loai, donGia));
        loadTable();
        xuLyLamMoi();
        JOptionPane.showMessageDialog(this, "Thêm món thành công!");
    }

    private void xuLySua() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn món cần sửa");
            return;
        }

        String ma = txtMaMon.getText().trim();
        String ten = txtTenMon.getText().trim();
        String loai = (String) cbLoai.getSelectedItem();
        String strGia = txtDonGia.getText().trim();

        if (ma.isEmpty() || ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã món và Tên món không được để trống!");
            return;
        }

        double donGia;
        try {
            donGia = Double.parseDouble(strGia);
            if (donGia <= 0) {
                JOptionPane.showMessageDialog(this, "Đơn giá phải là số dương!");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Đơn giá phải là số hợp lệ!");
            return;
        }

        // Kiểm tra mã trùng với các món khác (ngoại trừ chính món đang sửa)
        Mon monHienTai = dsMon.get(row);
        if (!monHienTai.getMaMon().equalsIgnoreCase(ma)) {
            for (int i = 0; i < dsMon.size(); i++) {
                if (i != row && dsMon.get(i).getMaMon().equalsIgnoreCase(ma)) {
                    JOptionPane.showMessageDialog(this, "Mã món đã tồn tại ở vị trí khác!");
                    return;
                }
            }
        }

        // Cập nhật thông tin trong list
        monHienTai.setMaMon(ma);
        monHienTai.setTenMon(ten);
        monHienTai.setLoai(loai);
        monHienTai.setDonGia(donGia);

        loadTable();
        JOptionPane.showMessageDialog(this, "Sửa món thành công!");
    }

    private void xuLyXoa() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một món trong bảng.");
            return;
        }

        int chon = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa món này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (chon == JOptionPane.YES_OPTION) {
            dsMon.remove(row);
            loadTable();
            xuLyLamMoi();
            JOptionPane.showMessageDialog(this, "Xóa thành công!");
        }
    }

    private void xuLyLamMoi() {
        txtMaMon.setText("");
        txtTenMon.setText("");
        txtDonGia.setText("");
        cbLoai.setSelectedIndex(0);
        txtTimKiem.setText("");
        table.clearSelection();
        loadTable();
    }
}