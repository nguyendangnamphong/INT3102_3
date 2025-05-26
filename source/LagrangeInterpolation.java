public class LagrangeInterpolation {
    // Tính đa thức nội suy Lagrange từ các cặp tọa độ
    public static double[] interpolate(double[][] coordinates) {
        if (coordinates == null || coordinates.length == 0) {
            throw new IllegalArgumentException("Coordinates array cannot be null or empty");
        }
        int n = coordinates.length;

        // Kiểm tra định dạng của coordinates
        for (double[] coord : coordinates) {
            if (coord.length != 2) {
                throw new IllegalArgumentException("Each coordinate must have exactly 2 values (x, y)");
            }
        }

        // Trích xuất x và y từ coordinates
        double[] x = new double[n];
        double[] y = new double[n];
        for (int i = 0; i < n; i++) {
            x[i] = coordinates[i][0];
            y[i] = coordinates[i][1];
        }

        // Kiểm tra các giá trị x phân biệt
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (x[i] == x[j]) {
                    throw new IllegalArgumentException("All x values must be distinct");
                }
            }
        }

        // Tính hệ số của đa thức nội suy
        double[] coefficients = new double[n]; // Đa thức bậc n-1
        for (int i = 0; i < n; i++) {
            double[] tempPoly = new double[]{1}; // Đa thức tạm thời cho l_i(x)
            double denominator = 1;

            // Tính l_i(x) = prod_{j!=i} (x - x_j) / (x_i - x_j)
            for (int j = 0; j < n; j++) {
                if (j != i) {
                    // Nhân với (x - x_j)
                    tempPoly = polynomialMultiply(tempPoly, new double[]{-x[j], 1});
                    denominator *= (x[i] - x[j]);
                }
            }

            // Nhân với y_i / denominator
            for (int k = 0; k < tempPoly.length; k++) {
                tempPoly[k] *= y[i] / denominator;
            }

            // Cộng vào đa thức kết quả
            coefficients = polynomialAdd(coefficients, tempPoly);
        }

        return coefficients;
    }

    // Nhân hai đa thức
    private static double[] polynomialMultiply(double[] p1, double[] p2) {
        int m = p1.length;
        int n = p2.length;
        double[] result = new double[m + n - 1];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                result[i + j] += p1[i] * p2[j];
            }
        }
        return result;
    }

    // Cộng hai đa thức
    private static double[] polynomialAdd(double[] p1, double[] p2) {
        int n = Math.max(p1.length, p2.length);
        double[] result = new double[n];

        for (int i = 0; i < n; i++) {
            double coef1 = i < p1.length ? p1[i] : 0;
            double coef2 = i < p2.length ? p2[i] : 0;
            result[i] = coef1 + coef2;
        }
        return result;
    }
}