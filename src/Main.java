import gui.HoaDonPanel;
import gui.MainFrame;
import gui.MonPanel;
import model.HoaDon;
import model.Mon;
import model.ChiTietHoaDon;

import javax.swing.*;
import java.awt.*;


public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));

        System.out.println("=== KẾT QUẢ NGHIỆM THU CHỨC NĂNG GIẢM GIÁ ===");

        // 1. Test ví dụ đề bài: Tạm tính 250.000 -> Giảm 10% (25.000) -> Còn 225.000
        Mon mon250k = new Mon("M01", "Combo Trà Sữa", "Đồ uống", 125000);
        ChiTietHoaDon ct1 = new ChiTietHoaDon();
        ct1.setMon(mon250k);
        ct1.setSoLuong(2); // 125.000 * 2 = 250.000
        HoaDon hd1 = new HoaDon();
        hd1.themChiTiet(ct1);
        chayTest("Ví dụ đề bài (250.000 VNĐ)", hd1);

        // 2. Test mốc biên 100.000: Từ 100k -> Giảm 5% (5.000) -> Còn 95.000
        Mon mon100k = new Mon("M02", "Cà phê máy", "Đồ uống", 100000);
        ChiTietHoaDon ct2 = new ChiTietHoaDon();
        ct2.setMon(mon100k);
        ct2.setSoLuong(1);
        HoaDon hd2 = new HoaDon();
        hd2.themChiTiet(ct2);
        chayTest("Mốc biên 100.000 VNĐ (Bắt đầu giảm 5%)", hd2);

        // 3. Test mốc biên 500.000: Từ 500k -> Giảm 15% (75.000) -> Còn 425.000
        Mon mon500k = new Mon("M03", "Tiệc Trà Sữa", "Đồ uống", 250000);
        ChiTietHoaDon ct3 = new ChiTietHoaDon();
        ct3.setMon(mon500k);
        ct3.setSoLuong(2); // 250.000 * 2 = 500.000
        HoaDon hd3 = new HoaDon();
        hd3.themChiTiet(ct3);
        chayTest("Mốc biên 500.000 VNĐ (Bắt đầu giảm 15%)", hd3);
    }

    private static void chayTest(String tenTest, HoaDon hd) {
        double tamTinh = hd.tinhTamTinh();
        double tienGiam = hd.tinhGiamGia(tamTinh);
        double tongTien = hd.tinhTongTien();

        System.out.println("\n--- " + tenTest + " ---");
        System.out.println("Tạm tính:  " + (long)tamTinh + " VNĐ");
        System.out.println("Tiền giảm: -" + (long)tienGiam + " VNĐ");
        System.out.println("Tổng tiền:  " + (long)tongTien + " VNĐ");
    }
}