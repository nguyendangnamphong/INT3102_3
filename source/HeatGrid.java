public class HeatGrid {
    private int n; // Kích thước lưới (n x n)
    private double[][] grid; // Ma trận lưu nhiệt độ
    private double topBoundary; // Nhiệt độ biên trên
    private double bottomBoundary; // Nhiệt độ biên dưới
    private double leftBoundary; // Nhiệt độ biên trái
    private double rightBoundary; // Nhiệt độ biên phải

    // Constructor: Khởi tạo lưới với kích thước và điều kiện biên
    public HeatGrid(int n, double topBoundary, double bottomBoundary, double leftBoundary, double rightBoundary) {
        if (n < 2) {
            throw new IllegalArgumentException("Grid size n must be at least 2");
        }
        this.n = n;
        this.topBoundary = topBoundary;
        this.bottomBoundary = bottomBoundary;
        this.leftBoundary = leftBoundary;
        this.rightBoundary = rightBoundary;
        this.grid = new double[n][n];
        initializeGrid();
    }

    // Khởi tạo lưới: Đặt điều kiện biên và giá trị ban đầu cho các điểm nội
    private void initializeGrid() {
        // Đặt nhiệt độ cho các biên
        for (int j = 0; j < n; j++) {
            grid[0][j] = topBoundary; // Biên trên
            grid[n - 1][j] = bottomBoundary; // Biên dưới
        }
        for (int i = 0; i < n; i++) {
            grid[i][0] = leftBoundary; // Biên trái
            grid[i][n - 1] = rightBoundary; // Biên phải
        }
        // Đặt giá trị ban đầu cho các điểm nội (ví dụ: 0)
        for (int i = 1; i < n - 1; i++) {
            for (int j = 1; j < n - 1; j++) {
                grid[i][j] = 0.0;
            }
        }
    }

    // Lấy kích thước lưới
    public int getSize() {
        return n;
    }

    // Lấy giá trị nhiệt độ tại điểm (i, j)
    public double getValue(int i, int j) {
        if (i < 0 || i >= n || j < 0 || j >= n) {
            throw new IllegalArgumentException("Index out of bounds: (" + i + ", " + j + ")");
        }
        return grid[i][j];
    }

    // Đặt giá trị nhiệt độ tại điểm (i, j)
    public void setValue(int i, int j, double value) {
        if (i < 0 || i >= n || j < 0 || j >= n) {
            throw new IllegalArgumentException("Index out of bounds: (" + i + ", " + j + ")");
        }
        grid[i][j] = value;
    }

    // Kiểm tra xem điểm (i, j) có phải là biên không
    public boolean isBoundary(int i, int j) {
        if (i < 0 || i >= n || j < 0 || j >= n) {
            return false;
        }
        return i == 0 || i == n - 1 || j == 0 || j == n - 1;
    }

    // Lấy toàn bộ lưới (dùng để xuất kết quả)
    public double[][] getGrid() {
        return grid;
    }

    // In lưới ra console để kiểm tra
    public void printGrid() {
        System.out.println("Heat Grid (" + n + " x " + n + "):");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.printf("%8.2f", grid[i][j]);
            }
            System.out.println();
        }
    }

    // Hàm main để kiểm tra độc lập
    public static void main(String[] args) {
        try {
            // Tạo lưới 4x4 với điều kiện biên mẫu
            int n = 4;
            double top = 100.0;
            double bottom = 0.0;
            double left = 50.0;
            double right = 50.0;
            System.out.println("Creating HeatGrid with n = " + n + ", top = " + top + ", bottom = " + bottom + ", left = " + left + ", right = " + right);
            HeatGrid grid = new HeatGrid(n, top, bottom, left, right);

            // In lưới ban đầu
            System.out.println("Initial grid:");
            grid.printGrid();

            // Kiểm tra truy cập và sửa đổi
            System.out.println("\nTesting getValue(1, 1): " + grid.getValue(1, 1));
            System.out.println("Testing isBoundary(0, 0): " + grid.isBoundary(0, 0));
            System.out.println("Testing isBoundary(1, 1): " + grid.isBoundary(1, 1));
            grid.setValue(1, 1, 25.0);
            System.out.println("After setValue(1, 1, 25.0):");
            grid.printGrid();

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
