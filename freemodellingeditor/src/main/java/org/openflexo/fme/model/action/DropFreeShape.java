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

import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.geom.FGEPoint;
import org.openflexo.fme.model.FreeMetaModel;
import org.openflexo.fme.model.FreeModel;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.view.FlexoConceptInstance;
import org.openflexo.foundation.viewpoint.FlexoConcept;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
import org.openflexo.technologyadapter.diagram.model.DiagramContainerElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.technologyadapter.diagram.model.action.DropSchemeAction;

/**
 * This action is used to create a new FreeShape in the Free Modelling Editor
 * 
 * @author sylvain
 * 
 */
public class DropFreeShape extends FlexoAction<DropFreeShape, DiagramContainerElement<?>, FlexoObject> {

	private static final Logger logger = Logger.getLogger(DropFreeShape.class.getPackage().getName());

	public static FlexoActionType<DropFreeShape, DiagramContainerElement<?>, FlexoObject> actionType = new FlexoActionType<DropFreeShape, DiagramContainerElement<?>, FlexoObject>(
			"drop_free_shape", FlexoActionType.newMenu, FlexoActionType.defaultGroup, FlexoActionType.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public DropFreeShape makeNewAction(DiagramContainerElement<?> focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
			return new DropFreeShape(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(DiagramContainerElement<?> object, Vector<FlexoObject> globalSelection) {
			return true;
		}

		@Override
		public boolean isEnabledForSelection(DiagramContainerElement<?> object, Vector<FlexoObject> globalSelection) {
			return true;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(DropFreeShape.actionType, DiagramContainerElement.class);
	}

	private FreeModel freeModel;
	private DiagramContainerElement<?> parent;
	private String newShapeName;
	private ShapeGraphicalRepresentation graphicalRepresentation;

	private FlexoConceptInstance newFlexoConceptInstance;

	DropFreeShape(DiagramContainerElement<?> focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	protected void doAction(Object context) throws SaveResourceException {

		boolean noneConceptIsExisting = freeModel.getMetaModel().getVirtualModel().getFlexoConcept(FreeMetaModel.NONE_FLEXO_CONCEPT) != null;

		FlexoConcept none = freeModel.getMetaModel().getNoneFlexoConcept(getEditor(), this);

		List<DropScheme> dsList = none.getFlexoBehaviours(DropScheme.class);

		if (dsList.size() == 1) {

			DropScheme dropScheme = dsList.get(0);

			DropSchemeAction action = DropSchemeAction.actionType.makeNewEmbeddedAction(getFreeModel().getVirtualModelInstance(), null,
					this);
			action.setDropScheme(dropScheme);
			action.setParent(getParent());
			action.setDropLocation(new FGEPoint(getGraphicalRepresentation().getX(), getGraphicalRepresentation().getY()));
			action.doAction();
			newFlexoConceptInstance = action.getFlexoConceptInstance();

			ShapeRole shapeRole = (ShapeRole) none.getFlexoRole(FreeMetaModel.SHAPE_ROLE_NAME);
			DiagramShape shape = newFlexoConceptInstance.getFlexoActor(shapeRole);

			shape.getGraphicalRepresentation().setsWith(getGraphicalRepresentation());

			if (!noneConceptIsExisting) {
				// This means that None FlexoConcept was not existing and has been created
				// We should notify this
				freeModel.getPropertyChangeSupport().firePropertyChange("usedFlexoConcepts", null, none);
			}

			// This is used to notify the adding of a new instance of None flexo concept
			freeModel.getPropertyChangeSupport().firePropertyChange("getInstances(FlexoConcept)", null, newFlexoConceptInstance);

		} else {
			logger.warning("Could not find DropScheme in " + none);
		}
	}

	public FreeModel getFreeModel() {
		return freeModel;
	}

	public void setFreeModel(FreeModel freeModel) {
		this.freeModel = freeModel;
	}

	public DiagramContainerElement<?> getParent() {
		if (parent == null) {
			parent = getFocusedObject();
		}
		return parent;
	}

	public void setParent(DiagramContainerElement<?> parent) {
		this.parent = parent;
	}

	public String getNewShapeName() {
		return newShapeName;
	}

	public void setNewShapeName(String newShapeName) {
		this.newShapeName = newShapeName;
	}

	public ShapeGraphicalRepresentation getGraphicalRepresentation() {
		return graphicalRepresentation;
	}

	public void setGraphicalRepresentation(ShapeGraphicalRepresentation graphicalRepresentation) {
		this.graphicalRepresentation = graphicalRepresentation;
	}

	public FlexoConceptInstance getNewFlexoConceptInstance() {
		return newFlexoConceptInstance;
	}

}
