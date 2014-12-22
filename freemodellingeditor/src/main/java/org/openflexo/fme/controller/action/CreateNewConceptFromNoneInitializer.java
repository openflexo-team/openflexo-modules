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
import org.openflexo.fme.model.action.CreateNewConceptFromNoneConcept;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.action.FlexoActionFinalizer;
import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.foundation.fmlrt.FlexoConceptInstance;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;

public class CreateNewConceptFromNoneInitializer extends
		ActionInitializer<CreateNewConceptFromNoneConcept, FlexoConceptInstance, FlexoObject> {

	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	public CreateNewConceptFromNoneInitializer(ControllerActionInitializer actionInitializer) {
		super(CreateNewConceptFromNoneConcept.actionType, actionInitializer);
	}

	@Override
	protected FlexoActionInitializer<CreateNewConceptFromNoneConcept> getDefaultInitializer() {
		return new FlexoActionInitializer<CreateNewConceptFromNoneConcept>() {
			@Override
			public boolean run(EventObject e, CreateNewConceptFromNoneConcept action) {
				logger.info("CreateNewConceptFromNoneConcept initializer");
				Wizard wizard = new CreateNewConceptFromNoneConceptWizard(action, getController());
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
	protected FlexoActionFinalizer<CreateNewConceptFromNoneConcept> getDefaultFinalizer() {
		return new FlexoActionFinalizer<CreateNewConceptFromNoneConcept>() {
			@Override
			public boolean run(EventObject e, CreateNewConceptFromNoneConcept action) {
				logger.info("CreateNewConceptFromNoneConcept finalizer");
				getController().selectAndFocusObject(action.getFocusedObject());
				return true;
			}
		};
	}

}
