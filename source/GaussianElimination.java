public class GaussianElimination {
    public static double[] solve(LinearSystem system) {
        double[][] coefficients = system.getCoefficients();
        double[] constants = system.getConstants();
        int numEquations = coefficients.length;
        int numVariables = coefficients[0].length;

        if (numEquations != numVariables) {
            throw new IllegalArgumentException("The system must be square (number of equations equals number of variables)");
        }

        double[][] augmentedMatrix = new double[numEquations][numVariables + 1];
        for (int i = 0; i < numEquations; i++) {
            System.arraycopy(coefficients[i], 0, augmentedMatrix[i], 0, numVariables);
            augmentedMatrix[i][numVariables] = constants[i];
        }

        for (int k = 0; k < numEquations; k++) {
            int maxRow = k;
            double maxValue = Math.abs(augmentedMatrix[k][k]);
            for (int i = k + 1; i < numEquations; i++) {
                double value = Math.abs(augmentedMatrix[i][k]);
                if (value > maxValue) {
                    maxValue = value;
                    maxRow = i;
                }
            }

            if (maxRow != k) {
                double[] temp = augmentedMatrix[k];
                augmentedMatrix[k] = augmentedMatrix[maxRow];
                augmentedMatrix[maxRow] = temp;
            }

            if (augmentedMatrix[k][k] == 0) {
                throw new ArithmeticException("The system does not have a unique solution");
            }

            for (int i = k + 1; i < numEquations; i++) {
                double factor = augmentedMatrix[i][k] / augmentedMatrix[k][k];
                for (int j = k; j <= numVariables; j++) {
                    augmentedMatrix[i][j] -= factor * augmentedMatrix[k][j];
                }
            }
        }

        double[] solution = new double[numVariables];
        for (int i = numEquations - 1; i >= 0; i--) {
            double sum = 0;
            for (int j = i + 1; j < numVariables; j++) {
                sum += augmentedMatrix[i][j] * solution[j];
            }
            solution[i] = (augmentedMatrix[i][numVariables] - sum) / augmentedMatrix[i][i];
        }

        return solution;
    }
}