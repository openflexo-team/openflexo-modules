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


package org.openflexo.module.sample.view.menu;

import org.openflexo.module.Module;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.menu.EditMenu;
import org.openflexo.view.menu.FlexoMenuBar;
import org.openflexo.view.menu.ToolsMenu;
import org.openflexo.view.menu.WindowMenu;
import org.openflexo.module.sample.controller.SMPController;
import org.openflexo.module.sample.sample;

/**
 * Class representing menus related to sample window
 * 
 * @author ALain Le Lapin
 */
public class SMPMenuBar extends FlexoMenuBar {

	private SMPFileMenu _fileMenu;

	public SMPMenuBar(SMPController controller) {
		super(controller, sample.INSTANCE);
	}

	// TODO Documenter la liste des menus qu'il est nécessaire d'avoir?
	
	/**
	 * Build if required and return ${moduleTrigam} 'File' menu. This method overrides the default one defined on superclass
	 * 
	 * @param controller
	 * @return a FileMenu instance
	 */
	@Override
	public SMPFileMenu getFileMenu(FlexoController controller) {
		if (_fileMenu == null) {
			_fileMenu = new SMPFileMenu((SMPController) controller);
		}
		return _fileMenu;
	}


}
