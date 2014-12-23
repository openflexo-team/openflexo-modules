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

import java.io.File;
import java.util.Vector;
import java.util.logging.Logger;

import org.apache.poi.hslf.model.Slide;
import org.openflexo.fme.model.FreeMetaModel;
import org.openflexo.fme.model.FreeModel;
import org.openflexo.fme.model.FreeModellingProject;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.PrimitiveRole;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
import org.openflexo.technologyadapter.diagram.fml.action.CreateDiagramFromPPTSlide;
import org.openflexo.technologyadapter.diagram.model.DiagramContainerElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.toolbox.StringUtils;

/**
 * This action is used to create a new {@link FreeModel} in a {@link FreeModellingProject}<br>
 * 
 * New {@link FreeModel} might be created while a new associated {@link FreeMetaModel} is created, or using an existing one.
 * 
 * @author sylvain
 * 
 */
public class CreateFreeModelDiagramFromPPT extends AbstractCreateFreeModelDiagram<CreateFreeModelDiagramFromPPT> {

	private static final Logger logger = Logger.getLogger(CreateFreeModelDiagramFromPPT.class.getPackage().getName());

	public static FlexoActionType<CreateFreeModelDiagramFromPPT, FreeMetaModel, FlexoObject> actionType = new FlexoActionType<CreateFreeModelDiagramFromPPT, FreeMetaModel, FlexoObject>(
			"create_diagram_from_ppt", FlexoActionType.newMenu, FlexoActionType.defaultGroup, FlexoActionType.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateFreeModelDiagramFromPPT makeNewAction(FreeMetaModel focusedObject, Vector<FlexoObject> globalSelection,
				FlexoEditor editor) {
			return new CreateFreeModelDiagramFromPPT(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(FreeMetaModel object, Vector<FlexoObject> globalSelection) {
			return true;
		}

		@Override
		public boolean isEnabledForSelection(FreeMetaModel object, Vector<FlexoObject> globalSelection) {
			return true;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(CreateFreeModelDiagramFromPPT.actionType, FreeMetaModel.class);
	}

	CreateFreeModelDiagramFromPPT(FreeMetaModel focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	private FlexoConcept none;

	private File pptFile;
	private Slide slide;
	private String diagramTitle;

	@Override
	protected void doAction(Object context) throws SaveResourceException {

		logger.info("Create free model from PPT slide");

		super.doAction(context);

		logger.info("Create free model from PPT slide : Import PPT Slide ");
		CreateDiagramFromPPTSlide actionCreateDiagramFromPPTSlide = CreateDiagramFromPPTSlide.actionType.makeNewEmbeddedAction(
				getFocusedObject().getFreeModellingProject().getDiagramSpecificationsFolder(), null, this);
		if (getFreeModel() != null) {
			actionCreateDiagramFromPPTSlide.setDiagram(getFreeModel().getDiagram());
			actionCreateDiagramFromPPTSlide.setDiagramName(getDiagramName());
			actionCreateDiagramFromPPTSlide.setDiagramTitle(getDiagramTitle());
			actionCreateDiagramFromPPTSlide.setFile(getFile());
			actionCreateDiagramFromPPTSlide.setSlide(getSlide());
			actionCreateDiagramFromPPTSlide.setDiagram(getFreeModel().getDiagram());

			// System.out.println("actionCreateDiagramFromPPTSlide=" + actionCreateDiagramFromPPTSlide);
			// System.out.println("valid=" + actionCreateDiagramFromPPTSlide.isValid());
			// System.out.println("message=" + actionCreateDiagramFromPPTSlide.getErrorMessage());

			actionCreateDiagramFromPPTSlide.doAction();

			none = getFreeModel().getMetaModel().getNoneFlexoConcept(getEditor(), this);
			createFlexoConceptInstancesFromDiagramContainer(actionCreateDiagramFromPPTSlide.getDiagram());

			logger.info("Create free model from PPT slide : Free Model Created ");
		}
	}

	private void createFlexoConceptInstancesFromDiagramContainer(DiagramContainerElement<?> diagramContainerElement) {
		for (DiagramShape diagramShape : diagramContainerElement.getShapes()) {
			createFlexoConceptInstanceFromDiagramShape(diagramShape);
			if (diagramShape.getShapes() != null) {
				createFlexoConceptInstancesFromDiagramContainer(diagramShape);
			}
		}
		// TODO connectors
	}

	private FlexoConceptInstance createFlexoConceptInstanceFromDiagramShape(DiagramShape diagramShape) {
		FlexoConceptInstance newFlexoConceptInstance = getFreeModel().getVirtualModelInstance().makeNewFlexoConceptInstance(none);
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

	@Override
	public boolean isValid() {
		if (!super.isValid()) {
			return false;
		}

		if (getFile() == null || !getFile().exists()) {
			return false;
		}

		if (getSlide() == null) {
			return false;
		}

		if (StringUtils.isEmpty(getDiagramName())) {
			return false;
		}

		return true;
	}

	public File getFile() {
		return pptFile;
	}

	public void setFile(File pptFile) {
		if ((pptFile == null && this.pptFile != null) || (pptFile != null && !pptFile.equals(this.pptFile))) {
			File oldValue = this.pptFile;
			this.pptFile = pptFile;
			getPropertyChangeSupport().firePropertyChange("file", oldValue, pptFile);
		}
	}

	public Slide getSlide() {
		return slide;
	}

	public void setSlide(Slide slide) {
		if ((slide == null && this.slide != null) || (slide != null && !slide.equals(this.slide))) {
			Slide oldValue = this.slide;
			this.slide = slide;
			getPropertyChangeSupport().firePropertyChange("slide", oldValue, slide);
		}
	}

	public String getDiagramTitle() {
		return diagramTitle;
	}

	public void setDiagramTitle(String diagramTitle) {
		if ((diagramTitle == null && this.diagramTitle != null) || (diagramTitle != null && !diagramTitle.equals(this.diagramTitle))) {
			String oldValue = this.diagramTitle;
			this.diagramTitle = diagramTitle;
			getPropertyChangeSupport().firePropertyChange("diagramTitle", oldValue, diagramTitle);
		}
	}

}
