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

import static org.junit.Assert.assertTrue;

import java.awt.Font;
import java.io.FileNotFoundException;

import org.openflexo.antar.binding.DataBinding;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.shapes.ShapeSpecification.ShapeType;
import org.openflexo.foundation.DefaultFlexoObject;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.InvalidArgumentException;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.viewpoint.FlexoConcept;
import org.openflexo.foundation.viewpoint.PrimitiveRole;
import org.openflexo.foundation.viewpoint.PrimitiveRole.PrimitiveType;
import org.openflexo.foundation.viewpoint.TextFieldParameter;
import org.openflexo.foundation.viewpoint.VirtualModel;
import org.openflexo.foundation.viewpoint.VirtualModelModelFactory;
import org.openflexo.foundation.viewpoint.action.AddFlexoConcept;
import org.openflexo.foundation.viewpoint.action.CreateEditionAction;
import org.openflexo.foundation.viewpoint.action.CreateEditionAction.CreateEditionActionChoice;
import org.openflexo.foundation.viewpoint.action.CreateEditionScheme;
import org.openflexo.foundation.viewpoint.action.CreateEditionSchemeParameter;
import org.openflexo.foundation.viewpoint.action.CreateFlexoRole;
import org.openflexo.foundation.viewpoint.editionaction.AssignationAction;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelNature;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
import org.openflexo.technologyadapter.diagram.fml.action.CreateDiagramPalette;
import org.openflexo.technologyadapter.diagram.fml.editionaction.AddShape;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;

/**
 * Represents a {@link FreeMetaModel} in the FreeModellingEditor<br>
 * 
 * The base of a {@link FreeMetaModel} is a {@link VirtualModel} with the specific {@link FMLControlledDiagramVirtualModelNature}<br>
 * From a technical point of view, a {@link FreeMetaModel} is just a wrapper above a {@link VirtualModel} located in project's
 * freeModellingViewPoint
 * 
 * @author sylvain
 * 
 */
public class FreeMetaModel extends DefaultFlexoObject {

	public static final String NONE_FLEXO_CONCEPT = "None";
	public static final String SHAPE_ROLE_NAME = "shape";
	public static final String NAME_ROLE_NAME = "name";
	public static final String PALETTE_NAME = "Concepts";
	public static final int PALETTE_GRID_WIDTH = 50;
	public static final int PALETTE_GRID_HEIGHT = 40;
	public static final Font PALETTE_DEFAULT_TEXT_FONT = new Font("SansSerif", Font.PLAIN, 7);
	public static final Font PALETTE_LABEL_FONT = new Font("SansSerif", Font.PLAIN, 11);

	private final VirtualModel virtualModel;
	private final FreeModellingProject fmProject;

	/**
	 * Provides generic method used to retrieve URI of DiagramSpecification of a given {@link FreeMetaModel}
	 * 
	 * @param project
	 * @param metaModelName
	 * @return
	 */
	public static String getDiagramSpecificationURI(FlexoProject project, String metaModelName) {
		return project.getURI() + "/DiagramSpecification/" + metaModelName;
	}

	public FreeMetaModel(VirtualModel virtualModel, FreeModellingProject fmProject) throws InvalidArgumentException {
		super();
		this.fmProject = fmProject;
		if (!virtualModel.hasNature(FMLControlledDiagramVirtualModelNature.INSTANCE)) {
			throw new InvalidArgumentException("VirtualModel " + virtualModel + " does not have the FMLControlledDiagramVirtualModelNature");
		}
		this.virtualModel = virtualModel;
	}

	public FreeModellingProject getFreeModellingProject() {
		return fmProject;
	}

	public VirtualModel getVirtualModel() {
		return virtualModel;
	}

	public FreeModellingProjectNature getProjectNature() {
		return getFreeModellingProject().getProjectNature();
	}

	public TypedDiagramModelSlot getTypedDiagramModelSlot() {
		return FMLControlledDiagramVirtualModelNature.getTypedDiagramModelSlot(getVirtualModel());
	}

	public String getName() {
		return virtualModel.getName();
	}

	public DiagramSpecification getDiagramSpecification() {
		try {
			return getTypedDiagramModelSlot().getMetaModelResource().getResourceData(null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ResourceLoadingCancelledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FlexoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String getDiagramSpecificationURI() {
		return getDiagramSpecificationURI(getFreeModellingProject().getProject(), getName());
	}

	public FlexoConcept getNoneFlexoConcept(FlexoEditor editor, FlexoAction<?, ?, ?> ownerAction) {
		return getFlexoConcept(NONE_FLEXO_CONCEPT, editor, ownerAction);
	}

	/**
	 * Return (creates when non-existant) a FlexoConcept (in the context of FreeModellingEditor)
	 * 
	 * @param conceptName
	 * @param editor
	 * @param ownerAction
	 * @return
	 */
	public FlexoConcept getFlexoConcept(String conceptName, FlexoEditor editor, FlexoAction<?, ?, ?> ownerAction) {

		FlexoConcept returned = getVirtualModel().getFlexoConcept(conceptName);

		if (returned == null) {

			// Creates the concept
			AddFlexoConcept action = AddFlexoConcept.actionType.makeNewAction(getVirtualModel(), null, editor);
			action.setNewFlexoConceptName(conceptName);
			action.doAction();
			returned = action.getNewFlexoConcept();

			// Creates shape role
			CreateFlexoRole createShapeRole = null;
			if (ownerAction != null) {
				createShapeRole = CreateFlexoRole.actionType.makeNewEmbeddedAction(returned, null, ownerAction);
			} else {
				createShapeRole = CreateFlexoRole.actionType.makeNewAction(returned, null, editor);
			}
			createShapeRole.setModelSlot(getTypedDiagramModelSlot());
			createShapeRole.setRoleName(SHAPE_ROLE_NAME);
			createShapeRole.setFlexoRoleClass(ShapeRole.class);
			createShapeRole.doAction();
			ShapeRole role = (ShapeRole) createShapeRole.getNewFlexoRole();

			// Create new PrimitiveRole (String type) to store the name of this instance
			CreateFlexoRole createNameRole = null;
			if (ownerAction != null) {
				createNameRole = CreateFlexoRole.actionType.makeNewEmbeddedAction(returned, null, ownerAction);
			} else {
				createNameRole = CreateFlexoRole.actionType.makeNewAction(returned, null, editor);
			}
			createNameRole.setRoleName(NAME_ROLE_NAME);
			createNameRole.setFlexoRoleClass(PrimitiveRole.class);
			createNameRole.setPrimitiveType(PrimitiveType.String);
			createNameRole.doAction();

			// Bind shapes's label to name role
			role.setLabel(new DataBinding("name"));

			// Init a default GR
			VirtualModelModelFactory factory = returned.getVirtualModelFactory();
			ShapeGraphicalRepresentation shapeGR = factory.makeShapeGraphicalRepresentation(ShapeType.RECTANGLE);
			shapeGR.setX(10);
			shapeGR.setY(10);
			shapeGR.setWidth(80);
			shapeGR.setHeight(60);
			role.setGraphicalRepresentation(shapeGR);

			// Create new DropScheme
			CreateEditionScheme createDropScheme = null;
			if (ownerAction != null) {
				createDropScheme = CreateEditionScheme.actionType.makeNewEmbeddedAction(returned, null, ownerAction);
			} else {
				createDropScheme = CreateEditionScheme.actionType.makeNewAction(returned, null, editor);
			}
			createDropScheme.setFlexoBehaviourName("drop");
			createDropScheme.setFlexoBehaviourClass(DropScheme.class);
			createDropScheme.doAction();
			assertTrue(createDropScheme.hasActionExecutionSucceeded());
			DropScheme dropScheme = (DropScheme) createDropScheme.getNewFlexoBehaviour();
			dropScheme.setSkipConfirmationPanel(true);

			// Create new DropScheme parameter
			CreateEditionSchemeParameter createDropSchemeParameter = null;
			if (ownerAction != null) {
				createDropSchemeParameter = CreateEditionSchemeParameter.actionType.makeNewEmbeddedAction(dropScheme, null, ownerAction);
			} else {
				createDropSchemeParameter = CreateEditionSchemeParameter.actionType.makeNewAction(dropScheme, null, editor);
			}
			createDropSchemeParameter.setParameterName("conceptName");
			createDropSchemeParameter.setFlexoBehaviourParameterClass(TextFieldParameter.class);
			createDropSchemeParameter.doAction();
			TextFieldParameter parameter = (TextFieldParameter) createDropSchemeParameter.getNewParameter();

			CreateEditionAction createAddShape = null;
			if (ownerAction != null) {
				createAddShape = CreateEditionAction.actionType.makeNewEmbeddedAction(dropScheme, null, ownerAction);
			} else {
				createAddShape = CreateEditionAction.actionType.makeNewAction(dropScheme, null, editor);
			}
			createAddShape.actionChoice = CreateEditionActionChoice.ModelSlotSpecificAction;
			createAddShape.setModelSlot(getTypedDiagramModelSlot());
			createAddShape.setModelSlotSpecificActionClass(AddShape.class);
			createAddShape.doAction();

			AddShape addShape = (AddShape) createAddShape.getNewEditionAction();
			addShape.setAssignation(new DataBinding(SHAPE_ROLE_NAME));

			CreateEditionAction givesNameAction = null;
			if (ownerAction != null) {
				givesNameAction = CreateEditionAction.actionType.makeNewEmbeddedAction(dropScheme, null, ownerAction);
			} else {
				givesNameAction = CreateEditionAction.actionType.makeNewAction(dropScheme, null, editor);
			}
			givesNameAction.actionChoice = CreateEditionActionChoice.BuiltInAction;
			givesNameAction.setBuiltInActionClass(AssignationAction.class);
			givesNameAction.doAction();

			AssignationAction nameAssignation = (AssignationAction) givesNameAction.getNewEditionAction();
			nameAssignation.setAssignation(new DataBinding(NAME_ROLE_NAME));
			nameAssignation.setValue(new DataBinding("parameters.conceptName"));

			returned.getInspector().setRenderer(new DataBinding<String>("instance.name"));
		}
		return returned;
	}

	public DiagramPalette getConceptsPalette() {
		if (getDiagramSpecification().getPalette(PALETTE_NAME) == null) {
			// Should not happen, but...
			CreateDiagramPalette createPalette = CreateDiagramPalette.actionType.makeNewAction(getDiagramSpecification(), null, null);
			createPalette.setNewPaletteName(FreeMetaModel.PALETTE_NAME);
			createPalette.doAction();
			System.out.println("Palette has been created: " + createPalette.getNewPalette());
		}
		return getDiagramSpecification().getPalette(PALETTE_NAME);
	}

}
