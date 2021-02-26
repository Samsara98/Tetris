import javax.swing.*;
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
    public JPanel createControlPanel() {

        JPanel panel = super.createControlPanel();
        random = new Random(); // 用于让形状随机出现
        debugButton = new JCheckBox("debug");
        panel.add(debugButton);
        return panel;
    }


    @Override
    public void startGame() {

        count = 0;
        score = 0;
        gameOn = true;
        if (debugButton.isSelected()) {
            random.setSeed(0);
        } else {
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
    public Shape pickNextShape() {

        int maxScore = 0;
        Shape bad = new Shape("0 0");
        for (Shape shape : shapes) {
            Move move = calculateBestMove(gamingArea, shape);
            if((int)move.score > maxScore){
                maxScore = Math.max(maxScore,(int)move.score);
                bad = move.shape;
            }
        }
        return bad;
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

