package jump61;

import ucb.junit.textui;

/** The suite of all JUnit tests for the Text Formatter.
 *  @author Jesse Li
 */
public class UnitTest {

    /** Run the JUnit tests in the tex61 package. */
    public static void main(String[] ignored) {
        textui.runClasses(jump61.BoardTest.class);
        textui.runClasses(jump61.GameTest.class);
        textui.runClasses(jump61.MethodsTest.class);
    }

}


