package org.openflexo.module.cartoeditor.model;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.nature.ProjectNature;
import org.openflexo.foundation.nature.ProjectNatureService;
import org.openflexo.foundation.view.action.CreateView;
import org.openflexo.foundation.view.rm.ViewResource;
import org.openflexo.foundation.viewpoint.action.CreateViewPoint;
import org.openflexo.foundation.viewpoint.rm.ViewPointResource;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;

/**
 * Created by eloubout on 05/09/14.
 */
public class CEProjectNature implements ProjectNature<CEProjectNature, CEProject> {

	private static final Logger LOGGER = Logger.getLogger(CEProjectNature.class.getPackage().getName());

	public static final String CARTO_EDITOR_VIEW_NAME = "CEView";
	public static final String CARTO_EDITOR_RELATIVE_VIEW_URI = "/" + CARTO_EDITOR_VIEW_NAME;
	public static final String CARTO_EDITOR_VIEWPOINT_NAME = "CEViewpoint";
	public static final String CARTO_EDITOR_RELATIVE_VIEWPOINT_URI = "/" + CARTO_EDITOR_VIEWPOINT_NAME;
	public static final String DIAGRAM_SPECIFICATIONS_FOLDER = "DiagramSpecifications";

	private ProjectNatureService projectNatureService;

	private Map<FlexoProject, CEProject> ceProjects;

	public CEProjectNature() {
		ceProjects = new HashMap<FlexoProject, CEProject>();
	}

	@Override
	public void givesNature(final FlexoProject project, final FlexoEditor editor) {

		ViewPointResource ceViewPointResource = project.getViewPointRepository().getResource(
				project.getURI() + CARTO_EDITOR_RELATIVE_VIEWPOINT_URI);
		if (ceViewPointResource == null) {
			CreateViewPoint action = CreateViewPoint.actionType.makeNewAction(project.getViewPointRepository().getRootFolder(), null,
					editor);
			action.setNewViewPointName(CARTO_EDITOR_VIEWPOINT_NAME);
			action.setNewViewPointURI(project.getURI() + CARTO_EDITOR_RELATIVE_VIEWPOINT_URI);
			action.setNewViewPointDescription("This is the generic ViewPoint storing all Cartography Editor meta-models");
			action.doAction();
			ceViewPointResource = (ViewPointResource) action.getNewViewPoint().getResource();
		}

		ViewResource ceViewResource = project.getViewLibrary().getResource(project.getURI() + CARTO_EDITOR_RELATIVE_VIEW_URI);
		if (ceViewResource == null) {
			CreateView action = CreateView.actionType.makeNewAction(project.getViewLibrary().getRootFolder(), null, editor);
			action.setNewViewName(CARTO_EDITOR_VIEW_NAME);
			action.setNewViewTitle(CARTO_EDITOR_VIEW_NAME);
			action.setViewpointResource(ceViewPointResource);
			action.doAction();
		}
	}

	@Override
	public CEProject getProjectWrapper(final FlexoProject files) {
		return getCEProject(files);
	}

	@Override
	public ProjectNatureService getProjectNatureService() {
		return this.projectNatureService;
	}

	@Override
	public void setProjectNatureService(ProjectNatureService projectNatureService) {
		this.projectNatureService = projectNatureService;
	}

	@Override
	public boolean hasNature(final FlexoProject project) {
		if (project == null || project.getViewPointRepository().getAllResources().size() == 0
				|| project.getViewLibrary().getAllResources().size() == 0 || getCEProject(project) == null) {
			return false;
		}

		return true;
	}

	public CEProject getCEProject(FlexoProject project) {
		CEProject returned = ceProjects.get(project);
		if (returned == null) {
			try {
				ModelFactory factory = new ModelFactory(CEProject.class);
				returned = factory.newInstance(CEProject.class);
				returned.init(project, this);
				ceProjects.put(project, returned);
			} catch (ModelDefinitionException e) {
				LOGGER.log(Level.SEVERE, "Error while initializing new CEProject", e);
			}
		}
		return returned;
	}
}
