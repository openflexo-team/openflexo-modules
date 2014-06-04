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

package org.openflexo.module.${moduleName}.view.menu;


import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import org.openflexo.FlexoCst;
import org.openflexo.icon.IconLibrary;
import org.openflexo.view.menu.FileMenu;
import org.openflexo.view.menu.FlexoMenuItem;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import org.openflexo.module.${moduleName}.controller.${moduleTrigram}Controller;
import org.openflexo.module.${moduleName}.controller.action.ImportAction;


/**
 * 'File' menu for this Module
 * 
 * @author ${author}
 */

public class ${moduleTrigram}FileMenu extends FileMenu {

	private static final Logger logger = Logger.getLogger(${moduleTrigram}FileMenu.class.getPackage().getName());

	public ${moduleTrigram}FileMenu(${moduleTrigram}Controller controller) {
		super(controller);
	}

	public ${moduleTrigram}Controller get${moduleTrigram}Controller() {
		return (${moduleTrigram}Controller) getController();
	}

	@Override
	public void addSpecificItems() {
		add(new SaveModifiedItem());
		addSeparator();
		add(new ImportItem());
		addSeparator();
	}

	@Override
	public void quit() {
		if (get${moduleTrigram}Controller().getApplicationContext().getResourceManager().getUnsavedResources().size() == 0) {
			super.quit();
		} else if (get${moduleTrigram}Controller().reviewModifiedResources()) {
			super.quit();
		}

	}

	public class ImportItem extends FlexoMenuItem {
		public ImportItem() {
			super(new ImportAction(), "Import", KeyStroke.getKeyStroke(KeyEvent.VK_I, FlexoCst.META_MASK), getController(), true);
			setIcon(IconLibrary.SAVE_ICON);
		}
	}
	
	public class SaveModifiedItem extends FlexoMenuItem {
		public SaveModifiedItem() {
			super(new SaveModifiedAction(), "save", KeyStroke.getKeyStroke(KeyEvent.VK_S, FlexoCst.META_MASK), getController(), true);
			setIcon(IconLibrary.SAVE_ICON);
		}
	}

	public class SaveModifiedAction extends AbstractAction {
		public SaveModifiedAction() {
			super();
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			get${moduleTrigram}Controller().reviewModifiedResources();
		}
	}

}
