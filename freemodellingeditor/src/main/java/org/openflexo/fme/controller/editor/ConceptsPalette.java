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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;

import org.openflexo.fge.Drawing.DrawingTreeNode;
import org.openflexo.fge.geom.FGEPoint;
import org.openflexo.fme.controller.editor.DynamicPalette.GraphicalRepresentationSet;
import org.openflexo.fme.model.FreeMetaModel;
import org.openflexo.fme.model.action.DropShape;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.ContextualPalette;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.DiagramEditor;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FMLControlledDiagramEditor;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelNature;
import org.openflexo.technologyadapter.diagram.fml.FMLDiagramPaletteElementBinding;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
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

		if (getEditor() == null) {
			return false;
		}

		DiagramContainerElement<?> rootContainer = (DiagramContainerElement<?>) target.getDrawable();
		VirtualModelInstance vmi = editor.getVirtualModelInstance();
		TypedDiagramModelSlot ms = FMLControlledDiagramVirtualModelNature.getTypedDiagramModelSlot(vmi.getVirtualModel());
		FMLDiagramPaletteElementBinding binding = ms.getPaletteElementBinding(paletteElement);
		// DropScheme dropScheme = binding.getDropScheme();

		DropShape action = DropShape.actionType.makeNewAction(rootContainer, null, getEditor().getFlexoController().getEditor());
		action.setFreeModel(getEditor().getFreeModel());
		action.setConcept(binding.getBoundFlexoConcept());
		action.setDropLocation(dropLocation);

		action.doAction();

		// FlexoConceptInstance newFlexoConceptInstance = action.getNewFlexoConceptInstance();
		// System.out.println("Created newFlexoConceptInstance:" + newFlexoConceptInstance);

		return action.hasActionExecutionSucceeded();

	}

	@Override
	protected ContextualPaletteElement makePaletteElement(final DiagramPaletteElement element, DiagramEditor editor) {
		if (editor instanceof FMLControlledDiagramEditor) {
			VirtualModelInstance vmi = ((FMLControlledDiagramEditor) editor).getVirtualModelInstance();
			TypedDiagramModelSlot ms = FMLControlledDiagramVirtualModelNature.getTypedDiagramModelSlot(vmi.getVirtualModel());
			if (ms != null) {
				FMLDiagramPaletteElementBinding binding = ms.getPaletteElementBinding(element);
				if (binding != null) {
					FlexoConcept concept = binding.getBoundFlexoConcept();
					final ShapeRole conceptShapeRole = (ShapeRole) concept.getFlexoRole(FreeMetaModel.SHAPE_ROLE_NAME);
					if (conceptShapeRole != null) {
						conceptShapeRole.getGraphicalRepresentation().getPropertyChangeSupport()
								.addPropertyChangeListener(new PropertyChangeListener() {
									@Override
									public void propertyChange(PropertyChangeEvent event) {
										if (element.getGraphicalRepresentation() != null && conceptShapeRole != null
												&& conceptShapeRole.getGraphicalRepresentation() != null) {
											element.getGraphicalRepresentation().setsWith(conceptShapeRole.getGraphicalRepresentation(),
													GraphicalRepresentationSet.IGNORED_PROPERTIES);
										}
									}
								});
					}
				}
			}
		}
		return super.makePaletteElement(element, editor);
	}

	public class FreeConceptPaletteElement extends ContextualPaletteElement {
		public FreeConceptPaletteElement(DiagramPaletteElement aPaletteElement) {
			super(aPaletteElement);

			// if (aPaletteElement.getD)

		}
	}
}
