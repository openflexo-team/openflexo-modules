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
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.InvalidArgumentException;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.view.VirtualModelInstance;
import org.openflexo.foundation.view.action.ModelSlotInstanceConfiguration.DefaultModelSlotInstanceConfigurationOption;
import org.openflexo.foundation.viewpoint.VirtualModel;
import org.openflexo.foundation.viewpoint.action.CreateModelSlot;
import org.openflexo.foundation.viewpoint.action.CreateVirtualModel;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlotInstanceConfiguration;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelNature;
import org.openflexo.technologyadapter.diagram.fml.action.CreateDiagramPalette;
import org.openflexo.technologyadapter.diagram.fml.action.CreateDiagramSpecification;
import org.openflexo.technologyadapter.diagram.fml.action.CreateFMLControlledDiagramVirtualModelInstance;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.toolbox.StringUtils;

/**
 * This action is used to create a new {@link FreeModel} in a {@link FreeModellingProject}<br>
 * 
 * New {@link FreeModel} might be created while a new associated {@link FreeMetaModel} is created, or using an existing one.
 * 
 * @author sylvain
 * 
 */
public class CreateFreeModel extends FlexoAction<CreateFreeModel, FreeModellingProject, FlexoObject> {

	private static final Logger logger = Logger.getLogger(CreateFreeModel.class.getPackage().getName());

	public static FlexoActionType<CreateFreeModel, FreeModellingProject, FlexoObject> actionType = new FlexoActionType<CreateFreeModel, FreeModellingProject, FlexoObject>(
			"create_free_model", FlexoActionType.newMenu, FlexoActionType.defaultGroup, FlexoActionType.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateFreeModel makeNewAction(FreeModellingProject focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
			return new CreateFreeModel(focusedObject, globalSelection, editor);
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
		FlexoObjectImpl.addActionForClass(CreateFreeModel.actionType, FreeModellingProject.class);
	}

	private FreeMetaModel freeMetaModel;
	private FreeModel freeModel;
	private boolean createNewMetaModel = true;
	private String freeModelName;
	private String freeModelDescription;

	CreateFreeModel(FreeModellingProject focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	protected void doAction(Object context) throws SaveResourceException {

		logger.info("Create free model");

		// Create FreeMetaModel when not existant
		// Use the same name
		if (getCreateNewMetaModel()) {
			freeMetaModel = createNewMetaModel(getFreeModelName());
		}

		CreateFMLControlledDiagramVirtualModelInstance action = CreateFMLControlledDiagramVirtualModelInstance.actionType
				.makeNewEmbeddedAction(getFocusedObject().getFreeModellingView(), null, this);
		action.setNewVirtualModelInstanceName(getFreeModelName());
		action.setNewVirtualModelInstanceTitle(getFreeModelName());
		action.setVirtualModel(freeMetaModel.getVirtualModel());

		TypedDiagramModelSlot diagramModelSlot = FMLControlledDiagramVirtualModelNature.getTypedDiagramModelSlot(freeMetaModel
				.getVirtualModel());
		TypedDiagramModelSlotInstanceConfiguration diagramModelSlotInstanceConfiguration = (TypedDiagramModelSlotInstanceConfiguration) action
				.getModelSlotInstanceConfiguration(diagramModelSlot);
		diagramModelSlotInstanceConfiguration.setOption(DefaultModelSlotInstanceConfigurationOption.CreatePrivateNewModel);
		diagramModelSlotInstanceConfiguration.setFilename(getFreeModelName() + ".diagram");
		diagramModelSlotInstanceConfiguration.setRelativePath("Diagrams/");
		diagramModelSlotInstanceConfiguration.setModelUri(FreeModel.getDiagramURI(getFocusedObject().getProject(), getFreeModelName()));
		action.doAction();

		VirtualModelInstance newVirtualModelInstance = action.getNewVirtualModelInstance();

		// Wrap into FreeModel
		try {
			freeModel = getFocusedObject().getFreeModel(newVirtualModelInstance);
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
		}
		
		getFocusedObject().getPropertyChangeSupport().firePropertyChange("freeMetaModels", null, null);
	}

	private FreeMetaModel createNewMetaModel(String metaModelName) {

		// First we create the diagram specification
		System.out.println("Creating DiagramSpecification...");
		CreateDiagramSpecification createDS = CreateDiagramSpecification.actionType.makeNewEmbeddedAction(getFocusedObject()
				.getDiagramSpecificationsFolder(), null, this);
		createDS.setNewDiagramSpecificationName(metaModelName);
		createDS.setNewDiagramSpecificationURI(FreeMetaModel.getDiagramSpecificationURI(getFocusedObject().getProject(), metaModelName));
		createDS.doAction();
		DiagramSpecification diagramSpecification = createDS.getNewDiagramSpecification();
		System.out.println("DiagramSpecification has been created: " + diagramSpecification);

		CreateDiagramPalette createPalette = CreateDiagramPalette.actionType.makeNewEmbeddedAction(diagramSpecification, null, this);
		createPalette.setNewPaletteName(FreeMetaModel.PALETTE_NAME);
		createPalette.doAction();
		System.out.println("Palette has been created: " + createPalette.getNewPalette());

		// Now we create the VirtualModel
		System.out.println("Creating VirtualModel...");
		CreateVirtualModel action = CreateVirtualModel.actionType.makeNewEmbeddedAction(getFocusedObject().getFreeModellingViewPoint(),
				null, this);
		action.setNewVirtualModelName(metaModelName);
		action.doAction();
		VirtualModel newVirtualModel = action.getNewVirtualModel();
		System.out.println("VirtualModel has been created: " + newVirtualModel);

		// Now we create the diagram model slot
		CreateModelSlot createMS = CreateModelSlot.actionType.makeNewEmbeddedAction(newVirtualModel, null, this);
		createMS.setTechnologyAdapter(getServiceManager().getTechnologyAdapterService()
				.getTechnologyAdapter(DiagramTechnologyAdapter.class));
		createMS.setModelSlotClass(TypedDiagramModelSlot.class);
		createMS.setModelSlotName("diagram");
		createMS.setMmRes(diagramSpecification.getResource());
		createMS.doAction();

		// Save
		try {
			newVirtualModel.getResource().save(null);
		} catch (SaveResourceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Wrap into FreeMetaModel
		try {
			return getFocusedObject().getFreeMetaModel(newVirtualModel);
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
			return null;
		}

	}

	private String errorMessage;

	public String getErrorMessage() {
		isValid();
		// System.out.println("valid=" + isValid());
		// System.out.println("errorMessage=" + errorMessage);
		return errorMessage;
	}

	@Override
	public boolean isValid() {

		if (StringUtils.isEmpty(freeModelName)) {
			errorMessage = FlexoLocalization.localizedForKey("no_free_model_name_defined");
			return false;
		}

		if (getFocusedObject().getFreeModel(freeModelName) != null) {
			errorMessage = FlexoLocalization.localizedForKey("a_free_model_with_that_name_already_exists");
			return false;
		}

		if (!getCreateNewMetaModel() && getFreeMetaModel() == null) {
			errorMessage = FlexoLocalization.localizedForKey("no_meta_model_defined");
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

	public FreeMetaModel getFreeMetaModel() {
		return freeMetaModel;
	}

	public void setFreeMetaModel(FreeMetaModel freeMetaModel) {
		boolean wasValid = isValid();
		this.freeMetaModel = freeMetaModel;
		getPropertyChangeSupport().firePropertyChange("freeMetaModel", null, freeMetaModel);
		getPropertyChangeSupport().firePropertyChange("isValid", wasValid, isValid());
		getPropertyChangeSupport().firePropertyChange("errorMessage", null, getErrorMessage());
	}

	public String getFreeModelName() {
		return freeModelName;
	}

	public void setFreeModelName(String freeModelName) {
		boolean wasValid = isValid();
		this.freeModelName = freeModelName;
		getPropertyChangeSupport().firePropertyChange("freeModelName", null, freeModelName);
		getPropertyChangeSupport().firePropertyChange("isValid", wasValid, isValid());
		getPropertyChangeSupport().firePropertyChange("errorMessage", null, getErrorMessage());
	}

	public boolean getCreateNewMetaModel() {
		return createNewMetaModel;
	}

	public void setCreateNewMetaModel(boolean createNewMetaModel) {
		boolean wasValid = isValid();
		this.createNewMetaModel = createNewMetaModel;
		getPropertyChangeSupport().firePropertyChange("createNewMetaModel", !createNewMetaModel, createNewMetaModel);
		getPropertyChangeSupport().firePropertyChange("isValid", wasValid, isValid());
		getPropertyChangeSupport().firePropertyChange("errorMessage", null, getErrorMessage());
	}

	public String getFreeModelDescription() {
		return freeModelDescription;
	}

	public void setFreeModelDescription(String freeModelDescription) {
		boolean wasValid = isValid();
		this.freeModelDescription = freeModelDescription;
		getPropertyChangeSupport().firePropertyChange("freeModelDescription", null, freeModelDescription);
		getPropertyChangeSupport().firePropertyChange("isValid", wasValid, isValid());
		getPropertyChangeSupport().firePropertyChange("errorMessage", null, getErrorMessage());
	}

}
