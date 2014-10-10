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
package org.openflexo.ve.controller.action;

import java.util.EventObject;
import java.util.logging.Logger;

import javax.swing.Icon;

import org.openflexo.components.widget.CommonFIB;
import org.openflexo.components.wizard.Wizard;
import org.openflexo.components.wizard.WizardDialog;
import org.openflexo.fib.controller.FIBController.Status;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.action.FlexoActionFinalizer;
import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.foundation.action.FlexoExceptionHandler;
import org.openflexo.foundation.action.NotImplementedException;
import org.openflexo.foundation.view.View;
import org.openflexo.foundation.view.action.CreateBasicVirtualModelInstance;
import org.openflexo.icon.VEIconLibrary;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;

public class CreateBasicVirtualModelInstanceInitializer extends ActionInitializer<CreateBasicVirtualModelInstance, View, FlexoObject> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	CreateBasicVirtualModelInstanceInitializer(VEControllerActionInitializer actionInitializer) {
		super(CreateBasicVirtualModelInstance.actionType, actionInitializer);
	}

	@Override
	protected VEControllerActionInitializer getControllerActionInitializer() {
		return (VEControllerActionInitializer) super.getControllerActionInitializer();
	}

	private Status chooseAndConfigureCreationScheme(CreateBasicVirtualModelInstance action) {
		return instanciateShowDialogAndReturnStatus(action, CommonFIB.CHOOSE_AND_CONFIGURE_CREATION_SCHEME_DIALOG_FIB);
	}

	/*private Status chooseVirtualModel(CreateVirtualModelInstance<?> action) {
		return instanciateShowDialogAndReturnStatus(action, CommonFIB.CREATE_VIRTUAL_MODEL_INSTANCE_DIALOG_FIB);
	}*/

	@Override
	protected FlexoActionInitializer<CreateBasicVirtualModelInstance> getDefaultInitializer() {
		return new FlexoActionInitializer<CreateBasicVirtualModelInstance>() {
			@Override
			public boolean run(EventObject e, CreateBasicVirtualModelInstance action) {
				if (action.skipChoosePopup) {
					return true;
				} else {

					Wizard wizard = new CreateBasicVirtualModelInstanceWizard(action, getController());
					WizardDialog dialog = new WizardDialog(wizard);
					dialog.showDialog();

					if (dialog.getStatus() != Status.VALIDATED) {
						// Operation cancelled
						return false;
					}

					return true;

					/*System.out.println("returned = " + dialog.getStatus());

					int step = 0;
					boolean shouldContinue = true;
					while (shouldContinue) {
						Status result;
						if (step == 0) {
							result = instanciateShowDialogAndReturnStatus(action, CommonFIB.CREATE_VIRTUAL_MODEL_INSTANCE_DIALOG_FIB);
						} else if (step == action.getStepsNumber() - 1 && action.getVirtualModel() != null
								&& action.getVirtualModel().hasCreationScheme()) {
							result = chooseAndConfigureCreationScheme(action);
						} else {
							ModelSlot<?> configuredModelSlot = action.getVirtualModel().getModelSlots().get(step - 1);
							result = instanciateShowDialogAndReturnStatus(action.getModelSlotInstanceConfiguration(configuredModelSlot),
									getModelSlotInstanceConfigurationFIB(configuredModelSlot.getClass()));
						}
						if (result == Status.CANCELED) {
							return false;
						} else if (result == Status.VALIDATED) {
							return true;
						} else if (result == Status.NEXT && step + 1 <= action.getStepsNumber()) {
							step = step + 1;
						} else if (result == Status.BACK && step - 1 >= 0) {
							step = step - 1;
						}
					}*/

					// return instanciateAndShowDialog(action, CommonFIB.CREATE_VIRTUAL_MODEL_INSTANCE_DIALOG_FIB);
				}

			}
		};
	}

	@Override
	protected FlexoActionFinalizer<CreateBasicVirtualModelInstance> getDefaultFinalizer() {
		return new FlexoActionFinalizer<CreateBasicVirtualModelInstance>() {
			@Override
			public boolean run(EventObject e, CreateBasicVirtualModelInstance action) {
				// getController().setCurrentEditedObjectAsModuleView(action.getNewVirtualModelInstance());
				getController().selectAndFocusObject(action.getNewVirtualModelInstance());
				return true;
			}
		};
	}

	@Override
	protected FlexoExceptionHandler<CreateBasicVirtualModelInstance> getDefaultExceptionHandler() {
		return new FlexoExceptionHandler<CreateBasicVirtualModelInstance>() {
			@Override
			public boolean handleException(FlexoException exception, CreateBasicVirtualModelInstance action) {
				if (exception instanceof NotImplementedException) {
					FlexoController.notify(FlexoLocalization.localizedForKey("not_implemented_yet"));
					return true;
				}
				return false;
			}
		};
	}

	@Override
	protected Icon getEnabledIcon() {
		return VEIconLibrary.VIRTUAL_MODEL_INSTANCE_ICON;
	}

	/**
	 * @author Vincent This method has to be removed as soon as we will have a real Wizard Management. Its purpose is to handle the
	 *         separation of FIBs for Model Slot Configurations.
	 * @return File that correspond to the FIB
	 */
	/*private Resource getModelSlotInstanceConfigurationFIB(Class<? extends ModelSlot> modelSlotClass) {
		if (TypeAwareModelSlot.class.isAssignableFrom(modelSlotClass)) {
			return CommonFIB.CONFIGURE_TYPE_AWARE_MODEL_SLOT_INSTANCE_DIALOG_FIB;
		}
		if (FreeModelSlot.class.isAssignableFrom(modelSlotClass)) {
			return CommonFIB.CONFIGURE_FREE_MODEL_SLOT_INSTANCE_DIALOG_FIB;
		}
		if (VirtualModelModelSlot.class.isAssignableFrom(modelSlotClass)) {
			return CommonFIB.CONFIGURE_VIRTUAL_MODEL_SLOT_INSTANCE_DIALOG_FIB;
		}
		return null;
	}*/
}
