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
import org.openflexo.fme.model.action.DropShape;
import org.openflexo.foundation.view.FlexoConceptInstance;
import org.openflexo.foundation.view.VirtualModelInstance;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.ContextualPalette;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FMLControlledDiagramEditor;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelNature;
import org.openflexo.technologyadapter.diagram.fml.FMLDiagramPaletteElementBinding;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteElement;
import org.openflexo.technologyadapter.diagram.model.DiagramContainerElement;

public class ConceptsPalette extends ContextualPalette implements PropertyChangeListener {

	private static final Logger logger = Logger.getLogger(ConceptsPalette.class.getPackage().getName());

	public ConceptsPalette(DiagramPalette diagramPalette, FreeModelDiagramEditor editor) {
		super(diagramPalette, editor);
	}

	@Override
	public void delete() {
		super.delete();
	}

	@Override
	public FreeModelDiagramEditor getEditor() {
		return (FreeModelDiagramEditor) super.getEditor();
	}

	@Override
	public boolean handleFMLControlledDrop(DrawingTreeNode<?, ?> target, DiagramPaletteElement paletteElement, FGEPoint dropLocation,
			FMLControlledDiagramEditor editor) {
		System.out.println("handleFMLControlledDrop in FME !!!");

		if (getEditor() == null) {
			return false;
		}

		DiagramContainerElement<?> rootContainer = (DiagramContainerElement<?>) target.getDrawable();
		VirtualModelInstance vmi = editor.getVirtualModelInstance();
		TypedDiagramModelSlot ms = FMLControlledDiagramVirtualModelNature.getTypedDiagramModelSlot(vmi.getVirtualModel());
		System.out.println("ms=" + ms);
		System.out.println("bindings=" + ms.getPaletteElementBindings());
		FMLDiagramPaletteElementBinding binding = ms.getPaletteElementBinding(paletteElement);
		System.out.println("binding=" + binding);
		DropScheme dropScheme = binding.getDropScheme();
		System.out.println("dropScheme=" + dropScheme);

		DropShape action = DropShape.actionType.makeNewAction(rootContainer, null, getEditor().getFlexoController().getEditor());
		action.setFreeModel(getEditor().getFreeModel());
		action.setConcept(binding.getFlexoConcept());
		action.setDropLocation(dropLocation);

		action.doAction();

		FlexoConceptInstance newFlexoConceptInstance = action.getNewFlexoConceptInstance();
		// DiagramShape shape = newFlexoConceptInstance.getFlexoActor(patternRole)

		System.out.println("Created newFlexoConceptInstance:" + newFlexoConceptInstance);
		// System.out.println("location=" + newShape.getGraphicalRepresentation().getLocation());
		// System.out.println("size=" + newShape.getGraphicalRepresentation().getSize());

		// getEditor().setCurrentTool(EditorTool.SelectionTool);

		// getEditor().setSelectedObject(getEditor().getDrawing().getDrawingTreeNode(newShape));

		return action.hasActionExecutionSucceeded();

	}

}
