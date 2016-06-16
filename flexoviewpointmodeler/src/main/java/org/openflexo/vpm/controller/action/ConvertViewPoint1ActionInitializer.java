/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Flexoviewpointmodeler, a component of the software infrastructure 
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

package org.openflexo.vpm.controller.action;

import java.io.FileNotFoundException;
import java.util.EventObject;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.action.FlexoActionFinalizer;
import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.foundation.fml.ViewPoint;
import org.openflexo.foundation.fml.action.ConvertViewPoint1Action;
import org.openflexo.foundation.fml.rm.ViewPointResource;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;

public class ConvertViewPoint1ActionInitializer extends ActionInitializer<ConvertViewPoint1Action, FlexoObject, FlexoObject> {

	private static final java.util.logging.Logger logger = org.openflexo.logging.FlexoLogger
			.getLogger(ConvertViewPoint1ActionInitializer.class.getPackage().getName());

	public ConvertViewPoint1ActionInitializer(ControllerActionInitializer actionInitializer) {
		super(ConvertViewPoint1Action.actionType, actionInitializer);
	}

	@Override
	protected FlexoActionInitializer<ConvertViewPoint1Action> getDefaultInitializer() {
		return new FlexoActionInitializer<ConvertViewPoint1Action>() {
			@Override
			public boolean run(EventObject e, ConvertViewPoint1Action action) {
				return FlexoController
						.confirm(action.getLocales().localizedForKey("would_you_really_like_to_convert_viewpoint_(undoable_action)?"));
			}
		};
	}

	@Override
	protected FlexoActionFinalizer<ConvertViewPoint1Action> getDefaultFinalizer() {
		return new FlexoActionFinalizer<ConvertViewPoint1Action>() {
			@Override
			public boolean run(EventObject e, ConvertViewPoint1Action action) {
				try {
					ViewPoint loadedData = ((ViewPointResource) action.getFocusedObject()).getResourceData(null);
					getController().setCurrentEditedObjectAsModuleView(loadedData);
					getController().getSelectionManager().setSelectedObject(loadedData);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ResourceLoadingCancelledException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (FlexoException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return true;
			}
		};
	}

}
