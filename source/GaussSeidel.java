public class GaussSeidel {
    // Phương thức giải hệ phương trình tuyến tính bằng phương pháp Gauss-Seidel
    public static double[] solve(LinearSystem system, double tolerance, int maxIterations) {
        double[][] A = system.getCoefficients();
        double[] b = system.getConstants();
        int n = A.length;
        double[] x = new double[n]; // Vector nghiệm ban đầu (khởi tạo là 0)
        double[] xNew = new double[n];

        for (int iter = 0; iter < maxIterations; iter++) {
            for (int i = 0; i < n; i++) {
                double sum1 = 0;
                for (int j = 0; j < i; j++) {
                    sum1 += A[i][j] * xNew[j];
                }
                double sum2 = 0;
                for (int j = i + 1; j < n; j++) {
                    sum2 += A[i][j] * x[j];
                }
                xNew[i] = (b[i] - sum1 - sum2) / A[i][i];
            }

            // Kiểm tra điều kiện dừng
            double maxDiff = 0;
            for (int i = 0; i < n; i++) {
                double diff = Math.abs(xNew[i] - x[i]);
                if (diff > maxDiff) {
                    maxDiff = diff;
                }
            }
            if (maxDiff < tolerance) {
                return xNew; // Hội tụ, trả về nghiệm
            }

            // Cập nhật vector nghiệm cho lần lặp tiếp theo
            System.arraycopy(xNew, 0, x, 0, n);
        }

        throw new ArithmeticException("The Gauss-Seidel method does not converge after" + maxIterations + " iteration");
    }
}
