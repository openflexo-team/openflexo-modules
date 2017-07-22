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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.openflexo.foundation.DefaultFlexoObject;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.InvalidArgumentException;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelInstanceNature;
import org.openflexo.technologyadapter.diagram.model.Diagram;

/**
 * Represents a {@link FreeModel} in the FreeModellingEditor<br>
 * 
 * The base of a {@link FreeModel} is a {@link FMLRTVirtualModelInstance} with the specific
 * {@link FMLControlledDiagramVirtualModelInstanceNature}<br>
 * From a technical point of view, a {@link FreeModel} is just a wrapper above a {@link FMLRTVirtualModelInstance} located in project's
 * freeModellingView
 * 
 * @author sylvain
 * 
 */
public class FreeModel extends DefaultFlexoObject implements PropertyChangeListener {

	private final FMLRTVirtualModelInstance virtualModelInstance;
	private final FreeModellingProject fmProject;

	/**
	 * Provides generic method used to retrieve URI of Diagram of a given {@link FreeModel}
	 * 
	 * @param project
	 * @param metaModelName
	 * @return
	 */
	public static String getDiagramURI(FlexoProject project, String modelName) {
		return project.getURI() + "/Diagram/" + modelName;
	}

	public FreeModel(FMLRTVirtualModelInstance virtualModelInstance, FreeModellingProject fmProject) throws InvalidArgumentException {
		super();
		this.fmProject = fmProject;
		if (!virtualModelInstance.hasNature(FMLControlledDiagramVirtualModelInstanceNature.INSTANCE)) {
			throw new InvalidArgumentException("FMLRTVirtualModelInstance does not have the FMLControlledDiagramVirtualModelInstanceNature");
		}
		this.virtualModelInstance = virtualModelInstance;
		virtualModelInstance.getPropertyChangeSupport().addPropertyChangeListener(this);
	}

	public FreeModellingProject getFreeModellingProject() {
		return fmProject;
	}

	public VirtualModel getVirtualModel() {
		return getMetaModel().getVirtualModel();
	}

	public FMLRTVirtualModelInstance getVirtualModelInstance() {
		return virtualModelInstance;
	}

	public FreeModellingProjectNature getProjectNature() {
		return getFreeModellingProject().getProjectNature();
	}

	public Diagram getDiagram() {
		return FMLControlledDiagramVirtualModelInstanceNature.getDiagram(virtualModelInstance);
	}

	public String getName() {
		return virtualModelInstance.getName();
	}

	public String getURI() {
		return fmProject.getProject().getURI() + "/" + getName();
	}

	public FreeMetaModel getMetaModel() {
		try {
			return fmProject.getFreeMetaModel(virtualModelInstance.getVirtualModel());
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Return a new list of FlexoConcept, which are all concepts used in this FreeModel
	 * 
	 * @return
	 */
	public List<FlexoConcept> getUsedFlexoConcepts() {
		return virtualModelInstance.getUsedFlexoConcepts();
	}

	/**
	 * Return a new list of FlexoConcept, which are all concepts used in this FreeModel
	 * 
	 * @return
	 */
	public List<FlexoConceptInstance> getInstances(FlexoConcept concept) {
		return virtualModelInstance.getFlexoConceptInstances(concept);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == virtualModelInstance) {
			if (evt.getPropertyName().equals(FMLRTVirtualModelInstance.FLEXO_CONCEPT_INSTANCES_KEY)) {
				if (evt.getNewValue() instanceof FlexoConceptInstance) {
					// A new FlexoConceptInstance has been created/added
					FlexoConceptInstance fci = (FlexoConceptInstance) evt.getNewValue();
					getPropertyChangeSupport().firePropertyChange("usedFlexoConcepts", null, fci);
					getPropertyChangeSupport().firePropertyChange("getInstances(FlexoConcept)", null, fci);
				} else if (evt.getOldValue() instanceof FlexoConceptInstance) {
					// A FlexoConceptInstance has been removed
					FlexoConceptInstance fci = (FlexoConceptInstance) evt.getOldValue();
					getPropertyChangeSupport().firePropertyChange("usedFlexoConcepts", fci, null);
					getPropertyChangeSupport().firePropertyChange("getInstances(FlexoConcept)", fci, null);
				}
			}
		}
	}

	public String getProposedName(FlexoConcept concept) {

		String baseName;
		if (concept.getName().equals(FreeMetaModel.NONE_FLEXO_CONCEPT)) {
			baseName = "unnamed";
		} else {
			baseName = concept.getName().toLowerCase();
		}

		String returned = baseName;
		int index = 2;

		while (getFlexoConceptInstanceNamed(returned, concept) != null) {
			returned = baseName + index;
			index++;
		}

		return returned;

	}

	public FlexoConceptInstance getFlexoConceptInstanceNamed(String name, FlexoConcept concept) {
		FlexoRole<?> nameRole = (FlexoRole<?>) concept.getAccessibleProperty(FreeMetaModel.NAME_ROLE_NAME);
		for (FlexoConceptInstance fci : getVirtualModelInstance().getFlexoConceptInstances(concept)) {
			String fciName = (String) fci.getFlexoActor(nameRole);
			if (fciName != null && fciName.equals(name)) {
				return fci;
			}
		}
		return null;
	}
}
