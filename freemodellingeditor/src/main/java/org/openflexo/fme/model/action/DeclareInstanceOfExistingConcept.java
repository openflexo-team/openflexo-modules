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
import org.openflexo.foundation.viewpoint.VirtualModel;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;

/**
 * This action is used to declare a concept to be an instance of an existing concept (move to None to Something)
 * 
 * @author sylvain
 * 
 */
public class DeclareInstanceOfExistingConcept extends FlexoAction<DeclareInstanceOfExistingConcept, FlexoConceptInstance, FlexoObject> {

	private static final Logger logger = Logger.getLogger(DeclareInstanceOfExistingConcept.class.getPackage().getName());

	public static enum GRStrategy {
		GetConceptShape, RedefineShapeOfConcept, KeepShape
	}

	public static FlexoActionType<DeclareInstanceOfExistingConcept, FlexoConceptInstance, FlexoObject> actionType = new FlexoActionType<DeclareInstanceOfExistingConcept, FlexoConceptInstance, FlexoObject>(
			"declare_instance_of_existing_concept", FlexoActionType.defaultGroup, FlexoActionType.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public DeclareInstanceOfExistingConcept makeNewAction(FlexoConceptInstance focusedObject, Vector<FlexoObject> globalSelection,
				FlexoEditor editor) {
			return new DeclareInstanceOfExistingConcept(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(FlexoConceptInstance object, Vector<FlexoObject> globalSelection) {
			return true;
		}

		@Override
		public boolean isEnabledForSelection(FlexoConceptInstance object, Vector<FlexoObject> globalSelection) {
			return true;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(DeclareInstanceOfExistingConcept.actionType, FlexoConceptInstance.class);
	}

	private FlexoConcept concept;
	private GRStrategy grStrategy = GRStrategy.GetConceptShape;

	DeclareInstanceOfExistingConcept(FlexoConceptInstance focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
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

	public FlexoConcept getConcept() {
		if (concept == null && getVirtualModel() != null && getVirtualModel().getFlexoConcepts().size() > 0) {
			return getVirtualModel().getFlexoConcepts().get(0);
		}
		return concept;
	}

	public void setConcept(FlexoConcept concept) {
		boolean wasValid = isValid();
		this.concept = concept;
		getPropertyChangeSupport().firePropertyChange("concept", null, concept);
		getPropertyChangeSupport().firePropertyChange("isValid", wasValid, isValid());
		getPropertyChangeSupport().firePropertyChange("errorMessage", null, getErrorMessage());
	}

	public GRStrategy getGrStrategy() {
		return grStrategy;
	}

	public void setGrStrategy(GRStrategy grStrategy) {
		boolean wasValid = isValid();
		this.grStrategy = grStrategy;
		getPropertyChangeSupport().firePropertyChange("grStrategy", null, grStrategy);
		getPropertyChangeSupport().firePropertyChange("isValid", wasValid, isValid());
		getPropertyChangeSupport().firePropertyChange("errorMessage", null, getErrorMessage());
	}

	@Override
	protected void doAction(Object context) throws InvalidArgumentException {

		FreeModel freeModel = getFreeModel();
		if (freeModel == null) {
			throw new InvalidArgumentException("FlexoConceptInstance does not belong to any FreeModel");
		}

		FlexoConceptInstance flexoConceptInstance = getFocusedObject();

		// Retrieve shape role of this FC
		ShapeRole currentShapeRole = (ShapeRole) flexoConceptInstance.getFlexoConcept().getFlexoRole(FreeMetaModel.SHAPE_ROLE_NAME);

		// Retrieve actual shape element
		DiagramShape shapeElement = flexoConceptInstance.getFlexoActor(currentShapeRole);

		ShapeRole newShapeRole = (ShapeRole) getConcept().getFlexoRole(FreeMetaModel.SHAPE_ROLE_NAME);

		switch (getGrStrategy()) {
		case RedefineShapeOfConcept:
			// Sets concept GR with actual shape GR
			newShapeRole.getGraphicalRepresentation().setsWith(shapeElement.getGraphicalRepresentation(), ShapeGraphicalRepresentation.X,
					ShapeGraphicalRepresentation.Y);
			break;
		case GetConceptShape:
			// Sets actual shape GR with concept GR
			shapeElement.getGraphicalRepresentation().setsWith(newShapeRole.getGraphicalRepresentation(), ShapeGraphicalRepresentation.X,
					ShapeGraphicalRepresentation.Y);
			break;
		case KeepShape:
			// Nothing to do
			break;
		}

		// We will here bypass the classical DropScheme
		flexoConceptInstance.setFlexoConcept(concept);

		// We should notify the creation of a new FlexoConcept
		freeModel.getPropertyChangeSupport().firePropertyChange("usedFlexoConcepts", null, getConcept());

		// This is used to notify the adding of a new instance of a flexo concept
		freeModel.getPropertyChangeSupport().firePropertyChange("getInstances(FlexoConcept)", null, getFocusedObject());
	}

	public VirtualModel getVirtualModel() {
		try {
			return getFreeModel().getVirtualModel();
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String errorMessage;

	public String getErrorMessage() {
		isValid();
		return errorMessage;
	}

	@Override
	public boolean isValid() {

		if (getConcept() == null) {
			errorMessage = FlexoLocalization.localizedForKey("no_concept_defined");
			return false;
		}

		return true;
	}

}