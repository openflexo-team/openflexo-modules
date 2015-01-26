/**
 * 
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2011-2012, AgileBirds
 * 
 * This file is part of Flexoviewpointmodeler, a component of the software infrastructure 
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

package org.openflexo.vpm.view.menu;

/*
 * Created on <date> by <yourname>
 *
 * Flexo Application Suite
 * (c) Denali 2003-2006
 */
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;

import org.openflexo.view.menu.FlexoMenuItem;
import org.openflexo.view.menu.ToolsMenu;
import org.openflexo.vpm.controller.VPMController;

/**
 * 'Tools' menu for this Module
 * 
 * @author yourname
 */
public class VPMToolsMenu extends ToolsMenu {

	private static final Logger logger = Logger.getLogger(VPMToolsMenu.class.getPackage().getName());

	// ==========================================================================
	// ============================= Instance Variables
	// =========================
	// ==========================================================================

	public FlexoMenuItem checkViewPointLibraryConsistencyItem;
	public FlexoMenuItem checkViewPointConsistencyItem;

	// ==========================================================================
	// ============================= Constructor
	// ================================
	// ==========================================================================

	public VPMToolsMenu(VPMController controller) {
		super(controller);
		// Put your actions here
	}

	public VPMController getVPMController() {
		return (VPMController) getController();
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
			super(new CheckViewPointLibraryConsistencyAction(), "check_all_viewpoint_consistency", null, getVPMController(), true);
		}

	}

	public class CheckViewPointLibraryConsistencyAction extends AbstractAction {
		public CheckViewPointLibraryConsistencyAction() {
			super();
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			logger.info("Check consistency for " + getVPMController().getViewPointLibrary());
			getVPMController().consistencyCheck(getVPMController().getViewPointLibrary());
		}

	}

	// ==========================================================================
	// ======================= CheckProcessConsistency =========================
	// ==========================================================================

	public class CheckViewPointConsistencyItem extends FlexoMenuItem {

		public CheckViewPointConsistencyItem() {
			super(new CheckViewPointConsistencyAction(), "check_viewpoint_consistency", null, getVPMController(), true);
		}

	}

	public class CheckViewPointConsistencyAction extends AbstractAction {
		public CheckViewPointConsistencyAction() {
			super();
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (getVPMController().getCurrentViewPoint() != null) {
				logger.info("Check consistency for " + getVPMController().getCurrentViewPoint());
				getVPMController().consistencyCheck(getVPMController().getCurrentViewPoint());
			}
		}

	}

}
