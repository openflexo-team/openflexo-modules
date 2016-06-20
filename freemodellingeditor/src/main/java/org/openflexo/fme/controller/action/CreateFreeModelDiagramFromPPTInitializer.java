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

package org.openflexo.fme.controller.action;

import java.util.EventObject;
import java.util.logging.Logger;

import javax.swing.Icon;

import org.openflexo.components.wizard.Wizard;
import org.openflexo.components.wizard.WizardDialog;
import org.openflexo.fme.model.FreeMetaModel;
import org.openflexo.fme.model.action.CreateFreeModelDiagramFromPPT;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.action.FlexoActionFinalizer;
import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.gina.controller.FIBController.Status;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.technologyadapter.powerpoint.PowerpointTechnologyAdapter;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;

public class CreateFreeModelDiagramFromPPTInitializer extends ActionInitializer<CreateFreeModelDiagramFromPPT, FreeMetaModel, FlexoObject> {

	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	CreateFreeModelDiagramFromPPTInitializer(FMEControllerActionInitializer actionInitializer) {
		super(CreateFreeModelDiagramFromPPT.actionType, actionInitializer);
	}

	@Override
	protected FMEControllerActionInitializer getControllerActionInitializer() {
		return (FMEControllerActionInitializer) super.getControllerActionInitializer();
	}

	@Override
	protected FlexoActionInitializer<CreateFreeModelDiagramFromPPT> getDefaultInitializer() {
		return new FlexoActionInitializer<CreateFreeModelDiagramFromPPT>() {
			@Override
			public boolean run(EventObject e, CreateFreeModelDiagramFromPPT action) {
				// We must activate PPT TA if it has not yet been done.
				FlexoController ctrl = getController();
				PowerpointTechnologyAdapter pptTa = ctrl.getTechnologyAdapter(PowerpointTechnologyAdapter.class);
				if (pptTa !=null & !pptTa.isActivated()){
					pptTa.activate();
				}
				Wizard wizard = new CreateFreeModelDiagramFromPPTWizard(action, ctrl);
				WizardDialog dialog = new WizardDialog(wizard, getController());
				dialog.showDialog();
				if (dialog.getStatus() != Status.VALIDATED) {
					// Operation cancelled
					return false;
				}
				return true;
			}
		};
	}

	@Override
	protected FlexoActionFinalizer<CreateFreeModelDiagramFromPPT> getDefaultFinalizer() {
		return new FlexoActionFinalizer<CreateFreeModelDiagramFromPPT>() {
			@Override
			public boolean run(EventObject e, CreateFreeModelDiagramFromPPT action) {
				getController().selectAndFocusObject(action.getFreeModel());
				return true;
			}
		};
	}

	@Override
	protected Icon getEnabledIcon() {
		return DiagramIconLibrary.DIAGRAM_ICON;
	}

}
