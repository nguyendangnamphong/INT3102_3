public class NewtonAlgorithm {
    // Hàm tính nghiệm bằng phương pháp Newton-Raphson
    public static double findRoot(PolynomialFunction f, double initialGuess, double tolerance, int maxIterations) {
        double x = initialGuess;
        int iteration = 0;

        while (iteration < maxIterations) {
            double fx = f.evaluate(x);              // Giá trị của đa thức tại x
            PolynomialFunction fPrime = f.derivative(); // Đạo hàm của đa thức
            double fPrimeX = fPrime.evaluate(x);    // Giá trị của đạo hàm tại x

            if (Math.abs(fPrimeX) < 1e-10) {        // Tránh chia cho số quá nhỏ
                throw new ArithmeticException("The derivative is close to 0, Newton's method does not converge.");
            }

            double nextX = x - fx / fPrimeX;        // Công thức Newton: x_n+1 = x_n - f(x_n)/f'(x_n)
            if (Math.abs(nextX - x) < tolerance) {  // Kiểm tra điều kiện hội tụ
                return nextX;
            }

            x = nextX;
            iteration++;
        }

        throw new RuntimeException("No solution found after" + maxIterations + " inerations");
    }

    // Ví dụ sử dụng
    public static void main(String[] args) {
        // Tạo đa thức f(x) = x^2 - 2
        double[] coefficients = {-2, 0, 1}; // Hệ số: -2 + 0x + 1x^2
        PolynomialFunction f = new PolynomialFunction(2, coefficients);

        double root = findRoot(f, 1.0, 1e-6, 100); // Tìm nghiệm với đoán ban đầu là 1.0
        System.out.println("Nghiệm gần đúng: " + root);
    }
}