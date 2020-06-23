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

package org.openflexo.eamodule.controller.action;

import java.awt.Dimension;
import java.awt.Image;
import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.components.wizard.FlexoActionWizard;
import org.openflexo.components.wizard.WizardStep;
import org.openflexo.eamodule.EAMIconLibrary;
import org.openflexo.eamodule.model.action.CreateBPMNVirtualModelInstance;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.gina.annotation.FIBPanel;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.toolbox.StringUtils;
import org.openflexo.view.controller.FlexoController;

public class CreateBPMNVirtualModelInstanceWizard extends FlexoActionWizard<CreateBPMNVirtualModelInstance> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CreateBPMNVirtualModelInstanceWizard.class.getPackage().getName());

	private final ConfigureBPMN configureConceptualModel;

	private static final Dimension DIMENSIONS = new Dimension(750, 500);

	public CreateBPMNVirtualModelInstanceWizard(CreateBPMNVirtualModelInstance action, FlexoController controller) {
		super(action, controller);
		addStep(configureConceptualModel = new ConfigureBPMN());
	}

	@Override
	public String getWizardTitle() {
		return getAction().getLocales().localizedForKey("starting_bpmn_modelling");
	}

	@Override
	public Image getDefaultPageImage() {
		return IconFactory.getImageIcon(EAMIconLibrary.BPMN_BIG_ICON, IconLibrary.BIG_NEW_MARKER).getImage();
	}

	@Override
	public Dimension getPreferredSize() {
		return DIMENSIONS;
	}

	public ConfigureBPMN getConfigureConceptualModel() {
		return configureConceptualModel;
	}

	/**
	 * This step is used to set {@link VirtualModel} to be used, as well as name and title of the {@link FMLRTVirtualModelInstance}
	 * 
	 * @author sylvain
	 *
	 */
	@FIBPanel("Fib/Wizard/ConfigureBPMN.fib")
	public class ConfigureBPMN extends WizardStep {

		public ApplicationContext getServiceManager() {
			return getController().getApplicationContext();
		}

		public CreateBPMNVirtualModelInstance getAction() {
			return CreateBPMNVirtualModelInstanceWizard.this.getAction();
		}

		@Override
		public String getTitle() {
			return getAction().getLocales().localizedForKey("configure_bpmn_modelling");
		}

		@Override
		public boolean isValid() {

			if (getAction().getBPMNVirtualModelResource() == null) {
				setIssueMessage(getAction().getLocales().localizedForKey("cannot_find_bpmn_modelling_virtual_model"),
						IssueMessageType.ERROR);
				return false;
			}

			if (StringUtils.isEmpty(getBPMNModelName())) {
				setIssueMessage(getAction().getLocales().localizedForKey("please_choose_a_name_for_bpmn_modelling"),
						IssueMessageType.ERROR);
				return false;
			}

			if (getAction().getFocusedObject().getProject().getVirtualModelInstanceRepository()
					.getVirtualModelInstanceResourceNamed(getBPMNModelName()) != null) {
				setIssueMessage(getAction().getLocales().localizedForKey("duplicated_name_for_bpmn_modelling"), IssueMessageType.ERROR);
				return false;
			}

			if (StringUtils.isEmpty(getBPMNModelDescription())) {
				setIssueMessage(getAction().getLocales().localizedForKey("it_is_recommanded_to_provide_a_description"),
						IssueMessageType.WARNING);
			}

			return true;

		}

		public String getBPMNModelName() {
			return getAction().getBPMNModelName();
		}

		public void setBPMNModelName(String bpmnModelName) {
			if ((bpmnModelName == null && getBPMNModelName() != null)
					|| (bpmnModelName != null && !bpmnModelName.equals(getBPMNModelName()))) {
				String oldValue = getBPMNModelName();
				getAction().setBPMNModelName(bpmnModelName);
				getPropertyChangeSupport().firePropertyChange("BPMNModelName", oldValue, bpmnModelName);
				checkValidity();
			}
		}

		public String getBPMNModelDescription() {
			return getAction().getBPMNModelDescription();
		}

		public void setBPMNModelDescription(String bpmnModelDescription) {
			if ((bpmnModelDescription == null && getBPMNModelDescription() != null)
					|| (bpmnModelDescription != null && !bpmnModelDescription.equals(getBPMNModelDescription()))) {
				String oldValue = getBPMNModelDescription();
				getAction().setBPMNModelDescription(bpmnModelDescription);
				getPropertyChangeSupport().firePropertyChange("BPMNModelDescription", oldValue, bpmnModelDescription);
				checkValidity();
			}
		}

	}

}
