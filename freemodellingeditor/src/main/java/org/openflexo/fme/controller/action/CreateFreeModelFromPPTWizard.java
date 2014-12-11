package org.openflexo.fme.controller.action;

import java.awt.Image;
import java.util.logging.Logger;

import org.openflexo.fme.FMEIconLibrary;
import org.openflexo.fme.model.action.CreateFreeModelFromPPT;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.technologyadapter.powerpoint.controller.ChoosePPTSlide;
import org.openflexo.view.controller.FlexoController;

public class CreateFreeModelFromPPTWizard extends AbstractCreateFreeModelWizard<CreateFreeModelFromPPT> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CreateFreeModelFromPPTWizard.class.getPackage().getName());

	private ChoosePPTSlide choosePPTSlide;

	public CreateFreeModelFromPPTWizard(CreateFreeModelFromPPT action, FlexoController controller) {
		super(action, controller);
		addStep(choosePPTSlide = new ChoosePPTSlide(controller) {
			@Override
			public void done() {
				super.done();
				getAction().setDiagramName(getDiagramName());
				getAction().setDiagramTitle(getDiagramTitle());
				getAction().setFile(getFile());
				getAction().setSlide(getSlide());
			}
		});
	}

	@Override
	public String getWizardTitle() {
		return FlexoLocalization.localizedForKey("create_free_model_from_powerpoint_file");
	}

	@Override
	public Image getDefaultPageImage() {
		return IconFactory.getImageIcon(FMEIconLibrary.FREE_MODEL_BIG_ICON, IconLibrary.NEW_32_32).getImage();
	}

	public ChoosePPTSlide getChoosePPTSlide() {
		return choosePPTSlide;
	}

}
