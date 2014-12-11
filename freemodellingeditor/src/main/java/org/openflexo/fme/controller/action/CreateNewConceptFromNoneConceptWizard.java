package org.openflexo.fme.controller.action;

import java.awt.Image;
import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.components.wizard.FlexoWizard;
import org.openflexo.components.wizard.WizardStep;
import org.openflexo.fme.model.action.CreateNewConceptFromNoneConcept;
import org.openflexo.foundation.view.FlexoConceptInstance;
import org.openflexo.foundation.view.VirtualModelInstance;
import org.openflexo.foundation.viewpoint.VirtualModel;
import org.openflexo.foundation.viewpoint.annotations.FIBPanel;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.icon.VPMIconLibrary;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.toolbox.StringUtils;
import org.openflexo.view.controller.FlexoController;

public class CreateNewConceptFromNoneConceptWizard extends FlexoWizard {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CreateNewConceptFromNoneConceptWizard.class.getPackage().getName());

	private final CreateNewConceptFromNoneConcept action;

	private final ConfigureNewConceptFromNoneConcept configureNewConceptFromNoneConcept;

	public CreateNewConceptFromNoneConceptWizard(CreateNewConceptFromNoneConcept action, FlexoController controller) {
		super(controller);
		this.action = action;
		addStep(configureNewConceptFromNoneConcept = new ConfigureNewConceptFromNoneConcept());
	}

	@Override
	public String getWizardTitle() {
		return FlexoLocalization.localizedForKey("create_new_concept");
	}

	@Override
	public Image getDefaultPageImage() {
		return IconFactory.getImageIcon(VPMIconLibrary.FLEXO_CONCEPT_BIG_ICON, IconLibrary.NEW_32_32).getImage();
	}

	public ConfigureNewConceptFromNoneConcept getConfigureNewConceptFromNoneConcept() {
		return configureNewConceptFromNoneConcept;
	}

	/**
	 * This step is used to set {@link VirtualModel} to be used, as well as name and title of the {@link VirtualModelInstance}
	 * 
	 * @author sylvain
	 *
	 */
	@FIBPanel("Fib/Wizard/ConfigureNewConceptFromNoneConcept.fib")
	public class ConfigureNewConceptFromNoneConcept extends WizardStep {

		public ApplicationContext getServiceManager() {
			return getController().getApplicationContext();
		}

		public CreateNewConceptFromNoneConcept getAction() {
			return action;
		}

		public FlexoConceptInstance getFlexoConceptInstance() {
			return getAction().getFocusedObject();
		}

		@Override
		public String getTitle() {
			return FlexoLocalization.localizedForKey("configure_new_concept");
		}

		@Override
		public boolean isValid() {

			if (StringUtils.isEmpty(getNewConceptName())) {
				setIssueMessage(FlexoLocalization.localizedForKey("no_concept_name_defined"), IssueMessageType.ERROR);
				return false;
			}

			if (getFlexoConceptInstance().getVirtualModelInstance().getVirtualModel().getFlexoConcept(getNewConceptName()) != null) {
				setIssueMessage(FlexoLocalization.localizedForKey("a_concept_with_that_name_already_exists"), IssueMessageType.ERROR);
				return false;
			}

			return true;

		}

		public String getNewConceptName() {
			return action.getNewConceptName();
		}

		public void setNewConceptName(String newConceptName) {
			if (!newConceptName.equals(getNewConceptName())) {
				String oldValue = getNewConceptName();
				action.setNewConceptName(newConceptName);
				getPropertyChangeSupport().firePropertyChange("newConceptName", oldValue, newConceptName);
				checkValidity();
			}
		}

		public String getNewConceptDescription() {
			return action.getNewConceptDescription();
		}

		public void setNewConceptDescription(String newConceptDescription) {
			if (!newConceptDescription.equals(getNewConceptDescription())) {
				String oldValue = getNewConceptDescription();
				action.setNewConceptDescription(newConceptDescription);
				getPropertyChangeSupport().firePropertyChange("newConceptDescription", oldValue, newConceptDescription);
				checkValidity();
			}
		}

	}

}
