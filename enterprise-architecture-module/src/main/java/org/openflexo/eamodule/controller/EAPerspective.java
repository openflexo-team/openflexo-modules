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

package org.openflexo.eamodule.controller;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.eamodule.EAMIconLibrary;
import org.openflexo.eamodule.model.EAProject;
import org.openflexo.eamodule.view.EAProjectBrowser;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.technologyadapter.diagram.controller.DiagramTechnologyAdapterController;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FMLControlledDiagramEditor;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FMLControlledDiagramModuleView;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelInstanceNature;
import org.openflexo.technologyadapter.gina.fml.FMLControlledFIBFlexoConceptInstanceNature;
import org.openflexo.technologyadapter.gina.fml.FMLControlledFIBVirtualModelInstanceNature;
import org.openflexo.technologyadapter.gina.view.FMLControlledFIBFlexoConceptInstanceModuleView;
import org.openflexo.technologyadapter.gina.view.FMLControlledFIBVirtualModelInstanceModuleView;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

public class EAPerspective extends FlexoPerspective {

	static final Logger logger = Logger.getLogger(EAPerspective.class.getPackage().getName());

	private final EAProjectBrowser browser;

	/**
	 * @param controller
	 * @param name
	 */
	public EAPerspective(FlexoController controller) {
		super("geve_perspective", controller);

		browser = new EAProjectBrowser(controller);

		setTopLeftView(browser);

		if (controller.getProject() != null) {
			setProject(controller.getProject());
		}
	}

	@Override
	public void willShow() {
		super.willShow();
		// updateBrowser(getController().getProject(), false);

		/*if (getController().getProject() != null && getController().getProject().hasNature(FormoseProjectNature.class.getName())
			&& projectBrowser != null) {
		projectBrowser.setRootObject((FormoseProject) getController().getProject().asNature(FormoseProjectNature.class.getName()));
		setTopLeftView(projectBrowser);
		}
		else if (genericBrowser != null) {
		genericBrowser.setRootObject(getController().getProject());
		setTopLeftView(genericBrowser);
		}*/
	}

	@Override
	public String getWindowTitleforObject(final FlexoObject object, final FlexoController controller) {
		if (object instanceof EAProject) {
			return ((EAProject) object).getName();
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

	public EAProject getEAProject() {
		if (getController() instanceof EAMController) {
			return ((EAMController) getController()).getEAProject();
		}
		return null;
	}

	public void setProject(final FlexoProject project) {
		// browser.setRootObject(getGEVEProject());

		System.out.println("Et hop, on set le project " + project);
		System.out.println("EAProject=" + getEAProject());

		browser.setDataObject(getEAProject());
	}

	public EAProjectBrowser getBrowser() {
		return browser;
	}

	@Override
	public ImageIcon getActiveIcon() {
		return EAMIconLibrary.EAM_SMALL_ICON;
	}

	@Override
	public ModuleView<?> createModuleViewForObject(FlexoObject object) {

		/*if (object instanceof GEVEProject) {
			return new FMLControlledFIBVirtualModelInstanceModuleView(((GEVEProject) object).getGEVEView(), getController(), this,
					getController().getModuleLocales());
		}*/

		if (object instanceof FMLRTVirtualModelInstance) {

			FMLRTVirtualModelInstance vmi = (FMLRTVirtualModelInstance) object;

			if (vmi.hasNature(FMLControlledDiagramVirtualModelInstanceNature.INSTANCE)) {
				DiagramTechnologyAdapterController diagramTAC = ((EAMController) getController()).getDiagramTAC();
				FMLControlledDiagramEditor editor = new FMLControlledDiagramEditor(vmi, false, getController(),
						diagramTAC.getToolFactory());
				return new FMLControlledDiagramModuleView(editor, this);
			}

			if (vmi.hasNature(FMLControlledFIBVirtualModelInstanceNature.INSTANCE)) {
				return new FMLControlledFIBVirtualModelInstanceModuleView(vmi, getController(), this, getController().getModuleLocales());
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
		if (object instanceof FMLRTVirtualModelInstance) {
			if (((FMLRTVirtualModelInstance) object).hasNature(FMLControlledDiagramVirtualModelInstanceNature.INSTANCE)) {
				return true;
			}
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

}
