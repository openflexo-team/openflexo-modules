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

import java.beans.PropertyChangeListener;
import java.util.logging.Logger;

import org.openflexo.fge.Drawing.DrawingTreeNode;
import org.openflexo.fge.geom.FGEPoint;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.ContextualPalette;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FMLControlledDiagramEditor;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteElement;

public class FreeModelContextualPalette extends ContextualPalette implements PropertyChangeListener {

	private static final Logger logger = Logger.getLogger(FreeModelContextualPalette.class.getPackage().getName());

	public FreeModelContextualPalette(DiagramPalette diagramPalette, FreeModelDiagramEditor editor) {
		super(diagramPalette, editor);
	}

	@Override
	public void delete() {
		super.delete();
	}

	@Override
	public boolean handleFMLControlledDrop(DrawingTreeNode<?, ?> target, DiagramPaletteElement paletteElement, FGEPoint dropLocation,
			FMLControlledDiagramEditor editor) {
		System.out.println("Yes, on est bien dans FME !!!");
		return super.handleFMLControlledDrop(target, paletteElement, dropLocation, editor);
	}

}
