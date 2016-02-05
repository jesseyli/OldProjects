package tex61;

import java.util.List;
import java.util.ArrayList;
import java.io.StringWriter;
import java.io.PrintWriter;

import org.junit.Test;
import static org.junit.Assert.*;

/** Unit tests for PageAssemblers.
 *  @author Jesse Li
 */
public class InputParserTest {

    void setupParser(String text) {
        testParser = new InputParser(text, testController);
    }

    @Test
    public void testPrinterContents1() {
        setupParser("Hello World");
        testParser.process();
        assertEquals("wrong output: printer", "   Hello World",
                output.toString());
    }

    /** Collects output to a PrintWriter. */
    private StringWriter output = new StringWriter();
    /** A PrintWriter that testController will send output to. */
    private PrintWriter testWriter = new PrintWriter(output);
    /** A test controller that will receive tokens from testParser. */
    private Controller testController = new Controller(testWriter);
    /** A test inputparser that will be passed a string. */
    private InputParser testParser;
}
