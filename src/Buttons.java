import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.LineBorder;
import java.awt.Color;
public class Buttons extends JButton{
    public Buttons(String label,SnakeGame game){
//        super(label);

        if(label.equals("Restart")){
            setBounds(260,400,80,30);

            // Make the button transparent
            this.setOpaque(false);
            this.setContentAreaFilled(false);
            this.setBorder(new LineBorder(new Color(0, 0, 0, 0), 1));

            game.add(this);
            restartAction(game);
        } else if(label.equals("Quit")){
            setBounds(260,440,80,30);

            // Make the button transparent
            this.setOpaque(false);
            this.setContentAreaFilled(false);
            this.setBorder(new LineBorder(new Color(0, 0, 0, 0), 1));

            game.add(this);
            setQuitAction();
        } else if(label.equals("Normal")){
            setBounds(100,430,180,60);

            // Make the button transparent
            this.setOpaque(false);
            this.setContentAreaFilled(false);
            this.setBorder(new LineBorder(new Color(0, 0, 0, 0), 1));

            game.add(this);
            startGameAction(game);
        } else if(label.equals("Bomb")){
            setBounds(320,430,180,60);

            // Make the button transparent
            this.setOpaque(false);
            this.setContentAreaFilled(false);
            this.setBorder(new LineBorder(new Color(0, 0, 0, 0), 1));

            game.add(this);
            startBombGameAction(game);
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
            public void actionPerformed(ActionEvent e) { game.restartGame();}
        });
    }
    public void startGameAction(SnakeGame game) {
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.startGameButton.setVisible(false);
                game.startBombGameButton.setVisible(false);
                game.gameStarted=true;
                game.bombMode=false;
            }
        });
    }
    public void startBombGameAction(SnakeGame game) {
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.startGameButton.setVisible(false);
                game.startBombGameButton.setVisible(false);
                game.gameStarted=true;
                game.bombMode=true;
            }
        });
    }
}
