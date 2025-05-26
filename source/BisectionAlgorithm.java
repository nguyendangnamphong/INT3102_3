public class BisectionAlgorithm {
    // Tìm nghiệm của f(x) = 0 trong khoảng [a, b] với sai số epsilon
    public static double findRoot(PolynomialFunction f, double a, double b, double epsilon) {
        // Kiểm tra điều kiện đầu vào
        if (f == null) {
            throw new IllegalArgumentException("Can no null");
        }
        if (a >= b) {
            throw new IllegalArgumentException(" [a, b] erorr: a must smaller than b");
        }
        if (epsilon <= 0) {
            throw new IllegalArgumentException(" epsilon need bigger 0");
        }

        // Kiểm tra dấu của f(a) và f(b)
        double fa = f.evaluate(a);
        double fb = f.evaluate(b);
        if (fa * fb > 0) {
            throw new IllegalArgumentException("f(a) and f(b) must have diffenrent signs");
        }

        // Thuật toán chia đôi
        while ((b - a) > epsilon) {
            double mid = (a + b) / 2;
            double fmid = f.evaluate(mid);

            if (Math.abs(fmid) < epsilon) {
                return mid; // Tìm thấy nghiệm đủ gần 0
            }

            if (fa * fmid < 0) {
                b = mid; // Nghiệm nằm trong [a, mid]
                fb = fmid;
            } else {
                a = mid; // Nghiệm nằm trong [mid, b]
                fa = fmid;
            }
        }

        // Trả về điểm giữa của khoảng cuối cùng
        return (a + b) / 2;
    }
}