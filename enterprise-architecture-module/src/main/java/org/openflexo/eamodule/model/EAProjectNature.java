/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Formose prototype, a component of the software infrastructure 
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

package org.openflexo.eamodule.model;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.rm.FMLRTVirtualModelInstanceResource;
import org.openflexo.foundation.nature.ProjectNature;
import org.openflexo.foundation.nature.ProjectNatureService;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.modelers.ModelersConstants;

/**
 * This class is used to interpret a {@link FlexoProject} as a {@link EAProject}<br>
 * 
 * A {@link FlexoProject} has the {@link EAProject} nature if it contains at least a view conform to formose viewpoint<br>
 * The first found view will be considered as the Formose View
 * 
 * @author sylvain
 */
public class EAProjectNature implements ProjectNature<EAProjectNature, EAProject> {

	private static final Logger LOGGER = Logger.getLogger(EAProjectNature.class.getPackage().getName());

	private ProjectNatureService projectNatureService;

	private final Map<FlexoProject, EAProject> projectsMap;

	public EAProjectNature() {
		projectsMap = new HashMap<FlexoProject, EAProject>();
	}

	@Override
	public void givesNature(final FlexoProject project, final FlexoEditor editor) {
		// Nothing to do
	}

	@Override
	public EAProject getProjectWrapper(final FlexoProject project) {
		return getEAProject(project);
	}

	@Override
	public ProjectNatureService getProjectNatureService() {
		return this.projectNatureService;
	}

	@Override
	public void setProjectNatureService(ProjectNatureService projectNatureService) {
		this.projectNatureService = projectNatureService;
	}

	/**
	 * Return boolean indicating if supplied concept might be interpreted according to this nature<br>
	 * Always return true
	 * 
	 * @param project
	 * @return
	 */
	@Override
	public boolean hasNature(final FlexoProject project) {
		return true;
	}

	public VirtualModel getBPMNVirtualModel(FlexoServiceManager serviceManager) {
		try {
			return serviceManager.getVirtualModelLibrary().getVirtualModel(ModelersConstants.BPMN_EDITOR_URI);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * If project has nature and is not already referenced, will reference it. Either way return CEContext.
	 * 
	 * @param project
	 *            with or without a set context
	 * @return Context associated to project
	 */
	public EAProject getEAProject(FlexoProject project) {
		if (project == null) {
			return null;
		}
		EAProject returned = projectsMap.get(project);
		if (returned == null) {
			try {
				ModelFactory factory = new ModelFactory(EAProject.class);
				returned = factory.newInstance(EAProject.class);
				returned.init(project, this);
				projectsMap.put(project, returned);
			} catch (ModelDefinitionException e) {
				LOGGER.log(Level.SEVERE, "Error while initializing new EAProject", e);
			}
		}
		return returned;
	}

	public FMLRTVirtualModelInstance getBPMNVirtualModelInstance(final FlexoProject<?> project) {
		if (project != null && project.hasNature(this)) {
			VirtualModel bpmnVM = getBPMNVirtualModel(project.getServiceManager());
			if (bpmnVM == null) {
				return null;
			}
			for (FMLRTVirtualModelInstanceResource viewResource : project.getVirtualModelInstanceRepository().getAllResources()) {
				if (viewResource.getVirtualModelResource() != null && viewResource.getVirtualModelResource() == bpmnVM.getResource()) {
					try {
						return viewResource.getResourceData(null);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ResourceLoadingCancelledException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (FlexoException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

}
