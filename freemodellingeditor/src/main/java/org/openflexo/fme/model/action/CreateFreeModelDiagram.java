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
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoActionType;

public class CreateFreeModelDiagram extends AbstractCreateFreeModelDiagram<CreateFreeModelDiagram> {

	private static final Logger logger = Logger.getLogger(CreateFreeModelDiagram.class.getPackage().getName());

	public static FlexoActionType<CreateFreeModelDiagram, FreeMetaModel, FlexoObject> actionType = new FlexoActionType<CreateFreeModelDiagram, FreeMetaModel, FlexoObject>(
			"create_diagram", FlexoActionType.newMenu, FlexoActionType.defaultGroup, FlexoActionType.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateFreeModelDiagram makeNewAction(FreeMetaModel focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
			return new CreateFreeModelDiagram(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(FreeMetaModel object, Vector<FlexoObject> globalSelection) {
			return true;
		}

		@Override
		public boolean isEnabledForSelection(FreeMetaModel object, Vector<FlexoObject> globalSelection) {
			return true;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(CreateFreeModelDiagram.actionType, FreeMetaModel.class);
	}

	CreateFreeModelDiagram(FreeMetaModel focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

}
