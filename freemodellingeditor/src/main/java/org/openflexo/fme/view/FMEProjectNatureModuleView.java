/*
 * (c) Copyright 2013- Openflexo
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

package org.openflexo.fme.view;

import org.openflexo.fme.model.FreeModellingProjectNature;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.view.FIBModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

/**
 * Main view for FreeModellingEditor
 */
@SuppressWarnings("serial")
public class FMEProjectNatureModuleView extends FIBModuleView<FreeModellingProjectNature> {

	public static Resource FME_PROJECT_MODULE_VIEW_FIB = ResourceLocator.locateResource("Fib/FMEProjectNaturePanel.fib");

	private final FlexoPerspective perspective;

	public FMEProjectNatureModuleView(FreeModellingProjectNature nature, FlexoController controller, FlexoPerspective perspective) {
		super(nature, controller, FME_PROJECT_MODULE_VIEW_FIB, controller.getModule().getLocales());
		this.perspective = perspective;
	}

	@Override
	public FlexoPerspective getPerspective() {
		return perspective;
	}
}
