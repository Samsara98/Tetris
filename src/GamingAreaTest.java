import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;


import static org.junit.Assert.*;

public class GamingAreaTest {


    GamingArea gamingArea = new GamingArea(5, 10);
    boolean[][] testBoard = {
            {true, false, true, false, false, false, false, false, false, false},
            {true, true, false, false, false, false, false, false, false, false},
            {true, false, true, false, false, false, false, false, false, false},
            {true, true, true, false, false, false, false, false, false, false},
            {true, true, true, false, false, false, false, false, false, false}
    };

    @Rule
    public Timeout timeout = new Timeout(100);


    @Before
    public void before() {

        gamingArea.setBoard(testBoard);
    }


    @Test
    public void GamingAreaTest() {

        assertEquals(5, gamingArea.getAreaWidth());
        assertEquals(10, gamingArea.getAreaHeight());
        assertEquals(5, gamingArea.getBoard().length);
        assertEquals(10, gamingArea.getBoard()[0].length);
        assertTrue("true", gamingArea.committed);
        System.out.println(gamingArea);
    }


    @Test
    public void getMaxHeightTest() {

        assertEquals(2, gamingArea.getMaxHeight());
    }


    @Test
    public void getFilledBlockCountTest() {

        assertEquals(0, gamingArea.getFilledBlockCount(-1));
        assertEquals(5, gamingArea.getFilledBlockCount(0));
        assertEquals(3, gamingArea.getFilledBlockCount(1));
        assertEquals(4, gamingArea.getFilledBlockCount(2));
        assertEquals(0, gamingArea.getFilledBlockCount(3));
        assertEquals(0, gamingArea.getFilledBlockCount(40));
    }


    @Test
    public void getColumnHeightTest() {

        assertEquals(0, gamingArea.getColumnHeight(-1));
        assertEquals(3, gamingArea.getColumnHeight(0));
        assertEquals(2, gamingArea.getColumnHeight(1));
        assertEquals(3, gamingArea.getColumnHeight(2));
        assertEquals(0, gamingArea.getColumnHeight(100));
    }


    @Test
    public void getDropHeightTest() {

        Shape t = new Shape("0 1  1 0  1 1  2 1");
        Shape t2 = new Shape("0 1  1 0  1 1  1 2");
        assertEquals(3, gamingArea.getDropHeight(t, 0));
        assertEquals(4, gamingArea.getDropHeight(t2, 0));
        assertEquals(0, gamingArea.getDropHeight(t2, -1));
        assertEquals(0, gamingArea.getDropHeight(t2, 100));
    }


    @Test
    public void isFilledTest() {

        assertTrue(gamingArea.isFilled(0, 0));
        assertTrue(gamingArea.isFilled(1, 1));
        assertFalse(gamingArea.isFilled(2, 1));
        assertFalse(gamingArea.isFilled(0, 1));
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
        assertEquals(1, gamingArea.place(t, 0, 2));
        gamingArea.commit();
        assertEquals(2, gamingArea.place(t, 4, 0));
        assertEquals(2, gamingArea.place(t, -1, 0));
        assertEquals(2, gamingArea.place(t, 0, -1));
        assertEquals(0, gamingArea.place(t, 0, 9));
        gamingArea.commit();
        assertEquals(3, gamingArea.place(t, 0, 0));
        assertEquals(0, gamingArea.place(t, 1, 2));

        gamingArea.undo();
        assertNotSame(gamingArea.getcache(), gamingArea.getBoard());
        assertArray(gamingArea.getcache(), gamingArea.getBoard());

        try {
            gamingArea.committed = false;
        } catch (Exception e) {
            assertEquals(e.getMessage(), "未commit时调用place");
        }
        gamingArea.commit();
        assertEquals(0, gamingArea.place(t2, 3, 1));

        gamingArea.commit();
        assertEquals(0, gamingArea.place(t, 2, 9));

        gamingArea.commit();
        assertEquals(0, gamingArea.place(l, 4, 7));
    }


    private void assertArray(boolean[][] b1, boolean[][] b2) {

        for (int x = 0; x < b1.length; x++) {
            for (int y = 0; y < b1[0].length; y++) {
                assertEquals(b1[x][y], b2[x][y]);
            }
        }
    }


    @Test
    public void clearTest() {

        Shape i = new Shape("0 0  1 0  2 0  3 0");
        boolean[][] testBoard = {
                {true, false, true, false, false, false, false, false, false, false},
                {true, true, true, false, false, false, false, false, false, false},
                {true, false, true, false, true, false, false, true, false, false},
                {true, true, true, false, false, false, false, false, false, false},
                {true, true, true, false, false, true, false, false, false, false}
        };
        gamingArea.setBoard(testBoard);
        gamingArea.place(i, 0, 5);
        System.out.println(gamingArea);
        boolean[][] testBoard2 = {
                {false, false, false, false, false, false, false, false, false, false},
                {true, false, false, false, false, false, false, false, false, false},
                {false, false, true, false, true, false, false, false, false, false},
                {true, false, false, false, false, false, false, false, false, false},
                {true, false, false, false, false, false, false, false, false, false}
        };
        GamingArea gamingArea2 = new GamingArea(5, 10);
        gamingArea2.setBoard(testBoard2);
        System.out.println(gamingArea2);
        assertArray(gamingArea.getBoard(), testBoard2);
    }
}
