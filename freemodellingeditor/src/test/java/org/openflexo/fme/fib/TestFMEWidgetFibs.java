package org.openflexo.fme.fib;

import org.junit.Test;
import org.openflexo.fib.utils.GenericFIBTestCase;
import org.openflexo.rm.FileResourceImpl;
import org.openflexo.rm.ResourceLocator;

public class TestFMEWidgetFibs extends GenericFIBTestCase {

	public static void main(String[] args) {
		System.out.println(generateFIBTestCaseClass(((FileResourceImpl) ResourceLocator.locateResource("Fib/Widget")).getFile(),
				"Fib/Widget/"));
	}

	@Test
	public void testFIBConceptBrowser() {
		validateFIB("Fib/Widget/FIBConceptBrowser.fib");
	}

	@Test
	public void testFIBFreeModellingProjectBrowser() {
		validateFIB("Fib/Widget/FIBFreeModellingProjectBrowser.fib");
	}

	@Test
	public void testFIBRepresentedConceptBrowser() {
		validateFIB("Fib/Widget/FIBRepresentedConceptBrowser.fib");
	}

}
