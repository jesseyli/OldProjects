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
        bestMove[0] = Integer.MIN_VALUE;
        bestMove[1] = 0;
        minmax(getColor(), boardCopy, Defaults.DEPTH,
                Integer.MIN_VALUE, Integer.MAX_VALUE);
        if (!getBoard().isLegal(getColor(), bestMove[1])) {
            bestMove[1] = randomLegal(getBoard(), getColor());
        }
        getGame().makeMove(bestMove[1]);
    }

    /** Returns the biggest possible MAX or smallest possible MIN given a
     *  depth D for player of color C for board B. MAX represents the
     *  heuristic value of the best move for this player and MIN represents
     *  the heuristic value of the worst move for the opponent. */
    private int minmax(Color c, Board b, int d, int max, int min) {
        if (d == 0) {
            if (c.equals(getColor())) {
                return staticEval(getColor(), b);
            } else {
                return staticEval(getColor().opposite(), b);
            }
        }
        if (c.equals(getColor())) {
            for (int i = 0; i < b.size() * b.size(); i += 1) {
                if (b.isLegal(getColor(), i)) {
                    b.addSpot(getColor(), i);
                    max = Math.max(max,
                            minmax(c.opposite(), b, d - 1, max, min));
                    b.undo();
                    if (min <= max) {
                        break;
                    }
                    if (max > bestMove[0]) {
                        bestMove[0] = max;
                        bestMove[1] = i;
                    }
                }
            }
            return max;
        } else {
            for (int j = 0; j < b.size() * b.size(); j += 1) {
                if (b.isLegal(getColor().opposite(), j)) {
                    b.addSpot(getColor().opposite(), j);
                    min = Math.min(min,
                            minmax(c, b, d - 1, max, min));
                    b.undo();
                    if (min <= max) {
                        break;
                    }
                }
            }
            return min;
        }
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


