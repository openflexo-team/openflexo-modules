/**
 * 
 * Copyright (c) 2013-2015, Openflexo
 * Copyright (c) 2011-2012, AgileBirds
 * 
 * This file is part of Flexovieweditor, a component of the software infrastructure 
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

package org.openflexo.ve.fib.dialogs;

import java.io.File;

import org.openflexo.fib.ProjectDialogEDITOR;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.rt.View;
import org.openflexo.foundation.fml.rt.action.CreateVirtualModelInstance;
import org.openflexo.foundation.fml.rt.action.CreateVirtualModelInstance.CreateConcreteVirtualModelInstance;
import org.openflexo.toolbox.FileResource;
import org.openflexo.ve.VECst;

public class ConfigureModelInstanceDialogEDITOR extends ProjectDialogEDITOR {

	@Override
	public Object[] getData() {
		FlexoEditor editor = loadProject(new FileResource("TestProjects/1.6/Test1.6.prj"));
		FlexoProject project = editor.getProject();
		View v = project.getViewLibrary().getRootFolder().getResources().get(0).getView();
		CreateConcreteVirtualModelInstance action = CreateVirtualModelInstance.actionType.makeNewAction(v, null, editor);
		action.setVirtualModel(v.getViewPoint().getVirtualModels().get(0));
		return makeArray(action.getModelSlotInstanceConfiguration(action.getVirtualModel().getModelSlots().get(0)));
	}

	@Override
	public File getFIBFile() {
		return VECst.CONFIGURE_FREE_MODEL_SLOT_INSTANCE_DIALOG_FIB;
	}

	public static void main(String[] args) {
		main(ConfigureModelInstanceDialogEDITOR.class);
	}

}