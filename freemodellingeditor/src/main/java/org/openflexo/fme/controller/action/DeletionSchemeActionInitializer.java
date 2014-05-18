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

import org.openflexo.foundation.action.FlexoActionFinalizer;
import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.foundation.view.FlexoConceptInstance;
import org.openflexo.foundation.view.VirtualModelInstanceObject;
import org.openflexo.foundation.view.action.DeletionSchemeAction;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;

public class DeletionSchemeActionInitializer extends
		ActionInitializer<DeletionSchemeAction, FlexoConceptInstance, VirtualModelInstanceObject> {

	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	DeletionSchemeActionInitializer(FMEControllerActionInitializer actionInitializer) {
		super(DeletionSchemeAction.actionType, actionInitializer);
	}

	@Override
	protected FlexoActionInitializer<DeletionSchemeAction> getDefaultInitializer() {
		return new FlexoActionInitializer<DeletionSchemeAction>() {
			@Override
			public boolean run(EventObject e, DeletionSchemeAction action) {
				action.setDeletionScheme(action.getFocusedObject().getFlexoConcept().getDeletionSchemes().get(0));
				return true;
			}
		};
	}

	@Override
	protected FlexoActionFinalizer<DeletionSchemeAction> getDefaultFinalizer() {
		return new FlexoActionFinalizer<DeletionSchemeAction>() {
			@Override
			public boolean run(EventObject e, DeletionSchemeAction action) {
				getController().selectAndFocusObject(action.getFocusedObject());
				return true;
			}
		};
	}

}
