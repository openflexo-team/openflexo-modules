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

package org.openflexo.xxxmodule.controller;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.gina.model.FIBComponent;
import org.openflexo.gina.swing.view.SwingViewFactory;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.view.controller.FlexoFIBController;
import org.openflexo.xxxmodule.model.XXXProjectNature;

/**
 * Represents the controller of a FIBComponent in XXX prototype
 * 
 * 
 * @author yourname
 */
public class XXXFIBController extends FlexoFIBController {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(XXXFIBController.class.getPackage().getName());

	public XXXFIBController(FIBComponent component) {
		super(component, SwingViewFactory.INSTANCE);
		// Default parent localizer is the main localizer
		setParentLocalizer(FlexoLocalization.getMainLocalizer());
	}

	@Override
	public XXXController getFlexoController() {
		return (XXXController) super.getFlexoController();
	}

	public XXXProjectNature getXXXNature() {
		if (getFlexoController() != null) {
			return getFlexoController().getXXXNature();
		}
		return null;
	}

	@Override
	public ImageIcon retrieveIconForObject(Object object) {
		if (getFlexoController() != null) {
			return getFlexoController().iconForObject(object);
		}
		return super.retrieveIconForObject(object);
	}

}
