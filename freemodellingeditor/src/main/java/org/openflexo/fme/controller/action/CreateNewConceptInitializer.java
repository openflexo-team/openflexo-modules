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

import org.openflexo.components.wizard.Wizard;
import org.openflexo.components.wizard.WizardDialog;
import org.openflexo.fib.controller.FIBController.Status;
import org.openflexo.fme.model.FreeMetaModel;
import org.openflexo.fme.model.action.CreateNewConcept;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.action.FlexoActionFinalizer;
import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;

public class CreateNewConceptInitializer extends ActionInitializer<CreateNewConcept, FreeMetaModel, FlexoObject> {

	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	public CreateNewConceptInitializer(ControllerActionInitializer actionInitializer) {
		super(CreateNewConcept.actionType, actionInitializer);
	}

	@Override
	protected FlexoActionInitializer<CreateNewConcept> getDefaultInitializer() {
		return new FlexoActionInitializer<CreateNewConcept>() {
			@Override
			public boolean run(EventObject e, CreateNewConcept action) {
				logger.info("CreateNewConcept initializer");
				Wizard wizard = new CreateNewConceptWizard(action, getController());
				WizardDialog dialog = new WizardDialog(wizard);
				dialog.showDialog();
				if (dialog.getStatus() != Status.VALIDATED) {
					// Operation cancelled
					return false;
				}
				return true;
				// return instanciateAndShowDialog(action, FMECst.CREATE_NEW_CONCEPT_DIALOG_FIB);
			}
		};
	}

	@Override
	protected FlexoActionFinalizer<CreateNewConcept> getDefaultFinalizer() {
		return new FlexoActionFinalizer<CreateNewConcept>() {
			@Override
			public boolean run(EventObject e, CreateNewConcept action) {
				logger.info("CreateNewConcept finalizer");
				getController().selectAndFocusObject(action.getFocusedObject());
				return true;
			}
		};
	}

}
