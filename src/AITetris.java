import javax.swing.*;
import java.awt.*;
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
                currentX = move.x;
                currentY = move.y;
                currentShape = move.shape;
                score = (int) move.score;
            }
        }

    }


    @Override
    public JPanel createControlPanel() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        countLabel = new JLabel("0");
        panel.add(countLabel);

        scoreLabel = new JLabel("0");
        panel.add(scoreLabel);

        timeLabel = new JLabel(" ");
        panel.add(timeLabel);

        panel.add(Box.createVerticalStrut(20));             // 空白间隔

        startButton = new JButton("开始");
        startButton.addActionListener(e -> startGame());
        panel.add(startButton);

        stopButton = new JButton("暂停");
        stopButton.addActionListener(e -> stopGame());
        panel.add(stopButton);
        toggleButtons();

        panel.add(Box.createVerticalStrut(20));

        speedSlider = new JSlider(0, 200, 100);
        speedSlider.setPreferredSize(new Dimension(100, 15));
        speedSlider.addChangeListener(e -> updateTimer());
        updateTimer();
        panel.add(speedSlider);

        panel.add(Box.createVerticalStrut(12));

        quitButton = new JButton("退出");
        quitButton.addActionListener(e -> System.exit(0));
        panel.add(quitButton);

        random = new Random(); // 用于让形状随机出现
        debugButton = new JCheckBox("debug");
        debugButton.addActionListener(e -> random.setSeed(0));
        panel.add(debugButton);

        return panel;
    }

    @Override
    public void startGame() {

        count = 0;
        score = 0;
        gameOn = true;
        startTime = System.currentTimeMillis();
        timer.start();

        gamingArea = new GamingArea(WIDTH, HEIGHT + TOP_SPACE);
        updateCounters();
        toggleButtons();
        timeLabel.setText(" ");
        addNewShape();

        repaint();
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

