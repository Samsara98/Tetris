import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;


import static org.junit.Assert.*;

public class GamingAreaTest {


    GamingArea gamingArea = new GamingArea(5, 10);
    Shape t;
    Shape t2;

    @Rule
    public Timeout timeout = new Timeout(100);


    @Before
    public void before() {

        assertTrue("true", gamingArea.committed);
        Shape testBoard = new Shape("0 1  1 0  3 0  4 0  2 1  3 1  4 1");
        gamingArea.place(testBoard, 0, 0);
        gamingArea.commit();

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


        gamingArea.place(t, 2, 2);
        assertEquals(4, gamingArea.getMaxHeight());
        gamingArea.undo();

        gamingArea.place(t, 0, 8);
        assertEquals(10, gamingArea.getMaxHeight());
        gamingArea.undo();

        gamingArea.place(t, 0, 9);
        assertEquals(10, gamingArea.getMaxHeight());
        gamingArea.undo();

        gamingArea.place(t, 0, 10);
        assertEquals(2, gamingArea.getMaxHeight());
        gamingArea.undo();
    }


    @Test
    public void getFilledBlockCountTest() {

        assertEquals(0, gamingArea.getFilledBlockCount(-1));
        assertEquals(3, gamingArea.getFilledBlockCount(0));
        assertEquals(4, gamingArea.getFilledBlockCount(1));
        assertEquals(0, gamingArea.getFilledBlockCount(2));
        assertEquals(0, gamingArea.getFilledBlockCount(3));
        assertEquals(0, gamingArea.getFilledBlockCount(40));
    }


    @Test
    public void getColumnHeightTest() {

        assertEquals(0, gamingArea.getColumnHeight(-1));
        assertEquals(2, gamingArea.getColumnHeight(0));
        assertEquals(1, gamingArea.getColumnHeight(1));
        assertEquals(2, gamingArea.getColumnHeight(2));
        assertEquals(2, gamingArea.getColumnHeight(3));
        assertEquals(2, gamingArea.getColumnHeight(4));
        assertEquals(0, gamingArea.getColumnHeight(100));
    }


    @Test
    public void getDropHeightTest() {

        Shape t = new Shape("0 1  1 0  1 1  2 1");
        Shape t2 = new Shape("0 1  1 0  1 1  1 2");
        assertEquals(2, gamingArea.getDropHeight(t, 0));
        assertEquals(3, gamingArea.getDropHeight(t2, 0));
        assertEquals(0, gamingArea.getDropHeight(t2, -1));
        assertEquals(0, gamingArea.getDropHeight(t2, 100));
    }


    @Test
    public void isFilledTest() {

        assertFalse(gamingArea.isFilled(0, 0));
        assertFalse(gamingArea.isFilled(1, 1));
        assertTrue(gamingArea.isFilled(2, 1));
        assertTrue(gamingArea.isFilled(0, 1));

        assertTrue(gamingArea.isFilled(100, 1));
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
        gamingArea.place(t,2,2);
        areaEquals(gamingArea1,gamingArea);
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
    public void  commitTest(){
        gamingArea.place(t,0,1);
        assertFalse(gamingArea.committed);
        gamingArea.commit();
        assertTrue(gamingArea.committed);
    }

    @Test
    public void  undoTest(){

        GamingArea gamingArea1 = new GamingArea(5,10);
        Shape testBoard = new Shape("0 0  1 0  2 0  3 0  4 0  1 1  2 2  0 2  3 1  3 2  4 1  4 2");
        gamingArea1.place(testBoard, 0, 0);
        gamingArea1.commit();

        gamingArea.place(t,2,2);

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

        Shape l = new Shape("0 0  0 1  0 2  0 3");
        Shape l2 = new Shape("0 0  2 0  3 0  4 0");
        Shape t2 = new Shape("0 1  1 0  1 1  2 1  3 1  4 1");

        Shape testBoard = new Shape("1 0  3 0  4 0  0 1  1 1  2 1");
        GamingArea gamingArea1 = new GamingArea(5, 10);
        gamingArea1.place(testBoard, 0, 0);
        gamingArea.place(t,0,1);
        areaEquals(gamingArea, gamingArea1);
        gamingArea.undo();

        Shape testBoard2 = new Shape("1 0  3 0  4 0");
        GamingArea gamingArea2 = new GamingArea(5, 10);
        gamingArea2.place(testBoard2, 0, 0);
        gamingArea.place(t2,0,1);
        areaEquals(gamingArea, gamingArea2);
        gamingArea.undo();



        Shape testBoard3 = new Shape("1 0  1 1  3 0  4 0");
        GamingArea gamingArea3 = new GamingArea(5, 10);
        gamingArea3.place(testBoard3, 0, 0);
        gamingArea.place(l2, 0, 2);
        gamingArea.commit();
        gamingArea.place(l2, 0, 3);
        gamingArea.commit();
        gamingArea.place(l2, 0, 3);
        gamingArea.commit();
        gamingArea.place(l, 1,1);   //多行消除
        areaEquals(gamingArea,gamingArea3);
    }
}
