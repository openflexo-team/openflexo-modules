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
package org.openflexo.fme.controller;

import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.openflexo.components.widget.FIBViewPointLibraryBrowser;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.viewpoint.FlexoConcept;
import org.openflexo.foundation.viewpoint.FlexoConceptObject;
import org.openflexo.foundation.viewpoint.ViewPoint;
import org.openflexo.foundation.viewpoint.ViewPointLibrary;
import org.openflexo.foundation.viewpoint.VirtualModel;
import org.openflexo.icon.VPMIconLibrary;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

public class FMEPerspective extends FlexoPerspective {

	protected static final Logger logger = Logger.getLogger(FMEPerspective.class.getPackage().getName());

	private FIBViewPointLibraryBrowser viewPointLibraryBrowser = null;

	private JPanel toolsPanel;

	/**
	 * Default constructor taking controller as argument
	 */
	public FMEPerspective(FMEController controller) {
		super("viewpoint_perspective", controller);
		// _controller = controller;

		viewPointLibraryBrowser = new FIBViewPointLibraryBrowser(controller.getViewPointLibrary(), controller);

		/*viewPointBrowser = new FIBViewPointBrowser(null, controller);
		virtualModelBrowser = new FIBVirtualModelBrowser(null, controller);
		exampleDiagramBrowser = new FIBExampleDiagramBrowser(null, controller);
		diagramPaletteBrowser = new FIBDiagramPaletteBrowser(null, controller);*/

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

}
