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
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
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

		if (getEditor() == null) {
			return false;
		}

		DiagramContainerElement<?> container = (DiagramContainerElement<?>) target.getDrawable();

		// logger.info("dragging " + this + " in " + container);

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
