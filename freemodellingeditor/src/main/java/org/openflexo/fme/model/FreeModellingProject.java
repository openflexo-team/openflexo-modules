/*
 * (c) Copyright 2013-2014 Openflexo
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
package org.openflexo.fme.model;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openflexo.foundation.DefaultFlexoObject;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.InvalidArgumentException;
import org.openflexo.foundation.fml.ViewPoint;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rm.ViewPointResource;
import org.openflexo.foundation.fml.rt.View;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.foundation.fml.rt.rm.ViewResource;
import org.openflexo.foundation.nature.ProjectWrapper;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelNature;
import org.openflexo.technologyadapter.diagram.rm.DiagramSpecificationRepository;
import org.openflexo.technologyadapter.diagram.rm.DiagramSpecificationResource;

/**
 * Represents a "FreeModelling" project<br>
 * 
 * This class is a wrapper class above {@link FlexoProject} and provides a specific interpretation of a generic {@link FlexoProject}<br>
 * Instances of {@link FreeModellingProject} are managed and should be retrieved from {@link FreeModellingProjectNature}.
 * 
 * @see FreeModellingProjectNature
 * 
 * @author sylvain
 * 
 */
public class FreeModellingProject extends DefaultFlexoObject implements ProjectWrapper<FreeModellingProjectNature> {

	private final FlexoProject project;
	private final ViewPoint freeModellingViewPoint;
	private final View freeModellingView;
	private final Map<VirtualModel, FreeMetaModel> metaModels;
	private final Map<VirtualModelInstance, FreeModel> models;

	private final DiagramTechnologyAdapter diagramTechnologyAdapter;
	// private final DiagramSpecificationRepository dsRepository;
	private RepositoryFolder<DiagramSpecificationResource> dsFolder;

	private final FreeModellingProjectNature projectNature;

	// Do never instanciate this class but ask it to FreeModellingProjectNature
	protected FreeModellingProject(FlexoProject project, FreeModellingProjectNature projectNature) throws FileNotFoundException,
			ResourceLoadingCancelledException, InvalidArgumentException, FlexoException {
		this.project = project;
		this.projectNature = projectNature;

		ViewPointResource freeModellingViewPointResource = project.getViewPointRepository().getResource(
				project.getURI() + FreeModellingProjectNature.FREE_MODELLING_VIEWPOINT_RELATIVE_URI);

		ViewResource freeModellingViewResource = project.getViewLibrary().getResource(
				project.getURI() + FreeModellingProjectNature.FREE_MODELLING_VIEW_RELATIVE_URI);

		if (freeModellingViewPointResource == null) {
			throw new InvalidArgumentException("Could not retrieve FreeModellingViewPoint resource (searched uri="
					+ (project.getURI() + FreeModellingProjectNature.FREE_MODELLING_VIEWPOINT_RELATIVE_URI) + ")");
		}
		if (freeModellingViewResource == null) {
			throw new InvalidArgumentException("Could not retrieve FreeModellingView resource (searched uri="
					+ (project.getURI() + FreeModellingProjectNature.FREE_MODELLING_VIEW_RELATIVE_URI) + ")");
		}

		try {
			freeModellingViewPoint = freeModellingViewPointResource.getResourceData(null);
			freeModellingView = freeModellingViewResource.getResourceData(null);
		} catch (Exception e) {
			throw new FlexoException(e);
		}

		if (freeModellingViewPoint == null) {
			throw new InvalidArgumentException("Could not load FreeModellingViewPoint");
		}
		if (freeModellingView == null) {
			throw new InvalidArgumentException("Could not load FreeModellingView");
		}

		metaModels = new HashMap<VirtualModel, FreeMetaModel>();
		models = new HashMap<VirtualModelInstance, FreeModel>();

		diagramTechnologyAdapter = project.getServiceManager().getTechnologyAdapterService()
				.getTechnologyAdapter(DiagramTechnologyAdapter.class);
		DiagramSpecificationRepository dsRepository = project.getRepository(DiagramSpecificationRepository.class, diagramTechnologyAdapter);

		dsFolder = dsRepository.getFolderWithName(FreeModellingProjectNature.DIAGRAM_SPECIFICATIONS_FOLDER);
		if (dsFolder == null) {
			dsFolder = dsRepository.createNewFolder(FreeModellingProjectNature.DIAGRAM_SPECIFICATIONS_FOLDER);
		}

	}

	public String getName() {
		return project.getProjectName();
	}

	@Override
	public FlexoProject getProject() {
		return project;
	}

	public ViewPoint getFreeModellingViewPoint() {
		return freeModellingViewPoint;
	}

	public List<FreeMetaModel> getFreeMetaModels() {
		List<FreeMetaModel> returned = new ArrayList<FreeMetaModel>();
		for (VirtualModel vm : getFreeModellingViewPoint().getVirtualModels()) {
			try {
				returned.add(getFreeMetaModel(vm));
			} catch (InvalidArgumentException e) {
				FreeModellingProjectNature.logger.warning(e.getMessage());
			}
		}
		return returned;
	}

	public FreeMetaModel getFreeMetaModel(VirtualModel virtualModel) throws InvalidArgumentException {
		if (virtualModel.hasNature(FMLControlledDiagramVirtualModelNature.INSTANCE)) {
			FreeMetaModel returned = metaModels.get(virtualModel);
			if (returned == null) {
				returned = new FreeMetaModel(virtualModel, this);
				metaModels.put(virtualModel, returned);
			}
			return returned;
		} else {
			throw new InvalidArgumentException("VirtualModel " + virtualModel + " does not have the FMLControlledDiagramVirtualModelNature");
		}
	}

	public FreeMetaModel getFreeMetaModel(String freeMetaModelName) {
		for (FreeMetaModel freeMetaModel : getFreeMetaModels()) {
			if (freeMetaModel.getName().equals(freeMetaModelName)) {
				return freeMetaModel;
			}
		}
		return null;
	}

	public View getFreeModellingView() {
		return freeModellingView;
	}

	public List<FreeModel> getFreeModels() {
		List<FreeModel> returned = new ArrayList<FreeModel>();
		for (VirtualModelInstance vmi : getFreeModellingView().getVirtualModelInstances()) {
			try {
				returned.add(getFreeModel(vmi));
			} catch (InvalidArgumentException e) {
				FreeModellingProjectNature.logger.warning(e.getMessage());
			}
		}
		return returned;
	}
	
	public List<FreeModel> getFreeModels(FreeMetaModel metamodel) {
		List<FreeModel> returned = new ArrayList<FreeModel>();
		for (VirtualModelInstance vmi : getFreeModellingView().getVirtualModelInstances()) {
			try {
				if(vmi.getVirtualModel().equals(metamodel.getVirtualModel())){
					returned.add(getFreeModel(vmi));
				}
			} catch (InvalidArgumentException e) {
				FreeModellingProjectNature.logger.warning(e.getMessage());
			}
		}
		return returned;
	}

	public FreeModel getFreeModel(VirtualModelInstance virtualModelInstance) throws InvalidArgumentException {
		FreeModel returned = models.get(virtualModelInstance);
		if (returned == null) {
			returned = new FreeModel(virtualModelInstance, this);
			models.put(virtualModelInstance, returned);
			getPropertyChangeSupport().firePropertyChange("freeModels", null, models);
		}
		return returned;
	}

	public FreeModel getFreeModel(String freeModelName) {
		for (FreeModel freeModel : getFreeModels()) {
			if (freeModel.getName().equals(freeModelName)) {
				return freeModel;
			}
		}
		return null;
	}

	public void removeFreeModel(FreeModel model){
		models.remove(model.getVirtualModelInstance());
		getFreeModellingView().removeFromVirtualModelInstances(model.getVirtualModelInstance());
	}
	
	public DiagramTechnologyAdapter getDiagramTechnologyAdapter() {
		return diagramTechnologyAdapter;
	}

	/*public DiagramSpecificationRepository getDiagramSpecificationRepository() {
		return dsRepository;
	}*/

	public RepositoryFolder<DiagramSpecificationResource> getDiagramSpecificationsFolder() {
		return dsFolder;
	}

	@Override
	public FreeModellingProjectNature getProjectNature() {
		return projectNature;
	}

}