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

package org.openflexo.xxxmodule.view;

import org.openflexo.foundation.FlexoProject;
import org.openflexo.gina.model.FIBComponent;
import org.openflexo.gina.view.GinaViewFactory;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.view.FIBModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.FlexoFIBController;
import org.openflexo.view.controller.model.FlexoPerspective;
import org.openflexo.xxxmodule.model.action.GivesXXXNature;

/**
 * Convert view for XXX module
 */
@SuppressWarnings("serial")
public class ConvertToXXXProjectView extends FIBModuleView<FlexoProject<?>> {

	public static Resource CONVERT_TO_FME_PROJECT_VIEW_FIB = ResourceLocator.locateResource("Fib/ConvertToXXXProjectPanel.fib");

	private final FlexoPerspective perspective;

	public ConvertToXXXProjectView(FlexoProject<?> project, FlexoController controller, FlexoPerspective perspective) {
		super(project, controller, CONVERT_TO_FME_PROJECT_VIEW_FIB, controller.getModule().getLocales());
		this.perspective = perspective;
	}

	@Override
	public FlexoPerspective getPerspective() {
		return perspective;
	}

	public static class ConvertToXXXProjectViewFIBController extends FlexoFIBController {
		public ConvertToXXXProjectViewFIBController(FIBComponent component, GinaViewFactory<?> viewFactory) {
			super(component, viewFactory);
		}

		public ConvertToXXXProjectViewFIBController(FIBComponent component, GinaViewFactory<?> viewFactory, FlexoController controller) {
			super(component, viewFactory, controller);
		}

		public void givesXXXNature(FlexoProject<?> project) {
			GivesXXXNature action = GivesXXXNature.actionType.makeNewAction(project, null, getEditor());
			action.doAction();
		}

	}
}
