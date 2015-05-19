/**
 * 
 * Copyright (c) 2014, Openflexo
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

package org.openflexo.fme;

import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.fge.Drawing.ContainerNode;
import org.openflexo.fge.Drawing.DrawingTreeNode;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.control.DianaInteractiveEditor.EditorTool;
import org.openflexo.fge.control.DrawingPalette;
import org.openflexo.fge.control.PaletteElement;
import org.openflexo.fge.geom.FGEPoint;
import org.openflexo.fge.shapes.ShapeSpecification.ShapeType;
import org.openflexo.fme.model.ConceptGRAssociation;
import org.openflexo.fme.model.DiagramElement;
import org.openflexo.fme.model.Shape;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.model.undo.CompoundEdit;

public class DynamicPalette extends DrawingPalette implements PropertyChangeListener {

	private static final Logger logger = FlexoLogger.getLogger(DynamicPalette.class.getPackage().getName());

	private static final int GRID_WIDTH = 50;
	private static final int GRID_HEIGHT = 40;
	public static final Font DEFAULT_TEXT_FONT = new Font("SansSerif", Font.PLAIN, 7);
	public static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 11);

	private DiagramEditor editor;

	private final Hashtable<ConceptGRAssociation, PaletteElement> elementsForAssociations;

	public DynamicPalette() {
		super(200, 200, "default");
		elementsForAssociations = new Hashtable<ConceptGRAssociation, PaletteElement>();
	}

	public void update() {

		List<PaletteElement> elementsToAdd = new ArrayList<PaletteElement>();
		List<PaletteElement> elementsToRemove = new ArrayList<PaletteElement>(getElements());
		System.out.println("PALETTE ELEMENTS:" + getElements());
		System.out.println("ELEMENTS ASSOCIATION:" + getEditor().getDiagram().getAssociations());
		// For each existing association
		for (ConceptGRAssociation association : getEditor().getDiagram().getAssociations()) {
			// Retrieve the corresponding palette element
			PaletteElement e = elementsForAssociations.get(association);

			// If a palette element exist
			if (e != null) {
				// If there is no graphical element then we can delete the palette element
				// Excepted if the palette element is associated to a ReadOnly concept.
				// None concept is an exception,
				// it is a read only concept but palette elements can be deleted if no diagram elements are presents.
				if (getEditor().getDiagram().getElementsWithAssociation(association).isEmpty()
						&& (!association.getConcept().getReadOnly() || association.getConcept().getName().equals("None"))) {
					System.out.println("No diagram elements with this palette element, delete the palette element");
					elementsForAssociations.remove(association);

				} else {
					elementsToRemove.remove(e);
				}
			} else if (association.getGraphicalRepresentation() instanceof ShapeGraphicalRepresentation) {
				e = new DynamicPaletteElement(association);
				elementsForAssociations.put(association, e);
				elementsToAdd.add(e);
			}
		}
		for (PaletteElement e : elementsToRemove) {
			removeElement(e);
		}
		for (PaletteElement e : elementsToAdd) {
			addElement(e);
		}

		for (PaletteElement e : getElements()) {
			int px, py;
			int index = getElements().indexOf(e);

			px = index % 3;
			py = index / 3;

			// FACTORY.applyDefaultProperties(gr);
			if (e.getGraphicalRepresentation().getShapeSpecification().getShapeType() == ShapeType.SQUARE
					|| e.getGraphicalRepresentation().getShapeSpecification().getShapeType() == ShapeType.CIRCLE) {
				e.getGraphicalRepresentation().setX(px * GRID_WIDTH + 15);
				e.getGraphicalRepresentation().setY(py * GRID_HEIGHT + 10);
				e.getGraphicalRepresentation().setWidth(30);
				e.getGraphicalRepresentation().setHeight(30);
			} else {
				e.getGraphicalRepresentation().setX(px * GRID_WIDTH + 10);
				e.getGraphicalRepresentation().setY(py * GRID_HEIGHT + 10);
				e.getGraphicalRepresentation().setWidth(40);
				e.getGraphicalRepresentation().setHeight(30);
			}

		}
	}

	public PaletteElement getPaletteElement(ConceptGRAssociation association) {
		return elementsForAssociations.get(association);
	}

	/*public void addAssociation(ConceptGRAssociation association) {
		if (association.getGraphicalRepresentation() instanceof ShapeGraphicalRepresentation) {
			addElement(makePaletteElement((ShapeGraphicalRepresentation) association.getGraphicalRepresentation().cloneObject()));
		}
	}*/

	public DiagramEditor getEditor() {
		return editor;
	}

	public void setEditor(DiagramEditor editor) {
		if (editor != getEditor()) {
			if (this.editor != null) {
				this.editor.getDiagram().getPropertyChangeSupport().removePropertyChangeListener(this);
			}
			this.editor = editor;
			List<PaletteElement> elementsToRemove = new ArrayList<PaletteElement>(getElements());
			for (PaletteElement e : elementsToRemove) {
				removeElement(e);
				elementsForAssociations.clear();

			}
			update();
			this.editor.getDiagram().getPropertyChangeSupport().addPropertyChangeListener(this);

		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println("On update la palette dynamique");
		update();
	}

	public class DynamicPaletteElement implements PaletteElement {

		private ConceptGRAssociation association;
		private final ShapeGraphicalRepresentation elementGR;

		public DynamicPaletteElement(ConceptGRAssociation association) {
			this.association = association;
			elementGR = ((ShapeGraphicalRepresentation) association.getGraphicalRepresentation());

			// gr.setText(st.name());
			elementGR.setIsFloatingLabel(false);
			elementGR.setIsVisible(true);
			elementGR.setAllowToLeaveBounds(false);

		}

		@Override
		public boolean acceptDragging(DrawingTreeNode<?, ?> target) {
			return getEditor() != null && target instanceof ContainerNode;
		}

		@Override
		public boolean elementDragged(DrawingTreeNode<?, ?> target, FGEPoint dropLocation) {

			if (getEditor() == null) {
				return false;
			}

			DiagramElement<?, ?> container = (DiagramElement<?, ?>) target.getDrawable();

			logger.info("dragging " + this + " in " + container);

			// getController().addNewShape(new Shape(getGraphicalRepresentation().getShapeType(), dropLocation,
			// getController().getDrawing()),container);

			CompoundEdit edit = getEditor().getEditingContext().getUndoManager().startRecording("Dragging new Element");

			Shape newShape = getEditor().createNewShape(container, association, dropLocation);
			newShape.getGraphicalRepresentation().setWidth(50);
			newShape.getGraphicalRepresentation().setHeight(40);

			getEditor().getEditingContext().getUndoManager().stopRecording(edit);

			getEditor().getController().setCurrentTool(EditorTool.SelectionTool);
			getEditor().getController().setSelectedObject(getEditor().getDrawing().getDrawingTreeNode(newShape));

			return true;
		}

		@Override
		public ShapeGraphicalRepresentation getGraphicalRepresentation() {
			// return (ShapeGraphicalRepresentation) association.getGraphicalRepresentation();
			return elementGR;
		}

		@Override
		public void delete() {
			association = null;
		}

	}
}
