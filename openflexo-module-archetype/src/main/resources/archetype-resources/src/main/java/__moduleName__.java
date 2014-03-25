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
package org.openflexo.${moduleTrigram}ditor;

import javax.swing.ImageIcon;

import org.openflexo.module.Module;
import org.openflexo.prefs.ModulePreferences;

public class ${moduleName} extends Module<${moduleName}Module> {

	public static Module<${moduleTrigram}Module> INSTANCE = null;

	public ${moduleName}() {
		
		// TODO: document all the parameters
		
		super(${moduleTrigram}Module.${moduleTrigram}_MODULE_NAME, ${moduleTrigram}Module.${moduleTrigram}_MODULE_NAME, ${moduleTrigram}Module.class, ${moduleTrigram}Preferences.class, "",
				null , "${moduleTrigram}", ${moduleTrigram}IconLibrary.${moduleTrigram}_SMALL_ICON, ${moduleTrigram}IconLibrary.${moduleTrigram}_MEDIUM_ICON, ${moduleTrigram}IconLibrary.${moduleTrigram}_MEDIUM_ICON_HOVER,
				${moduleTrigram}IconLibrary.${moduleTrigram}_BIG_ICON, false);

		INSTANCE = this;
	}


}


