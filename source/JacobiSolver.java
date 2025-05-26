public class JacobiSolver {
    private SparseMatrix A; // Ma trận thưa
    private double[] b; // Vector hằng số
    private int maxIterations; // Số lần lặp tối đa
    private double epsilon; // Sai số cho phép
    private HeatGrid grid; // Lưới để tính vector b từ điều kiện biên

    // Constructor: Khởi tạo solver với ma trận, lưới, số lần lặp tối đa và sai số
    public JacobiSolver(SparseMatrix A, HeatGrid grid, int maxIterations, double epsilon) {
        if (A == null || grid == null) {
            throw new IllegalArgumentException("SparseMatrix and HeatGrid cannot be null");
        }
        if (maxIterations <= 0) {
            throw new IllegalArgumentException("Max iterations must be positive");
        }
        if (epsilon <= 0) {
            throw new IllegalArgumentException("Epsilon must be positive");
        }
        this.A = A;
        this.grid = grid;
        this.maxIterations = maxIterations;
        this.epsilon = epsilon;
        this.b = new double[A.getRows()];
        initializeVectorB();
    }

    // Khởi tạo vector b từ điều kiện biên của lưới
    private void initializeVectorB() {
        int n = grid.getSize();
        for (int row = 0; row < A.getRows(); row++) {
            int i = row / (n - 2) + 1;
            int j = row % (n - 2) + 1;
            b[row] = 0.0;
            // Nếu hàng xóm là điểm biên, thêm giá trị biên vào b
            if (i == 1) { // Hàng xóm trên là biên
                b[row] += grid.getValue(i - 1, j);
            }
            if (i == n - 2) { // Hàng xóm dưới là biên
                b[row] += grid.getValue(i + 1, j);
            }
            if (j == 1) { // Hàng xóm trái là biên
                b[row] += grid.getValue(i, j - 1);
            }
            if (j == n - 2) { // Hàng xóm phải là biên
                b[row] += grid.getValue(i, j + 1);
            }
        }
    }

    // Giải hệ phương trình Ax = b bằng phương pháp Jacobi
    public double[] solve() {
        int n = A.getRows();
        double[] x = new double[n]; // Nghiệm khởi đầu (toàn 0)
        double[] xNew = new double[n]; // Nghiệm mới mỗi lần lặp
        
        for (int iter = 0; iter < maxIterations; iter++) {
            // Tính xNew cho mỗi hàng
            for (int i = 0; i < n; i++) {
                double sum = 0.0;
                double diag = 0.0; // Giá trị đường chéo
                for (int j = A.getRowPtr()[i]; j < A.getRowPtr()[i + 1]; j++) {
                    int col = A.getColIndices()[j];
                    double val = A.getValues()[j];
                    if (col == i) {
                        diag = val; // Lưu giá trị đường chéo
                    } else {
                        sum += val * x[col]; // Tính tổng các phần tử không phải đường chéo
                    }
                }
                if (Math.abs(diag) < 1e-10) {
                    throw new ArithmeticException("Zero or near-zero diagonal element at row " + i);
                }
                xNew[i] = (b[i] - sum) / diag; // Công thức Jacobi
            }
            
            // Kiểm tra điều kiện hội tụ
            double maxDiff = 0.0;
            for (int i = 0; i < n; i++) {
                maxDiff = Math.max(maxDiff, Math.abs(xNew[i] - x[i]));
                x[i] = xNew[i]; // Cập nhật x cho lần lặp tiếp theo
            }
            if (maxDiff < epsilon) {
                System.out.println("Converged after " + (iter + 1) + " iterations");
                return x;
            }
        }
        
        System.out.println("Did not converge within " + maxIterations + " iterations");
        return x;
    }

    // Lấy vector b (dùng để kiểm tra)
    public double[] getVectorB() {
        return b;
    }

    // In vector b để kiểm tra
    public void printVectorB() {
        System.out.println("Vector b:");
        System.out.print("[");
        for (int i = 0; i < b.length; i++) {
            System.out.printf("%.2f", b[i]);
            if (i < b.length - 1) System.out.print(", ");
        }
        System.out.println("]");
    }

    // Hàm main để kiểm tra độc lập
    public static void main(String[] args) {
        try {
            // Tạo HeatGrid mẫu (4x4)
            int n = 4;
            double top = 100.0, bottom = 0.0, left = 50.0, right = 50.0;
            System.out.println("Creating HeatGrid with n = " + n + ", top = " + top + ", bottom = " + bottom + ", left = " + left + ", right = " + right);
            HeatGrid grid = new HeatGrid(n, top, bottom, left, right);
            
            // Tạo SparseMatrix
            System.out.println("\nCreating SparseMatrix from HeatGrid");
            SparseMatrix matrix = new SparseMatrix(grid);
            
            // Tạo JacobiSolver
            int maxIterations = 100;
            double epsilon = 1e-6;
            System.out.println("\nCreating JacobiSolver with maxIterations = " + maxIterations + ", epsilon = " + epsilon);
            JacobiSolver solver = new JacobiSolver(matrix, grid, maxIterations, epsilon);
            
            // In vector b
            System.out.println("\nVector b content:");
            solver.printVectorB();
            
            // Giải hệ
            System.out.println("\nSolving system Ax = b");
            double[] solution = solver.solve();
            
            // In nghiệm
            System.out.println("\nSolution x:");
            System.out.print("[");
            for (int i = 0; i < solution.length; i++) {
                System.out.printf("%.2f", solution[i]);
                if (i < solution.length - 1) System.out.print(", ");
            }
            System.out.println("]");
            
        } catch (IllegalArgumentException | ArithmeticException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}