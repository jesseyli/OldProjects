# This a Makefile, an input file for the GNU 'make' program.  For you 
# command-line and Emacs enthusiasts, this makes it possible to build
# this program with a single command:
#     make 
# You can also clean up junk files and .class files with
#     make clean
# To run style61b (our style enforcer) over your source files, type
#     make style
# Finally, you can run any tests you'd care to with
#     make check

SHELL = bash

STYLEPROG = style61b

PACKAGE = jump61

TEST_CORRECT = test-jump61

TEST_ERROR = test-jump61

# Flags to pass to Java compilations (include debugging info and report
# "unsafe" operations.)
JFLAGS = -g -Xlint:unchecked 

SRCS = $(wildcard $(PACKAGE)/*.java)

RESOURCES = $(wildcard $(PACKAGE)/*.txt)

CLASSES = $(SRCS:.java=.class)

# Test directories
TESTS = tests
CORRECT_TESTS = $(TESTS)/correct/*.in
ERROR_TESTS = $(TESTS)/error/*.in


# Tell make that these are not really files.
.PHONY: clean default compile style  \
	check unit blackbox jar dist

# By default, make sure all classes are present and check if any sources have
# changed since the last build.
default: compile

compile: $(CLASSES)

style:
	$(STYLEPROG) $(SRCS) 

$(CLASSES): $(PACKAGE)/sentinel

$(PACKAGE)/sentinel: $(SRCS)
	javac $(JFLAGS) $(SRCS)
	touch $@

# Run Tests.
check: unit blackbox

# Run util Junit tests.
unit: $(CLASSES)
	java -ea $(PACKAGE).UnitTest

# Run all blackbox tests for this package.
blackbox: compile
	@code=0; \
	echo "Testing correct inputs:"; \
	if ! $(TEST_CORRECT) $(CORRECT_TESTS); then code=1; fi; \
	echo; echo "Testing erroneous inputs:"; \
	if ! $(TEST_ERROR) $(ERROR_TESTS); then code=1; fi; \
	test $$code -eq 0

# Find and remove all *~ and *.class files, and the generated jar
# files.  Do not touch .svn directories.
clean:
	$(RM) */sentinel bin/*.jar
	$(RM) -r classes
	find . -name .svn -prune -o \
            \( -name '*.out' -o -name '*.class' -o -name '*~' \) \
            -exec $(RM) {} \;

-include Makefile.local
