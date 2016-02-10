JDKPATH = /usr
LIBPATH = lib/heapAssign.jar
JUNITPATH = /usr/share/java/junit4.jar

CLASSPATH = .:..:$(LIBPATH):$(JUNITPATH)
BINPATH = $(JDKPATH)/bin
JAVAC = $(JDKPATH)/bin/javac 
JAVA  = $(JDKPATH)/bin/java 

TEST_DIR=./test
TEST_NAMES=DriverTest HeapFileTest
TEST_SOURCES=./test/tests/HFDriverTest.java ./test/heap/HeapFileTest.java

PROGS = xx

all: $(PROGS)

compile:src/*/*.java
	@mkdir -p ./bin
	$(JAVAC) -cp $(CLASSPATH) -d bin src/*/*.java

test: compile_tests
	$(JAVA) -cp $(CLASSPATH):bin:bin/tests:bin/heap org.junit.runner.JUnitCore heap.HeapFileTest tests.HFDriverTest

xx : compile
	$(JAVA) -cp $(CLASSPATH):bin tests.BMTest

compile_tests: compile
	$(JAVAC) -cp $(CLASSPATH):bin -d bin ./test/tests/HFDriverTest.java ./test/heap/HeapFileTest.java

clean:
	rm -rf ./bin
