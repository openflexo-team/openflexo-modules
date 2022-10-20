/**
 * 
 * Copyright (c) 2013-2017, Openflexo
 * Copyright (c) 2011-2012, AgileBirds
 * 
 * This file is part of Flexovieweditor, a component of the software infrastructure 
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

package org.openflexo.om.controller;

import java.util.logging.Logger;

import org.openflexo.fml.controller.FMLTechnologyPerspective;
import org.openflexo.fml.controller.LocalizationPerspective;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.FMLObject;
import org.openflexo.foundation.fml.FlexoConceptObject;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.VirtualModelLibrary;
import org.openflexo.foundation.nature.FlexoNature;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.validation.FlexoValidationModel;
import org.openflexo.module.FlexoModule.WelcomePanel;
import org.openflexo.om.OMModule;
import org.openflexo.om.OpenflexoModeller;
import org.openflexo.om.controller.action.OMControllerActionInitializer;
import org.openflexo.om.view.OMDefaultProjectView;
import org.openflexo.om.view.OMMainPane;
import org.openflexo.om.view.OMWelcomePanelModuleView;
import org.openflexo.om.view.menu.OMMenuBar;
import org.openflexo.selection.MouseSelectionManager;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.controller.FMLControlledDiagramNaturePerspective;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelInstanceNature;
import org.openflexo.technologyadapter.gina.GINATechnologyAdapter;
import org.openflexo.technologyadapter.gina.controller.FMLControlledFIBNaturePerspective;
import org.openflexo.view.FlexoMainPane;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.GenericPerspective;
import org.openflexo.view.controller.model.FlexoPerspective;
import org.openflexo.view.menu.FlexoMenuBar;

/**
 * Controller for {@link OpenflexoModeller} module
 * 
 * @author sylvain
 */
public class OMController extends FlexoController {

	private static final Logger logger = Logger.getLogger(OMController.class.getPackage().getName());

	private GenericPerspective genericPerspective;
	private FMLTechnologyPerspective fmlPerspective;
	private FMLControlledDiagramNaturePerspective diagramPerspective;
	private FMLControlledFIBNaturePerspective ginaPerspective;
	private LocalizationPerspective localesPerspective;

	/**
	 * Default constructor
	 */
	public OMController(OMModule module) {
		super(module);
	}

	@Override
	protected void initializePerspectives() {

		addToPerspectives(fmlPerspective = new FMLTechnologyPerspective(this));
		addToPerspectives(genericPerspective = new GenericPerspective(this));
		addToPerspectives(diagramPerspective = new FMLControlledDiagramNaturePerspective(this));
		addToPerspectives(ginaPerspective = new FMLControlledFIBNaturePerspective(this));
		addToPerspectives(localesPerspective = new LocalizationPerspective(this));
	}

	@Override
	public void switchToPerspective(FlexoNature<?> nature) {
		if (nature == FMLControlledDiagramVirtualModelInstanceNature.INSTANCE) {
			switchToPerspective(diagramPerspective);
		}
	}

	@Override
	public void focusOnTechnologyAdapter(TechnologyAdapter<?> technologyAdapter) {
		if (technologyAdapter instanceof DiagramTechnologyAdapter) {
			switchToPerspective(getDiagramPerspective());
		}
		else if (technologyAdapter instanceof GINATechnologyAdapter) {
			switchToPerspective(getGinaPerspective());
		}
	}

	public GenericPerspective getGenericPerspective() {
		return genericPerspective;
	}

	public FMLTechnologyPerspective getFMLPerspective() {
		return fmlPerspective;
	}

	public FMLControlledDiagramNaturePerspective getDiagramPerspective() {
		return diagramPerspective;
	}

	public FMLControlledFIBNaturePerspective getGinaPerspective() {
		return ginaPerspective;
	}

	public LocalizationPerspective getLocalesPerspective() {
		return localesPerspective;
	}

	@Override
	protected MouseSelectionManager createSelectionManager() {
		return new OMSelectionManager(this);
	}

	@Override
	public ControllerActionInitializer createControllerActionInitializer() {
		return new OMControllerActionInitializer(this);
	}

	/**
	 * Creates a new instance of MenuBar for the module this controller refers to
	 * 
	 * @return
	 */
	@Override
	protected FlexoMenuBar createNewMenuBar() {
		return new OMMenuBar(this);
	}

	@Override
	public FlexoObject getDefaultObjectToSelect(FlexoProject<?> project) {
		return project;
	}

	@Override
	protected FlexoMainPane createMainPane() {
		return new OMMainPane(this);
	}

	/**
	 * Return the VirtualModelLibrary
	 * 
	 * @return
	 */
	public VirtualModelLibrary getVirtualModelLibrary() {
		return getApplicationContext().getService(VirtualModelLibrary.class);
	}

	
	
	/**
	 * Return boolean indicating if there is a {@link ModuleView} which can represent supplied object in supplied perspective
	 * @param object
	 * @param perspective
	 * @return
	 */
	/*public boolean mayRepresent(FlexoObject object, FlexoPerspective perspective) {
		fdssdf
	}*/
	
	
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
				setCurrentEditedObject(((FlexoConceptObject) object).getFlexoConcept());
			}
			else {
				logger.info("setCurrentEditedObjectAsModuleView with " + object);
				setCurrentEditedObject(object);
			}

			getSelectionManager().setSelectedObject(object);
		}
		else {
			logger.warning("Cannot set focus on a NULL object");
		}
	}

	public VirtualModel getCurrentVirtualModel() {
		if (getCurrentDisplayedObjectAsModuleView() instanceof FMLObject) {
			return ((FMLObject) getCurrentDisplayedObjectAsModuleView()).getDeclaringCompilationUnit().getVirtualModel();
		}
		return null;
	}

	@Override
	public FlexoValidationModel getValidationModelForObject(FlexoObject object) {
		if (object instanceof FMLObject) {
			return getApplicationContext().getVirtualModelLibrary().getFMLValidationModel();
		}
		return super.getValidationModelForObject(object);
	}

	@Override
	public ModuleView<?> makeWelcomePanel(WelcomePanel<?> welcomePanel, FlexoPerspective perspective) {
		return new OMWelcomePanelModuleView((WelcomePanel<OMModule>) welcomePanel, this, perspective);
	}

	@Override
	public ModuleView<?> makeDefaultProjectView(FlexoProject<?> project, FlexoPerspective perspective) {
		return new OMDefaultProjectView(project, this, perspective);
	}
}
