/**
 * 
 * Copyright (c) 2014-2015, Openflexo
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

package org.openflexo.eamodule.model.action;

import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.action.ModuleSpecificFlexoAction;
import org.openflexo.eamodule.EAModule;
import org.openflexo.eamodule.EnterpriseArchitectureModule;
import org.openflexo.eamodule.model.EAProjectNature;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.nature.GivesNatureAction;
import org.openflexo.localization.LocalizedDelegate;

/**
 * This action is called to gives Enterprise Architecture nature to a project
 * 
 * @author sylvain
 */
public class GivesEANature extends GivesNatureAction<GivesEANature, EAProjectNature> implements ModuleSpecificFlexoAction<EAModule> {

	private static final Logger logger = Logger.getLogger(GivesEANature.class.getPackage().getName());

	public static FlexoActionFactory<GivesEANature, FlexoProject<?>, FlexoObject> actionType = new FlexoActionFactory<GivesEANature, FlexoProject<?>, FlexoObject>(
			"gives_enterprise_architecture_nature") {

		/**
		 * Factory method
		 */
		@Override
		public GivesEANature makeNewAction(FlexoProject<?> focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
			return new GivesEANature(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(FlexoProject<?> project, Vector<FlexoObject> globalSelection) {
			return true;
		}

		@Override
		public boolean isEnabledForSelection(FlexoProject<?> project, Vector<FlexoObject> globalSelection) {
			return project != null && !project.hasNature(EAProjectNature.class);
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(GivesEANature.actionType, FlexoProject.class);
	}

	GivesEANature(FlexoProject<?> focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	public LocalizedDelegate getLocales() {
		if (getServiceManager() instanceof ApplicationContext) {
			return ((ApplicationContext) getServiceManager()).getModuleLoader().getModule(EnterpriseArchitectureModule.class)
					.getLoadedModuleInstance().getLocales();
		}
		return super.getLocales();
	}

	@Override
	public Class<EAModule> getFlexoModuleClass() {
		return EAModule.class;
	}

	@Override
	public EAProjectNature makeNewNature() {

		EAProjectNature nature = getFocusedObject().getModelFactory().newInstance(EAProjectNature.class);

		return nature;
	}

}
