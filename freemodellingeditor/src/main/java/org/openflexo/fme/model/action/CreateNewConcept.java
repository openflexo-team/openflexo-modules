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
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
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
	private FlexoConceptInstance newFlexoConceptInstance;

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

		// Sets new concept GR with actual shape GR
		ShapeRole shapeRole = (ShapeRole) newFlexoConcept.getFlexoRole(FreeMetaModel.SHAPE_ROLE_NAME);
		shapeRole.getGraphicalRepresentation().setsWith(shapeElement.getGraphicalRepresentation());

		// We will here bypass the classical DropScheme
		flexoConceptInstance.setFlexoConcept(newFlexoConcept);

		/*List<DropScheme> dsList = newFlexoConcept.getFlexoBehaviours(DropScheme.class);

		// We now apply here the default drop scheme
		if (dsList.size() == 1) {

			DropScheme dropScheme = dsList.get(0);

			DropSchemeAction action = DropSchemeAction.actionType.makeNewEmbeddedAction(flexoConceptInstance.getVirtualModelInstance(),
					null, this);
			action.setDropScheme(dropScheme);
			action.setParent(shapeElement.getParent());
			action.setDropLocation(new FGEPoint(shapeElement.getGraphicalRepresentation().getX(), shapeElement.getGraphicalRepresentation()
					.getY()));
			action.doAction();
			newFlexoConceptInstance = action.getFlexoConceptInstance();

		} else {
			logger.warning("Could not find DropScheme in " + newFlexoConcept);
		}*/

		// Now remove old FCI
		// getFocusedObject().getVirtualModelInstance().removeFromFlexoConceptInstances(flexoConceptInstance);
		// flexoConceptInstance.delete();

		// We should notify the creation of a new FlexoConcept
		freeModel.getPropertyChangeSupport().firePropertyChange("usedFlexoConcepts", null, newFlexoConcept);

		// This is used to notify the adding of a new instance of a flexo concept
		freeModel.getPropertyChangeSupport().firePropertyChange("getInstances(FlexoConcept)", null, newFlexoConceptInstance);
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
