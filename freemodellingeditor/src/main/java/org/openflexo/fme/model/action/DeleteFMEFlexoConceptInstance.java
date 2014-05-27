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
package org.openflexo.fme.model.action;

import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.fme.model.FreeMetaModel;
import org.openflexo.fme.model.FreeModel;
import org.openflexo.fme.model.FreeModellingProject;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.view.FlexoConceptInstance;
import org.openflexo.foundation.view.VirtualModelInstanceObject;
import org.openflexo.foundation.view.action.DeletionSchemeAction;

/**
 * This action is used to create a new {@link FreeModel} in a {@link FreeModellingProject}<br>
 * 
 * New {@link FreeModel} might be created while a new associated {@link FreeMetaModel} is created, or using an existing one.
 * 
 * @author sylvain
 * 
 */
public class DeleteFMEFlexoConceptInstance extends
		FlexoAction<DeleteFMEFlexoConceptInstance, FlexoConceptInstance, VirtualModelInstanceObject> {

	private static final Logger logger = Logger.getLogger(DeleteFMEFlexoConceptInstance.class.getPackage().getName());

	public static FlexoActionType<DeleteFMEFlexoConceptInstance, FlexoConceptInstance, VirtualModelInstanceObject> actionType = new FlexoActionType<DeleteFMEFlexoConceptInstance, FlexoConceptInstance, VirtualModelInstanceObject>(
			"delete_flexo_concept_instance", FlexoActionType.newMenu, FlexoActionType.defaultGroup, FlexoActionType.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public DeleteFMEFlexoConceptInstance makeNewAction(FlexoConceptInstance focusedObject,
				Vector<VirtualModelInstanceObject> globalSelection, FlexoEditor editor) {
			return new DeleteFMEFlexoConceptInstance(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(FlexoConceptInstance object, Vector<VirtualModelInstanceObject> globalSelection) {
			return true;
		}

		@Override
		public boolean isEnabledForSelection(FlexoConceptInstance object, Vector<VirtualModelInstanceObject> globalSelection) {
			return true;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(DeleteFMEFlexoConceptInstance.actionType, FlexoConceptInstance.class);
	}

	DeleteFMEFlexoConceptInstance(FlexoConceptInstance focusedObject, Vector<VirtualModelInstanceObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	protected void doAction(Object context) throws SaveResourceException {

		logger.info("Delete flexo concept instances");

		for (FlexoObject selection : getGlobalSelectionAndFocusedObject()) {
			if (selection instanceof FlexoConceptInstance) {
				DeletionSchemeAction deletionSchemeAction = DeletionSchemeAction.actionType.makeNewEmbeddedAction(
						(FlexoConceptInstance) selection, getGlobalSelection(), this);
				deletionSchemeAction.setVirtualModelInstance(((FlexoConceptInstance) selection).getVirtualModelInstance());
				deletionSchemeAction.setDeletionScheme(((FlexoConceptInstance) selection).getFlexoConcept().getDeletionSchemes().get(0));
				deletionSchemeAction.doAction();
			}
		}

		// TODO Delete concept if no instances??
	}
}
