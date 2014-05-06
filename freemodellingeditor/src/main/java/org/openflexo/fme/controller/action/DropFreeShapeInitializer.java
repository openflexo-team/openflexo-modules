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

import org.openflexo.fme.model.action.DropShape;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.action.FlexoActionFinalizer;
import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.technologyadapter.diagram.model.DiagramContainerElement;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;

public class DropFreeShapeInitializer extends ActionInitializer<DropShape, DiagramContainerElement<?>, FlexoObject> {

	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	public DropFreeShapeInitializer(ControllerActionInitializer actionInitializer) {
		super(DropShape.actionType, actionInitializer);
	}

	@Override
	protected FlexoActionInitializer<DropShape> getDefaultInitializer() {
		return new FlexoActionInitializer<DropShape>() {
			@Override
			public boolean run(EventObject e, DropShape action) {
				logger.info("DropShape initializer");
				return true;
			}
		};
	}

	@Override
	protected FlexoActionFinalizer<DropShape> getDefaultFinalizer() {
		return new FlexoActionFinalizer<DropShape>() {
			@Override
			public boolean run(EventObject e, DropShape action) {
				logger.info("DropShape finalizer");
				getController().selectAndFocusObject(action.getNewFlexoConceptInstance());
				return true;
			}
		};
	}

}
