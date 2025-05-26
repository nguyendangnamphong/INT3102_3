import java.util.Random;

public class RandomPolynomialGenerator {
    private static final Random random = new Random();
    
    // Tạo PolynomialFunction ngẫu nhiên với bậc n
    public static PolynomialFunction generateRandomPolynomial(int degree) {
        if (degree < 0) {
            throw new IllegalArgumentException("The degree of the polynomial must be non-negative.");
        }
        
        // Tạo mảng hệ số ngẫu nhiên
        double[] coefficients = new double[degree + 1];
        for (int i = 0; i <= degree; i++) {
            // Tạo hệ số ngẫu nhiên trong khoảng [-10, 10]
            coefficients[i] = random.nextDouble() * 20 - 10;
            // Đảm bảo hệ số cao nhất khác 0
            if (i == degree && coefficients[i] == 0) {
                coefficients[i] = random.nextDouble() * 5 + 1; // [1, 6]
            }
        }
        
        return new PolynomialFunction(degree, coefficients);
    }
    
    // Phương thức chính để tạo và giải phương trình
    public static double solveRandomPolynomial(int degree, double a, double b, double epsilon) {
        // Tạo đa thức ngẫu nhiên
        PolynomialFunction polynomial = generateRandomPolynomial(degree);
        
        // In ra đa thức để kiểm tra
        System.out.println("The polynomial is generated: f(x) = " + polynomial);
        
        try {
            // Sử dụng thuật toán chia đôi để tìm nghiệm
            double root = BisectionAlgorithm.findRoot(polynomial, a, b, epsilon);
            System.out.println("x = " + root + " with f(x) = " + polynomial.evaluate(root));
            return root;
        } catch (IllegalArgumentException e) {
            System.out.println("No solution found in the interval [" + a + ", " + b + "]: " + e.getMessage());
            return Double.NaN;
        }
    }
    
    // Main để test
    public static void main(String[] args) {
        // Ví dụ sử dụng
        int degree = 3; // Bậc 3
        double a = -10; // Khoảng trái
        double b = 10;  // Khoảng phải
        double epsilon = 0.0001; // Sai số
        
        solveRandomPolynomial(degree, a, b, epsilon);
    }
}