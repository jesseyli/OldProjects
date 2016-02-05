package jump61;


/** An automated Player.
 *  @author Jesse Li
 */
class AI extends Player {

    /** A new player of GAME initially playing COLOR that chooses
     *  moves automatically.
     */
    AI(Game game, Color color) {
        super(game, color);
        _move = 0;
    }

    @Override
    void makeMove() {
        _move = 0;
        Board boardCopy = new MutableBoard(getBoard());
        minmax(getColor(), boardCopy, Defaults.DEPTH, Defaults.DEPTH);
        getGame().makeMove(_move);
    }

    /** Return the minmax value of board B for player P to a search depth of D
     *  (where D == 0 denotes evaluating just the next move) and store the
     *  move in the variable MOVE. DEPTH is the original depth. */
    protected int minmax(Color p, Board b, int d, int depth) {
        int currMove = 0;
        int currVal;
        if (d == 0) {
            return staticEval(p, b);
        }
        if (p.equals(getColor())) {
            currVal = Integer.MIN_VALUE;
            for (int i = 0; i < b.size() * b.size(); i += 1) {
                if (b.isLegal(p, i)) {
                    b.addSpot(p, i);
                    int move = i;
                    int val = minmax(p.opposite(), b, d - 1, depth);
                    b.undo();
                    if (val > currVal) {
                        currVal = val;
                        currMove = move;
                    }
                }
                if (currVal == Integer.MIN_VALUE) {
                    for (int j = 0; j < b.size() * b.size(); j += 1) {
                        if (b.isLegal(p, j)) {
                            currMove = j;
                        }
                    }
                }
            }
        } else {
            currVal = Integer.MAX_VALUE;
            for (int i = 0; i < b.size() * b.size(); i += 1) {
                if (b.isLegal(p, i)) {
                    b.addSpot(p, i);
                    int move = i;
                    int val = minmax(p.opposite(), b, d - 1, depth);
                    b.undo();
                    if (val < currVal) {
                        currVal = val;
                        currMove = move;
                    }
                }
                if (currVal == Integer.MAX_VALUE) {
                    for (int j = 0; j < b.size() * b.size(); j += 1) {
                        if (b.isLegal(p, j)) {
                            currMove = j;
                        }
                    }
                }
            }
        }
        if (d == depth) {
            _move = currMove;
        }
        return currVal;
    }

    /** Returns heuristic value of board B for player P.
     *  Higher is better for P. */
    protected int staticEval(Color p, Board b) {
        return b.numOfColor(p) - b.numOfColor(p.opposite());
    }

    /** The next move that the AI will make. */
    private int _move;

}
