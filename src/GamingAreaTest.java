import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

public class GamingAreaTest implements AI{

    GamingArea gamingArea = new GamingArea(5, 10);
    Shape t;
    Shape t2;

    @Rule
    public Timeout timeout = Timeout.millis(100);

    //运行所有测试前都将运行@Before修饰的方法
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

        assertEquals(2,NumberOfHoles(gamingArea));
        assertEquals(22,RowTransition(gamingArea));
        assertEquals(14,ColumnTransitions(gamingArea));

        gamingArea.place(t,2,2);
        gamingArea.commit();
        gamingArea.place(t2,0,3);
        assertEquals(8,NumberOfHoles(gamingArea));
        assertEquals(2,gamingArea.range());

        assertEquals(15,gamingArea.getPointsNum());

    }

    @Test
    public void test(){
        System.out.println(gamingArea);
        assertEquals(3,WellSums(gamingArea));
        gamingArea.place(t,1,2);
        gamingArea.commit();
        gamingArea.place(t2,0,4);
        gamingArea.commit();

        Shape a = new Shape("0 0");
        gamingArea.place(a,1,1);
        gamingArea.commit();
        gamingArea.place(a,1,2);
        System.out.println(gamingArea);
        assertEquals(10,WellSums(gamingArea));

    }


    @Test
    public void getMaxHeightTest() {

        assertTrue(gamingArea.getMaxHeight() >= 0 && gamingArea.getMaxHeight() <= 10);
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
        assertEquals(4, gamingArea.getMaxHeight());
        gamingArea.commit();
        assertEquals(4, gamingArea.getMaxHeight());
        gamingArea.undo();

        gamingArea.place(t, 0, 10);
        gamingArea.commit();
        assertEquals(4, gamingArea.getMaxHeight());
        gamingArea.undo();
    }


    @Test
    public void getFilledBlockCountTest() {

//        assertEquals(0,gamingArea.getFilledBlockCount(-1));
        assertEquals(3, gamingArea.getFilledBlockCount(0));
        assertEquals(4, gamingArea.getFilledBlockCount(1));
        assertEquals(0, gamingArea.getFilledBlockCount(2));
        assertEquals(0, gamingArea.getFilledBlockCount(3));
//        assertEquals(0,gamingArea.getFilledBlockCount(10));

        gamingArea.place(t, 0, 1);
        assertEquals(5, gamingArea.getFilledBlockCount(1));
        assertEquals(3, gamingArea.getFilledBlockCount(2));
        gamingArea = new GamingArea(5, 1);

        for (int i = 0; i < 10; i++) {
            assertEquals(0, gamingArea.getFilledBlockCount(i));
            assertTrue(gamingArea.getFilledBlockCount(i) >= 0 && gamingArea.getFilledBlockCount(i) <= 10);
        }
    }


    @Test
    public void getColumnHeightTest() {

//        assertEquals(0, gamingArea.getColumnHeight(-1));
        assertEquals(2, gamingArea.getColumnHeight(0));
        assertEquals(1, gamingArea.getColumnHeight(1));
        assertEquals(2, gamingArea.getColumnHeight(2));
        assertEquals(2, gamingArea.getColumnHeight(3));
        assertEquals(2, gamingArea.getColumnHeight(4));
//        assertEquals(0, gamingArea.getColumnHeight(10));

        assertEquals(2, gamingArea.getColumnHeight(0));
        gamingArea.place(t, 0, 8);
        assertEquals(10, gamingArea.getColumnHeight(0));
        assertEquals(10, gamingArea.getColumnHeight(1));
        assertEquals(10, gamingArea.getColumnHeight(2));
    }


    @Test
    public void getDropHeightTest() {

        Shape t = new Shape("0 1  1 0  1 1  2 1");
        Shape t2 = new Shape("0 1  1 0  1 1  1 2");
        Shape t3 = new Shape("0 0  0 1  0 2  1 2");
        Shape t4 = new Shape("0 2  1 1  1 0  1 2");

//        assertEquals(0, gamingArea.getDropHeight(t, -1));
//        assertEquals(0, gamingArea.getDropHeight(t, 10));

        gamingArea.place(t, 0, 8);
        assertEquals(1, gamingArea.getDropHeight(t, 0));
        gamingArea.undo();

        gamingArea.place(t, 0, 8);
        gamingArea.undo();
        assertEquals(1, gamingArea.getDropHeight(t, 0));

        gamingArea.place(t2, 0, 1);
        gamingArea.clearRows();
        gamingArea.commit();

        gamingArea.place(t, 0, 5);
        gamingArea.commit();
//        gamingArea.place(t,0,4);

//        gamingArea.place(t3,2,4);

//        assertEquals(7, gamingArea.getDropHeight(t3, 2));
//        assertEquals(6, gamingArea.getDropHeight(t, 2));
//        assertEquals(5, gamingArea.getDropHeight(t4, 2));
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
        assertTrue(gamingArea.isFilled(1, 10));
        assertTrue(gamingArea.isFilled(5, 10));
        assertTrue(gamingArea.isFilled(1, 11));
        assertTrue(gamingArea.isFilled(1, 12));
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

        assertEquals(GamingArea.OK, gamingArea.place(l, 1, 2));
        gamingArea.undo();

        assertEquals(GamingArea.OK, gamingArea.place(t, 2, 2));
        gamingArea.undo();
        assertEquals(GamingArea.OK, gamingArea.place(t, 0, 8));
        gamingArea.undo();
        assertEquals(GamingArea.OUT, gamingArea.place(t, 0, 9));
        gamingArea.undo();
        assertEquals(GamingArea.OUT, gamingArea.place(t, 0, 10));
        gamingArea.undo();


        assertEquals(GamingArea.ROW_FULL, gamingArea.place(t, 0, 1));
        assertFalse(gamingArea.committed);
        try {
            gamingArea.place(t, 4, 0);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "未commit时调用place");
        }
        gamingArea.undo();

        assertEquals(GamingArea.OUT, gamingArea.place(t, 4, 0));
        gamingArea.undo();
        assertEquals(GamingArea.OUT, gamingArea.place(t, -1, 0));
        gamingArea.undo();
        assertEquals(GamingArea.OUT, gamingArea.place(t, 0, -1));
        gamingArea.undo();
        assertEquals(GamingArea.OUT, gamingArea.place(l, -3, 0));
        gamingArea.undo();
        assertEquals(GamingArea.OUT, gamingArea.place(l, 5, 10));
        gamingArea.undo();
        assertEquals(GamingArea.OUT, gamingArea.place(l, 5, 0));
        gamingArea.undo();
        assertEquals(GamingArea.OUT, gamingArea.place(l, 0, 10));
        gamingArea.undo();

        assertEquals(GamingArea.COLLIDED, gamingArea.place(t, 0, 0));
        gamingArea.undo();
        assertEquals(GamingArea.COLLIDED, gamingArea.place(t, 1, 0));
        gamingArea.undo();
        assertEquals(GamingArea.COLLIDED, gamingArea.place(t, 2, 1));
        gamingArea.undo();
        assertEquals(GamingArea.COLLIDED, gamingArea.place(t, 2, 0));
        gamingArea.undo();
        assertEquals(GamingArea.COLLIDED, gamingArea.place(l, 0, 0));
        gamingArea.undo();
        assertEquals(GamingArea.COLLIDED, gamingArea.place(l, 0, 1));
        gamingArea.undo();

        Shape shape00 = new Shape("0 0");
        assertEquals(GamingArea.OK, gamingArea.place(shape00, 0, 0));
        gamingArea.commit();
        assertEquals(GamingArea.ROW_FULL, gamingArea.place(shape00, 2, 0));
        gamingArea.commit();
        System.out.println(gamingArea);
        assertEquals(GamingArea.OK, gamingArea.place(t, 1, 2));


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
        System.out.println(gamingArea1);

        assertEquals(0, gamingArea.getFilledBlockCount(3));
        assertEquals(2, gamingArea.getColumnHeight(2));
        assertEquals(2, gamingArea.getMaxHeight());

        assertEquals(GamingArea.OK, gamingArea.place(t, 2, 2));

        assertEquals(3, gamingArea.getFilledBlockCount(3));
        assertEquals(4, gamingArea.getColumnHeight(2));
        assertEquals(4, gamingArea.getMaxHeight());

        assertFalse(gamingArea.committed);
        gamingArea.undo();

        assertEquals(0, gamingArea.getFilledBlockCount(3));
        assertEquals(2, gamingArea.getColumnHeight(2));
        assertEquals(2, gamingArea.getMaxHeight());
        assertEquals(1, gamingArea.getDropHeight(t, 0));

        assertTrue(gamingArea.committed);
        areaEquals(gamingArea1, gamingArea);
        //多次undo
        gamingArea.undo();
        areaEquals(gamingArea1, gamingArea);

        assertEquals(GamingArea.ROW_FULL, gamingArea.place(t, 0, 1));
        gamingArea.undo();
        areaEquals(gamingArea1, gamingArea);

        assertEquals(GamingArea.OUT, gamingArea.place(t, 4, 2));
        gamingArea.undo();
        areaEquals(gamingArea1, gamingArea);

        assertEquals(GamingArea.COLLIDED, gamingArea.place(t, 0, 0));
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
        assertEquals(0, gamingArea1.clearRows());
        assertEquals(0, gamingArea.clearRows());

        gamingArea1.place(testBoard, 0, 0);
        gamingArea.place(t, 0, 1);
        assertEquals(1, gamingArea.clearRows());
        areaEquals(gamingArea, gamingArea1);


    }


    @Test
    public void clearTest2() {

        Shape t2 = new Shape("0 1  0 3  1 0  1 1  2 1  2 2  3 1  4 1");

        Shape testBoard2 = new Shape("0 2  1 0  2 1  3 0  4 0");
        GamingArea gamingArea2 = new GamingArea(5, 10);
        gamingArea2.place(testBoard2, 0, 0);

        gamingArea.place(t2, 0, 1);
        assertEquals(2, gamingArea.clearRows());
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
        System.out.println(gamingArea);
        gamingArea.place(l2, 0, 3);
        gamingArea.commit();
        gamingArea.place(l, 1, 1);   //多行消除
        assertEquals(3, gamingArea.clearRows());
        areaEquals(gamingArea, gamingArea3);
    }


    @Test
    public void clearTest4() {

        Shape l = new Shape("0 0  1 0  2 0  3 0  4 0");

        GamingArea gamingArea3 = new GamingArea(5, 10);
        gamingArea = new GamingArea(5, 10);
        assertEquals(GamingArea.OK, gamingArea3.place(t, 0, 0));
        assertEquals(GamingArea.OK, gamingArea.place(t, 0, 0));
        gamingArea.commit();
        assertEquals(GamingArea.ROW_FULL, gamingArea.place(l, 0, 9));
        assertEquals(1, gamingArea.clearRows());
        areaEquals(gamingArea, gamingArea3);
    }

}
