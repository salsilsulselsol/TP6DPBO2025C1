import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.io.InputStream;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    // Frame dimensions
    final int frameWidth = 360;
    final int frameHeight = 640;

    // Images
    Image backgroundImage;
    Image birdImage;
    Image lowerPipeImage;
    Image upperPipeImage;

    // Game states
    enum GameState { START_SCREEN, PLAYING, GAME_OVER }
    GameState gameState = GameState.START_SCREEN;

    // Player
    int playerStartPosX = frameWidth / 5;
    int playerStartPosY = frameHeight / 2;
    int playerWidth = 34;
    int playerHeight = 24;
    Player player;

    // Pipes
    int pipeStartPosX = frameWidth;
    int pipeWidth = 64;
    ArrayList<Pipe> pipes;
    int lastGapY = frameHeight / 2;
    Random random = new Random();

    // Game variables
    Timer gameLoop;
    Timer pipesCountdown;
    int gravity = 1;
    int score = 0;
    Font pixelFont;

    public FlappyBird() {
        setPreferredSize(new Dimension(frameWidth, frameHeight));
        setFocusable(true);
        addKeyListener(this);
        setLayout(null);

        // Load pixel font
        try {
            InputStream is = getClass().getResourceAsStream("/assets/fonts/PressStart2P-Regular.ttf");
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(12f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(pixelFont);
        } catch (Exception e) {
            pixelFont = new Font("Arial", Font.BOLD, 12);
        }

        // Load images
        try {
            backgroundImage = new ImageIcon(getClass().getResource("/assets/background.png")).getImage();
            birdImage = new ImageIcon(getClass().getResource("/assets/bird.png")).getImage();
            lowerPipeImage = new ImageIcon(getClass().getResource("/assets/lowerPipe.png")).getImage();
            upperPipeImage = new ImageIcon(getClass().getResource("/assets/upperPipe.png")).getImage();
        } catch (Exception e) {
            System.out.println("Error loading images: " + e.getMessage());
        }

        // Initialize player
        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage);
        pipes = new ArrayList<>();

        // Initialize timers
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();

        pipesCountdown = new Timer(1500, e -> {
            if (gameState == GameState.PLAYING) {
                placePipes();
            }
        });
        pipesCountdown.start();
    }

    private void placePipes() {
        int gapSize = 200;
        int minGapY = 150;
        int maxGapY = frameHeight - gapSize - 150;

        // Calculate new gap position
        int gapY;
        if (pipes.isEmpty()) {
            gapY = frameHeight / 2;
        } else {
            int direction = random.nextBoolean() ? 1 : -1;
            int change = 90 + random.nextInt(90);
            gapY = lastGapY + (direction * change);
            gapY = Math.max(minGapY, Math.min(gapY, maxGapY));
        }
        lastGapY = gapY;

        // Upper pipe
        pipes.add(new Pipe(pipeStartPosX, 0, pipeWidth, gapY, upperPipeImage));

        // Lower pipe
        pipes.add(new Pipe(pipeStartPosX, gapY + gapSize, pipeWidth, frameHeight - gapY - gapSize, lowerPipeImage
        ));
    }

    public void draw(Graphics g) {
        // Draw background
        g.drawImage(backgroundImage, 0, 0, frameWidth, frameHeight, null);

        // Draw pipes
        for (Pipe pipe : pipes) {
            g.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight(), null);
        }

        // Draw player
        g.drawImage(player.getImage(), player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight(), null);

        // Draw score
        g.setFont(pixelFont.deriveFont(20f));
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 20, 40);

        // Draw game states
        switch (gameState) {
            case START_SCREEN:
                drawStartScreen(g);
                break;
            case GAME_OVER:
                drawGameOver(g);
                break;
        }
    }

    private void drawStartScreen(Graphics g) {
        // Semi-transparent overlay
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, frameWidth, frameHeight);

        // Title
        g.setFont(pixelFont.deriveFont(30f));
        g.setColor(Color.WHITE);
        g.drawString("FLAPPY BIRD", frameWidth/2 - 165, frameHeight/2 - 50);

        // Instruction
        g.setFont(pixelFont.deriveFont(15f));
        g.drawString("PRESS SPACE TO START", frameWidth/2 - 150, frameHeight/2 + 50);
    }

    private void drawGameOver(Graphics g) {
        // Semi-transparent overlay
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, frameWidth, frameHeight);

        // Game over text (centered)
        g.setFont(pixelFont.deriveFont(30f));
        g.setColor(Color.RED);
        drawCenteredString(g, "GAME OVER", frameHeight/2 - 60);

        // Score (centered)
        g.setColor(Color.WHITE);
        drawCenteredString(g, "SCORE: " + score, frameHeight/2);

        // Restart instruction (centered)
        g.setFont(pixelFont.deriveFont(15f));
        drawCenteredString(g, "PRESS R TO RESTART", frameHeight/2 + 60);
    }

    private void drawCenteredString(Graphics g, String text, int y) {
        FontMetrics fm = g.getFontMetrics();
        int x = (frameWidth - fm.stringWidth(text)) / 2;
        g.drawString(text, x, y);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void move() {
        if (gameState != GameState.PLAYING) return;

        // Apply gravity
        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());

        // Check boundaries
        if (player.getPosY() <= 0) {
            player.setPosY(0);
            player.setVelocityY(0);
        }

        if (player.getPosY() + player.getHeight() >= frameHeight) {
            gameOver();
            return;
        }

        // Move pipes and check collisions
        Iterator<Pipe> it = pipes.iterator();
        while (it.hasNext()) {
            Pipe pipe = it.next();
            pipe.setPosX(pipe.getPosX() - pipe.getVelocityX());

            // Scoring
            if (!pipe.isPassed() && pipe.getPosX() + pipe.getWidth() < player.getPosX() && pipe.getImage() == upperPipeImage) {
                pipe.setPassed(true);
                score++;
            }

            // Remove off-screen pipes
            if (pipe.getPosX() + pipe.getWidth() < 0) {
                it.remove();
            }

            // Collision detection
            if (collision(player, pipe)) {
                gameOver();
                return;
            }
        }
    }

    private boolean collision(Player player, Pipe pipe) {
        Rectangle playerRect = new Rectangle(
                player.getPosX(), player.getPosY(),
                player.getWidth() - 10, player.getHeight() - 10
        );

        Rectangle pipeRect = new Rectangle(
                pipe.getPosX(), pipe.getPosY(),
                pipe.getWidth() - 10, pipe.getHeight()
        );

        return playerRect.intersects(pipeRect);
    }

    private void gameOver() {
        gameState = GameState.GAME_OVER;
        pipesCountdown.stop();
    }

    private void restartGame() {
        gameState = GameState.START_SCREEN;
        score = 0;
        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage);
        pipes.clear();
        pipesCountdown.start();
    }

    private void startGame() {
        gameState = GameState.PLAYING;
        player.setVelocityY(-10); // Initial jump
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (gameState) {
            case START_SCREEN:
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    startGame();
                }
                break;
            case PLAYING:
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    player.setVelocityY(-10);
                }
                break;
            case GAME_OVER:
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    restartGame();
                }
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}