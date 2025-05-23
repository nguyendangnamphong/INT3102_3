import java.util.Random;

public class RandomLinearSystemGenerator {
    private static final Random random = new Random();

    // Sinh hệ phương trình tuyến tính ngẫu nhiên với numEquations phương trình và numVariables biến
    public static LinearSystem generateRandomLinearSystem(int numEquations, int numVariables) {
        // Khởi tạo ma trận hệ số và vector hằng số
        double[][] coefficients = new double[numEquations][numVariables];
        double[] constants = new double[numEquations];

        // Sinh giá trị ngẫu nhiên cho ma trận hệ số
        for (int i = 0; i < numEquations; i++) {
            for (int j = 0; j < numVariables; j++) {
                coefficients[i][j] = random.nextDouble() * 20 - 10; // Giá trị trong khoảng [-10, 10]
            }
        }

        // Sinh giá trị ngẫu nhiên cho vector hằng số
        for (int i = 0; i < numEquations; i++) {
            constants[i] = random.nextDouble() * 20 - 10; // Giá trị trong khoảng [-10, 10]
        }

        // Tạo và trả về đối tượng LinearSystem
        return new LinearSystem(coefficients, constants);
    }
}
