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
import java.util.logging.Logger;

import org.openflexo.fme.controller.FMEController;
import org.openflexo.view.menu.ToolsMenu;

/**
 * 'Tools' menu for FreeModellingEditor module
 * 
 * @author sylvain
 */
public class FMEToolsMenu extends ToolsMenu {

	private static final Logger logger = Logger.getLogger(FMEToolsMenu.class.getPackage().getName());

	protected FMEController fmeController;

	public FMEToolsMenu(FMEController controller) {
		super(controller);
		fmeController = controller;
		// Put your actions here
	}

	public FMEController getFMEController() {
		return fmeController;
	}

}
