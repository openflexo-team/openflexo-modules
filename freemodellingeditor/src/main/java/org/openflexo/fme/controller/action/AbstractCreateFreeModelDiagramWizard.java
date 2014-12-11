package org.openflexo.fme.controller.action;

import java.awt.Dimension;
import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.components.wizard.FlexoWizard;
import org.openflexo.components.wizard.WizardStep;
import org.openflexo.fme.model.FreeMetaModel;
import org.openflexo.fme.model.action.AbstractCreateFreeModelDiagram;
import org.openflexo.foundation.view.VirtualModelInstance;
import org.openflexo.foundation.viewpoint.VirtualModel;
import org.openflexo.foundation.viewpoint.annotations.FIBPanel;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.toolbox.StringUtils;
import org.openflexo.view.controller.FlexoController;

public abstract class AbstractCreateFreeModelDiagramWizard<A extends AbstractCreateFreeModelDiagram<?>> extends FlexoWizard {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(AbstractCreateFreeModelDiagramWizard.class.getPackage().getName());

	private final A action;
	private final DescribeFreeModelDiagram describeFreeModelDiagram;

	private static final Dimension DIMENSIONS = new Dimension(800, 600);

	public AbstractCreateFreeModelDiagramWizard(A action, FlexoController controller) {
		super(controller);
		this.action = action;
		addStep(describeFreeModelDiagram = new DescribeFreeModelDiagram() {
			@Override
			public void done() {
				super.done();
				freeModelDiagramHasBeenDescribed();
			}
		});
	}

	protected void freeModelDiagramHasBeenDescribed() {
	}

	public A getAction() {
		return action;
	}

	@Override
	public Dimension getPreferredSize() {
		return DIMENSIONS;
	}

	public DescribeFreeModelDiagram getDescribeFreeModel() {
		return describeFreeModelDiagram;
	}

	/**
	 * This step is used to set {@link VirtualModel} to be used, as well as name and title of the {@link VirtualModelInstance}
	 * 
	 * @author sylvain
	 *
	 */
	@FIBPanel("Fib/Wizard/DescribeFreeModelDiagram.fib")
	public class DescribeFreeModelDiagram extends WizardStep {

		public ApplicationContext getServiceManager() {
			return getController().getApplicationContext();
		}

		public A getAction() {
			return AbstractCreateFreeModelDiagramWizard.this.getAction();
		}

		@Override
		public String getTitle() {
			return FlexoLocalization.localizedForKey("describe_new_diagram");
		}

		public FreeMetaModel getFreeMetaModel() {
			return getAction().getFocusedObject();
		}

		@Override
		public boolean isValid() {

			if (StringUtils.isEmpty(getDiagramName())) {
				setIssueMessage(FlexoLocalization.localizedForKey("no_diagram_name_defined"), IssueMessageType.ERROR);
				return false;
			}

			if (getFreeMetaModel().getFreeModellingProject().getFreeModel(getDiagramName()) != null) {
				setIssueMessage(FlexoLocalization.localizedForKey("a_diagram_with_that_name_already_exists"), IssueMessageType.ERROR);
				return false;
			} else if (StringUtils.isEmpty(getDescription())) {
				setIssueMessage(FlexoLocalization.localizedForKey("it_is_recommanded_to_describe_new_diagram"), IssueMessageType.WARNING);
			}

			return true;
		}

		public String getDiagramName() {
			return getAction().getDiagramName();
		}

		public void setDiagramName(String newDiagramName) {
			if (!newDiagramName.equals(getDiagramName())) {
				String oldValue = getDiagramName();
				getAction().setDiagramName(newDiagramName);
				getPropertyChangeSupport().firePropertyChange("diagramName", oldValue, newDiagramName);
				checkValidity();
			}
		}

		public String getDescription() {
			return getAction().getDescription();
		}

		public void setDescription(String newDescription) {
			if (!newDescription.equals(getDescription())) {
				String oldValue = getDescription();
				getAction().setDescription(newDescription);
				getPropertyChangeSupport().firePropertyChange("description", oldValue, newDescription);
				checkValidity();
			}
		}

	}

}
