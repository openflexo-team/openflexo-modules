/**
 * 
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2011-2012, AgileBirds
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

package org.openflexo.ism;

import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.components.ProgressWindow;
import org.openflexo.fge.swing.JDianaInteractiveEditor;
import org.openflexo.fge.swing.view.JDrawingView;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.ism.controller.ISMController;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.module.FlexoModule;
import org.openflexo.view.controller.FlexoController;

/**
 * InformationSpace module
 * 
 * @author sylvain
 */
public class ISMModule extends FlexoModule<ISMModule> {
	private static final Logger logger = Logger.getLogger(ISMModule.class.getPackage().getName());

	public static final String ISM_MODULE_SHORT_NAME = "ISM";
	public static final String ISM_MODULE_NAME = "ism_module_name";

	private JDianaInteractiveEditor<?> screenshotController;
	private JDrawingView<?> screenshot = null;
	private boolean drawWorkingArea;
	private FlexoObject screenshotObject;

	public ISMModule(ApplicationContext applicationContext) throws Exception {
		super(applicationContext);
		ProgressWindow.setProgressInstance(FlexoLocalization.localizedForKey("build_editor"));
	}

	@Override
	protected FlexoController createControllerForModule() {
		return new ISMController(this);
	}

	@Override
	public InformationSpaceModule getModule() {
		return getApplicationContext().getModuleLoader().getModule(InformationSpaceModule.class);
	}

	public ISMController getISMController() {
		return (ISMController) getFlexoController();
	}

	@Override
	public ISMPreferences getPreferences() {
		return (ISMPreferences) super.getPreferences();
	}

	public float getScreenshotQuality() {
		float reply = getPreferences().getScreenshotQuality();
		if (reply > 1) {
			return 1f;
		}
		if (reply < 0.1f) {
			return 0.1f;
		}
		return reply;
	}

	public void finalizeScreenshotGeneration() {
		if (screenshot != null) {
			// screenshotObject.setIgnoreNotifications();
			screenshot.getDrawing().getRoot().setDrawWorkingArea(drawWorkingArea);
			// screenshotObject.resetIgnoreNotifications();
			screenshotController.delete();
			if (screenshot.getParent() != null) {
				screenshot.getParent().remove(screenshot);
			}
			screenshotController = null;
			screenshot = null;
		}
	}

	@Override
	public boolean close() {
		if (getApplicationContext().getResourceManager().getUnsavedResources().size() == 0) {
			return super.close();
		} else {
			if (getISMController().reviewModifiedResources()) {
				return super.close();
			} else {
				return false;
			}
		}
	}

}
