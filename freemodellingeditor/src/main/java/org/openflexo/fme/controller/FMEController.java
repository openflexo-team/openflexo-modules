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

/*
 * Created on <date> by <yourname>
 *
 * Flexo Application Suite
 * (c) Denali 2003-2006
 */
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import org.openflexo.fme.controller.action.FMEControllerActionInitializer;
import org.openflexo.fme.view.menu.FMEMenuBar;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.validation.ValidationModel;
import org.openflexo.foundation.viewpoint.FlexoConcept;
import org.openflexo.foundation.viewpoint.FlexoConceptObject;
import org.openflexo.foundation.viewpoint.ViewPoint;
import org.openflexo.foundation.viewpoint.ViewPointLibrary;
import org.openflexo.foundation.viewpoint.ViewPointObject;
import org.openflexo.foundation.viewpoint.VirtualModel;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.module.FlexoModule;
import org.openflexo.selection.MouseSelectionManager;
import org.openflexo.view.FlexoMainPane;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.menu.FlexoMenuBar;
import org.openflexo.vpm.view.FlexoConceptView;

/**
 * This is the controller of ViewPointModeller module
 * 
 * @author sylvain
 */
public class FMEController extends FlexoController {

	private static final Logger logger = Logger.getLogger(FMEController.class.getPackage().getName());

	public FMEPerspective FREE_MODELLING_PERSPECTIVE;

	/**
	 * Default constructor
	 */
	public FMEController(FlexoModule module) {
		super(module);

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				setCurrentEditedObjectAsModuleView(getApplicationContext().getViewPointLibrary(), FREE_MODELLING_PERSPECTIVE);
			}
		});
	}

	@Override
	protected void initializePerspectives() {
		addToPerspectives(FREE_MODELLING_PERSPECTIVE = new FMEPerspective(this));
	}

	@Override
	protected MouseSelectionManager createSelectionManager() {
		return new FMESelectionManager(this);
	}

	@Override
	public ControllerActionInitializer createControllerActionInitializer() {
		return new FMEControllerActionInitializer(this);
	}

	/**
	 * Creates a new instance of MenuBar for the module this controller refers to
	 * 
	 * @return
	 */
	@Override
	protected FlexoMenuBar createNewMenuBar() {
		return new FMEMenuBar(this);
	}

	/**
	 * Init inspectors
	 */
	@Override
	public void initInspectors() {
		super.initInspectors();
		loadInspectorGroup("IFlexoOntology");

	}

	@Override
	protected FlexoMainPane createMainPane() {
		return new FlexoMainPane(this);
	}

	/**
	 * Return the ViewPointLibrary
	 * 
	 * @return
	 */
	public ViewPointLibrary getViewPointLibrary() {
		return getApplicationContext().getService(ViewPointLibrary.class);
	}

	@Override
	public FlexoObject getDefaultObjectToSelect(FlexoProject project) {
		return getViewPointLibrary();
	}

	/**
	 * Select the view representing supplied object, if this view exists. Try all to really display supplied object, even if required view
	 * is not the current displayed view
	 * 
	 * @param object
	 *            : the object to focus on
	 */
	@Override
	public void selectAndFocusObject(FlexoObject object) {
		if (object != null) {
			logger.info("selectAndFocusObject " + object + "of " + object.getClass().getSimpleName());
			if (object instanceof FlexoConceptObject) {
				setCurrentEditedObjectAsModuleView(((FlexoConceptObject) object).getFlexoConcept());
			} else {
				setCurrentEditedObjectAsModuleView(object);
			}
			if (getCurrentPerspective() == FREE_MODELLING_PERSPECTIVE) {
				if (object instanceof ViewPointLibrary) {
					/*ViewPointLibrary cl = (ViewPointLibrary) object;
					if (cl.getViewPoints().size() > 0) {
						getSelectionManager().setSelectedObject(cl.getViewPoints().firstElement());
					}*/
				} /*else if (object instanceof OWLMetaModel) {
					OWLMetaModel ontology = (OWLMetaModel) object;
					VIEW_POINT_PERSPECTIVE.focusOnOntology(ontology);
					if (ontology.getClasses().size() > 0) {
						getSelectionManager().setSelectedObject(ontology.getClasses().firstElement());
					}
					}*/
				/*else if (object instanceof ExampleDiagram) {
					VIEW_POINT_PERSPECTIVE.focusOnExampleDiagram((ExampleDiagram) object);
				} else if (object instanceof DiagramPalette) {
					VIEW_POINT_PERSPECTIVE.focusOnPalette((DiagramPalette) object);
				}*/else if (object instanceof ViewPoint) {
					ViewPoint viewPoint = (ViewPoint) object;
					FREE_MODELLING_PERSPECTIVE.focusOnViewPoint(viewPoint);
				} else if (object instanceof VirtualModel) {
					VirtualModel virtualModel = (VirtualModel) object;
					FREE_MODELLING_PERSPECTIVE.focusOnVirtualModel(virtualModel);
				} else if (object instanceof FlexoConcept) {
					FlexoConcept pattern = (FlexoConcept) object;
					if (pattern.getFlexoBehaviours().size() > 0) {
						getSelectionManager().setSelectedObject(pattern.getFlexoBehaviours().get(0));
					}
				} else if (object instanceof FlexoConceptObject) {
					if (getCurrentModuleView() instanceof FlexoConceptView) {
						((FlexoConceptView) getCurrentModuleView()).tryToSelect((FlexoConceptObject) object);
					}
				}
			}
			getSelectionManager().setSelectedObject(object);
		} else {
			logger.warning("Cannot set focus on a NULL object");
		}
	}

	// ================================================
	// ============ Exception management ==============
	// ================================================

	@Override
	public String getWindowTitleforObject(FlexoObject object) {
		// System.out.println("getWindowTitleforObject() "+object+" perspective="+getCurrentPerspective());
		if (object instanceof ViewPointLibrary) {
			return FlexoLocalization.localizedForKey("view_point_library");
		}
		/*if (object instanceof OntologyLibrary) {
			return FlexoLocalization.localizedForKey("ontology_library");
		}*/
		if (getCurrentPerspective() == FREE_MODELLING_PERSPECTIVE) {
			return FREE_MODELLING_PERSPECTIVE.getWindowTitleforObject(object, this);
		}
		return object.toString();
	}

	public ViewPoint getCurrentViewPoint() {
		if (getCurrentDisplayedObjectAsModuleView() instanceof ViewPointObject) {
			return ((ViewPointObject) getCurrentDisplayedObjectAsModuleView()).getViewPoint();
		}
		return null;
	}

	@Override
	public ValidationModel getDefaultValidationModel() {
		return ViewPointLibrary.VALIDATION_MODEL;
	}
}
