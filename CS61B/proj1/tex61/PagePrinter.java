package tex61;

import java.io.PrintWriter;

/** A PageAssembler that sends lines immediately to a PrintWriter, with
 *  terminating newlines.
 *  @author Jesse Li
 */
class PagePrinter extends PageAssembler {

    /** A new PagePrinter that sends lines to OUT. */
    PagePrinter(PrintWriter out) {
        super();
        _out = out;
    }

    /** Print LINE to my output. */
    @Override
    void write(String line) {
        if (line != null) {
            _out.println(line);
        }
    }

    /** The output to which I am printing. */
    private PrintWriter _out;
}
