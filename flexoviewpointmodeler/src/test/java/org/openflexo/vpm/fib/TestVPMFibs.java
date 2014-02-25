package org.openflexo.vpm.fib;

import java.io.File;

import org.openflexo.fib.utils.GenericFIBTestCase;

public class TestVPMFibs extends GenericFIBTestCase {

	public static void main(String[] args) {
		System.out.println(generateFIBTestCaseClass(new File(System.getProperty("user.dir") + "/src/main/resources/Fib"), "Fib/"));
	}

	public void testActionSchemePanel() {
		validateFIB("Fib/ActionSchemePanel.fib");
	}

	public void testAddClassPanel() {
		validateFIB("Fib/AddClassPanel.fib");
	}

	public void testAddConnectorPanel() {
		validateFIB("Fib/AddConnectorPanel.fib");
	}

	public void testAddDiagramPanel() {
		validateFIB("Fib/AddDiagramPanel.fib");
	}

	public void testAddFlexoConceptInstancePanel() {
		validateFIB("Fib/AddFlexoConceptInstancePanel.fib");
	}

	public void testAddIndividualPanel() {
		validateFIB("Fib/AddIndividualPanel.fib");
	}

	public void testAddShapePanel() {
		validateFIB("Fib/AddShapePanel.fib");
	}

	public void testAssignationActionPanel() {
		validateFIB("Fib/AssignationActionPanel.fib");
	}

	public void testCloningSchemePanel() {
		validateFIB("Fib/CloningSchemePanel.fib");
	}

	public void testConditionalActionPanel() {
		validateFIB("Fib/ConditionalActionPanel.fib");
	}

	public void testCreationSchemePanel() {
		validateFIB("Fib/CreationSchemePanel.fib");
	}

	public void testDeclarePatternRolePanel() {
		validateFIB("Fib/DeclarePatternRolePanel.fib");
	}

	public void testDeletionActionPanel() {
		validateFIB("Fib/DeletionActionPanel.fib");
	}

	public void testDeletionSchemePanel() {
		validateFIB("Fib/DeletionSchemePanel.fib");
	}

	public void testDiagramFlexoConceptView() {
		validateFIB("Fib/DiagramFlexoConceptView.fib");
	}

	public void testDiagramSpecificationStructuralPanel() {
		validateFIB("Fib/DiagramSpecificationStructuralPanel.fib");
	}

	public void testDiagramSpecificationView() {
		validateFIB("Fib/DiagramSpecificationView.fib");
	}

	public void testDropSchemePanel() {
		validateFIB("Fib/DropSchemePanel.fib");
	}

	public void testFlexoConceptInspectorPanel() {
		validateFIB("Fib/FlexoConceptInspectorPanel.fib");
	}

	public void testFlexoConceptPanel() {
		validateFIB("Fib/FlexoConceptPanel.fib");
	}

	public void testEditionSchemePanel() {
		validateFIB("Fib/FlexoBehaviourPanel.fib");
	}

	public void testExecutionActionPanel() {
		validateFIB("Fib/ExecutionActionPanel.fib");
	}

	public void testFetchRequestIterationActionPanel() {
		validateFIB("Fib/FetchRequestIterationActionPanel.fib");
	}

	public void testGraphicalActionPanel() {
		validateFIB("Fib/GraphicalActionPanel.fib");
	}

	public void testIterationActionPanel() {
		validateFIB("Fib/IterationActionPanel.fib");
	}

	public void testLinkSchemePanel() {
		validateFIB("Fib/LinkSchemePanel.fib");
	}

	public void testLocalizedDictionaryPanel() {
		validateFIB("Fib/LocalizedDictionaryPanel.fib");
	}

	public void testMatchFlexoConceptInstancePanel() {
		validateFIB("Fib/MatchFlexoConceptInstancePanel.fib");
	}

	public void testNavigationSchemePanel() {
		validateFIB("Fib/NavigationSchemePanel.fib");
	}

	public void testProcedureActionPanel() {
		validateFIB("Fib/ProcedureActionPanel.fib");
	}

	public void testSelectFlexoConceptInstancePanel() {
		validateFIB("Fib/SelectFlexoConceptInstancePanel.fib");
	}

	public void testSelectIndividualPanel() {
		validateFIB("Fib/SelectIndividualPanel.fib");
	}

	public void testStandardFlexoConceptView() {
		validateFIB("Fib/StandardFlexoConceptView.fib");
	}

	public void testSynchronizationSchemePanel() {
		validateFIB("Fib/SynchronizationSchemePanel.fib");
	}

	public void testViewPointView() {
		validateFIB("Fib/ViewPointView.fib");
	}

	public void testVirtualModelStructuralPanel() {
		validateFIB("Fib/VirtualModelStructuralPanel.fib");
	}

	public void testVirtualModelView() {
		validateFIB("Fib/VirtualModelView.fib");
	}

}
