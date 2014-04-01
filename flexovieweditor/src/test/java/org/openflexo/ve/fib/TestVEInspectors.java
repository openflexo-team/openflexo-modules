package org.openflexo.ve.fib;

import org.junit.Test;
import org.openflexo.fib.utils.GenericFIBInspectorTestCase;
import org.openflexo.rm.FileResourceImpl;
import org.openflexo.rm.ResourceLocator;

public class TestVEInspectors extends GenericFIBInspectorTestCase {

	/*
	 * Use this method to print all
	 * Then copy-paste 
	 */

	public static void main(String[] args) {
		System.out.println(generateInspectorTestCaseClass(((FileResourceImpl) ResourceLocator.locateResource("Inspectors/VE")).getFile(),
				"Inspectors/VE/"));
	}

	@Test
	public void testViewInspector() {
		validateFIB("Inspectors/VE/View.inspector");
	}

	@Test
	public void testViewLibraryInspector() {
		validateFIB("Inspectors/VE/ViewLibrary.inspector");
	}

	@Test
	public void testVirtualModelInstanceInspector() {
		validateFIB("Inspectors/VE/VirtualModelInstance.inspector");
	}

}
