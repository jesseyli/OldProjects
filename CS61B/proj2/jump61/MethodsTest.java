package jump61;

import static jump61.Color.*;

import org.junit.Test;
import static org.junit.Assert.*;

/** Unit tests of various methods.
 *  @author Jesse Li
 */
public class MethodsTest {

    @Test
    public void testMethods() {
        Board B = new MutableBoard(6);
        assertEquals("wrong row number", 3, B.row(12));
        assertEquals("wrong row number", 3, B.row(17));
        assertEquals("wrong row number", 6, B.row(35));
        assertEquals("wrong column number", 6, B.col(35));
        assertEquals("wrong column number", 1, B.col(6));
        assertEquals("wrong column number", 2, B.col(7));
        assertEquals("wrong square number", 35, B.sqNum(6, 6));
        assertEquals("wrong square number", 0, B.sqNum(1, 1));
        assertEquals("wrong square number", 20, B.sqNum(4, 3));
    }

    @Test
    public void testNeighbors() {
        Board B = new MutableBoard(4);
        Board B2 = new MutableBoard(6);
        assertEquals("wrong number of neighbors", 2, B.neighbors(1, 1));
        assertEquals("wrong number of neighbors", 2, B2.neighbors(1, 1));
        assertEquals("wrong number of neighbors", 3, B.neighbors(1, 3));
        assertEquals("wrong number of neighbors", 3, B2.neighbors(1, 5));
        assertEquals("wrong number of neighbors", 2, B.neighbors(1, 4));
        assertEquals("wrong number of neighbors", 2, B2.neighbors(6, 1));
        assertEquals("wrong number of neighbors", 3, B.neighbors(3, 1));
        assertEquals("wrong number of neighbors", 3, B2.neighbors(4, 1));
        assertEquals("wrong number of neighbors", 2, B.neighbors(0));
        assertEquals("wrong number of neighbors", 2, B2.neighbors(0));
        assertEquals("wrong number of neighbors", 4, B.neighbors(6));
        assertEquals("wrong number of neighbors", 4, B2.neighbors(19));
        assertEquals("wrong number of neighbors", 2, B.neighbors(3));
        assertEquals("wrong number of neighbors", 2, B2.neighbors(30));
    }
}
