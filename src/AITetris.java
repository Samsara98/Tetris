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
                if (move.x > newX){
                    super.tick(RIGHT);
                }else if (move.x < newX){
                    super.tick(LEFT);
                }else {
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
        debugButton.addActionListener(debugButton.isSelected()?e -> random.setSeed(0):e -> random.setSeed(System.currentTimeMillis()));
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

