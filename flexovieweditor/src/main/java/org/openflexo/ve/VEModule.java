/*
 * (c) Copyright 2010-2011 AgileBirds
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
package org.openflexo.ve;

import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.components.ProgressWindow;
import org.openflexo.fge.swing.JDianaInteractiveEditor;
import org.openflexo.fge.swing.view.JDrawingView;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.module.FlexoModule;
import org.openflexo.ve.controller.VEController;
import org.openflexo.view.controller.FlexoController;

/**
 * View Editor module
 * 
 * @author sylvain
 */
public class VEModule extends FlexoModule<VEModule> {
	private static final Logger logger = Logger.getLogger(VEModule.class.getPackage().getName());

	public static final String VE_MODULE_SHORT_NAME = "VE";
	public static final String VE_MODULE_NAME = "ve_module_name";

	private JDianaInteractiveEditor<?> screenshotController;
	private JDrawingView<?> screenshot = null;
	private boolean drawWorkingArea;
	private FlexoObject screenshotObject;

	public VEModule(ApplicationContext applicationContext) throws Exception {
		super(applicationContext);
		ProgressWindow.setProgressInstance(FlexoLocalization.localizedForKey("build_editor"));
	}

	@Override
	protected FlexoController createControllerForModule() {
		return new VEController(this);
	}

	@Override
	public ViewEditor getModule() {
		return getApplicationContext().getModuleLoader().getModule(ViewEditor.class);
	}

	public VEController getVEController() {
		return (VEController) getFlexoController();
	}

	@Override
	public VEPreferences getPreferences() {
		return (VEPreferences) super.getPreferences();
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

	/*@Override
	public JComponent createScreenshotForDiagram(DiagramResource diagramResource) {
		Diagram target = diagramResource.getDiagram();
		if (target == null) {
			if (logger.isLoggable(Level.SEVERE)) {
				logger.severe("Cannot create screenshot for null target!");
			}
			return null;
		}

		screenshotObject = target;

		// prevent process to be marked as modified during screenshot generation
		target.setIgnoreNotifications();
		screenshotController = new DiagramController(getVEController(), target, true);

		screenshot = screenshotController.getDrawingView();
		drawWorkingArea = screenshot.getDrawingGraphicalRepresentation().getDrawWorkingArea();
		screenshot.getDrawingGraphicalRepresentation().setDrawWorkingArea(false);
		screenshot.getPaintManager().disablePaintingCache();
		screenshot.validate();
		Dimension d = screenshot.getComputedMinimumSize();
		d.height += 20;
		d.width += 20;
		screenshot.setSize(d);
		screenshot.setPreferredSize(d);
		target.resetIgnoreNotifications();

		return screenshot;
	}*/

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

}
