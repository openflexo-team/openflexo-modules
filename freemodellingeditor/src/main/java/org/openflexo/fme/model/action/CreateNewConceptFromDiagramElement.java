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

package org.openflexo.fme.model.action;

import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.connie.DataBinding;
import org.openflexo.fme.model.FreeMetaModel;
import org.openflexo.fme.model.FreeModel;
import org.openflexo.fme.model.FreeModellingProject;
import org.openflexo.fme.model.FreeModellingProjectNature;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.InvalidArgumentException;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.PrimitiveRole;
import org.openflexo.foundation.fml.TextFieldParameter;
import org.openflexo.foundation.fml.PrimitiveRole.PrimitiveType;
import org.openflexo.foundation.fml.action.CreateEditionAction;
import org.openflexo.foundation.fml.action.CreateFlexoBehaviourParameter;
import org.openflexo.foundation.fml.action.CreateFlexoRole;
import org.openflexo.foundation.fml.editionaction.AssignationAction;
import org.openflexo.foundation.fml.editionaction.ExpressionAction;
import org.openflexo.foundation.fml.rm.VirtualModelResource;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.technologyadapter.diagram.fml.ConnectorRole;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.GraphicalElementRole;
import org.openflexo.technologyadapter.diagram.fml.LinkScheme;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
import org.openflexo.technologyadapter.diagram.fml.action.DeclareConnectorInFlexoConcept;
import org.openflexo.technologyadapter.diagram.fml.editionaction.AddShape;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;

/**
 * This action is used to create a new FlexoConcept from a diagram element
 * 
 * @author vincent
 * 
 */
public class CreateNewConceptFromDiagramElement extends FlexoAction<CreateNewConceptFromDiagramElement, DiagramElement<?>, FlexoObject> {

	private static final Logger logger = Logger.getLogger(CreateNewConceptFromDiagramElement.class.getPackage().getName());

	public static FlexoActionType<CreateNewConceptFromDiagramElement, DiagramElement<?>, FlexoObject> actionType = new FlexoActionType<CreateNewConceptFromDiagramElement, DiagramElement<?>, FlexoObject>(
			"create_new_concept_from_diagram_element", FlexoActionType.defaultGroup, FlexoActionType.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateNewConceptFromDiagramElement makeNewAction(DiagramElement<?> focusedObject, Vector<FlexoObject> globalSelection,
				FlexoEditor editor) {
			return new CreateNewConceptFromDiagramElement(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(DiagramElement<?> object, Vector<FlexoObject> globalSelection) {
			// TODO: handle other kind of elements
			//return object instanceof DiagramShape;
			return true;
		}

		@Override
		public boolean isEnabledForSelection(DiagramElement<?> object, Vector<FlexoObject> globalSelection) {
			// TODO: handle other kind of elements
			//return object instanceof DiagramShape;
			return true;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(CreateNewConceptFromDiagramElement.actionType, DiagramElement.class);
	}

	CreateNewConceptFromDiagramElement(DiagramElement<?> focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	public FreeModellingProjectNature getFreeModellingProjectNature() {
		return getServiceManager().getProjectNatureService().getProjectNature(FreeModellingProjectNature.class);
	}

	public FreeModellingProject getFreeModellingProject() {
		return getFreeModellingProjectNature().getFreeModellingProject(getEditor().getProject());
	}

	public FreeModel getFreeModel() {
		for (FreeModel freeModel : getFreeModellingProject().getFreeModels()) {
			if (freeModel.getDiagram().equals(getFocusedObject().getDiagram())) {
				return freeModel;
			}
		}
		return null;
	}

	private FlexoConcept none;
	private FlexoConceptInstance flexoConceptInstance;
	private FlexoConcept flexoConcept;

	@Override
	protected void doAction(Object context) throws InvalidArgumentException {
		
		logger.info("Create new instance of created concept from diagram element ");
		none = getFreeModel().getMetaModel().getNoneFlexoConcept(getEditor(), this);
		
		if(getFocusedObject() instanceof DiagramConnector){
			DiagramConnector connector = (DiagramConnector)getFocusedObject();
			DeclareConnectorInFlexoConcept declareConnectorConcept = DeclareConnectorInFlexoConcept.actionType
					.makeNewEmbeddedAction(connector, null, this);
			declareConnectorConcept.setVirtualModelResource((VirtualModelResource)(getFreeModel().getVirtualModel().getResource()));
			declareConnectorConcept.doAction();
			flexoConcept = declareConnectorConcept.getFlexoConcept();
		//	declareConnectorConcept.
			flexoConceptInstance = createFlexoConceptInstanceFromDiagramConnector(connector);
		}
		else{
			flexoConceptInstance = createFlexoConceptInstanceFromDiagramShape(getFocusedObject());

			logger.info("Create new concept from diagram element ");
			CreateNewConceptFromNoneConcept actionCreateNewConcept = CreateNewConceptFromNoneConcept.actionType
					.makeNewEmbeddedAction(flexoConceptInstance, null, this);
			actionCreateNewConcept.doAction();
			flexoConcept = actionCreateNewConcept.getNewFlexoConcept();
		}
		
	}

	private FlexoConceptInstance createFlexoConceptInstanceFromDiagramShape(DiagramElement<?> diagramElement) {
		FlexoConceptInstance newFlexoConceptInstance = getFreeModel().getVirtualModelInstance().makeNewFlexoConceptInstance(none);
		GraphicalElementRole geRole = (ShapeRole) none.getAccessibleProperty(FreeMetaModel.SHAPE_ROLE_NAME);
		newFlexoConceptInstance.setFlexoActor(diagramElement, geRole);
		PrimitiveRole<String> nameRole = (PrimitiveRole<String>) none.getAccessibleProperty(FreeMetaModel.NAME_ROLE_NAME);
		newFlexoConceptInstance.setFlexoActor(diagramElement.getName(), nameRole);
		return newFlexoConceptInstance;
	}

	private FlexoConceptInstance createFlexoConceptInstanceFromDiagramConnector(DiagramConnector connector) {
		FlexoConceptInstance newFlexoConceptInstance = getFreeModel().getVirtualModelInstance().makeNewFlexoConceptInstance(flexoConcept);
		ConnectorRole geRole = (ConnectorRole) flexoConcept.getAccessibleProperty("connector");
		geRole.setReadOnlyLabel(false);
		newFlexoConceptInstance.setFlexoActor(connector, geRole);
		CreateFlexoRole createNameRole = CreateFlexoRole.actionType.makeNewEmbeddedAction(flexoConcept, null, this);
		createNameRole.setRoleName(FreeMetaModel.NAME_ROLE_NAME);
		createNameRole.setFlexoRoleClass(PrimitiveRole.class);
		createNameRole.setPrimitiveType(PrimitiveType.String);
		createNameRole.doAction();
		// Bind shapes's label to name property
		geRole.setLabel(new DataBinding("name"));
		newFlexoConceptInstance.setFlexoActor(flexoConcept.getName(), (PrimitiveRole)createNameRole.getNewFlexoProperty());
		LinkScheme linkScheme = (LinkScheme) flexoConcept.getFlexoBehaviours(LinkScheme.class).get(0);
		
		// Create new DropScheme parameter
		CreateFlexoBehaviourParameter createSchemeParameter = CreateFlexoBehaviourParameter.actionType.makeNewEmbeddedAction(linkScheme, null, this);
		createSchemeParameter.setParameterName("conceptName");
		createSchemeParameter.setFlexoBehaviourParameterClass(TextFieldParameter.class);
		createSchemeParameter.doAction();
		TextFieldParameter parameter = (TextFieldParameter) createSchemeParameter.getNewParameter();
		parameter.setDefaultValue(new DataBinding<String>("\"" + parameter.getName() + "\""));

		CreateEditionAction givesNameAction = CreateEditionAction.actionType.makeNewEmbeddedAction(linkScheme.getControlGraph(), null, this);
		
		// givesNameAction.actionChoice = CreateEditionActionChoice.BuiltInAction;
		givesNameAction.setEditionActionClass(ExpressionAction.class);
		givesNameAction.setAssignation(new DataBinding(FreeMetaModel.NAME_ROLE_NAME));
		givesNameAction.doAction();

		AssignationAction<?> nameAssignation = (AssignationAction<?>) givesNameAction.getNewEditionAction();
		((ExpressionAction) nameAssignation.getAssignableAction()).setExpression(new DataBinding("parameters.conceptName"));

		
		connector.getParent().getPropertyChangeSupport().firePropertyChange(DiagramElement.INVALIDATE, null, connector.getParent());

		return newFlexoConceptInstance;
	}

	
	public FlexoConcept getFlexoConcept() {
		return flexoConcept;
	}

	public void setFlexoConcept(FlexoConcept flexoConcept) {
		this.flexoConcept = flexoConcept;
	}

}
