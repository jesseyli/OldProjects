package tex61;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

public class test {

    public static void main(String[] args) {
        /** Collects output to a PrintWriter. */
         StringWriter output = new StringWriter();
        /** A PrintWriter that testController will send output to. */
         PrintWriter testWriter = new PrintWriter(output);
        /** A test controller that will receive tokens from testParser. */
        Controller testController = new Controller(testWriter);
        /** A test inputparser that will be passed a string. */
        InputParser testParser = new InputParser("Hello my name", testController);
        testParser.process();
       System.out.println(output.toString());

    }
}
