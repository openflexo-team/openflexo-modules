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
package org.openflexo.fme.view.menu;

/*
 * Created on <date> by <yourname>
 *
 * Flexo Application Suite
 * (c) Denali 2003-2006
 */
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;

import org.openflexo.fme.controller.FMEController;
import org.openflexo.view.menu.FlexoMenuItem;
import org.openflexo.view.menu.ToolsMenu;

/**
 * 'Tools' menu for this Module
 * 
 * @author yourname
 */
public class FMEToolsMenu extends ToolsMenu {

	private static final Logger logger = Logger.getLogger(FMEToolsMenu.class.getPackage().getName());

	// ==========================================================================
	// ============================= Instance Variables
	// =========================
	// ==========================================================================

	public FlexoMenuItem checkViewPointLibraryConsistencyItem;
	public FlexoMenuItem checkViewPointConsistencyItem;

	protected FMEController fmeController;

	// ==========================================================================
	// ============================= Constructor
	// ================================
	// ==========================================================================

	public FMEToolsMenu(FMEController controller) {
		super(controller);
		fmeController = controller;
		// Put your actions here
	}

	public FMEController getFMEController() {
		return fmeController;
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
			super(new CheckViewPointLibraryConsistencyAction(), "check_all_viewpoint_consistency", null, getFMEController(), true);
		}

	}

	public class CheckViewPointLibraryConsistencyAction extends AbstractAction {
		public CheckViewPointLibraryConsistencyAction() {
			super();
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			logger.info("Check consistency for " + getFMEController().getViewPointLibrary());
			getFMEController().consistencyCheck(getFMEController().getViewPointLibrary());
		}

	}

	// ==========================================================================
	// ======================= CheckProcessConsistency =========================
	// ==========================================================================

	public class CheckViewPointConsistencyItem extends FlexoMenuItem {

		public CheckViewPointConsistencyItem() {
			super(new CheckViewPointConsistencyAction(), "check_viewpoint_consistency", null, getFMEController(), true);
		}

	}

	public class CheckViewPointConsistencyAction extends AbstractAction {
		public CheckViewPointConsistencyAction() {
			super();
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (getFMEController().getCurrentViewPoint() != null) {
				logger.info("Check consistency for " + getFMEController().getCurrentViewPoint());
				getFMEController().consistencyCheck(getFMEController().getCurrentViewPoint());
			}
		}

	}

}
