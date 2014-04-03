package org.openflexo.vpm.fib;

import org.junit.Test;
import org.openflexo.fib.utils.GenericFIBInspectorTestCase;
import org.openflexo.rm.FileResourceImpl;
import org.openflexo.rm.ResourceLocator;

public class TestVPMInspectors extends GenericFIBInspectorTestCase {

	/*
	 * Use this method to print all
	 * Then copy-paste 
	 */

	public static void main(String[] args) {
		System.out.println(generateInspectorTestCaseClass(((FileResourceImpl) ResourceLocator.locateResource("Inspectors/VPM")).getFile(),
				"Inspectors/VPM/"));
	}

	@Test
	public void testAbstractActionSchemeInspector() {
		validateFIB("Inspectors/VPM/AbstractActionScheme.inspector");
	}

	@Test
	public void testAbstractCreationSchemeInspector() {
		validateFIB("Inspectors/VPM/AbstractCreationScheme.inspector");
	}

	@Test
	public void testActionSchemeInspector() {
		validateFIB("Inspectors/VPM/ActionScheme.inspector");
	}

	@Test
	public void testCheckboxParameterInspector() {
		validateFIB("Inspectors/VPM/CheckboxParameter.inspector");
	}

	@Test
	public void testClassParameterInspector() {
		validateFIB("Inspectors/VPM/ClassParameter.inspector");
	}

	@Test
	public void testClassPatternRoleInspector() {
		validateFIB("Inspectors/VPM/ClassPatternRole.inspector");
	}

	@Test
	public void testCloningSchemeInspector() {
		validateFIB("Inspectors/VPM/CloningScheme.inspector");
	}

	@Test
	public void testCreationSchemeInspector() {
		validateFIB("Inspectors/VPM/CreationScheme.inspector");
	}

	@Test
	public void testDataPropertyParameterInspector() {
		validateFIB("Inspectors/VPM/DataPropertyParameter.inspector");
	}

	@Test
	public void testDataPropertyPatternRoleInspector() {
		validateFIB("Inspectors/VPM/DataPropertyPatternRole.inspector");
	}

	@Test
	public void testDeletionSchemeInspector() {
		validateFIB("Inspectors/VPM/DeletionScheme.inspector");
	}

	@Test
	public void testDropDownParameterInspector() {
		validateFIB("Inspectors/VPM/DropDownParameter.inspector");
	}

	@Test
	public void testFlexoBehaviourInspector() {
		validateFIB("Inspectors/VPM/FlexoBehaviour.inspector");
	}

	@Test
	public void testFlexoBehaviourParameterInspector() {
		validateFIB("Inspectors/VPM/FlexoBehaviourParameter.inspector");
	}

	@Test
	public void testFlexoConceptInspector() {
		validateFIB("Inspectors/VPM/FlexoConcept.inspector");
	}

	@Test
	public void testFlexoConceptInstanceParameterInspector() {
		validateFIB("Inspectors/VPM/FlexoConceptInstanceParameter.inspector");
	}

	@Test
	public void testFlexoConceptInstanceRoleInspector() {
		validateFIB("Inspectors/VPM/FlexoConceptInstanceRole.inspector");
	}

	@Test
	public void testFlexoConceptObjectInspector() {
		validateFIB("Inspectors/VPM/FlexoConceptObject.inspector");
	}

	@Test
	public void testFlexoRoleInspector() {
		validateFIB("Inspectors/VPM/FlexoRole.inspector");
	}

	@Test
	public void testFloatParameterInspector() {
		validateFIB("Inspectors/VPM/FloatParameter.inspector");
	}

	@Test
	public void testIndividualParameterInspector() {
		validateFIB("Inspectors/VPM/IndividualParameter.inspector");
	}

	@Test
	public void testIndividualRoleInspector() {
		validateFIB("Inspectors/VPM/IndividualRole.inspector");
	}

	@Test
	public void testInnerModelSlotParameterInspector() {
		validateFIB("Inspectors/VPM/InnerModelSlotParameter.inspector");
	}

	@Test
	public void testIntegerParameterInspector() {
		validateFIB("Inspectors/VPM/IntegerParameter.inspector");
	}

	@Test
	public void testListParameterInspector() {
		validateFIB("Inspectors/VPM/ListParameter.inspector");
	}

	@Test
	public void testModelSlotInspector() {
		validateFIB("Inspectors/VPM/ModelSlot.inspector");
	}

	@Test
	public void testNamedViewPointObjectInspector() {
		validateFIB("Inspectors/VPM/NamedViewPointObject.inspector");
	}

	@Test
	public void testNavigationSchemeInspector() {
		validateFIB("Inspectors/VPM/NavigationScheme.inspector");
	}

	@Test
	public void testObjectPropertyParameterInspector() {
		validateFIB("Inspectors/VPM/ObjectPropertyParameter.inspector");
	}

	@Test
	public void testObjectPropertyPatternRoleInspector() {
		validateFIB("Inspectors/VPM/ObjectPropertyPatternRole.inspector");
	}

	@Test
	public void testOntologicObjectPatternRoleInspector() {
		validateFIB("Inspectors/VPM/OntologicObjectPatternRole.inspector");
	}

	@Test
	public void testPrimitivePatternRoleInspector() {
		validateFIB("Inspectors/VPM/PrimitivePatternRole.inspector");
	}

	@Test
	public void testPropertyParameterInspector() {
		validateFIB("Inspectors/VPM/PropertyParameter.inspector");
	}

	@Test
	public void testPropertyPatternRoleInspector() {
		validateFIB("Inspectors/VPM/PropertyPatternRole.inspector");
	}

	@Test
	public void testSynchronizationSchemeInspector() {
		validateFIB("Inspectors/VPM/SynchronizationScheme.inspector");
	}

	@Test
	public void testTextAreaParameterInspector() {
		validateFIB("Inspectors/VPM/TextAreaParameter.inspector");
	}

	@Test
	public void testTextFieldParameterInspector() {
		validateFIB("Inspectors/VPM/TextFieldParameter.inspector");
	}

	@Test
	public void testTypeAwareModelSlotInspector() {
		validateFIB("Inspectors/VPM/TypeAwareModelSlot.inspector");
	}

	@Test
	public void testURIParameterInspector() {
		validateFIB("Inspectors/VPM/URIParameter.inspector");
	}

	@Test
	public void testViewPointInspector() {
		validateFIB("Inspectors/VPM/ViewPoint.inspector");
	}

	@Test
	public void testViewPointLibraryInspector() {
		validateFIB("Inspectors/VPM/ViewPointLibrary.inspector");
	}

	@Test
	public void testViewPointObjectInspector() {
		validateFIB("Inspectors/VPM/ViewPointObject.inspector");
	}

	@Test
	public void testViewPointResourceInspector() {
		validateFIB("Inspectors/VPM/ViewPointResource.inspector");
	}

	@Test
	public void testVirtualModelInspector() {
		validateFIB("Inspectors/VPM/VirtualModel.inspector");
	}

	@Test
	public void testVirtualModelModelSlotInspector() {
		validateFIB("Inspectors/VPM/VirtualModelModelSlot.inspector");
	}

	@Test
	public void testVirtualModelResourceInspector() {
		validateFIB("Inspectors/VPM/VirtualModelResource.inspector");
	}

}
