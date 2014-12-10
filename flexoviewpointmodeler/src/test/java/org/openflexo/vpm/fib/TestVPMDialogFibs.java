package org.openflexo.vpm.fib;

import java.io.File;

import org.junit.Test;
import org.openflexo.fib.utils.GenericFIBTestCase;

public class TestVPMDialogFibs extends GenericFIBTestCase {

	public static void main(String[] args) {
		System.out.println(generateFIBTestCaseClass(new File(System.getProperty("user.dir") + "/src/main/resources/Fib/Dialog"),
				"Fib/Dialog/"));
	}

	@Test
	public void testCreateEditionActionDialog() {
		validateFIB("Fib/Dialog/CreateEditionActionDialog.fib");
	}

	@Test
	public void testShowFMLRepresentationDialog() {
		validateFIB("Fib/Dialog/ShowFMLRepresentationDialog.fib");
	}

}
