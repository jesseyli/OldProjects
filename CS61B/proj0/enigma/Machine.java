package enigma;

/** Class that represents a complete enigma machine.
 *  @author Jesse Li
 */
class Machine {

    /** Construct a new machine with no rotors and no configuration. */
    public Machine() {
        _configured = false;
    }

    /** Set my rotors to (from left to right) ROTORS.  Initially, the rotor
     *  settings are all 'A'. */
    void replaceRotors(Rotor[] rotors) {
        _rotors = rotors;
    }

    /** Set my rotors according to SETTING, which must be a string of four
     *  upper-case letters. The first letter refers to the leftmost
     *  rotor setting.  */
    void setRotors(String setting) {
        _configured = true;
        for (int i = 0; i < 4; i += 1) {
            _rotors[i + 1].set(Rotor.toIndex(setting.charAt(i)));
        }
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        if (_configured) {
            if (msg.equals("")) {
                return "";
            } else {
                if (_rotors[3].atNotch()) {
                    _rotors[2].advance();
                    _rotors[3].advance();
                } else {
                    if (_rotors[4].atNotch()) {
                        _rotors[3].advance();
                    }
                }
                _rotors[4].advance();
                return convertChar(msg.charAt(0)) + convert(msg.substring(1));
            }
        } else {
            System.exit(1);
            return null;
        }
    }

    /** Returns the encoding/decoding of a single character C with the
     *  current rotor settings. */
    String convertChar(char c) {
        int signal = Rotor.toIndex(c);
        for (int i = 4; i > -1; i -= 1) {
            signal = _rotors[i].convertForward(signal);
        }
        for (int i = 1; i < 5; i += 1) {
            signal = _rotors[i].convertBackward(signal);
        }
        return Character.toString(Rotor.toLetter(signal));
    }

    /** Return the rotors I am currently containing. */
    Rotor[] getRotors() {
        return _rotors;
    }

    /** The rotors I am containing. */
    private Rotor[] _rotors;

    /** Whether or not I have been configured. */
    private boolean _configured;
}
