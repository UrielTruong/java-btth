-- Sửa lỗi: "Cannot delete or update a parent row: a foreign key constraint fails" khi sửa Mã món.
-- Nguyên nhân: khóa ngoại chi_tiet_hoa_don.ma_mon -> mon.ma_mon chưa có ON UPDATE CASCADE,
-- nên khi đổi mã món đã có trong hóa đơn cũ, MySQL từ chối UPDATE để bảo toàn dữ liệu.
-- Chạy script này 1 lần trên CSDL đã tồn tại (quanlytrasua) để thêm ON UPDATE CASCADE.

USE quanlytrasua;

SET @fk_name := (
    SELECT CONSTRAINT_NAME
    FROM information_schema.KEY_COLUMN_USAGE
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'chi_tiet_hoa_don'
      AND COLUMN_NAME = 'ma_mon'
      AND REFERENCED_TABLE_NAME = 'mon'
    LIMIT 1
);

SET @sql := CONCAT('ALTER TABLE chi_tiet_hoa_don DROP FOREIGN KEY ', @fk_name);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

ALTER TABLE chi_tiet_hoa_don
  ADD CONSTRAINT fk_ct_mon FOREIGN KEY (ma_mon) REFERENCES mon(ma_mon) ON UPDATE CASCADE;
