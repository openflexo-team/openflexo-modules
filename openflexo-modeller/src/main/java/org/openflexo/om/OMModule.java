/**
 * 
 * Copyright (c) 2013-2017, Openflexo
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

package org.openflexo.om;

import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.diana.swing.JDianaInteractiveEditor;
import org.openflexo.diana.swing.view.JDrawingView;
import org.openflexo.foundation.fml.FMLTechnologyAdapter;
import org.openflexo.foundation.fml.rt.FMLRTTechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterService;
import org.openflexo.module.FlexoModule;
import org.openflexo.om.controller.OMController;
import org.openflexo.view.controller.FlexoController;

/**
 * Openflexo Modeller module
 * 
 * @author sylvain
 */
public class OMModule extends FlexoModule<OMModule> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(OMModule.class.getPackage().getName());

	public static final String OM_MODULE_SHORT_NAME = "OM";
	public static final String OM_MODULE_NAME = "Openflexo Modeller"; // Not localized

	private JDianaInteractiveEditor<?> screenshotController;
	private JDrawingView<?> screenshot = null;
	private boolean drawWorkingArea;
	// private FlexoObject screenshotObject;

	public OMModule(ApplicationContext applicationContext) throws Exception {
		super(applicationContext);
	}

	@Override
	public String getLocalizationDirectory() {
		return "FlexoLocalization/OpenflexoModeller";
	}

	@Override
	public void initModule() {
		super.initModule();
		TechnologyAdapterService taService = getApplicationContext().getTechnologyAdapterService();
		taService.activateTechnologyAdapter(taService.getTechnologyAdapter(FMLTechnologyAdapter.class), true);
		taService.activateTechnologyAdapter(taService.getTechnologyAdapter(FMLRTTechnologyAdapter.class), true);

		/*FMLTechnologyAdapterController fmlTAC = getApplicationContext().getTechnologyAdapterControllerService()
				.getTechnologyAdapterController(FMLTechnologyAdapterController.class);
		fmlTAC.initializeAdvancedActions(getFlexoController().getControllerActionInitializer());*/
	}

	/**
	 * Hooks used to handle the fact that a module should activate or not advanced actions of a {@link TechnologyAdapter}<br>
	 * Overrides when required. Default behaviour returns null.
	 * 
	 * @param technologyAdapter
	 * @return
	 */
	@Override
	public boolean activateAdvancedActions(TechnologyAdapter<?> technologyAdapter) {
		return true;
	}

	@Override
	protected FlexoController createControllerForModule() {
		return new OMController(this);
	}

	@Override
	public OpenflexoModeller getModule() {
		return getApplicationContext().getModuleLoader().getModule(OpenflexoModeller.class);
	}

	public OMController getOMController() {
		return getFlexoController();
	}

	@Override
	public OMController getFlexoController() {
		return (OMController) super.getFlexoController();
	}

	@Override
	public OMPreferences getPreferences() {
		return (OMPreferences) super.getPreferences();
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

	@Override
	public boolean close() {
		if (getApplicationContext().getResourceManager().getUnsavedResources().size() == 0) {
			return super.close();
		}
		else {
			if (getOMController().reviewModifiedResources()) {
				return super.close();
			}
			else {
				return false;
			}
		}
	}

}
