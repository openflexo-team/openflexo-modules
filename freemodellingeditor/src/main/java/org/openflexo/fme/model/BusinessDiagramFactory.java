package org.openflexo.fme.model;

import org.openflexo.fge.BackgroundImageBackgroundStyle;
import org.openflexo.fge.BackgroundStyle.BackgroundStyleType;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.shapes.ShapeSpecification.ShapeType;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.undo.UndoManager;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;

public class BusinessDiagramFactory extends DiagramFactory {

	private static final Resource HW_COMPONENT = ResourceLocator.locateResource("Images/hardware.png");
	private static final Resource SW_COMPONENT = ResourceLocator.locateResource("Images/software.png");

	public BusinessDiagramFactory() throws ModelDefinitionException {
		super();
	}

	@Override
	public UndoManager getUndoManager() {
		if (getEditingContext() != null) {
			return getEditingContext().getUndoManager();
		}
		return null;
	}

	@Override
	public Diagram makeNewDiagram() {
		Diagram diagram = super.makeNewDiagram();

		/*
		 * Add a pre-Existing BusinessModel and a graphical representation
		 */
		createNewShapeGraphicalRepresentation(diagram, "HWComponent", HW_COMPONENT);
		createNewShapeGraphicalRepresentation(diagram, "SWComponent", SW_COMPONENT);
		/*
		 * End pre-existing BusinessModel
		 */

		return diagram;
	}

	private void createNewShapeGraphicalRepresentation(Diagram diagram, String name, Resource image) {
		Concept concept = newInstance(Concept.class);
		concept.setName(name);
		concept.setReadOnly(true);
		ConceptGRAssociation conceptAssoc = newInstance(ConceptGRAssociation.class);
		conceptAssoc.setConcept(concept);
		ShapeGraphicalRepresentation conceptGR = newInstance(ShapeGraphicalRepresentation.class);
		conceptGR.setShapeType(ShapeType.RECTANGLE);
		applyDefaultProperties(conceptGR);
		conceptGR.setWidth(40);
		conceptGR.setHeight(30);

		if (image != null) {
			conceptGR.setBackgroundType(BackgroundStyleType.IMAGE);
			conceptGR.getForeground().setNoStroke(true);
			conceptGR.getShadowStyle().setDrawShadow(false);
			BackgroundImageBackgroundStyle backgroundImage = (BackgroundImageBackgroundStyle) conceptGR.getBackground();
			backgroundImage.setFitToShape(true);
			backgroundImage.setImageResource(image);
		}

		conceptAssoc.setGraphicalRepresentation(conceptGR);
		diagram.addToAssociations(conceptAssoc);
		diagram.getDataModel().addToConcepts(concept);
	}

}
