JDKPATH = /usr
LIBPATH = lib/bufmgrAssign.jar

CLASSPATH = .:..:$(LIBPATH)
BINPATH = $(JDKPATH)/bin
JAVAC = $(JDKPATH)/bin/javac 
JAVA  = $(JDKPATH)/bin/java 

PROGS = xx

all: $(PROGS)

compile:src/*/*.java
	@mkdir -p ./bin
	$(JAVAC) -cp $(CLASSPATH) -d bin src/*/*.java

xx : compile
	$(JAVA) -cp $(CLASSPATH):bin tests.BMTest

clean:
	rm -rf ./bin
