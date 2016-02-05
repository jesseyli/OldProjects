package make;

/* You MAY add public @Test methods to this class.  You may also add
 * additional public classes containing "Testing" in their name. These
 * may not be part of your make package per se (that is, it must be
 * possible to remove them and still have your package work). */

import java.util.ArrayList;

import make.Main.Rule;

import org.junit.Test;

import ucb.junit.textui;
import static org.junit.Assert.*;

/** Unit tests for the make package. */
public class Testing {

    /** Run all JUnit tests in the make package. */
    public static void main(String[] ignored) {
        System.exit(textui.runClasses(make.Testing.class));
    }

    @Test
    public void ruleTest() {
        Rule rule1 = new Rule("Target1");
        Rule rule2 = new Rule("Target2");
        assertEquals("Wrong target", "Target1", rule1.getTarget());
        assertEquals("Wrong target", "Target2", rule2.getTarget());
        assertEquals("Commands should initially be empty", true,
                rule1.getCommands().isEmpty());
        assertEquals("Dependencies should initially be empty", true,
                rule2.getDependencies().isEmpty());
        ArrayList<String> commands = new ArrayList<String>();
        commands.add("command");
        rule1.setCommands(commands);
        assertEquals("Incorrect command", "command",
                rule1.getCommands().get(0));
    }

}
