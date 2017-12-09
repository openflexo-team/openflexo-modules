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
import java.awt.Image;
import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.components.wizard.FlexoWizard;
import org.openflexo.components.wizard.WizardStep;
import org.openflexo.fme.FMEIconLibrary;
import org.openflexo.fme.model.action.GivesFMENature;
import org.openflexo.fme.model.action.GivesFMENature.ConceptualModelChoice;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rm.VirtualModelResource;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.gina.annotation.FIBPanel;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.toolbox.StringUtils;
import org.openflexo.view.controller.FlexoController;

public class GivesFMENatureWizard extends FlexoWizard {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(GivesFMENatureWizard.class.getPackage().getName());

	private final GivesFMENature action;

	private final ConfigureConceptualModel configureConceptualModel;

	private static final Dimension DIMENSIONS = new Dimension(800, 600);

	public GivesFMENatureWizard(GivesFMENature action, FlexoController controller) {
		super(controller);
		this.action = action;
		addStep(configureConceptualModel = new ConfigureConceptualModel());
	}

	@Override
	public String getWizardTitle() {
		return action.getLocales().localizedForKey("gives_project_free_modelling_nature");
	}

	@Override
	public Image getDefaultPageImage() {
		return IconFactory.getImageIcon(FMEIconLibrary.FREE_MODEL_BIG_ICON, IconLibrary.NEW_32_32).getImage();
	}

	@Override
	public Dimension getPreferredSize() {
		return DIMENSIONS;
	}

	public ConfigureConceptualModel getConfigureConceptualModel() {
		return configureConceptualModel;
	}

	/**
	 * This step is used to set {@link VirtualModel} to be used, as well as name and title of the {@link FMLRTVirtualModelInstance}
	 * 
	 * @author sylvain
	 *
	 */
	@FIBPanel("Fib/Wizard/ConfigureConceptualModel.fib")
	public class ConfigureConceptualModel extends WizardStep {

		public ApplicationContext getServiceManager() {
			return getController().getApplicationContext();
		}

		public GivesFMENature getAction() {
			return action;
		}

		@Override
		public String getTitle() {
			return action.getLocales().localizedForKey("configure_conceptual_model");
		}

		@Override
		public boolean isValid() {

			if (getConceptualModelChoice() == null) {
				setIssueMessage(action.getLocales().localizedForKey("please_chose_an_option"), IssueMessageType.ERROR);
				return false;
			}

			switch (getConceptualModelChoice()) {
				case CreateNewVirtualModel:
					if (StringUtils.isEmpty(getNewConceptualModelName())) {
						setIssueMessage(action.getLocales().localizedForKey("no_conceptual_model_name_defined"), IssueMessageType.ERROR);
						return false;
					}
					/*if (StringUtils.isEmpty(getNewConceptualModelRelativePath())) {
						setIssueMessage(action.getLocales().localizedForKey("no_relative_path_defined"), IssueMessageType.ERROR);
						return false;
					}*/
					break;
				case SelectExistingVirtualModel:
					if (getExistingConceptualModelResource() == null) {
						setIssueMessage(action.getLocales().localizedForKey("please_select_a_virtual_model"), IssueMessageType.ERROR);
						return false;
					}
					break;
			}

			return true;

		}

		public ConceptualModelChoice getConceptualModelChoice() {
			return action.getConceptualModelChoice();
		}

		public void setConceptualModelChoice(ConceptualModelChoice conceptualModelChoice) {
			if (conceptualModelChoice != getConceptualModelChoice()) {
				ConceptualModelChoice oldValue = getConceptualModelChoice();
				action.setConceptualModelChoice(conceptualModelChoice);
				getPropertyChangeSupport().firePropertyChange("conceptualModelChoice", oldValue, conceptualModelChoice);
				checkValidity();
			}
		}

		public VirtualModelResource getExistingConceptualModelResource() {
			return action.getExistingConceptualModelResource();
		}

		public void setExistingConceptualModelResource(VirtualModelResource existingConceptualModelResource) {
			if (existingConceptualModelResource != getExistingConceptualModelResource()) {
				VirtualModelResource oldValue = getExistingConceptualModelResource();
				action.setExistingConceptualModelResource(existingConceptualModelResource);
				getPropertyChangeSupport().firePropertyChange("existingConceptualModelResource", oldValue, existingConceptualModelResource);
				checkValidity();
			}
		}

		public String getNewConceptualModelName() {
			return action.getNewConceptualModelName();
		}

		public void setNewConceptualModelName(String newConceptualModelName) {
			if ((newConceptualModelName == null && getNewConceptualModelName() != null)
					|| (newConceptualModelName != null && !newConceptualModelName.equals(getNewConceptualModelName()))) {
				String oldValue = getNewConceptualModelName();
				action.setNewConceptualModelName(newConceptualModelName);
				getPropertyChangeSupport().firePropertyChange("newConceptualModelName", oldValue, newConceptualModelName);
				checkValidity();
			}
		}

		public String getNewConceptualModelRelativePath() {
			return action.getNewConceptualModelRelativePath();
		}

		public void setNewConceptualModelRelativePath(String newConceptualModelRelativePath) {
			if ((newConceptualModelRelativePath == null && getNewConceptualModelRelativePath() != null)
					|| (newConceptualModelRelativePath != null
							&& !newConceptualModelRelativePath.equals(getNewConceptualModelRelativePath()))) {
				String oldValue = getNewConceptualModelRelativePath();
				action.setNewConceptualModelRelativePath(newConceptualModelRelativePath);
				getPropertyChangeSupport().firePropertyChange("newConceptualModelRelativePath", oldValue, newConceptualModelRelativePath);
				checkValidity();
			}
		}

	}

}
