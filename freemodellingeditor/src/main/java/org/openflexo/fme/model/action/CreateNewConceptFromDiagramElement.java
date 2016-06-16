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

import org.openflexo.ApplicationContext;
import org.openflexo.fme.FreeModellingEditor;
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
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.localization.LocalizedDelegate;
import org.openflexo.technologyadapter.diagram.fml.GraphicalElementRole;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;

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
			return object instanceof DiagramShape;
		}

		@Override
		public boolean isEnabledForSelection(DiagramElement<?> object, Vector<FlexoObject> globalSelection) {
			// TODO: handle other kind of elements
			return object instanceof DiagramShape;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(CreateNewConceptFromDiagramElement.actionType, DiagramElement.class);
	}

	CreateNewConceptFromDiagramElement(DiagramElement<?> focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	public LocalizedDelegate getLocales() {
		if (getServiceManager() instanceof ApplicationContext) {
			return ((ApplicationContext) getServiceManager()).getModuleLoader().getModule(FreeModellingEditor.class)
					.getLoadedModuleInstance().getLocales();
		}
		return super.getLocales();
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
		flexoConceptInstance = createFlexoConceptInstanceFromDiagramShape(getFocusedObject());

		logger.info("Create new concept from diagram element ");
		CreateNewConceptFromNoneConcept actionCreateNewConcept = CreateNewConceptFromNoneConcept.actionType
				.makeNewEmbeddedAction(flexoConceptInstance, null, this);
		actionCreateNewConcept.doAction();
		flexoConcept = actionCreateNewConcept.getNewFlexoConcept();
	}

	private FlexoConceptInstance createFlexoConceptInstanceFromDiagramShape(DiagramElement<?> diagramElement) {
		FlexoConceptInstance newFlexoConceptInstance = getFreeModel().getVirtualModelInstance().makeNewFlexoConceptInstance(none);
		GraphicalElementRole geRole = (ShapeRole) none.getAccessibleProperty(FreeMetaModel.SHAPE_ROLE_NAME);
		newFlexoConceptInstance.setFlexoActor(diagramElement, geRole);
		PrimitiveRole<String> nameRole = (PrimitiveRole<String>) none.getAccessibleProperty(FreeMetaModel.NAME_ROLE_NAME);
		newFlexoConceptInstance.setFlexoActor(diagramElement.getName(), nameRole);
		return newFlexoConceptInstance;
	}

	public FlexoConcept getFlexoConcept() {
		return flexoConcept;
	}

	public void setFlexoConcept(FlexoConcept flexoConcept) {
		this.flexoConcept = flexoConcept;
	}

}
