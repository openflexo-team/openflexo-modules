package org.openflexo.fme.controller.action;

import java.awt.Dimension;
import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.components.wizard.FlexoWizard;
import org.openflexo.components.wizard.WizardStep;
import org.openflexo.fme.model.FreeModellingProject;
import org.openflexo.fme.model.action.AbstractCreateFreeModel;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.annotations.FIBPanel;
import org.openflexo.foundation.fmlrt.VirtualModelInstance;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.toolbox.StringUtils;
import org.openflexo.view.controller.FlexoController;

public abstract class AbstractCreateFreeModelWizard<A extends AbstractCreateFreeModel<?>> extends FlexoWizard {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(AbstractCreateFreeModelWizard.class.getPackage().getName());

	private final A action;
	private final DescribeFreeModel describeFreeModel;

	private static final Dimension DIMENSIONS = new Dimension(800, 600);

	public AbstractCreateFreeModelWizard(A action, FlexoController controller) {
		super(controller);
		this.action = action;
		addStep(describeFreeModel = new DescribeFreeModel());
	}

	public A getAction() {
		return action;
	}

	@Override
	public Dimension getPreferredSize() {
		return DIMENSIONS;
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

		public A getAction() {
			return AbstractCreateFreeModelWizard.this.getAction();
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
