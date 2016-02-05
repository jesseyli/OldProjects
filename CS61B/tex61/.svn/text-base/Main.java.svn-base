package tex61;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.IOException;
import java.io.PrintWriter;

import static tex61.FormatException.reportError;
import static tex61.FormatException.getTotalErrors;

/** Simple Text Formatter. Main entry point.
 * @author Paul N. Hilfinger
 */
public class Main {

    /** Format the file ARGS[0], producing output on the standard
     *  output if ARGS.length is 1, and otherwise on ARGS[1].  ARGS must
     *  have length 1 or 2.  Print a usage message otherwise or if the
     *  files are unreadable or unwritable, respectively. */
    public static void main(String[] args) {
        if (args.length == 0) {
            usage();
            return;
        }

        if (args.length > 2) {
            reportError("too many command-line arguments");
            usage();
            System.exit(1);
        }

        try {
            Reader input;
            input = new FileReader(new File(args[0]));

            PrintWriter output;

            if (args.length == 2) {
                output = new PrintWriter(new File(args[1]));
            } else {
                output = new PrintWriter(System.out);
            }

            Controller cntrl = new Controller(output);
            InputParser src = new InputParser(input, cntrl);
            src.process();
            output.close();
        } catch (IOException e) {
            reportError(e.getMessage());
            System.exit(1);
            return;
        }

        System.exit(getTotalErrors() == 0 ? 0 : 1);
    }

    /** Print usage message. */
    private static void usage() {
        System.out.printf("Usage: java format.Main INFILE [OUTFILE]%n"
                          + "   Format INFILE, sending output to OUTFILE "
                          + "(default: standard output).%n");
    }

}

