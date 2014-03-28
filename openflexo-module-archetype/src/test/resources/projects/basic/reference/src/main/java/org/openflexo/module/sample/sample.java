/*******************************************************************************
 * Copyright (c) 2014 xtof.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     xtof - initial API and implementation
 ******************************************************************************/
package org.openflexo.SMPditor;

import javax.swing.ImageIcon;

import org.openflexo.module.Module;
import org.openflexo.prefs.ModulePreferences;

public class sample extends Module<sampleModule> {

	public static Module<SMPModule> INSTANCE = null;

	public sample() {
		
		// TODO: document all the parameters
		
		super(SMPModule.SMP_MODULE_NAME, SMPModule.SMP_MODULE_NAME, SMPModule.class, SMPPreferences.class, "",
				null , "SMP", SMPIconLibrary.SMP_SMALL_ICON, SMPIconLibrary.SMP_MEDIUM_ICON, SMPIconLibrary.SMP_MEDIUM_ICON_HOVER,
				SMPIconLibrary.SMP_BIG_ICON, false);

		INSTANCE = this;
	}


}


