import java.util.Scanner;

public class MainProgram {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Do you want to solve a theoretical or practical problem?");
            System.out.println("Enter 1 for theoretical, 2 for practical, 0 to exit:");
            String choice = scanner.nextLine();

            if (choice.equals("0")) {
                System.out.println("Exiting program.");
                running = false;
                continue;
            }

            if (choice.equals("1")) {
                handleTheoretical(scanner);
            } else if (choice.equals("2")) {
                solvePracticalProblem(scanner);
            } else {
                System.out.println("Invalid choice. Please enter 1, 2, or 0.");
            }
        }

        scanner.close();
    }

    private static void handleTheoretical(Scanner scanner) {
        System.out.println("Which problem type do you want to solve?");
        System.out.println("1: Equation");
        System.out.println("2: System of Equations");
        System.out.println("3: Coordinates");
        System.out.println("4: Integration");
        System.out.println("Enter number (1-4):");
        String problemType = scanner.nextLine();

        try {
            switch (problemType) {
                case "1":
                    handleEquation(scanner);
                    break;
                case "2":
                    handleSystemOfEquations(scanner);
                    break;
                case "3":
                    handleCoordinates(scanner);
                    break;
                case "4":
                    handleIntegration(scanner);
                    break;
                default:
                    System.out.println("Invalid problem type. Please enter 1, 2, 3, or 4.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input: Please enter valid numbers.");
        } catch (IllegalArgumentException | ArithmeticException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleEquation(Scanner scanner) {
        System.out.print("Enter the degree of the polynomial (n): ");
        int degree = Integer.parseInt(scanner.nextLine());
        if (degree < 0) {
            throw new IllegalArgumentException("Degree must be non-negative");
        }

        PolynomialFunction polynomial = RandomPolynomialGenerator.generateRandomPolynomial(degree);
        System.out.println("Generated polynomial: f(x) = " + polynomial);
        System.out.print("Do you want to enter the interval [a, b]? (y/n): ");
        String choice = scanner.nextLine().toLowerCase();

        if (choice.equals("y")) {
            System.out.print("Enter value a: ");
            double a = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter value b: ");
            double b = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter error epsilon: ");
            double epsilon = Double.parseDouble(scanner.nextLine());
            if (epsilon <= 0) {
                throw new IllegalArgumentException("Epsilon must be greater than 0");
            }
            double root = BisectionAlgorithm.findRoot(polynomial, a, b, epsilon);
            System.out.printf("Root x = %.6f with f(x) = %.6f%n", root, polynomial.evaluate(root));
        } else if (choice.equals("n")) {
            System.out.print("Enter initial guess x0: ");
            double x0 = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter error epsilon: ");
            double epsilon = Double.parseDouble(scanner.nextLine());
            if (epsilon <= 0) {
                throw new IllegalArgumentException("Epsilon must be greater than 0");
            }
            double root = NewtonAlgorithm.findRoot(polynomial, x0, epsilon, 1000);
            System.out.printf("Root x = %.6f with f(x) = %.6f%n", root, polynomial.evaluate(root));
        } else {
            System.out.println("Invalid choice. Please enter 'y' or 'n'.");
        }
    }

    private static void handleSystemOfEquations(Scanner scanner) {
        System.out.print("Enter the number of equations (n): ");
        int numEquations = Integer.parseInt(scanner.nextLine());
        if (numEquations <= 0) {
            throw new IllegalArgumentException("Number of equations must be positive");
        }
        System.out.print("Enter the number of variables (degree): ");
        int numVariables = Integer.parseInt(scanner.nextLine());
        if (numVariables <= 0) {
            throw new IllegalArgumentException("Number of variables must be positive");
        }

        LinearSystem system = RandomLinearSystemGenerator.generateRandomLinearSystem(numEquations, numVariables);
        double[] solution;
        String methodUsed;
        if (numEquations < 100) {
            solution = GaussianElimination.solve(system);
            methodUsed = "GaussianElimination";
        } else {
            solution = GaussSeidel.solve(system, 1e-6, 1000);
            methodUsed = "GaussSeidel";
        }
        System.out.println("Solution:");
        for (int i = 0; i < solution.length; i++) {
            System.out.printf("x%d = %.6f%n", i + 1, solution[i]);
        }
        System.out.println("Classes used: LinearSystem, RandomLinearSystemGenerator, " + methodUsed);
    }

    private static void handleCoordinates(Scanner scanner) {
        System.out.print("Enter the number of coordinates (n): ");
        int numCoordinates = Integer.parseInt(scanner.nextLine());
        if (numCoordinates <= 0) {
            throw new IllegalArgumentException("Number of coordinates must be positive");
        }

        double[][] coordinates = CoordinateGenerator.generateCoordinates(numCoordinates);
        System.out.println("Generated coordinates:");
        for (int i = 0; i < coordinates.length; i++) {
            System.out.printf("(%.6f, %.6f)%n", coordinates[i][0], coordinates[i][1]);
        }
        double[] coefficients = LagrangeInterpolation.interpolate(coordinates);
        System.out.println("Lagrange interpolation polynomial coefficients (from lowest to highest degree):");
        for (int i = 0; i < coefficients.length; i++) {
            System.out.printf("a%d = %.6f%n", i, coefficients[i]);
        }
        System.out.println("Classes used: CoordinateGenerator, LagrangeInterpolation");
    }

    private static void handleIntegration(Scanner scanner) {
        System.out.println("Function: f(x) = x^2 + 2x + 1");
        System.out.print("Enter lower bound a: ");
        double a = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter upper bound b: ");
        double b = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter max iterations: ");
        int maxIterations = Integer.parseInt(scanner.nextLine());

        RombergIntegration.Function f = x -> x * x + 2 * x + 1;
        double result = RombergIntegration.integrate(f, a, b, maxIterations);
        System.out.printf("Integral from %.2f to %.2f = %.6f%n", a, b, result);
        System.out.println("Classes used: RombergIntegration");
    }

    private static void solvePracticalProblem(Scanner scanner) {
        try {
            // Nhập tham số từ người dùng
            System.out.print("Enter grid size n (min 2): ");
            int n = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter top boundary temperature: ");
            double topBoundary = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter bottom boundary temperature: ");
            double bottomBoundary = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter left boundary temperature: ");
            double leftBoundary = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter right boundary temperature: ");
            double rightBoundary = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter max iterations: ");
            int maxIterations = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter epsilon: ");
            double epsilon = Double.parseDouble(scanner.nextLine());

            System.out.println("\nRunning Heat Distribution Simulation...");
            System.out.println("Parameters: n = " + n + ", top = " + topBoundary + ", bottom = " + bottomBoundary +
                              ", left = " + leftBoundary + ", right = " + rightBoundary +
                              ", maxIterations = " + maxIterations + ", epsilon = " + epsilon);

            // Tạo và chạy mô phỏng
            HeatSimulation sim = new HeatSimulation(n, topBoundary, bottomBoundary, leftBoundary, rightBoundary,
                                                    maxIterations, epsilon);
            sim.runSimulation();

            // Lấy và in kết quả
            HeatGrid resultGrid = sim.getResultGrid();
            System.out.println("\nTemperature Distribution Result:");
            resultGrid.printGrid();

        } catch (NumberFormatException e) {
            System.out.println("Invalid input: Please enter valid numbers.");
        } catch (IllegalArgumentException | IllegalStateException | ArithmeticException e) {
            System.out.println("Error in simulation: " + e.getMessage());
        }
    }
}