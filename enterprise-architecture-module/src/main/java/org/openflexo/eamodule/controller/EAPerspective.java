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
import org.openflexo.eamodule.EAModule;
import org.openflexo.eamodule.model.BPMNVirtualModelInstance;
import org.openflexo.eamodule.model.EAProjectNature;
import org.openflexo.eamodule.view.ConvertToEAMProjectView;
import org.openflexo.eamodule.view.EAMProjectNatureModuleView;
import org.openflexo.eamodule.view.EAMWelcomePanelModuleView;
import org.openflexo.eamodule.widget.FIBEAMProjectBrowser;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.module.FlexoModule.WelcomePanel;
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
import org.openflexo.view.controller.model.NaturePerspective;

public class EAPerspective extends NaturePerspective<EAProjectNature> {

	static final Logger logger = Logger.getLogger(EAPerspective.class.getPackage().getName());

	private final FIBEAMProjectBrowser browser;

	/**
	 * @param controller
	 * @param name
	 */
	public EAPerspective(EAMController controller) {
		super("geve_perspective", controller);

		browser = new FIBEAMProjectBrowser(controller.getProject(), controller);

		setTopLeftView(browser);

		if (controller.getProject() != null) {
			setProject(controller.getProject());
		}
	}

	@Override
	public Class<EAProjectNature> getNatureClass() {
		return EAProjectNature.class;
	}

	@Override
	public ImageIcon getActiveIcon() {
		return EAMIconLibrary.EAM_SMALL_ICON;
	}

	public void setProject(FlexoProject<?> project) {
		/*if (project.hasNature(FreeModellingProjectNature.class)) {
			freeModellingProjectBrowser.setRootObject(project.getNature(FreeModellingProjectNature.class));
		}*/

		browser.setRootObject(project);
	}
	
	@Override
	public boolean isRepresentableInModuleView(FlexoObject object) {
		if (object instanceof EAProjectNature) {
			return true;
		}
		if (object instanceof BPMNVirtualModelInstance) {
			return true;
		}
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
			if (((FlexoConceptInstance) object).getFlexoConcept().getName().equals("Process")) {
				FMLRTVirtualModelInstance processDiagram = getProcessDiagram((FlexoConceptInstance) object);
				if (processDiagram != null) {
					return true;
				}
			}
		}
		return super.isRepresentableInModuleView(object);
	}

	@Override
	public FlexoObject getRepresentableMasterObject(FlexoObject object) {
		if (object instanceof EAProjectNature) {
			return object;
		}
		if (object instanceof BPMNVirtualModelInstance) {
			return object;
		}
		if (object instanceof FMLRTVirtualModelInstance) {
			if (((FMLRTVirtualModelInstance) object).hasNature(FMLControlledDiagramVirtualModelInstanceNature.INSTANCE)) {
				return object;
			}
			if (((FMLRTVirtualModelInstance) object).hasNature(FMLControlledFIBVirtualModelInstanceNature.INSTANCE)) {
				return object;
			}
			return null;
		}
		if (object instanceof FlexoConceptInstance) {
			if (((FlexoConceptInstance) object).hasNature(FMLControlledFIBFlexoConceptInstanceNature.INSTANCE)) {
				return object;
			}
			if (((FlexoConceptInstance) object).getFlexoConcept().getName().equals("Process")) {
				FMLRTVirtualModelInstance processDiagram = getProcessDiagram((FlexoConceptInstance) object);
				if (processDiagram != null) {
					return object;
				}
			}
		}
		return super.getRepresentableMasterObject(object);
	}
	
	public FMLRTVirtualModelInstance getProcessDiagram(FlexoConceptInstance process) {
		try {
			return (FMLRTVirtualModelInstance) process.execute("container.container.getProcessDiagram(this)");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getWindowTitleforObject(FlexoObject object, FlexoController controller) {
		if (object instanceof WelcomePanel) {
			return "Welcome";
		}
		if (object instanceof FlexoProject) {
			return ((FlexoProject<?>) object).getName();
		}
		if (object instanceof EAProjectNature) {
			return ((EAProjectNature) object).getOwner().getName();
		}
		if (object instanceof BPMNVirtualModelInstance) {
			return ((BPMNVirtualModelInstance) object).getName();
		}
		if (object instanceof FlexoConceptInstance && ((FlexoConceptInstance) object).getFlexoConcept().getName().equals("Process")) {
			try {
				return (String) ((FlexoConceptInstance) object).execute("name");
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		if (object != null) {
			return object.toString();
		}
		return "null";
	}

	@Override
	public FlexoObject getDefaultObject(FlexoObject proposedObject) {
		if (proposedObject instanceof FlexoProject && ((FlexoProject<?>) proposedObject).hasNature(EAProjectNature.class)) {
			return ((FlexoProject<?>) proposedObject).getNature(EAProjectNature.class);
		}

		return super.getDefaultObject(proposedObject);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ModuleView<?> createModuleViewForMasterObject(FlexoObject object) {
		if (object instanceof WelcomePanel) {
			return new EAMWelcomePanelModuleView((WelcomePanel<EAModule>) object, getController(), this);
		}
		if (object instanceof FlexoProject) {
			return new ConvertToEAMProjectView((FlexoProject<?>) object, getController(), this);
		}
		if (object instanceof EAProjectNature) {
			return new EAMProjectNatureModuleView((EAProjectNature) object, getController(), this);
		}
		if (object instanceof BPMNVirtualModelInstance) {
			FMLRTVirtualModelInstance vmi = ((BPMNVirtualModelInstance) object).getAccessedVirtualModelInstance();
			if (vmi != null && vmi.hasNature(FMLControlledFIBVirtualModelInstanceNature.INSTANCE)) {
				return new FMLControlledFIBVirtualModelInstanceModuleView(vmi, getController(), this, getController().getModuleLocales());
			}
		}
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
			if (((FlexoConceptInstance) object).getFlexoConcept().getName().equals("Process")) {
				FMLRTVirtualModelInstance processDiagram = getProcessDiagram((FlexoConceptInstance) object);
				if (processDiagram != null) {
					DiagramTechnologyAdapterController diagramTAC = ((EAMController) getController()).getDiagramTAC();
					FMLControlledDiagramEditor editor = new FMLControlledDiagramEditor(processDiagram, false, getController(),
							diagramTAC.getToolFactory());
					return new FMLControlledDiagramModuleView(editor, this);
				}
			}
		}

		return super.createModuleViewForMasterObject(object);
	}

}
