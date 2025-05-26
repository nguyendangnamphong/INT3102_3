public class HeatSimulation {
    private HeatGrid grid; // Lưới lưu nhiệt độ
    private SparseMatrix matrix; // Ma trận thưa
    private JacobiSolver solver; // Bộ giải Jacobi
    private int n; // Kích thước lưới
    private double topBoundary; // Nhiệt độ biên trên
    private double bottomBoundary; // Nhiệt độ biên dưới
    private double leftBoundary; // Nhiệt độ biên trái
    private double rightBoundary; // Nhiệt độ biên phải
    private int maxIterations; // Số lần lặp tối đa
    private double epsilon; // Sai số cho phép

    // Constructor: Khởi tạo mô phỏng với các tham số
    public HeatSimulation(int n, double topBoundary, double bottomBoundary, double leftBoundary, double rightBoundary,
                          int maxIterations, double epsilon) {
        if (n < 2) {
            throw new IllegalArgumentException("Grid size n must be at least 2");
        }
        if (maxIterations <= 0) {
            throw new IllegalArgumentException("Max iterations must be positive");
        }
        if (epsilon <= 0) {
            throw new IllegalArgumentException("Epsilon must be positive");
        }
        this.n = n;
        this.topBoundary = topBoundary;
        this.bottomBoundary = bottomBoundary;
        this.leftBoundary = leftBoundary;
        this.rightBoundary = rightBoundary;
        this.maxIterations = maxIterations;
        this.epsilon = epsilon;
    }

    // Chạy mô phỏng và cập nhật lưới
    public void runSimulation() {
        // Bước 1: Tạo lưới
        grid = new HeatGrid(n, topBoundary, bottomBoundary, leftBoundary, rightBoundary);
        
        // Bước 2: Tạo ma trận thưa
        matrix = new SparseMatrix(grid);
        
        // Bước 3: Tạo bộ giải Jacobi
        solver = new JacobiSolver(matrix, grid, maxIterations, epsilon);
        
        // Bước 4: Giải hệ để lấy nhiệt độ các điểm nội
        double[] solution = solver.solve();
        
        // Bước 5: Cập nhật lưới với nghiệm
        updateGridWithSolution(solution);
    }

    // Cập nhật lưới với nghiệm từ JacobiSolver
    private void updateGridWithSolution(double[] solution) {
        int index = 0;
        for (int i = 1; i < n - 1; i++) {
            for (int j = 1; j < n - 1; j++) {
                grid.setValue(i, j, solution[index]);
                index++;
            }
        }
    }

    // Lấy lưới kết quả
    public HeatGrid getResultGrid() {
        if (grid == null) {
            throw new IllegalStateException("Simulation has not been run yet");
        }
        return grid;
    }

    // In thông tin mô phỏng
    public void printSimulationInfo() {
        System.out.println("Heat Simulation Parameters:");
        System.out.println("Grid size: " + n + " x " + n);
        System.out.println("Boundary conditions: Top = " + topBoundary + ", Bottom = " + bottomBoundary + 
                          ", Left = " + leftBoundary + ", Right = " + rightBoundary);
        System.out.println("Max iterations: " + maxIterations);
        System.out.println("Epsilon: " + epsilon);
        if (grid != null) {
            System.out.println("\nResult grid:");
            grid.printGrid();
        } else {
            System.out.println("No result grid available yet");
        }
    }

    // Hàm main để kiểm tra độc lập
    public static void main(String[] args) {
        try {
            // Tạo mô phỏng mẫu
            int n = 4;
            double top = 100.0, bottom = 0.0, left = 50.0, right = 50.0;
            int maxIterations = 100;
            double epsilon = 1e-6;
            System.out.println("Creating HeatSimulation with n = " + n + ", top = " + top + ", bottom = " + bottom + 
                              ", left = " + left + ", right = " + right + ", maxIterations = " + maxIterations + 
                              ", epsilon = " + epsilon);
            HeatSimulation sim = new HeatSimulation(n, top, bottom, left, right, maxIterations, epsilon);
            
            // In thông tin ban đầu
            System.out.println("\nSimulation info before running:");
            sim.printSimulationInfo();
            
            // Chạy mô phỏng
            System.out.println("\nRunning simulation...");
            sim.runSimulation();
            
            // In kết quả
            System.out.println("\nSimulation info after running:");
            sim.printSimulationInfo();
            
        } catch (IllegalArgumentException | IllegalStateException | ArithmeticException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
