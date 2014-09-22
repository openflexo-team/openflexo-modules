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

package org.openflexo.module.cartoeditor;

import org.openflexo.module.Module;
import org.openflexo.module.cartoeditor.view.CEIconLibrary;

public class CartoEditor extends Module<CEModule> {

	public static final Module<CEModule> INSTANCE = new CartoEditor();

	public CartoEditor() {
		super(CEModule.CE_MODULE_NAME, CEModule.CE_MODULE_NAME, CEModule.class, CEPreferences.class, "",
				null, "CE", CEIconLibrary.CE_SMALL_ICON, CEIconLibrary.CE_MEDIUM_ICON, CEIconLibrary.CE_MEDIUM_ICON,
				CEIconLibrary.CE_MEDIUM_ICON);
	}

}


