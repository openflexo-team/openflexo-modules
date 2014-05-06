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

import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.fge.Drawing.ContainerNode;
import org.openflexo.fge.Drawing.DrawingTreeNode;
import org.openflexo.fge.GRParameter;
import org.openflexo.fge.GraphicalRepresentation;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.control.PaletteElement;
import org.openflexo.fge.geom.FGEPoint;
import org.openflexo.fge.shapes.ShapeSpecification.ShapeType;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.model.factory.AccessibleProxyObject;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.AbstractDiagramPalette;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;

public class DynamicPalette extends AbstractDiagramPalette implements PropertyChangeListener {

	private static final Logger logger = FlexoLogger.getLogger(DynamicPalette.class.getPackage().getName());

	private static final int GRID_WIDTH = 50;
	private static final int GRID_HEIGHT = 40;
	public static final Font DEFAULT_TEXT_FONT = new Font("SansSerif", Font.PLAIN, 7);
	public static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 11);

	public static final String SHAPE_ADDED = "ShapeAdded";
	public static final String SHAPE_REMOVED = "ShapeRemoved";

	// private List<PaletteElement>

	// private final Hashtable<ConceptGRAssociation, PaletteElement> elementsForAssociations;

	public DynamicPalette(FreeModelDiagramEditor editor) {
		super(editor, 200, 200, "dynamic_palette");
		getEditor().getFreeModel().getPropertyChangeSupport().addPropertyChangeListener(this);
		// elementsForAssociations = new Hashtable<ConceptGRAssociation, PaletteElement>();
		update();
	}

	@Override
	public void delete() {
		getEditor().getFreeModel().getPropertyChangeSupport().removePropertyChangeListener(this);
		super.delete();
	}

	@SuppressWarnings("serial")
	public static class GraphicalRepresentationSet<T extends FlexoObject> extends HashMap<GraphicalRepresentation, List<T>> {

		public static final GRParameter<?>[] IGNORED_PROPERTIES = { GraphicalRepresentation.IDENTIFIER, GraphicalRepresentation.TEXT,
				ShapeGraphicalRepresentation.X, ShapeGraphicalRepresentation.Y, ShapeGraphicalRepresentation.WIDTH,
				ShapeGraphicalRepresentation.HEIGHT };

		public static <T> T valueForParameter(GraphicalRepresentation gr, GRParameter<T> parameter) {
			if (gr.hasKey(parameter.getName())) {
				return (T) gr.objectForKey(parameter.getName());
			}
			return null;
		}

		public static boolean equals(GraphicalRepresentation gr1, GraphicalRepresentation gr2) {
			if (!gr1.getClass().equals(gr2.getClass())) {
				return false;
			}
			for (GRParameter<?> p : GRParameter.getGRParameters(gr1.getClass())) {
				boolean isToBeIgnored = false;
				for (GRParameter<?> ignoredP : IGNORED_PROPERTIES) {
					if (p.getName().equals(ignoredP.getName())) {
						isToBeIgnored = true;
						break;
					}
				}
				if (!isToBeIgnored) {
					Object value1 = valueForParameter(gr1, p);
					Object value2 = valueForParameter(gr2, p);
					if (value1 == null) {
						if (value2 != null) {
							System.out.println("Differs 1 " + value1 + " and " + value2 + " for " + p);
							return false;
						}
					} else {
						if (value1 instanceof AccessibleProxyObject) {
							if (!((AccessibleProxyObject) value1).equalsObject(value2)) {
								System.out.println("Differs 2 " + value1 + " and " + value2 + " for " + p);
								return false;
							}
						} else {
							if (!value1.equals(value2)) {
								System.out.println("Differs 3 " + value1 + " and " + value2 + " for " + p);
								return false;
							}
						}
					}
				}
			}
			return true;
		}

		public T put(GraphicalRepresentation key, T value) {
			List<T> list;
			for (GraphicalRepresentation gr : keySet()) {
				if (equals(key, gr)) {
					// Found an existing list of FlexoObject which has same graphical representation
					list = get(gr);
					if (!list.contains(value)) {
						list.add(value);
						return value;
					}
				}
			}
			list = new ArrayList<T>();
			put(key, list);
			list.add(value);
			return value;
		}

	}

	public void update() {

		System.out.println("PALETTE ELEMENTS:" + getElements());
		// System.out.println("ELEMENTS ASSOCIATION:" + getEditor().getDiagram().getAssociations());

		GraphicalRepresentationSet<DiagramElement<?>> diagramGRs = new GraphicalRepresentationSet<DiagramElement<?>>();

		// For each existing DiagramElement:
		for (DiagramElement<?> e : getEditor().getFreeModel().getDiagram().getDescendants()) {
			diagramGRs.put(e.getGraphicalRepresentation(), e);
		}

		System.out.println("DiagramGRs=" + diagramGRs);
		for (GraphicalRepresentation gr : diagramGRs.keySet()) {
			System.out.println(" > For " + gr + " : " + diagramGRs.get(gr));
		}

		List<PaletteElement> elementsToAdd = new ArrayList<PaletteElement>();
		List<PaletteElement> elementsToRemove = new ArrayList<PaletteElement>(getElements());

		for (GraphicalRepresentation key : diagramGRs.keySet()) {
			// We iterate here on each different Shape GR
			if (key instanceof ShapeGraphicalRepresentation) {
				PaletteElement existingElement = null;
				for (PaletteElement e : getElements()) {
					if (GraphicalRepresentationSet.equals(e.getGraphicalRepresentation(), key)) {
						existingElement = e;
						break;
					}
				}
				if (existingElement != null) {
					// Fine, nothing to do for this one
					elementsToRemove.remove(existingElement);
				} else {
					existingElement = makePaletteElement((ShapeGraphicalRepresentation) key);
					elementsToAdd.add(existingElement);
				}
			}
		}

		for (PaletteElement e : elementsToRemove) {
			System.out.println("Removing: " + e);
			removeElement(e);
		}
		for (PaletteElement e : elementsToAdd) {
			System.out.println("Adding: " + e);
			addElement(e);
		}

		for (PaletteElement e : getElements()) {
			int px, py;
			int index = getElements().indexOf(e);

			px = index % 3;
			py = index / 3;

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

		// For each existing association
		/*for (ConceptGRAssociation association : getEditor().getDiagram().getAssociations()) {
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
		*/

	}

	/*public PaletteElement getPaletteElement(ConceptGRAssociation association) {
		return elementsForAssociations.get(association);
	}*/

	/*public void addAssociation(ConceptGRAssociation association) {
		if (association.getGraphicalRepresentation() instanceof ShapeGraphicalRepresentation) {
			addElement(makePaletteElement((ShapeGraphicalRepresentation) association.getGraphicalRepresentation().cloneObject()));
		}
	}*/

	@Override
	public FreeModelDiagramEditor getEditor() {
		return (FreeModelDiagramEditor) super.getEditor();
	}

	/*public void setEditor(DiagramEditor editor) {
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
	}*/

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(SHAPE_ADDED)) {
			System.out.println("On update la palette dynamique");
			update();
		}
	}

	/*public class DynamicPaletteElement implements PaletteElement {

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
	}*/

	private PaletteElement makePaletteElement(final ShapeGraphicalRepresentation prototypeGR) {
		final ShapeGraphicalRepresentation gr = (ShapeGraphicalRepresentation) prototypeGR.cloneObject();
		PaletteElement returned = new PaletteElement() {
			@Override
			public boolean acceptDragging(DrawingTreeNode<?, ?> target) {
				return getEditor() != null && target instanceof ContainerNode;
			}

			@Override
			public boolean elementDragged(DrawingTreeNode<?, ?> target, FGEPoint dropLocation) {
				return handleDrop(target, getGraphicalRepresentation(), dropLocation);
			}

			@Override
			public ShapeGraphicalRepresentation getGraphicalRepresentation() {
				return gr;
			}

			@Override
			public void delete() {
				gr.delete();
			}

		};
		return returned;
	}

	private boolean handleDrop(DrawingTreeNode<?, ?> target, ShapeGraphicalRepresentation graphicalRepresentation, FGEPoint dropLocation) {
		System.out.println("YES 888888       !!!!!!!!!!!!!!!!");
		return false;
	}

}
