/**
 * 
 * Copyright (c) 2013-2015, Openflexo
 * 
 * This file is part of Integration-tests, a component of the software infrastructure 
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

package org.openflexo.eam.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openflexo.OpenflexoProjectAtRunTimeTestCaseWithGUI;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rm.CompilationUnitResource;
import org.openflexo.modelers.ModelersConstants;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;
import org.openflexo.test.UITest;

/**
 * We test here Formose viewpoints
 * 
 * 
 * @author sylvain
 *
 */
@RunWith(OrderedRunner.class)
public class TestBPMNEditorVirtualModel extends OpenflexoProjectAtRunTimeTestCaseWithGUI {

	private static VirtualModel formoseVP;

	/**
	 * Instantiate test resource center
	 */
	@Test
	@TestOrder(1)
	@Category(UITest.class)
	public void instantiateResourceCenter() {

		log("testInstantiateResourceCenter()");

		instanciateTestServiceManager();

	}

	@Test
	@TestOrder(10)
	@Category(UITest.class)
	public void testBPMNEditorVirtualModel() {

		log("testBPMNEditorVirtualModel");

		CompilationUnitResource bmpnVirtualModelResource = serviceManager.getVirtualModelLibrary()
				.getCompilationUnitResource(ModelersConstants.BPMN_EDITOR_URI);
		assertNotNull(bmpnVirtualModelResource);

		VirtualModel bpmnVP;

		assertNotNull(bpmnVP = bmpnVirtualModelResource.getCompilationUnit().getVirtualModel());

		assertNotNull(bpmnVP);
		System.out.println("Found view point in " + bpmnVP.getResource().getIODelegate().toString());
		assertVirtualModelIsValid(bpmnVP);

		for (VirtualModel vm : bpmnVP.getVirtualModels()) {
			System.out.println(vm.getFMLPrettyPrint());
			assertObjectIsValid(vm);
		}

	}

}
