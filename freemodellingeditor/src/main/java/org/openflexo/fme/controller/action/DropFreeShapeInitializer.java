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

import org.openflexo.fme.model.action.DropFreeShape;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.action.FlexoActionFinalizer;
import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.technologyadapter.diagram.model.DiagramContainerElement;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;

public class DropFreeShapeInitializer extends ActionInitializer<DropFreeShape, DiagramContainerElement<?>, FlexoObject> {

	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	public DropFreeShapeInitializer(ControllerActionInitializer actionInitializer) {
		super(DropFreeShape.actionType, actionInitializer);
	}

	@Override
	protected FlexoActionInitializer<DropFreeShape> getDefaultInitializer() {
		return new FlexoActionInitializer<DropFreeShape>() {
			@Override
			public boolean run(EventObject e, DropFreeShape action) {
				logger.info("DropFreeShape initializer");
				return true;
			}
		};
	}

	@Override
	protected FlexoActionFinalizer<DropFreeShape> getDefaultFinalizer() {
		return new FlexoActionFinalizer<DropFreeShape>() {
			@Override
			public boolean run(EventObject e, DropFreeShape action) {
				logger.info("DropFreeShape finalizer");
				return true;
			}
		};
	}

}
