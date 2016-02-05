package enigma;

/** Class that represents a rotor in the enigma machine.
 *  @author Jesse Li
 */
class Rotor {

    /** A rotor with label, machine, and setting initialized with the
     *  variables LABEL, MACHINE, and SETTING. Assigns permutation data
     *  corresponding to the label provided. */
    public Rotor(String label, Machine machine, int setting) {
        _label = label;
        _machine = machine;
        _setting = setting;
        for (String[] dataSet: PermutationData.ROTOR_SPECS) {
            if (dataSet[0].equals(getLabel())) {
                _dataSet = dataSet;
                break;
            }
        }
    }

    /** Size of alphabet used for plaintext and ciphertext. */
    static final int ALPHABET_SIZE = 26;

    /** Capitals letters from A to Z in alphabetical order. */
    static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /** Assuming that P is an integer in the range 0..25, returns the
     *  corresponding upper-case letter in the range A..Z. */
    static char toLetter(int p) {
        return ALPHABET.charAt(p);
    }

    /** Assuming that C is an upper-case letter in the range A-Z, return the
     *  corresponding index in the range 0..25. Inverse of toLetter. */
    static int toIndex(char c) {
        return ALPHABET.indexOf(c);
    }

    /** Returns true iff this rotor has a ratchet and can advance. */
    boolean advances() {
        return true;
    }

    /** Returns true iff this rotor has a left-to-right inverse. */
    boolean hasInverse() {
        return true;
    }

    /** Return my current rotational setting as an integer between 0
     *  and 25 (corresponding to letters 'A' to 'Z').  */
    int getSetting() {
        return _setting;
    }

    /** Set getSetting() to POSN.  */
    void set(int posn) {
        assert 0 <= posn && posn < ALPHABET_SIZE;
        _setting = posn;
    }

    /** Return the conversion of P (an integer in the range 0..25)
     *  according to my permutation. */
    int convertForward(int p) {
        int result = toIndex(getData()[1].charAt(
                (p + getSetting()) % ALPHABET_SIZE));
        return (result - getSetting() + ALPHABET_SIZE)
                % ALPHABET_SIZE;
    }

    /** Return the conversion of E (an integer in the range 0..25)
     *  according to the inverse of my permutation. Exit program if
     *  I am incorrectly labeled. */
    int convertBackward(int e) {
        int result = toIndex(getData()[2].charAt((
                e + getSetting()) % ALPHABET_SIZE));
        return (result - getSetting() + ALPHABET_SIZE)
                % ALPHABET_SIZE;
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        String currposn = Character.toString(toLetter(getSetting()));
        return getData()[3].contains(currposn);
    }

    /** Advance me one position. */
    void advance() {
        _setting = (getSetting() + 1) % ALPHABET_SIZE;
    }

    /** Set getLabel() to LABEL. */
    void setLabel(String label) {
        _label = label;
    }

    /** Return my label (I-VIII, BETA, GAMMA, B, or C). */
    String getLabel() {
        return _label;
    }

    /** Set getMachine() to M. */
    void setMachine(Machine m) {
        _machine = m;
    }

    /** Return what machine I belong to. */
    Machine getMachine() {
        return _machine;
    }

    /** Return my permutation data set. */
    String[] getData() {
        return _dataSet;
    }

    /** My current setting (index 0..25, with 0 indicating that 'A'
     *  is showing). */
    private int _setting;

    /** What rotor label I have (I-VIII, BETA, GAMMA, B, or C). */
    private String _label;

    /** What machine I belong to. */
    private Machine _machine;

    /** My permutation data. One of the entries from the ROTOR_SPECS
     *  variable in the PermutationData file. See comments in
     *  PermutationData file for explanation of data format. */
    private String[] _dataSet;
}
