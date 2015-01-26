/**
 * 
 * Copyright (c) 2013-2014, Openflexo
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

package org.openflexo.ve.controller;

import java.util.logging.Logger;

import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.module.FlexoModule;
import org.openflexo.selection.MouseSelectionManager;
import org.openflexo.ve.controller.action.VEControllerActionInitializer;
import org.openflexo.ve.view.VEMainPane;
import org.openflexo.ve.view.menu.VEMenuBar;
import org.openflexo.view.FlexoMainPane;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.ProjectResourcesPerspective;
import org.openflexo.view.menu.FlexoMenuBar;

/**
 * Controller for ViewEditor module
 * 
 * @author sylvain
 */
public class VEController extends FlexoController {

	private static final Logger logger = Logger.getLogger(VEController.class.getPackage().getName());

	public ProjectResourcesPerspective RESOURCES_PERSPECTIVE;

	/**
	 * Default constructor
	 */
	public VEController(FlexoModule module) {
		super(module);
	}

	@Override
	protected void initializePerspectives() {
		addToPerspectives(RESOURCES_PERSPECTIVE = new ProjectResourcesPerspective(this));

		initializeAllAvailableTechnologyPerspectives();

		// Set the current Perspective to be the view library
		this.switchToPerspective(RESOURCES_PERSPECTIVE);
	}

	@Override
	protected MouseSelectionManager createSelectionManager() {
		return new VESelectionManager(this);
	}

	@Override
	public ControllerActionInitializer createControllerActionInitializer() {
		return new VEControllerActionInitializer(this);
	}

	/**
	 * Creates a new instance of MenuBar for the module this controller refers to
	 * 
	 * @return
	 */
	@Override
	protected FlexoMenuBar createNewMenuBar() {
		return new VEMenuBar(this);
	}

	/*@Override
	public void updateEditor(FlexoEditor from, FlexoEditor to) {
		super.updateEditor(from, to);
		FlexoProject project = to != null ? to.getProject() : null;*/
	/*if (project != null) {
		project.getStringEncoder()._addConverter(GraphicalRepresentation.POINT_CONVERTER);
		project.getStringEncoder()._addConverter(GraphicalRepresentation.RECT_POLYLIN_CONVERTER);
	}*/
	/*if (RESOURCES_PERSPECTIVE != null) {
		RESOURCES_PERSPECTIVE.setProject(project);
	}
	if (VIEW_LIBRARY_PERSPECTIVE != null) {
		VIEW_LIBRARY_PERSPECTIVE.setProject(project);
	}*/
	// ONTOLOGY_PERSPECTIVE.setProject(project);

	/*for (FlexoPerspective perspective : getControllerModel().getPerspectives()) {
		perspective.setProject(project);
	}
	}*/

	@Override
	public FlexoObject getDefaultObjectToSelect(FlexoProject project) {
		return project;
	}

	/**
	 * Init inspectors
	 */
	@Override
	public void initInspectors() {
		super.initInspectors();
		// loadInspectorGroup("Ontology");
		// TODO: do it in adapter-ui
		loadInspectorGroup("FML-RT");
		// TODO: do it in adapter-ui
		loadInspectorGroup("FML");
	}

	@Override
	protected FlexoMainPane createMainPane() {
		return new VEMainPane(this);
	}

}
