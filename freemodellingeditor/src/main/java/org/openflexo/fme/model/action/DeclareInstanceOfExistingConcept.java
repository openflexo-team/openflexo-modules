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

package org.openflexo.fme.model.action;

import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fme.FreeModellingEditor;
import org.openflexo.fme.controller.editor.DynamicPalette.GraphicalRepresentationSet;
import org.openflexo.fme.model.FreeMetaModel;
import org.openflexo.fme.model.FreeModel;
import org.openflexo.fme.model.FreeModellingProject;
import org.openflexo.fme.model.FreeModellingProjectNature;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.InvalidArgumentException;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.localization.LocalizedDelegate;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelNature;
import org.openflexo.technologyadapter.diagram.fml.FMLDiagramPaletteElementBinding;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;

/**
 * This action is used to declare a concept to be an instance of an existing concept (move to None to Something)
 * 
 * @author sylvain
 * 
 */
public class DeclareInstanceOfExistingConcept extends FlexoAction<DeclareInstanceOfExistingConcept, FlexoConceptInstance, FlexoObject> {

	private static final Logger logger = Logger.getLogger(DeclareInstanceOfExistingConcept.class.getPackage().getName());

	public static enum GRStrategy {
		GetConceptShape, RedefineShapeOfConcept, KeepShape
	}

	public static FlexoActionType<DeclareInstanceOfExistingConcept, FlexoConceptInstance, FlexoObject> actionType = new FlexoActionType<DeclareInstanceOfExistingConcept, FlexoConceptInstance, FlexoObject>(
			"declare_instance_of_existing_concept", FlexoActionType.defaultGroup, FlexoActionType.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public DeclareInstanceOfExistingConcept makeNewAction(FlexoConceptInstance focusedObject, Vector<FlexoObject> globalSelection,
				FlexoEditor editor) {
			return new DeclareInstanceOfExistingConcept(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(FlexoConceptInstance object, Vector<FlexoObject> globalSelection) {
			return true;
		}

		@Override
		public boolean isEnabledForSelection(FlexoConceptInstance object, Vector<FlexoObject> globalSelection) {
			return true;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(DeclareInstanceOfExistingConcept.actionType, FlexoConceptInstance.class);
	}

	private FlexoConcept concept;
	private GRStrategy grStrategy = null; // GRStrategy.GetConceptShape;

	DeclareInstanceOfExistingConcept(FlexoConceptInstance focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
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
		return getFreeModellingProjectNature().getFreeModellingProject((FlexoProject)getFocusedObject().getResourceCenter());
	}

	public FreeModel getFreeModel() throws InvalidArgumentException {
		return getFreeModellingProject().getFreeModel((FMLRTVirtualModelInstance) getFocusedObject().getVirtualModelInstance());
	}

	public FlexoConcept getConcept() {
		if (concept == null && getVirtualModel() != null && getVirtualModel().getFlexoConcepts().size() > 0) {
			return getVirtualModel().getFlexoConcepts().get(0);
		}
		return concept;
	}

	public void setConcept(FlexoConcept concept) {
		boolean wasValid = isValid();
		this.concept = concept;
		getPropertyChangeSupport().firePropertyChange("concept", null, concept);
		getPropertyChangeSupport().firePropertyChange("grStrategy", null, concept);
		getPropertyChangeSupport().firePropertyChange("isValid", wasValid, isValid());
		getPropertyChangeSupport().firePropertyChange("errorMessage", null, getErrorMessage());
	}

	public GRStrategy getGrStrategy() {
		if (grStrategy == null) {
			try {
				if (getFreeModel().getInstances(getConcept()).size() > 0) {
					return GRStrategy.GetConceptShape;
				}
			} catch (InvalidArgumentException e) {
				e.printStackTrace();
			}
			return GRStrategy.RedefineShapeOfConcept;
		}
		return grStrategy;
	}

	public void setGrStrategy(GRStrategy grStrategy) {
		boolean wasValid = isValid();
		this.grStrategy = grStrategy;
		getPropertyChangeSupport().firePropertyChange("grStrategy", null, grStrategy);
		getPropertyChangeSupport().firePropertyChange("isValid", wasValid, isValid());
		getPropertyChangeSupport().firePropertyChange("errorMessage", null, getErrorMessage());
	}

	@Override
	protected void doAction(Object context) throws InvalidArgumentException {

		FreeModel freeModel = getFreeModel();
		if (freeModel == null) {
			throw new InvalidArgumentException("FlexoConceptInstance does not belong to any FreeModel");
		}

		FlexoConceptInstance flexoConceptInstance = getFocusedObject();

		// Retrieve shape property of this FC
		ShapeRole currentShapeRole = (ShapeRole) flexoConceptInstance.getFlexoConcept()
				.getAccessibleProperty(FreeMetaModel.SHAPE_ROLE_NAME);

		// Retrieve actual shape element
		DiagramShape shapeElement = flexoConceptInstance.getFlexoActor(currentShapeRole);

		ShapeRole newShapeRole = (ShapeRole) getConcept().getAccessibleProperty(FreeMetaModel.SHAPE_ROLE_NAME);

		switch (getGrStrategy()) {
			case RedefineShapeOfConcept:
				// Sets concept GR with actual shape GR
				newShapeRole.getGraphicalRepresentation().setsWith(shapeElement.getGraphicalRepresentation(),
						ShapeGraphicalRepresentation.X, ShapeGraphicalRepresentation.Y);
				// Look at the palette element
				// DiagramPalette palette = freeModel.getMetaModel().getConceptsPalette();
				break;
			case GetConceptShape:
				// Sets actual shape GR with concept GR
				shapeElement.getGraphicalRepresentation().setsWith(newShapeRole.getGraphicalRepresentation(),
						ShapeGraphicalRepresentation.X, ShapeGraphicalRepresentation.Y);
				break;
			case KeepShape:
				// Nothing to do
				break;
		}

		// We will here bypass the classical DropScheme
		flexoConceptInstance.setFlexoConcept(concept);

		// In case of GRStrategy is to redefine concept shape, we now need to set GR of all instances
		if (getGrStrategy() == GRStrategy.RedefineShapeOfConcept) {
			for (FlexoConceptInstance fci : freeModel.getInstances(concept)) {
				fci.getFlexoActor(newShapeRole).getGraphicalRepresentation().setsWith(newShapeRole.getGraphicalRepresentation(),
						GraphicalRepresentationSet.IGNORED_PROPERTIES);
			}
		}

		if (freeModel.getInstances(concept).size() == 1) {
			// This was the first time such an instance of this concept is used
			// This might be a good idea to add a palette element (when non existant)
			DiagramPalette palette = freeModel.getMetaModel().getConceptsPalette();
			DiagramPaletteElement existingElement = null;
			TypedDiagramModelSlot ms = FMLControlledDiagramVirtualModelNature
					.getTypedDiagramModelSlot(freeModel.getMetaModel().getVirtualModel());
			for (FMLDiagramPaletteElementBinding b : ms.getPaletteElementBindings()) {
				if (b.getBoundFlexoConcept() == concept) {
					existingElement = b.getPaletteElement();
				}
			}
			if (existingElement == null) {
				// No palette element matching related concept was found
				freeModel.getMetaModel().createPaletteElementForConcept(concept, shapeElement.getGraphicalRepresentation(), this);
			}
		}

		// We should notify the creation of a new FlexoConcept
		freeModel.getPropertyChangeSupport().firePropertyChange("usedFlexoConcepts", null, getConcept());

		// This is used to notify the adding of a new instance of a flexo concept
		freeModel.getPropertyChangeSupport().firePropertyChange("getInstances(FlexoConcept)", null, getFocusedObject());
	}

	public VirtualModel getVirtualModel() {
		try {
			return getFreeModel().getVirtualModel();
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String errorMessage;

	public String getErrorMessage() {
		isValid();
		return errorMessage;
	}

	@Override
	public boolean isValid() {

		if (getConcept() == null) {
			errorMessage = getLocales().localizedForKey("no_concept_defined");
			return false;
		}

		return true;
	}

}
