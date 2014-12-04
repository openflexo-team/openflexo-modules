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

import java.util.logging.Logger;

import org.openflexo.fme.controller.FMEController;
import org.openflexo.selection.SelectionManager;
import org.openflexo.view.controller.ControllerActionInitializer;

/**
 * 
 * Action initializing for this module
 * 
 * @author yourname
 */
public class FMEControllerActionInitializer extends ControllerActionInitializer {

	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	public FMEControllerActionInitializer(FMEController controller) {
		super(controller);
	}

	protected FMEController getFMEController() {
		return (FMEController) getController();
	}

	protected SelectionManager getFMESelectionManager() {
		return getFMEController().getSelectionManager();
	}

	@Override
	public void initializeActions() {
		super.initializeActions();

		new ConvertToFreeModellingEditorProjectInitializer(this);

		new CreateFreeModelInitializer(this);
		new CreateFreeModelFromPPTInitializer(this);
		new RemoveFreeModelInitializer(this);
		new DropFreeShapeInitializer(this);
		new CreateNewConceptInitializer(this);
		new CreateNewConceptFromNoneInitializer(this);
		new DeclareInstanceOfExistingConceptInitializer(this);
		new CreateFreeModelDiagramInitializer(this);
		new CreateFreeModelDiagramFromPPTInitializer(this);
		
		// Actions applied on Diagram Elements(not currently associated with flexo concept)
		new CreateNewConceptFromDiagramElementInitializer(this);
		new DeclareInstanceOfExistingConceptFromDiagramElementInitializer(this);

		new DeleteFMEFlexoConceptInstanceActionInitializer(this);
	}

}
