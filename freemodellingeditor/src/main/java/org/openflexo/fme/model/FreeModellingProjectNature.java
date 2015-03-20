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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.openflexo.fme.model.action.GivesFMENature;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.ViewPoint;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.View;
import org.openflexo.foundation.nature.ProjectNature;
import org.openflexo.foundation.nature.ProjectNatureService;
import org.openflexo.logging.FlexoLogger;

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
		GivesFMENature action = GivesFMENature.actionType.makeNewAction(project, null, editor);
		action.doAction();
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

	public String getInstanceName(FlexoConceptInstance flexoConceptInstance) {
		FlexoRole<String> nameRole = (FlexoRole<String>) flexoConceptInstance.getFlexoConcept().getAccessibleProperty(
				FreeMetaModel.NAME_ROLE_NAME);
		return flexoConceptInstance.getFlexoActor(nameRole);
	}

	public void setInstanceName(FlexoConceptInstance flexoConceptInstance, String value) {
		FlexoRole<String> nameRole = (FlexoRole<String>) flexoConceptInstance.getFlexoConcept().getAccessibleProperty(
				FreeMetaModel.NAME_ROLE_NAME);
		flexoConceptInstance.setFlexoActor(value, nameRole);
	}

}
