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

package org.openflexo.module.sample;

import org.openflexo.module.Module;
import org.openflexo.module.sample.view.SMPIconLibrary;

public class sample extends Module<SMPModule> {

	public static Module<SMPModule> INSTANCE = null;

	public sample() {
			
		super(SMPModule.SMP_MODULE_NAME, SMPModule.SMP_MODULE_NAME, SMPModule.class, SMPPreferences.class, "",
				null , "SMP", SMPIconLibrary.SMP_SMALL_ICON, SMPIconLibrary.SMP_MEDIUM_ICON, SMPIconLibrary.SMP_MEDIUM_ICON_HOVER,
				SMPIconLibrary.SMP_BIG_ICON, false);

		INSTANCE = this;
	}


}


