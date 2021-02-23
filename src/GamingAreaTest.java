import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;


import static org.junit.Assert.*;

public class GamingAreaTest {


    GamingArea gamingArea = new GamingArea(5, 10);
//    boolean[][] testBoard = {
//            {true, false, true, false, false, false, false, false, false, false},
//            {true, true, false, false, false, false, false, false, false, false},
//            {true, false, true, false, false, false, false, false, false, false},
//            {true, true, true, false, false, false, false, false, false, false},
//            {true, true, true, false, false, false, false, false, false, false}
//    };

    @Rule
    public Timeout timeout = new Timeout(100);


    @Before
    public void before() {

        assertTrue("true", gamingArea.committed);
        Shape testBoard = new Shape("0 0  1 0  2 0  3 0  4 0  1 1  2 2  0 2  3 1  3 2  4 1  4 2");
        gamingArea.place(testBoard, 0, 0);
    }


    @Test
    public void gamingAreaTest() {

        assertEquals(5, gamingArea.getAreaWidth());
        assertEquals(10, gamingArea.getAreaHeight());
        assertFalse(gamingArea.committed);
        gamingArea.commit();
        assertTrue(gamingArea.committed);
    }


    @Test
    public void getMaxHeightTest() {

        GamingArea gamingArea2 = new GamingArea(5, 10);
        assertEquals(0, gamingArea2.getMaxHeight());

        gamingArea2.commit();
        Shape testBoard = new Shape("0 0 ");
        gamingArea2.place(testBoard, 0, 0);
        assertEquals(1, gamingArea2.getMaxHeight());


        Shape t11 = new Shape("0 1  1 0  1 1  2 1");
        gamingArea.commit();
        assertEquals(0, gamingArea.place(t11, 0, 3));
        assertEquals(5, gamingArea.getMaxHeight());
        gamingArea.undo();

        assertEquals(0, gamingArea.place(t11, 0, 9));
        assertEquals(10, gamingArea.getMaxHeight());
        gamingArea.undo();

        assertEquals(0, gamingArea.place(t11, 0, 8));
        assertEquals(10, gamingArea.getMaxHeight());
        gamingArea.undo();
    }


    @Test
    public void getFilledBlockCountTest() {

        System.out.println(gamingArea);
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

        Shape t = new Shape("0 1  1 0  1 1  2 1");
        Shape t2 = new Shape("0 1  1 0  1 1  1 2");
        Shape l = new Shape("0 0  0 1  0 2  0 3");
        gamingArea.commit();
        assertEquals(1, gamingArea.place(t, 0, 1));
        System.out.println(gamingArea);
        assertFalse(gamingArea.committed);
        try {
            gamingArea.committed = false;
        } catch (Exception e) {
            assertEquals(e.getMessage(), "未commit时调用place");
        }
        gamingArea.commit();
        assertTrue(gamingArea.committed);
        assertEquals(2, gamingArea.place(t, 4, 0));
        gamingArea.undo();
        assertEquals(2, gamingArea.place(t, -1, 0));
        gamingArea.undo();
        assertEquals(2, gamingArea.place(t, 0, -1));
        gamingArea.undo();
        assertEquals(0, gamingArea.place(t, 0, 9));
        gamingArea.commit();
        assertEquals(3, gamingArea.place(t, 0, 0));
        gamingArea.undo();
        assertEquals(0, gamingArea.place(t, 1, 2));

        gamingArea.undo();

        assertEquals(0, gamingArea.place(t2, 3, 1));

        gamingArea.commit();
        assertEquals(0, gamingArea.place(t, 2, 9));

        gamingArea.commit();
        assertEquals(0, gamingArea.place(l, 4, 7));
    }

    @Test
    public void  undoTest(){

        GamingArea gamingArea1 = new GamingArea(5, 10);
        assertEquals(0, gamingArea1.getMaxHeight());

        GamingArea gamingArea2 = new GamingArea(5, 10);
        assertEquals(0, gamingArea2.getMaxHeight());

        gamingArea2.commit();
        Shape testBoard = new Shape("0 0 ");
        gamingArea2.place(testBoard, 0, 0);
        gamingArea2.undo();
        for (int x = 0; x < gamingArea2.getAreaWidth(); x++) {
            for (int y = 0; y < gamingArea2.getAreaHeight(); y++) {
                assertEquals(gamingArea1.isFilled(x,y),gamingArea2.isFilled(x,y));
            }
        }
    }

    @Test
    public void clearTest() {

        Shape testBoard = new Shape("0 0  1 0  2 0  3 0  4 0  1 1  2 2  0 2");
        GamingArea gamingArea1 = new GamingArea(5, 10);
        gamingArea1.place(testBoard, 0, 0);

        Shape testBoard2 = new Shape("1 0  2 1  0 1");
        GamingArea gamingArea2 = new GamingArea(5, 10);
        gamingArea2.place(testBoard2, 0, 0);

        for (int x = 0; x < gamingArea2.getAreaWidth(); x++) {
            for (int y = 0; y < gamingArea2.getAreaHeight(); y++) {
                assertEquals(gamingArea1.isFilled(x,y),gamingArea2.isFilled(x,y));
            }
        }

    }
}
