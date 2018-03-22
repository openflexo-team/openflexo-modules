/**
 * 
 * Copyright (c) 2013-2017, Openflexo
 * Copyright (c) 2011-2012, AgileBirds
 * 
 * This file is part of Flexovieweditor, a component of the software infrastructure 
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

package org.openflexo.om.controller;

import java.util.logging.Logger;

import org.openflexo.foundation.FlexoObject;
import org.openflexo.om.OpenflexoModeller;
import org.openflexo.selection.MouseSelectionManager;

/**
 * This is the selection manager responsible for selection in {@link OpenflexoModeller} module
 * 
 * @author sylvain
 */
public class OMSelectionManager extends MouseSelectionManager {

	protected static final Logger logger = Logger.getLogger(OMSelectionManager.class.getPackage().getName());

	public OMSelectionManager(OMController controller) {
		super(controller);
		// FlexoMenuBar menuBar = controller.getMenuBar();
		/*_clipboard = new VEClipboard(this, menuBar.getEditMenu(controller).copyItem, menuBar.getEditMenu(controller).pasteItem,
				menuBar.getEditMenu(controller).cutItem);*/
		_contextualMenuManager = new OMContextualMenuManager(this, controller);
	}

	public OMController getODController() {
		return (OMController) getController();
	}

	/*@Override
	public boolean performSelectionSelectAll() {
		if (logger.isLoggable(Level.WARNING)) {
			logger.warning("'Select All' not implemented yet in this module");
		}
		return false;
	}*/

	// ==========================================================================
	// ============================= Deletion
	// ===================================
	// ==========================================================================

	/**
	 * Returns the root object that can be currently edited
	 * 
	 * @return FlexoModelObject
	 */
	@Override
	public FlexoObject getRootFocusedObject() {
		return getODController().getCurrentDisplayedObjectAsModuleView();
	}

	// @Override
	// public FlexoObject getPasteContext() {
	// TODO
	/*if (getVEController().getCurrentModuleView() instanceof DiagramModuleView) {
		DiagramView v = ((DiagramModuleView) getVEController().getCurrentModuleView()).getController().getDrawingView();
		GraphicalRepresentation gr = v.getController().getLastSelectedNode();
		if (gr != null && gr.getDrawable() instanceof FlexoModelObject) {
			return (FlexoModelObject) gr.getDrawable();
		} else {
			return (FlexoModelObject) ((DrawingView<?>) getVEController().getCurrentModuleView()).getDrawingGraphicalRepresentation()
					.getDrawable();
		}
	}*/
	// return null;
	// }

	// @Override
	// public PastingGraphicalContext getPastingGraphicalContext() {
	// PastingGraphicalContext pgc = new PastingGraphicalContext();
	/*if (getVEController().getCurrentModuleView() instanceof DiagramModuleView) {
		DiagramView v = ((DiagramModuleView) getVEController().getCurrentModuleView()).getController().getDrawingView();
		DrawingController controller = v.getController();
		GraphicalRepresentation target = controller.getLastSelectedNode();
		if (target == null) {
			pgc.targetContainer = controller.getDrawingView();
		} else {
			pgc.targetContainer = (JComponent) v.viewForNode(target);
		}
		if (controller.getLastClickedPoint() != null) {
			pgc.precisePastingLocation = controller.getLastClickedPoint();
			pgc.pastingLocation = new Point((int) pgc.precisePastingLocation.getX(), (int) pgc.precisePastingLocation.getY());
		} else {
			pgc.precisePastingLocation = new DianaPoint(0, 0);
			pgc.pastingLocation = new Point(0, 0);
		}
	}*/
	// return pgc;
	// }

}
