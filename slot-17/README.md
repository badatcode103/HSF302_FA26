# Student Management System

Ứng dụng JavaFX, Spring Boot và Spring Data JPA quản lý sinh viên.

## Yêu cầu

- JDK 21, Maven 3.9 trở lên và SQL Server có bật TCP/IP cổng 1433.
- Tạo cơ sở dữ liệu: `CREATE DATABASE StudentDB;`.
- Thiết lập các biến môi trường `DB_URL` (nếu cần), `DB_USERNAME` và `DB_PASSWORD` phù hợp. Giá trị mặc định URL kết nối `localhost:1433`, database `StudentDB`; tài khoản mặc định `sa`, mật khẩu mặc định rỗng. Có thể sửa `ui-module/src/main/resources/application.properties`.

## Chạy

Tại thư mục `slot-17`:

```sh
mvn clean install
mvn -f ui-module/pom.xml spring-boot:run
```

`mvn clean install` cũng chạy toàn bộ kiểm thử H2 trong `data-module`. Có thể chạy riêng với `mvn -pl data-module test`. Khởi chạy lớp `com.example.ui.Launcher` trong IDE sau khi build.

## Kiến trúc

`JavaFX/FXML → StudentController → StudentService → StudentServiceImpl → StudentRepository → SQL Server`.
Kiểm thử sử dụng H2 in-memory. Hibernate tự tạo/cập nhật bảng `students`. Truy vấn tìm theo tên hoặc email; kiểm tra trùng email không phân biệt chữ hoa/thường.

## Kiểm thử thủ công

Mở ứng dụng với SQL Server đang chạy, thêm sinh viên, tìm kiếm, chọn và cập nhật, kiểm tra lỗi dữ liệu và xác nhận xóa. Trường tên hỗ trợ Unicode.
