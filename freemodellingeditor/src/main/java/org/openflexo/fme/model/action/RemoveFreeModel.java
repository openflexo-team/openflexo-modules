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
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.fmlrt.action.DeleteVirtualModelInstance;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.technologyadapter.diagram.fml.action.CreateFMLControlledDiagramVirtualModelInstance;

/**
 * This action is used to remove a {@link FreeModel} from a {@link FreeModellingProject}<br>
 * 
 * @author vincent
 * 
 */
public class RemoveFreeModel extends FlexoAction<RemoveFreeModel, FreeModel, FlexoObject> {

	private static final Logger logger = Logger.getLogger(RemoveFreeModel.class.getPackage().getName());

	public static FlexoActionType<RemoveFreeModel, FreeModel, FlexoObject> actionType = new FlexoActionType<RemoveFreeModel, FreeModel, FlexoObject>(
			"remove_free_model", FlexoActionType.defaultGroup, FlexoActionType.DELETE_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public RemoveFreeModel makeNewAction(FreeModel focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
			return new RemoveFreeModel(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(FreeModel object, Vector<FlexoObject> globalSelection) {
			return true;
		}

		@Override
		public boolean isEnabledForSelection(FreeModel object, Vector<FlexoObject> globalSelection) {
			return true;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(RemoveFreeModel.actionType, FreeModel.class);
	}


	RemoveFreeModel(FreeModel focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	protected void doAction(Object context) throws SaveResourceException {

		logger.info("Remove free model action started ...");
		logger.info("Delete virtual model instance");
		getFocusedObject().getFreeModellingProject().removeFreeModel(getFocusedObject());
		DeleteVirtualModelInstance deleteVm = DeleteVirtualModelInstance.actionType.makeNewEmbeddedAction(getFocusedObject().getVirtualModelInstance(), null, this);
		deleteVm.doAction();
		logger.info("Delete free model");
		getFocusedObject().delete();
		if(getFocusedObject().isDeleted()){
			logger.info("Free Model " + getFocusedObject().getName() + " Deleted");
		}else{
			logger.warning("Free Model " + getFocusedObject().getName() + " Not Deleted");
		}
		getFocusedObject().getFreeModellingProject().getPropertyChangeSupport().firePropertyChange("freeModels", null, null);
	}

}
