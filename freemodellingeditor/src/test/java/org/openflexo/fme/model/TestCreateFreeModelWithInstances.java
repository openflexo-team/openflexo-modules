/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Freemodellingeditor, a component of the software infrastructure 
 * developed at Openflexo.
 * 
 * 
 * Openflexo is dual-licensed under the European Union Public License (EUPL, either 
 * version 1.1 of the License, or any later version ), which is available at 
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 * and the GNU General Public License (GPL, either version 3 of the License, or any 
 * later version), which is available at http://www.gnu.org/licenses/gpl.html .
 * 
 * You can redistribute it and/or modify under the terms of either of these licenses
 * 
 * If you choose to redistribute it and/or modify under the terms of the GNU GPL, you
 * must include the following additional permission.
 *
 *          Additional permission under GNU GPL version 3 section 7
 *
 *          If you modify this Program, or any covered work, by linking or 
 *          combining it with software containing parts covered by the terms 
 *          of EPL 1.0, the licensors of this Program grant you additional permission
 *          to convey the resulting work. * 
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. 
 *
 * See http://www.openflexo.org/license.html for details.
 * 
 * 
 * Please contact Openflexo (openflexo-contacts@openflexo.org)
 * or visit www.openflexo.org if you need additional information.
 * 
 */

package org.openflexo.fme.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.fge.geom.FGEPoint;
import org.openflexo.fme.model.action.CreateFreeModel;
import org.openflexo.fme.model.action.DropShape;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.OpenflexoProjectAtRunTimeTestCase;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * This unit test is intented to test project creation in the context of FreeModellingProjectNature
 * 
 * @author sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestCreateFreeModelWithInstances extends OpenflexoProjectAtRunTimeTestCase {

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
	public void testCreateInstance() {
		DropShape action = DropShape.actionType.makeNewAction(freeModel1.getDiagram(), null, editor);
		action.setFreeModel(freeModel1);
		action.setDropLocation(new FGEPoint(12,34));
		action.doAction();
		assertTrue(action.hasActionExecutionSucceeded());
		List<FlexoConceptInstance> result = freeModel1.getInstances(freeModel1.getMetaModel().getNoneFlexoConcept(editor, action));
		assertEquals(1,result.size());
		FlexoConceptInstance tutu = result.get(0);
		assertNotNull(tutu);
		DiagramShape laShapeDeLinstance = tutu.getFlexoActor(FreeMetaModel.SHAPE_ROLE_NAME);
		assertNotNull(laShapeDeLinstance);
		List<DiagramShape> lesShapes = freeModel1.getDiagram().getShapes();
		assertNotNull(lesShapes);
		assertEquals(1,lesShapes.size());
		assertSame(laShapeDeLinstance,lesShapes.get(0));
	}
}