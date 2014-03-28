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


import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.module.FlexoModule;
import org.openflexo.module.Module;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.module.sample.controller.SMPController;

/**
 * DbEditor module
 * 
 * @author Someone
 */
public class SMPModule extends FlexoModule<SMPModule> {

	private static final Logger logger = Logger.getLogger(SMPModule.class.getPackage().getName());

	public static final String SMP_MODULE_SHORT_NAME = "SMP";
	public static final String SMP_MODULE_NAME = "A Sample Module";

	public SMPModule(ApplicationContext applicationContext) {
		super(applicationContext);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Module<SMPModule> getModule() {
		// TODO Auto-generated method stub
		return sample.INSTANCE;
	}

	@Override
	protected FlexoController createControllerForModule() {
		// TODO Auto-generated method stub
		return new SMPController(this);
	}
}
