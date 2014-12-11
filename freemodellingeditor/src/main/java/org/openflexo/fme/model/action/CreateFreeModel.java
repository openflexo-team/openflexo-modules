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
import org.openflexo.foundation.action.FlexoActionType;

/**
 * This action is used to create a new {@link FreeModel} in a {@link FreeModellingProject}<br>
 * 
 * New {@link FreeModel} might be created while a new associated {@link FreeMetaModel} is created, or using an existing one.
 * 
 * @author sylvain
 * 
 */
public class CreateFreeModel extends AbstractCreateFreeModel<CreateFreeModel> {

	private static final Logger logger = Logger.getLogger(CreateFreeModel.class.getPackage().getName());

	public static FlexoActionType<CreateFreeModel, FreeModellingProject, FlexoObject> actionType = new FlexoActionType<CreateFreeModel, FreeModellingProject, FlexoObject>(
			"create_free_model", FlexoActionType.newMenu, FlexoActionType.defaultGroup, FlexoActionType.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateFreeModel makeNewAction(FreeModellingProject focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
			return new CreateFreeModel(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(FreeModellingProject object, Vector<FlexoObject> globalSelection) {
			return true;
		}

		@Override
		public boolean isEnabledForSelection(FreeModellingProject object, Vector<FlexoObject> globalSelection) {
			return true;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(CreateFreeModel.actionType, FreeModellingProject.class);
	}

	CreateFreeModel(FreeModellingProject focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

}
