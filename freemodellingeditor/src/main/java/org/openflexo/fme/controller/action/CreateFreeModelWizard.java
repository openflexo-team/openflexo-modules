package org.openflexo.fme.controller.action;

import java.awt.Image;
import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.components.wizard.FlexoWizard;
import org.openflexo.components.wizard.WizardStep;
import org.openflexo.fme.FMEIconLibrary;
import org.openflexo.fme.model.FreeModellingProject;
import org.openflexo.fme.model.action.CreateFreeModel;
import org.openflexo.foundation.view.VirtualModelInstance;
import org.openflexo.foundation.viewpoint.VirtualModel;
import org.openflexo.foundation.viewpoint.annotations.FIBPanel;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.toolbox.StringUtils;
import org.openflexo.view.controller.FlexoController;

public class CreateFreeModelWizard extends FlexoWizard {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CreateFreeModelWizard.class.getPackage().getName());

	private final CreateFreeModel action;

	private final DescribeFreeModel describeFreeModel;

	public CreateFreeModelWizard(CreateFreeModel action, FlexoController controller) {
		super(controller);
		this.action = action;
		addStep(describeFreeModel = new DescribeFreeModel());
	}

	@Override
	public String getWizardTitle() {
		return FlexoLocalization.localizedForKey("create_free_model");
	}

	@Override
	public Image getDefaultPageImage() {
		return IconFactory.getImageIcon(FMEIconLibrary.FREE_MODEL_BIG_ICON, IconLibrary.NEW_32_32).getImage();
	}

	public CreateFreeModel getAction() {
		return action;
	}

	public DescribeFreeModel getDescribeFreeModel() {
		return describeFreeModel;
	}

	/**
	 * This step is used to set {@link VirtualModel} to be used, as well as name and title of the {@link VirtualModelInstance}
	 * 
	 * @author sylvain
	 *
	 */
	@FIBPanel("Fib/Wizard/DescribeFreeModel.fib")
	public class DescribeFreeModel extends WizardStep {

		public ApplicationContext getServiceManager() {
			return getController().getApplicationContext();
		}

		public CreateFreeModel getAction() {
			return CreateFreeModelWizard.this.getAction();
		}

		@Override
		public String getTitle() {
			return FlexoLocalization.localizedForKey("describe_free_model");
		}

		public FreeModellingProject getFreeModellingProject() {
			return getAction().getFocusedObject();
		}

		@Override
		public boolean isValid() {

			if (StringUtils.isEmpty(getFreeModelName())) {
				setIssueMessage(FlexoLocalization.localizedForKey("no_free_model_name_defined"), IssueMessageType.ERROR);
				return false;
			}

			else if (getFreeModellingProject().getFreeModel(getFreeModelName()) != null) {
				setIssueMessage(FlexoLocalization.localizedForKey("a_free_model_with_that_name_already_exists"), IssueMessageType.ERROR);
				return false;
			}

			else if (!getAction().getCreateNewMetaModel() && getAction().getFreeMetaModel() == null) {
				setIssueMessage(FlexoLocalization.localizedForKey("no_meta_model_defined"), IssueMessageType.ERROR);
				return false;
			}

			else if (StringUtils.isEmpty(getFreeModelDescription())) {
				setIssueMessage(FlexoLocalization.localizedForKey("it_is_recommanded_to_describe_free_model"), IssueMessageType.WARNING);
			}

			return true;
		}

		public String getFreeModelName() {
			return getAction().getFreeModelName();
		}

		public void setFreeModelName(String newFreeModelName) {
			if (!newFreeModelName.equals(getFreeModelName())) {
				String oldValue = getFreeModelName();
				getAction().setFreeModelName(newFreeModelName);
				getPropertyChangeSupport().firePropertyChange("newFreeModelName", oldValue, newFreeModelName);
				checkValidity();
			}
		}

		public String getFreeModelDescription() {
			return getAction().getFreeModelDescription();
		}

		public void setFreeModelDescription(String newFreeModelDescription) {
			if (!newFreeModelDescription.equals(getFreeModelDescription())) {
				String oldValue = getFreeModelDescription();
				getAction().setFreeModelDescription(newFreeModelDescription);
				getPropertyChangeSupport().firePropertyChange("newFreeModelDescription", oldValue, newFreeModelDescription);
				checkValidity();
			}
		}

	}

}
