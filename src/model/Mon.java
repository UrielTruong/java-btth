package model;

public class Mon {
    private String maMon, tenMon, loai;
    private double donGia;

    public Mon() {}

    public Mon(String maMon, String tenMon, String loai, double donGia) {
        this.maMon = maMon;
        this.tenMon = tenMon;
        this.loai = loai;
        this.donGia = donGia;
    }

    public String getMaMon() {
        return maMon;
    }

    public void setMaMon(String maMon) {
        this.maMon = maMon;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    @Override
    public String toString() { return tenMon + ", " + loai + ", " + donGia; }
}
