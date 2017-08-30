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

package org.openflexo.fme.model.action;

import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.fme.FreeModellingEditor;
import org.openflexo.fme.model.FreeModellingProjectNature;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.localization.LocalizedDelegate;

/**
 * This action is used to gives {@link FreeModellingProjectNature} to a {@link FlexoProject}<br>
 * 
 * 
 * @author sylvain
 * 
 */
public class ConvertToFreeModellingEditorProject extends FlexoAction<ConvertToFreeModellingEditorProject, FlexoProject, FlexoObject> {

	private static final Logger logger = Logger.getLogger(ConvertToFreeModellingEditorProject.class.getPackage().getName());

	public static FlexoActionFactory<ConvertToFreeModellingEditorProject, FlexoProject, FlexoObject> actionType = new FlexoActionFactory<ConvertToFreeModellingEditorProject, FlexoProject, FlexoObject>(
			"free_modelling_project", FlexoActionFactory.convertMenu, FlexoActionFactory.defaultGroup, FlexoActionFactory.NORMAL_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public ConvertToFreeModellingEditorProject makeNewAction(FlexoProject focusedObject, Vector<FlexoObject> globalSelection,
				FlexoEditor editor) {
			return new ConvertToFreeModellingEditorProject(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(FlexoProject project, Vector<FlexoObject> globalSelection) {
			FreeModellingProjectNature nature = project.getServiceManager().getProjectNatureService()
					.getProjectNature(FreeModellingProjectNature.class);
			return !project.hasNature(nature);
		}

		@Override
		public boolean isEnabledForSelection(FlexoProject project, Vector<FlexoObject> globalSelection) {
			return isVisibleForSelection(project, globalSelection);
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(ConvertToFreeModellingEditorProject.actionType, FlexoProject.class);
	}

	ConvertToFreeModellingEditorProject(FlexoProject focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	public LocalizedDelegate getLocales() {
		if (getServiceManager() instanceof ApplicationContext) {
			return ((ApplicationContext) getServiceManager()).getModuleLoader().getModule(FreeModellingEditor.class)
					.getLoadedModuleInstance().getLocales();
		}
		return super.getLocales();
	}

	@Override
	protected void doAction(Object context) throws SaveResourceException {

		logger.info("Convert to FreeModellingProject");

		getFreeModellingProjectNature().givesNature(getFocusedObject(), getEditor());

	}

	public FreeModellingProjectNature getFreeModellingProjectNature() {
		return getServiceManager().getProjectNatureService().getProjectNature(FreeModellingProjectNature.class);
	}

}
