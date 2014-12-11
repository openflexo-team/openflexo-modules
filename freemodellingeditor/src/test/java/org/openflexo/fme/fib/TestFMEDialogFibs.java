package org.openflexo.fme.fib;

import org.junit.Test;
import org.openflexo.fib.utils.GenericFIBTestCase;
import org.openflexo.rm.FileResourceImpl;
import org.openflexo.rm.ResourceLocator;

public class TestFMEDialogFibs extends GenericFIBTestCase {

	public static void main(String[] args) {
		System.out.println(generateFIBTestCaseClass(((FileResourceImpl) ResourceLocator.locateResource("Fib/Dialog")).getFile(),
				"Fib/Dialog/"));
	}

	@Test
	public void testDeclareInstanceOfExistingConceptDialog() {
		validateFIB("Fib/Dialog/DeclareInstanceOfExistingConceptDialog.fib");
	}

	@Test
	public void testDeclareInstanceOfExistingConceptFromDiagramElementDialog() {
		validateFIB("Fib/Dialog/DeclareInstanceOfExistingConceptFromDiagramElementDialog.fib");
	}

}
