package jump61;

import static jump61.Color.*;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.junit.Test;

import static org.junit.Assert.*;

/** Unit tests of Game.
 *  @author Jesse Li
 */
public class GameTest {

    @Test
    public void testGameMethods() {
        Writer output = new OutputStreamWriter(System.out);
        Game G = new Game(new InputStreamReader(System.in),
                output, output,
                new OutputStreamWriter(System.err));
        int[] moveholder = {0, 0};
        assertEquals("First player is incorrect", RED,
                G.nextPlayer().getColor());
        assertEquals("getMove should be false at start of game", false,
                G.getMove(moveholder));
        assertEquals("Blue player should be an AI initially", true,
                G.getPlayer2() instanceof AI);
        assertEquals("Red player should be manual initially", true,
                G.getPlayer1() instanceof HumanPlayer);
    }
}

