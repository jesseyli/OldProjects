package jump61;


import static jump61.Color.*;
import java.util.ArrayList;

/** A Jump61 board state.
 *  @author Jesse Li
 */
class MutableBoard extends Board {

    /** An N x N board in initial configuration. */
    MutableBoard(int N) {
        _spots = new ArrayList<Integer>();
        _color = new ArrayList<Color>();
        _undoHistory = new ArrayList<Board>();
        _N = N;
        _moves = 1;
        for (int i = 0; i < size() * size(); i += 1) {
            _spots.add(Defaults.SPOT_NUM);
            _color.add(Defaults.COLOR);
        }
    }

    /** A board whose initial contents are copied from BOARD0. Clears the
     *  undo history. */
    MutableBoard(Board board0) {
        _color = new ArrayList<Color>();
        _spots = new ArrayList<Integer>();
        _undoHistory = new ArrayList<Board>();
        _N = board0.size();
        _moves = 1;
        for (int i = 0; i < size() * size(); i += 1) {
            _color.add(board0.color(i));
            _spots.add(board0.spots(i));
        }
    }

    @Override
    void clear(int N) {
        _undoHistory.clear();
        _color.clear();
        _spots.clear();
        _N = N;
        _moves = 1;
        for (int i = 0; i < size() * size(); i += 1) {
            _color.add(Defaults.COLOR);
            _spots.add(Defaults.SPOT_NUM);
        }
    }

    @Override
    void copy(Board board) {
        for (int i = 0; i < size() * size(); i += 1) {
            _color.set(i, board.color(i));
            _spots.set(i, board.spots(i));
        }
    }

    @Override
    int size() {
        return _N;
    }

    @Override
    int spots(int r, int c) {
        return _spots.get(sqNum(r, c));
    }

    @Override
    int spots(int n) {
        return _spots.get(n);
    }

    @Override
    Color color(int r, int c) {
        return _color.get(sqNum(r, c));
    }

    @Override
    Color color(int n) {
        return _color.get(n);
    }

    @Override
    int numMoves() {
        return _moves;
    }

    @Override
    int numOfColor(Color color) {
        int counter = 0;
        for (Color c: _color) {
            if (c.equals(color)) {
                counter += 1;
            }
        }
        return counter;
    }

    @Override
    void addSpot(Color player, int r, int c) {
        addSpot(player, sqNum(r, c));
    }

    @Override
    void addSpot(Color player, int n) {
        _undoHistory.add(new MutableBoard(this));
        _color.set(n, player);
        _spots.set(n, _spots.get(n) + 1);
        jump(n);
        _moves += 1;
    }

    @Override
    void set(int r, int c, int num, Color player) {
        set(sqNum(r, c), num, player);
    }

    @Override
    void set(int n, int num, Color player) {
        if (num == 0) {
            _color.set(n, WHITE);
        } else {
            _color.set(n, player);
        }
        _spots.set(n, num);
        _undoHistory.clear();
    }

    @Override
    void setMoves(int num) {
        assert num > 0;
        _moves = num;
        _undoHistory.clear();
    }

    @Override
    void undo() {
        if (!_undoHistory.isEmpty()) {
            copy(_undoHistory.get(_undoHistory.size() - 1));
            _undoHistory.remove(_undoHistory.size() - 1);
            _moves -= 1;
        }
    }

    /** Do all jumping on this board, assuming that initially, S is the only
     *  square that might be over-full. */
    private void jump(int S) {
        if (_spots.get(S) > neighbors(S) && getWinner() == null) {
            _spots.set(S, _spots.get(S) - neighbors(S));
            if (exists(row(S), col(S) - 1)) {
                _spots.set(S - 1, _spots.get(S - 1) + 1);
                _color.set(S - 1, _color.get(S));
                jump(S - 1);
            }
            if (exists(row(S), col(S) + 1)) {
                _spots.set(S + 1, _spots.get(S + 1) + 1);
                _color.set(S + 1, _color.get(S));
                jump(S + 1);
            }
            if (exists(S - size())) {
                _spots.set(S - size(), _spots.get(S - size()) + 1);
                _color.set(S - size(), _color.get(S));
                jump(S - size());
            }
            if (exists(S + size())) {
                _spots.set(S + size(), _spots.get(S + size()) + 1);
                _color.set(S + size(), _color.get(S));
                jump(S + size());
            }
        }
    }

    /** Total combined number of moves by both sides. */
    protected int _moves;
    /** Convenience variable: size of board (squares along one edge). */
    private int _N;
    /** ArrayList that stores the undo history of the game. */
    private ArrayList<Board> _undoHistory;
    /** ArrayList that stores the colors of the spaces on the board. */
    private ArrayList<Color> _color;
    /** ArrayList that stores the number of spots in each space on the
     *  board. */
    private ArrayList<Integer> _spots;

}
