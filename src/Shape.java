import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 俄罗斯方块中的一个形状
 */
public class Shape {
    private Point[] points; // 每个形状是由4个block组成，points数组即是这4个block的坐标。可参考Point.java
    private int width;  // 该形状最宽占了几个block
    private int height; // 该形状最高占了几个block
    private Shape next; // 该形状旋转后的下一个"角度"

    private static Shape[] shapes;    // 静态数组，储存所有7种形状，加快读取速度


    /**
     * 以Point数组作为参数的构造函数
     */
    public Shape(Point[] points) {
        // TODO
        this.points = points;
    }


    /**
     * 以字符串作为参数的构造函数，把字符串解析为Point数组，然后调用对应的构造函数，生成形状。
     * 例如"0 0  1 0  0 1  0 2"字符串对应了L形，坐标系的描述参考Point.java
     */
    public Shape(String points) {
        this(parsePoints(points));
    }


    /**
     * 形状的宽度，以block为单位
     */
    public int getWidth() {
        return width;
    }


    /**
     * 形状的高度，以block为单位
     */
    public int getHeight() {
        return height;
    }


    /**
     * 返回该构成形状的点。
     * 由于数组是引用类型，调用该函数后，不应该对返回的数组继续修改。
     */
    public Point[] getPoints() {
        return points;
    }


    /**
     * 返回当前形状逆时针旋转了90度之后的形状
     * 注意：该方法不应改变当前形状，而应返回一个新形状
     */
    public Shape rotateCounterclockwise() {
        // TODO
        return null;
    }


    /**
     * （快速版）返回当前形状逆时针旋转了90度之后的形状
     * 注意：该方法不应改变当前形状，而应返回一个新形状
     */
    public Shape fastRotation() {
        return next;
    }

    /**
     * 检查两个形状是否完全相同。
     * 注意：同一个形状的不同旋转角度，应该返回false。
     */
    public boolean equals(Object other) {
        // TODO
        return true;
    }


    // 7种形状的字符串表示，坐标系描述参考Point.java
    public static final String I_STR = "0 0  0 1  0 2  0 3";
    public static final String L_STR = "0 0  0 1  0 2  1 0";
    public static final String J_STR = "0 0  1 0  1 1  1 2";
    public static final String S_STR = "0 0  1 0  1 1  2 1";
    public static final String Z_STR = "0 1  1 1  1 0  2 0";
    public static final String O_STR = "0 0  0 1  1 0  1 1";
    public static final String T_STR = "0 0  1 0  1 1  2 0";

    /**
     * 生成所有的形状，放进一个数组里。
     * 注意，生成的数组长度为7，每种形状只包含了一种旋转角度。
     * 数组里的每个形状又通过自己的next字段（指针），链接了该形状的所有旋转角度。
     */
    public static Shape[] getShapes() {
        // 第一次调用时生成，且只生成1次，提高效率
        if (Shape.shapes == null) {
            Shape.shapes = new Shape[]{
                    makeFastRotations(new Shape(I_STR)),
                    makeFastRotations(new Shape(L_STR)),
                    makeFastRotations(new Shape(J_STR)),
                    makeFastRotations(new Shape(S_STR)),
                    makeFastRotations(new Shape(Z_STR)),
                    makeFastRotations(new Shape(O_STR)),
                    makeFastRotations(new Shape(T_STR)),
            };
        }
        return Shape.shapes;
    }


    /**
     * 生成某一形状所有旋转，并用next字段串联起来。最后一种角度的next应指向第一种角度，形成一个环。
     * 注意：不同形状旋转后的角度数量不一样，例如O型只有一种角度
     */
    private static Shape makeFastRotations(Shape root) {
        // TODO
        return null;
    }


    /**
     * 从字符串中解析Point数组
     */
    private static Point[] parsePoints(String string) {
        List<Point> points = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(string);
        try {
            while (tokenizer.hasMoreTokens()) {
                int x = Integer.parseInt(tokenizer.nextToken());
                int y = Integer.parseInt(tokenizer.nextToken());

                points.add(new Point(x, y));
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException("无法解析字符串：" + string);
        }

        // 为了兼容性，Java里将ArrayList转成Array时必须传入一个同类型的数组做参数
        Point[] array = points.toArray(new Point[0]);
        return array;
    }


}
