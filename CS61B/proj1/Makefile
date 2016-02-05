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

PACKAGE = tex61

UCBJAR = $(HOME)/lib/classes/ucb.jar

TEST_CORRECT = ./test-correct

TEST_ERROR = ./test-error

DESTDIR = cs61b@torus.cs:bin

# Flags to pass to Java compilations (include debugging info and report
# "unsafe" operations.)
JFLAGS = -g -Xlint:unchecked 

SRCS = $(wildcard $(PACKAGE)/*.java)

CLASSES = $(SRCS:.java=.class)

# Test directories
TESTS = tests
CORRECT_TESTS = $(TESTS)/correct/*.tx
ERROR_TESTS = $(TESTS)/error/*.tx


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

jar:
	$(RM) -r classes
	mkdir classes
	javac -d classes $(SRCS)
	cp $(RESOURCES) classes/$(PACKAGE)
	cd classes; jar xf $(UCBJAR) ucb/util; \
	jar cvfe ../bin/$(PACKAGE).jar $(PACKAGE).Main $(PACKAGE) ucb

dist: jar
	rsync -av --exclude .svn bin/ $(DESTDIR)


# Find and remove all *~ and *.class files, and the generated jar
# files.  Do not touch .svn directories.
clean:
	$(RM) */sentinel bin/*.jar
	$(RM) -r classes
	find . -name .svn -prune -o \
            \( -name '*.out' -o -name '*.class' -o -name '*~' \) \
            -exec $(RM) {} \;
