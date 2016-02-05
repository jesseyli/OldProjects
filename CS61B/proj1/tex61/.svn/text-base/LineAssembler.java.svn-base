package tex61;

import java.util.ArrayList;

import static tex61.FormatException.error;

/** An object that receives a sequence of words of text and formats
 *  the words into filled and justified text lines that are sent to a receiver.
 *  @author
 */
class LineAssembler {

    /** A new, empty line assembler with default settings of all
     *  parameters, sending finished lines to PAGES. */
    LineAssembler(PageAssembler pages) {
        _pages = pages;
        // FIXME
    }

    /** Add TEXT to the word currently being built. */
    void addText(String text) {
        // FIXME
    }

    /** Finish the current word, if any, and add to words being accumulated. */
    void finishWord() {
        // FIXME
    }

    /** Add WORD to the formatted text. */
    void addWord(String word) {
        // FIXME
    }

    /** Add LINE to our output, with no preceding paragraph skip.  There must
     *  not be an unfinished line pending. */
    void addLine(String line) {
        // FIXME
    }

    /** Set the current indentation to VAL. VAL >= 0. */
    void setIndentation(int val) {
        // FIXME
    }

    /** Set the current paragraph indentation to VAL. VAL >= 0. */
    void setParIndentation(int val) {
        // FIXME
    }

    /** Set the text width to VAL, where VAL >= 0. */
    void setTextWidth(int val) {
        // FIXME
    }

    /** Iff ON, set fill mode. */
    void setFill(boolean on) {
        // FIXME
    }

    /** Iff ON, set justify mode (which is active only when filling is
     *  also on). */
    void setJustify(boolean on) {
        // FIXME
    }

    /** Set paragraph skip to VAL.  VAL >= 0. */
    void setParSkip(int val) {
        // FIXME
    }

    /** Set page height to VAL > 0. */
    void setTextHeight(int val) {
        // FIXME
    }

    /** Process the end of the current input line.  No effect if
     *  current line accumulator is empty or in fill mode.  Otherwise,
     *  adds a new complete line to the finished line queue and clears
     *  the line accumulator. */
    void newLine() {
        // FIXME
    }

    /** If there is a current unfinished paragraph pending, close it
     *  out and start a new one. */
    void endParagraph() {
        // FIXME
    }

    /** Transfer contents of _words to _pages, adding INDENT characters of
     *  indentation, and a total of SPACES spaces between words, evenly
     *  distributed.  Assumes _words is not empty.  Clears _words and _chars. */
    private void emitLine(int indent, int spaces) {
        // FIXME
    }

    /** If the line accumulator is non-empty, justify its current
     *  contents, if needed, add a new complete line to _pages,
     *  and clear the line accumulator. LASTLINE indicates the last line
     *  of a paragraph. */
    private void outputLine(boolean lastLine) {
        // FIXME
    }

    /** Destination given in constructor for formatted lines. */
    private final PageAssembler _pages;

}
