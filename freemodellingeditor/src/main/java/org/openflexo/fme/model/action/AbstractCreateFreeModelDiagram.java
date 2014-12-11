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
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.InvalidArgumentException;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.view.VirtualModelInstance;
import org.openflexo.foundation.view.action.ModelSlotInstanceConfiguration.DefaultModelSlotInstanceConfigurationOption;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlotInstanceConfiguration;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelNature;
import org.openflexo.technologyadapter.diagram.fml.action.CreateFMLControlledDiagramVirtualModelInstance;
import org.openflexo.toolbox.StringUtils;

public class AbstractCreateFreeModelDiagram<A extends AbstractCreateFreeModelDiagram<A>> extends FlexoAction<A, FreeMetaModel, FlexoObject> {

	private static final Logger logger = Logger.getLogger(AbstractCreateFreeModelDiagram.class.getPackage().getName());

	private FreeModel freeModel;
	private String diagramName;
	private String description;

	AbstractCreateFreeModelDiagram(FlexoActionType<A, FreeMetaModel, FlexoObject> actionType, FreeMetaModel focusedObject,
			Vector<FlexoObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	protected void doAction(Object context) throws SaveResourceException {

		logger.info("Create diagram free model");

		System.out.println("getDiagramName=" + getDiagramName());

		CreateFMLControlledDiagramVirtualModelInstance action = CreateFMLControlledDiagramVirtualModelInstance.actionType
				.makeNewEmbeddedAction(getFocusedObject().getFreeModellingProject().getFreeModellingView(), null, this);
		action.setNewVirtualModelInstanceName(getDiagramName());
		action.setNewVirtualModelInstanceTitle(getDiagramName());
		action.setVirtualModel(getFocusedObject().getVirtualModel());

		TypedDiagramModelSlot diagramModelSlot = FMLControlledDiagramVirtualModelNature.getTypedDiagramModelSlot(getFocusedObject()
				.getVirtualModel());
		TypedDiagramModelSlotInstanceConfiguration diagramModelSlotInstanceConfiguration = (TypedDiagramModelSlotInstanceConfiguration) action
				.getModelSlotInstanceConfiguration(diagramModelSlot);
		diagramModelSlotInstanceConfiguration.setOption(DefaultModelSlotInstanceConfigurationOption.CreatePrivateNewModel);
		diagramModelSlotInstanceConfiguration.setFilename(getDiagramName() + ".diagram");
		diagramModelSlotInstanceConfiguration.setRelativePath("Diagrams/");
		diagramModelSlotInstanceConfiguration.setModelUri(FreeModel.getDiagramURI(
				getFocusedObject().getFreeModellingProject().getProject(), getDiagramName()));

		System.out.println("action=" + action);
		System.out.println("action.isValid()=" + action.isValid());

		action.doAction();

		VirtualModelInstance newVirtualModelInstance = action.getNewVirtualModelInstance();

		// Wrap into FreeModel
		try {
			freeModel = getFocusedObject().getFreeModellingProject().getFreeModel(newVirtualModelInstance);
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
		}

		getFocusedObject().getFreeModellingProject().getPropertyChangeSupport().firePropertyChange("freeMetaModels", null, null);

	}

	@Override
	public boolean isValid() {

		if (StringUtils.isEmpty(diagramName)) {
			return false;
		}

		if (getFocusedObject().getFreeModellingProject().getFreeModel(diagramName) != null) {
			return false;
		}

		return true;
	}

	/**
	 * Return newly created FreeModel
	 * 
	 * @return
	 */
	public FreeModel getFreeModel() {
		return freeModel;
	}

	public String getDiagramName() {
		return diagramName;
	}

	public void setDiagramName(String diagramName) {
		boolean wasValid = isValid();
		this.diagramName = diagramName;
		getPropertyChangeSupport().firePropertyChange("diagramName", null, diagramName);
		getPropertyChangeSupport().firePropertyChange("isValid", wasValid, isValid());
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		boolean wasValid = isValid();
		this.description = description;
		getPropertyChangeSupport().firePropertyChange("description", null, description);
		getPropertyChangeSupport().firePropertyChange("isValid", wasValid, isValid());
	}

}
