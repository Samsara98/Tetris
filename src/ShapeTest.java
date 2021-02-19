import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class ShapeTest {
    private Shape t;
    private Shape t2;
    private Shape i;
    private Shape l;
    private Shape j;
    private Shape s;
    private Shape z;
    private Shape o;

    @Before
    public void setUp() {
        t = new Shape(Shape.T_STR);
        t2 = new Shape(Shape.T_STR);
        i = new Shape(Shape.I_STR);
        l = new Shape(Shape.L_STR);
        j = new Shape(Shape.J_STR);
        s = new Shape(Shape.S_STR);
        z = new Shape(Shape.Z_STR);
        o = new Shape(Shape.O_STR);
    }

    @Test
    public void stupidTest() {
        // 形状T
        assertEquals(4, t.getPoints().length);
        assertTrue(Arrays.asList(t.getPoints()).contains(new Point(1, 0)));

        // 形状L
        Shape l = new Shape(Shape.L_STR);
        assertTrue(Arrays.asList(l.getPoints()).contains(new Point(0, 1)));

    }

    @Test
    public void shapeTest(){
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

    @Test
    public void equalsTest(){
        assertEquals(t, t2);
        assertEquals(o, o);
        assertNotEquals(o, l);
        assertNotEquals(j, l);
        assertNotEquals(i, l);
        assertNotEquals(j, s);
    }

    @Test
    public void rotateCounterclockwiseTest(){
        Shape t1 = t.rotateCounterclockwise();
        Shape t2 = t1.rotateCounterclockwise();
        Shape t3 = t2.rotateCounterclockwise();
        Shape t4 = t3.rotateCounterclockwise();
        assertNotEquals(t1,t);
        assertEquals(t,t4);

        Shape o1 = o.rotateCounterclockwise();
        Shape o2 = o1.rotateCounterclockwise();
        assertEquals(o1,o);
        assertEquals(o2,o);
        assertEquals(o2,o1);
    }

    @Test
    public void makeFastRotationsTest(){
        Shape[] shapes = Shape.getShapes();
        Shape i1 = shapes[0];
        assertEquals(i1,i);

        Shape i2 = i1.fastRotation();
        Shape i3 = i.rotateCounterclockwise();
        assertEquals(i2,i3);
    }
}