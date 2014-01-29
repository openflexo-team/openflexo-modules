package org.openflexo.ve.fib;

import java.io.File;

import org.openflexo.fib.utils.GenericFIBTestCase;

public class TestVEWidgetFibs extends GenericFIBTestCase {

	public static void main(String[] args) {
		System.out.println(generateFIBTestCaseClass(new File(System.getProperty("user.dir") + "/src/main/resources/Fib/Widget"),
				"Fib/Widget/"));
	}

	public void testFIBViewLibraryBrowser() {
		validateFIB("Fib/Widget/FIBViewLibraryBrowser.fib");
	}

}
