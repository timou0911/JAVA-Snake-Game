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
    // result 1 : snake1 win, 2 : snake2 win, 0 : Tie, 3 : not set yet
    int result = 3;

    // bomb
    boolean bombMode = false;
    ArrayList<Tile> snakeBomb = new ArrayList<Tile>();
    ArrayList<Tile> snake2Bomb = new ArrayList<Tile>();
    ImageIcon snakeBombIcon = new ImageIcon("Images/green-bomb.png");
    Image snakeBombImage = snakeBombIcon.getImage();
    ImageIcon snake2BombIcon = new ImageIcon("Images/yellow-bomb.png");
    Image snake2BombImage = snake2BombIcon.getImage();

    //debug
    boolean debug1 = false;
    boolean debug2 = false;

    // panel
    int boardWidth;
    int boardHeight;

    int tileSize = 25;
    int snakeSpeed;
    Tile snakeHead;
    ArrayList<Tile> snakeBody;
    Tile snake2Head;
    ArrayList<Tile> snake2Body;

    Tile food;
    Random foodRandomGenerate;
    ImageIcon foodIcon = new ImageIcon("Images/JAVA.png");
    Image foodImage = foodIcon.getImage();
    ImageIcon headIcon = new ImageIcon("Images/Snake-head1.jpg");
    Image headImage = headIcon.getImage();
    ImageIcon bodyIcon = new ImageIcon("Images/SnakeBody1.jpg");
    Image bodyImage = bodyIcon.getImage();
    ImageIcon head2Icon = new ImageIcon("Images/Snake-head2.jpg");
    Image head2Image = head2Icon.getImage();
    ImageIcon body2Icon = new ImageIcon("Images/SnakeBody2.jpg");
    Image body2Image = body2Icon.getImage();
    ImageIcon backgroundIcon = new ImageIcon("Images/snake-game-background.png");
    Image backgroundImage = backgroundIcon.getImage();
    ImageIcon greenBombIcon = new ImageIcon("Images/green-bomb.png");
    Image greenBombImage = greenBombIcon.getImage();
    ImageIcon yellowBombIcon = new ImageIcon("Images/yellow-bomb.png");
    Image yellowBombImage = yellowBombIcon.getImage();
    // game logic
    Timer gameLoop;
    int velocityX;
    int velocityY;
    int snake2velocityX;
    int snake2velocityY;
    boolean gameOver = false;
    boolean gameStarted = false;

    // sound
    Sound sound = new Sound();

    // buttons
    Buttons startGameButton, startBombGameButton, restartButton, quitButton;
    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setLayout(null);

        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.GRAY);
        addKeyListener(this);
        setFocusable(true);

        //snake1
        snakeHead = new Tile(20, 20);
        snakeBody = new ArrayList<Tile>();
        snakeSpeed = 100;
        velocityX = 0;
        velocityY = -1;

        //snake2
        snake2Head = new Tile(5, 5);
        snake2Body = new ArrayList<Tile>();
        snake2velocityX = 0;
        snake2velocityY = 1;

        food = new Tile(10, 10);
        foodRandomGenerate = new Random();
        placeFood();

        gameLoop = new Timer(snakeSpeed, this);
        gameLoop.start();

        sound.playAndLoopBackgroundMusic();

        startGameButton = new Buttons("Normal",this);
        startGameButton.setVisible(true);

        startBombGameButton = new Buttons("Bomb",this);
        startBombGameButton.setVisible(true);

        restartButton = new Buttons("Restart",this);
        restartButton.setVisible(false);

        quitButton = new Buttons("Quit",this);
        quitButton.setVisible(false);
    }
    public void restartGame() {
        result = 3;
        //bombMode = false;

        snakeHead = new Tile(20, 20);
        snakeBody.clear();
        snakeBomb.clear();
        velocityX = 0;
        velocityY = -1;

        snake2Head = new Tile(5, 5);
        snake2Body.clear();
        snake2Bomb.clear();
        snake2velocityX = 0;
        snake2velocityY = 1;

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

        gameStarted = false;
        startGameButton.setVisible(true);
        startBombGameButton.setVisible(true);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(!gameStarted){
            repaint();
            g.drawImage(backgroundImage, 0, 0, boardWidth, boardHeight, null);
        }
        else
            draw(g);
    }

    public void draw(Graphics g) {
        // grid
        for (int i = 0; i < boardWidth / tileSize; ++i) {
            // (x1, y1, x2, y2)
            g.drawLine(i * tileSize, 0, i * tileSize, boardHeight);
            g.drawLine(0, i * tileSize, boardWidth, i * tileSize);
        }

        // score and fail screen
        g.setColor(Color.ORANGE);
        g.setFont(new Font("Arial", Font.PLAIN, 16));

        if (gameOver) {
            System.out.println("test");
            drawGameOverScreen(g);
        } else {
            g.drawString("Score1: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
            g.drawString("Score2: " + String.valueOf(snake2Body.size()), tileSize - 16, 2*tileSize);
            // food
            g.drawImage(foodImage, food.x * tileSize, food.y * tileSize, tileSize, tileSize, null);
            // snake1 head
            g.setColor(Color.green);
            g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);
            g.drawImage(headImage, snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, null);
            // snake1 body
            for (int i = 0; i < snakeBody.size(); ++i) {
                Tile snakePart = snakeBody.get(i);
                g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
                g.drawImage(bodyImage, snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, null);
            }
            // snake1 bomb
            for (int i = 0; i < snakeBomb.size(); ++i) {
                Tile snakePart = snakeBomb.get(i);
                g.drawImage(snakeBombImage, snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, null);
            }

            // snake2 head
            g.setColor(Color.ORANGE);
            g.fill3DRect(snake2Head.x * tileSize, snake2Head.y * tileSize, tileSize, tileSize, true);
            g.drawImage(head2Image, snake2Head.x * tileSize, snake2Head.y * tileSize, tileSize, tileSize, null);
            // snake2 body
            for (int i = 0; i < snake2Body.size(); ++i) {
                Tile snake2Part = snake2Body.get(i);
                g.fill3DRect(snake2Part.x * tileSize, snake2Part.y * tileSize, tileSize, tileSize, true);
                g.drawImage(body2Image, snake2Part.x * tileSize, snake2Part.y * tileSize, tileSize, tileSize, null);
            }
            // snake2 bomb
            for (int i = 0; i < snake2Bomb.size(); ++i) {
                Tile snake2Part = snake2Bomb.get(i);
                g.drawImage(snake2BombImage, snake2Part.x * tileSize, snake2Part.y * tileSize, tileSize, tileSize, null);
            }
        }
    }

    public void placeFood() {
        int temp_x;
        int temp_y;
        while (true) {
            temp_x = foodRandomGenerate.nextInt(boardWidth / tileSize);
            temp_y = foodRandomGenerate.nextInt(boardHeight / tileSize);
            boolean flag = true;
            outerLoop:
            for (int i = 0; i < snake2Bomb.size(); ++i) {
                for (int j = 0; j < snakeBomb.size(); ++j) {
                    Tile snake2Part = snake2Bomb.get(i);
                    Tile snakePart = snakeBomb.get(j);
                    if (snake2Part.x == temp_x || snake2Part.y == temp_y || snakePart.x == temp_x || snakePart.y == temp_y) {
                        flag = false;
                        break outerLoop;
                    }
                }
            }
            if (flag) break;
        }

        food.x = temp_x;
        food.y = temp_y;
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move() {
        if (!debug1) snakeMovement(snakeHead, food, snakeBody, velocityX, velocityY, snake2Head, snake2Body, snake2Bomb, 1);
        if (!debug2) snakeMovement(snake2Head, food, snake2Body, snake2velocityX, snake2velocityY, snakeHead, snakeBody, snakeBomb, 2);
    }

    public void snakeMovement(Tile snakeHead, Tile food, ArrayList<Tile> snakeBody, int velocityX, int velocityY, Tile snake2Head, ArrayList<Tile> snake2Body, ArrayList<Tile> snake2Bomb, int snakeId) {

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
                if(snakeId == 1) {
                    result = 2;
                }else if (snakeId == 2) {
                    result = 1;
                }else {
                    System.out.println("Error: wrong snake!");
                }
                gameOver = true;
                sound.stopBackgroundMusic();
                sound.playGameOverSound();
            }
        }

        //collision with other snake's body
        for (int i = 0; i < snake2Body.size(); i++) {
            Tile snake2Part = snake2Body.get(i);
            if (collision(snakeHead, snake2Part)) {
                if(snakeId == 1) {
                    result = 2;
                }else if (snakeId == 2) {
                    result = 1;
                }else {
                    System.out.println("Error: wrong snake!");
                }
                gameOver = true;
                sound.stopBackgroundMusic();
                sound.playGameOverSound();
            }
        }

        //collision with other snake's head
        if (collision(snakeHead, snake2Head)) {
            if(snakeId == 1) {
                result = 0;
            }else if (snakeId == 2) {
                result = 0;
            }else {
                System.out.println("Error: wrong snake!");
            }
            gameOver = true;
            sound.stopBackgroundMusic();
            sound.playGameOverSound();
        }

        //win by length
        if ((snakeBody.size() - snake2Body.size()) > 7) {
            if(snakeId == 1) {
                result = 1;
            }else if (snakeId == 2) {
                result = 2;
            }else {
                System.out.println("Error: wrong snake!");
            }
            gameOver = true;
            sound.stopBackgroundMusic();
            sound.playGameOverSound();
        }

        //lose by length
        if ((snake2Body.size() - snakeBody.size()) > 7) {
            if(snakeId == 1) {
                result = 2;
            }else if (snakeId == 2) {
                result = 1;
            }else {
                System.out.println("Error: wrong snake!");
            }
            gameOver = true;
            sound.stopBackgroundMusic();
            sound.playGameOverSound();
        }

        //collision with wall
        if (snakeHead.x * tileSize < 0 || snakeHead.x * tileSize > boardWidth || snakeHead.y * tileSize < 0
                || snakeHead.y * tileSize > boardHeight) {
            if(snakeId == 1) {
                result = 2;
            }else if (snakeId == 2) {
                result = 1;
            }else {
                System.out.println("Error: wrong snake!");
            }
            gameOver = true;
            sound.stopBackgroundMusic();
            sound.playGameOverSound();
        }

        // snake1 collision with snake2's bomb
        for (int i = 0; i < snake2Bomb.size(); i++) {
            Tile snake2Part = snake2Bomb.get(i);
            if (collision(snakeHead, snake2Part)) {
                if(snakeId == 1) {
                    result = 2;
                }else if (snakeId == 2) {
                    result = 1;
                }else {
                    System.out.println("Error: wrong snake!");
                }
                gameOver = true;
                sound.stopBackgroundMusic();
                sound.playGameOverSound();
            }
        }
    }

    public void drawGameOverScreen(Graphics g) {
        //check who wins
        if (result == 1) System.out.println("Snake1 win!");
        else if (result == 2) System.out.println("Snake2 win!");
        else if (result == 0) System.out.println("Tie!");
        else System.out.println("Error: result = " + result);


        g.setColor(new Color(0, 0, 0, 100));
        g.fillRect(0, 0, boardWidth, boardHeight);
        g.setFont(g.getFont().deriveFont(Font.BOLD, 110f));

        // shadow
        g.setColor(Color.BLACK);
        g.drawString("Game Over", 0, 295);

        // text
        g.setColor(Color.WHITE);
        g.drawString("Game Over", 5, 290);

        // button text
        g.setFont(g.getFont().deriveFont(Font.BOLD, 20f));
        g.setColor(Color.BLACK);
        g.drawString("Restart", 270, 420);

        g.setFont(g.getFont().deriveFont(Font.BOLD, 20f));
        g.setColor(Color.BLACK);
        g.drawString("Quit", 285, 470);

        // score
        g.setFont(g.getFont().deriveFont(30f));
        if(result==1){
            g.drawString("Score: " + String.valueOf(snakeBody.size()), 240, 360);
        } else if(result==2){
            g.drawString("Score: " + String.valueOf(snake2Body.size()), 240, 360);
        } else{
            g.drawString("Tie!", 275, 360);
        }



        restartButton.setVisible(true);
        quitButton.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(gameStarted){
            move();
            repaint();
        }

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
        if (bombMode && !snake2Body.isEmpty()) {
            if (e.getKeyCode() == KeyEvent.VK_F) {
                int body_last_index = snake2Body.size() - 1;
                Tile temp = snake2Body.get(body_last_index);
                snake2Bomb.add(new Tile(temp.x, temp.y));
                snake2Body.remove(body_last_index);
                //System.out.println(temp.x + " " + temp.y);
            }

        }

        if (bombMode && !snakeBody.isEmpty()) {
            if (e.getKeyCode() == KeyEvent.VK_SLASH) {
                int body_last_index = snakeBody.size() - 1;
                Tile temp = snakeBody.get(body_last_index);
                snakeBomb.add(new Tile(temp.x, temp.y));
                snakeBody.remove(body_last_index);
                //System.out.println(temp.x + " " + temp.y);
            }

        }

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