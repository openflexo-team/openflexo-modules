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

package org.openflexo.eamodule.controller.action;

import javax.swing.Icon;

import org.openflexo.components.wizard.Wizard;
import org.openflexo.components.wizard.WizardDialog;
import org.openflexo.eamodule.EAMIconLibrary;
import org.openflexo.eamodule.model.EAProjectNature;
import org.openflexo.eamodule.model.action.CreateBPMNVirtualModelInstance;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.action.FlexoActionRunnable;
import org.openflexo.gina.controller.FIBController.Status;
import org.openflexo.view.controller.ActionInitializer;

public class CreateBPMNVirtualModelInstanceInitializer
		extends ActionInitializer<CreateBPMNVirtualModelInstance, EAProjectNature, FlexoObject> {
	protected CreateBPMNVirtualModelInstanceInitializer(EAMControllerActionInitializer actionInitializer) {
		super(CreateBPMNVirtualModelInstance.actionType, actionInitializer);
	}

	@Override
	protected EAMControllerActionInitializer getControllerActionInitializer() {
		return (EAMControllerActionInitializer) super.getControllerActionInitializer();
	}

	@Override
	protected FlexoActionRunnable<CreateBPMNVirtualModelInstance, EAProjectNature, FlexoObject> getDefaultInitializer() {
		return (e, action) -> {
			Wizard wizard = new CreateBPMNVirtualModelInstanceWizard(action, getController());
			WizardDialog dialog = new WizardDialog(wizard, getController());
			dialog.showDialog();
			if (dialog.getStatus() != Status.VALIDATED) {
				// Operation cancelled
				return false;
			}
			return true;
		};
	}

	@Override
	protected FlexoActionRunnable<CreateBPMNVirtualModelInstance, EAProjectNature, FlexoObject> getDefaultFinalizer() {
		return (e, action) -> {
			getController().selectAndFocusObject(action.getBPMNVirtualModelInstance());
			return true;
		};
	}

	@Override
	protected Icon getEnabledIcon(FlexoActionFactory<CreateBPMNVirtualModelInstance, EAProjectNature, FlexoObject> actionType) {
		return EAMIconLibrary.BPMN_SMALL_ICON;
	}

}
