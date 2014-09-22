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

package org.openflexo.module.cartoeditor.view.menu;

import org.openflexo.module.cartoeditor.CartoEditor;
import org.openflexo.module.cartoeditor.controller.CEController;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.menu.FlexoMenuBar;

/**
 * Class representing menus related to CartoEditor window
 *
 * @author Somebody
 */
public class CEMenuBar extends FlexoMenuBar {

	private CEFileMenu _fileMenu;

	public CEMenuBar(CEController controller) {
		super(controller, CartoEditor.INSTANCE);
	}

	// TODO Documenter la liste des menus qu'il est nécessaire d'avoir?

	/**
	 * Build if required and return ${moduleTrigam} 'File' menu. This method overrides the default one defined on superclass
	 *
	 * @param controller
	 * @return a FileMenu instance
	 */
	@Override
	public CEFileMenu getFileMenu(FlexoController controller) {
		if (_fileMenu == null) {
			_fileMenu = new CEFileMenu((CEController) controller);
		}
		return _fileMenu;
	}

}
