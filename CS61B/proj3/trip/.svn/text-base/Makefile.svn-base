# This a Makefile, an input file for the 'make' program.  For you 
# command-line and Emacs enthusiasts, this makes it possible to build
# this program with a single command:
#     gmake 
# (or just 'make' if you are on a system that uses GNU make by default,
# such as Linux.) You can also clean up junk files and .class files with
#     gmake clean
# To run style61b (our style enforcer) over your source files, type
#     gmake style
# Finally, you can run tests with
#     gmake check

# This is not an especially efficient Makefile, because it's not easy to
# figure out the minimal set of Java files that need to be recompiled.  
# So if any .class file does not exist or is older than its .java file,
# we just remove all the .class files, compile the main class, and 
# then compile everything in the plugin directory.  

# All source files
SRCS := $(wildcard *.java)

# Unit test files.
TESTING_SRCS = Testing.java 

# Flags to pass to Java compilations (include debugging info and report
# "unsafe" operations.)
JFLAGS = -g -Xlint:unchecked -Xlint:deprecation -sourcepath .. \
	-classpath ..:$(CLASSPATH)

CLASSES = $(SRCS:.java=.class)
TESTING_CLASSES = $(TESTING_SRCS:.java=.class)

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

# Run Tests.
check: unit-test regression-test

# Run Junit tests.
unit-test: $(CLASSES)
	java -ea -classpath ..:$(CLASSPATH) trip.Testing

# Run regression tests
regression-test: $(CLASSES)
	cd ..; ./test-app trip-tests/*.tst

# Check style of source files with style61b.
style:
	style61b $(STYLEFLAGS) $(SRCS)

# Find and remove all *~, *.class, and testing output files.
# Do not touch .svn directories.
clean :
	$(RM) *~ *.class


