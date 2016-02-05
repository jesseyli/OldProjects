package tex61;

import java.io.PrintWriter;

import static tex61.FormatException.reportError;

/** Receives (partial) words and commands, performs commands, and
 *  accumulates and formats words into lines of text, which are sent to a
 *  designated PageAssembler.  At any given time, a Controller has a
 *  current word, which may be added to by addText, a current list of
 *  words that are being accumulated into a line of text, and a list of
 *  lines of endnotes.
 *  @author
 */
class Controller {

    /** A new Controller that sends formatted output to OUT. */
    Controller(PrintWriter out) {
        // FIXME
    }

    /** Add TEXT to the end of the word of formatted text currently
     *  being accumulated. */
    void addText(String text) {
        // FIXME
    }

    /** Finish any current word of text and, if present, add to the
     *  list of words for the next line.  Has no effect if no unfinished
     *  word is being accumulated. */
    void endWord() {
        // FIXME
    }

    /** Finish any current word of formatted text and process an end-of-line
     *  according to the current formatting parameters. */
    void addNewline() {
        // FIXME
    }

    /** Finish any current word of formatted text, format and output any
     *  current line of text, and start a new paragraph. */
    void endParagraph() {
        // FIXME
    }

    /** If valid, process TEXT into an endnote, first appending a reference
     *  to it to the line currently being accumulated. */
    void formatEndnote(String text) {
        // FIXME
    }

    /** Set the current text height (number of lines per page) to VAL, if
     *  it is a valid setting.  Ignored when accumulating an endnote. */
    void setTextHeight(int val) {
        // FIXME
    }

    /** Set the current text width (width of lines including indentation)
     *  to VAL, if it is a valid setting. */
    void setTextWidth(int val) {
        // FIXME
    }

    /** Set the current text indentation (number of spaces inserted before
     *  each line of formatted text) to VAL, if it is a valid setting. */
    void setIndentation(int val) {
        // FIXME
    }

    /** Set the current paragraph indentation (number of spaces inserted before
     *  first line of a paragraph in addition to indentation) to VAL, if it is
     *  a valid setting. */
    void setParIndentation(int val) {
        // FIXME
    }

    /** Set the current paragraph skip (number of blank lines inserted before
     *  a new paragraph, if it is not the first on a page) to VAL, if it is
     *  a valid setting. */
    void setParSkip(int val) {
        // FIXME
    }

    /** Iff ON, begin filling lines of formatted text. */
    void setFill(boolean on) {
        // FIXME
    }

    /** Iff ON, begin justifying lines of formatted text whenever filling is
     *  also on. */
    void setJustify(boolean on) {
        // FIXME
    }

    /** Finish the current formatted document or endnote (depending on mode).
     *  Formats and outputs all pending text. */
    void close() {
        // FIXME
    }

    /** Start directing all formatted text to the endnote assembler. */
    private void setEndnoteMode() {
        // FIXME
    }

    /** Return to directing all formatted text to _mainText. */
    private void setNormalMode() {
        // FIXME
    }

    /** Write all accumulated endnotes to _mainText. */
    private void writeEndnotes() {
        // FIXME
    }

    /** True iff we are currently processing an endnote. */
    private boolean _endnoteMode;
    /** Number of next endnote. */
    private int _refNum;
    // FIXME
}

