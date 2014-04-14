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
package org.openflexo.fme.controller;

import java.util.logging.Logger;

import org.openflexo.foundation.FlexoObject;
import org.openflexo.selection.MouseSelectionManager;
import org.openflexo.view.menu.FlexoMenuBar;

/**
 * Selection manager dedicated to this module
 */
public class FMESelectionManager extends MouseSelectionManager {

	protected static final Logger logger = Logger.getLogger(FMESelectionManager.class.getPackage().getName());

	public FMESelectionManager(FMEController controller) {
		super(controller);
		FlexoMenuBar menuBar = controller.getMenuBar();
		/*_clipboard = new FMEClipboard(this, menuBar.getEditMenu(controller).copyItem, menuBar.getEditMenu(controller).pasteItem,
				menuBar.getEditMenu(controller).cutItem);*/
		_contextualMenuManager = new FMEContextualMenuManager(this, controller);
	}

	public FMEController getFMEController() {
		return (FMEController) getController();
	}

	/*@Override
	public boolean performSelectionSelectAll() {
		if (logger.isLoggable(Level.WARNING)) {
			logger.warning("'Select All' not implemented yet in this module");
		}
		return false;
	}*/

	/**
	 * Returns the root object that can be currently edited
	 * 
	 * @return FlexoModelObject
	 */
	@Override
	public FlexoObject getRootFocusedObject() {
		return getFMEController().getCurrentDisplayedObjectAsModuleView();
	}

	/*@Override
	public FlexoObject getPasteContext() {
		// TODO please implement this
		return null;
	}*/

}
