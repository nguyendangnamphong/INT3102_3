public class LinearSystem {
    private double[][] coefficients; // Ma trận hệ số A
    private double[] constants;      // Vector hằng số b
    private int numEquations;        // Số phương trình
    private int numVariables;        // Số biến

    // Constructor
    public LinearSystem(double[][] coefficients, double[] constants) {
        if (coefficients == null || constants == null) {
            throw new IllegalArgumentException("Coefficient matrix and constant vector cannot be null");
        }
        numEquations = coefficients.length;
        numVariables = coefficients[0].length;
        if (constants.length != numEquations) {
            throw new IllegalArgumentException("The size of the constant vector does not match the number of equations.");
        }
        this.coefficients = new double[numEquations][numVariables];
        for (int i = 0; i < numEquations; i++) {
            if (coefficients[i].length != numVariables) {
                throw new IllegalArgumentException("Invalid coefficient matrix");
            }
            System.arraycopy(coefficients[i], 0, this.coefficients[i], 0, numVariables);
        }
        this.constants = new double[numEquations];
        System.arraycopy(constants, 0, this.constants, 0, numEquations);
    }

    // Getter cho ma trận hệ số
    public double[][] getCoefficients() {
        double[][] copy = new double[numEquations][numVariables];
        for (int i = 0; i < numEquations; i++) {
            System.arraycopy(coefficients[i], 0, copy[i], 0, numVariables);
        }
        return copy;
    }

    // Getter cho vector hằng số
    public double[] getConstants() {
        double[] copy = new double[numEquations];
        System.arraycopy(constants, 0, copy, 0, numEquations);
        return copy;
    }

    // Phương thức giải hệ phương trình (Gauss-Jordan đơn giản)
    public double[] solve() {
        double[][] augMatrix = new double[numEquations][numVariables + 1];
        // Tạo ma trận mở rộng
        for (int i = 0; i < numEquations; i++) {
            System.arraycopy(coefficients[i], 0, augMatrix[i], 0, numVariables);
            augMatrix[i][numVariables] = constants[i];
        }

        // Thuật toán Gauss-Jordan
        for (int i = 0; i < numEquations; i++) {
            // Tìm pivot
            double pivot = augMatrix[i][i];
            if (pivot == 0) {
                throw new ArithmeticException("The system of equations has no unique solution.");
            }
            // Chuẩn hóa hàng
            for (int j = 0; j <= numVariables; j++) {
                augMatrix[i][j] /= pivot;
            }
            // Loại bỏ cột
            for (int k = 0; k < numEquations; k++) {
                if (k != i) {
                    double factor = augMatrix[k][i];
                    for (int j = 0; j <= numVariables; j++) {
                        augMatrix[k][j] -= factor * augMatrix[i][j];
                    }
                }
            }
        }

        // Trích xuất nghiệm
        double[] solution = new double[numVariables];
        for (int i = 0; i < numEquations; i++) {
            solution[i] = augMatrix[i][numVariables];
        }
        return solution;
    }
}
