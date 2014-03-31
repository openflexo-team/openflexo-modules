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

package org.openflexo.module.${moduleName}.controller;

import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.module.FlexoModule;
import org.openflexo.selection.SelectionManager;
import org.openflexo.view.FlexoMainPane;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.menu.FlexoMenuBar;
import org.openflexo.module.${moduleName}.${moduleTrigram}Module;
import org.openflexo.module.${moduleName}.controller.${moduleTrigram}SelectionManager;
import org.openflexo.module.${moduleName}.view.menu.${moduleTrigram}MenuBar;

public class ${moduleTrigram}Controller extends FlexoController {

	public ${moduleTrigram}Controller(FlexoModule module) {
		super(module);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initializePerspectives() {
		// TODO Auto-generated method stub

	}

	@Override
	protected SelectionManager createSelectionManager() {
		if (selectionManager == null){
			this.selectionManager = new ${moduleTrigram}SelectionManager(this);
		}
		return selectionManager;
	}

	@Override
	protected FlexoMenuBar createNewMenuBar() {
		return new ${moduleTrigram}MenuBar(this);
	}

	@Override
	public FlexoObject getDefaultObjectToSelect(FlexoProject project) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected FlexoMainPane createMainPane() {
		// TODO Auto-generated method stub
		return new FlexoMainPane(this);
	}

	@Override
	public String getWindowTitleforObject(FlexoObject object) {
		// TODO Auto-generated method stub
		return ${moduleTrigram}Module.${moduleTrigram}_MODULE_NAME;
	}

}
