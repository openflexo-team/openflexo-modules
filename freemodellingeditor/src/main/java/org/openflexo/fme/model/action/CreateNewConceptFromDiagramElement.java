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
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.PrimitiveRole;
import org.openflexo.foundation.fmlrt.FlexoConceptInstance;
import org.openflexo.technologyadapter.diagram.fml.GraphicalElementRole;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;

/**
 * This action is used to create a new FlexoConcept from a diagram element
 * 
 * @author vincent
 * 
 */
public class CreateNewConceptFromDiagramElement extends FlexoAction<CreateNewConceptFromDiagramElement, DiagramElement<?>, FlexoObject> {

	private static final Logger logger = Logger.getLogger(CreateNewConceptFromDiagramElement.class.getPackage().getName());

	public static FlexoActionType<CreateNewConceptFromDiagramElement, DiagramElement<?>, FlexoObject> actionType = new FlexoActionType<CreateNewConceptFromDiagramElement, DiagramElement<?>, FlexoObject>(
			"create_new_concept_from_diagram_element", FlexoActionType.defaultGroup, FlexoActionType.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateNewConceptFromDiagramElement makeNewAction(DiagramElement<?> focusedObject, Vector<FlexoObject> globalSelection,
				FlexoEditor editor) {
			return new CreateNewConceptFromDiagramElement(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(DiagramElement<?> object, Vector<FlexoObject> globalSelection) {
			return true;
		}

		@Override
		public boolean isEnabledForSelection(DiagramElement<?> object, Vector<FlexoObject> globalSelection) {
			return true;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(CreateNewConceptFromDiagramElement.actionType, DiagramElement.class);
	}

	CreateNewConceptFromDiagramElement(DiagramElement<?> focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	public FreeModellingProjectNature getFreeModellingProjectNature() {
		return getServiceManager().getProjectNatureService().getProjectNature(FreeModellingProjectNature.class);
	}

	public FreeModellingProject getFreeModellingProject() {
		return getFreeModellingProjectNature().getFreeModellingProject(getEditor().getProject());
	}

	public FreeModel getFreeModel() {
		for (FreeModel freeModel : getFreeModellingProject().getFreeModels()) {
			if (freeModel.getDiagram().equals(getFocusedObject().getDiagram())) {
				return freeModel;
			}
		}
		return null;
	}

	private FlexoConcept none;
	private FlexoConceptInstance flexoConceptInstance;
	private FlexoConcept flexoConcept;

	@Override
	protected void doAction(Object context) throws InvalidArgumentException {

		logger.info("Create new instance of created concept from diagram element ");
		none = getFreeModel().getMetaModel().getNoneFlexoConcept(getEditor(), this);
		flexoConceptInstance = createFlexoConceptInstanceFromDiagramShape(getFocusedObject());

		logger.info("Create new concept from diagram element ");
		CreateNewConceptFromNoneConcept actionCreateNewConcept = CreateNewConceptFromNoneConcept.actionType.makeNewEmbeddedAction(flexoConceptInstance, null, this);
		actionCreateNewConcept.doAction();
		flexoConcept = actionCreateNewConcept.getNewFlexoConcept();
	}

	private FlexoConceptInstance createFlexoConceptInstanceFromDiagramShape(DiagramElement<?> diagramElement) {
		FlexoConceptInstance newFlexoConceptInstance = getFreeModel().getVirtualModelInstance().makeNewFlexoConceptInstance(none);
		GraphicalElementRole geRole = (ShapeRole) none.getFlexoRole(FreeMetaModel.SHAPE_ROLE_NAME);
		newFlexoConceptInstance.setFlexoActor(diagramElement, geRole);
		PrimitiveRole<String> nameRole = (PrimitiveRole<String>) none.getFlexoRole(FreeMetaModel.NAME_ROLE_NAME);
		newFlexoConceptInstance.setFlexoActor(diagramElement.getName(), nameRole);
		return newFlexoConceptInstance;
	}

	public FlexoConcept getFlexoConcept() {
		return flexoConcept;
	}

	public void setFlexoConcept(FlexoConcept flexoConcept) {
		this.flexoConcept = flexoConcept;
	}

}
