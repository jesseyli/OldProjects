package tex61;

import static tex61.FormatException.error;

/** A PageAssembler accepts complete lines of text (minus any
 *  terminating newlines) and turns them into pages, adding form
 *  feeds as needed.  It prepends a form feed (Control-L  or ASCII 12)
 *  to the first line of each page after the first.  By overriding the
 *  'write' method, subtypes can determine what is done with
 *  the finished lines.
 *  @author Jesse Li
 */
abstract class PageAssembler {

    /** Create a new PageAssembler that sends its output to OUT.
     *  Initially, its text height is unlimited. It prepends a form
     *  feed character to the first line of each page except the first. */
    PageAssembler() {
        _textHeight = Integer.MAX_VALUE;
        _numLines = 0;
    }

    /** Add LINE to the current page, starting a new page with it if
     *  the previous page is full. A null LINE indicates a skipped line,
     *  and has no effect at the top of a page. */
    void addLine(String line) {
        if (_numLines == _textHeight) {
            if (line != null) {
                _numLines = 1;
                write("\f" + line);
            }
        } else {
            if ((line == null) && _numLines != 0) {
                line = "";
            }
            if (line != null) {
                _numLines += 1;
            }
            write(line);
        }
    }

    /** Set text height to VAL, where VAL > 0. */
    void setTextHeight(int val) {
        if (val > 0) {
            _textHeight = val;
        } else {
            throw error("Text height must be greater than or "
                        + "equal to 0.", val);
        }
    }

    /** Returns the current number of lines on page.  Used for unit testing. */
    int getnumLines() {
        return _numLines;
    }

    /** Perform final disposition of LINE, as determined by the
     *  concrete subtype. */
    abstract void write(String line);

    /** The current text height. */
    private int _textHeight;

    /** The number of lines on the current page. */
    private int _numLines;

}
