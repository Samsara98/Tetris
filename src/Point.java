/**
 * 常规俄罗斯方块中的每个形状都由4个block组成，Point类用来保存这4个block的坐标
 * 注意：记录block坐标时，不用考虑游戏区域，而只考虑当前形状本身，以自身左下角为(0, 0)
 * 例如
 * - L型的4个坐标为(0,0) (0,1) (0,2) (1,0)
 * - O型的4个坐标为(0,0) (0,1) (1,0) (1,1)
 * - 以此类推
 */
public class Point {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // 覆盖默认的equals实现
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof  Point)) {
            return false;
        }

        Point otherPoint = (Point) other;

        return x == otherPoint.x && y == otherPoint.y;
    }

    // 转成字符串，方便debug
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
