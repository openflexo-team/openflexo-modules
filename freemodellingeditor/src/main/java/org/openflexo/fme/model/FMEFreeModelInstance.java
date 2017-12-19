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
import java.util.logging.Logger;

import org.openflexo.connie.annotations.NotificationUnsafe;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.nature.VirtualModelInstanceBasedNatureObject;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.Import;
import org.openflexo.model.annotations.Imports;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelInstanceNature;

/**
 * Represents a {@link FMEFreeModelInstance} in the FreeModellingEditor<br>
 * 
 * The base of a {@link FMEFreeModelInstance} is a {@link FMLRTVirtualModelInstance} with the specific
 * {@link FMLControlledDiagramVirtualModelInstanceNature}<br>
 * From a technical point of view, a {@link FMEFreeModelInstance} is just a wrapper above a {@link FMLRTVirtualModelInstance} located in
 * project's freeModellingView
 * 
 * @author sylvain
 * 
 */
@ModelEntity(isAbstract = true)
@Imports({ @Import(FMEDiagramFreeModelInstance.class) })
public interface FMEFreeModelInstance extends VirtualModelInstanceBasedNatureObject<FreeModellingProjectNature>, PropertyChangeListener {

	@PropertyIdentifier(type = FMEFreeModel.class)
	public static final String FREE_MODEL = "freeModel";

	@Getter(value = FREE_MODEL)
	public FMEFreeModel getFreeModel();

	@Setter(FREE_MODEL)
	public void setFreeModel(FMEFreeModel freeModel);

	@Override
	public String getName();

	@Override
	public String getURI();

	public List<FlexoConceptInstance> getInstances(FlexoConcept flexoConcept);

	public String getProposedName(FlexoConcept concept);

	public FlexoConceptInstance getFlexoConceptInstanceNamed(String name, FlexoConcept concept);

	public List<FlexoConcept> getUsedFlexoConcepts();

	public abstract class FMEFreeModelInstanceImpl extends VirtualModelInstanceBasedNatureObjectImpl<FreeModellingProjectNature>
			implements FMEFreeModelInstance {

		private static final Logger logger = FlexoLogger.getLogger(FMEFreeModel.class.getPackage().getName());

		private FMLRTVirtualModelInstance virtualModelInstance = null;

		@Override
		public FreeModellingProjectNature getNature() {
			if (getFreeModel() != null) {
				return getFreeModel().getNature();
			}
			return (FreeModellingProjectNature) performSuperGetter(NATURE);
		}

		@Override
		public void fireVirtualModelInstanceConnected(FMLRTVirtualModelInstance aVirtualModelInstance) {
			super.fireVirtualModelInstanceConnected(aVirtualModelInstance);
			if (virtualModelInstance != aVirtualModelInstance) {
				if (virtualModelInstance != null) {
					virtualModelInstance.getPropertyChangeSupport().removePropertyChangeListener(this);
				}
				virtualModelInstance = aVirtualModelInstance;
				aVirtualModelInstance.getPropertyChangeSupport().addPropertyChangeListener(this);
			}
		}

		@Override
		public void fireVirtualModelInstanceDisconnected(FMLRTVirtualModelInstance aVirtualModelInstance) {
			super.fireVirtualModelInstanceDisconnected(aVirtualModelInstance);
			aVirtualModelInstance.getPropertyChangeSupport().removePropertyChangeListener(this);
		}

		/**
		 * Return a new list of FlexoConcept, which are all concepts used in this FreeModel
		 * 
		 * @return
		 */
		@Override
		@NotificationUnsafe
		public List<FlexoConcept> getUsedFlexoConcepts() {
			return getAccessedVirtualModelInstance().getUsedFlexoConcepts();
		}

		/**
		 * Return a new list of FlexoConcept, which are all concepts used in this FreeModel
		 * 
		 * @return
		 */
		@Override
		@NotificationUnsafe
		public List<FlexoConceptInstance> getInstances(FlexoConcept concept) {
			return getAccessedVirtualModelInstance().getFlexoConceptInstances(concept);
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getSource() == getAccessedVirtualModelInstance()) {
				if (evt.getPropertyName().equals(FMLRTVirtualModelInstance.FLEXO_CONCEPT_INSTANCES_KEY)) {
					if (evt.getNewValue() instanceof FlexoConceptInstance) {
						// A new FlexoConceptInstance has been created/added
						FlexoConceptInstance fci = (FlexoConceptInstance) evt.getNewValue();
						getPropertyChangeSupport().firePropertyChange("usedFlexoConcepts", null, fci);
						getPropertyChangeSupport().firePropertyChange("getInstances(FlexoConcept)", null, fci);
					}
					else if (evt.getOldValue() instanceof FlexoConceptInstance) {
						// A FlexoConceptInstance has been removed
						FlexoConceptInstance fci = (FlexoConceptInstance) evt.getOldValue();
						getPropertyChangeSupport().firePropertyChange("usedFlexoConcepts", fci, null);
						getPropertyChangeSupport().firePropertyChange("getInstances(FlexoConcept)", fci, null);
					}
				}
			}
		}

		@Override
		public String getProposedName(FlexoConcept concept) {

			String baseName;
			if (concept.getName().equals(FMEFreeModel.NONE_FLEXO_CONCEPT_NAME)) {
				baseName = "unnamed";
			}
			else {
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

		@Override
		public FlexoConceptInstance getFlexoConceptInstanceNamed(String name, FlexoConcept concept) {
			FlexoRole<?> nameRole = (FlexoRole<?>) concept.getAccessibleProperty(FMEConceptualModel.NAME_ROLE_NAME);
			for (FlexoConceptInstance fci : getAccessedVirtualModelInstance().getFlexoConceptInstances(concept)) {
				String fciName = (String) fci.getFlexoActor(nameRole);
				if (fciName != null && fciName.equals(name)) {
					return fci;
				}
			}
			return null;
		}
	}
}
