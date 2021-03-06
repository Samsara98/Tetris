import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * 游戏区域，本质上是一个boolean型二维数组，一些地方有方块、另一些地方是空白
 */
public class GamingArea {
    private int width;
    private int height;
    private boolean[][] board;
    private boolean[][] cache;
    boolean committed;
    private int currentY;
    private Shape currentShape;
    private int changeNum;


    public GamingArea(int width, int height) {
        //board：添加shape后的游戏区域，cache：已放置好shape的游戏区域
        this.width = Math.abs(width);
        this.height = Math.abs(height);
        board = new boolean[this.width][this.height];
        cache = new boolean[this.width][this.height];
        arrayCopy(cache, board);
        committed = true;
    }


    /**
     * 二维数组复制
     * @param cache
     * @param board
     */
    private void arrayCopy(boolean[][] cache, boolean[][] board) {

        for (int i = 0; i < width; i++) {
            cache[i] = Arrays.copyOf(board[i], height);
        }
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

        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
                if (board[x][y]) {
                    return y + 1;
                }
            }
        }
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
        if (col >= width || col < 0) {
            return 0;
        }
        if (null == shape) {
            return 0;
        }

        int row = currentY;

        int maxHeight = 0;

        label:
        for (int y_ = row - 1; y_ >= 0; y_--) {
            for (int x = col; x < col + shape.getWidth(); x++) {
                if (cache[x][y_]) {
                    maxHeight = y_ + 1;
                    break label;
                }
            }
        }
        if (maxHeight + shape.getHeight() - 1 >= height) {
            return maxHeight;
        }

        int target = 0;

        for (int y = maxHeight; y >= maxHeight - shape.getHeight() + 1; y--) {
            if (y < 0) {
                return target;
            }
            for (Point point : shape.getPoints()) {
                if (isFilled(point.x + col, point.y + y)) {
                    return target;
                }
            }
            target = y;
        }
        return target;
    }


    /**
     * 返回某一列的当前高度。如果没方块则高度是0。
     * 注意：如果某一列有空心的情况，高度应以位置最高的方块为准。
     */
    public int getColumnHeight(int col) {

        if (col >= width || col < 0) {
            return 0;
        }
        for (int y = height - 1; y >= 0; y--) {
            if (board[col][y]) {
                return y + 1;
            }
        }

        return 0;
    }


    public int getPointsNum() {

        int num = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (isFilled(x, y)) {
                    num++;
                }
            }
        }
        return num;
    }


    /**
     * 某一行已经填上方块的个数
     */
    public int getFilledBlockCount(int row) {

        int rowNum = 0;
        if (row >= height || row < 0) {
            return rowNum;
        }
        for (int x = 0; x < width; x++) {
            if (board[x][row]) {
                rowNum += 1;
            }
        }

        return rowNum;
    }


    /**
     * 检查游戏区域的某个坐标是否已经放置有方块。
     * 如果坐标超出边界，返回true即可。
     */
    public boolean isFilled(int col, int row) {

        if (col >= width || row >= height || col < 0 || row < 0)
            return true;
        return board[col][row];
    }


    /**
     * 游戏区域方块高度的最大差值
     * @return
     */
    public int range() {

        int[] gaps = new int[width];
        for (int x = 0; x < width; x++) {
            gaps[x] = getColumnHeight(x);
        }
        int max = Arrays.stream(gaps).max().getAsInt();
        int min = Arrays.stream(gaps).min().getAsInt();
        int maxGap = max - min;

        return maxGap;
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
        //调用place成功使游戏区变脏
        committed = false;
        if (col + shape.getWidth() - 1 >= width || shape.getHeight() + row - 1 >= height || col < 0 || row < 0) {
            return OUT;
        }

        for (Point point : shape.getPoints()) {
            if (isFilled(point.x + col, point.y + row)) {
                return COLLIDED;
            }
        }
        //若可以放置修改board
        for (Point point : shape.getPoints()) {
            if (point.y + row < height) {
                board[point.x + col][point.y + row] = true;
            }
        }

        currentY = row;
        currentShape = shape;

        for (int y = 0; y < shape.getHeight(); y++) {
            if (getFilledBlockCount(y + row) == width) {
                return ROW_FULL;
            }
        }

        return OK;
    }


    /**
     * 消除填满的行，更新游戏区，并返回消除的行数
     */
    public int clearRows() {

        int rowsCleared = 0;

        boolean[][] board2 = new boolean[width][height];
        int row = 0;
        for (int y = 0; y < height; y++) {
            if (getFilledBlockCount(y) == width) {
                rowsCleared += 1;
                for (Point point :currentShape.getPoints()) {
                    if(currentY + point.y ==y){
                        changeNum ++;
                    }
                }
            } else {
                for (int x = 0; x < width; x++) {
                    board2[x][row] = board[x][y];
                }
                row++;
            }
        }
        board = board2;
        return rowsCleared;
    }


    /**
     * 消除行时shape贡献的方块数
     * @return
     */
    public int getChangeNum(){

        int num = changeNum;
        changeNum = 0;
        return num;
    }

    /**
     * 撤销操作，恢复到放置前的状态
     */
    public void undo() {

        arrayCopy(board, cache);
        committed = true;
    }


    /**
     * 放置生效
     */
    public void commit() {

        arrayCopy(cache, board);
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


