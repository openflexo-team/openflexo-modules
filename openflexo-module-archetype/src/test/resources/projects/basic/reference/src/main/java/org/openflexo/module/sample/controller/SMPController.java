/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Openflexo-module-archetype, a component of the software infrastructure 
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


package org.openflexo.module.sample.controller;

import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.module.FlexoModule;
import org.openflexo.selection.MouseSelectionManager;
import org.openflexo.view.FlexoMainPane;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.menu.FlexoMenuBar;
import org.openflexo.module.sample.SMPModule;
import org.openflexo.module.sample.controller.SMPSelectionManager;
import org.openflexo.module.sample.view.menu.SMPMenuBar;

public class SMPController extends FlexoController {

	public SMPController(FlexoModule module) {
		super(module);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initializePerspectives() {
		// TODO Auto-generated method stub

	}

	@Override
	protected MouseSelectionManager createSelectionManager() {
		return new SMPSelectionManager(this);
	}

	@Override
	protected FlexoMenuBar createNewMenuBar() {
		return new SMPMenuBar(this);
	}

	@Override
	public FlexoObject getDefaultObjectToSelect(FlexoProject project) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected FlexoMainPane createMainPane() {
		// TODO Auto-generated method stub
		return new FlexoMainPane(this);
	}

}
