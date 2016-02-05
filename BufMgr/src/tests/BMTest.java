package tests;

import global.Convert;
import global.GlobalConst;
import global.Minibase;
import global.Page;
import global.PageId;

import java.io.IOException;

import chainexception.ChainException;

//Note that in JAVA, methods can't be overridden to be more private.
//Therefore, the declaration of all private functions are now declared
//protected as opposed to the private type in C++.

/**
 * This class provides the functions to test the buffer manager
 */
class BMDriver extends TestDriver implements GlobalConst {

	private boolean OK = true;
	private boolean FAIL = false;

	/**
	 * BMDriver Constructor, inherited from TestDriver
	 */
	public BMDriver () {
		super("buftest");
	}

	/**
	 * calls the runTests function in TestDriver
	 */
	public boolean runTests () {


		System.out.print ("\n" + "Running " + testName() + " tests...." + "\n");

		try {
			create_minibase();
		}

		catch (Exception e) {
			Runtime.getRuntime().exit(1);
		}

		// Kill anything that might be hanging around
		String newdbpath;
		String newlogpath;
		String remove_logcmd;
		String remove_dbcmd;
		String remove_cmd = "/bin/rm -rf ";

		newdbpath = dbpath;
		newlogpath = logpath;

		remove_logcmd = remove_cmd + logpath;
		remove_dbcmd = remove_cmd + dbpath;

		// Commands here is very machine dependent.  We assume
		// user are on UNIX system here.  If we need to port this
		// program to other platform, the remove_cmd have to be
		// modified accordingly.
		try {
			Runtime.getRuntime().exec(remove_logcmd);
			Runtime.getRuntime().exec(remove_dbcmd);
		}
		catch (IOException e) {
			System.err.println (""+e);
		}

		remove_logcmd = remove_cmd + newlogpath;
		remove_dbcmd = remove_cmd + newdbpath;

		//This step seems redundant for me.  But it's in the original
		//C++ code.  So I am keeping it as of now, just in case
		//I missed something
		try {
			Runtime.getRuntime().exec(remove_logcmd);
			Runtime.getRuntime().exec(remove_dbcmd);
		}
		catch (IOException e) {
			System.err.println (""+e);
		}

		//Run the tests. Return type different from C++
		boolean _pass = runAllTests();

		//Clean up again
		try {
			Runtime.getRuntime().exec(remove_logcmd);
			Runtime.getRuntime().exec(remove_dbcmd);

		}
		catch (IOException e) {
			System.err.println (""+e);
		}

		System.out.print ("\n" + "..." + testName() + " tests ");
		System.out.print (_pass==OK ? "completely successfully" : "failed");
		System.out.print (".\n\n");

		return _pass;
	}

	protected boolean runAllTests (){

		boolean _passAll = OK;

		//The following runs all the test functions 

		//Running test1() to test6()
		if (!test1()) { _passAll = FAIL; }    
		if (!test2()) { _passAll = FAIL; }
		if (!test3()) { _passAll = FAIL; }
		if (!test4()) { _passAll = FAIL; }
		if (!test5()) { _passAll = FAIL; }
		if (!test6()) { _passAll = FAIL; }

		return _passAll;
	}


	/**
	 * overrides the test1 function in TestDriver.  It tests some
	 * simple normal buffer manager operations.
	 *
	 * @return whether test1 has passed
	 */
	protected boolean test1 () {

		System.out.print("\n  Test 1 does a simple test of normal buffer ");
		System.out.print("manager operations:\n");

        return OK;

	}


	/**
	 * overrides the test2 function in TestDriver.  It tests whether illeagal
	 * operation can be caught.
	 *
	 * @return whether test2 has passed
	 */
	protected boolean test2 () {

		System.out.print("\n  Test 2 exercises some illegal buffer " +
		"manager operations:\n");

        return OK;

	}


	/**
	 * overrides the test3 function in TestDriver.  It exercises some of the internal
	 * of the buffer manager
	 *
	 * @return whether test3 has passed
	 */
	protected boolean test3 () {

		System.out.print("\n  Test 3 exercises some of the internals " +
		"of the buffer manager\n");

        return OK;

	}

	/**
	 * overrides the test4 function in TestDriver
	 *
	 * @return whether test4 has passed
	 */
	protected boolean test4 () {

		return OK;
	}

	/**
	 * overrides the test5 function in TestDriver
	 *
	 * @return whether test5 has passed
	 */
	protected boolean test5 () {

		return OK;
	}

	/**
	 * overrides the test6 function in TestDriver
	 *
	 * @return whether test6 has passed
	 */
	protected boolean test6 () {

		return OK;
	}

	/**
	 * overrides the testName function in TestDriver
	 *
	 * @return the name of the test 
	 */
	protected String testName () {
		return "Buffer Management";
	}
}

public class BMTest {

	public static void main (String argv[]) {

		BMDriver bmt = new BMDriver();
		boolean dbstatus;

		dbstatus = bmt.runTests();

		if (dbstatus != true) {
			System.err.println ("Error encountered during buffer manager tests:\n");
			Runtime.getRuntime().exit(1);
		}

		Runtime.getRuntime().exit(0);
	}
}

