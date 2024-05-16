import javax.swing.*;
import java.awt.event.*;

public class Buttons extends JButton{
    public Buttons(String label,SnakeGame game){
        super(label);

        if(label.equals("Restart")){
            setBounds(260,400,80,30);
            game.add(this);
            restartAction(game);
        }else{
            setBounds(260,440,80,30);
            game.add(this);
            setQuitAction();
        }
    }
    // Method to set the action to quit the JFrame
    public void setQuitAction() {
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    public void restartAction(SnakeGame game) {
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.restartGame();
            }
        });
    }

    public void restartgame(SnakeGame g){// couldn't reach Tile
//        snakeHead = new SnakeGame.Tile(5, 5);
        g.snakeBody.clear();
        g.snakeSpeed = 100;
        g.gameOver = false;
        g.velocityX = 0;
        g.velocityY = 1;
        g.placeFood();
        g.gameLoop.start();
        requestFocusInWindow();
        g.restartButton.setVisible(false);
        g.quitButton.setVisible(false);
    }
}

