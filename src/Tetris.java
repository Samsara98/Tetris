import javax.swing.*;
import java.awt.*;
import java.util.Random;


/**
 * 俄罗斯方块GUI模块
 * 这个类主要负责交互和动画，用j、k、l键调整形状，用n键落下
 * 游戏的逻辑则主要放在GamingArea和Shape里
 */
public class Tetris extends JComponent {
    public static final int WIDTH = 10;
    public static final int HEIGHT = 20;

    // 顶部预留一些空间用于形状的掉落。当方块堆积进入该空间时，即可算作游戏结束
    public static final int TOP_SPACE = 4;

    // 游戏的核心数据结构
    protected GamingArea gamingArea;    // 游戏区域
    protected Shape[] shapes;           // 所有的7种形状

    // 目前正在掉落的形状（如果没有，则为null）
    protected Shape currentShape;
    protected int currentX;
    protected int currentY;

    // 当前形状下一步（旋转、下落等）即将变成的样子
    protected Shape newShape;
    protected int newX;
    protected int newY;

    // 游戏状态
    protected boolean gameOn;       // 游戏是否仍然继续
    protected int count;            // 已经掉落了多少个形状
    protected int score;            // 分数
    protected long startTime;       // 开始时间
    protected Random random;


    // 控制面板上的按键
    protected JLabel countLabel;
    protected JLabel scoreLabel;
    protected JLabel timeLabel;
    protected JButton startButton;
    protected JButton stopButton;
    protected JButton quitButton;
    protected javax.swing.Timer timer;
    protected JSlider speedSlider;

    public final int DELAY = 300;


    Tetris(int pixels) {

        super();

        // 两边边界各1px，所以+2
        setPreferredSize(new Dimension((WIDTH * pixels) + 2,
                (HEIGHT + TOP_SPACE) * pixels + 2));
        gameOn = false;

        shapes = Shape.getShapes();
        gamingArea = new GamingArea(WIDTH, HEIGHT + TOP_SPACE);


        // 上下左右箭头可能跟速度滑动块冲突，所以用j、l、k、n
        // 左
        registerKeyboardAction(
                e -> tick(LEFT), "left", KeyStroke.getKeyStroke('j'), WHEN_IN_FOCUSED_WINDOW
        );
        // 右
        registerKeyboardAction(
                e -> tick(RIGHT), "right", KeyStroke.getKeyStroke('l'), WHEN_IN_FOCUSED_WINDOW
        );
        // 旋转
        registerKeyboardAction(
                e -> tick(ROTATE), "rotate", KeyStroke.getKeyStroke('k'), WHEN_IN_FOCUSED_WINDOW
        );
        // 直接掉落
        registerKeyboardAction(
                e -> tick(DROP), "drop", KeyStroke.getKeyStroke('n'), WHEN_IN_FOCUSED_WINDOW
        );
        // 定时器定期调用tick函数
        timer = new javax.swing.Timer(DELAY, e -> tick(DOWN));

        requestFocusInWindow();
    }


    /**
     * 开始游戏
     */
    public void startGame() {

        count = 0;
        score = 0;
        gameOn = true;
        random = new Random(); // 用于让形状随机出现
        startTime = System.currentTimeMillis();
        timer.start();

        gamingArea = new GamingArea(WIDTH, HEIGHT + TOP_SPACE);
        updateCounters();
        toggleButtons();
        timeLabel.setText(" ");
        addNewShape();

        repaint();
    }


    /**
     * 设置开始、暂停键状态
     */
    void toggleButtons() {

        startButton.setEnabled(!gameOn);
        stopButton.setEnabled(gameOn);
    }


    /**
     * 暂停游戏
     */
    public void stopGame() {

        gameOn = false;
        toggleButtons();
        timer.stop();

        long timeElapsed = (System.currentTimeMillis() - startTime) / 10;
        timeLabel.setText(timeElapsed / 100.0 + " s");
    }


    /**
     * 更新当前形状，如果失败则撤销，游戏区域保持原样。返回值和GamingArea.place()相同。
     * 注意：在调用该函数前，游戏区域应处于干净状态
     */
    public int updateCurrentShape(Shape shape, int x, int y) {


        int result = gamingArea.place(shape, x, y);

        // 放置成功
        if (result <= GamingArea.ROW_FULL) {
            repaint();
            currentShape = shape;
            currentX = x;
            currentY = y;
            repaint();
        }
        // 放置失败
        else {
            gamingArea.undo();
        }

        return result;
    }


    /**
     * 随机产生下一个形状
     */
    public Shape pickNextShape() {

        int shapeIndex;
        shapeIndex = (int) (shapes.length * random.nextDouble());
        Shape shape = shapes[shapeIndex];
        for (int i = 0; i < (int) (random.nextDouble() * 4); i++) {
            shape = shape.fastRotation();
        }
        return shape;
    }


    /**
     * 添加一个新的形状，如果不能添加则结束游戏
     */
    public void addNewShape() {

        count++;
        score++;

        gamingArea.commit();
        currentShape = null;

        Shape shape = pickNextShape();

        // 顶部居中的位置
        int px = (gamingArea.getAreaWidth() - shape.getWidth()) / 2;
        int py = gamingArea.getAreaHeight() - shape.getHeight();

        // 放置新形状
        updateCurrentShape(shape, px, py);

        updateCounters();
    }


    /**
     * 更新计数器label
     */
    void updateCounters() {

        countLabel.setText("掉落：" + count);
        scoreLabel.setText("得分：" + score);
    }


    /**
     * 根据用户按键计算新位置，并保存进相关成员变量中。
     * 计算前，最好确保当前形状不在游戏区域中，以免调用getDropHeight时计算出错
     */
    public void computeNewPosition(int direction) {
        // 新位置初始化为跟当前位置一样
        newShape = currentShape;
        newX = currentX;
        newY = currentY;

        switch (direction) {
            case LEFT:
                newX--;
                break;
            case RIGHT:
                newX++;
                break;
            case ROTATE:
                newShape = newShape.fastRotation();
                // 使旋转围绕中心而不是边缘
                newX = newX + (currentShape.getWidth() - newShape.getWidth()) / 2;
                newY = newY + (currentShape.getHeight() - newShape.getHeight()) / 2;
                break;
            case DOWN:
                newY--;
                break;
            case DROP:
                newY = gamingArea.getDropHeight(newShape, newX);
                break;
            default:
                throw new RuntimeException("方向错误");
        }

    }


    public static final int ROTATE = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int DROP = 3;
    public static final int DOWN = 4;


    /**
     * 游戏进行的最小"时间"单位，每tick一次，就移动一次Shape。
     * 由两种调用方式：
     * - 定时器触发定期触发，形状会下落一格。
     * - 用户按j、k、l、n键，形状会调整位置、旋转角度、直接掉落
     * <p>
     * 定时器 - 调用时参数direction为DOWN
     * 按键j - 调用时参数direction为LEFT
     * 按键k - 调用时参数direction为ROTATE
     * 按键l - 调用时参数direction为RIGHT
     * 按键n - 调用时参数direction为DROP，直接掉落
     */
    public void tick(int direction) {
        // sanity check
        if (!gameOn) {
            return;
        }

        // 先将当前形状移除
        if (currentShape != null) {
            gamingArea.undo();
        }

        // 根据用户按键计算新位置，结果会更新进newShape/newX/newY等成员变量中
        computeNewPosition(direction);

        // 尝试在新位置放置新形状，如果失败则放回当前形状
        int result = updateCurrentShape(newShape, newX, newY);
        switch (result) {
            case GamingArea.OK:
                break;
            case GamingArea.ROW_FULL:
                repaint();
                break;
            case GamingArea.OUT:
            case GamingArea.COLLIDED:

                // 新形状失败，将旧（即当前）形状放回
                if (currentShape != null) {
                    gamingArea.place(currentShape, currentX, currentY);
                }
                repaint();

                // 自由下落失败，说明已经触底
                if (direction == DOWN) {
                    tryClearRows();
                    if (currentShapeReachedTop()) {
                        gamingArea.commit();
                        stopGame();
                    } else {
                        addNewShape();
                    }
                }
        }

    }


    /**
     * 判断当前形状是否已经触及天花板
     */
    protected boolean currentShapeReachedTop() {

        return gamingArea.getMaxHeight() > gamingArea.getAreaHeight() - TOP_SPACE;
    }


    private void tryClearRows() {

        int cleared = gamingArea.clearRows();
        if (cleared > 0) {
            // 消1行 - 5分
            // 消2行 - 10分
            // 消3行 - 20分
            // 消4行 - 40分
            switch (cleared) {
                case 1:
                    score += 5;
                    break;
                case 2:
                    score += 10;
                    break;
                case 3:
                    score += 20;
                    break;
                case 4:
                    score += 40;
                    break;
                default:
                    throw new RuntimeException("到底想消几行？你咋不上天呢？");
            }
            updateCounters();
            repaint();
        }
    }


    /**
     * 以下函数处理游戏坐标系和像素坐标系之间的转换
     * +1、-2是处理边界
     */


    // 每个block的宽度
    private float dX() {

        return (((float) (getWidth() - 2)) / gamingArea.getAreaWidth());
    }


    // 每个block的高度
    private float dY() {

        return (((float) (getHeight() - 2)) / gamingArea.getAreaHeight());
    }


    // block左边界的x坐标
    private int xPixel(int x) {

        return (Math.round(1 + (x * dX())));
    }


    // block上边界的y坐标
    private int yPixel(int y) {

        return (Math.round(getHeight() - 1 - (y + 1) * dY()));
    }


    /**
     * 画图功能
     */
    @Override
    public void paintComponent(Graphics g) {

        // 整个游戏区域
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);


        // 顶部空间的分割线
        int divider = yPixel(gamingArea.getAreaHeight() - TOP_SPACE - 1);
        g.drawLine(0, divider, getWidth() - 1, divider);

        // 屏幕上的方块
        paintBlocks(g);
    }


    private void paintBlocks(Graphics g) {

        final int dx = Math.round(dX() - 2);
        final int dy = Math.round(dY() - 2);
        final int nCols = gamingArea.getAreaWidth();

        int col, row;
        // 从左往右，从下往上
        for (col = 0; col < nCols; col++) {
            int left = xPixel(col);
            // 只画到本列的最高点即可
            final int yHeight = gamingArea.getColumnHeight(col);
            for (row = 0; row < yHeight; row++) {
                if (gamingArea.isFilled(col, row)) {
                    boolean filled = (gamingArea.getFilledBlockCount(row) == nCols);
                    // 满行的话，先调整为绿色
                    if (filled) {
                        g.setColor(Color.green);
                    }

                    g.fillRect(left + 1, yPixel(row) + 1, dx, dy);

                    // 重置颜色
                    if (filled) {
                        g.setColor(Color.black);
                    }
                }
            }
        }
    }


    /**
     * 调整速度
     */
    public void updateTimer() {

        double value = ((double) speedSlider.getValue()) / speedSlider.getMaximum();
        timer.setDelay((int) (DELAY - value * DELAY));
    }


    /**
     * 游戏的控制面板，作为Tetris的一个方法，可以访问其成员变量
     */
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

        return panel;
    }


    public static JFrame createFrame(Tetris tetris) {

        JFrame frame = new JFrame("俄罗斯方块");
        frame.setLayout(new BorderLayout());

        // 游戏面板
        frame.add(tetris, BorderLayout.CENTER);

        // 控制面板
        JComponent controls = tetris.createControlPanel();
        frame.add(controls, BorderLayout.EAST);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        return frame;
    }


    public static void main(String[] args) {
        // boilerplate code
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        Tetris tetris = new Tetris(16);
        JFrame frame = createFrame(tetris);
        frame.setVisible(true);
    }
}

