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

import org.openflexo.fge.geom.FGEPoint;
import org.openflexo.fme.model.FreeMetaModel;
import org.openflexo.fme.model.FreeModel;
import org.openflexo.fme.model.FreeModellingProject;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.view.FlexoConceptInstance;
import org.openflexo.foundation.viewpoint.FlexoConcept;
import org.openflexo.foundation.viewpoint.PrimitiveRole;
import org.openflexo.technologyadapter.diagram.fml.ConnectorRole;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
import org.openflexo.technologyadapter.diagram.fml.action.CreateDiagramFromPPTSlide;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramContainerElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;


/**
 * This action is used to create a new {@link FreeModel} in a {@link FreeModellingProject}<br>
 * 
 * New {@link FreeModel} might be created while a new associated {@link FreeMetaModel} is created, or using an existing one.
 * 
 * @author sylvain
 * 
 */
public class CreateFreeModelFromPPT extends FlexoAction<CreateFreeModelFromPPT, FreeModellingProject, FlexoObject> {

	private static final Logger logger = Logger.getLogger(CreateFreeModelFromPPT.class.getPackage().getName());

	public static FlexoActionType<CreateFreeModelFromPPT, FreeModellingProject, FlexoObject> actionType = new FlexoActionType<CreateFreeModelFromPPT, FreeModellingProject, FlexoObject>(
			"create_free_model_from_ppt", FlexoActionType.newMenu, FlexoActionType.defaultGroup, FlexoActionType.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateFreeModelFromPPT makeNewAction(FreeModellingProject focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
			return new CreateFreeModelFromPPT(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(FreeModellingProject object, Vector<FlexoObject> globalSelection) {
			return true;
		}

		@Override
		public boolean isEnabledForSelection(FreeModellingProject object, Vector<FlexoObject> globalSelection) {
			return true;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(CreateFreeModelFromPPT.actionType, FreeModellingProject.class);
	}

	CreateFreeModelFromPPT(FreeModellingProject focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}
	
	private FreeModel freeModel;

	private FlexoConcept none;
	
	@Override
	protected void doAction(Object context) throws SaveResourceException {

		logger.info("Create free model from PPT slide");
		logger.info("Create free model from PPT slide : Create free model ");
		CreateFreeModel actionCreateFreeModel = CreateFreeModel.actionType.makeNewEmbeddedAction(getFocusedObject(), null, this);
		actionCreateFreeModel.doAction();
		freeModel = actionCreateFreeModel.getFreeModel();
		
		logger.info("Create free model from PPT slide : Import PPT Slide ");
		CreateDiagramFromPPTSlide actionCreateDiagramFromPPTSlide = CreateDiagramFromPPTSlide.actionType
				.makeNewEmbeddedAction(getFocusedObject().getDiagramSpecificationsFolder(), null, this);
		
		actionCreateDiagramFromPPTSlide.setDiagram(actionCreateFreeModel.getFreeModel().getDiagram());
		actionCreateDiagramFromPPTSlide.doAction();
		
		none = freeModel.getMetaModel().getNoneFlexoConcept(getEditor(), this);
		createFlexoConceptInstancesFromDiagramContainer(actionCreateDiagramFromPPTSlide.getDiagram());
		
		logger.info("Create free model from PPT slide : Free Model Created ");
	}
	
	public FreeModel getFreeModel() {
		return freeModel;
	}
	
	private void createFlexoConceptInstancesFromDiagramContainer(DiagramContainerElement<?> diagramContainerElement){
		for(DiagramShape diagramShape : diagramContainerElement.getShapes()){
			createFlexoConceptInstanceFromDiagramShape(diagramShape);
			if(diagramShape.getShapes()!=null){
				createFlexoConceptInstancesFromDiagramContainer(diagramShape);
			}
		}
		// TODO connectors
	}
	
	private FlexoConceptInstance createFlexoConceptInstanceFromDiagramShape(DiagramShape diagramShape){
		FlexoConceptInstance newFlexoConceptInstance = freeModel.getVirtualModelInstance().makeNewFlexoConceptInstance(none);
		ShapeRole shapeRole = (ShapeRole) none.getFlexoRole(FreeMetaModel.SHAPE_ROLE_NAME);
		newFlexoConceptInstance.setFlexoActor(diagramShape, shapeRole);
		PrimitiveRole<String> nameRole = (PrimitiveRole<String>) none.getFlexoRole(FreeMetaModel.NAME_ROLE_NAME);
		newFlexoConceptInstance.setFlexoActor(diagramShape.getName(), nameRole);
		return newFlexoConceptInstance;
	}
	
	/*private FlexoConceptInstance createFlexoConceptInstanceFromDiagramConnector(DiagramConnector diagramConnector){
		FlexoConceptInstance newFlexoConceptInstance = freeModel.getVirtualModelInstance().makeNewFlexoConceptInstance(none);
		ConnectorRole connectorRole = (ConnectorRole) none.getFlexoRole(FreeMetaModel.SHAPE_ROLE_NAME);
		newFlexoConceptInstance.setFlexoActor(diagramConnector, connectorRole);
		return newFlexoConceptInstance;
	}*/

}
