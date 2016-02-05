package enigma;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/** Enigma simulator.
 *  @author Jesse Li
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified in the input from the standard input.  Print the
     *  results on the standard output. Exits normally if there are
     *  no errors in the input; otherwise with code 1. */
    public static void main(String[] unused) {
        Machine M = new Machine();
        BufferedReader input =
            new BufferedReader(new InputStreamReader(System.in));

        try {
            while (true) {
                String line = input.readLine();
                if (line == null) {
                    break;
                }
                if (isConfigurationLine(line)) {
                    configure(M, line);
                } else {
                    printMessageLine(M.convert(standardize(line)));
                }
            }
        } catch (IOException excp) {
            System.err.printf("Input error: %s%n", excp.getMessage());
            System.exit(1);
        }
    }

    /** Possible labels for rotors 3-5. */
    static final String[] ROTOR_LABELS = {"I", "II", "III", "IV", "V",
                                          "VI", "VII", "VIII"};

    /** Return true iff LINE is an Enigma configuration line. */
    public static boolean isConfigurationLine(String line) {
        line = line.replaceAll("\\s+", " ");
        String[] config = line.split(" ");
        if (config[0].equals("*") && config.length == 7) {
            if ((config[1].equals("B") || config[1].equals("C"))
                && (config[2].equals("BETA")
                    || config[2].equals("GAMMA"))) {
                String[] rotor345 = {config[3], config[4], config[5]};
                for (String item: rotor345) {
                    if (!Arrays.asList(ROTOR_LABELS).contains(item)) {
                        return false;
                    }
                }
                if (checkDuplicates(rotor345)
                    || config[6].length() != 4) {
                    return false;
                }
                for (int i = 0; i < config[6].length(); i += 1) {
                    char c = config[6].charAt(i);
                    if (!Rotor.ALPHABET.contains(Character.toString(c))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    /** Checks a string array STR for duplicates and returns true if there
     * are duplicates. */
    static boolean checkDuplicates(String[] str) {
        ArrayList<String> checked = new ArrayList<String>();
        for (String item: str) {
            if (checked.contains(item)) {
                return true;
            }
            checked.add(item);
        }
        return false;
    }

    /** Configure M according to the specification given on CONFIG,
     *  which must have the format specified in the assignment. */
    private static void configure(Machine M, String config) {
        config = config.replaceAll("\\s+", " ");
        String[] line = config.split(" ");
        Reflector reflector = new Reflector(line[1], M);
        FixedRotor fixedRotor = new FixedRotor(line[2], M, 0);
        Rotor rotor3 = new Rotor(line[3], M, 0);
        Rotor rotor4 = new Rotor(line[4], M, 0);
        Rotor rotor5 = new Rotor(line[5], M, 0);
        Rotor[] rotors = {reflector, fixedRotor, rotor3, rotor4, rotor5};
        M.replaceRotors(rotors);
        M.setRotors(line[6]);
    }

    /** Return the result of converting LINE to all upper case,
     *  removing all blanks and tabs.  It is an error if LINE contains
     *  characters other than letters and blanks. */
    public static String standardize(String line) {
        line = line.replaceAll("\\s", "");
        if (line.matches("(.*)\\W(.*)") || line.matches("(.*)\\d(.*)")) {
            System.exit(1);
        }
        return line.toUpperCase();
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    public static void printMessageLine(String msg) {
        while (msg.length() > 4) {
            System.out.print(msg.substring(0, 5) + " ");
            msg = msg.substring(5);
        }
        System.out.println(msg);
    }

}

