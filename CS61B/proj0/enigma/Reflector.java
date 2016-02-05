package enigma;

/** Class that represents a reflector in the enigma.
 *  @author Jesse Li
 */
class Reflector extends FixedRotor {

    /** A reflector with label LABEL that belongs to machine MACHINE.
     *  Reflectors cannot have their settings changed and are initialized
     *  to 0. */
    public Reflector(String label, Machine machine) {
        super(label, machine, 0);
    }

    /** Reflectors do not have inverses. Returns false. */
    @Override
    boolean hasInverse() {
        return false;
    }

    /** Returns a useless value; should never be called. */
    @Override
    int convertBackward(int unused) {
        throw new UnsupportedOperationException();
    }

    /** The setting of a reflector can't be changed.
     *  Takes in a position POSN and does nothing. */
    @Override
    void set(int posn) {
    }

}
