package org.openflexo.vpm.fib;

import java.io.File;

import org.junit.Test;
import org.openflexo.fib.utils.GenericFIBTestCase;

public class TestVPMDialogFibs extends GenericFIBTestCase {

	public static void main(String[] args) {
		System.out.println(generateFIBTestCaseClass(new File(System.getProperty("user.dir") + "/src/main/resources/Fib/Dialog"),
				"Fib/Dialog/"));
	}

	public void testCreateDiagramSpecificationDialog() {
		validateFIB("Fib/Dialog/CreateDiagramSpecificationDialog.fib");
	}

	@Test
	public void testCreateEditionActionDialog() {
		validateFIB("Fib/Dialog/CreateEditionActionDialog.fib");
	}

	public void testCreateExampleDrawingDialog() {
		validateFIB("Fib/Dialog/CreateExampleDrawingDialog.fib");
	}

	@Test
	public void testCreateModelSlotDialog() {
		validateFIB("Fib/Dialog/CreateModelSlotDialog.fib");
	}

	public void testCreatePaletteDialog() {
		validateFIB("Fib/Dialog/CreatePaletteDialog.fib");
	}

	@Test
	public void testCreatePatternRoleDialog() {
		validateFIB("Fib/Dialog/CreateFlexoRoleDialog.fib");
	}

	@Test
	public void testCreateViewPointDialog() {
		validateFIB("Fib/Dialog/CreateViewPointDialog.fib");
	}

	@Test
	public void testCreateVirtualModelDialog() {
		validateFIB("Fib/Dialog/CreateVirtualModelDialog.fib");
	}

	public void testPushToPaletteDialog() {
		validateFIB("Fib/Dialog/PushToPaletteDialog.fib");
	}

	@Test
	public void testShowFMLRepresentationDialog() {
		validateFIB("Fib/Dialog/ShowFMLRepresentationDialog.fib");
	}

}
