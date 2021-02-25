import javax.swing.*;

public class AITetris extends Tetris implements AI {

    AITetris(int pixels) {

        super(pixels);
    }


    @Override
    public void tick(int direction) {

        super.tick(direction);
        if (direction == DOWN) {
            gamingArea.undo();
            Move move = calculateBestMove(gamingArea, currentShape);
            if(null != move){
                newX = move.x;
                newY = move.y;
                currentShape = move.shape;
            }
            super.tick(direction);
        }

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

