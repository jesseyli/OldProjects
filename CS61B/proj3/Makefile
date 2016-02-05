# This a Makefile, an input file for the 'make' program.  For you 
# command-line and Emacs enthusiasts, this makes it possible to build
# this program with a single command:
#     make 
# (or just 'make' if you are on a system that uses GNU make by default,
# such as Linux.) You can also clean up junk files and .class files with
#     make clean
# To run style61b (our style enforcer) over your source files, type
#     make style
# Finally, you can run tests with
#     make check

# This is not an especially efficient Makefile, because it's not easy to
# figure out the minimal set of Java files that need to be recompiled.  
# So if any .class file does not exist or is older than its .java file,
# we just remove all the .class files, compile the main class, and 
# then compile everything in the plugin directory.  

SHELL = bash 

STYLEPROG = style61b

GRAPH_SRCS := $(wildcard graph/*.java)
MAKE_SRCS := $(wildcard make/*.java)
TRIP_SRCS := $(wildcard trip/*.java)

# All source files
SRCS := $(GRAPH_SRCS) $(MAKE_SRCS) $(TRIP_SRCS)

# Test-spec files
ALL_TESTS = $(wildcard make-tests/*.tst) $(wildcard trip-tests/*.tst)

# Flags to pass to Java compilations (include debugging info and report
# "unsafe" operations.)
JFLAGS = -g -Xlint:unchecked -Xlint:deprecation

CLASSES = $(SRCS:.java=.class)

# Tell make that these are not really files.
.PHONY: clean default check regression-test unit-test style

# By default, make sure all classes are present and check if any sources have
# changed since the last build.
default: $(CLASSES)

# If any class is missing, or any source changed since the main classes were
# compiled, remove all class files and recompile.
$(CLASSES): $(SRCS)
	$(RM) $(CLASSES)
	javac $(JFLAGS) $(SRCS) || { $(RM) $(CLASSES); false; }

check: $(CLASSES)
	$(MAKE) -C graph check
	$(MAKE) -C make check
	$(MAKE) -C trip check

# Check style of source files.
style: $(CLASSES)
	$(STYLEPROG) $(SRCS)

# Find and remove all *~, *.class, and testing output files.
# Do not touch .svn directories.
clean :
	$(RM) *~
	$(MAKE) -C graph clean
	$(MAKE) -C trip clean
	$(MAKE) -C make clean
