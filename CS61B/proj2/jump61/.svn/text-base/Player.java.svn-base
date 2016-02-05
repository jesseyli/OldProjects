package jump61;

import static jump61.Color.*;

/** Represents one player in a game of Jump61.  At any given time, each
 *  Player is attached to a Game and has a Color.  Each call of makeMove()
 *  returns a valid move.
 *
 *  @author
 */
abstract class Player {

    /** A Player in GAME, initially playing COLOR. */
    Player(Game game, Color color) {
        _game = game;
        _color = color;
    }

    /** Return the color I am currently playing. */
    final Color getColor() {
        return _color;
    }

    /** Set getColor() to COLOR, which must be RED or BLUE. */
    void setColor(Color color) {
        assert color == RED || color == BLUE;
        _color = color;
    }

    /** Return the Game I am currently playing in. */
    final Game getGame() {
        return _game;
    }

    /** Return the Board containing the current position
     *  (read-only). */
    final Board getBoard() {
        return _game.getBoard();
    }

    /** Ask my game to make my next move.  Assumes that I am of the
     *  proper color and that the game is not yet won. */
    abstract void makeMove();

    /** My current color. */
    private Color _color;
    /** The game I'm in. */
    private final Game _game;

}
