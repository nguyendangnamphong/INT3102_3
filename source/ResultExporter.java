import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ResultExporter {
    private HeatGrid grid; // Lưới chứa kết quả nhiệt độ
    private int pixelSize; // Kích thước mỗi ô lưới (pixel)

    // Constructor: Khởi tạo với lưới và kích thước ô
    public ResultExporter(HeatGrid grid, int pixelSize) {
        if (grid == null) {
            throw new IllegalArgumentException("HeatGrid cannot be null");
        }
        if (pixelSize <= 0) {
            throw new IllegalArgumentException("Pixel size must be positive");
        }
        this.grid = grid;
        this.pixelSize = pixelSize;
    }

    // Tạo panel chứa heatmap
    public JPanel createHeatmapPanel() {
        return new HeatmapPanel();
    }

    // Lớp nội bộ để vẽ heatmap
    private class HeatmapPanel extends JPanel {
        public HeatmapPanel() {
            int n = grid.getSize();
            setPreferredSize(new Dimension(n * pixelSize, n * pixelSize));
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int n = grid.getSize();

            // Tìm giá trị nhiệt độ min và max để chuẩn hóa màu
            double minTemp = Double.MAX_VALUE;
            double maxTemp = Double.MIN_VALUE;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    double temp = grid.getValue(i, j);
                    minTemp = Math.min(minTemp, temp);
                    maxTemp = Math.max(maxTemp, temp);
                }
            }
            double tempRange = maxTemp - minTemp;
            if (tempRange == 0) tempRange = 1.0; // Tránh chia cho 0

            // Vẽ từng ô lưới trực tiếp lên Graphics
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    double temp = grid.getValue(i, j);
                    // Chuẩn hóa nhiệt độ thành giá trị [0, 1]
                    double normalized = (temp - minTemp) / tempRange;
                    // Tạo màu từ đỏ (nóng) đến xanh dương (lạnh)
                    Color color = getColorForTemperature(normalized);
                    g2d.setColor(color);
                    g2d.fillRect(j * pixelSize, i * pixelSize, pixelSize, pixelSize);
                }
            }

            // Vẽ lưới phân tách
            g2d.setColor(Color.BLACK);
            for (int i = 0; i <= n; i++) {
                g2d.drawLine(0, i * pixelSize, n * pixelSize, i * pixelSize);
                g2d.drawLine(i * pixelSize, 0, i * pixelSize, n * pixelSize);
            }
        }

        // Tạo màu dựa trên nhiệt độ chuẩn hóa [0, 1]
        private Color getColorForTemperature(double normalized) {
            // Từ xanh dương (0) -> xanh lá -> vàng -> đỏ (1)
            float h = (float) (0.666 - 0.666 * normalized); // Hue: 0.666 (xanh dương) đến 0 (đỏ)
            float s = 1.0f; // Saturation: 100%
            float b = 1.0f; // Brightness: 100%
            return Color.getHSBColor(h, s, b);
        }
    }

    // Hàm main để kiểm tra độc lập
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Tạo HeatSimulation mẫu
                int n = 4;
                double top = 100.0, bottom = 0.0, left = 50.0, right = 50.0;
                int maxIterations = 100;
                double epsilon = 1e-6;
                System.out.println("Creating HeatSimulation with n = " + n + ", top = " + top + ", bottom = " + bottom +
                                  ", left = " + left + ", right = " + right);
                HeatSimulation sim = new HeatSimulation(n, top, bottom, left, right, maxIterations, epsilon);
                sim.runSimulation();
                HeatGrid grid = sim.getResultGrid();

                // Tạo ResultExporter và hiển thị heatmap
                ResultExporter exporter = new ResultExporter(grid, 100); // Tăng pixelSize để rõ hơn
                JFrame frame = new JFrame("Heat Distribution Heatmap");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(exporter.createHeatmapPanel());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                // In lưới text để đối chiếu
                System.out.println("\nTemperature Distribution (Text):");
                grid.printGrid();

            } catch (IllegalArgumentException | IllegalStateException | ArithmeticException e) {
                System.out.println("Error: " + e.getMessage());
            }
        });
    }
}