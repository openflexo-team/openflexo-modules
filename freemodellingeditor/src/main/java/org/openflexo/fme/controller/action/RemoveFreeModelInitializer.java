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

import java.io.ObjectInputStream.GetField;
import java.util.EventObject;
import java.util.logging.Logger;

import javax.swing.Icon;

import org.openflexo.fme.FMEIconLibrary;
import org.openflexo.fme.FreeModellingEditor;
import org.openflexo.fme.controller.FMEController;
import org.openflexo.fme.model.FreeModel;
import org.openflexo.fme.model.action.RemoveFreeModel;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.action.FlexoActionFinalizer;
import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;

public class RemoveFreeModelInitializer extends ActionInitializer<RemoveFreeModel, FreeModel, FlexoObject> {

	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	RemoveFreeModelInitializer(FMEControllerActionInitializer actionInitializer) {
		super(RemoveFreeModel.actionType, actionInitializer);
	}

	@Override
	protected FMEControllerActionInitializer getControllerActionInitializer() {
		return (FMEControllerActionInitializer) super.getControllerActionInitializer();
	}

	@Override
	protected FlexoActionInitializer<RemoveFreeModel> getDefaultInitializer() {
		return new FlexoActionInitializer<RemoveFreeModel>() {
			@Override
			public boolean run(EventObject e, RemoveFreeModel action) {
				return true;
			}
		};
	}

	@Override
	protected FlexoActionFinalizer<RemoveFreeModel> getDefaultFinalizer() {
		return new FlexoActionFinalizer<RemoveFreeModel>() {
			@Override
			public boolean run(EventObject e, RemoveFreeModel action) {
				FMEController fmeController = (FMEController)getController();
				if(action.getFocusedObject().getFreeModellingProject().getFreeModels().size()>0){
					fmeController.selectAndFocusObject(action.getFocusedObject().getFreeModellingProject().getFreeModels().get(0));
				}else{
					fmeController.selectAndFocusObject(action.getFocusedObject().getFreeModellingProject());
					fmeController.FREE_MODELLING_PERSPECTIVE.closeFreeModelBrowsers();
				}
				return true;
			}
		};
	}

	@Override
	protected Icon getEnabledIcon() {
		return FMEIconLibrary.FME_SMALL_ICON;
	}

}
