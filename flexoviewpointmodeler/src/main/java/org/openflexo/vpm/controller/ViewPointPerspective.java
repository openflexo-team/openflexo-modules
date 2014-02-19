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
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.openflexo.FlexoCst;
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
import org.openflexo.vpm.view.StandardFlexoConceptView;
import org.openflexo.vpm.view.ViewPointView;
import org.openflexo.vpm.view.VirtualModelView;

public class ViewPointPerspective extends FlexoPerspective {

	protected static final Logger logger = Logger.getLogger(ViewPointPerspective.class.getPackage().getName());

	// private final VPMController _controller;

	private final JLabel infoLabel;

	private final JPanel EMPTY_RIGHT_VIEW = new JPanel();

	private FIBViewPointLibraryBrowser viewPointLibraryBrowser = null;

	private JPanel toolsPanel;

	/**
	 * Default constructor taking controller as argument
	 */
	public ViewPointPerspective(VPMController controller) {
		super("viewpoint_perspective");
		// _controller = controller;

		viewPointLibraryBrowser = new FIBViewPointLibraryBrowser(controller.getViewPointLibrary(), controller);

		/*viewPointBrowser = new FIBViewPointBrowser(null, controller);
		virtualModelBrowser = new FIBVirtualModelBrowser(null, controller);
		exampleDiagramBrowser = new FIBExampleDiagramBrowser(null, controller);
		diagramPaletteBrowser = new FIBDiagramPaletteBrowser(null, controller);*/

		setTopLeftView(viewPointLibraryBrowser);

		infoLabel = new JLabel("ViewPoint perspective");
		infoLabel.setFont(FlexoCst.SMALL_FONT);
		setFooter(infoLabel);
	}

	/*@Override
	public JComponent getHeader() {
		if (_controller.getCurrentModuleView() instanceof ExampleDiagramModuleView) {
			return scaleSelector.getComponent();
		}
		return null;
	}*/

	public ModuleView<?> getCurrentModuleView(FlexoController controller) {
		return controller.getCurrentModuleView();
	}

	public void focusOnViewPoint(ViewPoint viewPoint) {
		logger.info("focusOnViewPoint " + viewPoint);

		// viewPointBrowser.setRootObject(viewPoint);
		// setBottomLeftView(viewPointBrowser);
	}

	public void focusOnVirtualModel(VirtualModel virtualModel) {
		logger.info("focusOnVirtualModel " + virtualModel);

		// virtualModelBrowser.setRootObject(virtualModel);
		// setBottomLeftView(virtualModelBrowser);
	}

	/*public void focusOnPalette(DiagramPalette palette) {
		logger.info("focusOnPalette " + palette);
		// diagramPaletteBrowser.setRootObject(palette);
		// setBottomLeftView(diagramPaletteBrowser);
	}

	public void focusOnExampleDiagram(ExampleDiagram exampleDiagram) {
		logger.info("focusOnExampleDiagram " + exampleDiagram);
		// exampleDiagramBrowser.setRootObject(exampleDiagram);
		// setBottomLeftView(exampleDiagramBrowser);
	}*/

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
	public FlexoObject getDefaultObject(FlexoObject proposedObject, FlexoController controller) {
		if (hasModuleViewForObject(proposedObject, controller)) {
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

	@Override
	public boolean hasModuleViewForObject(FlexoObject object, FlexoController controller) {
		return /*object instanceof DiagramPalette || object instanceof ExampleDiagram ||*/object instanceof ViewPoint
				|| object instanceof FlexoConcept;
	}

	@Override
	public ModuleView<? extends FlexoObject> createModuleViewForObject(FlexoObject object, FlexoController controller) {
		if (object.isDeleted()) {
			return null;
		}
		if (object instanceof ViewPoint) {
			return new ViewPointView((ViewPoint) object, controller, this);
		}
		if (object instanceof FlexoConcept) {
			FlexoConcept ep = (FlexoConcept) object;
			if (ep instanceof VirtualModel) {
				// if (ep instanceof DiagramSpecification) {
				// return new DiagramSpecificationView(ep, (VPMController) controller);
				// } else {
				return new VirtualModelView(ep, controller, this);
				// }
			} else {
				// if (ep.getVirtualModel() instanceof DiagramSpecification) {
				// return new DiagramEditionPatternView(ep, (VPMController) controller);
				// } else {
				return new StandardFlexoConceptView(ep, controller, this);
				// }
			}

		}
		/*if (object instanceof DiagramPalette) {
			return new DiagramPaletteEditor(_controller, (DiagramPalette) object, false).getModuleView();
		}
		if (object instanceof ExampleDiagram) {
			return new ExampleDiagramEditor(_controller, (ExampleDiagram) object, false).getModuleView();
		}*/
		return null;
	}

	@Override
	public JComponent getTopRightView() {
		/*if (getCurrentModuleView() instanceof DiagramPaletteModuleView) {
			return ((DiagramPaletteModuleView) getCurrentModuleView()).getController().getPaletteView();
		} else if (getCurrentModuleView() instanceof ExampleDiagramModuleView) {
			return ((ExampleDiagramModuleView) getCurrentModuleView()).getController().getPaletteView();
		}*/
		return EMPTY_RIGHT_VIEW;
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
		/*if (object instanceof DiagramPalette) {
			return ((DiagramPalette) object).getName() + " (" + FlexoLocalization.localizedForKey("palette") + ")";
		}
		if (object instanceof ExampleDiagram) {
			return ((ExampleDiagram) object).getName() + " (" + FlexoLocalization.localizedForKey("example_diagram") + ")";
		}*/
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

}
