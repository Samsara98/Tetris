/**
 * 游戏区域，本质上是一个boolean型二维数组，一些地方有方块、另一些地方是空白
 */
public class GamingArea {
    private int width;
    private int height;
    private boolean[][] board;
    boolean committed;


    public GamingArea(int width, int height) {
        this.width = width;
        this.height = height;
        board = new boolean[width][height];
        committed = true;

        // TODO
    }

    /**
     * 返回游戏区域的宽度（非像素，而是横着可以放多少方块）
     */
    public int getAreaWidth() {
        return width;
    }


    /**
     * 返回游戏区域的高度（非像素，而是竖着可以放多少方块）
     */
    public int getAreaHeight() {
        return height;
    }


    /**
     * 整个游戏区域中已经放置的最高方块的位置
     */
    public int getMaxHeight() {
        // TODO
        return 0;
    }

    /**
     * 基于当前的游戏区格局，计算当用户按下掉落键（n）时，某一形状应该下降到的高度
     *
     * @param shape 即将掉落的形状
     * @param col   该形状目前所在的位置（仅x坐标，即列）
     * @return 掉落后，该形状理应到达的高度（仅y坐标，即行）
     */
    public int getDropHeight(Shape shape, int col) {
        // TODO
        return 0;
    }


    /**
     * 返回某一列的当前高度。如果没方块则高度是0。
     * 注意：如果某一列有空心的情况，高度应以位置最高的方块为准。
     */
    public int getColumnHeight(int col) {
        // TODO
        return 0;
    }


    /**
     * 某一行已经填上方块的个数
     */
    public int getFilledBlockCount(int row) {
        // TODO
        return 0;
    }


    /**
     * 检查游戏区域的某个坐标是否已经放置有方块。
     * 如果坐标超出边界，返回true即可。
     */
    public boolean isFilled(int col, int row) {
        // TODO
        return false;
    }


    public static final int OK = 0;
    public static final int ROW_FULL = 1;
    public static final int OUT = 2;
    public static final int COLLIDED = 3;

    /**
     * 尝试将形状放置在/调整到游戏区域某个位置，可能有几种不同的结果：
     * <p>
     * - 正常放置，无特殊事情发生，返回OK
     * - 放置后导致某一行或几行填满，可以消除，ROW_FILLED
     * - 形状超出边界，操作无效，返回OUT
     * - 和现有的方块重叠，操作无效，返回COLLIDED
     * <p>
     * 对于后两种情况，调用者后续应使用undo进行撤销。
     */
    public int place(Shape shape, int col, int row) {
        // sanity check
        if (!committed) {
            throw new RuntimeException("未commit时调用place");
        }

        // TODO

        return OK;
    }


    /**
     * 消除填满的行，更新游戏区，并返回消除的行数
     */
    public int clearRows() {
        int rowsCleared = 0;
        // TODO:
        return rowsCleared;
    }


    /**
     * 撤销操作，恢复到放置前的状态
     */
    public void undo() {
        // TODO
    }


    /**
     * 放置生效
     */
    public void commit() {
        committed = true;
    }


    /**
     * 将当前游戏区域转换成一个字符串，可以方便debug
     */
    public String toString() {
        StringBuilder buffer = new StringBuilder();

        // 当前的游戏区域
        for (int row = height - 1; row >= 0; row--) {
            buffer.append('|');
            for (int col = 0; col < width; col++) {
                if (isFilled(col, row)) {
                    buffer.append('+');
                } else {
                    buffer.append(' ');
                }
            }
            buffer.append("|\n");
        }

        // 分割线
        buffer.append("-".repeat(width + 2));
        buffer.append('\n');

        return buffer.toString();
    }
}


