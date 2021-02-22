import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GamingAreaTest {

    GamingArea gamingArea = new GamingArea(5, 10);
    boolean[][] testBoard = {
            {true, false, true, false, false,false, false, false, false, false},
            {true, true, false, false, false,false, false, false, false, false},
            {true, false, true, false, false,false, false, false, false, false},
            {true, true, true, false, false,false, false, false, false, false},
            {true, true, true, false, false,false, false, false, false, false}
    };


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
        assertTrue(gamingArea.isFilled(1, 100));
        assertTrue(gamingArea.isFilled(100, 100));
    }

    @Test
    public void placeTest() {

        Shape t = new Shape("0 1  1 0  1 1  2 1");
        Shape t2 = new Shape("0 1  1 0  1 1  1 2");
        assertEquals(1, gamingArea.place(t, 0,2));
        System.out.println(gamingArea);
        assertEquals(2, gamingArea.place(t, 50,2));
        assertEquals(3, gamingArea.place(t, 0,0));
        assertEquals(0, gamingArea.place(t, 1,2));
        System.out.println(gamingArea);

        gamingArea.undo();
        assertSame(gamingArea.getCurrentBoard(),gamingArea.getBoard());
        System.out.println(gamingArea);

        try{
            gamingArea.committed = false;
        }catch (Exception e){
            assertEquals(e.getMessage(),"未commit时调用place");
        }
    }


}
