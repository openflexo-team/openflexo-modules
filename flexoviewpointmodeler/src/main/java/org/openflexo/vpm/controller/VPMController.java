/**
 * 
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2011-2012, AgileBirds
 * 
 * This file is part of Flexoviewpointmodeler, a component of the software infrastructure 
 * developed at Openflexo.
 * 
 * 
 * Openflexo is dual-licensed under the European Union Public License (EUPL, either 
 * version 1.1 of the License, or any later version ), which is available at 
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 * and the GNU General Public License (GPL, either version 3 of the License, or any 
 * later version), which is available at http://www.gnu.org/licenses/gpl.html .
 * 
 * You can redistribute it and/or modify under the terms of either of these licenses
 * 
 * If you choose to redistribute it and/or modify under the terms of the GNU GPL, you
 * must include the following additional permission.
 *
 *          Additional permission under GNU GPL version 3 section 7
 *
 *          If you modify this Program, or any covered work, by linking or 
 *          combining it with software containing parts covered by the terms 
 *          of EPL 1.0, the licensors of this Program grant you additional permission
 *          to convey the resulting work. * 
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. 
 *
 * See http://www.openflexo.org/license.html for details.
 * 
 * 
 * Please contact Openflexo (openflexo-contacts@openflexo.org)
 * or visit www.openflexo.org if you need additional information.
 * 
 */

package org.openflexo.vpm.controller;

import java.util.logging.Logger;

import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.FMLObject;
import org.openflexo.foundation.fml.FlexoConceptObject;
import org.openflexo.foundation.fml.ViewPoint;
import org.openflexo.foundation.fml.ViewPointLibrary;
import org.openflexo.foundation.validation.FlexoValidationModel;
import org.openflexo.module.FlexoModule;
import org.openflexo.view.FlexoMainPane;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.menu.FlexoMenuBar;
import org.openflexo.vpm.controller.action.VPMControllerActionInitializer;
import org.openflexo.vpm.view.menu.VPMMenuBar;

/**
 * This is the controller of ViewPointModeller module
 * 
 * @author sylvain
 */
public class VPMController extends FlexoController {

	private static final Logger logger = Logger.getLogger(VPMController.class.getPackage().getName());

	/**
	 * Default constructor
	 */
	public VPMController(FlexoModule module) {
		super(module);

	}

	@Override
	protected void initializePerspectives() {
		// initializeAllAvailableTechnologyPerspectives();

		initializeFMLTechnologyAdapterPerspectives();

	}

	@Override
	protected VPMSelectionManager createSelectionManager() {
		return new VPMSelectionManager(this);
	}

	@Override
	public ControllerActionInitializer createControllerActionInitializer() {
		return new VPMControllerActionInitializer(this);
	}

	/**
	 * Creates a new instance of MenuBar for the module this controller refers to
	 * 
	 * @return
	 */
	@Override
	protected FlexoMenuBar createNewMenuBar() {
		return new VPMMenuBar(this);
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
			}
			else {
				logger.info("setCurrentEditedObjectAsModuleView with " + object);
				setCurrentEditedObjectAsModuleView(object);
			}

			getSelectionManager().setSelectedObject(object);
		}
		else {
			logger.warning("Cannot set focus on a NULL object");
		}
	}

	public ViewPoint getCurrentViewPoint() {
		if (getCurrentDisplayedObjectAsModuleView() instanceof FMLObject) {
			return ((FMLObject) getCurrentDisplayedObjectAsModuleView()).getViewPoint();
		}
		return null;
	}

	@Override
	public FlexoValidationModel getValidationModelForObject(FlexoObject object) {
		if (object instanceof FMLObject) {
			return getApplicationContext().getViewPointLibrary().getViewPointValidationModel();
		}
		return super.getValidationModelForObject(object);
	}

}
