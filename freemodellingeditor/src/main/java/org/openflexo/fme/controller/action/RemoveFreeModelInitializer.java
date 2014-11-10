/*
 * (c) Copyright 2010-2011 AgileBirds
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
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
