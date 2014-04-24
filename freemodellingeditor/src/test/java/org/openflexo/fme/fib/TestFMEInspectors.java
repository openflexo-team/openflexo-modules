package org.openflexo.fme.fib;

import org.junit.Test;
import org.openflexo.fib.utils.GenericFIBInspectorTestCase;
import org.openflexo.rm.FileResourceImpl;
import org.openflexo.rm.ResourceLocator;

public class TestFMEInspectors extends GenericFIBInspectorTestCase {

	/*
	 * Use this method to print all
	 * Then copy-paste 
	 */

	public static void main(String[] args) {
		System.out.println(generateInspectorTestCaseClass(((FileResourceImpl) ResourceLocator.locateResource("Inspectors/FME")).getFile(),
				"Inspectors/FME/"));
	}

	@Test
	public void testFreeModelInspector() {
		validateFIB("Inspectors/FME/FreeModel.inspector");
	}

}
