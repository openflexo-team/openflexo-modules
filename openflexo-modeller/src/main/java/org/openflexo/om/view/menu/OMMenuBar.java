/**
 * 
 * Copyright (c) 2017, Openflexo
 * 
 * This file is part of Flexovieweditor, a component of the software infrastructure 
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

package org.openflexo.om.view.menu;

import org.openflexo.module.Module;
import org.openflexo.om.OpenflexoModeller;
import org.openflexo.om.controller.OMController;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.menu.EditMenu;
import org.openflexo.view.menu.FileMenu;
import org.openflexo.view.menu.FlexoMenuBar;
import org.openflexo.view.menu.ToolsMenu;
import org.openflexo.view.menu.WindowMenu;

/**
 * Class representing menus related to {@link OpenflexoModeller} module
 * 
 * @author sylvain
 */
@SuppressWarnings("serial")
public class OMMenuBar extends FlexoMenuBar {

	private OMFileMenu fileMenu;
	private OMEditMenu editMenu;
	private OMToolsMenu toolsMenu;

	public OMMenuBar(OMController controller) {
		super(controller, controller.getApplicationContext().getModuleLoader().getModule(OpenflexoModeller.class));
	}

	/**
	 * Build if required and return 'File' menu.
	 * 
	 * @param controller
	 * @return
	 */
	@Override
	public FileMenu getFileMenu(FlexoController controller) {
		if (fileMenu == null) {
			fileMenu = new OMFileMenu((OMController) controller);
		}
		return fileMenu;
	}

	/**
	 * Build if required and return 'Edit' menu.
	 * 
	 * @param controller
	 * @return
	 */
	@Override
	public EditMenu getEditMenu(FlexoController controller) {
		if (editMenu == null) {
			editMenu = new OMEditMenu((OMController) controller);
		}
		return editMenu;
	}

	/**
	 * Build if required and return 'Window' menu.
	 * 
	 * @param controller
	 * @return
	 */
	@Override
	public WindowMenu getWindowMenu(FlexoController controller, Module<?> module) {
		if (windowMenu == null) {
			windowMenu = new OMWindowMenu((OMController) controller);
		}
		return windowMenu;
	}

	/**
	 * Build if required and return 'Tools' menu.
	 * 
	 * @param controller
	 * @return
	 */
	@Override
	public ToolsMenu getToolsMenu(FlexoController controller) {
		if (toolsMenu == null) {
			toolsMenu = new OMToolsMenu((OMController) controller);
		}
		return toolsMenu;
	}

}
