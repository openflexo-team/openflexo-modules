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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.connie.DataBinding;
import org.openflexo.connie.type.PrimitiveType;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.fml.CreationScheme;
import org.openflexo.foundation.fml.DeletionScheme;
import org.openflexo.foundation.fml.FlexoBehaviourParameter.WidgetType;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.action.CreateEditionAction;
import org.openflexo.foundation.fml.action.CreateFlexoBehaviour;
import org.openflexo.foundation.fml.action.CreateFlexoConcept;
import org.openflexo.foundation.fml.action.CreateGenericBehaviourParameter;
import org.openflexo.foundation.fml.action.CreateInspectorEntry;
import org.openflexo.foundation.fml.action.CreatePrimitiveRole;
import org.openflexo.foundation.fml.editionaction.AssignationAction;
import org.openflexo.foundation.fml.editionaction.ExpressionAction;
import org.openflexo.foundation.fml.inspector.InspectorEntry;
import org.openflexo.foundation.fml.rm.VirtualModelResourceFactory;
import org.openflexo.foundation.nature.NatureObject;
import org.openflexo.foundation.nature.VirtualModelBasedNatureObject;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLElement;

/**
 * Defines conceptual model of a {@link FreeModellingProjectNature}<br>
 * 
 * Typically points on a {@link VirtualModel}
 * 
 * @author sylvain
 * 
 */
@ModelEntity
@XMLElement
@ImplementationClass(FMEConceptualModel.FMEConceptualModelImpl.class)
public interface FMEConceptualModel extends VirtualModelBasedNatureObject<FreeModellingProjectNature> {

	public static final String NAME_ROLE_NAME = "name";
	public static final String DESCRIPTION_ROLE_NAME = "description";
	public static final String CONCEPT_NAME_PARAMETER = "conceptName";

	@PropertyIdentifier(type = NatureObject.class)
	public static final String OWNER_KEY = "owner";

	/**
	 * Return (creates when non-existant) a conceptual FlexoConcept in {@link VirtualModel} considered as conceptual model
	 * 
	 * @param conceptName
	 * @param editor
	 * @param ownerAction
	 * @return
	 * @throws FlexoException
	 */
	public FlexoConcept getFlexoConcept(String conceptName, FlexoConcept containerConcept, FlexoEditor editor,
			FlexoAction<?, ?, ?> ownerAction) throws FlexoException;

	public String getName();

	@Getter(value = OWNER_KEY)
	public NatureObject<FreeModellingProjectNature> getOwner();

	@Setter(OWNER_KEY)
	public void setOwner(NatureObject<FreeModellingProjectNature> owner);

	public FMEConceptualModel getParent();

	public List<FMEConceptualModel> getChildren();

	public abstract class FMEConceptualModelImpl extends VirtualModelBasedNatureObjectImpl<FreeModellingProjectNature>
			implements FMEConceptualModel {

		private static final Logger logger = FlexoLogger.getLogger(FMEConceptualModel.class.getPackage().getName());

		@Override
		public String getName() {
			if (getAccessedVirtualModel() != null) {
				return getAccessedVirtualModel().getName() + VirtualModelResourceFactory.FML_SUFFIX;
			}
			return null;
		}

		/**
		 * Return (creates when non-existant) a conceptual FlexoConcept in {@link VirtualModel} considered as conceptual model
		 * 
		 * @param conceptName
		 * @param editor
		 * @param ownerAction
		 * @return
		 * @throws FlexoException
		 */
		@Override
		public FlexoConcept getFlexoConcept(String conceptName, FlexoConcept containerConcept, FlexoEditor editor,
				FlexoAction<?, ?, ?> ownerAction) throws FlexoException {

			FlexoConcept returned = getAccessedVirtualModel().getFlexoConcept(conceptName);

			if (returned == null) {

				// Creates the concept
				CreateFlexoConcept action;
				if (ownerAction != null) {
					action = CreateFlexoConcept.actionType.makeNewEmbeddedAction(getAccessedVirtualModel(), null, ownerAction);
				}
				else {
					action = CreateFlexoConcept.actionType.makeNewAction(getAccessedVirtualModel(), null, editor);
				}
				action.setNewFlexoConceptName(conceptName);
				action.setContainerFlexoConcept(containerConcept);
				action.doAction();
				returned = action.getNewFlexoConcept();

				// Create new PrimitiveRole (String type) to store the name of this instance
				CreatePrimitiveRole createNameRole = null;
				if (ownerAction != null) {
					createNameRole = CreatePrimitiveRole.actionType.makeNewEmbeddedAction(returned, null, ownerAction);
				}
				else {
					createNameRole = CreatePrimitiveRole.actionType.makeNewAction(returned, null, editor);
				}
				createNameRole.setRoleName(NAME_ROLE_NAME);
				createNameRole.setPrimitiveType(PrimitiveType.String);
				createNameRole.doAction();

				// Create new PrimitiveRole (String type) to store the description of this instance
				CreatePrimitiveRole createDescRole = null;
				if (ownerAction != null) {
					createDescRole = CreatePrimitiveRole.actionType.makeNewEmbeddedAction(returned, null, ownerAction);
				}
				else {
					createDescRole = CreatePrimitiveRole.actionType.makeNewAction(returned, null, editor);
				}
				createDescRole.setRoleName(DESCRIPTION_ROLE_NAME);
				createDescRole.setPrimitiveType(PrimitiveType.String);
				createDescRole.doAction();

				// Create new CreationScheme
				CreateFlexoBehaviour createCreationScheme = null;
				if (ownerAction != null) {
					createCreationScheme = CreateFlexoBehaviour.actionType.makeNewEmbeddedAction(returned, null, ownerAction);
				}
				else {
					createCreationScheme = CreateFlexoBehaviour.actionType.makeNewAction(returned, null, editor);
				}
				createCreationScheme.setFlexoBehaviourName("create");
				createCreationScheme.setFlexoBehaviourClass(CreationScheme.class);
				createCreationScheme.doAction();
				CreationScheme creationScheme = (CreationScheme) createCreationScheme.getNewFlexoBehaviour();
				creationScheme.setSkipConfirmationPanel(true);

				// Create new CreationScheme parameter
				CreateGenericBehaviourParameter createNameParameter = null;
				if (ownerAction != null) {
					createNameParameter = CreateGenericBehaviourParameter.actionType.makeNewEmbeddedAction(creationScheme, null,
							ownerAction);
				}
				else {
					createNameParameter = CreateGenericBehaviourParameter.actionType.makeNewAction(creationScheme, null, editor);
				}
				createNameParameter.setParameterName(CONCEPT_NAME_PARAMETER);
				createNameParameter.setParameterType(String.class);
				createNameParameter.setWidgetType(WidgetType.TEXT_FIELD);
				createNameParameter.doAction();

				CreateEditionAction givesNameAction = null;
				if (ownerAction != null) {
					givesNameAction = CreateEditionAction.actionType.makeNewEmbeddedAction(creationScheme.getControlGraph(), null,
							ownerAction);
				}
				else {
					givesNameAction = CreateEditionAction.actionType.makeNewAction(creationScheme.getControlGraph(), null, editor);
				}
				// givesNameAction.actionChoice = CreateEditionActionChoice.BuiltInAction;
				givesNameAction.setEditionActionClass(ExpressionAction.class);
				givesNameAction.setAssignation(new DataBinding<>(NAME_ROLE_NAME));
				givesNameAction.doAction();

				AssignationAction<?> nameAssignation = (AssignationAction<?>) givesNameAction.getNewEditionAction();
				((ExpressionAction<?>) nameAssignation.getAssignableAction()).setExpression(new DataBinding<>("parameters.conceptName"));

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
				createNameEntry.setEntryName(NAME_ROLE_NAME);
				createNameEntry.setEntryType(String.class);
				createNameEntry.setWidgetType(WidgetType.TEXT_FIELD);
				createNameEntry.setData(new DataBinding<String>("name"));

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
				createDescriptionEntry.setEntryName(DESCRIPTION_ROLE_NAME);
				createDescriptionEntry.setEntryType(String.class);
				createDescriptionEntry.setWidgetType(WidgetType.TEXT_AREA);
				createDescriptionEntry.setData(new DataBinding<String>("description"));
				createDescriptionEntry.doAction();
				InspectorEntry descriptionEntry = createDescriptionEntry.getNewEntry();

				returned.getInspector().setRenderer(new DataBinding<String>("instance.name"));
			}
			return returned;
		}

		@Override
		public FreeModellingProjectNature getNature() {
			if (getOwner() != null) {
				return getOwner().getNature();
			}
			return null;
		}

		private List<FMEConceptualModel> children = null;

		@Override
		public FMEConceptualModel getParent() {
			if (getOwner() instanceof FreeModellingProjectNature) {
				return null;
			}
			else if (getOwner() instanceof FMEFreeModel) {
				return getOwner().getNature().getConceptualModel();
			}
			else {
				return null;
			}
		}

		@Override
		public List<FMEConceptualModel> getChildren() {
			if (getOwner() instanceof FMEFreeModel) {
				return Collections.emptyList();
			}
			else if (getOwner() instanceof FreeModellingProjectNature) {
				if (children == null) {
					children = new ArrayList<>();
					for (FMEFreeModel freeModel : getNature().getFreeModels()) {
						children.add(freeModel.getConceptualModel());
					}
				}
				return children;
			}
			return null;
		}

	}
}
