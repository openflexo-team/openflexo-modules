/**
 * 
 * Copyright (c) 2014, Openflexo
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

package org.openflexo.eamodule.view.menu;

import java.util.logging.Logger;

import org.openflexo.eamodule.controller.EAMController;
import org.openflexo.view.menu.EditMenu;

/**
 * 'Edit' menu for this module
 * 
 * @author yourname
 */
public class EAMEditMenu extends EditMenu {

	private static final Logger logger = Logger.getLogger(EAMEditMenu.class.getPackage().getName());

	// ==========================================================================
	// ============================= Instance Variables
	// =========================
	// ==========================================================================

	protected EAMController _eamController;

	// ==========================================================================
	// ============================= Constructor
	// ================================
	// ==========================================================================

	public EAMEditMenu(EAMController controller) {
		super(controller);
		_eamController = controller;
		addSeparator();
		// Add actions here
		// add(deleteItem = new FlexoMenuItem(EAMDelete.actionType, getController()));
		// add(cutItem = new FlexoMenuItem(EAMCut.actionType, getController()));
		// add(copyItem = new FlexoMenuItem(EAMCopy.actionType, getController()));
		// add(pasteItem = new FlexoMenuItem(EAMPaste.actionType, getController()));
		// add(selectAllItem = new FlexoMenuItem(EAMSelectAll.actionType, getController()));
	}

}
