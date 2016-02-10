JDKPATH = /usr
LIBPATH = lib/bufmgrAssign.jar
JUNITPATH = /usr/share/java/junit4.jar

CLASSPATH = .:..:$(LIBPATH):$(JUNITPATH)
BINPATH = $(JDKPATH)/bin
JAVAC = $(JDKPATH)/bin/javac 
JAVA  = $(JDKPATH)/bin/java 

TEST_DIR=./test
TEST_NAMES=BMDriverTest BufMgrTest FrameTest
TEST_SOURCES=./test/tests/BMDriverTest.java ./test/bufmgr/BufMgrTest.java ./test/bufmgr/FrameTest.java

PROGS = xx

all: $(PROGS)

compile:src/*/*.java
	@mkdir -p ./bin
	$(JAVAC) -cp $(CLASSPATH) -d bin src/*/*.java

test: compile_tests
	$(JAVA) -cp $(CLASSPATH):bin:bin/tests:bin/bufmgr org.junit.runner.JUnitCore bufmgr.BufMgrTest bufmgr.FrameTest tests.BMDriverTest

xx : compile
	$(JAVA) -cp $(CLASSPATH):bin tests.BMTest

compile_tests: compile
	$(JAVAC) -cp $(CLASSPATH):bin -d bin ./test/tests/BMDriverTest.java ./test/bufmgr/BufMgrTest.java ./test/bufmgr/FrameTest.java

clean:
	rm -rf ./bin
