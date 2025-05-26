import java.util.Random;

public class CoordinateGenerator {
    private static final Random random = new Random();

    // Tạo n cặp tọa độ (x, y) ngẫu nhiên
    public static double[][] generateCoordinates(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Number of coordinates must be non-negative");
        }

        // Mảng kết quả: mỗi hàng chứa một cặp [x, y]
        double[][] coordinates = new double[n][2];

        // Sinh giá trị ngẫu nhiên cho x và y trong khoảng [-10, 10]
        for (int i = 0; i < n; i++) {
            coordinates[i][0] = random.nextDouble() * 20 - 10; // x trong [-10, 10]
            coordinates[i][1] = random.nextDouble() * 20 - 10; // y trong [-10, 10]
        }

        return coordinates;
    }
}
