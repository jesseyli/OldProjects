package tex61;

import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.*;

/** Unit tests for LineAssemblers.
 *  @author Jesse Li
 */
public class LineAssemblerTest {

    @Test public void addTextTest() {
        testLineAssembler.addText("qwertyuiop[];lasdfzxb,m");
        assertEquals("Text added incorrectly", "qwertyuiop[];lasdfzxb,m",
            testLineAssembler.getChars());

    }

    @Test public void currIndentTest1() {
        int indent = testLineAssembler.currIndent();
        assertEquals("Current indentation is incorrect", 0, indent);
    }

    @Test public void currIndentTest2() {
        testLineAssembler.setIndentation(7);
        int indent = testLineAssembler.currIndent();
        assertEquals("Current indentation is incorrect", 3, indent);
    }

    @Test public void currIndentTest3() {
        testLineAssembler.setIndentation(7);
        testLineAssembler.setParIndentation(3);
        int indent = testLineAssembler.currIndent();
        assertEquals("Current indentation is incorrect", 10, indent);
    }

    @Test public void totalCharsAndAddWordTest() {
        for (int i = 0; i < 5; i += 1) {
            testLineAssembler.addWord("word");
        }
        ArrayList<String> words = testLineAssembler.getWords();
        assertEquals("The total number of characters is incorrect", 20,
                testLineAssembler.totalChars(words));
    }

    @Test public void outputTest1() {
        String[] words = {"Let's", "see", "if", "this",
            "code", "works", "correctly."};
        String unfinishedword = "understa";
        for (String word: words) {
            testLineAssembler.addWord(word);
        }
        testLineAssembler.addText(unfinishedword);
        testLineAssembler.setFill(false);
        testLineAssembler.newLine();
        assertEquals("The outputted line is incorrect", "Let's see if "
                + "this code works correctly. understa", testPages.get(0));
    }

    @Test public void outputTest2() {
        String[] words = {"This", "line", "should", "not", "be", "justified."};
        for (String word: words) {
            testLineAssembler.addWord(word);
        }
        testLineAssembler.setFill(true);
        testLineAssembler.endParagraph();
        assertEquals("The outputted line is incorrect", "This line should "
                + "not be justified.", testPages.get(0));
    }

    /** An ArrayList for storing the test lines. */
    private ArrayList<String> testPages = new ArrayList<String>();
    /** A PageAssembler that writes to testPages. */
    private PageAssembler testPageAssembler = new PageCollector(testPages);
    /** A LineAssembler that writes to testPageAssembler. */
    private LineAssembler testLineAssembler = new
                                            LineAssembler(testPageAssembler);
}
