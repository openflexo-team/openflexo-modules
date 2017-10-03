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

import org.openflexo.fme.model.FreeMetaModel;
import org.openflexo.fme.model.FreeModel;
import org.openflexo.fme.model.FreeModellingProject;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.InvalidArgumentException;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.action.CreateContainedVirtualModel;
import org.openflexo.foundation.fml.action.CreateModelSlot;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.localization.LocalizedDelegate;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelNature;
import org.openflexo.technologyadapter.diagram.fml.action.CreateDiagramPalette;
import org.openflexo.technologyadapter.diagram.fml.action.CreateDiagramSpecification;
import org.openflexo.technologyadapter.diagram.fml.action.CreateExampleDiagram;
import org.openflexo.technologyadapter.diagram.fml.action.CreateFMLControlledDiagramVirtualModelInstance;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.model.action.CreateDiagram;
import org.openflexo.technologyadapter.diagram.rm.DiagramRepository;
import org.openflexo.technologyadapter.diagram.rm.DiagramResource;
import org.openflexo.toolbox.StringUtils;

/**
 * This action is used to create a new {@link FreeModel} in a {@link FreeModellingProject}<br>
 * 
 * New {@link FreeModel} might be created while a new associated {@link FreeMetaModel} is created, or using an existing one.
 * 
 * @author sylvain
 * 
 */
public class AbstractCreateFreeModel<A extends AbstractCreateFreeModel<A>> extends FlexoAction<A, FreeModellingProject, FlexoObject> {

	private static final Logger logger = Logger.getLogger(AbstractCreateFreeModel.class.getPackage().getName());

	private FreeMetaModel freeMetaModel;
	protected FreeModel freeModel;
	private boolean createNewMetaModel = true;
	private String freeModelName;
	private String freeModelDescription;

	AbstractCreateFreeModel(FlexoActionFactory<A, FreeModellingProject, FlexoObject> actionType, FreeModellingProject focusedObject,
			Vector<FlexoObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	public LocalizedDelegate getLocales() {
		if (getFocusedObject() != null) {
			return getFocusedObject().getLocales();
		}
		return super.getLocales();
	}

	@Override
	protected void doAction(Object context) throws SaveResourceException {

		freeModel = createNewFreeModel();
	}

	protected FreeModel createNewFreeModel() {

		FreeModel returned = null;

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

		/*TypedDiagramModelSlot diagramModelSlot = FMLControlledDiagramVirtualModelNature
				.getTypedDiagramModelSlot(freeMetaModel.getVirtualModel());
		TypedDiagramModelSlotInstanceConfiguration diagramModelSlotInstanceConfiguration = (TypedDiagramModelSlotInstanceConfiguration) action
				.getModelSlotInstanceConfiguration(diagramModelSlot);
		diagramModelSlotInstanceConfiguration.setOption(DefaultModelSlotInstanceConfigurationOption.CreatePrivateNewModel);
		diagramModelSlotInstanceConfiguration.setFilename(getFreeModelName() + ".diagram");
		diagramModelSlotInstanceConfiguration.setRelativePath("Diagrams/");
		diagramModelSlotInstanceConfiguration.setModelUri(FreeModel.getDiagramURI(getFocusedObject().getProject(), getFreeModelName()));
		*/

		action.doAction();

		FMLRTVirtualModelInstance newVirtualModelInstance = action.getNewVirtualModelInstance();

		DiagramTechnologyAdapter diagramTA = getServiceManager().getTechnologyAdapterService()
				.getTechnologyAdapter(DiagramTechnologyAdapter.class);

		DiagramRepository diagramRepository = diagramTA.getDiagramRepository(getFocusedObject().getProject());
		RepositoryFolder<DiagramResource, ?> rootFolder = diagramRepository.getRootFolder();

		if (rootFolder.getSerializationArtefact() instanceof File) {
			File prjDir = (File) rootFolder.getSerializationArtefact();
			File diagramDir = new File(prjDir, "Diagrams");
			if (!diagramDir.exists()) {
				diagramDir.mkdirs();
			}
		}

		RepositoryFolder<DiagramResource, ?> diagramFolder = rootFolder.getFolderNamed("Diagrams");
		if (diagramFolder == null) {
			diagramFolder = diagramRepository.createNewFolder("Diagrams", rootFolder);
		}

		CreateDiagram createDiagram = CreateDiagram.actionType.makeNewEmbeddedAction(diagramFolder, null, this);
		createDiagram.setDiagramName(getFreeModelName() + ".diagram");
		createDiagram.setDiagramTitle(getFreeModelDescription());
		createDiagram.setDiagramSpecification(freeMetaModel.getDiagramSpecification());

		createDiagram.doAction();

		DiagramResource newDiagramResource = createDiagram.getNewDiagramResource();
		TypedDiagramModelSlot diagramModelSlot = FMLControlledDiagramVirtualModelNature
				.getTypedDiagramModelSlot(freeMetaModel.getVirtualModel());

		System.out.println("Le nouveau diagramme res : " + newDiagramResource);
		System.out.println("Le nouveau diagramme : " + newDiagramResource.getLoadedResourceData());

		newVirtualModelInstance.setFlexoPropertyValue(diagramModelSlot, newDiagramResource.getLoadedResourceData());

		// Wrap into FreeModel
		try {
			returned = getFocusedObject().getFreeModel(newVirtualModelInstance);
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
		}

		getFocusedObject().getPropertyChangeSupport().firePropertyChange("freeMetaModels", null, null);

		return returned;
	}

	private FreeMetaModel createNewMetaModel(String metaModelName) {

		// First we create the diagram specification
		System.out.println("Creating DiagramSpecification...");
		CreateDiagramSpecification createDS = CreateDiagramSpecification.actionType
				.makeNewEmbeddedAction(getFocusedObject().getDiagramSpecificationsFolder(), null, this);
		createDS.setNewDiagramSpecificationName(metaModelName);
		createDS.setNewDiagramSpecificationURI(FreeMetaModel.getDiagramSpecificationURI(getFocusedObject().getProject(), metaModelName));
		createDS.doAction();
		DiagramSpecification diagramSpecification = createDS.getNewDiagramSpecification();
		System.out.println("DiagramSpecification has been created: " + diagramSpecification);

		CreateExampleDiagram createExampleDiagram = CreateExampleDiagram.actionType.makeNewEmbeddedAction(diagramSpecification, null, this);
		createExampleDiagram.setNewDiagramName("Default");
		createExampleDiagram.setNewDiagramTitle("Default example diagram");
		createExampleDiagram.doAction();

		CreateDiagramPalette createPalette = CreateDiagramPalette.actionType.makeNewEmbeddedAction(diagramSpecification, null, this);
		createPalette.setNewPaletteName(FreeMetaModel.PALETTE_NAME);
		createPalette.doAction();
		System.out.println("Palette has been created: " + createPalette.getNewPalette());

		// Now we create the VirtualModel
		System.out.println("Creating VirtualModel...");
		CreateContainedVirtualModel action = CreateContainedVirtualModel.actionType
				.makeNewEmbeddedAction(getFocusedObject().getFreeModellingViewPoint(), null, this);
		action.setNewVirtualModelName(metaModelName);
		action.doAction();
		VirtualModel newVirtualModel = action.getNewVirtualModel();
		System.out.println("VirtualModel has been created: " + newVirtualModel);

		// Now we create the diagram model slot
		CreateModelSlot createMS = CreateModelSlot.actionType.makeNewEmbeddedAction(newVirtualModel, null, this);
		createMS.setTechnologyAdapter(
				getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(DiagramTechnologyAdapter.class));
		createMS.setModelSlotClass(TypedDiagramModelSlot.class);
		createMS.setModelSlotName("diagram");
		createMS.setMmRes(diagramSpecification.getResource());
		createMS.doAction();

		// Save
		try {
			newVirtualModel.getResource().save(null);
		} catch (SaveResourceException e1) {
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

	@Override
	public boolean isValid() {

		if (StringUtils.isEmpty(freeModelName)) {
			return false;
		}

		if (getFocusedObject() != null && getFocusedObject().getFreeModel(freeModelName) != null) {
			return false;
		}

		if (!getCreateNewMetaModel() && getFreeMetaModel() == null) {
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
	}

	public String getFreeModelName() {
		return freeModelName;
	}

	public void setFreeModelName(String freeModelName) {
		boolean wasValid = isValid();
		this.freeModelName = freeModelName;
		getPropertyChangeSupport().firePropertyChange("freeModelName", null, freeModelName);
		getPropertyChangeSupport().firePropertyChange("isValid", wasValid, isValid());
	}

	public boolean getCreateNewMetaModel() {
		return createNewMetaModel;
	}

	public void setCreateNewMetaModel(boolean createNewMetaModel) {
		boolean wasValid = isValid();
		this.createNewMetaModel = createNewMetaModel;
		getPropertyChangeSupport().firePropertyChange("createNewMetaModel", !createNewMetaModel, createNewMetaModel);
		getPropertyChangeSupport().firePropertyChange("isValid", wasValid, isValid());
	}

	public String getFreeModelDescription() {
		return freeModelDescription;
	}

	public void setFreeModelDescription(String freeModelDescription) {
		boolean wasValid = isValid();
		this.freeModelDescription = freeModelDescription;
		getPropertyChangeSupport().firePropertyChange("freeModelDescription", null, freeModelDescription);
		getPropertyChangeSupport().firePropertyChange("isValid", wasValid, isValid());
	}

}
