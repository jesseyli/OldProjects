package tex61;

import java.util.ArrayList;
import static tex61.FormatException.error;

/** An object that receives a sequence of words of text and formats
 *  the words into filled and justified text lines that are sent to a receiver.
 *  @author Jesse Li
 */
class LineAssembler {

    /** A new, empty line assembler with default settings of all
     *  parameters, sending finished lines to PAGES. */
    LineAssembler(PageAssembler pages) {
        _pages = pages;
        if (pages instanceof PagePrinter) {
            _indent = Defaults.INDENTATION;
            _parIndent = Defaults.PARAGRAPH_INDENTATION;
            _parSkip = Defaults.PARAGRAPH_SKIP;
            _pages.setTextHeight(Defaults.TEXT_HEIGHT);
        } else if (pages instanceof PageCollector) {
            _indent = Defaults.ENDNOTE_INDENTATION;
            _parIndent = Defaults.ENDNOTE_PARAGRAPH_INDENTATION;
            _parSkip = Defaults.ENDNOTE_PARAGRAPH_SKIP;
        }
        _textWidth = Defaults.TEXT_WIDTH;
        _chars = "";
        _firstLine = true;
        _lastLine = false;
        _fill = true;
        _justify = true;
        _words = new ArrayList<String>();
    }

    /** Add TEXT to the word currently being built. */
    void addText(String text) {
        _chars += text;
    }

    /** Finish the current word, if any, and add to words being accumulated. */
    void finishWord() {
        if (!_chars.equals("")) {
            addWord(_chars);
            _chars = "";
        }
    }

    /** Add WORD to the formatted text. */
    void addWord(String word) {
        if (_fill) {
            if ((totalChars(_words) + _words.size() + word.length()
                    + currIndent()) > _textWidth) {
                outputLine(_lastLine);
            }
        }
        _words.add(word);
    }

    /** Add LINE to our output, with no preceding paragraph skip.  There must
     *  not be an unfinished line pending. */
    void addLine(String line) {
        _pages.addLine(line);
    }

    /** Set the current indentation to VAL. VAL >= 0. */
    void setIndentation(int val) {
        if (val >= 0) {
            _indent = val;
        } else {
            throw error("Indentation must be greater than or "
                    + "equal to 0.", val);
        }
    }

    /** Set the current paragraph indentation to VAL. Sum of this and
     *  indentation must be greater than or equal to 0. */
    void setParIndentation(int val) {
        if (val + _indent >= 0) {
            _parIndent = val;
        } else {
            throw error("Paragraph Indentation must be greater "
                    + "than or equal to 0.", val);
        }
    }

    /** Set the text width to VAL, where VAL >= 0. */
    void setTextWidth(int val) {
        if (val >= 0) {
            _textWidth = val;
        } else {
            throw error("Text width must be greater than or "
                    + "equal to 0.", val);
        }
    }

    /** Iff ON, set fill mode. */
    void setFill(boolean on) {
        _fill = on;
    }

    /** Iff ON, set justify mode (which is active only when filling is
     *  also on). */
    void setJustify(boolean on) {
        _justify = on;
    }

    /** Set paragraph skip to VAL.  VAL >= 0. */
    void setParSkip(int val) {
        if (val >= 0) {
            _parSkip = val;
        } else {
            throw error("Parskip must be greater than or equal "
                    + "to 0.", val);
        }
    }

    /** Set page height to VAL > 0. */
    void setTextHeight(int val) {
        if (val >= 0) {
            _pages.setTextHeight(val);
        } else {
            throw error("Text height must be greater than or "
                    + "equal to 0.", val);
        }
    }

    /** Process the end of the current input line.  No effect if
     *  current line accumulator is empty or in fill mode.  Otherwise,
     *  adds a new complete line to the finished line queue and clears
     *  the line accumulator. */
    void newLine() {
        finishWord();
        if (!_fill) {
            outputLine(_lastLine);
        }
    }

    /** If there is a current unfinished paragraph pending, close it
     *  out and start a new one. */
    void endParagraph() {
        finishWord();
        if (!_words.isEmpty()) {
            _lastLine = true;
            outputLine(_lastLine);
            _lastLine = false;
            _firstLine = true;
        }
    }

    /** Transfer contents of _words to _pages, adding INDENT characters of
     *  indentation, and a total of SPACES spaces between words, evenly
     *  distributed.  Assumes _words is not empty.  Clears _words and _chars. */
    private void emitLine(int indent, int spaces) {
        String output = "";
        while (indent > 0) {
            output += " ";
            indent -= 1;
        }
        if (_words.size() > 1) {
            output += _words.get(0);
            if (3 * (_words.size() - 1) < spaces) {
                for (int i = 1; i < _words.size(); i += 1) {
                    output = addSpaces(output, 3);
                    output += _words.get(i);
                }
            } else {
                int counter = 0;
                for (double i = 1; i < _words.size(); i += 1) {
                    output = addSpaces(output, (int) (0.5 + (spaces * i)
                            / (_words.size() - 1)) - counter);
                    counter = (int) (0.5 + (spaces * i)
                                     / (_words.size() - 1));
                    output += _words.get((int) i);
                }
            }
        } else {
            output += _words.get(0);
        }
        addLine(output);
        _words.clear();
    }

    /** Returns the string that results from adding NUMBER of spaces to the
     *  end of string STR. */
    private String addSpaces(String str, int number) {
        while (number > 0) {
            str += " ";
            number -= 1;
        }
        return str;
    }

    /** If the line accumulator is non-empty, justify its current
     *  contents, if needed, add a new complete line to _pages,
     *  and clear the line accumulator. LASTLINE indicates the last line
     *  of a paragraph. */
    public void outputLine(boolean lastLine) {
        if (!_words.isEmpty()) {
            if (_firstLine) {
                for (int i = 0; i < _parSkip; i += 1) {
                    addLine(null);
                }
            }
            if (lastLine || !_justify || !_fill) {
                emitLine(currIndent(), _words.size() - 1);
            } else {
                emitLine(currIndent(), _textWidth - totalChars(_words)
                        - currIndent());
            }
            _firstLine = false;
        }
    }

    /** Returns the total number of characters from all of the items of
     *  LIST combined. */
    private int totalChars(ArrayList<String> list) {
        int total = 0;
        for (String word: list) {
            total += word.length();
        }
        return total;
    }

    /** Returns the current total line indentation. */
    private int currIndent() {
        int indentation = _indent;
        if (_firstLine) {
            indentation += _parIndent;
        }
        return indentation;
    }

    /** Destination given in constructor for formatted lines. */
    private final PageAssembler _pages;
    /** List of words to be output on current line. */
    private ArrayList<String> _words;
    /** Characters of the word currently being constructed. */
    private String _chars;
    /** The current amount of indentation per line. */
    private int _indent;
    /** The amount of additional indentation at the start of each
     *  paragraph. */
    private int _parIndent;
    /** The current text width. */
    private int _textWidth;
    /** The current number of blank lines before each subsequent
     *  paragraph. */
    private int _parSkip;
    /** If and only if this boolean is true, fill mode is on.  */
    private boolean _fill;
    /** If and only if this boolean is true, justify mode is on. */
    private boolean _justify;
    /** Tells whether or not the current line is the first line of a paragraph
     *  and needs additional indentation.  True if the current line is the
     *  first line of a paragraph. */
    private boolean _firstLine;
    /** Tells whether or not the current line being accumulated is the last
     *  line of a paragraph. */
    private boolean _lastLine;
}
