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
package org.openflexo.fme.controller;

import java.util.logging.Logger;

import org.openflexo.fib.model.FIBComponent;
import org.openflexo.fme.controller.editor.FreeModelDiagramEditor;
import org.openflexo.fme.view.FreeModelModuleView;
import org.openflexo.foundation.viewpoint.FlexoConcept;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.view.controller.FlexoFIBController;

/**
 * Represents the controller of a FIBComponent in FME graphical context
 * 
 * 
 * @author sylvain
 * 
 * @param <T>
 */
public class FMEFIBController extends FlexoFIBController {

	private static final Logger logger = Logger.getLogger(FMEFIBController.class.getPackage().getName());

	public FMEFIBController(FIBComponent component) {
		super(component);
		// Default parent localizer is the main localizer
		setParentLocalizer(FlexoLocalization.getMainLocalizer());
	}

	@Override
	public FMEController getFlexoController() {
		return (FMEController) super.getFlexoController();
	}

	public FreeModelDiagramEditor getDiagramEditor() {
		if (getFlexoController().getCurrentModuleView() instanceof FreeModelModuleView) {
			return ((FreeModelModuleView) getFlexoController().getCurrentModuleView()).getEditor();
		}
		return null;
	}

	public void createConcept(String name) {
		// getDiagramEditor().createNewConcept(name);
	}

	public void removeConcept(FlexoConcept concept) {
		// getDiagramEditor().removeConcept(concept);
	}

	public void renameConcept(FlexoConcept concept) {
		// getDiagramEditor().renameConcept(concept);
	}

	/*public Icon getIcon(FMEModelObject object) {
		if (object instanceof Concept) {
			return FMEIconLibrary.CONCEPT_ICON;
		} else if (object instanceof Instance) {
			return FMEIconLibrary.INSTANCE_ICON;
		}
		return null;
	}*/

}
