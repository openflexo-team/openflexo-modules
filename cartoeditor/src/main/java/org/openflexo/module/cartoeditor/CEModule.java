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

import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.components.ProgressWindow;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.module.FlexoModule;
import org.openflexo.module.Module;
import org.openflexo.module.cartoeditor.controller.CEController;
import org.openflexo.view.controller.FlexoController;

/**
 * BMO Cartography Editor module
 *
 * @author eloubout
 */
public class CEModule extends FlexoModule<CEModule> {

	private static final Logger LOGGER = Logger.getLogger(CEModule.class.getPackage().getName());

	public static final String CE_MODULE_SHORT_NAME = "CE";
	public static final String CE_MODULE_NAME = "CartoEditor";

	public CEModule(ApplicationContext applicationContext) {
		super(applicationContext);
		ProgressWindow.setProgressInstance(FlexoLocalization.localizedForKey("build_editor"));
	}

	@Override
	public Module<CEModule> getModule() {
		return CartoEditor.INSTANCE;
	}

	/**
	 * Create a binded carto editor controller.
	 *
	 * @return a freshly created CEController.
	 */
	@Override
	protected FlexoController createControllerForModule() {
		return new CEController(this);
	}
}
