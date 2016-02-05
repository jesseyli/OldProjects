package enigma;

public class newclass {
    public newclass(max) {
    _max = max;}
 

    private int _total = 0;
    private int _max;

    @Override
    void accum(int x) {
        _total += Math.min(x, _max);
        }

    @Override
    int result() {
        return _total;
    }
} 

