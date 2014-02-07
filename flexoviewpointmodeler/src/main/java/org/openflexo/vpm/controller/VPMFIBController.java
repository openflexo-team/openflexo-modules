package org.openflexo.vpm.controller;

import java.util.logging.Logger;

import org.openflexo.fib.model.FIBComponent;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.view.controller.ViewPointEditingFIBController;

/**
 * Represents the controller used in VPM (ViewPointModeller)<br>
 * Extends ViewPointEditingFIBController by supporting features relative to VPM module
 * 
 * 
 * @author sylvain
 */
public class VPMFIBController extends ViewPointEditingFIBController {

	protected static final Logger logger = FlexoLogger.getLogger(VPMFIBController.class.getPackage().getName());

	public VPMFIBController(FIBComponent component) {
		super(component);
	}

	public VPMFIBController(FIBComponent component, VPMController controller) {
		super(component, controller);
	}

	@Override
	public VPMController getFlexoController() {
		return (VPMController) super.getFlexoController();
	}

}
