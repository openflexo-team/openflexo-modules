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

package org.openflexo.eamodule.controller;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.eamodule.EAMIconLibrary;
import org.openflexo.eamodule.EAModule;
import org.openflexo.eamodule.EnterpriseArchitectureModule;
import org.openflexo.eamodule.controller.action.EAMControllerActionInitializer;
import org.openflexo.eamodule.model.BPMNVirtualModelInstance;
import org.openflexo.eamodule.model.EAProjectNature;
import org.openflexo.eamodule.view.ConvertToEAMProjectView;
import org.openflexo.eamodule.view.EAMMainPane;
import org.openflexo.eamodule.view.EAMWelcomePanelModuleView;
import org.openflexo.eamodule.view.menu.EAMMenuBar;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.module.FlexoModule.WelcomePanel;
import org.openflexo.selection.MouseSelectionManager;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.controller.DiagramTechnologyAdapterController;
import org.openflexo.view.FlexoMainPane;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;
import org.openflexo.view.menu.FlexoMenuBar;

/**
 * Controller for {@link EnterpriseArchitectureModule} module
 * 
 * @author sylvain
 */
public class EAMController extends FlexoController {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(EAMController.class.getPackage().getName());

	private EAPerspective eaPerspective;

	/**
	 * Default constructor
	 */
	public EAMController(EAModule module) {
		super(module);
	}

	@Override
	protected void initializePerspectives() {

		addToPerspectives(eaPerspective = new EAPerspective(this));
	}

	@Override
	protected MouseSelectionManager createSelectionManager() {
		return new EAMSelectionManager(this);
	}

	@Override
	public ControllerActionInitializer createControllerActionInitializer() {
		return new EAMControllerActionInitializer(this);
	}

	/**
	 * Creates a new instance of MenuBar for the module this controller refers to
	 * 
	 * @return
	 */
	@Override
	protected FlexoMenuBar createNewMenuBar() {
		return new EAMMenuBar(this);
	}

	@Override
	public FlexoObject getDefaultObjectToSelect(FlexoProject<?> project) {
		if (project != null && project.hasNature(EAProjectNature.class)) {
			return project.getNature(EAProjectNature.class);
		}
		return project;
	}

	@Override
	protected FlexoMainPane createMainPane() {
		return new EAMMainPane(this);
	}

	@Override
	protected void updateEditor(final FlexoEditor from, final FlexoEditor to) {
		super.updateEditor(from, to);
		FlexoProject<?> project = (to != null ? to.getProject() : null);
		eaPerspective.setProject(project);
	}

	@Override
	public ImageIcon iconForObject(final Object object) {
		if (object instanceof EAProjectNature) {
			return IconFactory.getImageIcon(IconLibrary.OPENFLEXO_NOTEXT_16, EAMIconLibrary.EAM_MARKER);
		}
		else if (object instanceof BPMNVirtualModelInstance) {
			return EAMIconLibrary.BPMN_MEDIUM_ICON;
		}
		return super.iconForObject(object);
	}

	private DiagramTechnologyAdapterController diagramTAC;

	/**
	 * Helper functio to ease access to DiagramTAController
	 * 
	 * @return
	 */
	public DiagramTechnologyAdapterController getDiagramTAC() {
		if (diagramTAC == null) {
			DiagramTechnologyAdapter diagramTA = this.getTechnologyAdapter(DiagramTechnologyAdapter.class);
			diagramTAC = (DiagramTechnologyAdapterController) getTechnologyAdapterController(diagramTA);
		}
		return this.diagramTAC;
	}

	@Override
	public ModuleView<?> makeWelcomePanel(WelcomePanel<?> welcomePanel, FlexoPerspective perspective) {
		return new EAMWelcomePanelModuleView((WelcomePanel<EAModule>) welcomePanel, this, perspective);
	}

	@Override
	public ModuleView<?> makeDefaultProjectView(FlexoProject<?> project, FlexoPerspective perspective) {
		return new ConvertToEAMProjectView(project, this, perspective);
	}

}
