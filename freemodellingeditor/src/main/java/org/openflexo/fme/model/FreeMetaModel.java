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

import java.io.FileNotFoundException;

import org.openflexo.antar.binding.DataBinding;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.shapes.Rectangle;
import org.openflexo.foundation.DefaultFlexoObject;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.InvalidArgumentException;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.viewpoint.FlexoConcept;
import org.openflexo.foundation.viewpoint.VirtualModel;
import org.openflexo.foundation.viewpoint.VirtualModelModelFactory;
import org.openflexo.foundation.viewpoint.action.AddFlexoConcept;
import org.openflexo.foundation.viewpoint.action.CreateEditionAction;
import org.openflexo.foundation.viewpoint.action.CreateEditionAction.CreateEditionActionChoice;
import org.openflexo.foundation.viewpoint.action.CreateEditionScheme;
import org.openflexo.foundation.viewpoint.action.CreateFlexoRole;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelNature;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
import org.openflexo.technologyadapter.diagram.fml.editionaction.AddShape;
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
	public static final String SHAPE_ROLE_NAME = "Shape";

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
		FlexoConcept noneFC = getVirtualModel().getFlexoConcept(NONE_FLEXO_CONCEPT);
		if (noneFC == null) {
			AddFlexoConcept action = AddFlexoConcept.actionType.makeNewAction(getVirtualModel(), null, editor);
			action.setNewFlexoConceptName(NONE_FLEXO_CONCEPT);
			action.doAction();
			noneFC = action.getNewFlexoConcept();

			CreateFlexoRole createShapeRole = null;
			if (ownerAction != null) {
				createShapeRole = CreateFlexoRole.actionType.makeNewEmbeddedAction(noneFC, null, ownerAction);
			} else {
				createShapeRole = CreateFlexoRole.actionType.makeNewAction(noneFC, null, editor);
			}
			createShapeRole.setRoleName(SHAPE_ROLE_NAME);
			createShapeRole.setFlexoRoleClass(ShapeRole.class);
			createShapeRole.doAction();

			ShapeRole role = (ShapeRole) createShapeRole.getNewFlexoRole();
			VirtualModelModelFactory factory = noneFC.getVirtualModelFactory();
			ShapeGraphicalRepresentation shapeGR = factory.newInstance(ShapeGraphicalRepresentation.class);
			Rectangle rectangleShape = factory.newInstance(Rectangle.class);
			shapeGR.setShapeSpecification(rectangleShape);
			role.setGraphicalRepresentation(shapeGR);

			CreateEditionScheme createDropScheme = null;
			if (ownerAction != null) {
				createDropScheme = CreateEditionScheme.actionType.makeNewEmbeddedAction(noneFC, null, ownerAction);
			} else {
				createDropScheme = CreateEditionScheme.actionType.makeNewAction(noneFC, null, editor);
			}
			createDropScheme.setFlexoBehaviourName("drop");
			createDropScheme.setFlexoBehaviourClass(DropScheme.class);
			createDropScheme.doAction();
			assertTrue(createDropScheme.hasActionExecutionSucceeded());
			DropScheme dropScheme = (DropScheme) createDropScheme.getNewFlexoBehaviour();

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

		}
		return noneFC;
	}

}
