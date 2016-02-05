package tex61;

import java.io.PrintWriter;
import java.util.ArrayList;


/** Receives (partial) words and commands, performs commands, and
 *  accumulates and formats words into lines of text, which are sent to a
 *  designated PageAssembler.  At any given time, a Controller has a
 *  current word, which may be added to by addText, a current list of
 *  words that are being accumulated into a line of text, and a list of
 *  lines of endnotes.
 *  @author Jesse Li
 */
class Controller {

    /** A new Controller that sends formatted output to OUT. */
    Controller(PrintWriter out) {
        _endnoteMode = false;
        _refNum = 1;
        _endnotes = new ArrayList<String>();
        _mainText = new PagePrinter(out);
        _endPages = new PageCollector(_endnotes);
        _mainLines = new LineAssembler(_mainText);
        _endLines = new LineAssembler(_endPages);
    }

    /** Add TEXT to the end of the word of formatted text currently
     *  being accumulated. */
    void addText(String text) {
        if (_endnoteMode) {
            _endLines.addText(text);
        } else {
            _mainLines.addText(text);
        }
    }

    /** Finish any current word of text and, if present, add to the
     *  list of words for the next line.  Has no effect if no unfinished
     *  word is being accumulated. */
    void endWord() {
        if (_endnoteMode) {
            _endLines.finishWord();
        } else {
            _mainLines.finishWord();
        }
    }

    /** Finish any current word of formatted text and process an end-of-line
     *  according to the current formatting parameters. */
    void addNewline() {
        if (_endnoteMode) {
            _endLines.newLine();
        } else {
            _mainLines.newLine();
        }
    }

    /** Finish any current word of formatted text, format and output any
     *  current line of text, and start a new paragraph. */
    void endParagraph() {
        if (_endnoteMode) {
            _endLines.endParagraph();
        } else {
            _mainLines.endParagraph();
        }
    }

    /** If valid, process TEXT into an endnote, first appending a reference
     *  to it to the line currently being accumulated. */
    void formatEndnote(String text) {
        String reference = "[" + _refNum + "]";
        addText(reference);
        setEndnoteMode();
        InputParser endNoteParser = new InputParser(reference + "\\ " + text,
                this);
        endNoteParser.process();
        _refNum += 1;
        setNormalMode();
    }

    /** Set the current text height (number of lines per page) to VAL, if
     *  it is a valid setting.  Ignored when accumulating an endnote. */
    void setTextHeight(int val) {
        if (!_endnoteMode) {
            _mainLines.setTextHeight(val);
        }
    }

    /** Set the current text width (width of lines including indentation)
     *  to VAL, if it is a valid setting. */
    void setTextWidth(int val) {
        if (_endnoteMode) {
            _endLines.setTextWidth(val);
        } else {
            _mainLines.setTextWidth(val);
        }
    }

    /** Set the current text indentation (number of spaces inserted before
     *  each line of formatted text) to VAL, if it is a valid setting. */
    void setIndentation(int val) {
        if (_endnoteMode) {
            _endLines.setIndentation(val);
        } else {
            _mainLines.setIndentation(val);
        }
    }

    /** Set the current paragraph indentation (number of spaces inserted before
     *  first line of a paragraph in addition to indentation) to VAL, if it is
     *  a valid setting. */
    void setParIndentation(int val) {
        if (_endnoteMode) {
            _endLines.setParIndentation(val);
        } else {
            _mainLines.setParIndentation(val);
        }
    }

    /** Set the current paragraph skip (number of blank lines inserted before
     *  a new paragraph, if it is not the first on a page) to VAL, if it is
     *  a valid setting. */
    void setParSkip(int val) {
        if (_endnoteMode) {
            _endLines.setParSkip(val);
        } else {
            _mainLines.setParSkip(val);
        }
    }

    /** Iff ON, begin filling lines of formatted text. */
    void setFill(boolean on) {
        if (_endnoteMode) {
            _endLines.setFill(on);
        } else {
            _mainLines.setFill(on);
        }
    }

    /** Iff ON, begin justifying lines of formatted text whenever filling is
     *  also on. */
    void setJustify(boolean on) {
        if (_endnoteMode) {
            _endLines.setJustify(on);
        } else {
            _mainLines.setJustify(on);
        }
    }

    /** Finish the current formatted document or endnote (depending on mode).
     *  Formats and outputs all pending text. */
    void close() {
        endParagraph();
        if (!_endnoteMode) {
            writeEndnotes();
        }
    }

    /** Start directing all formatted text to the endnote assembler. */
    private void setEndnoteMode() {
        _endnoteMode = true;
    }

    /** Return to directing all formatted text to _mainText. */
    private void setNormalMode() {
        _endnoteMode = false;
    }

    /** Write all accumulated endnotes to _mainText. */
    private void writeEndnotes() {
        for (String line: _endnotes) {
            _mainText.addLine(line);
        }
    }

    /** True iff we are currently processing an endnote. */
    private boolean _endnoteMode;
    /** Number of next endnote. */
    private int _refNum;
    /** A list used for storing endnotes. */
    private ArrayList<String> _endnotes;
    /** A LineAssembler for the _mainText. */
    private LineAssembler _mainLines;
    /** A LineAssembler for the endnotes. */
    private LineAssembler _endLines;
    /** A PageCollector that stores endnotes in the list _endnotes. */
    private PageCollector _endPages;
    /** A PagePrinter that the final document will be printed from. */
    private PagePrinter _mainText;
}

