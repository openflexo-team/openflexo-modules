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
package org.openflexo.vpm;

import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.components.ProgressWindow;
import org.openflexo.fge.swing.JDianaInteractiveEditor;
import org.openflexo.fge.swing.view.JDrawingView;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.module.FlexoModule;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.vpm.controller.VPMController;

/**
 * ViewPointModeller module
 * 
 * @author sylvain
 */
public class VPMModule extends FlexoModule<VPMModule> {

	private static final Logger logger = Logger.getLogger(VPMModule.class.getPackage().getName());

	public static final String VPM_MODULE_SHORT_NAME = "VPM";
	public static final String VPM_MODULE_NAME = "vpm_module_name";

	private JDianaInteractiveEditor<?> screenshotController;
	private JDrawingView<?> screenshot = null;
	private boolean drawWorkingArea;
	private FlexoObject screenshotObject;

	public VPMModule(ApplicationContext applicationContext) throws Exception {
		super(applicationContext);
		// UGLIEST HACK EVER TO BE REMOVED ASAP:
		// DiagramPalette.setModuleLoader(applicationContext.getModuleLoader());
		// Hack removed : guillaume, what about that ?
		// VPMPreferences.init();
		ProgressWindow.setProgressInstance(FlexoLocalization.localizedForKey("build_editor"));

	}

	@Override
	public void initModule() {
		super.initModule();
		// Put here a code to display default view
		getVPMController().setCurrentEditedObjectAsModuleView(getVPMController().getViewPointLibrary());
	}

	@Override
	protected FlexoController createControllerForModule() {
		return new VPMController(this);
	}

	@Override
	public ViewPointModeller getModule() {
		return ViewPointModeller.INSTANCE;
	}

	public VPMController getVPMController() {
		return (VPMController) getFlexoController();
	}

	@Override
	public boolean close() {
		if (getApplicationContext().getResourceManager().getUnsavedResources().size() == 0) {
			return super.close();
		} else {
			if (getVPMController().reviewModifiedResources()) {
				return super.close();
			} else {
				return false;
			}
		}
	}

	@Override
	public VPMPreferences getPreferences() {
		return (VPMPreferences) super.getPreferences();
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
	public JComponent createScreenshotForExampleDiagram(ExampleDiagram target) {
		if (target == null) {
			if (logger.isLoggable(Level.SEVERE)) {
				logger.severe("Cannot create screenshot for null target!");
			}
			return null;
		}

		logger.info("createScreenshotForShema() " + target);

		screenshotObject = target;

		// prevent example diagram to be marked as modified during screenshot generation
		target.setIgnoreNotifications();
		screenshotController = new ExampleDiagramEditor(getVPMController(), target, true);

		screenshot = screenshotController.getDrawingView();
		drawWorkingArea = screenshot.getDrawing().getRoot().getDrawWorkingArea();
		screenshot.getDrawing().getRoot().setDrawWorkingArea(false);
		screenshot.getPaintManager().disablePaintingCache();
		screenshot.validate();
		Dimension d = screenshot.getComputedMinimumSize();
		d.height += 20;
		d.width += 20;
		screenshot.setSize(d);
		screenshot.setPreferredSize(d);
		target.resetIgnoreNotifications();

		return screenshot;
	}

	@Override
	public JComponent createScreenshotForDiagramPalette(DiagramPalette target) {
		if (target == null) {
			if (logger.isLoggable(Level.SEVERE)) {
				logger.severe("Cannot create screenshot for null target!");
			}
			return null;
		}

		logger.info("createScreenshotForPalette() " + target);

		screenshotObject = target;

		// prevent process to be marked as modified during screenshot generation
		target.setIgnoreNotifications();
		screenshotController = new DiagramPaletteEditor(getVPMController(), target, true);

		screenshot = screenshotController.getDrawingView();
		drawWorkingArea = screenshot.getDrawing().getRoot().getDrawWorkingArea();
		screenshot.getDrawing().getRoot().setDrawWorkingArea(false);
		screenshot.getPaintManager().disablePaintingCache();
		screenshot.validate();
		Dimension d = new Dimension((int) screenshot.getDrawing().getRoot().getWidth(), (int) screenshot.getDrawing().getRoot().getHeight());
		screenshot.setSize(d);
		screenshot.setPreferredSize(d);
		target.resetIgnoreNotifications();

		return screenshot;
	}*/

	public void finalizeScreenshotGeneration() {
		logger.info("finalizeScreenshotGeneration()");

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
