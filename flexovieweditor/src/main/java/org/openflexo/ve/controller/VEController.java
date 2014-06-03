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

/*
 * Created on <date> by <yourname>
 *
 * Flexo Application Suite
 * (c) Denali 2003-2006
 */
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.viewpoint.VirtualModelTechnologyAdapter;
import org.openflexo.module.FlexoModule;
import org.openflexo.selection.MouseSelectionManager;
import org.openflexo.ve.controller.action.VEControllerActionInitializer;
import org.openflexo.ve.view.VEMainPane;
import org.openflexo.ve.view.menu.VEMenuBar;
import org.openflexo.view.FlexoMainPane;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.ProjectResourcesPerspective;
import org.openflexo.view.controller.TechnologyAdapterController;
import org.openflexo.view.menu.FlexoMenuBar;

/**
 * Controller for ViewEditor module
 * 
 * @author sylvain
 */
public class VEController extends FlexoController {

	private static final Logger logger = Logger.getLogger(VEController.class.getPackage().getName());

	public ProjectResourcesPerspective RESOURCES_PERSPECTIVE;
	public ViewLibraryPerspective VIEW_LIBRARY_PERSPECTIVE;

	/**
	 * Default constructor
	 */
	public VEController(FlexoModule module) {
		super(module);
	}

	@Override
	protected void initializePerspectives() {
		addToPerspectives(RESOURCES_PERSPECTIVE = new ProjectResourcesPerspective(this));
		addToPerspectives(VIEW_LIBRARY_PERSPECTIVE = new ViewLibraryPerspective(this));

		for (TechnologyAdapter ta : getApplicationContext().getTechnologyAdapterService().getTechnologyAdapters()) {
			if (!(ta instanceof VirtualModelTechnologyAdapter)) {
				TechnologyAdapterController<?> tac = getApplicationContext().getTechnologyAdapterControllerService()
						.getTechnologyAdapterController(ta);
				if (tac != null) {
					tac.installTechnologyPerspective(this);
				}
			}
		}

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

	@Override
	public void updateEditor(FlexoEditor from, FlexoEditor to) {
		super.updateEditor(from, to);
		FlexoProject project = to != null ? to.getProject() : null;
		/*if (project != null) {
			project.getStringEncoder()._addConverter(GraphicalRepresentation.POINT_CONVERTER);
			project.getStringEncoder()._addConverter(GraphicalRepresentation.RECT_POLYLIN_CONVERTER);
		}*/
		if (RESOURCES_PERSPECTIVE != null) {
			RESOURCES_PERSPECTIVE.setProject(project);
		}
		if (VIEW_LIBRARY_PERSPECTIVE != null) {
			VIEW_LIBRARY_PERSPECTIVE.setProject(project);
		}
		// ONTOLOGY_PERSPECTIVE.setProject(project);
	}

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
	}

	@Override
	protected FlexoMainPane createMainPane() {
		return new VEMainPane(this);
	}

}
