package jump61;

/** A ConstantBoard is a view of an existing Board that does not allow
 *  modifications. Changes made to the underlying Board are reflected in
 *  ConstantBoards formed from it.
 *  @author P. N. Hilfinger
 */
class ConstantBoard extends Board {

    /** A new ConstantBoard that allows a read-only view of BOARD. */
    ConstantBoard(Board board) {
        _board = board;
    }

    @Override
    int size() {
        return _board.size();
    }

    @Override
    int spots(int r, int c) {
        return _board.spots(r, c);
    }

    @Override
    int spots(int n) {
        return _board.spots(n);
    }

    @Override
    Color color(int r, int c) {
        return _board.color(r, c);
    }

    @Override
    Color color(int n) {
        return _board.color(n);
    }

    @Override
    int numMoves() {
        return _board.numMoves();
    }

    @Override
    Color whoseMove() {
        return _board.whoseMove();
    }

    @Override
    boolean isLegal(Color player, int r, int c) {
        return _board.isLegal(player, r, c);
    }

    @Override
    boolean isLegal(Color player) {
        return _board.isLegal(player);
    }

    @Override
    int numOfColor(Color color) {
        return _board.numOfColor(color);
    }

    @Override
    public boolean equals(Object obj) {
        return _board.equals(obj);
    }

    @Override
    public int hashCode() {
        return _board.hashCode();
    }

    /** Board to which all operations delegated. */
    private Board _board;

}
