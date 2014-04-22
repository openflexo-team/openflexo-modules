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

import java.util.EventObject;
import java.util.logging.Logger;

import javax.swing.Icon;

import org.openflexo.fme.FMEIconLibrary;
import org.openflexo.fme.controller.FMEPerspective;
import org.openflexo.fme.model.action.ConvertToFreeModellingEditorProject;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.action.FlexoActionFinalizer;
import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;

public class ConvertToFreeModellingEditorProjectInitializer extends
		ActionInitializer<ConvertToFreeModellingEditorProject, FlexoProject, FlexoObject> {

	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	ConvertToFreeModellingEditorProjectInitializer(FMEControllerActionInitializer actionInitializer) {
		super(ConvertToFreeModellingEditorProject.actionType, actionInitializer);
	}

	@Override
	protected FMEControllerActionInitializer getControllerActionInitializer() {
		return (FMEControllerActionInitializer) super.getControllerActionInitializer();
	}

	@Override
	protected FlexoActionInitializer<ConvertToFreeModellingEditorProject> getDefaultInitializer() {
		return new FlexoActionInitializer<ConvertToFreeModellingEditorProject>() {
			@Override
			public boolean run(EventObject e, ConvertToFreeModellingEditorProject action) {
				return true;
			}
		};
	}

	@Override
	protected FlexoActionFinalizer<ConvertToFreeModellingEditorProject> getDefaultFinalizer() {
		return new FlexoActionFinalizer<ConvertToFreeModellingEditorProject>() {
			@Override
			public boolean run(EventObject e, ConvertToFreeModellingEditorProject action) {
				if (getController().getCurrentPerspective() instanceof FMEPerspective) {
					((FMEPerspective) getController().getCurrentPerspective()).setProject(action.getFocusedObject());
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
