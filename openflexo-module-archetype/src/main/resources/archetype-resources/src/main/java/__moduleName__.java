/*
 * (c) Copyright 2014- Openflexo
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

package org.openflexo.${moduleTrigram}ditor;

import javax.swing.ImageIcon;

import org.openflexo.module.Module;
import org.openflexo.prefs.ModulePreferences;

public class ${moduleName} extends Module<${moduleName}Module> {

	public static Module<${moduleTrigram}Module> INSTANCE = null;

	public ${moduleName}() {
			
		super(${moduleTrigram}Module.${moduleTrigram}_MODULE_NAME, ${moduleTrigram}Module.${moduleTrigram}_MODULE_NAME, ${moduleTrigram}Module.class, ${moduleTrigram}Preferences.class, "",
				null , "${moduleTrigram}", ${moduleTrigram}IconLibrary.${moduleTrigram}_SMALL_ICON, ${moduleTrigram}IconLibrary.${moduleTrigram}_MEDIUM_ICON, ${moduleTrigram}IconLibrary.${moduleTrigram}_MEDIUM_ICON_HOVER,
				${moduleTrigram}IconLibrary.${moduleTrigram}_BIG_ICON, false);

		INSTANCE = this;
	}


}


