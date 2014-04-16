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
package org.openflexo.fme.controller.editor;

import java.util.logging.Logger;

import org.openflexo.fge.swing.control.SwingToolFactory;
import org.openflexo.fme.model.FreeModel;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.AbstractDiagramPalette;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.DiagramEditor;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FMLControlledDiagramDrawing;
import org.openflexo.view.controller.FlexoController;

/**
 * Editor of a FreeModel diagram<br>
 * There is a FreeMetaModel who has a VirtualModel controlling the edition of the diagram in FML-controlled mode
 * 
 * @author sylvain
 * 
 */
public class FreeModelDiagramEditor extends DiagramEditor {

	private static final Logger logger = Logger.getLogger(FreeModelDiagramEditor.class.getPackage().getName());

	private final FreeModel freeModel;

	public FreeModelDiagramEditor(FreeModel freeModel, boolean readOnly, FlexoController controller, SwingToolFactory swingToolFactory) {
		super(new FMLControlledDiagramDrawing(freeModel.getVirtualModelInstance(), readOnly), readOnly, controller, swingToolFactory);
		this.freeModel = freeModel;
	}

	public FreeModel getFreeModel() {
		return freeModel;
	}

	@Override
	public AbstractDiagramPalette makeCommonPalette() {
		return new FreeShapesPalette(this);
	}
}
