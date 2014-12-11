package org.openflexo.fme.controller.action;

import java.awt.Image;
import java.util.logging.Logger;

import org.openflexo.fme.FMEIconLibrary;
import org.openflexo.fme.model.action.CreateFreeModel;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.view.controller.FlexoController;

public class CreateFreeModelWizard extends AbstractCreateFreeModelWizard<CreateFreeModel> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CreateFreeModelWizard.class.getPackage().getName());

	public CreateFreeModelWizard(CreateFreeModel action, FlexoController controller) {
		super(action, controller);
	}

	@Override
	public String getWizardTitle() {
		return FlexoLocalization.localizedForKey("create_free_model");
	}

	@Override
	public Image getDefaultPageImage() {
		return IconFactory.getImageIcon(FMEIconLibrary.FREE_MODEL_BIG_ICON, IconLibrary.NEW_32_32).getImage();
	}

}
