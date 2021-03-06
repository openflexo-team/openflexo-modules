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

package org.openflexo.xxxmodule.controller;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.module.FlexoModule.WelcomePanel;
import org.openflexo.selection.MouseSelectionManager;
import org.openflexo.view.FlexoMainPane;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;
import org.openflexo.view.menu.FlexoMenuBar;
import org.openflexo.xxxmodule.XXXIconLibrary;
import org.openflexo.xxxmodule.XXXModule;
import org.openflexo.xxxmodule.controller.action.XXXControllerActionInitializer;
import org.openflexo.xxxmodule.model.XXXProjectNature;
import org.openflexo.xxxmodule.view.ConvertToXXXProjectView;
import org.openflexo.xxxmodule.view.XXXMainPane;
import org.openflexo.xxxmodule.view.XXXWelcomePanelModuleView;
import org.openflexo.xxxmodule.view.menu.XXXMenuBar;

/**
 * Controller for XXX module
 * 
 * @author yourname
 */
public class XXXController extends FlexoController {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(XXXController.class.getPackage().getName());

	private XXXPerspective xxxPerspective;

	/**
	 * Default constructor
	 */
	public XXXController(XXXModule module) {
		super(module);
	}

	@Override
	protected void initializePerspectives() {
		this.addToPerspectives(xxxPerspective = new XXXPerspective(this));
	}

	public XXXPerspective getXXXPerspective() {
		return xxxPerspective;
	}

	@Override
	protected MouseSelectionManager createSelectionManager() {
		return new XXXSelectionManager(this);
	}

	@Override
	public ControllerActionInitializer createControllerActionInitializer() {
		return new XXXControllerActionInitializer(this);
	}

	/**
	 * Creates a new instance of MenuBar for the module this controller refers to
	 * 
	 * @return
	 */
	@Override
	protected FlexoMenuBar createNewMenuBar() {
		return new XXXMenuBar(this);
	}

	@Override
	public FlexoObject getDefaultObjectToSelect(FlexoProject<?> project) {
		if (project != null && project.hasNature(XXXProjectNature.class)) {
			return project.getNature(XXXProjectNature.class);
		}
		return project;
	}

	@Override
	protected FlexoMainPane createMainPane() {
		return new XXXMainPane(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ModuleView<?> makeWelcomePanel(WelcomePanel<?> welcomePanel, FlexoPerspective perspective) {
		return new XXXWelcomePanelModuleView((WelcomePanel<XXXModule>) welcomePanel, this, perspective);
	}

	@Override
	public ModuleView<?> makeDefaultProjectView(FlexoProject<?> project, FlexoPerspective perspective) {
		return new ConvertToXXXProjectView(project, this, perspective);
	}

	public XXXProjectNature getXXXNature() {
		if (getProject() != null) {
			return getProject().getNature(XXXProjectNature.class);
		}
		return null;
	}

	@Override
	protected void updateEditor(final FlexoEditor from, final FlexoEditor to) {
		super.updateEditor(from, to);
		FlexoProject<?> project = (to != null ? to.getProject() : null);
		xxxPerspective.setProject(project);
	}

	@Override
	public ImageIcon iconForObject(final Object object) {
		if (object instanceof XXXProjectNature) {
			return XXXIconLibrary.XXX_SMALL_ICON;
		}
		return super.iconForObject(object);
	}

}
