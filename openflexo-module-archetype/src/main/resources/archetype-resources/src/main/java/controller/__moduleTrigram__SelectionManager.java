/*
 * (c) Copyright 2014- Openflexo
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

package org.openflexo.module.${moduleName}.controller;

import org.openflexo.foundation.FlexoObject;
import org.openflexo.selection.ContextualMenuManager;
import org.openflexo.selection.MouseSelectionManager;
import org.openflexo.view.menu.FlexoMenuBar;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ${moduleTrigram}SelectionManager extends MouseSelectionManager {

	protected static final Logger logger = Logger.getLogger(${moduleTrigram}SelectionManager.class.getPackage().getName());

	public ${moduleTrigram}SelectionManager(${moduleTrigram}Controller controller) {
		super(controller);
		FlexoMenuBar menuBar = controller.getMenuBar();
		_contextualMenuManager = new ContextualMenuManager(this, controller);
	}

	public ${moduleTrigram}Controller get${moduleTrigram}Controller() {
		return (${moduleTrigram}Controller) getController();
	}

	/**
	 * Returns the root object that can be currently edited
	 * 
	 * @return FlexoModelObject
	 */
	@Override
	public FlexoObject getRootFocusedObject() {
		return get${moduleTrigram}Controller().getCurrentDisplayedObjectAsModuleView();
	}

}