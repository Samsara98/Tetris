import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class AITetris extends Tetris implements AI {

    JCheckBox debugButton = new JCheckBox();


    AITetris(int pixels) {

        super(pixels);
    }


    @Override
    public void tick(int direction) {

        super.tick(direction);
        if (direction == DOWN) {
            gamingArea.undo();
            Move move = calculateBestMove(gamingArea, currentShape);
            if (null != move) {
                if (move.x > newX) {
                    super.tick(RIGHT);
                } else if (move.x < newX) {
                    super.tick(LEFT);
                } else {
                    super.tick(direction);
                }
                currentShape = move.shape;
                score = (int) move.score;
            }
        }

    }

    @Override
    public void startGame() {

        count = 0;
        score = 0;
        gameOn = true;
        if(debugButton.isSelected()){
            random.setSeed(0);
        }else {
            random = new Random();
        }
        startTime = System.currentTimeMillis();
        timer.start();

        gamingArea = new GamingArea(WIDTH, HEIGHT + TOP_SPACE);
        updateCounters();
        toggleButtons();
        timeLabel.setText(" ");
        addNewShape();

        repaint();
    }

    @Override
    public JPanel createControlPanel() {

        JPanel panel = super.createControlPanel();
        random = new Random(); // 用于让形状随机出现
        debugButton = new JCheckBox("debug");
        panel.add(debugButton);


        return panel;
    }


    public static void main(String[] args) {
        // boilerplate code
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        Tetris tetris = new AITetris(16);
        JFrame frame = createFrame(tetris);
        frame.setVisible(true);
    }

}

