/*
 * (c) Copyright 2010-2011 AgileBirds
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openflexo.fme.controller.action;

import java.util.EventObject;
import java.util.logging.Logger;

import javax.swing.Icon;

import org.openflexo.components.wizard.Wizard;
import org.openflexo.components.wizard.WizardDialog;
import org.openflexo.fib.controller.FIBController.Status;
import org.openflexo.fme.model.FreeMetaModel;
import org.openflexo.fme.model.action.CreateFreeModelDiagram;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.action.FlexoActionFinalizer;
import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;

public class CreateFreeModelDiagramInitializer extends ActionInitializer<CreateFreeModelDiagram, FreeMetaModel, FlexoObject> {

	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	CreateFreeModelDiagramInitializer(FMEControllerActionInitializer actionInitializer) {
		super(CreateFreeModelDiagram.actionType, actionInitializer);
	}

	@Override
	protected FMEControllerActionInitializer getControllerActionInitializer() {
		return (FMEControllerActionInitializer) super.getControllerActionInitializer();
	}

	@Override
	protected FlexoActionInitializer<CreateFreeModelDiagram> getDefaultInitializer() {
		return new FlexoActionInitializer<CreateFreeModelDiagram>() {
			@Override
			public boolean run(EventObject e, CreateFreeModelDiagram action) {
				Wizard wizard = new CreateFreeModelDiagramWizard(action, getController());
				WizardDialog dialog = new WizardDialog(wizard);
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
	protected FlexoActionFinalizer<CreateFreeModelDiagram> getDefaultFinalizer() {
		return new FlexoActionFinalizer<CreateFreeModelDiagram>() {
			@Override
			public boolean run(EventObject e, CreateFreeModelDiagram action) {
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
