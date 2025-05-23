public class PolynomialFunction {
    private int degree; // Bậc của đa thức
    private double[] coefficients; // Mảng chứa các hệ số của đa thức

    // Constructor nhận bậc và mảng hệ số
    public PolynomialFunction(int degree, double[] coefficients) {
        if (degree < 0) {
            throw new IllegalArgumentException("The degree of the polynomial must be non-negative.");
        }
        if (coefficients.length != degree + 1) {
            throw new IllegalArgumentException("The number of coefficients must be equal to the polynomial degree plus 1.");
        }

        this.degree = degree;
        this.coefficients = new double[degree + 1];
        for (int i = 0; i <= degree; i++) {
            this.coefficients[i] = coefficients[i];
        }
    }

    // Lấy bậc của đa thức
    public int getDegree() {
        return degree;
    }

    // Lấy hệ số tại vị trí index
    public double getCoefficient(int index) {
        if (index < 0 || index > degree) {
            throw new IllegalArgumentException("Invalid coefficient index");
        }
        return coefficients[index];
    }

    // Tính giá trị của f(x) tại x
    public double evaluate(double x) {
        double result = 0;
        for (int i = degree; i >= 0; i--) {
            result = result * x + coefficients[i];
        }
        return result;
    }

    // Trả về đạo hàm của đa thức
    public PolynomialFunction derivative() {
        if (degree == 0) {
            // Đạo hàm của hằng số là 0
            return new PolynomialFunction(0, new double[]{0});
        }

        // Đạo hàm của đa thức bậc n là đa thức bậc n-1
        double[] derivCoefficients = new double[degree];
        for (int i = 1; i <= degree; i++) {
            derivCoefficients[i - 1] = i * coefficients[i];
        }
        return new PolynomialFunction(degree - 1, derivCoefficients);
    }

    // Trả về biểu diễn chuỗi của đa thức
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean firstTerm = true;

        for (int i = degree; i >= 0; i--) {
            if (coefficients[i] != 0) {
                // Xử lý dấu
                if (!firstTerm) {
                    sb.append(coefficients[i] > 0 ? " + " : " - ");
                } else if (coefficients[i] < 0) {
                    sb.append("-");
                }

                // Xử lý giá trị tuyệt đối của hệ số
                double absCoef = Math.abs(coefficients[i]);
                if (i == 0 || absCoef != 1) {
                    sb.append(absCoef);
                }

                // Xử lý biến x và số mũ
                if (i > 0) {
                    sb.append("x");
                    if (i > 1) {
                        sb.append("^").append(i);
                    }
                }

                firstTerm = false;
            }
        }

        return sb.length() == 0 ? "0" : sb.toString();
    }
}