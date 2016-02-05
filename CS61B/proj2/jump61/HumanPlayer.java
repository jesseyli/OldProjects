package jump61;

/** A Player that gets its moves from manual input.
 *  @author Jesse Li
 */
class HumanPlayer extends Player {

    /** A new player initially playing COLOR taking manual input of
     *  moves from GAME's input source. */
    HumanPlayer(Game game, Color color) {
        super(game, color);
    }

    @Override
    void makeMove() {
        Game game = getGame();
        Board board = getBoard();
        int[] currMove = {0, 0};
        if (game.getMove(currMove)) {
            if (board.isLegal(this.getColor(), currMove[0], currMove[1])) {
                game.makeMove(currMove[0], currMove[1]);
            } else {
                game.reportError("invalid move: %s %s", currMove[0],
                        currMove[1]);
            }
        }
    }

}
