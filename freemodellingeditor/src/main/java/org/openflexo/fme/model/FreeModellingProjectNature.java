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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.nature.ProjectNature;
import org.openflexo.foundation.nature.ProjectNatureService;
import org.openflexo.foundation.view.View;
import org.openflexo.foundation.view.action.CreateView;
import org.openflexo.foundation.view.rm.ViewResource;
import org.openflexo.foundation.viewpoint.ViewPoint;
import org.openflexo.foundation.viewpoint.action.CreateViewPoint;
import org.openflexo.foundation.viewpoint.rm.ViewPointResource;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.rm.DiagramSpecificationRepository;

/**
 * Defines the nature of a project to be interpreted as a FreeModelProject<br>
 * 
 * Such project contains exactely one ViewPoint (the end-user never see it) where are defined all VirtualModel which are the basis of
 * {@link FreeMetaModel}<br>
 * 
 * A {@link FlexoProject} which has this nature gives access to a list of {@link FreeModel} (where one {@link FreeModel} is conform to one
 * or many FreeMetaModel). Such project also gives access to the list of {@link FreeMetaModel} encoded in this project
 * 
 * @author sylvain
 * 
 */
public class FreeModellingProjectNature implements ProjectNature<FreeModellingProjectNature, FreeModellingProject> {

	static final Logger logger = FlexoLogger.getLogger(FreeModellingProjectNature.class.getPackage().getName());

	private ProjectNatureService projectNatureService;

	public static final String FREE_MODELLING_VIEW_NAME = "FreeModellingView";
	public static final String FREE_MODELLING_VIEW_RELATIVE_URI = "/" + FREE_MODELLING_VIEW_NAME;
	public static final String FREE_MODELLING_VIEWPOINT_NAME = "FreeModellingViewPoint";
	public static final String FREE_MODELLING_VIEWPOINT_RELATIVE_URI = "/" + FREE_MODELLING_VIEWPOINT_NAME;
	public static final String DIAGRAM_SPECIFICATIONS_FOLDER = "DiagramSpecifications";

	private final Map<FlexoProject, FreeModellingProject> freeModellingProjects;

	// Never call this: this is done via services
	public FreeModellingProjectNature() {
		freeModellingProjects = new HashMap<FlexoProject, FreeModellingProject>();
	}

	@Override
	public void setProjectNatureService(ProjectNatureService projectNatureService) {
		this.projectNatureService = projectNatureService;
	}

	@Override
	public ProjectNatureService getProjectNatureService() {
		return projectNatureService;
	}

	/**
	 * Return boolean indicating if supplied project might be interpreted according to this nature
	 * 
	 * @param concept
	 * @return
	 */
	@Override
	public boolean hasNature(FlexoProject project) {
		if (project == null) {
			return false;
		}
		if (project.getViewPointRepository().getAllResources().size() == 0) {
			return false;
		}
		if (project.getViewLibrary().getAllResources().size() == 0) {
			return false;
		}
		FreeModellingProject factory = getFreeModellingProject(project);
		if (factory == null) {
			return false;
		}
		return true;
	}

	/**
	 * Return wrapping object representing the interpretation of supplied project with this nature
	 * 
	 * @param project
	 * @return
	 */
	@Override
	public FreeModellingProject getProjectWrapper(FlexoProject project) {
		return getFreeModellingProject(project);
	}

	/*public static FreeModellingProject getFreeModellingProject(FlexoProject project) {
		return INSTANCE._getFreeModellingProject(project);
	}*/

	/**
	 * Gives to supplied FlexoProject this nature
	 * 
	 * @return
	 */
	/*public static void givesNature(FlexoProject project, FlexoEditor editor) {
		INSTANCE._givesNature(project, editor);
	}*/

	/*public static List<FreeModel> getFreeModels(FlexoProject project) {
		return INSTANCE._getFreeModels(project);
	}

	public static List<FreeMetaModel> getFreeMetaModels(FlexoProject project) {
		return INSTANCE._getFreeMetaModels(project);
	}

	public static ViewPoint getFreeModellingViewPoint(FlexoProject project) {
		return INSTANCE._getFreeModellingViewPoint(project);
	}

	public static View getFreeModellingView(FlexoProject project) {
		return INSTANCE._getFreeModellingView(project);
	}*/

	/**
	 * Gives to supplied FlexoProject this nature
	 * 
	 * @return
	 */
	@Override
	public void givesNature(FlexoProject project, FlexoEditor editor) {

		ViewPointResource freeModellingViewPointResource = project.getViewPointRepository().getResource(
				project.getURI() + FREE_MODELLING_VIEWPOINT_RELATIVE_URI);

		if (freeModellingViewPointResource == null) {
			CreateViewPoint action = CreateViewPoint.actionType.makeNewAction(project.getViewPointRepository().getRootFolder(), null,
					editor);
			action.setNewViewPointName(FREE_MODELLING_VIEWPOINT_NAME);
			action.setNewViewPointURI(project.getURI() + FREE_MODELLING_VIEWPOINT_RELATIVE_URI);
			action.setNewViewPointDescription("This is the generic ViewPoint storing all FreeModelling meta-models");
			action.doAction();
			freeModellingViewPointResource = (ViewPointResource) action.getNewViewPoint().getResource();
		}

		ViewResource freeModellingViewResource = project.getViewLibrary().getResource(
				project.getURI() + FREE_MODELLING_VIEWPOINT_RELATIVE_URI);

		if (freeModellingViewResource == null) {
			CreateView action = CreateView.actionType.makeNewAction(project.getViewLibrary().getRootFolder(), null, editor);
			action.setNewViewName(FREE_MODELLING_VIEW_NAME);
			action.setNewViewTitle(FREE_MODELLING_VIEW_NAME);
			action.setViewpointResource(freeModellingViewPointResource);
			action.doAction();
			freeModellingViewResource = (ViewResource) action.getNewView().getResource();
		}

		DiagramTechnologyAdapter diagramTechnologyAdapter = project.getServiceManager().getTechnologyAdapterService()
				.getTechnologyAdapter(DiagramTechnologyAdapter.class);
		DiagramSpecificationRepository dsRepository = project.getRepository(DiagramSpecificationRepository.class, diagramTechnologyAdapter);
		dsRepository.createNewFolder(DIAGRAM_SPECIFICATIONS_FOLDER);

	}

	public List<FreeModel> getFreeModels(FlexoProject project) {
		FreeModellingProject freeModellingProject = getFreeModellingProject(project);
		if (freeModellingProject == null) {
			return null;
		}
		return freeModellingProject.getFreeModels();
	}

	public List<FreeMetaModel> getFreeMetaModels(FlexoProject project) {
		FreeModellingProject freeModellingProject = getFreeModellingProject(project);
		if (freeModellingProject == null) {
			return null;
		}
		return freeModellingProject.getFreeMetaModels();
	}

	public ViewPoint getFreeModellingViewPoint(FlexoProject project) {
		FreeModellingProject factory = getFreeModellingProject(project);
		if (factory == null) {
			return null;
		}
		return factory.getFreeModellingViewPoint();
	}

	public View getFreeModellingView(FlexoProject project) {
		FreeModellingProject freeModellingProject = getFreeModellingProject(project);
		if (freeModellingProject == null) {
			return null;
		}
		return freeModellingProject.getFreeModellingView();
	}

	public FreeModellingProject getFreeModellingProject(FlexoProject project) {
		FreeModellingProject returned = freeModellingProjects.get(project);
		if (returned == null) {
			try {
				returned = new FreeModellingProject(project, this);
				freeModellingProjects.put(project, returned);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return returned;
	}

}
