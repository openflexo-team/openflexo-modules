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

import java.awt.Font;
import java.io.FileNotFoundException;
import java.util.List;

import org.openflexo.connie.DataBinding;
import org.openflexo.connie.type.PrimitiveType;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.shapes.ShapeSpecification.ShapeType;
import org.openflexo.foundation.DefaultFlexoObject;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.InvalidArgumentException;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.fml.DeletionScheme;
import org.openflexo.foundation.fml.FMLModelFactory;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.TextFieldParameter;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.action.CreateEditionAction;
import org.openflexo.foundation.fml.action.CreateFlexoBehaviour;
import org.openflexo.foundation.fml.action.CreateFlexoBehaviourParameter;
import org.openflexo.foundation.fml.action.CreateFlexoConcept;
import org.openflexo.foundation.fml.action.CreatePrimitiveRole;
import org.openflexo.foundation.fml.action.CreateTechnologyRole;
import org.openflexo.foundation.fml.editionaction.AssignationAction;
import org.openflexo.foundation.fml.editionaction.DeleteAction;
import org.openflexo.foundation.fml.editionaction.ExpressionAction;
import org.openflexo.foundation.fml.inspector.TextAreaInspectorEntry;
import org.openflexo.foundation.fml.inspector.TextFieldInspectorEntry;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelNature;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
import org.openflexo.technologyadapter.diagram.fml.action.CreateDiagramPalette;
import org.openflexo.technologyadapter.diagram.fml.action.CreateFMLControlledDiagramPaletteElement;
import org.openflexo.technologyadapter.diagram.fml.editionaction.AddShape;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteElement;
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
	public static final String DESCRIPTION_ROLE_NAME = "description";
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
			throw new InvalidArgumentException(
					"VirtualModel " + virtualModel + " does not have the FMLControlledDiagramVirtualModelNature");
		}
		this.virtualModel = virtualModel;
	}

	public FreeModellingProject getFreeModellingProject() {
		return fmProject;
	}

	public VirtualModel getVirtualModel() {
		return virtualModel;
	}

	public List<FreeModel> getFreeModels() {
		return getFreeModellingProject().getFreeModels(this);
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
			CreateFlexoConcept action;
			if (ownerAction != null) {
				action = CreateFlexoConcept.actionType.makeNewEmbeddedAction(getVirtualModel(), null, ownerAction);
			} else {
				action = CreateFlexoConcept.actionType.makeNewAction(getVirtualModel(), null, editor);
			}
			action.setNewFlexoConceptName(conceptName);
			action.doAction();
			returned = action.getNewFlexoConcept();

			// Creates shape property
			CreateTechnologyRole createShapeRole = null;
			if (ownerAction != null) {
				createShapeRole = CreateTechnologyRole.actionType.makeNewEmbeddedAction(returned, null, ownerAction);
			} else {
				createShapeRole = CreateTechnologyRole.actionType.makeNewAction(returned, null, editor);
			}
			createShapeRole.setModelSlot(getTypedDiagramModelSlot());
			createShapeRole.setRoleName(SHAPE_ROLE_NAME);
			createShapeRole.setFlexoRoleClass(ShapeRole.class);
			createShapeRole.doAction();
			ShapeRole role = (ShapeRole) createShapeRole.getNewFlexoRole();

			// Create new PrimitiveRole (String type) to store the name of this instance
			CreatePrimitiveRole createNameRole = null;
			if (ownerAction != null) {
				createNameRole = CreatePrimitiveRole.actionType.makeNewEmbeddedAction(returned, null, ownerAction);
			} else {
				createNameRole = CreatePrimitiveRole.actionType.makeNewAction(returned, null, editor);
			}
			createNameRole.setRoleName(NAME_ROLE_NAME);
			createNameRole.setPrimitiveType(PrimitiveType.String);
			createNameRole.doAction();

			// Bind shapes's label to name property
			role.setLabel(new DataBinding("name"));

			// Init a default GR
			FMLModelFactory factory = returned.getFMLModelFactory();
			ShapeGraphicalRepresentation shapeGR = factory.makeShapeGraphicalRepresentation(ShapeType.RECTANGLE);
			shapeGR.setX(10);
			shapeGR.setY(10);
			shapeGR.setWidth(80);
			shapeGR.setHeight(60);
			role.setGraphicalRepresentation(shapeGR);

			// Create new DropScheme
			CreateFlexoBehaviour createDropScheme = null;
			if (ownerAction != null) {
				createDropScheme = CreateFlexoBehaviour.actionType.makeNewEmbeddedAction(returned, null, ownerAction);
			} else {
				createDropScheme = CreateFlexoBehaviour.actionType.makeNewAction(returned, null, editor);
			}
			createDropScheme.setFlexoBehaviourName("drop");
			createDropScheme.setFlexoBehaviourClass(DropScheme.class);
			createDropScheme.doAction();
			DropScheme dropScheme = (DropScheme) createDropScheme.getNewFlexoBehaviour();
			dropScheme.setSkipConfirmationPanel(false);
			dropScheme.setTopTarget(true);

			// Create new DropScheme parameter
			CreateFlexoBehaviourParameter createDropSchemeParameter = null;
			if (ownerAction != null) {
				createDropSchemeParameter = CreateFlexoBehaviourParameter.actionType.makeNewEmbeddedAction(dropScheme, null, ownerAction);
			} else {
				createDropSchemeParameter = CreateFlexoBehaviourParameter.actionType.makeNewAction(dropScheme, null, editor);
			}
			createDropSchemeParameter.setParameterName("conceptName");
			createDropSchemeParameter.setFlexoBehaviourParameterClass(TextFieldParameter.class);
			createDropSchemeParameter.doAction();
			TextFieldParameter parameter = (TextFieldParameter) createDropSchemeParameter.getNewParameter();
			parameter.setDefaultValue(new DataBinding<String>("\"" + parameter.getName() + "\""));

			CreateEditionAction createAddShape = null;
			if (ownerAction != null) {
				createAddShape = CreateEditionAction.actionType.makeNewEmbeddedAction(dropScheme.getControlGraph(), null, ownerAction);
			} else {
				createAddShape = CreateEditionAction.actionType.makeNewAction(dropScheme.getControlGraph(), null, editor);
			}
			// createAddShape.actionChoice = CreateEditionActionChoice.ModelSlotSpecificAction;
			createAddShape.setModelSlot(getTypedDiagramModelSlot());
			createAddShape.setEditionActionClass(AddShape.class);
			createAddShape.setAssignation(new DataBinding(SHAPE_ROLE_NAME));
			createAddShape.doAction();

			// AssignationAction<DiagramShape> addShapeAssigment = (AssignationAction<DiagramShape>) createAddShape.getNewEditionAction();
			// AddShape addShape = (AddShape)addShapeAssigment.getAssignableAction();

			CreateEditionAction givesNameAction = null;
			if (ownerAction != null) {
				givesNameAction = CreateEditionAction.actionType.makeNewEmbeddedAction(dropScheme.getControlGraph(), null, ownerAction);
			} else {
				givesNameAction = CreateEditionAction.actionType.makeNewAction(dropScheme.getControlGraph(), null, editor);
			}
			// givesNameAction.actionChoice = CreateEditionActionChoice.BuiltInAction;
			givesNameAction.setEditionActionClass(ExpressionAction.class);
			givesNameAction.setAssignation(new DataBinding(NAME_ROLE_NAME));
			givesNameAction.doAction();

			AssignationAction<?> nameAssignation = (AssignationAction<?>) givesNameAction.getNewEditionAction();
			((ExpressionAction) nameAssignation.getAssignableAction()).setExpression(new DataBinding("parameters.conceptName"));

			// Create new DeletionScheme
			CreateFlexoBehaviour createDeletionScheme = null;
			if (ownerAction != null) {
				createDeletionScheme = CreateFlexoBehaviour.actionType.makeNewEmbeddedAction(returned, null, ownerAction);
			} else {
				createDeletionScheme = CreateFlexoBehaviour.actionType.makeNewAction(returned, null, editor);
			}
			createDeletionScheme.setFlexoBehaviourName("delete");
			createDeletionScheme.setFlexoBehaviourClass(DeletionScheme.class);
			createDeletionScheme.doAction();
			DeletionScheme deletionScheme = (DeletionScheme) createDeletionScheme.getNewFlexoBehaviour();
			deletionScheme.setSkipConfirmationPanel(true);

			CreateEditionAction createDeleteShape = null;
			if (ownerAction != null) {
				createDeleteShape = CreateEditionAction.actionType.makeNewEmbeddedAction(deletionScheme.getControlGraph(), null,
						ownerAction);
			} else {
				createDeleteShape = CreateEditionAction.actionType.makeNewAction(deletionScheme.getControlGraph(), null, editor);
			}
			// createDeleteShape.actionChoice = CreateEditionActionChoice.BuiltInAction;
			// createDeleteShape.setModelSlot(getTypedDiagramModelSlot());
			createDeleteShape.setEditionActionClass(DeleteAction.class);
			createDeleteShape.doAction();

			DeleteAction deleteShape = (DeleteAction) createDeleteShape.getNewEditionAction();
			deleteShape.setObject(new DataBinding(SHAPE_ROLE_NAME));

			// Create inspector
			TextFieldInspectorEntry nameEntry = returned.getInspector().createNewTextField();
			nameEntry.setName(NAME_ROLE_NAME);
			nameEntry.setLabel(NAME_ROLE_NAME);
			nameEntry.setData(new DataBinding<String>("name"));
			TextAreaInspectorEntry descriptionEntry = returned.getInspector().createNewTextArea();
			descriptionEntry.setName(DESCRIPTION_ROLE_NAME);
			descriptionEntry.setLabel(DESCRIPTION_ROLE_NAME);
			descriptionEntry.setData(new DataBinding<String>("description"));

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

	public DiagramPaletteElement createPaletteElementForConcept(FlexoConcept concept, ShapeGraphicalRepresentation gr,
			FlexoAction<?, ?, ?> ownerAction) {

		CreateFMLControlledDiagramPaletteElement action = CreateFMLControlledDiagramPaletteElement.actionType
				.makeNewEmbeddedAction(concept.getOwningVirtualModel(), null, ownerAction);
		action.setPalette(getConceptsPalette());

		ShapeGraphicalRepresentation paletteElementGR = (ShapeGraphicalRepresentation) gr.cloneObject();
		int px, py;
		int index = getConceptsPalette().getElements().size();
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
		paletteElementGR.setText(concept.getName());

		action.setGraphicalRepresentation(paletteElementGR);
		action.setConcept(concept);
		if (concept.getFlexoBehaviours(DropScheme.class).size() > 0) {
			action.setDropScheme(concept.getFlexoBehaviours(DropScheme.class).get(0));
		}

		action.doAction();

		return action.getNewElement();

	}

}
