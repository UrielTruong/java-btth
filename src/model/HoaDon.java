package model;

import java.util.ArrayList;
import java.util.List;

public class HoaDon {
    private String maHoaDon;
    private String ngayLap;
    private String tenKhachHang;
    private  String soDT;
    private  String soBan;
    private List<ChiTietHoaDon> chiTiet;


    public HoaDon() {
        this.chiTiet = new ArrayList<>();
    }
    public HoaDon(String tenKhachHang, String soDT, String soBan) {
        this.tenKhachHang = tenKhachHang;
        this.soDT = soDT;
        this.soBan = soBan;
        this.chiTiet = new ArrayList<>();
    }

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public String getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(String ngayLap) {
        this.ngayLap = ngayLap;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public String getSoDT() {
        return soDT;
    }

    public void setSoDT(String soDT) {
        this.soDT = soDT;
    }

    public String getSoBan() {
        return soBan;
    }

    public void setSoBan(String soBan) {
        this.soBan = soBan;
    }

    public List<ChiTietHoaDon> getChiTiet() {
        return chiTiet;
    }

    public void setChiTiet(List<ChiTietHoaDon> chiTiet) {
        this.chiTiet = chiTiet;
    }

    public void themChiTiet(ChiTietHoaDon ct){
        this.chiTiet.add(ct);
    }

    public void xoaChiTiet(ChiTietHoaDon ct){
        this.chiTiet.remove(ct);
    }

    public double tinhTamTinh(){
        double s = 0;
        for(ChiTietHoaDon ct: chiTiet){
            s += ct.tinhThanhTien();
        }
        return s;
    }

    // Tính tiền giảm giá dựa theo các mốc tạm tính
    public double tinhGiamGia(double tamTinh) {
        if (tamTinh >= 500000) {
            return tamTinh * 0.15; // Từ 500.000 trở lên: giảm 15%
        } else if (tamTinh >= 200000) {
            return tamTinh * 0.10; // Từ 200.000 đến dưới 500.000: giảm 10%
        } else if (tamTinh >= 100000) {
            return tamTinh * 0.05; // Từ 100.000 đến dưới 200.000: giảm 5%
        }
        return 0; // Dưới 100.000: không giảm (0%)
    }

    // Tính tổng tiền sau khi trừ tiền giảm giá
    public double tinhTongTien() {
        double tamTinh = tinhTamTinh();
        return tamTinh - tinhGiamGia(tamTinh);
    }

}
