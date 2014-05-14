/*
 * (c) Copyright 2010-2011 AgileBirds
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
package org.openflexo.fme.controller.editor;

import java.util.logging.Logger;

import org.openflexo.fge.Drawing.DrawingTreeNode;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.ShapeGraphicalRepresentation.LocationConstraints;
import org.openflexo.fge.control.DianaInteractiveEditor.EditorTool;
import org.openflexo.fge.geom.FGEPoint;
import org.openflexo.fge.shapes.ShapeSpecification.ShapeType;
import org.openflexo.fib.FIBLibrary;
import org.openflexo.fib.controller.FIBDialog;
import org.openflexo.fib.model.FIBComponent;
import org.openflexo.fme.model.action.DropShape;
import org.openflexo.foundation.action.FlexoUndoManager.FlexoActionCompoundEdit;
import org.openflexo.foundation.view.FlexoConceptInstance;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.technologyadapter.diagram.controller.DiagramCst;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.CommonPalette;
import org.openflexo.technologyadapter.diagram.model.DiagramContainerElement;
import org.openflexo.view.FlexoFrame;
import org.openflexo.view.controller.FlexoFIBController;

public class FreeShapesPalette extends CommonPalette {

	@SuppressWarnings("unused")
	private static final Logger logger = FlexoLogger.getLogger(FreeShapesPalette.class.getPackage().getName());

	public FreeShapesPalette(FreeModelDiagramEditor editor) {
		super(editor);
	}

	@Override
	public FreeModelDiagramEditor getEditor() {
		return (FreeModelDiagramEditor) super.getEditor();
	}

	@Override
	public boolean handleBasicGraphicalRepresentationDrop(DrawingTreeNode<?, ?> target, ShapeGraphicalRepresentation gr,
			FGEPoint dropLocation, boolean applyCurrentForeground, boolean applyCurrentBackground, boolean applyCurrentTextStyle,
			boolean applyCurrentShadowStyle, boolean isImage, boolean resize) {

		System.out.println("handleBasicGraphicalRepresentationDrop in FME !!!");

		if (getEditor() == null) {
			return false;
		}

		DiagramContainerElement<?> container = (DiagramContainerElement<?>) target.getDrawable();

		logger.info("dragging " + this + " in " + container);

		FlexoActionCompoundEdit edit = (FlexoActionCompoundEdit) getEditor().getUndoManager().startRecording("Dragging new Element");

		ShapeGraphicalRepresentation shapeGR = getEditor().getFactory().makeShapeGraphicalRepresentation(gr);

		shapeGR.setIsReadOnly(false);
		shapeGR.setIsFocusable(true);
		shapeGR.setIsSelectable(true);
		shapeGR.setLocationConstraints(LocationConstraints.FREELY_MOVABLE);

		if (resize) {
			if (shapeGR.getShapeSpecification().getShapeType() == ShapeType.SQUARE
					|| shapeGR.getShapeSpecification().getShapeType() == ShapeType.CIRCLE) {
				shapeGR.setWidth(50);
				shapeGR.setHeight(50);
			} else {
				shapeGR.setWidth(60);
				shapeGR.setHeight(45);
			}
		}
		if (applyCurrentForeground) {
			shapeGR.setForeground(getEditor().getInspectedForegroundStyle().cloneStyle());
		}
		if (applyCurrentBackground) {
			shapeGR.setBackground(getEditor().getInspectedBackgroundStyle().cloneStyle());
		}
		if (applyCurrentTextStyle) {
			shapeGR.setTextStyle(getEditor().getInspectedTextStyle().cloneStyle());
		}
		if (applyCurrentShadowStyle) {
			shapeGR.setShadowStyle(getEditor().getInspectedShadowStyle().cloneStyle());
		}

		// shapeGR.setX(dropLocation.x);
		// shapeGR.setY(dropLocation.y);

		if (isImage) {
			FIBComponent fibComponent = FIBLibrary.instance().retrieveFIBComponent(DiagramCst.IMPORT_IMAGE_FILE_DIALOG_FIB);
			FIBDialog dialog = FIBDialog.instanciateAndShowDialog(fibComponent, shapeGR, FlexoFrame.getActiveFrame(), true,
					new FlexoFIBController(fibComponent, getEditor().getFlexoController()));
		}

		System.out.println("OK, create DropShape");
		System.out.println("location=" + shapeGR.getLocation());
		System.out.println("size=" + shapeGR.getSize());

		DropShape action = DropShape.actionType.makeNewAction(container, null, getEditor().getFlexoController().getEditor());
		action.setFreeModel(getEditor().getFreeModel());
		action.setGraphicalRepresentation(shapeGR);
		action.setDropLocation(dropLocation);

		action.setCompoundEdit(edit);
		action.doAction();

		FlexoConceptInstance newFlexoConceptInstance = action.getNewFlexoConceptInstance();
		// DiagramShape shape = newFlexoConceptInstance.getFlexoActor(patternRole)

		getEditor().setCurrentTool(EditorTool.SelectionTool);

		// getEditor().setSelectedObject(getEditor().getDrawing().getDrawingTreeNode(newShape));

		return action.hasActionExecutionSucceeded();
	}
}
