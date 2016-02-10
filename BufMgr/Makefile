JDKPATH = /usr
LIBPATH = lib/bufmgrAssign.jar

CLASSPATH = .:..:$(LIBPATH):$(JUNITPATH)
BINPATH = $(JDKPATH)/bin
JAVAC = $(JDKPATH)/bin/javac 
JAVA  = $(JDKPATH)/bin/java 

TEST_DIR=./test
TEST_NAMES=tests.BMDriverTest bufmgr.BufMgrTest bufmgr.FrameTest
TEST_SOURCES=./test/tests/*.java ./test/bufmgr/*.java

PROGS = xx

all: $(PROGS)

compile:src/*/*.java
	@mkdir -p ./bin
	$(JAVAC) -cp $(CLASSPATH) -d bin src/*/*.java

test: compile_tests
	$(JAVA) -cp $(CLASSPATH):bin:bin/tests:bin/bufmgr org.junit.runner.JUnitCore $(TEST_NAMES)

xx : compile
	$(JAVA) -cp $(CLASSPATH):bin tests.BMTest

compile_tests: compile
	$(JAVAC) -cp $(CLASSPATH):bin -d bin $(TEST_SOURCES)

clean:
	rm -rf ./bin
