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

package org.openflexo.module.cartoeditor.controller;

import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.viewpoint.VirtualModelTechnologyAdapter;
import org.openflexo.module.FlexoModule;
import org.openflexo.module.cartoeditor.view.menu.CEMenuBar;
import org.openflexo.selection.MouseSelectionManager;
import org.openflexo.view.FlexoMainPane;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.TechnologyAdapterController;
import org.openflexo.view.menu.FlexoMenuBar;

/**
 * Cartography Editor specific FlexoController.
 */
public class CEController extends FlexoController {

	public CEController(FlexoModule module) {
		super(module);
	}

	@Override
	protected void initializePerspectives() {
		this.addToPerspectives(new CEPerspective(this));

		for (TechnologyAdapter ta : getApplicationContext().getTechnologyAdapterService().getTechnologyAdapters()) {
			if (!(ta instanceof VirtualModelTechnologyAdapter)) {
				TechnologyAdapterController<?> tac = getApplicationContext().getTechnologyAdapterControllerService()
						.getTechnologyAdapterController(ta);
				if (tac != null) {
					tac.installTechnologyPerspective(this);
				}
			}
		}
	}

	@Override
	protected MouseSelectionManager createSelectionManager() {
		return new CESelectionManager(this);
	}

	@Override
	protected FlexoMenuBar createNewMenuBar() {
		return new CEMenuBar(this);
	}

	@Override
	public FlexoObject getDefaultObjectToSelect(FlexoProject project) {
		return project;
	}

	@Override
	protected FlexoMainPane createMainPane() {
		return new FlexoMainPane(this);
	}

}
