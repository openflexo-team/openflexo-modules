package org.openflexo.fme.fib;

import org.junit.Test;
import org.openflexo.fib.utils.GenericFIBTestCase;
import org.openflexo.rm.FileResourceImpl;
import org.openflexo.rm.ResourceLocator;

public class TestFMEWizardFibs extends GenericFIBTestCase {

	public static void main(String[] args) {
		System.out.println(generateFIBTestCaseClass(((FileResourceImpl) ResourceLocator.locateResource("Fib/Wizard")).getFile(),
				"Fib/Wizard/"));
	}

	@Test
	public void testConfigureNewConcept() {
		validateFIB("Fib/Wizard/ConfigureNewConcept.fib");
	}

	@Test
	public void testConfigureNewConceptFromNoneConcept() {
		validateFIB("Fib/Wizard/ConfigureNewConceptFromNoneConcept.fib");
	}

	@Test
	public void testDescribeFreeModel() {
		validateFIB("Fib/Wizard/DescribeFreeModel.fib");
	}

	@Test
	public void testDescribeFreeModelDiagram() {
		validateFIB("Fib/Wizard/DescribeFreeModelDiagram.fib");
	}

}
