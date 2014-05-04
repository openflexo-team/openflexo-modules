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

import org.openflexo.fme.FMECst;
import org.openflexo.fme.model.action.DeclareInstanceOfExistingConcept;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.action.FlexoActionFinalizer;
import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.foundation.view.FlexoConceptInstance;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;

public class DeclareInstanceOfExistingConceptInitializer extends
		ActionInitializer<DeclareInstanceOfExistingConcept, FlexoConceptInstance, FlexoObject> {

	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	public DeclareInstanceOfExistingConceptInitializer(ControllerActionInitializer actionInitializer) {
		super(DeclareInstanceOfExistingConcept.actionType, actionInitializer);
	}

	@Override
	protected FlexoActionInitializer<DeclareInstanceOfExistingConcept> getDefaultInitializer() {
		return new FlexoActionInitializer<DeclareInstanceOfExistingConcept>() {
			@Override
			public boolean run(EventObject e, DeclareInstanceOfExistingConcept action) {
				return instanciateAndShowDialog(action, FMECst.DECLARE_INSTANCE_OF_EXISTING_CONCEPT_DIALOG_FIB);
			}
		};
	}

	@Override
	protected FlexoActionFinalizer<DeclareInstanceOfExistingConcept> getDefaultFinalizer() {
		return new FlexoActionFinalizer<DeclareInstanceOfExistingConcept>() {
			@Override
			public boolean run(EventObject e, DeclareInstanceOfExistingConcept action) {
				getController().selectAndFocusObject(action.getFocusedObject());
				return true;
			}
		};
	}

}
