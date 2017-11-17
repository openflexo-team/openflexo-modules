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

import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.nature.ProjectWrapper;
import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Setter;
import org.openflexo.modelers.ModelersConstants;

/**
 * An "Enterprise Architecture" project<br>
 *
 * @author sylvain
 */
@ModelEntity
@ImplementationClass(value = EAProject.EAProjectImpl.class)
public interface EAProject extends FlexoObject, ProjectWrapper<EAProjectNature>, ResourceData<EAProject> {

	public static final String PROJECT_KEY = "project";
	public static final String PROJECT_NATURE_KEY = "projectNature";

	@Override
	@Getter(value = PROJECT_KEY, ignoreType = true)
	public FlexoProject<?> getProject();

	@Setter(PROJECT_KEY)
	public void setProject(FlexoProject<?> project);

	@Override
	@Getter(value = PROJECT_NATURE_KEY, ignoreType = true)
	public EAProjectNature getProjectNature();

	@Setter(PROJECT_NATURE_KEY)
	public void setProjectNature(EAProjectNature projectNature);

	public FMLRTVirtualModelInstance getBPMNVirtualModelInstance();

	public void init(FlexoProject<?> project, EAProjectNature nature);

	public String getName();

	/**
	 * @return the BPMN {@link VirtualModel}
	 */
	public VirtualModel getBPMNVirtualModel();

	/**
	 * Default base implementation for {@link EAProject}
	 * 
	 * @author sylvain
	 *
	 */
	public static abstract class EAProjectImpl extends FlexoObjectImpl implements EAProject {

		/**
		 * Initialize project with all setter and resource loader with some protection. No exception are thrown.
		 * 
		 * @param project
		 *            FlexoProject
		 * @param nature
		 *            Nature of formose project project.hasNature(nature) has to be true
		 */
		@Override
		public void init(FlexoProject project, EAProjectNature nature) {
			setProject(project);
			setProjectNature(nature);
		}

		@Override
		public String getName() {
			if (getProject() != null) {
				return getProject().getName();
			}
			return null;
		}

		@Override
		public FMLRTVirtualModelInstance getBPMNVirtualModelInstance() {

			System.out.println("On me demande le BPMN VMI");

			return getProjectNature().getBPMNVirtualModelInstance(getProject());
		}

		@Override
		public VirtualModel getBPMNVirtualModel() {
			try {
				return getProject().getServiceManager().getVirtualModelLibrary().getVirtualModel(ModelersConstants.BPMN_EDITOR_URI);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

	}
}
