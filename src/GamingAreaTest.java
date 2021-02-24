import jdk.swing.interop.SwingInterOpUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class GamingAreaTest {


    GamingArea gamingArea = new GamingArea(5, 10);
    Shape t;
    Shape t2;

    @Rule
    public Timeout timeout = new Timeout(100);


    @Before
    public void before() {

        isFilledTest();

        assertTrue("true", gamingArea.committed);
        Shape testBoard = new Shape("0 1  1 0  3 0  4 0  2 1  3 1  4 1");
        gamingArea.place(testBoard, 0, 0);
        gamingArea.commit();

        isFilledTest2();

        t = new Shape("0 1  1 0  1 1  2 1");
        t2 = new Shape("0 1  1 0  1 1  1 2");


    }


    @Test
    public void gamingAreaTest() {

        assertEquals(5, gamingArea.getAreaWidth());
        assertEquals(10, gamingArea.getAreaHeight());
        assertTrue(gamingArea.committed);
    }


    @Test
    public void getMaxHeightTest() {

        //空场景高度为0
        GamingArea gamingArea2 = new GamingArea(5, 10);
        assertEquals(0, gamingArea2.getMaxHeight());

        //一个点高度为1
        gamingArea2.commit();
        Shape testBoard = new Shape("0 0 ");
        gamingArea2.place(testBoard, 0, 0);
        assertEquals(1, gamingArea2.getMaxHeight());
        gamingArea2.commit();
        assertEquals(1, gamingArea2.getMaxHeight());


        gamingArea.place(t, 2, 2);
        gamingArea.commit();
        assertEquals(4, gamingArea.getMaxHeight());
        gamingArea.undo();

        gamingArea.place(t, 0, 9);
        System.out.println(gamingArea);
        assertEquals(10, gamingArea.getMaxHeight());
        gamingArea.commit();
        assertEquals(10, gamingArea.getMaxHeight());
        gamingArea.undo();

        gamingArea.place(t, 0, 10);
        gamingArea.commit();
        assertEquals(10, gamingArea.getMaxHeight());
        gamingArea.undo();
    }


    @Test
    public void getFilledBlockCountTest() {

        assertEquals(3, gamingArea.getFilledBlockCount(0));
        assertEquals(4, gamingArea.getFilledBlockCount(1));
        assertEquals(0, gamingArea.getFilledBlockCount(2));
        assertEquals(0, gamingArea.getFilledBlockCount(3));
    }


    @Test
    public void getColumnHeightTest() {


        assertEquals(2, gamingArea.getColumnHeight(0));
        assertEquals(1, gamingArea.getColumnHeight(1));
        assertEquals(2, gamingArea.getColumnHeight(2));
        assertEquals(2, gamingArea.getColumnHeight(3));
        assertEquals(2, gamingArea.getColumnHeight(4));

        assertEquals(2, gamingArea.getColumnHeight(0));
        gamingArea.place(t, 0, 8);
        assertEquals(10, gamingArea.getColumnHeight(0));
    }


    @Test
    public void getDropHeightTest() {

        Shape t = new Shape("0 1  1 0  1 1  2 1");
        Shape t2 = new Shape("0 1  1 0  1 1  1 2");
        Shape t3 = new Shape("0 0  0 1  0 2  1 2");

        gamingArea.place(t, 0, 8);
        assertEquals(1, gamingArea.getDropHeight(t, 0));
        gamingArea.undo();

        gamingArea.place(t, 1, 8);
        gamingArea.undo();
        assertEquals(2, gamingArea.getDropHeight(t2, 1));

        gamingArea.place(t2, 0, 1);
        gamingArea.clearRows();
        gamingArea.commit();

        gamingArea.place(t, 0, 6);
        gamingArea.commit();
//        gamingArea.place(t,0,4);
        assertEquals(8, gamingArea.getDropHeight(t, 0));
        gamingArea.undo();

//        gamingArea.place(t3,2,4);
        assertEquals(8, gamingArea.getDropHeight(t3, 2));

    }


    public void isFilledTest() {

        assertFalse(gamingArea.isFilled(0, 1));
        assertFalse(gamingArea.isFilled(1, 0));
        assertFalse(gamingArea.isFilled(3, 0));
        assertFalse(gamingArea.isFilled(4, 0));
        assertFalse(gamingArea.isFilled(2, 1));
        assertFalse(gamingArea.isFilled(3, 1));
        assertFalse(gamingArea.isFilled(4, 1));
    }


    public void isFilledTest2() {

        assertFalse(gamingArea.isFilled(0, 0));
        assertFalse(gamingArea.isFilled(1, 1));
        assertTrue(gamingArea.isFilled(0, 1));
        assertTrue(gamingArea.isFilled(1, 0));
        assertTrue(gamingArea.isFilled(3, 0));
        assertTrue(gamingArea.isFilled(4, 0));
        assertTrue(gamingArea.isFilled(2, 1));
        assertTrue(gamingArea.isFilled(3, 1));
        assertTrue(gamingArea.isFilled(4, 1));

        assertTrue(gamingArea.isFilled(100, 1));
        assertTrue(gamingArea.isFilled(-1, 1));
        assertTrue(gamingArea.isFilled(-1, -1));
        assertFalse(gamingArea.isFilled(1, 9));
        assertFalse(gamingArea.isFilled(1, 10));
        assertFalse(gamingArea.isFilled(1, 11));
        assertFalse(gamingArea.isFilled(1, 12));
        assertTrue(gamingArea.isFilled(100, 100));
    }


    @Test
    public void placeTest() {

        Shape l = new Shape("0 0  0 1  0 2  0 3");

        Shape testBoard = new Shape("0 1  1 0  3 0  4 0  2 1  3 1  4 1  2 3  3 3  3 2  4 3");
        GamingArea gamingArea1 = new GamingArea(5, 10);
        gamingArea1.place(testBoard, 0, 0);
        gamingArea.place(t, 2, 2);
        areaEquals(gamingArea1, gamingArea);
        gamingArea.undo();

        assertEquals(0, gamingArea.place(l, 1, 2));
        gamingArea.undo();

        assertEquals(0, gamingArea.place(t, 2, 2));
        gamingArea.undo();
        assertEquals(0, gamingArea.place(t, 0, 8));
        gamingArea.undo();
        assertEquals(0, gamingArea.place(t, 0, 9));
        gamingArea.undo();
        assertEquals(0, gamingArea.place(t, 0, 10));
        gamingArea.undo();


        assertEquals(1, gamingArea.place(t, 0, 1));
        assertFalse(gamingArea.committed);
        try {
            gamingArea.place(t, 4, 0);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "未commit时调用place");
        }
        gamingArea.undo();

        assertEquals(2, gamingArea.place(t, 4, 0));
        gamingArea.undo();
        assertEquals(2, gamingArea.place(t, -1, 0));
        gamingArea.undo();
        assertEquals(2, gamingArea.place(t, 0, -1));
        gamingArea.undo();
        assertEquals(2, gamingArea.place(l, -3, 0));
        gamingArea.undo();

        assertEquals(3, gamingArea.place(t, 0, 0));
        gamingArea.undo();
        assertEquals(3, gamingArea.place(t, 1, 0));
        gamingArea.undo();
        assertEquals(3, gamingArea.place(t, 2, 1));
        gamingArea.undo();
        assertEquals(3, gamingArea.place(t, 2, 0));
        gamingArea.undo();
        assertEquals(3, gamingArea.place(l, 0, 0));
        gamingArea.undo();
        assertEquals(3, gamingArea.place(l, 0, 1));
        gamingArea.undo();

    }


    @Test
    public void commitTest() {

        gamingArea.place(t, 0, 1);
        assertFalse(gamingArea.committed);
        gamingArea.commit();
        assertTrue(gamingArea.committed);
    }


    @Test
    public void undoTest() {

        GamingArea gamingArea1 = new GamingArea(5, 10);
        Shape testBoard = new Shape("1 0  2 1  0 1  3 0  3 1  4 0  4 1");
        gamingArea1.place(testBoard, 0, 0);

        gamingArea.place(t, 2, 2);
        assertFalse(gamingArea.committed);
        gamingArea.undo();
        assertTrue(gamingArea.committed);
        areaEquals(gamingArea1, gamingArea);

        gamingArea.undo();
        areaEquals(gamingArea1, gamingArea);
    }


    private void areaEquals(GamingArea gamingArea, GamingArea gamingArea1) {

        for (int x = 0; x < gamingArea.getAreaWidth(); x++) {
            for (int y = 0; y < gamingArea.getAreaHeight(); y++) {
                assertEquals(gamingArea.isFilled(x, y), gamingArea1.isFilled(x, y));
            }
        }
    }


    @Test
    public void clearTest() {

        Shape testBoard = new Shape("1 0  3 0  4 0  0 1  1 1  2 1");
        GamingArea gamingArea1 = new GamingArea(5, 10);
        gamingArea1.place(testBoard, 0, 0);
        gamingArea.place(t, 0, 1);
        gamingArea.clearRows();
        areaEquals(gamingArea, gamingArea1);

    }


    @Test
    public void clearTest2() {

        Shape t2 = new Shape("0 1  1 0  1 1  2 1  3 1  4 1");

        Shape testBoard2 = new Shape("1 0  3 0  4 0");
        GamingArea gamingArea2 = new GamingArea(5, 10);
        gamingArea2.place(testBoard2, 0, 0);
        gamingArea.place(t2, 0, 1);
        gamingArea.clearRows();
        areaEquals(gamingArea, gamingArea2);
    }


    @Test
    public void clearTest3() {

        Shape l = new Shape("0 0  0 1  0 2  0 3");
        Shape l2 = new Shape("0 0  2 0  3 0  4 0");

        Shape testBoard3 = new Shape("1 0  1 1  3 0  4 0");
        GamingArea gamingArea3 = new GamingArea(5, 10);
        gamingArea3.place(testBoard3, 0, 0);
        gamingArea.place(l2, 0, 2);
        gamingArea.commit();
        gamingArea.place(l2, 0, 3);
        gamingArea.commit();
        gamingArea.place(l2, 0, 3);
        gamingArea.commit();
        gamingArea.place(l, 1, 1);   //多行消除
        gamingArea.clearRows();
        areaEquals(gamingArea, gamingArea3);
    }


    @Test
    public void a() {

        int[] customers = new int[]{1, 0, 1, 2, 1, 1, 7, 5};
        int[] grumpy = new int[]{0, 1, 0, 1, 0, 1, 0, 1};
        int X = 3;


        int satisfaction = 0;
        for (int i = 0; i < customers.length; i++) {
            if (grumpy[i] == 0) {
                satisfaction += customers[i];
            }
        }

        int increase = 0;
        for (int i = 0; i < X; i++) {
            increase += customers[i] * grumpy[i];
        }
        int maxIncrease = increase;

        for (int i = 1; i <= customers.length - X; i++) {
            increase = increase - customers[i - 1] * grumpy[i - 1] + customers[i + X-1] * grumpy[i + X-1];
            maxIncrease = Math.max(increase, maxIncrease);
        }
        System.out.println(satisfaction + maxIncrease);
    }


}
