import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AITetris extends Tetris implements AI {

    JCheckBox debugButton;
    JCheckBox AIButton;
    JRadioButton levelButton1;
    JRadioButton levelButton2;
    JRadioButton levelButton3;
    int Level = 1;
    private List<Shape> shapesList;


    AITetris(int pixels) {

        super(pixels);
    }


    @Override
    public void tick(int direction) {

        shapesList = gamingAreaAnalyse(gamingArea,shapes);
        if(AIButton.isSelected()){
            super.tick(direction);
            if (direction == DOWN) {
                gamingArea.undo();
                Move move = calculateBestMove(gamingArea, currentShape);
                if (null != move) {
                    if (move.x > newX) {
                        delay();
                        super.tick(RIGHT);
                    } else if (move.x < newX) {
                        delay();
                        super.tick(LEFT);
                    } else {
                        delay();
                        super.tick(direction);
                    }
                    currentShape = move.shape;
                    score = (int) move.score;
                }
            }
        }else {
            super.tick(direction);
        }

    }


    private void delay() {

        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public JPanel createControlPanel() {

        JPanel panel = super.createControlPanel();
        random = new Random(); // 用于让形状随机出现
        debugButton = new JCheckBox("debug");
        panel.add(debugButton);

        levelButton1 = new JRadioButton("NORMAL");
        levelButton1.setSelected(true);
        levelButton2 = new JRadioButton("HARD");
        levelButton3 = new JRadioButton("HELL");
        AIButton = new JCheckBox("AI");
        panel.add(AIButton);

        levelButton1.addActionListener(e -> Level = 1);
        levelButton2.addActionListener(e -> Level = 2);
        levelButton3.addActionListener(e -> Level = 3);

        ButtonGroup bg = new ButtonGroup();
        bg.add(levelButton1);
        bg.add(levelButton2);
        bg.add(levelButton3);

        panel.add(levelButton1);
        panel.add(levelButton2);
        panel.add(levelButton3);
        panel.add(AIButton);

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
        shapesList = gamingAreaAnalyse(gamingArea,shapes);
        addNewShape();

        repaint();
    }


    @Override
    public Shape pickNextShape() {

//        int maxScore = 0;
//        Shape bad = new Shape("0 0");
//        for (Shape shape : shapes) {
//            Move move = calculateBestMove(gamingArea, shape);
//            if((int)move.score > maxScore){
//                maxScore = Math.max(maxScore,(int)move.score);
//                bad = move.shape;
//            }
//        }
        int index = 6;
        switch (Level){
            case 1:
                index = random.nextInt(7);
                break;
            case 2:
                List<Integer> list = new ArrayList<>(Arrays.asList(2, 2, 2, 1, 1, 1, 1));
                index = Xrandom(list);
                break;
            case 3:
                List<Integer> list2 = new ArrayList<>(Arrays.asList(60, 40, 25, 20, 20, 20, 15));
                index = Xrandom(list2);
                break;
        }
        Shape nextShape = shapesList.get(index);
        for (int i = 0; i < (int) (random.nextDouble() * 4); i++) {
            nextShape = nextShape.fastRotation();
        }
        return nextShape;
    }

    /**
     * 权重随机数
     * @param weight [15,568,4181,2]
     * @return 索引值
     */
    public static int Xrandom(List<Integer> weight){
        List<Integer> weightTmp = new ArrayList<>(weight.size()+1);
        weightTmp.add(0);
        Integer sum = 0;
        for( Integer d : weight ){
            sum += d;
            weightTmp.add(sum);
        }
        Random random = new Random();
        int rand = random.nextInt(sum);
        int index = 0;
        for(int i = weightTmp.size()-1; i >0; i--){
            if( rand >= weightTmp.get(i)){
                index = i;
                break;
            }
        }
        return index;
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

