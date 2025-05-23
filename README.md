**INT3102_3**
Dự án này triển khai các phương pháp số để giải quyết cả bài toán lý thuyết và thực tế. Chương trình chính (MainProgram.java) cung cấp giao diện dòng lệnh cho phép người dùng chọn giữa các loại bài toán và nhập dữ liệu để giải.  
Có 2 loại bài toán:  
![image](https://github.com/user-attachments/assets/6b59aeb8-1c37-4f25-a2ab-22a9344d9582)   


*Phần 1: Bài Toán Lý Thuyết*
Phần này bao gồm 4 loại bài toán lý thuyết được hỗ trợ trong dự án:    
![image](https://github.com/user-attachments/assets/148461e2-f40b-4421-97d5-730717faca69)     


1. Giải Phương Trình (Equation Solving)

Mục đích: Tìm nghiệm (root) của một đa thức bằng phương pháp Chia Đôi (Bisection) hoặc Newton-Raphson.
Đầu vào: 
Bậc của đa thức (degree).
Lựa chọn phương pháp: khoảng [a, b] cho Bisection hoặc giá trị khởi đầu x0 cho Newton-Raphson.
Sai số epsilon.


Đầu ra: Nghiệm của đa thức và giá trị hàm tại nghiệm đó.
Ví dụ:
Đầu vào: Bậc = 2, Khoảng [a=0, b=1], Epsilon=0.001
Đầu ra: Nghiệm x = 0.500000 với f(x) = 0.000000



2. Hệ Phương Trình Tuyến Tính (System of Equations)

Mục đích: Giải hệ phương trình tuyến tính bằng phương pháp Gaussian Elimination (cho hệ nhỏ) hoặc Gauss-Seidel (cho hệ lớn).
Đầu vào: 
Số phương trình (n).
Số biến (degree).


Đầu ra: Vector nghiệm của hệ.
Ví dụ:
Đầu vào: Số phương trình = 2, Số biến = 2
Đầu ra: x1 = 1.000000, x2 = 2.000000



3. Tọa Độ (Coordinates)

Mục đích: Thực hiện nội suy Lagrange trên tập hợp các điểm tọa độ.
Đầu vào: 
Số lượng tọa độ (n).


Đầu ra: Hệ số của đa thức nội suy.
Ví dụ:
Đầu vào: Số tọa độ = 3
Đầu ra: a0 = 1.000000, a1 = 2.000000, a2 = 3.000000



4. Tích Phân (Integration)

Mục đích: Tính tích phân xác định của một hàm bằng phương pháp Romberg.
Đầu vào: 
Giới hạn dưới (a).
Giới hạn trên (b).
Số lần lặp tối đa (max iterations).


Đầu ra: Giá trị tích phân.
Ví dụ:
Đầu vào: a = 0, b = 1, maxIterations = 5
Đầu ra: Tích phân từ 0.00 đến 1.00 = 2.333333



*Phần 2: Bài Toán Thực Tế - Mô Phỏng Phân Phối Nhiệt*  

Chủ đề: Mô phỏng sự phân phối nhiệt trên lưới 2D.
Mục tiêu: Xác định cách nhiệt lan truyền trên một lưới vuông với các điều kiện biên cố định.
Lý thuyết:
Sử dụng phương pháp sai phân hữu hạn (finite difference method) để rời rạc hóa phương trình nhiệt 2D.
Giải hệ phương trình tuyến tính thu được bằng phương pháp lặp Jacobi.


Đầu vào: 
Kích thước lưới (n x n).
Nhiệt độ biên: trên (top), dưới (bottom), trái (left), phải (right).
Số lần lặp tối đa (max iterations).
Sai số hội tụ (epsilon).


Cách tính toán cho đầu ra: 
Khởi tạo lưới với nhiệt độ ban đầu (thường là 0 bên trong, biên theo giá trị nhập).
Lặp qua các điểm trong lưới, cập nhật nhiệt độ dựa trên trung bình của 4 điểm lân cận.
Dừng khi sự thay đổi giữa các lần lặp nhỏ hơn epsilon hoặc đạt max iterations.
Kết quả là lưới nhiệt độ thể hiện trạng thái ổn định.


Ví dụ:
Đầu vào: n = 100, top = 100.0, bottom = 0.0, left = 50.0, right = 50.0, maxIterations = 1000, epsilon = 1e-6
Đầu ra: Một lưới 100x100 với các giá trị nhiệt độ từ 0 đến 100, thể hiện sự khuếch tán nhiệt từ các biên.    
![image](https://github.com/user-attachments/assets/1ae5351e-1a7a-4912-8f3b-bef6666cbd95)


