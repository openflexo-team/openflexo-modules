package org.openflexo.fme.controller.action;

import java.awt.Image;
import java.util.logging.Logger;

import org.openflexo.fme.model.action.CreateFreeModelDiagram;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.view.controller.FlexoController;

public class CreateFreeModelDiagramWizard extends AbstractCreateFreeModelDiagramWizard<CreateFreeModelDiagram> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CreateFreeModelDiagramWizard.class.getPackage().getName());

	public CreateFreeModelDiagramWizard(CreateFreeModelDiagram action, FlexoController controller) {
		super(action, controller);
	}

	@Override
	public String getWizardTitle() {
		return FlexoLocalization.localizedForKey("create_free_model_diagram");
	}

	@Override
	public Image getDefaultPageImage() {
		return IconFactory.getImageIcon(DiagramIconLibrary.DIAGRAM_BIG_ICON, IconLibrary.NEW_32_32).getImage();
	}

}
