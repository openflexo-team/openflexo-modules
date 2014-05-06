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
package org.openflexo.fme.model.action;

import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.shapes.ShapeSpecification.ShapeType;
import org.openflexo.fme.model.FreeMetaModel;
import org.openflexo.fme.model.FreeModel;
import org.openflexo.fme.model.FreeModellingProject;
import org.openflexo.fme.model.FreeModellingProjectNature;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.InvalidArgumentException;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.view.FlexoConceptInstance;
import org.openflexo.foundation.viewpoint.FlexoConcept;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.FMLDiagramPaletteElementBinding;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
import org.openflexo.technologyadapter.diagram.fml.action.AddDiagramPaletteElement;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.toolbox.StringUtils;

/**
 * This action is used to create a new FlexoConcept
 * 
 * @author sylvain
 * 
 */
public class CreateNewConcept extends FlexoAction<CreateNewConcept, FlexoConceptInstance, FlexoObject> {

	private static final Logger logger = Logger.getLogger(CreateNewConcept.class.getPackage().getName());

	public static FlexoActionType<CreateNewConcept, FlexoConceptInstance, FlexoObject> actionType = new FlexoActionType<CreateNewConcept, FlexoConceptInstance, FlexoObject>(
			"create_new_concept", FlexoActionType.defaultGroup, FlexoActionType.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateNewConcept makeNewAction(FlexoConceptInstance focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
			return new CreateNewConcept(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(FlexoConceptInstance object, Vector<FlexoObject> globalSelection) {
			return true;
		}

		@Override
		public boolean isEnabledForSelection(FlexoConceptInstance object, Vector<FlexoObject> globalSelection) {
			return object.getFlexoConcept().getName().equals(FreeMetaModel.NONE_FLEXO_CONCEPT);
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(CreateNewConcept.actionType, FlexoConceptInstance.class);
	}

	private String newConceptName;
	private String newConceptDescription;

	private FlexoConcept newFlexoConcept;

	// private FlexoConceptInstance newFlexoConceptInstance;

	CreateNewConcept(FlexoConceptInstance focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	public FreeModellingProjectNature getFreeModellingProjectNature() {
		return getServiceManager().getProjectNatureService().getProjectNature(FreeModellingProjectNature.class);
	}

	public FreeModellingProject getFreeModellingProject() {
		return getFreeModellingProjectNature().getFreeModellingProject(getFocusedObject().getProject());
	}

	public FreeModel getFreeModel() throws InvalidArgumentException {
		return getFreeModellingProject().getFreeModel(getFocusedObject().getVirtualModelInstance());
	}

	@Override
	protected void doAction(Object context) throws InvalidArgumentException {

		FreeModel freeModel = getFreeModel();
		if (freeModel == null) {
			throw new InvalidArgumentException("FlexoConceptInstance does not belong to any FreeModel");
		}

		FlexoConceptInstance flexoConceptInstance = getFocusedObject();

		// Retrieve shape role of None FC
		ShapeRole noneShapeRole = (ShapeRole) flexoConceptInstance.getFlexoConcept().getFlexoRole(FreeMetaModel.SHAPE_ROLE_NAME);

		// Retrieve actual shape element
		DiagramShape shapeElement = flexoConceptInstance.getFlexoActor(noneShapeRole);

		// Now we create the new concept
		newFlexoConcept = freeModel.getMetaModel().getFlexoConcept(getNewConceptName(), getEditor(), this);

		// Add entry in palette
		AddDiagramPaletteElement addDiagramPaletteElement = AddDiagramPaletteElement.actionType.makeNewEmbeddedAction(freeModel
				.getMetaModel().getConceptsPalette(), null, this);
		ShapeGraphicalRepresentation paletteElementGR = (ShapeGraphicalRepresentation) shapeElement.getGraphicalRepresentation()
				.cloneObject();
		paletteElementGR.setX(10);
		paletteElementGR.setY(10);
		paletteElementGR.setText(getNewConceptName());
		paletteElementGR.setIsFloatingLabel(false);
		addDiagramPaletteElement.setGraphicalRepresentation(paletteElementGR);
		addDiagramPaletteElement.setNewElementName(getNewConceptName());
		addDiagramPaletteElement.doAction();

		DiagramPaletteElement paletteElement = addDiagramPaletteElement.getNewElement();

		System.out.println("Created palette element: " + paletteElement);
		int px, py;
		int index = freeModel.getMetaModel().getConceptsPalette().getElements().indexOf(addDiagramPaletteElement.getNewElement());
		px = index % 3;
		py = index / 3;

		// FACTORY.applyDefaultProperties(gr);
		if (paletteElementGR.getShapeSpecification().getShapeType() == ShapeType.SQUARE
				|| paletteElementGR.getShapeSpecification().getShapeType() == ShapeType.CIRCLE) {
			paletteElementGR.setX(10);
			paletteElementGR.setY(10);
			paletteElementGR.setX(px * FreeMetaModel.PALETTE_GRID_WIDTH + 15);
			paletteElementGR.setY(py * FreeMetaModel.PALETTE_GRID_HEIGHT + 10);
			paletteElementGR.setWidth(30);
			paletteElementGR.setHeight(30);
		} else {
			paletteElementGR.setX(px * FreeMetaModel.PALETTE_GRID_WIDTH + 10);
			paletteElementGR.setY(py * FreeMetaModel.PALETTE_GRID_HEIGHT + 10);
			paletteElementGR.setWidth(40);
			paletteElementGR.setHeight(30);
		}

		// Create binding and associate it
		DropScheme dropScheme = newFlexoConcept.getFlexoBehaviours(DropScheme.class).get(0);
		FMLDiagramPaletteElementBinding newBinding = newFlexoConcept.getVirtualModel().getVirtualModelFactory()
				.newInstance(FMLDiagramPaletteElementBinding.class);
		newBinding.setPaletteElement(paletteElement);
		newBinding.setFlexoConcept(newFlexoConcept);
		newBinding.setDropScheme(dropScheme);
		freeModel.getMetaModel().getTypedDiagramModelSlot().addToPaletteElementBindings(newBinding);

		// Sets new concept GR with actual shape GR
		ShapeRole shapeRole = (ShapeRole) newFlexoConcept.getFlexoRole(FreeMetaModel.SHAPE_ROLE_NAME);
		shapeRole.getGraphicalRepresentation().setsWith(shapeElement.getGraphicalRepresentation());
		shapeRole.getGraphicalRepresentation().setX(10);
		shapeRole.getGraphicalRepresentation().setY(10);

		// We will here bypass the classical DropScheme
		flexoConceptInstance.setFlexoConcept(newFlexoConcept);

		// We should notify the creation of a new FlexoConcept
		freeModel.getPropertyChangeSupport().firePropertyChange("usedFlexoConcepts", null, newFlexoConcept);

		// This is used to notify the adding of a new instance of a flexo concept
		freeModel.getPropertyChangeSupport().firePropertyChange("getInstances(FlexoConcept)", null, getFocusedObject());
	}

	public String getNewConceptName() {
		return newConceptName;
	}

	public void setNewConceptName(String newConceptName) {
		boolean wasValid = isValid();
		this.newConceptName = newConceptName;
		getPropertyChangeSupport().firePropertyChange("newConceptName", null, newConceptName);
		getPropertyChangeSupport().firePropertyChange("isValid", wasValid, isValid());
		getPropertyChangeSupport().firePropertyChange("errorMessage", null, getErrorMessage());
	}

	public String getNewConceptDescription() {
		return newConceptDescription;
	}

	public void setNewConceptDescription(String newConceptDescription) {
		boolean wasValid = isValid();
		this.newConceptDescription = newConceptDescription;
		getPropertyChangeSupport().firePropertyChange("newConceptDescription", null, newConceptDescription);
		getPropertyChangeSupport().firePropertyChange("isValid", wasValid, isValid());
		getPropertyChangeSupport().firePropertyChange("errorMessage", null, getErrorMessage());
	}

	public FlexoConcept getNewFlexoConcept() {
		return newFlexoConcept;
	}

	private String errorMessage;

	public String getErrorMessage() {
		isValid();
		return errorMessage;
	}

	@Override
	public boolean isValid() {

		if (StringUtils.isEmpty(newConceptName)) {
			errorMessage = FlexoLocalization.localizedForKey("no_concept_name_defined");
			return false;
		}

		if (getFocusedObject().getVirtualModelInstance().getVirtualModel().getFlexoConcept(newConceptName) != null) {
			errorMessage = FlexoLocalization.localizedForKey("a_concept_with_that_name_already_exists");
			return false;
		}

		return true;
	}

}
