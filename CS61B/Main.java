package make;
import graph.Graph;
import graph.DirectedGraph;
import graph.Traversal;
import graph.RejectException;
import graph.StopException;
import java.util.HashSet;
import java.util.ArrayList;
import java.io.IOException;
import java.util.List;
import java.util.Hashtable;
import java.util.Scanner;
import java.io.File;
import java.util.Collections;

/** Initial class for the 'make' program.
 *  @author D. Hsu
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
        Hashtable<String, Integer> names = new Hashtable<String, Integer>();
        Scanner file;
        Scanner make;
        try {
            file = new Scanner(new File(fileInfoName));
            make = new Scanner(new File(makefileName));
        } catch (IOException e) {
            usage();
            return;
        }
        int time = Integer.parseInt(file.next()); file.nextLine();
        List<Rule> rules = new ArrayList<Rule>();
        int i = 1;
        while (file.hasNextLine()) {
            String[] words = file.nextLine().split("\\s+");
            try {
                names.put(words[0], Integer.parseInt(words[1]));
            } catch (IllegalArgumentException e) {
                System.err.printf("Format error at line %d.\n", i);
                System.exit(1);
            }
            i++;
        }
        file.close(); i = 1; Rule rule = null;
        while (make.hasNextLine()) {
            String line = make.nextLine();
            String[] words;
            if (line.startsWith("#") || line.matches("\\s*\r*\n*")) {
                continue;
            } else if (line.matches("\\s+.+")) {
                rule.addArgument(line);
            } else {
                if (rule != null) {
                    rules.add(new Rule(rule)); rule = null;
                }
                words = line.split("\\s+");
                if (words[0].matches(".*[:=\\#].*:+")) {
                    System.out.printf("Error: Malformed type in makefile"
                            + " at line %d.\n", i);
                    System.exit(1);
                }
                if (words.length > 0) {
                    rule = new Rule(words[0].substring(
                            0, words[0].length() - 1));
                    for (int k = 1; k < words.length; k++) {
                        rule.addDependency(words[k]);
                    }
                }
            }
            i++;
        }
        if (rule != null) {
            rules.add(new Rule(rule));
        }
        make.close();
        process(rules, names, targets, time);
    }

    /** Builds TARGETS from RULES, with TIME being the time
     *  at the beginning of Main's execution and NAMES mapping
     *  names of targets to changedates. */
    private static void process(List<Rule> R,
            final Hashtable<String, Integer> names, final List<String> targets,
            final int time) {
        final Graph<Rule, String> make = new DirectedGraph<Rule, String>();
        final Hashtable<Rule, Graph<Rule, String>.Vertex> vertices =
                new Hashtable<Rule, Graph<Rule, String>.Vertex>();
        final HashSet<Graph<Rule, String>.Vertex> visited =
                new HashSet<Graph<Rule, String>.Vertex>();
        final List<Rule> rules = merge(R);
        for (String target : targets) {
            vertices.put(containsTarget(rules, target),
                    make.add(containsTarget(rules, target)));
        }
        if (targets.size() == 0) {
            targets.add(rules.get(0)._target);
        }

        Traversal<Rule, String> traverse = new Traversal<Rule, String>() {
            protected void preVisit(Graph<Rule, String>.Edge e,
            Graph<Rule, String>.Vertex v0) {
            }
            protected void visit(Graph<Rule, String>.Vertex v) {
                if (!toMake(names, v.getLabel(), time)) {
                    throw new RejectException();
                }
                visited.add(v);
                Rule rule = v.getLabel();
                for (String p : rule.deps) {
                    Rule prereq = containsTarget(rules, p);
                    for (Graph<Rule, String>.Vertex vert : make.successors(v)) {
                        if (vert.getLabel() == prereq) {
                            throw new StopException();
                        }
                    }
                    vertices.put(prereq,
                            make.add(prereq));
                    make.add(vertices.get(rule), vertices.get(prereq));
                }
            }
            protected void postVisit(Graph<Rule, String>.Vertex v) {
                build(v.getLabel());
            }
        };
        try {
        for (String target : targets) {
            Rule rule = containsTarget(rules, target);
            Graph<Rule, String>.Vertex vert = vertices.get(rule);
            if (vert == null) {
                continue;
            }
            traverse.depthFirstTraverse(make, vert);
        }
        } catch (StopException e) {
            System.err.println("Makefile contains an infinite cycle.");
            System.exit(1);
        }
    }


    /** Returns TRUE if RULE's target is older than its
     *  dependencies in NAMES. */
    private static boolean toMake(Hashtable<String, Integer> names,
            Rule rule, int time) {
        String target = rule._target;
        if (names.containsKey(target)) {
            for (String prereq : rule.deps) {
                if (names.containsKey(prereq)) {
                    if (names.get(prereq) > names.get(target)) {
                        names.put(target, time + 1);
                        return true;
                    }
                } else if (!names.containsKey(prereq)) {
                    return true;
                }
            }
            return false;
        } else {
            names.put(target, time + 1);
            return true;
        }
    }

    /** Merges any duplicate Rule entries in RULES. */
    private static List<Rule> merge(List<Rule> rules) {
        for (Rule rule : rules) {
            for (Rule copy : rules) {
                if (rule._target.equals(copy._target) && rule != copy) {
                    if (rule.args.size() != 0) {
                        if (copy.args.size() != 0) {
                            System.err.printf("Error: More than one command"
                                    + "set for target %s.", copy._target);
                            System.exit(1);
                        }
                    }
                    if (copy.args.size() != 0) {
                        if (rule.args.size() != 0) {
                            System.err.printf("Error: More than one command"
                                    + "set for target %s.", copy._target);
                            System.exit(1);
                        }
                    }
                    rule.deps.addAll(copy.deps);
                    rules.remove(copy);
                }
            }
        }
        return rules;
    }

    /** Builds the given rule. */
    private static void build(Rule rule) {
        for (String arg : rule.args) {
            System.out.println(arg);
        }
    }

    /** Returns a rule if RULES contains a rule with target TARGET. */
    private static Rule containsTarget(List<Rule> rules, String target) {
        for (Rule rule : rules) {
            if (rule._target.equals(target)) {
                return rule;
            }
        }
        return new Rule(target);
    }

    /** Print a brief usage message and exit program abnormally. */
    private static void usage() {
        Scanner usage;
        try {
        usage = new Scanner(new File("make/Usage.txt"));
        } catch (IOException e) {
            System.out.println("No help found.");
            System.exit(1);
            return;
        }
        while (usage.hasNextLine()) {
            System.out.println(usage.nextLine());
        }
        usage.close();
        System.exit(1);
    }

    /** Subclass Rule. Contains a target, a list of dependences, and a list
     *  of non-empty commands.
     */
    static class Rule {

        /** Constructor for Rules. TARGET is the makefile's target. */
        Rule(String target) {
            _target = target;
            deps = new ArrayList<String>();
            args = new ArrayList<String>();
        }

        /** Constructor to copy COPY, a Rule provided as input. */
        Rule(Rule copy) {
            _target = copy._target;
            deps = copy.deps;
            args = copy.args;
        }

        /** Adds ITEM to deps. */
        void addDependency(String item) {
            deps.add(item);
        }

        /** Adds ITEM to args. */
        void addArgument(String item) {
            args.add(item);
        }

        /** This Rule's target. */
        private String _target;
        /** A list containing dependences. May be empty. */
        private List<String> deps;
        /** A list containing command-line arguments. May not be empty. */
        private List<String> args;
    }
}
