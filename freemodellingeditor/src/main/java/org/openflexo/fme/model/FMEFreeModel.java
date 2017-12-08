/**
 * 
 * Copyright (c) 2014-2015, Openflexo
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

import java.util.List;
import java.util.logging.Logger;

import org.openflexo.connie.DataBinding;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.fml.DeletionScheme;
import org.openflexo.foundation.fml.FlexoBehaviourParameter.WidgetType;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.action.CreateFlexoBehaviour;
import org.openflexo.foundation.fml.action.CreateFlexoConcept;
import org.openflexo.foundation.fml.action.CreateFlexoConceptInstanceRole;
import org.openflexo.foundation.fml.action.CreateInspectorEntry;
import org.openflexo.foundation.fml.inspector.InspectorEntry;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstanceModelSlot;
import org.openflexo.foundation.fml.rt.rm.FMLRTVirtualModelInstanceResourceFactory;
import org.openflexo.foundation.nature.VirtualModelBasedNatureObject;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.model.annotations.Adder;
import org.openflexo.model.annotations.Embedded;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.Getter.Cardinality;
import org.openflexo.model.annotations.Import;
import org.openflexo.model.annotations.Imports;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Remover;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLElement;

/**
 * Represents a {@link FMEFreeModel} in the FreeModellingEditor<br>
 * This is an abstract concept.
 * 
 * The base of a {@link FMEFreeModel} is a {@link VirtualModel} which aims to federate the {@link FMEConceptualModel} with a given
 * representation to elicitate new or existing concepts.
 * 
 * 
 * @author sylvain
 * 
 */
@ModelEntity(isAbstract = true)
@Imports({ @Import(FMEDiagramFreeModel.class) })
public interface FMEFreeModel extends VirtualModelBasedNatureObject<FreeModellingProjectNature> {

	public static final String NONE_FLEXO_CONCEPT = "None";
	public static final String CONCEPT_ROLE_NAME = "concept";
	public static final String SAMPLE_DATA_MODEL_SLOT_NAME = "sampleData";

	@PropertyIdentifier(type = FMEFreeModelInstance.class, cardinality = Cardinality.LIST)
	public static final String FREE_MODELS_INSTANCES = "freeModelInstances";

	public String getName();

	public FMLRTVirtualModelInstanceModelSlot getSampleDataModelSlot();

	@Getter(value = FREE_MODELS_INSTANCES, cardinality = Cardinality.LIST, inverse = FMEFreeModelInstance.FREE_MODEL)
	@XMLElement
	@Embedded
	public List<FMEFreeModelInstance> getFreeModelInstances();

	@Setter(value = FREE_MODELS_INSTANCES)
	public void setFreeModelInstances(List<FMEFreeModelInstance> freeModelInstances);

	@Adder(FREE_MODELS_INSTANCES)
	public void addToFreeModelInstances(FMEFreeModelInstance freeModelInstance);

	@Remover(value = FREE_MODELS_INSTANCES)
	public void removeFromFreeModelInstances(FMEFreeModelInstance freeModelInstance);

	public FMEFreeModelInstance getFreeModelInstance(String freeModelInstanceName);

	public FlexoConcept getNoneFlexoConcept(FlexoEditor editor, FlexoAction<?, ?, ?> ownerAction);

	/**
	 * Return (creates when non-existant) a FlexoConcept (in the context of FreeModellingEditor)
	 * 
	 * @param conceptName
	 * @param editor
	 * @param ownerAction
	 * @return
	 * @throws FlexoException
	 */
	public FlexoConcept getGRFlexoConcept(FlexoConcept concept, FlexoEditor editor, FlexoAction<?, ?, ?> ownerAction) throws FlexoException;

	public abstract class FMEFreeModelImpl extends VirtualModelBasedNatureObjectImpl<FreeModellingProjectNature> implements FMEFreeModel {

		private static final Logger logger = FlexoLogger.getLogger(FMEFreeModel.class.getPackage().getName());

		@Override
		public String getName() {
			return getAccessedVirtualModel().getName();
		}

		@Override
		public FMLRTVirtualModelInstanceModelSlot getSampleDataModelSlot() {
			return getAccessedVirtualModel().getModelSlots(FMLRTVirtualModelInstanceModelSlot.class).get(0);
		}

		@Override
		public FlexoConcept getNoneFlexoConcept(FlexoEditor editor, FlexoAction<?, ?, ?> ownerAction) {
			try {
				return getGRFlexoConcept(null, editor, ownerAction);
			} catch (FlexoException e) {
				// TODO
				e.printStackTrace();
				return null;
			}
		}

		/**
		 * Return (creates when non-existant) a FlexoConcept (in the context of FreeModellingEditor)
		 * 
		 * @param conceptName
		 * @param editor
		 * @param ownerAction
		 * @return
		 * @throws FlexoException
		 */
		@Override
		public FlexoConcept getGRFlexoConcept(FlexoConcept concept, FlexoEditor editor, FlexoAction<?, ?, ?> ownerAction)
				throws FlexoException {

			FlexoConcept returned = getAccessedVirtualModel().getFlexoConcept(concept != null ? concept.getName() + "GR" : "None");

			if (returned == null) {

				// Creates the concept
				CreateFlexoConcept action;
				if (ownerAction != null) {
					action = CreateFlexoConcept.actionType.makeNewEmbeddedAction(getAccessedVirtualModel(), null, ownerAction);
				}
				else {
					action = CreateFlexoConcept.actionType.makeNewAction(getAccessedVirtualModel(), null, editor);
				}
				action.setNewFlexoConceptName(concept != null ? concept.getName() + "GR" : "None");
				action.doAction();
				returned = action.getNewFlexoConcept();

				// Create new FlexoConceptInstanceRole to store the concept
				if (concept != null) {
					CreateFlexoConceptInstanceRole createConceptRole = null;
					if (ownerAction != null) {
						createConceptRole = CreateFlexoConceptInstanceRole.actionType.makeNewEmbeddedAction(returned, null, ownerAction);
					}
					else {
						createConceptRole = CreateFlexoConceptInstanceRole.actionType.makeNewAction(returned, null, editor);
					}
					createConceptRole.setRoleName(CONCEPT_ROLE_NAME);
					createConceptRole.setFlexoConceptInstanceType(concept);
					createConceptRole.doAction();
				}

				// Create new DeletionScheme
				CreateFlexoBehaviour createDeletionScheme = null;
				if (ownerAction != null) {
					createDeletionScheme = CreateFlexoBehaviour.actionType.makeNewEmbeddedAction(returned, null, ownerAction);
				}
				else {
					createDeletionScheme = CreateFlexoBehaviour.actionType.makeNewAction(returned, null, editor);
				}
				createDeletionScheme.setFlexoBehaviourName("delete");
				createDeletionScheme.setFlexoBehaviourClass(DeletionScheme.class);
				createDeletionScheme.doAction();
				DeletionScheme deletionScheme = (DeletionScheme) createDeletionScheme.getNewFlexoBehaviour();
				deletionScheme.setSkipConfirmationPanel(true);

				// Create inspector name entry
				CreateInspectorEntry createNameEntry = null;
				if (ownerAction != null) {
					createNameEntry = CreateInspectorEntry.actionType.makeNewEmbeddedAction(returned.getInspector(), null, ownerAction);
				}
				else {
					createNameEntry = CreateInspectorEntry.actionType.makeNewAction(returned.getInspector(), null, editor);
				}
				createNameEntry.setEntryName(FMEConceptualModel.NAME_ROLE_NAME);
				createNameEntry.setEntryType(String.class);
				createNameEntry.setWidgetType(WidgetType.TEXT_FIELD);
				createNameEntry.setData(new DataBinding<String>("concept.name"));

				createNameEntry.doAction();
				InspectorEntry nameEntry = createNameEntry.getNewEntry();

				// Create inspector description entry
				CreateInspectorEntry createDescriptionEntry = null;
				if (ownerAction != null) {
					createDescriptionEntry = CreateInspectorEntry.actionType.makeNewEmbeddedAction(returned.getInspector(), null,
							ownerAction);
				}
				else {
					createDescriptionEntry = CreateInspectorEntry.actionType.makeNewAction(returned.getInspector(), null, editor);
				}
				createDescriptionEntry.setEntryName(FMEConceptualModel.DESCRIPTION_ROLE_NAME);
				createDescriptionEntry.setEntryType(String.class);
				createDescriptionEntry.setWidgetType(WidgetType.TEXT_AREA);
				createDescriptionEntry.setData(new DataBinding<String>("concept.description"));
				createDescriptionEntry.doAction();
				InspectorEntry descriptionEntry = createDescriptionEntry.getNewEntry();

				returned.getInspector().setRenderer(new DataBinding<String>("instance.name"));

				configureGRFlexoConcept(returned, concept, editor, ownerAction);
			}

			return returned;
		}

		protected abstract void configureGRFlexoConcept(FlexoConcept returned, FlexoConcept concept, FlexoEditor editor,
				FlexoAction<?, ?, ?> ownerAction);

		@Override
		public FMEFreeModelInstance getFreeModelInstance(String freeModelName) {
			for (FMEFreeModelInstance freeModelInstance : getFreeModelInstances()) {
				if (freeModelInstance.getName().equals(freeModelName)) {
					return freeModelInstance;
				}
				if (freeModelInstance.getName().equals(freeModelName + FMLRTVirtualModelInstanceResourceFactory.FML_RT_SUFFIX)) {
					return freeModelInstance;
				}
			}
			return null;
		}

	}

}
