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

import org.openflexo.fme.model.action.DeleteFMEFlexoConceptInstance;
import org.openflexo.foundation.action.FlexoActionFinalizer;
import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstanceObject;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;

public class DeleteFMEFlexoConceptInstanceActionInitializer extends
		ActionInitializer<DeleteFMEFlexoConceptInstance, FlexoConceptInstance, VirtualModelInstanceObject> {

	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	DeleteFMEFlexoConceptInstanceActionInitializer(FMEControllerActionInitializer actionInitializer) {
		super(DeleteFMEFlexoConceptInstance.actionType, actionInitializer);
	}

	@Override
	protected FlexoActionInitializer<DeleteFMEFlexoConceptInstance> getDefaultInitializer() {
		return new FlexoActionInitializer<DeleteFMEFlexoConceptInstance>() {
			@Override
			public boolean run(EventObject e, DeleteFMEFlexoConceptInstance action) {
				getController().getSelectionManager().resetSelection();
				return true;
			}
		};
	}

	@Override
	protected FlexoActionFinalizer<DeleteFMEFlexoConceptInstance> getDefaultFinalizer() {
		return new FlexoActionFinalizer<DeleteFMEFlexoConceptInstance>() {
			@Override
			public boolean run(EventObject e, DeleteFMEFlexoConceptInstance action) {
				if (getControllerActionInitializer().getController().getSelectionManager().getLastSelectedObject() != null
						&& getControllerActionInitializer().getController().getSelectionManager().getLastSelectedObject().isDeleted()) {
					getControllerActionInitializer().getController().getSelectionManager().resetSelection();
				}
				return true;
			}
		};
	}

}
