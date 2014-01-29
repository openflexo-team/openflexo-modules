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
package org.openflexo.fme.view.menu;

/*
 * Created on <date> by <yourname>
 *
 * Flexo Application Suite
 * (c) Denali 2003-2006
 */
import org.openflexo.fme.FreeModellingEditor;
import org.openflexo.fme.controller.FMEController;
import org.openflexo.module.Module;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.menu.EditMenu;
import org.openflexo.view.menu.FlexoMenuBar;
import org.openflexo.view.menu.ToolsMenu;
import org.openflexo.view.menu.WindowMenu;

/**
 * Class representing menus related to ViewPointModeller window
 * 
 * @author sylvain
 */
public class FMEMenuBar extends FlexoMenuBar {

	private FMEFileMenu _fileMenu;
	private FMEEditMenu _editMenu;
	private FMEToolsMenu _toolsMenu;

	public FMEMenuBar(FMEController controller) {
		super(controller, FreeModellingEditor.INSTANCE);
	}

	/**
	 * Build if required and return VPM 'File' menu. This method overrides the default one defined on superclass
	 * 
	 * @param controller
	 * @return a FileMenu instance
	 */
	@Override
	public FMEFileMenu getFileMenu(FlexoController controller) {
		if (_fileMenu == null) {
			_fileMenu = new FMEFileMenu((FMEController) controller);
		}
		return _fileMenu;
	}

	/**
	 * Build if required and return VPM 'Edit' menu. This method overrides the default one defined on superclass
	 * 
	 * @param controller
	 * @return a EditMenu instance
	 */
	@Override
	public EditMenu getEditMenu(FlexoController controller) {
		if (_editMenu == null) {
			_editMenu = new FMEEditMenu((FMEController) controller);
		}
		return _editMenu;
	}

	/**
	 * Build if required and return VPM 'Window' menu. This method overrides the default one defined on superclass
	 * 
	 * @param controller
	 * @return a WindowMenu instance
	 */
	@Override
	public WindowMenu getWindowMenu(FlexoController controller, Module module) {
		if (_windowMenu == null) {
			_windowMenu = new FMEWindowMenu((FMEController) controller);
		}
		return _windowMenu;
	}

	/**
	 * Build if required and return VPM 'Tools' menu. This method overrides the default one defined on superclass
	 * 
	 * @param controller
	 * @return a ToolsMenu instance
	 */
	@Override
	public ToolsMenu getToolsMenu(FlexoController controller) {
		if (_toolsMenu == null) {
			_toolsMenu = new FMEToolsMenu((FMEController) controller);
		}
		return _toolsMenu;
	}

}
