package make;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/** Initial class for the 'make' program.
 *  @author Jesse Li
 */
public final class Main {

    /** Entry point for the CS61B make program.  ARGS may contain options
     *  and targets:
     *      [ -f MAKEFILE ] [ -D FILEINFO ] TARGET1 TARGET2 ...
     */
    public static void main(String... args) {
        String makefileName;
        String fileInfoName;

        if (args.length == 0) {
            usage();
        }

        makefileName = "Makefile";
        fileInfoName = "fileinfo";

        int a;
        for (a = 0; a < args.length; a += 1) {
            if (args[a].equals("-f")) {
                a += 1;
                if (a == args.length) {
                    usage();
                } else {
                    makefileName = args[a];
                }
            } else if (args[a].equals("-D")) {
                a += 1;
                if (a == args.length) {
                    usage();
                } else {
                    fileInfoName = args[a];
                }
            } else if (args[a].startsWith("-")) {
                usage();
            } else {
                break;
            }
        }

        ArrayList<String> targets = new ArrayList<String>();

        for (; a < args.length; a += 1) {
            targets.add(args[a]);
        }

        make(makefileName, fileInfoName, targets);
    }

    /** Carry out the make procedure using MAKEFILENAME as the makefile,
     *  taking information on the current file-system state from FILEINFONAME,
     *  and building TARGETS, or the first target in the makefile if TARGETS
     *  is empty.
     */
    private static void make(String makefileName, String fileInfoName,
                             List<String> targets) {
        ArrayList<String> makeFileLines = read(makefileName);
        ArrayList<String> fileInfoLines = read(fileInfoName);
        storeFileInfo(fileInfoLines);
        storeRules(makeFileLines, targets);
        ArrayList<String> visited = new ArrayList<String>();
        for (String target: targets) {
            build(target, visited);
        }
    }

    /** Builds the desired target TARGET following all rules and
     *  dependencies. VISITED is an ArrayList passed to ensure that an infinite
     *  loop does not occur. */
    private static void build(String target, ArrayList<String> visited) {
        if (visited.contains(target) || !_rules.containsKey(target)) {
            return;
        }
        visited.add(target);
        for (String prereq: _rules.get(target).getDependencies()) {
            if (checkToBuild(prereq)) {
                build(prereq, visited);
            }
        }
        if (checkToBuild(target)) {
            _built.put(target, _time);
            _time += 1;
            for (String command: _rules.get(target).getCommands()) {
                System.out.println(command);
            }
        }
        visited.remove(target);
    }

    /** Checks if target TARGET is older than any of its prerequisites. If so,
     *  this returns true. Also returns true if target is not yet built. */
    private static boolean checkToBuild(String target) {
        if (_built.containsKey(target)) {
            if (_rules.containsKey(target)) {
                for (String prereq: _rules.get(target).getDependencies()) {
                    if (_built.get(prereq) > _built.get(target)) {
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
    }

    /** Reads the file named NAME and returns a list of the nonempty lines from
     *  that file. Returns null if the file can't be found. */
    private static ArrayList<String> read(String name) {
        try {
            ArrayList<String> output = new ArrayList<String>();
            InputStream file =
                Main.class.getClassLoader().getResourceAsStream(name);
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line != null && !line.isEmpty()
                        && !line.trim().isEmpty() && !line.startsWith("#")) {
                    output.add(line);
                }
            }
            reader.close();
            return output;
        } catch (IOException excp) {
            System.out.println("File not found");
            System.exit(0);
            return null;
        }
    }

    /** Stores the file info from array INPUT into HashMap _BUILT. Also
     *  sets variable TIME according to the file info. */
    private static void storeFileInfo(ArrayList<String> input) {
        try {
            _time = Integer.parseInt(input.get(0));
            input.remove(0);
        } catch (NumberFormatException e) {
            usage();
        }
        for (String line: input) {
            String[] builtFile = line.split("\\s+");
            if (builtFile.length == 2) {
                try {
                    _built.put(builtFile[0], Integer.parseInt(builtFile[1]));
                } catch (NumberFormatException e) {
                    usage();
                }
            } else {
                usage();
            }
        }
    }

    /** Stores the rules according to the make file info MAKEINFO provided.
     *  Also adds a target to TARGETS if it is empty. */
    private static void storeRules(ArrayList<String> makeInfo,
            List<String> targets) {
        if (Character.isWhitespace(makeInfo.get(0).charAt(0))) {
            usage();
        }
        String[] header = makeInfo.get(0).split("\\s+");
        String currentTarget = header[0];
        if (targets.isEmpty()) {
            targets.add(currentTarget.substring(0,
                    currentTarget.length() - 1));
        }
        for (int i = 0; i < makeInfo.size(); i += 1) {
            if (!Character.isWhitespace(makeInfo.get(i).charAt(0))) {
                header = makeInfo.get(i).split("\\s+");
                if (!header[0].endsWith(":")) {
                    usage();
                }
                currentTarget = header[0].substring(0, header[0].length() - 1);
                addRule(currentTarget);
                for (int j = 1; j < header.length; j += 1) {
                    _rules.get(currentTarget).getDependencies().add(header[j]);
                }
            } else {
                ArrayList<String> commands = new ArrayList<String>();
                while (i < makeInfo.size()
                        && Character.isWhitespace(makeInfo.get(i).charAt(0))) {
                    commands.add(makeInfo.get(i));
                    i += 1;
                }
                i -= 1;
                _rules.get(currentTarget).setCommands(commands);
            }
        }
    }

    /** Print a brief usage message and exit program abnormally. */
    private static void usage() {
        System.out.println("Invalid input.");
        System.exit(1);
    }

    /** Adds a new rule to the _RULES HashMap with target TARGET. If a rule
     *  the specified target already exists, this does nothing. */
    private static void addRule(String target) {
        if (!_rules.containsKey(target)) {
            Rule newRule = new Rule(target);
            _rules.put(target, newRule);
        }
    }

    /** A rule corresponding to a target. */
    public static class Rule {

        /** A new Rule with target TARGET. */
        Rule(String target) {
            _target = target;
            _dependencies = new ArrayList<String>();
            _commands = new ArrayList<String>();
        }

        /** Return this Rule's target. */
        public String getTarget() {
            return _target;
        }

        /** Return this Rule's dependencies. */
        public ArrayList<String> getDependencies() {
            return _dependencies;
        }

        /** Sets this Rule's commands to the commands in ArrayList INPUT.
         *  Should only be called once and reports an error by calling
         *  usage() otherwise. */
        public void setCommands(ArrayList<String> input) {
            if (_commands.isEmpty()) {
                for (String s: input) {
                    _commands.add(s);
                }
            } else {
                usage();
            }
        }

        /** Return this Rule's commands. */
        public ArrayList<String> getCommands() {
            return _commands;
        }

        /** The target this Rule is associated with. */
        private final String _target;
        /** A list of the dependencies of this Rule. */
        private ArrayList<String> _dependencies;
        /** A list of the command lines associated with this rule. */
        private ArrayList<String> _commands;
    }

    /** The current time. */
    private static int _time;
    /** A HashMap of names of built objects as keys and the times the objects
     *  were built as values.
     */
    private static HashMap<String, Integer> _built = new HashMap<String,
            Integer>();
    /** A HashMap of objects and the rules that correspond to them. */
    private static HashMap<String, Rule> _rules = new HashMap<String, Rule>();
}
