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
    }

    @Override
    void makeMove() {
        MutableBoard boardCopy = new MutableBoard(getBoard());
        minmax(getColor(), boardCopy, Defaults.DEPTH, Defaults.DEPTH);
        getGame().makeMove(bestMove[1]);
    }

    /** Returns the highest final heuristic value given a
     *  depth D for player of color C for board B. DEPTH stores
     *  the max depth. */
    private int minmax(Color c, Board b, int d, int depth) {
        int currValue = Integer.MIN_VALUE;
        int currMove = 0;
        if (d == 0) {
            return staticEval(c, b);
        } else {
            for (int i = 0; i < b.size() * b.size(); i += 1) {
                if (b.isLegal(c, i)) {
                    b.addSpot(c, i);
                    int val = minmax(c.opposite(), b, d - 1, depth);
                    b.undo();
                    if (c.equals(getColor())) {
                        if (val > currValue) {
                            currValue = val;
                            currMove = i;
                        }
                    }
                    if (c.equals(getColor().opposite())) {
                        if (val < currValue) {
                            currValue = val;
                            currMove = i;
                        }
                    }
                }
            }
            if (depth == d) {
                bestMove[1] = currMove;
            }
        }
        return currValue;
    }

    /** Returns square number of a random legal space in board B for PLAYER. */
    int randomLegal(Board b, Color player) {
        for (int i = getGame().randInt(b.size() * b.size());
                i < b.size() * b.size(); i += 1) {
            if (b.isLegal(player, i)) {
                return i;
            }
            if (i >= b.size() * b.size()) {
                i = -1;
            }
        }
        return 0;
    }

    /** Returns heuristic value of board B for player P.
     *  Higher is better for P. */
    private int staticEval(Color p, Board b) {
        return b.numOfColor(p) - b.numOfColor(p.opposite());
    }

    /** The int at index one holds the move for the highest heuristic value
     *  possible at a certain depth. The int at index 0 is that value. */
    private int[] bestMove = new int[2];
}


