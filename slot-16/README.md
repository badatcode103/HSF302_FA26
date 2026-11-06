# JavaFX Chapter 8 — B1 đến B6 và T1 đến T6

Project Maven dùng JDK 21 và JavaFX 21. Mỗi bài là một ứng dụng riêng. Mở thư mục này bằng IntelliJ IDEA (Open → chọn thư mục chứa `pom.xml`) và chọn JDK 21.

## Chạy từng bài

Mở terminal tại thư mục project và chạy một trong các lệnh:

```powershell
mvn clean compile
mvn -Pb1 javafx:run
mvn -Pb2 javafx:run
mvn -Pb3 javafx:run
mvn -Pb4 javafx:run
mvn -Pb5 javafx:run
mvn -Pb6 javafx:run
mvn -Pt1 javafx:run
mvn -Pt2 javafx:run
mvn -Pt3 javafx:run
mvn -Pt4 javafx:run
mvn -Pt5 javafx:run
mvn -Pt6 javafx:run
```

Cũng có thể chạy class `DrinkOrderApp`, `LoginLayoutApp`, `RegisterApp` hoặc `LoginApp` trong IntelliJ sau khi Maven tải dependency. Nếu chạy lệnh `mvn javafx:run` mà không chọn profile, mặc định là B1.

## Nội dung và tài khoản mẫu

- **B1:** Đặt đồ uống, tính tiền và nhật ký sự kiện. Ví dụ trà sữa, size L, hai topping, 5 ly = 225.000 đ sau giảm giá.
- **B2:** Form đăng nhập viết bằng Java; tài khoản `admin` / `Admin@123` hoặc `student` / `Student@123`.
- **B3:** Đăng ký và đăng nhập bằng giao diện Java; tài khoản mẫu `admin` / `Admin@123`. Tài khoản mới chỉ lưu trong bộ nhớ và mất khi ứng dụng đóng. Đây là ví dụ học tập, không dùng cơ chế lưu mật khẩu này trong ứng dụng thật.
- **B4:** Đăng nhập bằng FXML và CSS. Chọn vai trò tương ứng: `student` / `Student@123` / Sinh viên hoặc `teacher` / `Teacher@123` / Giảng viên. Chức năng quên mật khẩu chỉ minh họa, không gửi email.

- **B5:** Thư viện minh họa các layout và cách chúng thay đổi khi resize.
- **B6:** Phiếu khảo sát, kiểm tra dữ liệu và tổng hợp câu trả lời.
- **T1:** Quản lý sinh viên với cây khoa/lớp, bảng và import/export CSV.
- **T2:** Vẽ, chọn và chỉnh sửa các hình 2D.
- **T3:** Minh họa tác vụ nền, tiến độ và hủy tác vụ.
- **T4:** Bảng điểm và biểu đồ cập nhật theo dữ liệu.
- **T5:** Trình duyệt WebView và trình phát media; cần mạng hoặc file media tương ứng.
- **T6:** Xem và điều khiển hình 3D; cần phần cứng hỗ trợ JavaFX 3D.

## Tìm hiểu code

- `src/main/java/com/fu/ch08/b1` đến `b4`: code của từng bài.
- `src/main/java/com/fu/ch08/common/AlertUtil.java`: hộp thoại dùng chung.
- `src/main/resources/com/fu/ch08/b4`: hai file FXML và file CSS; có thể mở FXML bằng Scene Builder.
- `src/main/java/com/fu/ch08/b5` đến `b6` và `t1` đến `t6`: các bài còn lại, mỗi bài có profile Maven tương ứng.
- `module-info.java`: khai báo module JavaFX và cho FXMLLoader truy cập controller B4.

Trong IntelliJ, có thể cấu hình đường dẫn Scene Builder tại Settings → Languages & Frameworks → JavaFX, chọn `D:\Apps\SceneBuilder\SceneBuilder.exe`. Khi chỉnh FXML bằng Scene Builder 23, giữ namespace JavaFX 21 và tránh control hoặc thuộc tính chỉ có ở JavaFX mới hơn.
