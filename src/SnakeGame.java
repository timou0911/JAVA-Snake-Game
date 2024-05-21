import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    // bomb
    boolean bombMode = false;
    ArrayList<Tile> snakeBomb;
    ArrayList<Tile> snake2Bomb;

    //debug
    boolean debug1 = false;
    boolean debug2 = false;

    // panel
    int boardWidth;
    int boardHeight;
    int tileSize = 25;

    int snakeSpeed;
    // snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    //snake2
    Tile snake2Head;
    ArrayList<Tile> snake2Body;

    // food
    Tile food;
    Random foodRandomGenerate;
    ImageIcon foodIcon = new ImageIcon("JAVA.png");
    Image foodImage = foodIcon.getImage();
    ImageIcon headIcon1 = new ImageIcon("Snake-head1.jpg");
    Image headImage1 = headIcon1.getImage();
    ImageIcon snakeBodyIcon1= new ImageIcon("snakeBody1.jpg");
    Image snakeBodyImage1 = snakeBodyIcon1.getImage();
    ImageIcon headIcon2 = new ImageIcon("Snake-head2.jpg");
    Image headImage2 = headIcon2.getImage();
    ImageIcon snakeBodyIcon2= new ImageIcon("snakeBody2.jpg");
    Image snakeBodyImage2 = snakeBodyIcon2.getImage();

    // game logic
    Timer gameLoop;
    int velocityX;
    int velocityY;
    int snake2velocityX;
    int snake2velocityY;
    boolean gameOver = false;

    // sound
    Sound sound = new Sound();

    // buttons
    Buttons restartButton, quitButton;
    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setLayout(null);

        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.GRAY);
        addKeyListener(this);
        setFocusable(true);

        //snake
        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();
        snakeSpeed = 100;

        //snake2
        snake2Head = new Tile(20, 20);
        snake2Body = new ArrayList<Tile>();

        food = new Tile(10, 10);
        foodRandomGenerate = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 1;

        snake2velocityX = 0;
        snake2velocityY = -1;

        gameLoop = new Timer(snakeSpeed, this);
        gameLoop.start();

        sound.playAndLoopBackgroundMusic();

        restartButton = new Buttons("Restart",this);
        restartButton.setVisible(false);

        quitButton = new Buttons("Quit",this);
        quitButton.setVisible(false);
    }
    public void restartGame() {
        snakeHead = new Tile(5, 5);
        snakeBody.clear();
        velocityX = 0;
        velocityY = 1;

        snake2Head = new Tile(20, 20);
        snake2Body.clear();
        snake2velocityX = 0;
        snake2velocityY = -1;

        snakeSpeed = 100;
        debug1 = false;
        debug2 = false;
        gameOver = false;

        placeFood();
        gameLoop.start();
        requestFocusInWindow();
        restartButton.setVisible(false);
        quitButton.setVisible(false);
        sound.playAndLoopBackgroundMusic();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // grid
        for (int i = 0; i < boardWidth / tileSize; ++i) {
            // (x1, y1, x2, y2)
            g.drawLine(i * tileSize, 0, i * tileSize, boardHeight);
            g.drawLine(0, i * tileSize, boardWidth, i * tileSize);
        }

        // food
        //g.setColor(Color.red);
        //g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);
        g.drawImage(foodImage, food.x * tileSize, food.y * tileSize, tileSize, tileSize, null);

        // snake head
        g.setColor(Color.green);
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);
        g.drawImage(headImage1, snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, null);

        // snake body
        for (int i = 0; i < snakeBody.size(); ++i) {
            Tile snakePart = snakeBody.get(i);
            g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
            g.drawImage(snakeBodyImage1, snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, null);
        }

        // snake2 head
        g.setColor(Color.ORANGE);
        g.fill3DRect(snake2Head.x * tileSize, snake2Head.y * tileSize, tileSize, tileSize, true);
        g.drawImage(headImage2, snake2Head.x * tileSize, snake2Head.y * tileSize, tileSize, tileSize, null);

        // snake2 body
        for (int i = 0; i < snake2Body.size(); ++i) {
            Tile snake2Part = snake2Body.get(i);
            g.fill3DRect(snake2Part.x * tileSize, snake2Part.y * tileSize, tileSize, tileSize, true);
            g.drawImage(snakeBodyImage2, snake2Part.x * tileSize, snake2Part.y * tileSize, tileSize, tileSize, null);
        }

        // score and fail screen
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) {
            drawGameOverScreen(g);

        } else {
            g.drawString("Score1: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
            g.drawString("Score2: " + String.valueOf(snake2Body.size()), tileSize - 16, 2*tileSize);
        }
    }

    public void placeFood() {
        food.x = foodRandomGenerate.nextInt(boardWidth / tileSize);
        food.y = foodRandomGenerate.nextInt(boardHeight / tileSize);
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move() {
        if (!debug1) snakeMovement(snakeHead, food, snakeBody, velocityX, velocityY, snake2Head, snake2Body);
        if (!debug2) snakeMovement(snake2Head, food, snake2Body, snake2velocityX, snake2velocityY, snakeHead, snakeBody);

        
    }

    public void snakeMovement(Tile snakeHead, Tile food, ArrayList<Tile> snakeBody, int velocityX, int velocityY, Tile snake2Head, ArrayList<Tile> snake2Body) {

         // snake eat food
         if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
            if (snakeSpeed > 80) {
                snakeSpeed -= 2;
                gameLoop.setDelay(snakeSpeed);
            }
        }

        // snake body
        for (int i = snakeBody.size() - 1; i >= 0; --i) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else {
                Tile previousSnakePart = snakeBody.get(i - 1);
                snakePart.x = previousSnakePart.x;
                snakePart.y = previousSnakePart.y;
            }
        }

        // snake head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        // game over condition
        // collision with itself
        for (int i = 0; i < snakeBody.size(); ++i) {
            Tile snakePart = snakeBody.get(i);
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }

        //collision with other snake's body
        for (int i = 0; i < snake2Body.size(); i++) {
            Tile snake2Part = snake2Body.get(i);
            if (collision(snakeHead, snake2Part)) {
                gameOver = true;
            }
        }

        //collision with other snake's head
        if (collision(snakeHead, snake2Head)) {
            gameOver = true;
        }

        if ((snakeBody.size() - snake2Body.size()) > 7) {
            gameOver = true;
        }

        if ((snake2Body.size() - snakeBody.size()) > 7) {
            gameOver = true;
        }


        if (snakeHead.x * tileSize < 0 || snakeHead.x * tileSize > boardWidth || snakeHead.y * tileSize < 0
                || snakeHead.y * tileSize > boardHeight) {
            gameOver = true;
        }
    }

    public void drawGameOverScreen(Graphics g) {
        g.setColor(new Color(0, 0, 0, 100));
        g.fillRect(0, 0, boardWidth, boardHeight);
        g.setFont(g.getFont().deriveFont(Font.BOLD, 110f));

        // shadow
        g.setColor(Color.BLACK);
        g.drawString("Game Over", 0, 295);

        // text
        g.setColor(Color.WHITE);
        g.drawString("Game Over", 5, 290);

        // score
        g.setFont(g.getFont().deriveFont(30f));
        g.drawString("Score: " + String.valueOf(snakeBody.size()), 235, 360);

        sound.stopBackgroundMusic();
        sound.playGameOverSound();

        restartButton.setVisible(true);
        quitButton.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();

        if (gameOver) {
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            debug1 = false;
            velocityX = 0;
            velocityY = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            debug1 = false;
            velocityX = 0;
            velocityY = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            debug1 = false;
            velocityX = 1;
            velocityY = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            debug1 = false;
            velocityX = -1;
            velocityY = 0;
        }
        
        if (e.getKeyCode() == KeyEvent.VK_W && snake2velocityY != 1) {
            debug2 = false;
            snake2velocityX = 0;
            snake2velocityY = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_S && snake2velocityY != -1) {
            debug2 = false;
            snake2velocityX = 0;
            snake2velocityY = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_D && snake2velocityX != -1) {
            debug2 = false;
            snake2velocityX = 1;
            snake2velocityY = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_A && snake2velocityX != 1) {
            debug2 = false;
            snake2velocityX = -1;
            snake2velocityY = 0;
        }

        // bomb mode ? f

        //for debug
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            debug1 = true;
            debug2 = true;
            velocityX = 0;
            velocityY = 0;
            snake2velocityX = 0;
            snake2velocityY = 0;
            gameOver = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}