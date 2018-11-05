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

import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;

import org.openflexo.om.OpenflexoModeller;
import org.openflexo.om.controller.OMController;
import org.openflexo.view.menu.FlexoMenuItem;
import org.openflexo.view.menu.ToolsMenu;

/**
 * 'Tools' menu for {@link OpenflexoModeller} Module
 * 
 * @author sylvain
 */
@SuppressWarnings("serial")
public class OMToolsMenu extends ToolsMenu {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(OMToolsMenu.class.getPackage().getName());

	public FlexoMenuItem checkViewPointLibraryConsistencyItem;
	public FlexoMenuItem checkViewPointConsistencyItem;

	public OMToolsMenu(OMController controller) {
		super(controller);
	}

	@Override
	public OMController getController() {
		return (OMController) super.getController();
	}

	@Override
	public void addSpecificItems() {
		add(checkViewPointLibraryConsistencyItem = new CheckViewPointLibraryConsistencyItem());
		add(checkViewPointConsistencyItem = new CheckViewPointConsistencyItem());
		addSeparator();
	}

	// ==========================================================================
	// ======================= CheckWorkflowConsistency
	// =========================
	// ==========================================================================

	public class CheckViewPointLibraryConsistencyItem extends FlexoMenuItem {

		public CheckViewPointLibraryConsistencyItem() {
			super(new CheckViewPointLibraryConsistencyAction(), "check_all_virtual_model_consistency", null, getController(), true);
		}

	}

	public class CheckViewPointLibraryConsistencyAction extends AbstractAction {
		public CheckViewPointLibraryConsistencyAction() {
			super();
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			logger.info("Check consistency for " + getController().getVirtualModelLibrary());
			getController().consistencyCheck(getController().getVirtualModelLibrary());
		}

	}

	// ==========================================================================
	// ======================= CheckProcessConsistency =========================
	// ==========================================================================

	public class CheckViewPointConsistencyItem extends FlexoMenuItem {

		public CheckViewPointConsistencyItem() {
			super(new CheckViewPointConsistencyAction(), "check_viewpoint_consistency", null, getController(), true);
		}

	}

	public class CheckViewPointConsistencyAction extends AbstractAction {
		public CheckViewPointConsistencyAction() {
			super();
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (getController().getCurrentVirtualModel() != null) {
				logger.info("Check consistency for " + getController().getCurrentVirtualModel());
				getController().consistencyCheck(getController().getCurrentVirtualModel());
			}
		}

	}

}
