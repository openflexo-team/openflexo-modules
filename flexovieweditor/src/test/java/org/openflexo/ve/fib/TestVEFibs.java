package org.openflexo.ve.fib;

import java.io.File;

import org.openflexo.fib.utils.GenericFIBTestCase;

public class TestVEFibs extends GenericFIBTestCase {

	public static void main(String[] args) {
		System.out.println(generateFIBTestCaseClass(new File(System.getProperty("user.dir") + "/src/main/resources/Fib"), "Fib/"));
	}

	public void testOntologyView() {
		validateFIB("Fib/OntologyView.fib");
	}

	public void testVirtualModelInstanceView() {
		validateFIB("Fib/VirtualModelInstanceView.fib");
	}

}
