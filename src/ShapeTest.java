import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class ShapeTest {
    private Shape t;
    private Shape i;
    private Shape l;
    private Shape j;
    private Shape s;
    private Shape z;
    private Shape o;


    @Before
    public void setUp() {

        t = new Shape(Shape.T_STR);
        i = new Shape(Shape.I_STR);
        l = new Shape(Shape.L_STR);
        j = new Shape(Shape.J_STR);
        s = new Shape(Shape.S_STR);
        z = new Shape(Shape.Z_STR);
        o = new Shape(Shape.O_STR);
    }


    @Test(timeout = 100)
    public void stupidTest() {
        // 形状T
        assertEquals(4, t.getPoints().length);
        assertTrue(Arrays.asList(t.getPoints()).contains(new Point(1, 0)));

        // 形状L
        assertTrue(Arrays.asList(l.getPoints()).contains(new Point(0, 1)));

    }


    @Test(timeout = 100)
    public void shapeTest() {

        Shape t1 = new Shape("1 5  2 4  3 3  4 2  1 2  3 2");
        Shape t2 = new Shape("1 5  2 4  3 3");
        Point[] points = t1.getPoints();
        assertTrue(Arrays.asList(points).contains(new Point(1, 5)));
        assertTrue(Arrays.asList(points).contains(new Point(2, 4)));
        assertTrue(Arrays.asList(points).contains(new Point(3, 3)));
        assertTrue(Arrays.asList(points).contains(new Point(4, 2)));
        assertEquals(5, t1.getWidth());
        assertEquals(6, t1.getHeight());
        assertEquals(6, points.length);
        assertEquals(3, t2.getPoints().length);



        assertEquals(1, i.getWidth());
        assertEquals(2, l.getWidth());
        assertEquals(2, j.getWidth());
        assertEquals(3, s.getWidth());
        assertEquals(3, z.getWidth());
        assertEquals(2, o.getWidth());
        assertEquals(3, t.getWidth());

        assertEquals(4, i.getHeight());
        assertEquals(3, l.getHeight());
        assertEquals(3, j.getHeight());
        assertEquals(2, s.getHeight());
        assertEquals(2, z.getHeight());
        assertEquals(2, o.getHeight());
        assertEquals(2, t.getHeight());
    }


    @Test(timeout = 100)
    public void equalsTest() {

        Shape t1 = new Shape("0 0  1 0  1 1  2 0");
        Shape t2 = new Shape("0 0  1 0  1 1  2 0  2 1");
        Shape t3 = new Shape("0 0  1 0  1 1");
        Shape t4 = new Shape(t.getPoints());
        Shape t5 = new Shape("0 0");
        Shape t6 = new Shape("0 0  1 0");

        assertEquals(t, t);                 // 自反性
        assertNotEquals(t, null);     // x.equals(null)应该返回false
        assertNotEquals(null, t);
        assertEquals(t, t1);                // 传递性
        assertEquals(t, t4);
        assertEquals(t1, t4);
        assertEquals(t, t1);               // 一致性
        assertEquals(t, t1);
        assertNotEquals(t1, t2);            // 对称性
        assertNotEquals(t2, t1);
        assertNotEquals(t1, t3);
        assertNotEquals(t3, t1);
        assertNotEquals(t, t5);
        assertNotEquals(t5, t);
        assertNotEquals(t, t6);
        assertNotEquals(t6, t);


        assertEquals(o, o);
        assertNotEquals(o, l);
        assertNotEquals(j, l);
        assertNotEquals(l, j);
        assertNotEquals(i, l);
        assertNotEquals(j, s);
        assertNotEquals(z, s);
    }


    @Test(timeout = 100)
    public void rotateCounterclockwiseTest() {

        Shape t1 = t.rotateCounterclockwise();
        Shape t2 = t1.rotateCounterclockwise();
        Shape t3 = t2.rotateCounterclockwise();
        Shape t4 = t3.rotateCounterclockwise();
        Shape t5 = t4.rotateCounterclockwise();

        assertNotEquals(t, t1);
        assertNotEquals(t, t2);
        assertNotEquals(t, t3);
        assertEquals(t, t4);
        assertEquals(t5, t1);

        assertNotEquals(t1, t2);
        assertNotEquals(t2, t3);
        assertNotEquals(t3, t4);
        assertNotEquals(t4, t5);

        Point[] points = t1.getPoints();
        assertTrue(Arrays.asList(points).contains(new Point(0, 1)));
        assertTrue(Arrays.asList(points).contains(new Point(1, 0)));
        assertTrue(Arrays.asList(points).contains(new Point(1, 1)));
        assertTrue(Arrays.asList(points).contains(new Point(1, 2)));

        Shape o1 = o.rotateCounterclockwise();
        Shape o2 = o1.rotateCounterclockwise();
        Shape o3 = o2.rotateCounterclockwise();
        assertEquals(o1, o);
        assertEquals(o2, o);
        assertEquals(o3, o);

        Shape diy = new Shape("0 0  1 0  1 1  2 0  2 1");
        Shape diy2 = diy.rotateCounterclockwise();
        points = diy2.getPoints();
        assertTrue(Arrays.asList(points).contains(new Point(0, 1)));
        assertTrue(Arrays.asList(points).contains(new Point(0, 2)));
        assertTrue(Arrays.asList(points).contains(new Point(1, 0)));
        assertTrue(Arrays.asList(points).contains(new Point(1, 1)));
        assertTrue(Arrays.asList(points).contains(new Point(1, 2)));
    }


    @Test(timeout = 100)
    public void makeFastRotationsTest() {

        Shape[] shapes = Shape.getShapes();
        Shape t1 = shapes[6];
        assertEquals(t1, t);

        Shape t2 = t1.fastRotation();
        Shape t3 = t2.fastRotation();
        Shape t4 = t3.fastRotation();
        Shape t5 = t4.fastRotation();

        assertNotNull(t2);
        assertNotNull(t3);
        assertNotNull(t4);
        assertNotNull(t5);
        assertNotEquals(t, t2);
        assertNotEquals(t, t3);
        assertNotEquals(t, t4);

        assertTrue(t1.fastRotation().fastRotation() == t3);
        assertTrue(t1 == t5);

        Shape o1 = shapes[5];
        Shape o2 = o1.fastRotation();
        assertEquals(o1, o2);
        assertTrue(o1 == o2);
//
//        for (Shape sh : shapes) {
//            printShape(sh,sh);
//        }


    }


//    private void printShape(Shape sh,Shape root) {
//
//        int height = sh.getHeight();
//        int width = sh.getWidth();
//        int[][] before = new int[height][width];
//        for (Point p : sh.getPoints()) {
//            before[p.y][p.x] = 1;
//        }
//        for (int y = height - 1; y >= 0; y--) {
//            for (int x = 0; x < width; x++) {
//                System.out.print(before[y][x]);
//            }
//            System.out.println("");
//        }
//        System.out.println("-");
//        if(!sh.fastRotation().equals(root)){
//            printShape(sh.fastRotation(),root);
//        }
//    }
}