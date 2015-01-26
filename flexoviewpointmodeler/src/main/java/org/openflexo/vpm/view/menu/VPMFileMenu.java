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

import java.util.logging.Logger;

import org.openflexo.view.menu.FileMenu;
import org.openflexo.vpm.controller.VPMController;

/**
 * 'File' menu for VPM Module
 * 
 * @author sylvain
 */
public class VPMFileMenu extends FileMenu {

	private static final Logger logger = Logger.getLogger(VPMFileMenu.class.getPackage().getName());

	public VPMFileMenu(VPMController controller) {
		super(controller);
	}

	public VPMController getCEDController() {
		return (VPMController) getController();
	}

	@Override
	public void addSpecificItems() {
		// add(new SaveModifiedItem());
		// addSeparator();
	}

	@Override
	public void quit() {
		if (getCEDController().getApplicationContext().getResourceManager().getUnsavedResources().size() == 0) {
			super.quit();
		} else if (getCEDController().reviewModifiedResources()) {
			super.quit();
		}

	}

	// Already provided by super class
	/*public class SaveModifiedItem extends FlexoMenuItem {
		public SaveModifiedItem() {
			super(new SaveModifiedAction(), "save", KeyStroke.getKeyStroke(KeyEvent.VK_S, FlexoCst.META_MASK), getController(), true);
			setIcon(IconLibrary.SAVE_ICON);
		}
	}

	public class SaveModifiedAction extends AbstractAction {
		public SaveModifiedAction() {
			super();
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			getCEDController().reviewModifiedResources();
		}
	}*/

}
