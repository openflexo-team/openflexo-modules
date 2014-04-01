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
package org.openflexo.ve.controller;

import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.openflexo.FlexoCst;
import org.openflexo.components.widget.FIBProjectResourcesBrowser;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.FlexoProjectObject;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.foundation.view.View;
import org.openflexo.foundation.view.ViewLibrary;
import org.openflexo.foundation.view.VirtualModelInstance;
import org.openflexo.icon.IconLibrary;
import org.openflexo.inspector.FIBInspectorPanel;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.ve.view.ViewModuleView;
import org.openflexo.ve.view.VirtualModelInstanceView;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.TechnologyAdapterController;
import org.openflexo.view.controller.TechnologyAdapterControllerService;
import org.openflexo.view.controller.model.FlexoPerspective;

public class ResourcesPerspective extends FlexoPerspective {

	protected static final Logger logger = Logger.getLogger(ResourcesPerspective.class.getPackage().getName());

	private final VEController _controller;

	private final FIBProjectResourcesBrowser projectResourcesBrowser;

	// private final Map<Diagram, DiagramController> _controllers;

	private final JLabel infoLabel;

	private final FIBInspectorPanel inspectorPanel;

	/**
	 * @param controller
	 *            TODO
	 * @param name
	 */
	public ResourcesPerspective(VEController controller) {
		super("resources_perspective");

		projectResourcesBrowser = new FIBProjectResourcesBrowser(controller.getProject() != null ? controller.getProject() : null,
				controller);

		setTopLeftView(projectResourcesBrowser);

		_controller = controller;

		infoLabel = new JLabel("Resources perspective");
		infoLabel.setFont(FlexoCst.SMALL_FONT);

		// Initialized inspector panel
		inspectorPanel = new FIBInspectorPanel(controller.getModuleInspectorController());
	}

	/*public void focusOnD(Diagram aDiagram) {
		logger.info("focusOnDiagram " + aDiagram);
		// calcBrowser.deleteBrowserListener(_browserView);
		// calcBrowser.setRepresentedObject(viewPoint);
		// calcBrowser.update();
		// calcBrowser.addBrowserListener(_browserView);

		// viewBrowser.setRootObject(viewPoint);
		// setBottomLeftView(viewBrowser);
	}*/

	/**
	 * Overrides getIcon
	 * 
	 * @see org.openflexo.view.controller.model.FlexoPerspective#getActiveIcon()
	 */
	@Override
	public ImageIcon getActiveIcon() {
		return IconLibrary.OPENFLEXO_NOTEXT_16;
	}

	@Override
	public FlexoObject getDefaultObject(FlexoObject proposedObject, FlexoController controller) {
		if (proposedObject instanceof View) {
			return proposedObject;
		}
		if (proposedObject instanceof FlexoProjectObject) {
			return ((FlexoProjectObject) proposedObject).getProject().getViewLibrary();
		} else {
			return null;
		}
	}

	@Override
	public boolean hasModuleViewForObject(FlexoObject object, FlexoController controller) {
		if (object instanceof VirtualModelInstance || object instanceof View) {
			return true;
		}
		if (object instanceof TechnologyObject) {
			return hasModuleViewForTechnologyObject((TechnologyObject<?>) object, controller);
		}
		return false;
	}

	private <TA extends TechnologyAdapter> boolean hasModuleViewForTechnologyObject(TechnologyObject<TA> object, FlexoController controller) {
		TechnologyAdapterControllerService tacService = _controller.getApplicationContext().getTechnologyAdapterControllerService();
		TechnologyAdapterController<TA> tac = tacService.getTechnologyAdapterController(object.getTechnologyAdapter());
		return tac.hasModuleViewForObject(object, controller);
	}

	@Override
	public ModuleView<?> createModuleViewForObject(FlexoObject object, FlexoController controller) {
		/*if (object instanceof Diagram) {
			return getControllerForDiagram((Diagram) object).getModuleView();
		}*/
		if (object instanceof VirtualModelInstance) {
			return new VirtualModelInstanceView((VirtualModelInstance) object, (VEController) controller);
		}
		if (object instanceof View) {
			return new ViewModuleView((View) object, (VEController) controller, this);
		}
		if (object instanceof TechnologyObject) {
			return createModuleViewForTechnologyObject((TechnologyObject<?>) object, controller);
		}
		return null;
	}

	private <TA extends TechnologyAdapter> ModuleView<?> createModuleViewForTechnologyObject(TechnologyObject<TA> object,
			FlexoController controller) {
		TechnologyAdapterControllerService tacService = _controller.getApplicationContext().getTechnologyAdapterControllerService();
		TechnologyAdapterController<TA> tac = tacService.getTechnologyAdapterController(object.getTechnologyAdapter());
		return tac.createModuleViewForObject(object, controller, this);
	}

	/*@Override
	public JComponent getHeader() {
		//if (getCurrentDiagramModuleView() != null) {
		//	return getCurrentDiagramModuleView().getController().getScalePanel();
		//}
		return null;
	}*/

	/*@Override
	public JComponent getFooter() {
		return infoLabel;
	}*/

	/*public DiagramModuleView getCurrentDiagramModuleView() {
		if (_controller != null && _controller.getCurrentModuleView() instanceof DiagramModuleView) {
			return (DiagramModuleView) _controller.getCurrentModuleView();
		}
		return null;
	}*/

	public VirtualModelInstanceView getCurrentVirtualModelInstanceView() {
		if (_controller != null && _controller.getCurrentModuleView() instanceof VirtualModelInstanceView) {
			return (VirtualModelInstanceView) _controller.getCurrentModuleView();
		}
		return null;
	}

	/*public DiagramController getControllerForDiagram(Diagram diagram) {
		DiagramController returned = _controllers.get(diagram);
		if (returned == null) {
			returned = new DiagramController(_controller, diagram, false);
			_controllers.put(diagram, returned);
		}
		return returned;
	}

	public void removeFromControllers(DiagramController shemaController) {
		if (shemaController != null) {
			if (shemaController.getDrawing() != null && shemaController.getDrawing().getDiagram() != null) {
				_controllers.remove(shemaController.getDrawing().getDiagram());
			}
		}
	}*/

	public String getWindowTitleForObject(FlexoObject object) {
		if (object == null) {
			return FlexoLocalization.localizedForKey("no_selection");
		}
		if (object instanceof ViewLibrary) {
			return FlexoLocalization.localizedForKey("view_library");
		}
		if (object instanceof VirtualModelInstance) {
			return ((VirtualModelInstance) object).getTitle();
		}
		if (object instanceof View) {
			return ((View) object).getName();
		}
		if (object instanceof TechnologyObject) {
			return getWindowTitleForTechnologyObject((TechnologyObject<?>) object, _controller);
		}
		return object.toString();
	}

	private <TA extends TechnologyAdapter> String getWindowTitleForTechnologyObject(TechnologyObject<TA> object, FlexoController controller) {
		TechnologyAdapterControllerService tacService = _controller.getApplicationContext().getTechnologyAdapterControllerService();
		TechnologyAdapterController<TA> tac = tacService.getTechnologyAdapterController(object.getTechnologyAdapter());
		return tac.getWindowTitleforObject(object, controller);
	}

	public void setProject(FlexoProject project) {
		projectResourcesBrowser.setRootObject(project);
	}

	/*@Override
	public void notifyModuleViewDisplayed(ModuleView<?> moduleView) {
		super.notifyModuleViewDisplayed(moduleView);

		if (moduleView.getRepresentedObject() instanceof TechnologyObject) {
			notifyTechnologyObjectModuleViewDisplayed((ModuleView<TechnologyObject<?>>) moduleView, _controller);
		}
		//if (moduleView instanceof DiagramModuleView) {
		//	_controller.getControllerModel().setRightViewVisible(true);
		//} else {
		_controller.getControllerModel().setRightViewVisible(false);
		// }
	}

	private void notifyTechnologyObjectModuleViewDisplayed(ModuleView<TechnologyObject<?>> moduleView, FlexoController controller) {
		TechnologyAdapterControllerService tacService = _controller.getApplicationContext().getTechnologyAdapterControllerService();
		TechnologyAdapterController<?> tac = tacService.getTechnologyAdapterController(moduleView.getRepresentedObject()
				.getTechnologyAdapter());
		tac.notifyModuleViewDisplayed(moduleView, controller, this);
	}*/

}
