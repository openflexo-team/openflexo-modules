package org.openflexo.fme.controller.action;

import java.awt.Image;
import java.util.logging.Logger;

import org.openflexo.fme.model.action.CreateFreeModelDiagramFromPPT;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.technologyadapter.powerpoint.controller.ChoosePPTSlide;
import org.openflexo.view.controller.FlexoController;

public class CreateFreeModelDiagramFromPPTWizard extends AbstractCreateFreeModelDiagramWizard<CreateFreeModelDiagramFromPPT> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CreateFreeModelDiagramFromPPTWizard.class.getPackage().getName());

	private ChoosePPTSlide choosePPTSlide;

	public CreateFreeModelDiagramFromPPTWizard(CreateFreeModelDiagramFromPPT action, FlexoController controller) {
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
	protected void freeModelDiagramHasBeenDescribed() {
		super.freeModelDiagramHasBeenDescribed();
		choosePPTSlide.setDiagramName(getDescribeFreeModel().getDiagramName());
	}

	@Override
	public String getWizardTitle() {
		return FlexoLocalization.localizedForKey("create_free_model_diagram_based_on_a_powerpoint_slide");
	}

	@Override
	public Image getDefaultPageImage() {
		return IconFactory.getImageIcon(DiagramIconLibrary.DIAGRAM_BIG_ICON, IconLibrary.NEW_32_32).getImage();
	}

}
