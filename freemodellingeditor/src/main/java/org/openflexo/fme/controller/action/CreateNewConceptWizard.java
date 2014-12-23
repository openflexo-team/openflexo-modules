package org.openflexo.fme.controller.action;

import java.awt.Image;
import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.components.wizard.FlexoWizard;
import org.openflexo.components.wizard.WizardStep;
import org.openflexo.fme.model.FreeMetaModel;
import org.openflexo.fme.model.action.CreateNewConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.annotations.FIBPanel;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.icon.FMLIconLibrary;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.toolbox.StringUtils;
import org.openflexo.view.controller.FlexoController;

public class CreateNewConceptWizard extends FlexoWizard {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CreateNewConceptWizard.class.getPackage().getName());

	private final CreateNewConcept action;

	private final ConfigureNewConcept configureNewConcept;

	public CreateNewConceptWizard(CreateNewConcept action, FlexoController controller) {
		super(controller);
		this.action = action;
		addStep(configureNewConcept = new ConfigureNewConcept());
	}

	@Override
	public String getWizardTitle() {
		return FlexoLocalization.localizedForKey("create_new_concept");
	}

	@Override
	public Image getDefaultPageImage() {
		return IconFactory.getImageIcon(FMLIconLibrary.FLEXO_CONCEPT_BIG_ICON, IconLibrary.NEW_32_32).getImage();
	}

	public ConfigureNewConcept getConfigureNewConcept() {
		return configureNewConcept;
	}

	/**
	 * This step is used to set {@link VirtualModel} to be used, as well as name and title of the {@link VirtualModelInstance}
	 * 
	 * @author sylvain
	 *
	 */
	@FIBPanel("Fib/Wizard/ConfigureNewConcept.fib")
	public class ConfigureNewConcept extends WizardStep {

		public ApplicationContext getServiceManager() {
			return getController().getApplicationContext();
		}

		public CreateNewConcept getAction() {
			return action;
		}

		public FreeMetaModel getFreeMetaModel() {
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

			if (getFreeMetaModel().getVirtualModel().getFlexoConcept(getNewConceptName()) != null) {
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
