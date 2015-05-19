/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Openflexo-module-archetype, a component of the software infrastructure 
 * developed at Openflexo.
 * 
 * 
 * Openflexo is dual-licensed under the European Union Public License (EUPL, either 
 * version 1.1 of the License, or any later version ), which is available at 
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 * and the GNU General Public License (GPL, either version 3 of the License, or any 
 * later version), which is available at http://www.gnu.org/licenses/gpl.html .
 * 
 * You can redistribute it and/or modify under the terms of either of these licenses
 * 
 * If you choose to redistribute it and/or modify under the terms of the GNU GPL, you
 * must include the following additional permission.
 *
 *          Additional permission under GNU GPL version 3 section 7
 *
 *          If you modify this Program, or any covered work, by linking or 
 *          combining it with software containing parts covered by the terms 
 *          of EPL 1.0, the licensors of this Program grant you additional permission
 *          to convey the resulting work. * 
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. 
 *
 * See http://www.openflexo.org/license.html for details.
 * 
 * 
 * Please contact Openflexo (openflexo-contacts@openflexo.org)
 * or visit www.openflexo.org if you need additional information.
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
