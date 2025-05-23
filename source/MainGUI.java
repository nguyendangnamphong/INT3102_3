import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class MainGUI extends JFrame {
    private JTextArea outputArea;
    private JPanel mainPanel;
    private JTextField inputField;
    private JButton submitButton;
    private JButton continueButton;
    private JPanel heatmapPanel;
    private JLabel questionLabel;
    private JRadioButton theoreticalButton, practicalButton;
    private ButtonGroup problemCategoryGroup;
    private int currentProblemType = -1;
    private boolean selectingProblemType = false;
    private boolean choosingProblemCategory = true;
    private int numProblemTypes = 4;
    private String[] problemNames = {"1 - Equation", "2 - System of Equations", "3 - Coordinates", "4 - Integration"};
    private int step = 0;
    private int degree, numEquations, numVariables, numCoordinates, maxIterations;
    private double a, b, x0, epsilon;
    private PolynomialFunction polynomial;
    private BufferedImage backgroundImage;
    private int gridSize;
    private double topBoundary, bottomBoundary, leftBoundary, rightBoundary;

    public MainGUI() {
        setTitle("Math Problem Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        try {
            backgroundImage = ImageIO.read(new File("E:\\Môn đang học\\Phương pháp tính\\project\\BackGround.jpg"));
        } catch (Exception e) {
            System.out.println("Error loading background image: " + e.getMessage());
            backgroundImage = null;
        }

        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(Color.WHITE);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        mainPanel.setLayout(null);
        add(mainPanel, BorderLayout.CENTER);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Arial", Font.PLAIN, 16));

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                }
            }
        });
        setFocusable(true);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (selectingProblemType) {
                    showProblemTypeSelection();
                } else if (choosingProblemCategory) {
                    showProblemCategorySelection();
                }
            }
        });

        showProblemCategorySelection();
    }

    private void showProblemCategorySelection() {
        choosingProblemCategory = true;
        selectingProblemType = false;
        currentProblemType = -1;
        step = 0;
        removeHeatmapPanel();
        for (Component comp : mainPanel.getComponents()) {
            mainPanel.remove(comp);
        }

        // Tạo câu hỏi thông báo
        if (questionLabel != null) {
            mainPanel.remove(questionLabel);
        }
        questionLabel = new JLabel("Choose Problem Category:", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 24));
        questionLabel.setForeground(Color.BLACK);
        int labelWidth = 600;
        int labelHeight = 50;
        questionLabel.setBounds((mainPanel.getWidth() - labelWidth) / 2, 100, labelWidth, labelHeight);
        mainPanel.add(questionLabel);

        // Hiệu ứng nhấp nháy
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            private float opacity = 1.0f;
            private boolean increasing = false;

            @Override
            public void run() {
                opacity = increasing ? opacity + 0.05f : opacity - 0.05f;
                if (opacity >= 1.0f) {
                    opacity = 1.0f;
                    increasing = false;
                } else if (opacity <= 0.5f) {
                    opacity = 0.5f;
                    increasing = true;
                }
                SwingUtilities.invokeLater(() -> {
                    questionLabel.setForeground(new Color(0, 0, 0, opacity));
                    questionLabel.repaint();
                });
            }
        }, 0, 50);

        // Tạo radio buttons kiểu hình tròn màu xanh
        if (theoreticalButton != null) {
            mainPanel.remove(theoreticalButton);
            mainPanel.remove(practicalButton);
            mainPanel.remove(submitButton);
        }
        theoreticalButton = new JRadioButton("Theoretical", true) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(135, 206, 250));
                g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
                super.paintComponent(g);
            }
            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.BLACK);
                g2.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
            }
        };
        practicalButton = new JRadioButton("Practical", false) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(135, 206, 250));
                g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
                super.paintComponent(g);
            }
            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.BLACK);
                g2.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
            }
        };
        problemCategoryGroup = new ButtonGroup();
        problemCategoryGroup.add(theoreticalButton);
        problemCategoryGroup.add(practicalButton);
        theoreticalButton.setFont(new Font("Arial", Font.PLAIN, 16));
        practicalButton.setFont(new Font("Arial", Font.PLAIN, 16));
        theoreticalButton.setContentAreaFilled(false);
        practicalButton.setContentAreaFilled(false);
        theoreticalButton.setBorderPainted(false);
        practicalButton.setBorderPainted(false);
        int buttonWidth = 150;
        int buttonHeight = 50;
        theoreticalButton.setBounds((mainPanel.getWidth() - buttonWidth * 2 - 20) / 2, 200, buttonWidth, buttonHeight);
        practicalButton.setBounds((mainPanel.getWidth() - buttonWidth * 2 - 20) / 2 + buttonWidth + 20, 200, buttonWidth, buttonHeight);
        mainPanel.add(theoreticalButton);
        mainPanel.add(practicalButton);

        // Nút Submit
        submitButton = new JButton("Submit");
        submitButton.setBounds(mainPanel.getWidth() / 2 - 50, 270, 100, 30);
        submitButton.addActionListener(e -> handleProblemCategorySelection());
        mainPanel.add(submitButton);

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showProblemTypeSelection() {
        choosingProblemCategory = false;
        selectingProblemType = true;
        step = 0;
        removeHeatmapPanel();
        for (Component comp : mainPanel.getComponents()) {
            mainPanel.remove(comp);
        }

        // Tạo câu hỏi thông báo
        if (questionLabel != null) {
            mainPanel.remove(questionLabel);
        }
        questionLabel = new JLabel("Choose Problem Type:", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 24));
        questionLabel.setForeground(Color.BLACK);
        int labelWidth = 600;
        int labelHeight = 50;
        questionLabel.setBounds((mainPanel.getWidth() - labelWidth) / 2, 100, labelWidth, labelHeight);
        mainPanel.add(questionLabel);

        // Hiệu ứng nhấp nháy
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            private float opacity = 1.0f;
            private boolean increasing = false;

            @Override
            public void run() {
                opacity = increasing ? opacity + 0.05f : opacity - 0.05f;
                if (opacity >= 1.0f) {
                    opacity = 1.0f;
                    increasing = false;
                } else if (opacity <= 0.5f) {
                    opacity = 0.5f;
                    increasing = true;
                }
                SwingUtilities.invokeLater(() -> {
                    questionLabel.setForeground(new Color(0, 0, 0, opacity));
                    questionLabel.repaint();
                });
            }
        }, 0, 50);

        // Tạo nút chọn loại bài toán
        int buttonSize = 100;
        int spacing = 20;
        int totalWidth = numProblemTypes * buttonSize + (numProblemTypes - 1) * spacing;
        int startX = Math.max(50, (mainPanel.getWidth() - totalWidth) / 2);
        int startY = 200;

        if (startX + totalWidth > mainPanel.getWidth() - 50) {
            startX = mainPanel.getWidth() - totalWidth - 50;
        }

        for (int i = 0; i < numProblemTypes; i++) {
            JButton button = new JButton(problemNames[i]) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(135, 206, 250));
                    g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
                    super.paintComponent(g);
                }
                @Override
                protected void paintBorder(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(Color.BLACK);
                    g2.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
                }
            };
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setBounds(startX + i * (buttonSize + spacing), startY, buttonSize, buttonSize);
            final int problemType = i + 1;
            button.addActionListener(e -> selectProblemType(problemType));
            mainPanel.add(button);
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void selectProblemType(int type) {
        currentProblemType = type;
        selectingProblemType = false;
        step = 1;
        removeHeatmapPanel();
        for (Component comp : mainPanel.getComponents()) {
            if (!(comp instanceof JScrollPane)) {
                mainPanel.remove(comp);
            }
        }
        outputArea.setText("");

        // Thêm outputArea
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBounds(50, 50, 600, 400);
        mainPanel.add(scrollPane);

        if (type == 1) {
            outputArea.append("Enter the degree of the polynomial (n):\n");
        } else if (type == 2) {
            outputArea.append("Enter the number of equations (n):\n");
        } else if (type == 3) {
            outputArea.append("Enter the number of coordinates (n):\n");
        } else if (type == 4) {
            outputArea.append("Function: f(x) = x^2 + 2x + 1\nEnter lower bound a:\n");
        }

        showInputField();
    }

    private void showInputField() {
        if (questionLabel != null) {
            mainPanel.remove(questionLabel);
            questionLabel = null;
        }
        if (theoreticalButton != null) {
            mainPanel.remove(theoreticalButton);
            mainPanel.remove(practicalButton);
            theoreticalButton = null;
            practicalButton = null;
        }
        if (inputField != null) {
            mainPanel.remove(inputField);
        }
        if (submitButton != null) {
            mainPanel.remove(submitButton);
        }
        if (continueButton != null) {
            mainPanel.remove(continueButton);
        }

        inputField = new JTextField();
        inputField.setBounds(700, 200, 200, 30);
        mainPanel.add(inputField);

        submitButton = new JButton("Submit");
        submitButton.setBounds(700, 240, 100, 30);
        submitButton.addActionListener(e -> processInput());
        mainPanel.add(submitButton);

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showContinueButton() {
        if (questionLabel != null) {
            mainPanel.remove(questionLabel);
            questionLabel = null;
        }
        if (theoreticalButton != null) {
            mainPanel.remove(theoreticalButton);
            mainPanel.remove(practicalButton);
            theoreticalButton = null;
            practicalButton = null;
        }
        if (inputField != null) {
            mainPanel.remove(inputField);
        }
        if (submitButton != null) {
            mainPanel.remove(submitButton);
        }
        if (continueButton != null) {
            mainPanel.remove(continueButton);
        }

        continueButton = new JButton("Continue");
        continueButton.setBounds(700, 200, 100, 30);
        continueButton.addActionListener(e -> showProblemCategorySelection());
        mainPanel.add(continueButton);

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void removeHeatmapPanel() {
        if (heatmapPanel != null) {
            mainPanel.remove(heatmapPanel);
            heatmapPanel = null;
            mainPanel.revalidate();
            mainPanel.repaint();
        }
    }

    private void handleProblemCategorySelection() {
        if (theoreticalButton.isSelected()) {
            outputArea.setText("Problem category is theoretical\n");
            showProblemTypeSelection();
        } else if (practicalButton.isSelected()) {
            outputArea.setText("Problem category is practical\n");
            choosingProblemCategory = false;
            currentProblemType = -2;
            step = 1;
            for (Component comp : mainPanel.getComponents()) {
                if (!(comp instanceof JScrollPane)) {
                    mainPanel.remove(comp);
                }
            }
            JScrollPane scrollPane = new JScrollPane(outputArea);
            scrollPane.setBounds(50, 50, 600, 400);
            mainPanel.add(scrollPane);
            outputArea.append("Enter grid size n (min 2):\n");
            showInputField();
        } else {
            outputArea.setText("Please select a problem category.\n");
            for (Component comp : mainPanel.getComponents()) {
                if (!(comp instanceof JScrollPane)) {
                    mainPanel.remove(comp);
                }
            }
            JScrollPane scrollPane = new JScrollPane(outputArea);
            scrollPane.setBounds(50, 50, 600, 400);
            mainPanel.add(scrollPane);
            mainPanel.add(questionLabel);
            mainPanel.add(theoreticalButton);
            mainPanel.add(practicalButton);
            mainPanel.add(submitButton);
            mainPanel.revalidate();
            mainPanel.repaint();
        }
    }

    private void processInput() {
        String input = inputField.getText().trim();
        try {
            if (choosingProblemCategory) {
                outputArea.append("Please use the radio buttons to select a category.\n");
                showInputField();
            } else if (currentProblemType == 1) {
                handleEquation(input);
            } else if (currentProblemType == 2) {
                handleSystemOfEquations(input);
            } else if (currentProblemType == 3) {
                handleCoordinates(input);
            } else if (currentProblemType == 4) {
                handleIntegration(input);
            } else if (currentProblemType == -2) {
                handlePracticalProblem(input);
            } else {
                outputArea.append("Invalid state. Please restart.\n");
                showContinueButton();
            }
        } catch (NumberFormatException e) {
            outputArea.append("Invalid input: Please enter a valid number\n");
            showInputField();
        } catch (IllegalArgumentException | IllegalStateException | ArithmeticException e) {
            outputArea.append("Error: " + e.getMessage() + "\n");
            showContinueButton();
        }

        inputField.setText("");
    }

    private void handleEquation(String input) {
        if (step == 1) {
            degree = Integer.parseInt(input);
            if (degree < 0) {
                throw new IllegalArgumentException("Degree must be non-negative");
            }
            outputArea.append("Polynomial degree is " + degree + "\n");
            polynomial = RandomPolynomialGenerator.generateRandomPolynomial(degree);
            outputArea.append("Generated polynomial: f(x) = " + polynomial + "\n");
            outputArea.append("Do you want to enter the interval [a, b]? (y/n):\n");
            step = 2;
            showInputField();
        } else if (step == 2) {
            String choice = input.toLowerCase();
            if (choice.equals("y")) {
                outputArea.append("Interval choice is [a, b]\n");
                outputArea.append("Enter value a:\n");
                step = 3;
                showInputField();
            } else if (choice.equals("n")) {
                outputArea.append("Interval choice is initial guess\n");
                outputArea.append("Enter initial guess x0:\n");
                step = 6;
                showInputField();
            } else {
                outputArea.append("Invalid choice. Please enter 'y' or 'n'.\n");
                showInputField();
            }
        } else if (step == 3) {
            a = Double.parseDouble(input);
            outputArea.append("Value a is " + a + "\n");
            outputArea.append("Enter value b:\n");
            step = 4;
            showInputField();
        } else if (step == 4) {
            b = Double.parseDouble(input);
            outputArea.append("Value b is " + b + "\n");
            outputArea.append("Enter error epsilon:\n");
            step = 5;
            showInputField();
        } else if (step == 5) {
            epsilon = Double.parseDouble(input);
            if (epsilon <= 0) {
                throw new IllegalArgumentException("Epsilon must be greater than 0");
            }
            outputArea.append("Epsilon is " + epsilon + "\n");
            double root = BisectionAlgorithm.findRoot(polynomial, a, b, epsilon);
            outputArea.append(String.format("Root x = %.6f with f(x) = %.6f%n", root, polynomial.evaluate(root)));
            outputArea.append("Press Continue to solve another problem.\n");
            showContinueButton();
        } else if (step == 6) {
            x0 = Double.parseDouble(input);
            outputArea.append("Initial guess x0 is " + x0 + "\n");
            outputArea.append("Enter error epsilon:\n");
            step = 7;
            showInputField();
        } else if (step == 7) {
            epsilon = Double.parseDouble(input);
            if (epsilon <= 0) {
                throw new IllegalArgumentException("Epsilon must be greater than 0");
            }
            outputArea.append("Epsilon is " + epsilon + "\n");
            double root = NewtonAlgorithm.findRoot(polynomial, x0, epsilon, 1000);
            outputArea.append(String.format("Root x = %.6f with f(x) = %.6f%n", root, polynomial.evaluate(root)));
            outputArea.append("Press Continue to solve another problem.\n");
            showContinueButton();
        }
    }

    private void handleSystemOfEquations(String input) {
        if (step == 1) {
            numEquations = Integer.parseInt(input);
            if (numEquations <= 0) {
                throw new IllegalArgumentException("Number of equations must be positive");
            }
            outputArea.append("Number of equations is " + numEquations + "\n");
            outputArea.append("Enter the number of variables (degree):\n");
            step = 2;
            showInputField();
        } else if (step == 2) {
            numVariables = Integer.parseInt(input);
            if (numVariables <= 0) {
                throw new IllegalArgumentException("Number of variables must be positive");
            }
            outputArea.append("Number of variables is " + numVariables + "\n");
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
            outputArea.append("Solution:\n");
            for (int i = 0; i < solution.length; i++) {
                outputArea.append(String.format("x%d = %.6f%n", i + 1, solution[i]));
            }
            outputArea.append("Classes used: LinearSystem, RandomLinearSystemGenerator, " + methodUsed + "\n");
            outputArea.append("Press Continue to solve another problem.\n");
            showContinueButton();
        }
    }

    private void handleCoordinates(String input) {
        numCoordinates = Integer.parseInt(input);
        if (numCoordinates <= 0) {
            throw new IllegalArgumentException("Number of coordinates must be positive");
        }
        outputArea.append("Number of coordinates is " + numCoordinates + "\n");
        double[][] coordinates = CoordinateGenerator.generateCoordinates(numCoordinates);
        outputArea.append("Generated coordinates:\n");
        for (int i = 0; i < coordinates.length; i++) {
            outputArea.append(String.format("(%.6f, %.6f)%n", coordinates[i][0], coordinates[i][1]));
        }
        double[] coefficients = LagrangeInterpolation.interpolate(coordinates);
        outputArea.append("Lagrange interpolation polynomial coefficients (from lowest to highest degree):\n");
        for (int i = 0; i < coefficients.length; i++) {
            outputArea.append(String.format("a%d = %.6f%n", i, coefficients[i]));
        }
        outputArea.append("Classes used: CoordinateGenerator, LagrangeInterpolation\n");
        outputArea.append("Press Continue to solve another problem.\n");
        showContinueButton();
    }

    private void handleIntegration(String input) {
        if (step == 1) {
            a = Double.parseDouble(input);
            outputArea.append("Bound a is " + a + "\n");
            outputArea.append("Enter upper bound b:\n");
            step = 2;
            showInputField();
        } else if (step == 2) {
            b = Double.parseDouble(input);
            outputArea.append("Bound b is " + b + "\n");
            outputArea.append("Enter max iterations:\n");
            step = 3;
            showInputField();
        } else if (step == 3) {
            maxIterations = Integer.parseInt(input);
            outputArea.append("Max iterations is " + maxIterations + "\n");
            RombergIntegration.Function f = x -> x * x + 2 * x + 1;
            double result = RombergIntegration.integrate(f, a, b, maxIterations);
            outputArea.append(String.format("Integral from %.2f to %.2f = %.6f%n", a, b, result));
            outputArea.append("Classes used: RombergIntegration\n");
            outputArea.append("Press Continue to solve another problem.\n");
            showContinueButton();
        }
    }

    private void handlePracticalProblem(String input) {
        if (step == 1) {
            gridSize = Integer.parseInt(input);
            if (gridSize < 2) {
                throw new IllegalArgumentException("Grid size must be at least 2");
            }
            outputArea.append("Grid size is " + gridSize + "\n");
            outputArea.append("Enter top boundary temperature:\n");
            step = 2;
            showInputField();
        } else if (step == 2) {
            topBoundary = Double.parseDouble(input);
            outputArea.append("Top boundary temperature is " + topBoundary + "\n");
            outputArea.append("Enter bottom boundary temperature:\n");
            step = 3;
            showInputField();
        } else if (step == 3) {
            bottomBoundary = Double.parseDouble(input);
            outputArea.append("Bottom boundary temperature is " + bottomBoundary + "\n");
            outputArea.append("Enter left boundary temperature:\n");
            step = 4;
            showInputField();
        } else if (step == 4) {
            leftBoundary = Double.parseDouble(input);
            outputArea.append("Left boundary temperature is " + leftBoundary + "\n");
            outputArea.append("Enter right boundary temperature:\n");
            step = 5;
            showInputField();
        } else if (step == 5) {
            rightBoundary = Double.parseDouble(input);
            outputArea.append("Right boundary temperature is " + rightBoundary + "\n");
            outputArea.append("Enter max iterations:\n");
            step = 6;
            showInputField();
        } else if (step == 6) {
            maxIterations = Integer.parseInt(input);
            if (maxIterations <= 0) {
                throw new IllegalArgumentException("Max iterations must be positive");
            }
            outputArea.append("Max iterations is " + maxIterations + "\n");
            outputArea.append("Enter epsilon:\n");
            step = 7;
            showInputField();
        } else if (step == 7) {
            epsilon = Double.parseDouble(input);
            if (epsilon <= 0) {
                throw new IllegalArgumentException("Epsilon must be positive");
            }
            outputArea.append("Epsilon is " + epsilon + "\n");
            outputArea.append("Running heat distribution simulation...\n");

            HeatSimulation sim = new HeatSimulation(gridSize, topBoundary, bottomBoundary, leftBoundary, rightBoundary,
                                                    maxIterations, epsilon);
            sim.runSimulation();
            HeatGrid resultGrid = sim.getResultGrid();

            outputArea.append("Temperature Distribution Result:\n");
            StringBuilder gridText = new StringBuilder();
            gridText.append("Heat Grid (").append(gridSize).append(" x ").append(gridSize).append("):\n");
            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    gridText.append(String.format("%8.2f", resultGrid.getValue(i, j)));
                }
                gridText.append("\n");
            }
            outputArea.append(gridText.toString());

            int pixelSize = Math.max(500 / gridSize, 5);
            ResultExporter exporter = new ResultExporter(resultGrid, pixelSize);
            heatmapPanel = exporter.createHeatmapPanel();
            heatmapPanel.setBounds(700, 50, gridSize * pixelSize, gridSize * pixelSize);
            mainPanel.add(heatmapPanel);

            outputArea.append("Heatmap displayed. Press Continue to solve another problem.\n");
            showContinueButton();

            mainPanel.revalidate();
            mainPanel.repaint();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainGUI gui = new MainGUI();
            gui.setVisible(true);
        });
    }
}