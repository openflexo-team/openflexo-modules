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

import org.openflexo.fme.model.FreeModel;
import org.openflexo.fme.model.FreeModellingProject;
import org.openflexo.fme.model.FreeModellingProjectNature;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.InvalidArgumentException;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstanceObject;
import org.openflexo.foundation.fml.rt.action.DeletionSchemeAction;
import org.openflexo.foundation.fml.rt.action.DeletionSchemeActionType;
import org.openflexo.foundation.resource.SaveResourceException;

/**
 * This action is used to delete a flexo concept instance within Free modeling editor<br>
 * 
 * @author vincent
 * 
 */
public class DeleteFMEFlexoConceptInstance extends
		FlexoAction<DeleteFMEFlexoConceptInstance, FlexoConceptInstance, VirtualModelInstanceObject> {

	private static final Logger logger = Logger.getLogger(DeleteFMEFlexoConceptInstance.class.getPackage().getName());

	public static FlexoActionType<DeleteFMEFlexoConceptInstance, FlexoConceptInstance, VirtualModelInstanceObject> actionType = new FlexoActionType<DeleteFMEFlexoConceptInstance, FlexoConceptInstance, VirtualModelInstanceObject>(
			"delete_flexo_concept_instance", FlexoActionType.defaultGroup, FlexoActionType.DELETE_ACTION_TYPE) {

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

		try {
			FreeModel freeModel = getFreeModel();
			for (FlexoObject selection : getGlobalSelection()) {
				if (selection instanceof FlexoConceptInstance) {
					VirtualModelInstance vmi = ((FlexoConceptInstance) selection).getVirtualModelInstance();
					DeletionSchemeActionType deletionSchemeActionType = new DeletionSchemeActionType(((FlexoConceptInstance) selection)
							.getFlexoConcept().getDefaultDeletionScheme(), (FlexoConceptInstance) selection);
					DeletionSchemeAction deletionSchemeAction = deletionSchemeActionType.makeNewEmbeddedAction(
							(FlexoConceptInstance) selection, getGlobalSelection(), this);
					deletionSchemeAction.setVirtualModelInstance(vmi);
					deletionSchemeAction.doAction();
					vmi.removeFromFlexoConceptInstances(((FlexoConceptInstance) selection));
					selection.delete(context);
					// This is used to notify the deletion of an instance of a flexo concept
					freeModel.getPropertyChangeSupport().firePropertyChange("getInstances(FlexoConcept)", selection, null);
				}
			}

			// TODO Delete concept if no instances??

		} catch (InvalidArgumentException e) {
			e.printStackTrace();
		}

	}

	public FreeModellingProjectNature getFreeModellingProjectNature() {
		return getServiceManager().getProjectNatureService().getProjectNature(FreeModellingProjectNature.class);
	}

	public FreeModellingProject getFreeModellingProject() {
		return getFreeModellingProjectNature().getFreeModellingProject(getEditor().getProject());
	}

	public FreeModel getFreeModel() throws InvalidArgumentException {
		return getFreeModellingProject().getFreeModel(getFocusedObject().getVirtualModelInstance());
	}
}
