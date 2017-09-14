/**
 * 
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2011-2012, AgileBirds
 * 
 * This file is part of Flexoviewpointmodeler, a component of the software infrastructure 
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

package org.openflexo.vpm.view.menu;

/*
 * Created on <date> by <yourname>
 *
 * Flexo Application Suite
 * (c) Denali 2003-2006
 */
import org.openflexo.module.Module;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.menu.EditMenu;
import org.openflexo.view.menu.FlexoMenuBar;
import org.openflexo.view.menu.ToolsMenu;
import org.openflexo.view.menu.WindowMenu;
import org.openflexo.vpm.ViewPointModeller;
import org.openflexo.vpm.controller.VPMController;

/**
 * Class representing menus related to ViewPointModeller window
 * 
 * @author sylvain
 */
public class VPMMenuBar extends FlexoMenuBar {

	private VPMFileMenu _fileMenu;
	private VPMEditMenu _editMenu;
	private VPMToolsMenu _toolsMenu;

	public VPMMenuBar(VPMController controller) {
		super(controller, ViewPointModeller.INSTANCE);
	}

	/**
	 * Build if required and return VPM 'File' menu. This method overrides the default one defined on superclass
	 * 
	 * @param controller
	 * @return a FileMenu instance
	 */
	@Override
	public VPMFileMenu getFileMenu(FlexoController controller) {
		if (_fileMenu == null) {
			_fileMenu = new VPMFileMenu((VPMController) controller);
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
			_editMenu = new VPMEditMenu((VPMController) controller);
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
		if (windowMenu == null) {
			windowMenu = new VPMWindowMenu((VPMController) controller);
		}
		return windowMenu;
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
			_toolsMenu = new VPMToolsMenu((VPMController) controller);
		}
		return _toolsMenu;
	}

}
