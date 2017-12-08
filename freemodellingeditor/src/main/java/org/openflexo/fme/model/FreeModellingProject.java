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

package org.openflexo.fme.model;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openflexo.ApplicationContext;
import org.openflexo.fme.FreeModellingEditor;
import org.openflexo.foundation.DefaultFlexoObject;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.InvalidArgumentException;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rm.VirtualModelResource;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.foundation.fml.rt.rm.FMLRTVirtualModelInstanceResource;
import org.openflexo.foundation.fml.rt.rm.FMLRTVirtualModelInstanceResourceFactory;
import org.openflexo.foundation.nature.ProjectWrapper;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.localization.LocalizedDelegate;
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
	private final VirtualModel freeModellingViewPoint;
	private final FMLRTVirtualModelInstance freeModellingView;
	private final Map<VirtualModel, FMEFreeModel> metaModels;
	private final Map<FMLRTVirtualModelInstance, FMEFreeModelInstance> models;

	private final DiagramTechnologyAdapter diagramTechnologyAdapter;
	// private final DiagramSpecificationRepository dsRepository;
	private RepositoryFolder<DiagramSpecificationResource, ?> dsFolder;

	private final FreeModellingProjectNature projectNature;

	// Do never instanciate this class but ask it to FreeModellingProjectNature
	protected FreeModellingProject(FlexoProject<?> project, FreeModellingProjectNature projectNature)
			throws FileNotFoundException, ResourceLoadingCancelledException, InvalidArgumentException, FlexoException {
		this.project = project;
		this.projectNature = projectNature;

		VirtualModelResource freeModellingViewPointResource = project.getVirtualModelRepository()
				.getResource(project.getProjectURI() + FreeModellingProjectNature.FREE_MODELLING_VIEWPOINT_RELATIVE_URI);
		// Attempt to retrieve viewpoints for old projects
		if (freeModellingViewPointResource == null) {
			freeModellingViewPointResource = project.getVirtualModelRepository()
					.getResource(project.getProjectURI() + "/" + FreeModellingProjectNature.FREE_MODELLING_VIEWPOINT_NAME);
		}

		FMLRTVirtualModelInstanceResource freeModellingViewResource = project.getVirtualModelInstanceRepository()
				.getResource(project.getProjectURI() + FreeModellingProjectNature.FREE_MODELLING_VIEW_RELATIVE_URI);

		if (freeModellingViewPointResource == null) {
			throw new InvalidArgumentException("Could not retrieve FreeModellingViewPoint resource (searched uri="
					+ (project.getProjectURI() + FreeModellingProjectNature.FREE_MODELLING_VIEWPOINT_RELATIVE_URI) + ")");
		}
		if (freeModellingViewResource == null) {
			throw new InvalidArgumentException("Could not retrieve FreeModellingView resource (searched uri="
					+ (project.getProjectURI() + FreeModellingProjectNature.FREE_MODELLING_VIEW_RELATIVE_URI) + ")");
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

		metaModels = new HashMap<VirtualModel, FMEFreeModel>();
		models = new HashMap<FMLRTVirtualModelInstance, FMEFreeModelInstance>();

		diagramTechnologyAdapter = project.getServiceManager().getTechnologyAdapterService()
				.getTechnologyAdapter(DiagramTechnologyAdapter.class);
		DiagramSpecificationRepository<?> dsRepository = diagramTechnologyAdapter.getDiagramSpecificationRepository(project);

		dsFolder = dsRepository.getFolderWithName(FreeModellingProjectNature.DIAGRAM_SPECIFICATIONS_FOLDER);
		if (dsFolder == null) {
			dsFolder = dsRepository.createNewFolder(FreeModellingProjectNature.DIAGRAM_SPECIFICATIONS_FOLDER);
		}

	}

	@Override
	public LocalizedDelegate getLocales() {
		if (getProject() != null && getProject().getServiceManager() instanceof ApplicationContext) {
			return ((ApplicationContext) getProject().getServiceManager()).getModuleLoader().getModule(FreeModellingEditor.class)
					.getLoadedModuleInstance().getLocales();
		}
		return super.getLocales();
	}

	public String getName() {
		return project.getProjectName();
	}

	@Override
	public FlexoProject getProject() {
		return project;
	}

	public VirtualModel getFreeModellingViewPoint() {
		return freeModellingViewPoint;
	}

	public List<FMEFreeModel> getFreeMetaModels() {
		List<FMEFreeModel> returned = new ArrayList<FMEFreeModel>();
		getFreeModellingViewPoint().loadContainedVirtualModelsWhenUnloaded();
		for (VirtualModel vm : getFreeModellingViewPoint().getVirtualModels()) {
			try {
				returned.add(getFreeMetaModel(vm));
			} catch (InvalidArgumentException e) {
				FreeModellingProjectNature.logger.warning(e.getMessage());
			}
		}
		return returned;
	}

	public FMEFreeModel getFreeMetaModel(VirtualModel virtualModel) throws InvalidArgumentException {
		if (virtualModel.hasNature(FMLControlledDiagramVirtualModelNature.INSTANCE)) {
			FMEFreeModel returned = metaModels.get(virtualModel);
			if (returned == null) {
				returned = new FMEFreeModel(virtualModel, this);
				metaModels.put(virtualModel, returned);
			}
			return returned;
		}
		else {
			throw new InvalidArgumentException(
					"VirtualModel " + virtualModel + " does not have the FMLControlledDiagramVirtualModelNature");
		}
	}

	public FMEFreeModel getFreeMetaModel(String freeMetaModelName) {
		for (FMEFreeModel freeMetaModel : getFreeMetaModels()) {
			if (freeMetaModel.getName().equals(freeMetaModelName)) {
				return freeMetaModel;
			}
		}
		return null;
	}

	public FMLRTVirtualModelInstance getFreeModellingView() {
		return freeModellingView;
	}

	public List<FMEFreeModelInstance> getFreeModels() {
		List<FMEFreeModelInstance> returned = new ArrayList<FMEFreeModelInstance>();
		for (VirtualModelInstance<?, ?> vmi : getFreeModellingView().getVirtualModelInstances()) {
			try {
				if (vmi instanceof FMLRTVirtualModelInstance) {
					returned.add(getFreeModel((FMLRTVirtualModelInstance) vmi));
				}
			} catch (InvalidArgumentException e) {
				FreeModellingProjectNature.logger.warning(e.getMessage());
			}
		}
		return returned;
	}

	public List<FMEFreeModelInstance> getFreeModels(FMEFreeModel metamodel) {
		List<FMEFreeModelInstance> returned = new ArrayList<FMEFreeModelInstance>();
		for (VirtualModelInstance<?, ?> vmi : getFreeModellingView().getVirtualModelInstances()) {
			try {
				if (vmi instanceof FMLRTVirtualModelInstance && vmi.getVirtualModel().equals(metamodel.getVirtualModel())) {
					returned.add(getFreeModel((FMLRTVirtualModelInstance) vmi));
				}
			} catch (InvalidArgumentException e) {
				FreeModellingProjectNature.logger.warning(e.getMessage());
			}
		}
		return returned;
	}

	public FMEFreeModelInstance getFreeModel(FMLRTVirtualModelInstance virtualModelInstance) throws InvalidArgumentException {
		FMEFreeModelInstance returned = models.get(virtualModelInstance);
		if (returned == null) {
			returned = new FMEFreeModelInstance(virtualModelInstance, this);
			models.put(virtualModelInstance, returned);
			getPropertyChangeSupport().firePropertyChange("freeModels", null, models);
		}
		return returned;
	}

	public FMEFreeModelInstance getFreeModel(String freeModelName) {
		for (FMEFreeModelInstance freeModel : getFreeModels()) {
			if (freeModel.getName().equals(freeModelName)) {
				return freeModel;
			}
			if (freeModel.getName().equals(freeModelName + FMLRTVirtualModelInstanceResourceFactory.FML_RT_SUFFIX)) {
				return freeModel;
			}
		}
		return null;
	}

	public void removeFreeModel(FMEFreeModelInstance model) {
		models.remove(model.getVirtualModelInstance());
		getFreeModellingView().removeFromVirtualModelInstances(model.getVirtualModelInstance());
	}

	public DiagramTechnologyAdapter getDiagramTechnologyAdapter() {
		return diagramTechnologyAdapter;
	}

	public RepositoryFolder<DiagramSpecificationResource, ?> getDiagramSpecificationsFolder() {
		return dsFolder;
	}

	@Override
	public FreeModellingProjectNature getProjectNature() {
		return projectNature;
	}

}
