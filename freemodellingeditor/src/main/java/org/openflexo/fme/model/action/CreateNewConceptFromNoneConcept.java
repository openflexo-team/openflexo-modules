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
import org.openflexo.fme.FreeModellingEditor;
import org.openflexo.fme.model.FreeMetaModel;
import org.openflexo.fme.model.FreeModel;
import org.openflexo.fme.model.FreeModellingProject;
import org.openflexo.fme.model.FreeModellingProjectNature;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.InvalidArgumentException;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.localization.LocalizedDelegate;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.toolbox.StringUtils;

/**
 * This action is used to create a new FlexoConcept
 * 
 * @author sylvain
 * 
 */
public class CreateNewConceptFromNoneConcept extends FMEAction<CreateNewConceptFromNoneConcept, FlexoConceptInstance, FlexoObject> {

	private static final Logger logger = Logger.getLogger(CreateNewConceptFromNoneConcept.class.getPackage().getName());

	public static FlexoActionFactory<CreateNewConceptFromNoneConcept, FlexoConceptInstance, FlexoObject> actionType = new FlexoActionFactory<CreateNewConceptFromNoneConcept, FlexoConceptInstance, FlexoObject>(
			"create_new_concept", FlexoActionFactory.defaultGroup, FlexoActionFactory.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateNewConceptFromNoneConcept makeNewAction(FlexoConceptInstance focusedObject, Vector<FlexoObject> globalSelection,
				FlexoEditor editor) {
			return new CreateNewConceptFromNoneConcept(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(FlexoConceptInstance object, Vector<FlexoObject> globalSelection) {
			return true;
		}

		@Override
		public boolean isEnabledForSelection(FlexoConceptInstance object, Vector<FlexoObject> globalSelection) {
			return object.getFlexoConcept().getName().equals(FreeMetaModel.NONE_FLEXO_CONCEPT);
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(CreateNewConceptFromNoneConcept.actionType, FlexoConceptInstance.class);
	}

	private String newConceptName;
	private String newConceptDescription;

	private FlexoConcept newFlexoConcept;

	// private FlexoConceptInstance newFlexoConceptInstance;

	CreateNewConceptFromNoneConcept(FlexoConceptInstance focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
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
		FlexoProject<?> project = null;
		if (getFocusedObject().getResourceCenter() instanceof FlexoProject) {
			project = (FlexoProject<?>) getFocusedObject().getResourceCenter();
		}
		else if (getFocusedObject().getResourceCenter().getDelegatingProjectResource() != null) {
			project = getFocusedObject().getResourceCenter().getDelegatingProjectResource().getFlexoProject();
		}
		else {
			logger.warning("Could not access to FlexoProject from " + getFocusedObject());
			return null;
		}
		return getFreeModellingProjectNature().getFreeModellingProject(project);
	}

	public FreeModel getFreeModel() throws InvalidArgumentException {
		return getFreeModellingProject().getFreeModel((FMLRTVirtualModelInstance) getFocusedObject().getVirtualModelInstance());
	}

	@Override
	protected void doAction(Object context) throws FlexoException {

		FreeModel freeModel = getFreeModel();
		if (freeModel == null) {
			throw new InvalidArgumentException("FlexoConceptInstance does not belong to any FreeModel");
		}

		FlexoConceptInstance flexoConceptInstance = getFocusedObject();

		// Retrieve shape property of None FC
		ShapeRole noneShapeRole = (ShapeRole) flexoConceptInstance.getFlexoConcept().getAccessibleProperty(FreeMetaModel.SHAPE_ROLE_NAME);

		// Retrieve actual shape element
		DiagramShape shapeElement = flexoConceptInstance.getFlexoActor(noneShapeRole);

		// Now we create the new concept
		newFlexoConcept = freeModel.getMetaModel().getFlexoConcept(getNewConceptName(), getEditor(), this);

		// Add entry in palette
		freeModel.getMetaModel().createPaletteElementForConcept(newFlexoConcept, shapeElement.getGraphicalRepresentation(), this);

		/*
		CreateDiagramPaletteElement createDiagramPaletteElement = CreateDiagramPaletteElement.actionType.makeNewEmbeddedAction(freeModel
				.getMetaModel().getConceptsPalette(), null, this);
		ShapeGraphicalRepresentation paletteElementGR = (ShapeGraphicalRepresentation) shapeElement.getGraphicalRepresentation()
				.cloneObject();
		paletteElementGR.setX(10);
		paletteElementGR.setY(10);
		paletteElementGR.setText(getNewConceptName());
		paletteElementGR.setIsFloatingLabel(false);
		createDiagramPaletteElement.setGraphicalRepresentation(paletteElementGR);
		createDiagramPaletteElement.setNewElementName(getNewConceptName());
		createDiagramPaletteElement.doAction();
		
		DiagramPaletteElement paletteElement = createDiagramPaletteElement.getNewElement();
		
		System.out.println("Created palette element: " + paletteElement);
		int px, py;
		int index = freeModel.getMetaModel().getConceptsPalette().getElements().indexOf(createDiagramPaletteElement.getNewElement());
		px = index % 3;
		py = index / 3;
		
		// FACTORY.applyDefaultProperties(gr);
		if (paletteElementGR.getShapeSpecification().getShapeType() == ShapeType.SQUARE
				|| paletteElementGR.getShapeSpecification().getShapeType() == ShapeType.CIRCLE) {
			paletteElementGR.setX(10);
			paletteElementGR.setY(10);
			paletteElementGR.setX(px * FreeMetaModel.PALETTE_GRID_WIDTH + 15);
			paletteElementGR.setY(py * FreeMetaModel.PALETTE_GRID_HEIGHT + 10);
			paletteElementGR.setWidth(30);
			paletteElementGR.setHeight(30);
		} else {
			paletteElementGR.setX(px * FreeMetaModel.PALETTE_GRID_WIDTH + 10);
			paletteElementGR.setY(py * FreeMetaModel.PALETTE_GRID_HEIGHT + 10);
			paletteElementGR.setWidth(40);
			paletteElementGR.setHeight(30);
		}
		
		// Create binding and associate it
		DropScheme dropScheme = newFlexoConcept.getFlexoBehaviours(DropScheme.class).get(0);
		FMLDiagramPaletteElementBinding newBinding = newFlexoConcept.getVirtualModel().getFMLModelFactory()
				.newInstance(FMLDiagramPaletteElementBinding.class);
		newBinding.setPaletteElement(paletteElement);
		newBinding.setFlexoConcept(newFlexoConcept);
		newBinding.setDropScheme(dropScheme);
		freeModel.getMetaModel().getTypedDiagramModelSlot().addToPaletteElementBindings(newBinding);
		*/

		// Sets new concept GR with actual shape GR
		ShapeRole shapeRole = (ShapeRole) newFlexoConcept.getAccessibleProperty(FreeMetaModel.SHAPE_ROLE_NAME);
		shapeRole.getGraphicalRepresentation().setsWith(shapeElement.getGraphicalRepresentation());
		shapeRole.getGraphicalRepresentation().setX(10);
		shapeRole.getGraphicalRepresentation().setY(10);

		// We will here bypass the classical DropScheme
		flexoConceptInstance.setFlexoConcept(newFlexoConcept);

		// We should notify the creation of a new FlexoConcept
		freeModel.getPropertyChangeSupport().firePropertyChange("usedFlexoConcepts", null, newFlexoConcept);

		// This is used to notify the adding of a new instance of a flexo concept
		freeModel.getPropertyChangeSupport().firePropertyChange("getInstances(FlexoConcept)", null, getFocusedObject());
	}

	public String getNewConceptName() {
		return newConceptName;
	}

	public void setNewConceptName(String newConceptName) {
		boolean wasValid = isValid();
		this.newConceptName = newConceptName;
		getPropertyChangeSupport().firePropertyChange("newConceptName", null, newConceptName);
		getPropertyChangeSupport().firePropertyChange("isValid", wasValid, isValid());
	}

	public String getNewConceptDescription() {
		return newConceptDescription;
	}

	public void setNewConceptDescription(String newConceptDescription) {
		boolean wasValid = isValid();
		this.newConceptDescription = newConceptDescription;
		getPropertyChangeSupport().firePropertyChange("newConceptDescription", null, newConceptDescription);
		getPropertyChangeSupport().firePropertyChange("isValid", wasValid, isValid());
	}

	public FlexoConcept getNewFlexoConcept() {
		return newFlexoConcept;
	}

	@Override
	public boolean isValid() {

		if (StringUtils.isEmpty(newConceptName)) {
			return false;
		}

		if (getFocusedObject().getVirtualModelInstance().getVirtualModel().getFlexoConcept(newConceptName) != null) {
			return false;
		}

		return true;
	}

}
