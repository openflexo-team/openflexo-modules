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
package org.openflexo.vpm.controller;

import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.openflexo.components.widget.FIBViewPointLibraryBrowser;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.viewpoint.FlexoConcept;
import org.openflexo.foundation.viewpoint.FlexoConceptObject;
import org.openflexo.foundation.viewpoint.ViewPoint;
import org.openflexo.foundation.viewpoint.ViewPointLibrary;
import org.openflexo.foundation.viewpoint.ViewPointObject;
import org.openflexo.foundation.viewpoint.VirtualModel;
import org.openflexo.icon.VPMIconLibrary;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

public class ViewPointPerspective extends FlexoPerspective {

	protected static final Logger logger = Logger.getLogger(ViewPointPerspective.class.getPackage().getName());

	private FIBViewPointLibraryBrowser viewPointLibraryBrowser = null;

	private JPanel toolsPanel;

	/**
	 * Default constructor taking controller as argument
	 */
	public ViewPointPerspective(VPMController controller) {
		super("viewpoint_perspective", controller);

		viewPointLibraryBrowser = new FIBViewPointLibraryBrowser(controller.getViewPointLibrary(), controller);

		setTopLeftView(viewPointLibraryBrowser);

	}

	public ModuleView<?> getCurrentModuleView(FlexoController controller) {
		return controller.getCurrentModuleView();
	}

	public void focusOnViewPoint(ViewPoint viewPoint) {
		logger.info("focusOnViewPoint " + viewPoint);

	}

	public void focusOnVirtualModel(VirtualModel virtualModel) {
		logger.info("focusOnVirtualModel " + virtualModel);

	}

	public void hideBottomBrowser() {
		setBottomLeftView(null);
	}

	/**
	 * Overrides getIcon
	 * 
	 * @see org.openflexo.view.controller.model.FlexoPerspective#getActiveIcon()
	 */
	@Override
	public ImageIcon getActiveIcon() {
		return VPMIconLibrary.VIEWPOINT_ICON;
	}

	@Override
	public FlexoObject getDefaultObject(FlexoObject proposedObject) {
		if (hasModuleViewForObject(proposedObject)) {
			return proposedObject;
		}
		if (proposedObject instanceof FlexoConceptObject) {
			return ((FlexoConceptObject) proposedObject).getFlexoConcept();
		}
		if (proposedObject instanceof ViewPointObject) {
			return ((ViewPointObject) proposedObject).getViewPoint();
		}
		return null;
	}

	public String getWindowTitleforObject(FlexoObject object, FlexoController controller) {
		if (object instanceof ViewPointLibrary) {
			return FlexoLocalization.localizedForKey("view_point_library");
		}
		if (object instanceof ViewPoint) {
			return ((ViewPoint) object).getName();
		}
		if (object instanceof VirtualModel) {
			return ((VirtualModel) object).getName();
		}
		if (object instanceof FlexoConcept) {
			return ((FlexoConcept) object).getName();
		}
		if (object != null) {
			return object.toString();
		}
		return "null";
	}

	@Override
	public void objectWasClicked(Object object, FlexoController controller) {
		// logger.info("ViewPointPerspective: object was clicked: " + object);
		if (object == null) {
			return;
		}

		/*if (getBottomLeftView() == viewPointBrowser) {
			if (!(object instanceof ViewPointObject) || (object instanceof DiagramPaletteObject)
					|| (object instanceof ExampleDiagramObject)) {
				setBottomLeftView(null);
			} else {
				ViewPointObject o = (ViewPointObject) object;
				if (o.getViewPoint() != viewPointBrowser.getRootObject()) {
					setBottomLeftView(null);
				}
			}
		}

		else if (getBottomLeftView() == exampleDiagramBrowser) {
			if (!(object instanceof ViewPointObject)) {
				setBottomLeftView(null);
			} else {
				ViewPointObject o = (ViewPointObject) object;
				if (o instanceof ExampleDiagramObject) {
					if (((ExampleDiagramObject) o).getExampleDiagram() != exampleDiagramBrowser.getRootObject()) {
						setBottomLeftView(null);
					}
				} else {
					setBottomLeftView(null);
				}
			}
		}

		else if (getBottomLeftView() == diagramPaletteBrowser) {

			if (!(object instanceof ViewPointObject)) {
				setBottomLeftView(null);
			} else {
				ViewPointObject o = (ViewPointObject) object;
				if (o instanceof DiagramPaletteObject) {
					if (((DiagramPaletteObject) o).getPalette() != diagramPaletteBrowser.getRootObject()) {
						setBottomLeftView(null);
					}
				} else {
					setBottomLeftView(null);
				}
			}
		}

		if (getBottomLeftView() == null) {
			if (object instanceof ViewPointObject) {
				if (object instanceof DiagramPaletteObject) {
					diagramPaletteBrowser.setRootObject(((DiagramPaletteObject) object).getPalette());
					setBottomLeftView(diagramPaletteBrowser);
				} else if (object instanceof ExampleDiagramObject) {
					exampleDiagramBrowser.setRootObject(((ExampleDiagramObject) object).getExampleDiagram());
					setBottomLeftView(exampleDiagramBrowser);
				} else {
					viewPointBrowser.setRootObject(((ViewPointObject) object).getViewPoint());
					setBottomLeftView(viewPointBrowser);
				}
			}
		}*/
	}

	@Override
	public void objectWasRightClicked(Object object, FlexoController controller) {
		// logger.info("ViewPointPerspective: object was right-clicked: " + object);
	}

	@Override
	public void objectWasDoubleClicked(Object object, FlexoController controller) {
		// logger.info("ViewPointPerspective: object was double-clicked: " + object);
		if (object instanceof FlexoConceptObject) {
			controller.selectAndFocusObject((FlexoConceptObject) object);
		}
	}

	/*@Override
	public void notifyModuleViewDisplayed(final ModuleView<?> moduleView) {
		super.notifyModuleViewDisplayed(moduleView);
		if (moduleView instanceof ViewPointView || moduleView instanceof VirtualModelView || moduleView instanceof StandardFlexoConceptView) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					// Force right view to be visible
					controller.getControllerModel().setRightViewVisible(false);
				}
			});
		}
	}*/
}
