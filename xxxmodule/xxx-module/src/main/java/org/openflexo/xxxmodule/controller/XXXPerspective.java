/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Flexo-ui, a component of the software infrastructure 
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
import javax.swing.JPanel;

import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.technologyadapter.gina.fml.FMLControlledFIBFlexoConceptInstanceNature;
import org.openflexo.technologyadapter.gina.fml.FMLControlledFIBVirtualModelInstanceNature;
import org.openflexo.technologyadapter.gina.view.FMLControlledFIBFlexoConceptInstanceModuleView;
import org.openflexo.technologyadapter.gina.view.FMLControlledFIBVirtualModelInstanceModuleView;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;
import org.openflexo.xxxmodule.XXXIconLibrary;
import org.openflexo.xxxmodule.model.XXXProjectNature;
import org.openflexo.xxxmodule.view.XXXProjectNatureModuleView;
import org.openflexo.xxxmodule.widget.GenericProjectBrowser;
import org.openflexo.xxxmodule.widget.XXXProjectBrowser;

public class XXXPerspective extends FlexoPerspective {

	static final Logger logger = Logger.getLogger(XXXPerspective.class.getPackage().getName());

	private XXXProjectBrowser browser;
	private GenericProjectBrowser genericBrowser;

	/**
	 * @param controller
	 * @param name
	 */
	public XXXPerspective(FlexoController controller) {
		super("xxx_perspective", controller);

		// browser = new XXXProjectBrowser(controller);

		// setTopLeftView(browser);

		if (controller.getProject() != null) {
			setProject(controller.getProject());
		}
	}

	public Class<XXXProjectNature> getNatureClass() {
		return XXXProjectNature.class;
	}

	@Override
	public void willShow() {
		super.willShow();
		updateBrowser(getController().getProject(), false);
	}

	public void setProject(final FlexoProject<?> project) {

		updateBrowser(project, true);
	}

	public void updateBrowser(final FlexoProject<?> project, boolean rebuildBrowser) {

		System.out.println("updating browser " + browser + " with " + project);

		if (project != null) {
			if (project.hasNature(XXXProjectNature.class)) {
				if (browser == null || rebuildBrowser || browser.getDataObject().getProject() != project) {
					browser = new XXXProjectBrowser(getController());
				}
				if (browser != null) {
					browser.setRootObject(project.getNature(XXXProjectNature.class));
				}
				setTopLeftView(browser);
			}
			else {
				if (genericBrowser == null || rebuildBrowser || genericBrowser.getDataObject() != project) {
					genericBrowser = new GenericProjectBrowser(getController());
				}
				if (genericBrowser != null) {
					genericBrowser.setRootObject(project);
				}
				setTopLeftView(genericBrowser);
			}
		}
		else {
			setTopLeftView(new JPanel());
		}
	}

	public XXXProjectBrowser getBrowser() {
		return browser;
	}

	public GenericProjectBrowser getGenericBrowser() {
		return genericBrowser;
	}

	@Override
	public String getWindowTitleforObject(final FlexoObject object, final FlexoController controller) {
		if (object instanceof FlexoProject) {
			return ((FlexoProject<?>) object).getName();
		}
		else if (object instanceof XXXProjectNature) {
			if (((XXXProjectNature) object).getOwner() != null) {
				return ((XXXProjectNature) object).getOwner().getName();
			}
			return null;
		}
		else if (object instanceof FMLRTVirtualModelInstance) {
			return ((FMLRTVirtualModelInstance) object).getTitle();
		}
		else if (object instanceof FlexoConceptInstance) {
			return ((FlexoConceptInstance) object).getStringRepresentation();
		}
		else {
			return "Object has no title";
		}
	}

	@Override
	public ImageIcon getActiveIcon() {
		return XXXIconLibrary.XXX_SMALL_ICON;
	}

	@Override
	public ModuleView<?> createModuleViewForObject(FlexoObject object) {

		if (object instanceof XXXProjectNature) {
			return new XXXProjectNatureModuleView((XXXProjectNature) object, getController(), this);
		}

		if (object instanceof FMLRTVirtualModelInstance) {
			if (((FMLRTVirtualModelInstance) object).hasNature(FMLControlledFIBVirtualModelInstanceNature.INSTANCE)) {
				return new FMLControlledFIBVirtualModelInstanceModuleView((FMLRTVirtualModelInstance) object, getController(), this,
						getController().getModuleLocales());
			}
		}

		if (object instanceof FlexoConceptInstance) {
			if (((FlexoConceptInstance) object).hasNature(FMLControlledFIBFlexoConceptInstanceNature.INSTANCE)) {
				return new FMLControlledFIBFlexoConceptInstanceModuleView((FlexoConceptInstance) object, getController(), this,
						getController().getModuleLocales());
			}
		}

		// In all other cases...
		return super.createModuleViewForObject(object);

	}

	@Override
	public boolean hasModuleViewForObject(FlexoObject object) {
		if (object instanceof XXXProjectNature) {
			return true;
		}
		if (object instanceof FMLRTVirtualModelInstance) {
			if (((FMLRTVirtualModelInstance) object).hasNature(FMLControlledFIBVirtualModelInstanceNature.INSTANCE)) {
				return true;
			}
			return false;
		}
		if (object instanceof FlexoConceptInstance) {
			if (((FlexoConceptInstance) object).hasNature(FMLControlledFIBFlexoConceptInstanceNature.INSTANCE)) {
				return true;
			}
		}
		return super.hasModuleViewForObject(object);
	}

	@Override
	public FlexoObject getDefaultObject(FlexoObject proposedObject) {
		if (proposedObject instanceof FlexoProject && ((FlexoProject<?>) proposedObject).hasNature(XXXProjectNature.class)) {
			return ((FlexoProject<?>) proposedObject).getNature(XXXProjectNature.class);
		}

		return super.getDefaultObject(proposedObject);
	}

}
