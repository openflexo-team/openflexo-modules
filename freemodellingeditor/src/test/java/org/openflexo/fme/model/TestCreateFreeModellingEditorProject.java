package org.openflexo.fme.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.OpenflexoProjectAtRunTimeTestCase;
import org.openflexo.test.TestOrder;

/**
 * This unit test is intented to test project creation in the context of FreeModellingProjectNature
 * 
 * @author sylvain
 * 
 */
public class TestCreateFreeModellingEditorProject extends OpenflexoProjectAtRunTimeTestCase {

	@Test
	@TestOrder(1)
	public void testCreateFreeModellingEditorProject() {

		instanciateTestServiceManager();

		FreeModellingProjectNature FREE_MODELLING_NATURE = serviceManager.getProjectNatureService().getProjectNature(
				FreeModellingProjectNature.class);
		assertNotNull(FREE_MODELLING_NATURE);

		FlexoEditor editor = createProject("TestFMEProject", FREE_MODELLING_NATURE);
		FlexoProject project = editor.getProject();
		System.out.println("Created project " + project.getProjectDirectory());
		assertTrue(project.getProjectDirectory().exists());
		assertTrue(project.getProjectDataResource().getFile().exists());
		assertTrue(project.hasNature(FREE_MODELLING_NATURE));
	}

}
