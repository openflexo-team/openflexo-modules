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

package org.openflexo.eamodule.view;

import javax.swing.ImageIcon;

import org.openflexo.eamodule.EAMIconLibrary;
import org.openflexo.eamodule.model.EAProjectNature;
import org.openflexo.eamodule.model.action.CreateBPMNVirtualModelInstance;
import org.openflexo.foundation.nature.VirtualModelInstanceBasedNatureObject;
import org.openflexo.gina.model.FIBComponent;
import org.openflexo.gina.view.GinaViewFactory;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.view.FIBModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.FlexoFIBController;
import org.openflexo.view.controller.model.FlexoPerspective;

/**
 * Main view for FreeModellingEditor
 */
@SuppressWarnings("serial")
public class EAMProjectNatureModuleView extends FIBModuleView<EAProjectNature> {

	public static Resource EAM_PROJECT_MODULE_VIEW_FIB = ResourceLocator.locateResource("Fib/EAMProjectNaturePanel.fib");

	private final FlexoPerspective perspective;

	public EAMProjectNatureModuleView(EAProjectNature nature, FlexoController controller, FlexoPerspective perspective) {
		super(nature, controller, EAM_PROJECT_MODULE_VIEW_FIB, controller.getModule().getLocales());
		this.perspective = perspective;
	}

	@Override
	public FlexoPerspective getPerspective() {
		return perspective;
	}

	public static class EAMProjectNatureModuleViewFIBController extends FlexoFIBController {
		public EAMProjectNatureModuleViewFIBController(FIBComponent component, GinaViewFactory<?> viewFactory) {
			super(component, viewFactory);
		}

		public EAMProjectNatureModuleViewFIBController(FIBComponent component, GinaViewFactory<?> viewFactory, FlexoController controller) {
			super(component, viewFactory, controller);
		}

		public ImageIcon getProjectIcon() {
			return IconFactory.getImageIcon(IconLibrary.OPENFLEXO_NOTEXT_64, EAMIconLibrary.EAM_BIG_MARKER);
		}

		public ImageIcon getBPMNBigIcon() {
			return EAMIconLibrary.BPMN_BIG_ICON;
		}

		public void selectModel(VirtualModelInstanceBasedNatureObject<?> model) {
			System.out.println("selecting " + model);
			getFlexoController().selectAndFocusObject(model);
		}

		public void startBPMNModelling(EAProjectNature nature) {
			CreateBPMNVirtualModelInstance action = CreateBPMNVirtualModelInstance.actionType.makeNewAction(nature, null, getEditor());
			action.doAction();
		}
	}

}
