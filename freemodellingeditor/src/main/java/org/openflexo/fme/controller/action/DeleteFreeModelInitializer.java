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

package org.openflexo.fme.controller.action;

import java.util.EventObject;
import java.util.logging.Logger;

import javax.swing.Icon;

import org.openflexo.fme.controller.FMEController;
import org.openflexo.fme.model.FMEFreeModel;
import org.openflexo.fme.model.action.DeleteFreeModel;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.action.FlexoActionFinalizer;
import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.icon.IconLibrary;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;

public class DeleteFreeModelInitializer extends ActionInitializer<DeleteFreeModel, FMEFreeModel, FlexoObject> {

	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	DeleteFreeModelInitializer(FMEControllerActionInitializer actionInitializer) {
		super(DeleteFreeModel.actionType, actionInitializer);
	}

	@Override
	protected FMEControllerActionInitializer getControllerActionInitializer() {
		return (FMEControllerActionInitializer) super.getControllerActionInitializer();
	}

	@Override
	protected FlexoActionInitializer<DeleteFreeModel> getDefaultInitializer() {
		return new FlexoActionInitializer<DeleteFreeModel>() {
			@Override
			public boolean run(EventObject e, DeleteFreeModel action) {
				return FlexoController.confirm(action.getLocales().localizedForKey("would_you_really_like_to_delete_this_free_model"));
			}
		};
	}

	@Override
	protected FlexoActionFinalizer<DeleteFreeModel> getDefaultFinalizer() {
		return new FlexoActionFinalizer<DeleteFreeModel>() {
			@Override
			public boolean run(EventObject e, DeleteFreeModel action) {
				FMEController fmeController = (FMEController) getController();
				if (action.getFocusedObject().getNature().getFreeModels().size() > 0) {
					fmeController.selectAndFocusObject(action.getFocusedObject().getNature().getFreeModels().get(0));
				}
				else {
					fmeController.selectAndFocusObject(action.getFocusedObject().getNature());
					// fmeController.FREE_MODELLING_PERSPECTIVE.closeFreeModelBrowsers();
				}
				return true;
			}
		};
	}

	@Override
	protected Icon getEnabledIcon(FlexoActionFactory actionType) {
		return IconLibrary.DELETE_ICON;
	}

}
