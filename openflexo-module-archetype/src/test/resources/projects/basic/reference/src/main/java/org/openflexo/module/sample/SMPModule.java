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
package org.openflexo.DBEditor;


import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.module.FlexoModule;
import org.openflexo.module.Module;
import org.openflexo.view.controller.FlexoController;

/**
 * DbEditor module
 * 
 * @author christophe
 */
public class DBEModule extends FlexoModule<DBEModule> {

	private static final Logger logger = Logger.getLogger(DBEModule.class.getPackage().getName());

	public static final String DBE_MODULE_SHORT_NAME = "DBE";
	public static final String DBE_MODULE_NAME = "database_editor";

	public DBEModule(ApplicationContext applicationContext) {
		super(applicationContext);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Module<DBEModule> getModule() {
		// TODO Auto-generated method stub
		return DBEditor.INSTANCE;
	}

	@Override
	protected FlexoController createControllerForModule() {
		// TODO Auto-generated method stub
		return new DBEController(this);
	}
}
