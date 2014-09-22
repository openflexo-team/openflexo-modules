package org.openflexo.module.cartoeditor.controller.action;

import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;

/**
 * Created by eloubout on 09/09/14.
 */
public class CEControllerActionInitializer extends ControllerActionInitializer {

	public CEControllerActionInitializer(FlexoController controller){
		super(controller);
	}

	@Override
	public void initializeActions() {
		super.initializeActions();

		new ConvertToCEProjectInitializer(this);
	}
}
