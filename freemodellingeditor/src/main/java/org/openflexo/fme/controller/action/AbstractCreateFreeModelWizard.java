/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Freemodellingeditor, a component of the software infrastructure 
 * developed at Openflexo.
 * 
 * 
 * Openflexo is dual-licensed under the European Union Public License (EUPL, either 
 * version 1.1 of the License, or any later version ), which is available at 
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 * and the GNU General Public License (GPL, either version 3 of the License, or any 
 * later version), which is available at http://www.gnu.org/licenses/gpl.html .
 * 
 * You can redistribute it and/or modify under the terms of either of these licenses
 * 
 * If you choose to redistribute it and/or modify under the terms of the GNU GPL, you
 * must include the following additional permission.
 *
 *          Additional permission under GNU GPL version 3 section 7
 *
 *          If you modify this Program, or any covered work, by linking or 
 *          combining it with software containing parts covered by the terms 
 *          of EPL 1.0, the licensors of this Program grant you additional permission
 *          to convey the resulting work. * 
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. 
 *
 * See http://www.openflexo.org/license.html for details.
 * 
 * 
 * Please contact Openflexo (openflexo-contacts@openflexo.org)
 * or visit www.openflexo.org if you need additional information.
 * 
 */

package org.openflexo.fme.controller.action;

import java.awt.Dimension;
import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.components.wizard.FlexoWizard;
import org.openflexo.components.wizard.WizardStep;
import org.openflexo.fib.annotation.FIBPanel;
import org.openflexo.fme.model.FreeModellingProject;
import org.openflexo.fme.model.action.AbstractCreateFreeModel;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
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
