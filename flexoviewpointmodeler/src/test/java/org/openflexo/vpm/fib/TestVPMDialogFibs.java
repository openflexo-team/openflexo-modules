package org.openflexo.vpm.fib;

import java.io.File;

import org.openflexo.fib.utils.GenericFIBTestCase;

public class TestVPMDialogFibs extends GenericFIBTestCase {

	public static void main(String[] args) {
		System.out.println(generateFIBTestCaseClass(new File(System.getProperty("user.dir") + "/src/main/resources/Fib/Dialog"),
				"Fib/Dialog/"));
	}

	// No test yet

}
