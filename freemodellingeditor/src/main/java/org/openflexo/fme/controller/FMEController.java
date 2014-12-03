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

import javax.swing.ImageIcon;

import org.openflexo.fme.FMEIconLibrary;
import org.openflexo.fme.controller.action.FMEControllerActionInitializer;
import org.openflexo.fme.model.FreeMetaModel;
import org.openflexo.fme.model.FreeModel;
import org.openflexo.fme.model.FreeModellingProject;
import org.openflexo.fme.view.menu.FMEMenuBar;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.validation.FlexoValidationModel;
import org.openflexo.foundation.viewpoint.ViewPoint;
import org.openflexo.foundation.viewpoint.ViewPointObject;
import org.openflexo.icon.IconLibrary;
import org.openflexo.icon.VPMIconLibrary;
import org.openflexo.module.FlexoModule;
import org.openflexo.selection.MouseSelectionManager;
import org.openflexo.view.FlexoMainPane;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.menu.FlexoMenuBar;

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

		/*SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				setCurrentEditedObjectAsModuleView(getApplicationContext().getViewPointLibrary(), FREE_MODELLING_PERSPECTIVE);
			}
		});*/
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

	@Override
	public FlexoObject getDefaultObjectToSelect(FlexoProject project) {
		return project;
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
			if (object instanceof FreeModel) {
				setCurrentEditedObjectAsModuleView(object);
			}
			if (getCurrentPerspective() == FREE_MODELLING_PERSPECTIVE) {
				if (object instanceof FreeModel) {
					FREE_MODELLING_PERSPECTIVE.focusOnFreeModel((FreeModel) object);
				}
			}
			getSelectionManager().setSelectedObject(object);
		} else {
			logger.warning("Cannot set focus on a NULL object");
		}
	}

	public ViewPoint getCurrentViewPoint() {
		if (getCurrentDisplayedObjectAsModuleView() instanceof ViewPointObject) {
			return ((ViewPointObject) getCurrentDisplayedObjectAsModuleView()).getViewPoint();
		}
		return null;
	}

	@Override
	public FlexoValidationModel getValidationModelForObject(FlexoObject object) {
		if (object instanceof ViewPointObject) {
			return getApplicationContext().getViewPointLibrary().getViewPointValidationModel();
		}
		return super.getValidationModelForObject(object);
	}

	@Override
	public void updateEditor(FlexoEditor from, FlexoEditor to) {
		super.updateEditor(from, to);
		FlexoProject project = (to != null ? to.getProject() : null);
		FREE_MODELLING_PERSPECTIVE.setProject(project);
	}

	@Override
	public ImageIcon iconForObject(Object object) {
		if (object instanceof FreeModellingProject) {
			return IconLibrary.OPENFLEXO_NOTEXT_16;
		} else if (object instanceof FreeModel) {
			return FMEIconLibrary.DIAGRAM_ICON;
		} else if (object instanceof FreeMetaModel) {
			return FMEIconLibrary.FME_SMALL_ICON;
		}
		return super.iconForObject(object);
	}

}
