package trip;

/* You MAY add public @Test methods to this class.  You may also add
 * additional public classes containing "Testing" in their name. These
 * may not be part of your trip package per se (that is, it must be
 * possible to remove them and still have your package work). */

import org.junit.Test;
import trip.Main.Distance;
import trip.Main.Location;
import ucb.junit.textui;
import static org.junit.Assert.*;

/** Unit tests for the trip package. */
public class Testing {

    /** Run all JUnit tests in the graph package. */
    public static void main(String[] ignored) {
        System.exit(textui.runClasses(trip.Testing.class));
    }

    @Test
    public void locationTest() {
        Location loc1 = new Location("Berkeley", 0, 0);
        Location loc2 = new Location("San Francisco", 5, 0);
        loc1.setWeight(3);
        loc2.setWeight(7);
        assertEquals(3, loc1.weight(), 0.1);
        assertEquals(7, loc2.weight(), 0.1);
    }

    @Test
    public void distanceTest() {
        Distance dist = new Distance("Berkeley", "1", 5, "West",
                "San Francisco");
        assertEquals("Wrong starting city", "Berkeley", dist.getStart());
        assertEquals("Wrong ending city", "San Francisco", dist.getEnd());
        assertEquals(5, dist.getLength(), 0.1);
        assertEquals("Wrong road name", "1", dist.getRoad());
        assertEquals("Wrong direction", "West", dist.getDir());
    }
}
