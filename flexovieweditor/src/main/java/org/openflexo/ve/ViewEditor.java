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

package org.openflexo.ve;

import org.openflexo.module.Module;

/**
 * ViewEditor module<br>
 * This class represents the static definition of the module<br>
 * You should never instantiate this class (never invoke constructor of this class): this will be done by the ModuleLoader during dynamic
 * service discovering
 * 
 * @author sylvain
 */
public class ViewEditor extends Module<VEModule> {

	public static ViewEditor INSTANCE;

	public ViewEditor() {
		super(VEModule.VE_MODULE_NAME, VEModule.VE_MODULE_SHORT_NAME, VEModule.class, VEPreferences.class,
				"modules/flexoviewpointmodeller", "10402", "ve", VEIconLibrary.VE_SMALL_ICON, VEIconLibrary.VE_MEDIUM_ICON,
				VEIconLibrary.VE_MEDIUM_ICON_WITH_HOVER, VEIconLibrary.VE_BIG_ICON);

		// WE set it now, because we are sure the ServiceManager did this call first
		// WE want to avoid twice defined objects
		INSTANCE = this;

	}

	@Override
	public String getHTMLDescription() {
		return "<html>The <b>ViewEditor</b> offers domain-specific modeling features for business users & engineers."
				+ "This module allows you to create and edit models (Views) based on ViewPoints built with  <b>ViewPointModeller</b></html>";
	}

}
