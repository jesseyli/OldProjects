package jump61;

import java.io.Reader;
import java.io.Writer;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Random;

import static jump61.Color.*;
import static jump61.GameException.error;

/** Main logic for playing (a) game(s) of Jump61.
 *  @author Jesse Li
 */
class Game {

    /** Name of resource containing help message. */
    private static final String HELP = "jump61/Help.txt";

    /** A new Game that takes command/move input from INPUT, prints
     *  normal output on OUTPUT, prints prompts for input on PROMPTS,
     *  and prints error messages on ERROROUTPUT. The Game now "owns"
     *  INPUT, PROMPTS, OUTPUT, and ERROROUTPUT, and is responsible for
     *  closing them when its play method returns. */
    Game(Reader input, Writer prompts, Writer output, Writer errorOutput) {
        _board = new MutableBoard(Defaults.BOARD_SIZE);
        _readonlyBoard = new ConstantBoard(_board);
        _prompter = new PrintWriter(prompts, true);
        _inp = new Scanner(input);
        _inp.useDelimiter("(?m)$|^|\\p{Blank}");
        _out = new PrintWriter(output, true);
        _err = new PrintWriter(errorOutput, true);
        _redPlayer = new HumanPlayer(this, RED);
        _bluePlayer = new AI(this, BLUE);
        _playing = false;
        _running = true;
        _move[0] = 0;
    }

    /** Returns a readonly view of the game board.  This board remains valid
     *  throughout the session. */
    Board getBoard() {
        return _readonlyBoard;
    }

    /** Play a session of Jump61.  This may include multiple games,
     *  and proceeds until the user exits.  Returns an exit code: 0 is
     *  normal; any positive quantity indicates an error.  */
    int play() {
        _out.println("Welcome to " + Defaults.VERSION);
        _out.flush();
        while (_running) {
            if (_playing) {
                nextPlayer().makeMove();
            } else if (promptForNext()) {
                readExecuteCommand();
            } else {
                _running = false;
            }
        }
        _out.close();
        _err.close();
        _inp.close();
        _prompter.close();
        return 0;
    }

    /** Returns the player who is next to move. */
    Player nextPlayer() {
        if (_board.whoseMove().equals(RED)) {
            return _redPlayer;
        } else {
            return _bluePlayer;
        }
    }

    /** Get a move from my input and place its row and column in
     *  MOVE.  Returns true if this is successful, false if game stops
     *  or ends first. */
    boolean getMove(int[] move) {
        while (_playing && _move[0] == 0 && promptForNext()) {
            readExecuteCommand();
        }
        if (_move[0] > 0) {
            move[0] = _move[0];
            move[1] = _move[1];
            _move[0] = 0;
            return true;
        } else {
            return false;
        }
    }

    /** Add a spot to R C, if legal to do so. */
    void makeMove(int r, int c) {
        if (nextPlayer() instanceof AI) {
            _out.printf("%s moves %d %d. \n",
                    nextPlayer().getColor().toCapitalizedString(), r, c);
            _out.flush();
        }
        _board.addSpot(_board.whoseMove(), r, c);
        checkForWin();
    }

    /** Add a spot to square #N, if legal to do so. */
    void makeMove(int n) {
        makeMove(_board.row(n), _board.col(n));
    }

    /** Return a random integer in the range [0 .. N), uniformly
     *  distributed.  Requires N > 0. */
    int randInt(int n) {
        return _random.nextInt(n);
    }

    /** Send a message to the user as determined by FORMAT and ARGS, which
     *  are interpreted as for String.format or PrintWriter.printf. */
    void message(String format, Object... args) {
        _out.printf(format, args);
    }

    /** Check whether we are playing and there is an unannounced winner.
     *  If so, announce and stop play. */
    private void checkForWin() {
        if (_playing && _board.getWinner() != null) {
            _playing = false;
            announceWinner();
        }
    }

    /** Send announcement of winner to my user output. */
    private void announceWinner() {
        _out.printf("%s wins." + System.getProperty("line.separator"),
                _board.getWinner().toCapitalizedString());
        _out.flush();
    }

    /** Make PLAYER an AI for subsequent moves. */
    private void setAuto(Color player) {
        _playing = false;
        if (player.equals(RED)) {
            _redPlayer = new AI(this, player);
        } else {
            _bluePlayer = new AI(this, player);
        }
    }

    /** Make PLAYER take manual input from the user for subsequent moves. */
    private void setManual(Color player) {
        _playing = false;
        if (player.equals(RED)) {
            _redPlayer = new HumanPlayer(this, player);
        } else {
            _bluePlayer = new HumanPlayer(this, player);
        }
    }

    /** Stop any current game and clear the board to its initial
     *  state. */
    private void clear() {
        _playing = false;
        _board.clear(_board.size());
    }

    /** Print the current board using standard board-dump format. */
    private void dump() {
        _out.println(_board);
    }

    /** Print a help message. */
    private void help() {
        Main.printHelpResource(HELP, _out);
    }

    /** Stop any current game and set the move number to N. */
    private void setMoveNumber(int n) {
        if (n > 0) {
            _playing = false;
            _board.setMoves(n);
        } else {
            throw error("Invalid move number: '%s'", n);
        }
    }

    /** Seed the random-number generator with SEED. */
    private void setSeed(long seed) {
        _random.setSeed(seed);
    }

    /** Place SPOTS spots on square R:C and color the square red or
     *  blue depending on whether COLOR is "r" or "b".  If SPOTS is
     *  0, clears the square, ignoring COLOR.  SPOTS must be less than
     *  or equal to the number of neighbors of square R, C. */
    private void setSpots(int r, int c, int spots, String color) {
        if (_board.exists(r, c)) {
            if (spots >= 0 && spots <= _board.neighbors(r, c)) {
                if (color.equals("r")) {
                    _board.set(r, c, spots, RED);
                } else if (color.equals("b")) {
                    _board.set(r, c, spots, BLUE);
                } else {
                    throw error("invalid input: '%s'", color);
                }
            } else {
                throw error("Number of spots is invalid: '%s'", spots);
            }
        } else {
            throw error("Invalid square: %s %s", r, c);
        }
    }

    /** Stop any current game and set the board to an empty N x N board
     *  with numMoves() == 1.  */
    private void setSize(int n) {
        if (n > 1) {
            _playing = false;
            _board.clear(n);
        } else {
            throw error("invalid size: '%s'", n);
        }
    }

    /** Begin accepting moves for game.  If the game is won,
     *  immediately print a win message and end the game. */
    private void restartGame() {
        _playing = true;
        checkForWin();
    }

    /** Save move R C in _move.  Error if R and C do not indicate an
     *  existing square on the current board. */
    private void saveMove(int r, int c) {
        if (!_board.exists(r, c)) {
            throw error("move %d %d out of bounds", r, c);
        }
        _move[0] = r;
        _move[1] = c;
    }

    /** Returns a color (player) name from _inp: either RED or BLUE.
     *  Throws an exception if not present. */
    private Color readColor() {
        try {
            return Color.parseColor(
                    _inp.next("[rR][eE][dD]|[Bb][Ll][Uu][Ee]"));
        } catch (InputMismatchException e) {
            throw error("invalid color input");
        }
    }

    /** Read and execute one command.  Leave the input at the start of
     *  a line, if there is more input. */
    private void readExecuteCommand() {
        if (_inp.hasNextInt()) {
            if (_playing) {
                try {
                    saveMove(Integer.parseInt(_inp.next()),
                        Integer.parseInt(_inp.next()));
                } catch (GameException e) {
                    reportError(e.getMessage());
                }
            } else {
                reportError("no game in progress.");
            }
            _inp.nextLine();
        } else {
            executeCommand(_inp.next());
        }
    }

    /** Gather arguments and execute command CMND.  Throws GameException
     *  on errors. */
    private void executeCommand(String cmnd) {
        try {
            switch (cmnd) {
            case "\n": case "\r\n":
                return;
            case "#":
                break;
            case "start":
                restartGame();
                break;
            case "quit":
                _running = false;
                break;
            case "clear":
                clear();
                break;
            case "auto":
                setAuto(readColor());
                break;
            case "manual":
                setManual(readColor());
                break;
            case "size":
                setSize(parseInt());
                break;
            case "move":
                setMoveNumber(parseInt());
                break;
            case "set":
                setSpots(parseInt(), parseInt(), parseInt(), _inp.next());
                break;
            case "dump":
                dump();
                break;
            case "seed":
                setSeed(parseInt());
                break;
            case "help":
                help();
                break;
            default:
                throw error("bad command: '%s'", cmnd);
            }
        } catch (GameException e) {
            reportError(e.getMessage());
        }
        _inp.nextLine();
    }

    /** Print a prompt and wait for input. Returns true iff there is another
     *  token. */
    private boolean promptForNext() {
        if (_playing) {
            _prompter.print(_board.whoseMove().toString());
        }
        _prompter.print("> ");
        _prompter.flush();
        return _inp.hasNextLine();
    }

    /** Send an error message to the user formed from arguments FORMAT
     *  and ARGS, whose meanings are as for printf. */
    void reportError(String format, Object... args) {
        _err.print("Error: ");
        _err.printf(format, args);
        _err.println();
    }

    /** Checks if the next argument is an integer and returns that integer.
     *  Otherwise throws an error. */
    int parseInt() {
        if (_inp.hasNextInt()) {
            return Integer.parseInt(_inp.next());
        } else {
            throw error("invalid argument, not an integer: %s", _inp.next());
        }
    }

    /** Returns the blue player. Used for Unit testing. */
    Player getPlayer2() {
        return _bluePlayer;
    }

    /** Returns the red player. Used for Unit testing. */
    Player getPlayer1() {
        return _redPlayer;
    }

    /** Writer on which to print prompts for input. */
    private final PrintWriter _prompter;
    /** Scanner from current game input.  Initialized to return
     *  newlines as tokens. */
    private final Scanner _inp;
    /** Outlet for responses to the user. */
    private final PrintWriter _out;
    /** Outlet for error responses to the user. */
    private final PrintWriter _err;

    /** The board on which I record all moves. */
    private final Board _board;
    /** A readonly view of _board. */
    private final Board _readonlyBoard;

    /** A pseudo-random number generator used by players as needed. */
    private final Random _random = new Random();

    /** True iff a game is currently in progress. */
    private boolean _playing;

    /** Tells whether or not the program should continue running.
     *  Becomes false when the game should be ended. */
    private boolean _running;

    /** The red player that goes first. */
    private Player _redPlayer;

    /** The blue player that goes second. */
    private Player _bluePlayer;

   /** Used to return a move entered from the console.  Allocated
     *  here to avoid allocations. */
    private final int[] _move = new int[2];
}
