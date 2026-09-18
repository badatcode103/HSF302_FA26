Thực hành 2:
CRUD JPA Mapping
Created by traltb@fe.edu.vn
Thực thi quan hệ OneToMany dùng JPA
1. Đề bài
Một công ty có nhiều Department (phòng ban), mỗi Department có nhiều Employee, mỗi Employee chỉ thuộc đúng 1 Department — quan hệ OneToMany / ManyToOne.
2. Yêu cầu thiết kế
Entity	Field	Ghi chú
Department	id	PK, tự sinh
	name	unique
	location	—
Employee	id	PK, tự sinh
	fullName	—
	salary	kiểu BigDecimal
	hireDate	dùng @Temporal hoặc LocalDate
	email	Unique
	gender	Kiểu enum MALE, FEMALE, OTHER dùng @Enumerated(EnumType.STRING)
	active	Boolean đánh dấu nhân viên đang làm việc (`true`) hay đã nghỉ (`false`) 
•	Quan hệ 1–N, owning side là Employee (phía giữ khóa ngoại department_id).
TODO 1: Cấu hình dự án:
File pom.xml:
<dependencies>
<!-- Source: https://mvnrepository.com/artifact/org.hibernate.orm/hibernate-core -->
<!-- Hibernate Core dependency for JPA implementation -->
<dependency>
    <groupId>org.hibernate.orm</groupId>
    <artifactId>hibernate-core</artifactId>
    <version>6.5.2.Final</version>
</dependency>
<!-- MSSQL JDBC Driver -->
<!-- Source: https://mvnrepository.com/artifact/com.microsoft.sqlserver/mssql-jdbc -->
<dependency>
    <groupId>com.microsoft.sqlserver</groupId>
    <artifactId>mssql-jdbc</artifactId>
    <version>13.2.1.jre11</version>
    <scope>compile</scope>
</dependency>
<!-- Jakarta Persistence API -->
<!-- Source: https://mvnrepository.com/artifact/jakarta.persistence/jakarta.persistence-api -->
<dependency>
    <groupId>jakarta.persistence</groupId>
    <artifactId>jakarta.persistence-api</artifactId>
    <version>3.1.0</version>
</dependency>
</dependencies>
•	Cấu hình cấu trúc thư mục:
Tạo các thư mục pojo (chứa Department.java, Employee.java, Gender.java)
Thư mục dao DepartmentDAO.java, EmployeeDAO.java
Thư mục util
Thư mục META-INF, chứa file persistence.xml
 
•	Cấu hình file persistence.xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence xmlns="https://jakarta.ee/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="https://jakarta.ee/xml/ns/persistence
                                 https://jakarta.ee/xml/ns/persistence/persistence_3_0.xsd"
             version="3.0">
    <persistence-unit name="hsf302FU" transaction-type="RESOURCE_LOCAL">
        <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>

        <class>fu.se123456.pojo.Department</class>
        <class>fu.se123456.pojo.Employee</class>

        <properties>
            <property name="jakarta.persistence.jdbc.driver"
                      value="com.microsoft.sqlserver.jdbc.SQLServerDriver"/>
            <property name="jakarta.persistence.jdbc.url"
                      value="jdbc:sqlserver://localhost:1433;databaseName=HSF302_Chapter1;encrypt=false;
trustServerCertificate=true"/>
            <property name="jakarta.persistence.jdbc.user" value="sa"/>
            <property name="jakarta.persistence.jdbc.password" value="sa"/>

            <property name="hibernate.dialect"
                      value="org.hibernate.dialect.SQLServerDialect"/>
            <property name="hibernate.hbm2ddl.auto" value="update"/>

            <!-- Bật để đếm số câu SQL khi làm TODO 2.8 / 2.9 -->
            <property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.format_sql" value="true"/>
        </properties>
    </persistence-unit>
</persistence>

Tạo database HSF302_Chapter1 trong SQL Server.
Checklist tự kiểm tra:
•	File nằm đúng src/main/resources/META-INF/persistence.xml (đúng như cây thư mục trong ảnh).
•	persistence-unit name khớp với chuỗi bạn sẽ truyền vào Persistence.createEntityManagerFactory(...) khi viết JPAUtil.java trong package util.
•	Cả 2 dòng <class> viết đúng full package fu.se123456.pojo.Department / fu.se123456.pojo.Employee — không phải chỉ Department/Employee.
•	Đã đổi user/password khớp với SQL Server thật trên máy bạn (không để nguyên yourPassword).
•	Database HSF302_Chapter1 (hoặc tên bạn muốn) đã tồn tại (rỗng) trong SQL Server trước khi chạy Main.
3. Danh sách yêu cầu (TODO) + Checklist tự kiểm tra
1.	TODO 2.1 — Entity Department, Employee, Gender
•	Yêu cầu: Tạo 2 entity với các field ở bảng thiết kế trên.
Tự kiểm tra:
[ ] Đã tạo enum Gender { MALE, FEMALE, OTHER } là 1 file/type riêng trong package fe.masv.pojo (không khai báo lồng trong class Employee). 
[ ] Department có @Entity, @Table(name = "departments"), id dùng @Id + @GeneratedValue.
[ ] name có @Column(unique = true). 
[ ] Employee có @Entity, @Table(name = "employees").
[ ] email có @Column(unique = true).
[ ] gender khai báo kiểu Gender và có @Enumerated(EnumType.STRING) — không để mặc định EnumType.ORDINAL (lưu số thứ tự 0/1/2 sẽ vỡ dữ liệu nếu sau này thêm/đổi thứ tự hằng số enum).
[ ] active khai báo kiểu boolean nguyên thủy (không phải Boolean object, trừ khi cố ý cho phép null).
[ ] salary khai báo kiểu java.math.BigDecimal (không dùng double/float cho dữ liệu tiền tệ).
[ ] hireDate dùng LocalDate (khuyến khích, JPA 2.2+ map thẳng) hoặc java.util.Date + @Temporal(TemporalType.DATE).
[ ] Cả 2 class có constructor không tham số + getter/sette 
TODO 2.2 — Owning side trong Employee
•	Yêu cầu: Khai báo phía sở hữu quan hệ (phía "Many"):
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "department_id", nullable = false)
private Department department;
•	Tự kiểm tra:
•	[ ] Field tên department, kiểu Department, nằm trong class Employee.
•	[ ] Có @JoinColumn(name = "department_id", nullable = false) — đây là dấu hiệu owning side (phía giữ FK).
•	[ ] fetch = FetchType.LAZY được khai báo tường minh (không để mặc định EAGER của @ManyToOne).
•	[ ] Sau khi chạy app lần đầu, kiểm tra bảng employees trong DB có cột department_id kiểu tương ứng với PK của departments.
TODO 2.3 — Inverse side trong Department
•	Yêu cầu: Khai báo phía "One":
@OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Employee> employees = new ArrayList<>();
•	Tự kiểm tra:
•	[ ] mappedBy = "department" khớp chính xác tên field department đã khai báo ở Employee (TODO 2.2).
•	[ ] Field employees được khởi tạo sẵn = new ArrayList<>() (tránh NullPointerException khi gọi .add() lần đầu).
•	[ ] Có cascade = CascadeType.ALL và orphanRemoval = true.
•	[ ] Không có @JoinColumn ở phía này.
TODO 2.4 — Helper method đồng bộ 2 chiều
•	Yêu cầu: Viết addEmployee(Employee e) / removeEmployee(Employee e) trong Department.
•	Tự kiểm tra:
[ ] addEmployee() vừa this.employees.add(e) vừa e.setDepartment(this).
[ ] removeEmployee() vừa this.employees.remove(e) vừa e.setDepartment(null).
[ ] Viết thử: gọi dept.addEmployee(emp) rồi kiểm tra cả dept.getEmployees().contains(emp) và emp.getDepartment() == dept đều đúng.
Kiểm chứng nhanh: (viết tạm trong main để test)
•	Department dept = new Department("IT", "Ha Noi");
Employee emp = new Employee("test@company.com", "Test", Gender.OTHER,
        new BigDecimal("1000"), LocalDate.now());
dept.addEmployee(emp);
System.out.println(dept.getEmployees().contains(emp)); // phải true
System.out.println(emp.getDepartment() == dept); // phải true
Chạy hàm main, kết quả ra 2 dòng true ở output.
Viết JPAUtil (EntityManagerFactory dùng chung)
Tạo file fe/masv/util/JPAUtil.java:
 
Checklist:
•	Tên "hsf302FU" khớp đúng persistence-unit name 
•	EMF khai báo static final — chỉ tạo 1 lần cho cả ứng dụng (EMF nặng, EntityManager nhẹ và tạo mới mỗi thao tác trong DAO).
2.	TODO 2.5 — DAO cho Department/Employee
•	Yêu cầu: Viết DAO đầy đủ save, findAll, findById, update, delete 
Tự kiểm tra:
•	[ ] Mỗi method tự mở/đóng EntityManager riêng (không dùng chung 1 EntityManager cho nhiều thao tác).
•	[ ] Có try/catch/finally với rollback() khi lỗi và close() trong finally.
•	[ ] update() dùng em.merge() và gán lại kết quả (entity = em.merge(entity)), không giả định merge() sửa trực tiếp object truyền vào.

3.	TODO 2.6 — JPQL JOIN FETCH
•	Yêu cầu: Viết query lấy 1 Department kèm danh sách Employee trong 1 lần:
em.createQuery("SELECT d FROM Department d JOIN FETCH d.employees WHERE d.id = :id", Department.class)
•	Tự kiểm tra:
•	[ ] Query dùng đúng cú pháp JOIN FETCH d.employees (không phải JOIN d.employees — thiếu FETCH sẽ không load kèm dữ liệu).
•	[ ] Đặt tham số bằng .setParameter("id", id) trước khi .getSingleResult().
•	[ ] Sau khi gọi query này, truy cập department.getEmployees() không ném exception dù đã đóng EntityManager sau đó trong cùng lần load ban đầu.

4.	TODO 2.7 — Demo trong Main
•	Yêu cầu: Tạo 1 Department, thêm 3 Employee (qua addEmployee), lưu xuống DB.
•	Tự kiểm tra:
•	[ ] Gọi addEmployee() cho cả 3 Employee trước khi persist Department.
•	[ ] Chỉ cần persist(department) — không persist riêng từng Employee (nhờ cascade = ALL).
•	[ ] Sau khi chạy, DB có đúng 1 dòng departments và 3 dòng employees cùng trỏ về department_id đó.
•	SELECT * FROM employees; có đúng 3 dòng, cả 3 đều có department_id trỏ đúng về dòng departments vừa tạo, cột gender lưu chuỗi MALE/FEMALE/OTHER, cột active là 1/true. 
•	Mở Foreign Keys của bảng employees (SSMS: Object Explorer → employees → Keys), thấy FK trỏ đúng về departments(id). 
•	Thử save() thêm 1 Employee dùng lại email đã tồn tại → phải ném exception vi phạm unique.
DepartmentDAO departmentDAO = new DepartmentDAO();

// 1) Tạo Department + 3 Employee, add qua helper method (TODO 2.4)
Department it = new Department("Marketing", "Ha Noi");

Employee e1 = new Employee("aa.nguyen@company.com", "Nguyen Van A", Gender.MALE,
        new BigDecimal("15000000"), LocalDate.of(2022, 1, 10));
Employee e2 = new Employee("bb.tran@company.com", "Tran Thi B", Gender.FEMALE,
        new BigDecimal("18000000"), LocalDate.of(2021, 6, 1));
Employee e3 = new Employee("cc.le@company.com", "Le Van C", Gender.OTHER,
        new BigDecimal("12000000"), LocalDate.of(2023, 3, 15));

it.addEmployee(e1);
it.addEmployee(e2);
it.addEmployee(e3);

// 2) Chỉ persist(department) — cascade = ALL tự lo phần Employee (TODO 2.7)
departmentDAO.save(it);
System.out.println("Da luu Department, id = " + it.getId());

// 3) Tim lai kem employees bang JOIN FETCH (TODO 2.6) — khong bi
//    LazyInitializationException du EntityManager cua lan tim nay da dong,
//    vi employees da duoc load ngay trong cung 1 query.
Department found = departmentDAO.findByIdWithEmployees(it.getId());
System.out.println("Phong ban: " + found.getName());
for (Employee e : found.getEmployees()) {
    System.out.println("  - " + e);
}

JPAUtil.close();

5.	TODO 2.8 — Tái hiện N+1 Query Problem
•	Yêu cầu: Gọi findAll() các Department (không JOIN FETCH), sau đó loop qua department.getEmployees() của từng phần tử; bật hibernate.show_sql=true để đếm số câu SQL sinh ra.
•	Tự kiểm tra:
•	[ ] hibernate.show_sql=true đã bật trong persistence.xml.
•	[ ] Đã đếm và ghi lại: 1 câu SELECT cho findAll() + N câu SELECT riêng (mỗi lần truy cập .getEmployees() của 1 department) — tổng 1 + N câu.
•	[ ] Hiểu đúng nguyên nhân: employees là quan hệ LAZY (mặc định của @OneToMany), nên mỗi lần truy cập lần đầu ở 1 department khác nhau sẽ kích hoạt 1 query riêng.

6.	TODO 2.9 — Fix N+1 bằng JOIN FETCH
•	Yêu cầu: Sửa đoạn TODO 2.8 bằng cách dùng query có JOIN FETCH (TODO 2.6, viết thêm bản findAllWithEmployees() không lọc theo id), so sánh số câu SQL trước/sau.
•	Tự kiểm tra:
•	[ ] Sau khi fix, chỉ còn đúng 1 câu SQL (có JOIN) cho toàn bộ thao tác load Department + Employees.
•	[ ] Đã ghi lại so sánh rõ ràng (comment hoặc log): số câu SQL trước fix (1 + N) vs sau fix (1).

4. Checklist hoàn thành tổng thể (chấm cuối bài)
•	[ ] Bảng employees có cột FK department_id trỏ đúng về departments.
•	[ ] Thêm nhân viên vào phòng ban qua addEmployee() thì cả employee.department và department.employees đều đồng bộ (kiểm tra bằng assert/System.out).
•	[ ] Xóa 1 Department thì toàn bộ Employee thuộc phòng đó cũng bị xóa theo (cascade + orphanRemoval).
•	[ ] Đã đo và ghi lại (comment hoặc log) số câu SQL sinh ra ở TODO 2.8 (N+1, ví dụ 1 + N câu) so với TODO 2.9 (chỉ 1 câu JOIN FETCH).
•	[ ] Không còn truy cập employees list bên ngoài transaction/session gây LazyInitializationException.


