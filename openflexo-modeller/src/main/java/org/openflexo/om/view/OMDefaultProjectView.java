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

package org.openflexo.om.view;

import java.io.FileNotFoundException;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.action.CreateTopLevelVirtualModel;
import org.openflexo.foundation.fml.rm.CompilationUnitResource;
import org.openflexo.foundation.fml.rt.action.CreateBasicVirtualModelInstance;
import org.openflexo.foundation.fml.rt.rm.FMLRTVirtualModelInstanceResource;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.gina.model.FIBComponent;
import org.openflexo.gina.view.GinaViewFactory;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.view.FIBModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.FlexoFIBController;
import org.openflexo.view.controller.model.FlexoPerspective;

/**
 * Convert view for FreeModellingEditor
 */
@SuppressWarnings("serial")
public class OMDefaultProjectView extends FIBModuleView<FlexoProject<?>> {

	public static Resource DEFAULT_PROJECT_VIEW_FIB = ResourceLocator.locateResource("Fib/DefaultProjectPanel.fib");

	private final FlexoPerspective perspective;

	public OMDefaultProjectView(FlexoProject<?> project, FlexoController controller, FlexoPerspective perspective) {
		super(project, controller, DEFAULT_PROJECT_VIEW_FIB, controller.getModule().getLocales());
		this.perspective = perspective;
	}

	@Override
	public FlexoPerspective getPerspective() {
		return perspective;
	}

	public static class OMDefaultProjectViewFIBController extends FlexoFIBController {

		private CompilationUnitResource selectedVirtualModelResource;
		private FMLRTVirtualModelInstanceResource selectedVirtualModelInstanceResource;

		public OMDefaultProjectViewFIBController(FIBComponent component, GinaViewFactory<?> viewFactory) {
			super(component, viewFactory);
		}

		public OMDefaultProjectViewFIBController(FIBComponent component, GinaViewFactory<?> viewFactory, FlexoController controller) {
			super(component, viewFactory, controller);
		}

		public CompilationUnitResource getSelectedVirtualModelResource() {
			return selectedVirtualModelResource;
		}

		public void setSelectedVirtualModelResource(CompilationUnitResource selectedVirtualModelResource) {
			if ((selectedVirtualModelResource == null && this.selectedVirtualModelResource != null)
					|| (selectedVirtualModelResource != null && !selectedVirtualModelResource.equals(this.selectedVirtualModelResource))) {
				CompilationUnitResource oldValue = this.selectedVirtualModelResource;
				this.selectedVirtualModelResource = selectedVirtualModelResource;
				getPropertyChangeSupport().firePropertyChange("selectedVirtualModelResource", oldValue, selectedVirtualModelResource);
			}
		}

		public FMLRTVirtualModelInstanceResource getSelectedVirtualModelInstanceResource() {
			return selectedVirtualModelInstanceResource;
		}

		public void setSelectedVirtualModelInstanceResource(FMLRTVirtualModelInstanceResource selectedVirtualModelInstanceResource) {
			if ((selectedVirtualModelInstanceResource == null && this.selectedVirtualModelInstanceResource != null)
					|| (selectedVirtualModelInstanceResource != null
							&& !selectedVirtualModelInstanceResource.equals(this.selectedVirtualModelInstanceResource))) {
				FMLRTVirtualModelInstanceResource oldValue = this.selectedVirtualModelInstanceResource;
				this.selectedVirtualModelInstanceResource = selectedVirtualModelInstanceResource;
				getPropertyChangeSupport().firePropertyChange("selectedVirtualModelInstanceResource", oldValue,
						selectedVirtualModelInstanceResource);
			}
		}

		public FlexoProject<?> getFlexoProject() {
			return (FlexoProject<?>) getDataObject();
		}

		public void openVirtualModel() {
			try {
				getFlexoController().selectAndFocusObject(getSelectedVirtualModelResource().getResourceData());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (ResourceLoadingCancelledException e) {
				e.printStackTrace();
			} catch (FlexoException e) {
				e.printStackTrace();
			}
		}

		public void newVirtualModel() {
			CreateTopLevelVirtualModel createVirtualModelAction = CreateTopLevelVirtualModel.actionType
					.makeNewAction(getFlexoProject().getVirtualModelRepository().getRootFolder(), null, getFlexoController().getEditor());
			createVirtualModelAction.doAction();
		}

		public void openVirtualModelInstance() {
			try {
				getFlexoController().selectAndFocusObject(getSelectedVirtualModelInstanceResource().getResourceData());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (ResourceLoadingCancelledException e) {
				e.printStackTrace();
			} catch (FlexoException e) {
				e.printStackTrace();
			}
		}

		public void newVirtualModelInstance() {
			CreateBasicVirtualModelInstance createVirtualModelInstanceAction = CreateBasicVirtualModelInstance.actionType.makeNewAction(
					getFlexoProject().getVirtualModelInstanceRepository().getRootFolder(), null, getFlexoController().getEditor());
			createVirtualModelInstanceAction.doAction();
		}

	}
}
