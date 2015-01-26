/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Freemodellingeditor, a component of the software infrastructure 
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
