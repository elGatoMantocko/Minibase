JDKPATH = /usr
LIBPATH = lib/bufmgrAssign.jar
JUNITPATH = /usr/share/java/junit4.jar

CLASSPATH = .:..:$(LIBPATH):$(JUNITPATH)
BINPATH = $(JDKPATH)/bin
JAVAC = $(JDKPATH)/bin/javac 
JAVA  = $(JDKPATH)/bin/java 
TEST_DIR=./test/tests
TEST_NAMES=BMDriverTest
TEST_FILES=$(TEST_NAMES:=.java)
TEST_SOURCES=$(addprefix $(TEST_DIR)/, $(TEST_FILES))
TEST_CLASSES=$(TEST_SOURCES:.java=.class)

PROGS = xx

all: $(PROGS)

compile:src/*/*.java
	@mkdir -p ./bin
	$(JAVAC) -cp $(CLASSPATH) -d bin src/*/*.java

test: $(TEST_CLASSES)
	$(JAVA) -cp $(CLASSPATH) org.junit.runner.JUnitCore $(TEST_NAMES)

xx : compile
	$(JAVA) -cp $(CLASSPATH):bin tests.BMTest

$(TEST_CLASSES): compile $(TEST_SOURCES)
	$(JAVAC) -cp $(CLASSPATH):bin $(TEST_SOURCES)

clean:
	rm -rf ./bin
