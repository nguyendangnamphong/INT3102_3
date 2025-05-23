import java.util.Scanner;

public class RombergIntegration {
    // Giao diện hàm số
    public interface Function {
        double evaluate(double x);
    }

    // Triển khai thuật toán Romberg Integration
    public static double integrate(Function f, double a, double b, int maxIterations) {
        if (maxIterations < 1) {
            throw new IllegalArgumentException("Max iterations must be positive");
        }
        if (a >= b) {
            throw new IllegalArgumentException("Lower bound a must be less than upper bound b");
        }

        double[][] R = new double[maxIterations][maxIterations];

        // Tính R[i][0] bằng quy tắc Trapezoid
        for (int i = 0; i < maxIterations; i++) {
            int n = (int) Math.pow(2, i); // Số đoạn chia
            double h = (b - a) / n;
            double sum = f.evaluate(a) + f.evaluate(b);
            for (int j = 1; j < n; j++) {
                sum += 2 * f.evaluate(a + j * h);
            }
            R[i][0] = h / 2 * sum;
        }

        // Tính R[i][j] bằng ngoại suy Richardson
        for (int j = 1; j < maxIterations; j++) {
            for (int i = j; i < maxIterations; i++) {
                R[i][j] = R[i][j-1] + (R[i][j-1] - R[i-1][j-1]) / (Math.pow(4, j) - 1);
            }
        }

        return R[maxIterations-1][maxIterations-1];
    }

    // Hàm main để kiểm tra
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Định nghĩa hàm số mẫu: f(x) = x^2 + 2x + 1
        Function f = x -> x * x + 2 * x + 1;

        System.out.println("Romberg Integration Test");
        System.out.println("Function: f(x) = x^2 + 2x + 1");

        // Nhập a
        System.out.print("Enter lower bound a: ");
        double a;
        try {
            a = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input for a");
            scanner.close();
            return;
        }

        // Nhập b
        System.out.print("Enter upper bound b: ");
        double b;
        try {
            b = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input for b");
            scanner.close();
            return;
        }

        // Nhập maxIterations
        System.out.print("Enter max iterations: ");
        int maxIterations;
        try {
            maxIterations = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input for max iterations");
            scanner.close();
            return;
        }

        // Tính tích phân
        try {
            double result = integrate(f, a, b, maxIterations);
            System.out.printf("Integral from %.2f to %.2f = %.6f%n", a, b, result);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        scanner.close();
    }
}
