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

package org.openflexo.fme.model.action;

import java.io.File;
import java.util.Vector;
import java.util.logging.Logger;

import org.apache.poi.hslf.model.Slide;
import org.openflexo.connie.DataBinding;
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
public class CreateFreeModelFromPPT extends AbstractCreateFreeModel<CreateFreeModelFromPPT> {

	private static final Logger logger = Logger.getLogger(CreateFreeModelFromPPT.class.getPackage().getName());

	public static FlexoActionType<CreateFreeModelFromPPT, FreeModellingProject, FlexoObject> actionType = new FlexoActionType<CreateFreeModelFromPPT, FreeModellingProject, FlexoObject>(
			"create_free_model_from_ppt", FlexoActionType.newMenu, FlexoActionType.defaultGroup, FlexoActionType.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateFreeModelFromPPT makeNewAction(FreeModellingProject focusedObject, Vector<FlexoObject> globalSelection,
				FlexoEditor editor) {
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

	private FlexoConcept none;

	private File pptFile;
	private Slide slide;
	private String diagramName;
	private String diagramTitle;

	CreateFreeModelFromPPT(FreeModellingProject focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	protected void doAction(Object context) throws SaveResourceException {

		logger.info("Create free model from PPT slide");

		freeModel = createNewFreeModel();

		logger.info("Create free model from PPT slide : Import PPT Slide ");
		CreateDiagramFromPPTSlide actionCreateDiagramFromPPTSlide = CreateDiagramFromPPTSlide.actionType.makeNewEmbeddedAction(
				getFocusedObject().getDiagramSpecificationsFolder(), null, this);
		if (freeModel != null) {
			actionCreateDiagramFromPPTSlide.setDiagramName(getDiagramName());
			actionCreateDiagramFromPPTSlide.setDiagramTitle(getDiagramTitle());
			actionCreateDiagramFromPPTSlide.setFile(getFile());
			actionCreateDiagramFromPPTSlide.setSlide(getSlide());
			actionCreateDiagramFromPPTSlide.setDiagram(freeModel.getDiagram());

			actionCreateDiagramFromPPTSlide.doAction();
			none = freeModel.getMetaModel().getNoneFlexoConcept(getEditor(), this);
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
		FlexoConceptInstance newFlexoConceptInstance = freeModel.getVirtualModelInstance().makeNewFlexoConceptInstance(none);
		ShapeRole shapeRole = (ShapeRole) none.getAccessibleProperty(FreeMetaModel.SHAPE_ROLE_NAME);
		newFlexoConceptInstance.setFlexoActor(diagramShape, shapeRole);
		PrimitiveRole<String> nameRole = (PrimitiveRole<String>) none.getAccessibleProperty(FreeMetaModel.NAME_ROLE_NAME);
		shapeRole.setLabel(new DataBinding(FreeMetaModel.NAME_ROLE_NAME));
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

	public String getDiagramName() {
		return diagramName;
	}

	public void setDiagramName(String diagramName) {
		if ((diagramName == null && this.diagramName != null) || (diagramName != null && !diagramName.equals(this.diagramName))) {
			String oldValue = this.diagramName;
			this.diagramName = diagramName;
			getPropertyChangeSupport().firePropertyChange("diagramName", oldValue, diagramName);
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
