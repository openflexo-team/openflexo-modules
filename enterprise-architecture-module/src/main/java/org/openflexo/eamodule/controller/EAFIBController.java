/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Freemodellingeditor, a component of the software infrastructure 
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

import java.util.List;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.eamodule.EAModule;
import org.openflexo.eamodule.model.EAProjectNature;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.gina.model.FIBComponent;
import org.openflexo.gina.swing.view.SwingViewFactory;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.localization.LocalizedDelegate;
import org.openflexo.module.ModuleLoadingException;
import org.openflexo.technologyadapter.gina.controller.FMLControlledFIBController;

/**
 * Represents the controller of a FIBComponent in Enterprise Architecture Module
 * 
 * 
 * @author sylvain
 * 
 * @param <T>
 */
public class EAFIBController extends FMLControlledFIBController {

	private static final Logger logger = Logger.getLogger(EAFIBController.class.getPackage().getName());

	public EAFIBController(FIBComponent component) {
		super(component, SwingViewFactory.INSTANCE);
		// Default parent localizer is the main localizer
		setParentLocalizer(FlexoLocalization.getMainLocalizer());
	}

	@Override
	public EAMController getFlexoController() {
		return (EAMController) super.getFlexoController();
	}

	public final LocalizedDelegate getLocales() {
		try {
			return getServiceManager().getModuleLoader().getModuleInstance(EAModule.class).getLocales();
		} catch (ModuleLoadingException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public ImageIcon retrieveIconForObject(Object object) {
		if (getFlexoController() != null) {
			return getFlexoController().iconForObject(object);
		}
		return super.retrieveIconForObject(object);
	}

	public EAProjectNature getEAProjectNature() {
		if (getEditor() != null) {
			FlexoProject<?> project = getEditor().getProject();
			if (project != null)
				return project.getNature(EAProjectNature.class);
		}
		return null;
	}

	public String getProcessName(FlexoConceptInstance process) {
		try {
			return (String) process.execute("name");
		} catch (Exception e) {
			e.printStackTrace();
			return "???";
		}
	}

	public List<FlexoConceptInstance> getChildProcesses(FlexoConceptInstance process) {
		try {
			return (List<FlexoConceptInstance>) process.execute("childProcesses");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public FMLRTVirtualModelInstance getProcessDiagram(FlexoConceptInstance process) {
		try {
			return (FMLRTVirtualModelInstance) process.execute("container.container.getProcessDiagram(this)");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
