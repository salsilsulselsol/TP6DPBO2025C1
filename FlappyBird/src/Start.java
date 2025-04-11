import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;

public class Start extends JFrame {
    private JPanel mainPanel;
    private JButton startButton;
    private JLabel titleLabel;
    private JLabel instructionLabel;
    private JLabel birdIconLabel;
    private boolean up = true;
    private Font pixelFont;
    private Image backgroundImage;

    public Start() {
        // Initialize components
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    // Fallback background
                    g.setColor(new Color(135, 206, 235));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };

        // Setup frame
        setTitle("Flappy Bird");
        setSize(360, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Load resources
        loadResources();
        setupComponents();
        setupLayout();
        startBirdAnimation();

        setContentPane(mainPanel);
        setVisible(true);
    }

    private void loadResources() {
        // Load background
        try {
            backgroundImage = new ImageIcon(getClass().getResource("/assets/background.png")).getImage();
        } catch (Exception e) {
            System.err.println("Error loading background: " + e.getMessage());
        }

        // Load font
        try {
            InputStream is = getClass().getResourceAsStream("/assets/fonts/PressStart2P-Regular.ttf");
            if (is != null) {
                pixelFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(24f);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(pixelFont);
            }
        } catch (Exception e) {
            System.err.println("Error loading font: " + e.getMessage());
            pixelFont = new Font("Arial", Font.BOLD, 24);
        }

        // Load bird icon
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/assets/bird.png"));
            Image scaled = icon.getImage().getScaledInstance(60, 45, Image.SCALE_SMOOTH);
            birdIconLabel = new JLabel(new ImageIcon(scaled));
        } catch (Exception e) {
            birdIconLabel = new JLabel("(^>^)");
            birdIconLabel.setFont(pixelFont.deriveFont(20f));
            birdIconLabel.setForeground(Color.WHITE);
        }
    }

    private void setupComponents() {
        mainPanel.setLayout(null);

        // Title label
        titleLabel = new JLabel("FLAPPY BIRD");
        titleLabel.setFont(pixelFont.deriveFont(30f));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Instruction label
        instructionLabel = new JLabel("PRESS SPACE OR CLICK START");
        instructionLabel.setFont(pixelFont.deriveFont(12f));
        instructionLabel.setForeground(Color.YELLOW);
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Start button
        startButton = new JButton("START");
        startButton.setFont(pixelFont.deriveFont(14f));
        startButton.setForeground(Color.BLACK);
        startButton.setBackground(new Color(255, 215, 0));
        startButton.setFocusPainted(false);
        startButton.setBorder(BorderFactory.createRaisedBevelBorder());
        startButton.addActionListener(e -> startGame());

        // Key listener
        mainPanel.setFocusable(true);
        mainPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    startGame();
                }
            }
        });
    }

    private void setupLayout() {
        // Set bounds (centered)
        titleLabel.setBounds(-5, 100, 360, 50);
        birdIconLabel.setBounds(150, 220, 60, 45);
        instructionLabel.setBounds(-5, 350, 360, 30);
        startButton.setBounds(125, 400, 100, 40);

        // Add components
        mainPanel.add(titleLabel);
        mainPanel.add(birdIconLabel);
        mainPanel.add(instructionLabel);
        mainPanel.add(startButton);
    }

    private void startBirdAnimation() {
        Timer timer = new Timer(150, e -> {
            int y = birdIconLabel.getY() + (up ? -3 : 3);
            birdIconLabel.setLocation(birdIconLabel.getX(), y);

            if (y < 200) up = false;
            if (y > 280) up = true;
        });
        timer.start();
    }

    private void startGame() {
        dispose();
        SwingUtilities.invokeLater(() -> new App().startGame());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Start());
    }
}