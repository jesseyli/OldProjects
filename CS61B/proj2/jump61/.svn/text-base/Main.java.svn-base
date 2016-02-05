package jump61;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.io.IOException;

/** The jump61 game.
 * @author
 */
public class Main {

    /** Location of usage message resource. */
    static final String USAGE = "jump61/Usage.txt";

    /** Play jump61.  ARGS0 may consist of the single string
     *  '--display' to indicate that the game is played using a GUI. Prints
     *  a usage message if the arguments are wrong. */
    public static void main(String[] args0) {
        // FIXME?

        Writer output = new OutputStreamWriter(System.out);
        Game game = new Game(new InputStreamReader(System.in),
                             output, output,
                             new OutputStreamWriter(System.err));
        System.exit(game.play());
    }

    /** Print the contents of the resource named NAME on OUT.
     *  NAME will typically be a file name based in one of the directories
     *  in the class path.  */
    static void printHelpResource(String name, PrintWriter out) {
        try {
            InputStream resource =
                Main.class.getClassLoader().getResourceAsStream(name);
            BufferedReader str =
                new BufferedReader(new InputStreamReader(resource));
            for (String s = str.readLine(); s != null; s = str.readLine())  {
                out.println(s);
            }
            str.close();
            out.flush();
        } catch (IOException excp) {
            out.printf("No help found.");
            out.flush();
        }
    }

    /** Print usage message. */
    private static void usage() {
        printHelpResource(USAGE, new PrintWriter(System.err));
    }

}

