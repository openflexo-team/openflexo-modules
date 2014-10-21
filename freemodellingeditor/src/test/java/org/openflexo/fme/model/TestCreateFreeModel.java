package org.openflexo.fme.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.fme.model.action.CreateFreeModel;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.OpenflexoProjectAtRunTimeTestCase;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * This unit test is intented to test project creation in the context of FreeModellingProjectNature
 * 
 * @author sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestCreateFreeModel extends OpenflexoProjectAtRunTimeTestCase {

	static FlexoEditor editor;
	static FlexoProject project;
	static FreeModellingProject fmProject;
	static FreeModel freeModel1;
	static FreeModel freeModel2;
	static FreeMetaModel freeMetaModel;

	@Test
	@TestOrder(1)
	public void testCreateFreeModellingEditorProject() {

		instanciateTestServiceManager();

		FreeModellingProjectNature FREE_MODELLING_NATURE = serviceManager.getProjectNatureService().getProjectNature(
				FreeModellingProjectNature.class);
		assertNotNull(FREE_MODELLING_NATURE);

		editor = createProject("TestFMEProject", FREE_MODELLING_NATURE);
		project = editor.getProject();
		System.out.println("Created project " + project.getProjectDirectory());
		assertTrue(project.getProjectDirectory().exists());
		assertTrue(project.getProjectDataResource().getFlexoIODelegate().exists());
		assertTrue(project.hasNature(FREE_MODELLING_NATURE));
		fmProject = FREE_MODELLING_NATURE.getFreeModellingProject(project);
		assertNotNull(fmProject);
	}

	@Test
	@TestOrder(2)
	public void testCreateFreeModel() {
		CreateFreeModel action = CreateFreeModel.actionType.makeNewAction(fmProject, null, editor);
		action.setFreeModelName("FreeModel1");
		assertTrue(action.isValid());
		action.doAction();
		assertTrue(action.hasActionExecutionSucceeded());
		freeMetaModel = action.getFreeMetaModel();
		assertNotNull(freeMetaModel);
		freeModel1 = action.getFreeModel();
		assertNotNull(freeModel1);
	}

	@Test
	@TestOrder(3)
	public void testAvoidDuplicatedFreeModel() {
		CreateFreeModel action = CreateFreeModel.actionType.makeNewAction(fmProject, null, editor);
		action.setFreeModelName("FreeModel1");
		assertFalse(action.isValid());
	}

	@Test
	@TestOrder(4)
	public void testCreateFreeModelWithSameMetaModel() {
		CreateFreeModel action = CreateFreeModel.actionType.makeNewAction(fmProject, null, editor);
		action.setFreeModelName("FreeModel2");
		action.setFreeMetaModel(freeMetaModel);
		action.setCreateNewMetaModel(false);
		assertTrue(action.isValid());
		action.doAction();
		assertTrue(action.hasActionExecutionSucceeded());
	}

	/**
	 * Reload project, check that everything is still ok
	 * 
	 * @throws FileNotFoundException
	 * @throws ResourceLoadingCancelledException
	 * @throws FlexoException
	 */
	@Test
	@TestOrder(5)
	public void testReloadProject() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		instanciateTestServiceManager();
		editor = reloadProject(project.getDirectory());
		project = editor.getProject();
		assertNotNull(editor);
		assertNotNull(project);

		FreeModellingProjectNature FREE_MODELLING_NATURE = serviceManager.getProjectNatureService().getProjectNature(
				FreeModellingProjectNature.class);
		assertNotNull(FREE_MODELLING_NATURE);

		assertTrue(project.hasNature(FREE_MODELLING_NATURE));
		fmProject = FREE_MODELLING_NATURE.getFreeModellingProject(project);
		assertNotNull(fmProject);

		assertNotNull(fmProject.getFreeModellingViewPoint());
		assertNotNull(fmProject.getFreeModellingView());

		assertEquals(1, fmProject.getFreeMetaModels().size());
		assertEquals(2, fmProject.getFreeModels().size());

		freeMetaModel = fmProject.getFreeMetaModel("FreeModel1");
		assertNotNull(freeMetaModel);

		freeModel1 = fmProject.getFreeModel("FreeModel1");
		assertNotNull(freeModel1);
		assertEquals(freeMetaModel, freeModel1.getMetaModel());

		freeModel2 = fmProject.getFreeModel("FreeModel2");
		assertNotNull(freeModel1);
		assertEquals(freeMetaModel, freeModel2.getMetaModel());

	}
}
