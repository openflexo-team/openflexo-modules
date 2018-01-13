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

package org.openflexo.eamodule;

import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.eamodule.controller.EAMController;
import org.openflexo.foundation.fml.FMLTechnologyAdapter;
import org.openflexo.foundation.fml.rt.FMLRTTechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterService;
import org.openflexo.module.FlexoModule;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.view.controller.FlexoController;

/**
 * {@link EnterpriseArchitectureModule} module
 * 
 * @author sylvain
 */
public class EAModule extends FlexoModule<EAModule> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(EAModule.class.getPackage().getName());

	public static final String EAM_MODULE_SHORT_NAME = "EAM";
	public static final String EAM_MODULE_NAME = "Entreprise Architecture Editor";

	public EAModule(ApplicationContext applicationContext) throws Exception {
		super(applicationContext);
	}

	@Override
	public String getLocalizationDirectory() {
		return "FlexoLocalization/EAModule";
	}

	@Override
	protected FlexoController createControllerForModule() {
		return new EAMController(this);
	}

	@Override
	public EnterpriseArchitectureModule getModule() {
		return getApplicationContext().getModuleLoader().getModule(EnterpriseArchitectureModule.class);
	}

	public EAMController getEAMController() {
		return (EAMController) getFlexoController();
	}

	@Override
	public EAMPreferences getPreferences() {
		return (EAMPreferences) super.getPreferences();
	}

	@Override
	public void initModule() {
		super.initModule();
		TechnologyAdapterService taService = getApplicationContext().getTechnologyAdapterService();
		taService.activateTechnologyAdapter(taService.getTechnologyAdapter(FMLTechnologyAdapter.class), true);
		taService.activateTechnologyAdapter(taService.getTechnologyAdapter(FMLRTTechnologyAdapter.class), true);
		taService.activateTechnologyAdapter(taService.getTechnologyAdapter(DiagramTechnologyAdapter.class), true);
	}

	@Override
	public boolean close() {
		if (getApplicationContext().getResourceManager().getUnsavedResources().size() == 0) {
			return super.close();
		}
		else {
			if (getEAMController().reviewModifiedResources()) {
				return super.close();
			}
			else {
				return false;
			}
		}
	}

}
