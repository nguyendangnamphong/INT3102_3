public class SparseMatrix {
    private int rows; // Số hàng của ma trận
    private int cols; // Số cột của ma trận
    private double[] values; // Mảng lưu các giá trị khác 0
    private int[] colIndices; // Mảng lưu chỉ số cột của các giá trị
    private int[] rowPtr; // Mảng lưu vị trí bắt đầu của mỗi hàng
    private int nnz; // Số phần tử khác 0 (non-zero)

    // Constructor: Tạo ma trận thưa từ lưới HeatGrid
    public SparseMatrix(HeatGrid grid) {
        if (grid == null) {
            throw new IllegalArgumentException("HeatGrid cannot be null");
        }
        int n = grid.getSize();
        // Số điểm nội: (n-2) x (n-2)
        this.rows = (n - 2) * (n - 2);
        this.cols = (n - 2) * (n - 2);
        
        // Tính số phần tử khác 0 (nnz)
        // Mỗi điểm nội có tối đa 5 phần tử (trung tâm và 4 hàng xóm)
        this.nnz = 0;
        for (int i = 1; i < n - 1; i++) {
            for (int j = 1; j < n - 1; j++) {
                nnz += 5; // Trung tâm (4) và 4 hàng xóm (-1 mỗi cái)
            }
        }
        
        // Khởi tạo mảng CSR
        this.values = new double[nnz];
        this.colIndices = new int[nnz];
        this.rowPtr = new int[rows + 1];
        
        // Xây dựng ma trận
        buildMatrix(grid);
    }

    // Xây dựng ma trận thưa từ lưới
    private void buildMatrix(HeatGrid grid) {
        int n = grid.getSize();
        int index = 0; // Chỉ số cho values và colIndices
        rowPtr[0] = 0;
        
        for (int row = 0; row < rows; row++) {
            // Chuyển row thành tọa độ (i, j) trong lưới
            int i = row / (n - 2) + 1;
            int j = row % (n - 2) + 1;
            int colBase = (i - 1) * (n - 2) + (j - 1); // Chỉ số cột cơ sở
            
            // Thêm các phần tử khác 0 cho điểm (i, j)
            // 1. Hàng xóm trên (i-1, j) nếu tồn tại
            if (i > 1) {
                values[index] = -1.0;
                colIndices[index] = colBase - (n - 2);
                index++;
            }
            // 2. Hàng xóm trái (i, j-1) nếu tồn tại
            if (j > 1) {
                values[index] = -1.0;
                colIndices[index] = colBase - 1;
                index++;
            }
            // 3. Trung tâm (i, j)
            values[index] = 4.0;
            colIndices[index] = colBase;
            index++;
            // 4. Hàng xóm phải (i, j+1) nếu tồn tại
            if (j < n - 2) {
                values[index] = -1.0;
                colIndices[index] = colBase + 1;
                index++;
            }
            // 5. Hàng xóm dưới (i+1, j) nếu tồn tại
            if (i < n - 2) {
                values[index] = -1.0;
                colIndices[index] = colBase + (n - 2);
                index++;
            }
            
            // Cập nhật rowPtr
            rowPtr[row + 1] = index;
        }
    }

    // Lấy số hàng
    public int getRows() {
        return rows;
    }

    // Lấy số cột
    public int getCols() {
        return cols;
    }

    // Lấy giá trị tại (row, col)
    public double getValue(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IllegalArgumentException("Index out of bounds: (" + row + ", " + col + ")");
        }
        // Tìm trong khoảng từ rowPtr[row] đến rowPtr[row+1]
        for (int i = rowPtr[row]; i < rowPtr[row + 1]; i++) {
            if (colIndices[i] == col) {
                return values[i];
            }
        }
        return 0.0; // Nếu không tìm thấy, trả về 0
    }

    // Nhân ma trận với vector
    public double[] multiplyVector(double[] x) {
        if (x.length != cols) {
            throw new IllegalArgumentException("Vector length must match matrix columns: " + cols);
        }
        double[] result = new double[rows];
        for (int i = 0; i < rows; i++) {
            result[i] = 0.0;
            for (int j = rowPtr[i]; j < rowPtr[i + 1]; j++) {
                result[i] += values[j] * x[colIndices[j]];
            }
        }
        return result;
    }

    // Getter cho mảng values
    public double[] getValues() {
        return values;
    }

    // Getter cho mảng colIndices
    public int[] getColIndices() {
        return colIndices;
    }

    // Getter cho mảng rowPtr
    public int[] getRowPtr() {
        return rowPtr;
    }

    // In ma trận (dạng đầy đủ) để kiểm tra
    public void printMatrix() {
        System.out.println("Sparse Matrix (" + rows + " x " + cols + "):");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.printf("%8.2f", getValue(i, j));
            }
            System.out.println();
        }
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
            
            // In ma trận
            System.out.println("\nMatrix content:");
            matrix.printMatrix();
            
            // Kiểm tra nhân vector
            double[] x = new double[matrix.getCols()];
            for (int i = 0; i < x.length; i++) {
                x[i] = 1.0; // Vector toàn 1
            }
            System.out.println("\nTesting multiplyVector with vector of ones:");
            double[] result = matrix.multiplyVector(x);
            System.out.print("Result: [");
            for (int i = 0; i < result.length; i++) {
                System.out.printf("%.2f", result[i]);
                if (i < result.length - 1) System.out.print(", ");
            }
            System.out.println("]");
            
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}