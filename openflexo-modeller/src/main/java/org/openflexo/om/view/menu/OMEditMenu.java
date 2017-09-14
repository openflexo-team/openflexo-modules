/**
 * 
 * Copyright (c) 2017, Openflexo
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

package org.openflexo.om.view.menu;

import java.util.logging.Logger;

import org.openflexo.om.OpenflexoModeller;
import org.openflexo.om.controller.OMController;
import org.openflexo.view.menu.EditMenu;

/**
 * 'Edit' menu for {@link OpenflexoModeller} module
 * 
 * @author sylvain
 */
@SuppressWarnings("serial")
public class OMEditMenu extends EditMenu {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(OMEditMenu.class.getPackage().getName());

	public OMEditMenu(OMController controller) {
		super(controller);
		addSeparator();
		// Add actions here
		// add(deleteItem = new FlexoMenuItem(XXXDelete.actionType, getController()));
		// add(cutItem = new FlexoMenuItem(XXXCut.actionType, getController()));
		// add(copyItem = new FlexoMenuItem(XXXCopy.actionType, getController()));
		// add(pasteItem = new FlexoMenuItem(XXXPaste.actionType, getController()));
		// add(selectAllItem = new FlexoMenuItem(XXXSelectAll.actionType, getController()));
	}

}
