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

package org.openflexo.module.cartoeditor.controller;

import java.util.logging.Logger;

import org.openflexo.foundation.FlexoObject;
import org.openflexo.selection.ContextualMenuManager;
import org.openflexo.selection.MouseSelectionManager;
import org.openflexo.view.menu.FlexoMenuBar;

/**
 * Cartography Editor specific MouseSelectionManager Implementation.
 */
public class CESelectionManager extends MouseSelectionManager {

	protected static final Logger LOGGER = Logger.getLogger(CESelectionManager.class.getPackage().getName());

	public CESelectionManager(CEController controller) {
		super(controller);
		FlexoMenuBar menuBar = controller.getMenuBar();
		_contextualMenuManager = new ContextualMenuManager(this, controller);
	}

	public CEController getCEController() {
		return (CEController) getController();
	}

	/**
	 * Returns the root object that can be currently edited
	 *
	 * @return FlexoModelObject
	 */
	@Override
	public FlexoObject getRootFocusedObject() {
		return getCEController().getCurrentDisplayedObjectAsModuleView();
	}

}
