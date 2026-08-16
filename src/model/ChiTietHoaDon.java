package model;

public class ChiTietHoaDon {
    private Mon mon;
    private int soLuong;
    private String size;
    private double tienTopping;

    public ChiTietHoaDon() {}

    public ChiTietHoaDon(Mon mon, int soLuong, String size, double tienTopping) {
        this.mon = mon;
        this.soLuong = soLuong;
        this.size = size;
        this.tienTopping = tienTopping;
    }

    public Mon getMon() {
        return mon;
    }

    public void setMon(Mon mon) {
        this.mon = mon;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getTienTopping() {
        return tienTopping;
    }

    public void setTienTopping(double tienTopping) {
        this.tienTopping = tienTopping;
    }

    public ChiTietHoaDon(int soLuong, String size, double tienTopping) {
        this.soLuong = soLuong;
        this.size = size;
        this.tienTopping = tienTopping;
    }


    public double getPhuThuSize() {
        return "L".equals(size) ? 5000 :0;
    }

    public double tinhThanhTien(){
        return soLuong * (mon.getDonGia() + getPhuThuSize() + tienTopping);
    }

    @Override
    public String toString() {
        if (mon == null) return "Chưa chọn món";
        return mon.getTenMon() + " | Size: " + size +
                " | SL: " + soLuong +
                " | Phụ thu topping: " + (long)tienTopping + " VNĐ" +
                " | Thành tiền: " + (long)tinhThanhTien() + " VNĐ";
    }
}
