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

import java.util.List;

import org.openflexo.foundation.DefaultFlexoObject;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.InvalidArgumentException;
import org.openflexo.foundation.view.FlexoConceptInstance;
import org.openflexo.foundation.view.VirtualModelInstance;
import org.openflexo.foundation.viewpoint.FlexoConcept;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelInstanceNature;
import org.openflexo.technologyadapter.diagram.model.Diagram;

/**
 * Represents a {@link FreeModel} in the FreeModellingEditor<br>
 * 
 * The base of a {@link FreeModel} is a {@link VirtualModelInstance} with the specific
 * {@link FMLControlledDiagramVirtualModelInstanceNature}<br>
 * From a technical point of view, a {@link FreeModel} is just a wrapper above a {@link VirtualModelInstance} located in project's
 * freeModellingView
 * 
 * @author sylvain
 * 
 */
public class FreeModel extends DefaultFlexoObject {

	private final VirtualModelInstance virtualModelInstance;
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

	public FreeModel(VirtualModelInstance virtualModelInstance, FreeModellingProject fmProject) throws InvalidArgumentException {
		super();
		this.fmProject = fmProject;
		if (!virtualModelInstance.hasNature(FMLControlledDiagramVirtualModelInstanceNature.INSTANCE)) {
			throw new InvalidArgumentException("VirtualModelInstance does not have the FMLControlledDiagramVirtualModelInstanceNature");
		}
		this.virtualModelInstance = virtualModelInstance;
	}

	public FreeModellingProject getFreeModellingProject() {
		return fmProject;
	}

	public VirtualModelInstance getVirtualModelInstance() {
		return virtualModelInstance;
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

}
