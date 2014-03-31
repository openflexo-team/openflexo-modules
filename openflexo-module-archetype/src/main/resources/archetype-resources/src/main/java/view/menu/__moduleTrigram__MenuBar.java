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

import org.openflexo.module.Module;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.menu.EditMenu;
import org.openflexo.view.menu.FlexoMenuBar;
import org.openflexo.view.menu.ToolsMenu;
import org.openflexo.view.menu.WindowMenu;
import org.openflexo.module.${moduleName}.controller.${moduleTrigram}Controller;
import org.openflexo.module.${moduleName}.${moduleName};

/**
 * Class representing menus related to ${moduleName} window
 * 
 * @author ${author}
 */
public class ${moduleTrigram}MenuBar extends FlexoMenuBar {

	private ${moduleTrigram}FileMenu _fileMenu;

	public ${moduleTrigram}MenuBar(${moduleTrigram}Controller controller) {
		super(controller, ${moduleName}.INSTANCE);
	}

	// TODO Documenter la liste des menus qu'il est nécessaire d'avoir?
	
	/**
	 * Build if required and return ${moduleTrigam} 'File' menu. This method overrides the default one defined on superclass
	 * 
	 * @param controller
	 * @return a FileMenu instance
	 */
	@Override
	public ${moduleTrigram}FileMenu getFileMenu(FlexoController controller) {
		if (_fileMenu == null) {
			_fileMenu = new ${moduleTrigram}FileMenu((${moduleTrigram}Controller) controller);
		}
		return _fileMenu;
	}


}
