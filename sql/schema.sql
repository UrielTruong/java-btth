CREATE DATABASE IF NOT EXISTS quanlytrasua;
USE quanlytrasua;

CREATE TABLE IF NOT EXISTS mon (
  ma_mon VARCHAR(20) PRIMARY KEY,
  ten_mon VARCHAR(100) NOT NULL,
  loai VARCHAR(50),
  don_gia DOUBLE NOT NULL
);

CREATE TABLE IF NOT EXISTS ban (
  ten_ban VARCHAR(20) PRIMARY KEY,
  trang_thai VARCHAR(30) NOT NULL DEFAULT 'Trống'
);

CREATE TABLE IF NOT EXISTS hoa_don (
  ma_hoa_don VARCHAR(20) PRIMARY KEY,
  ngay_lap VARCHAR(20),
  ten_khach_hang VARCHAR(100),
  so_dt VARCHAR(20),
  so_ban VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS chi_tiet_hoa_don (
  id INT AUTO_INCREMENT PRIMARY KEY,
  ma_hoa_don VARCHAR(20) NOT NULL,
  ma_mon VARCHAR(20) NOT NULL,
  so_luong INT NOT NULL,
  size VARCHAR(5),
  tien_topping DOUBLE DEFAULT 0,
  CONSTRAINT fk_ct_hoadon FOREIGN KEY (ma_hoa_don) REFERENCES hoa_don(ma_hoa_don),
  CONSTRAINT fk_ct_mon FOREIGN KEY (ma_mon) REFERENCES mon(ma_mon) ON UPDATE CASCADE
);

INSERT INTO mon (ma_mon, ten_mon, loai, don_gia) VALUES
  ('TS01', 'Trà sữa Matcha', 'Trà sữa', 35000),
  ('TS02', 'Trà sữa Trân châu', 'Trà sữa', 30000),
  ('TC01', 'Trà đào cam sả', 'Trà trái cây', 35000)
ON DUPLICATE KEY UPDATE ten_mon = VALUES(ten_mon);

INSERT INTO ban (ten_ban, trang_thai) VALUES
  ('Bàn 1', 'Trống'),
  ('Bàn 2', 'Trống'),
  ('Bàn 3', 'Trống'),
  ('Bàn 4', 'Trống'),
  ('Bàn 5', 'Trống')
ON DUPLICATE KEY UPDATE trang_thai = trang_thai;
