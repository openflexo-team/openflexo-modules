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
import org.openflexo.connie.type.PrimitiveType;
import org.openflexo.fme.model.action.CreateNewPrimitiveProperty;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.gina.annotation.FIBPanel;
import org.openflexo.icon.FMLIconLibrary;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.toolbox.StringUtils;
import org.openflexo.view.controller.FlexoController;

public class CreateNewPrimitivePropertyWizard extends FlexoWizard {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CreateNewPrimitivePropertyWizard.class.getPackage().getName());

	private static final Dimension DIMENSIONS = new Dimension(600, 400);

	private final CreateNewPrimitiveProperty action;

	private final ConfigureNewPrimitiveProperty configureNewPrimitiveProperty;

	public CreateNewPrimitivePropertyWizard(CreateNewPrimitiveProperty action, FlexoController controller) {
		super(controller);
		this.action = action;
		addStep(configureNewPrimitiveProperty = new ConfigureNewPrimitiveProperty());
	}

	@Override
	public String getWizardTitle() {
		return action.getLocales().localizedForKey("create_new_property");
	}

	@Override
	public Dimension getPreferredSize() {
		return DIMENSIONS;
	}

	@Override
	public Image getDefaultPageImage() {
		return IconFactory.getImageIcon(FMLIconLibrary.FLEXO_ROLE_BIG_ICON, IconLibrary.NEW_32_32).getImage();
	}

	public ConfigureNewPrimitiveProperty getConfigureNewPrimitiveProperty() {
		return configureNewPrimitiveProperty;
	}

	/**
	 * This step is used to set {@link VirtualModel} to be used, as well as name and title of the {@link FMLRTVirtualModelInstance}
	 * 
	 * @author sylvain
	 *
	 */
	@FIBPanel("Fib/Wizard/ConfigureNewPrimitiveProperty.fib")
	public class ConfigureNewPrimitiveProperty extends WizardStep {

		public ApplicationContext getServiceManager() {
			return getController().getApplicationContext();
		}

		public CreateNewPrimitiveProperty getAction() {
			return action;
		}

		@Override
		public String getTitle() {
			return action.getLocales().localizedForKey("configure_new_property");
		}

		@Override
		public boolean isValid() {

			if (StringUtils.isEmpty(getPropertyName())) {
				setIssueMessage(action.getLocales().localizedForKey("no_property_name_defined"), IssueMessageType.ERROR);
				return false;
			}

			if (getPrimitiveType() == null) {
				setIssueMessage(action.getLocales().localizedForKey("no_property_type_defined"), IssueMessageType.ERROR);
				return false;
			}

			if (getAction().getConcept().getAccessibleProperty(getPropertyName()) != null) {
				setIssueMessage(action.getLocales().localizedForKey("a_property_with_that_name_already_exists"), IssueMessageType.ERROR);
				return false;
			}

			if (StringUtils.isEmpty(getDescription())) {
				setIssueMessage(action.getLocales().localizedForKey("it_is_recommanded_to_describe_the_new_property"),
						IssueMessageType.WARNING);
			}

			return true;

		}

		public String getPropertyName() {
			return action.getPropertyName();
		}

		public void setPropertyName(String propertyName) {
			if ((propertyName == null && getPropertyName() != null) || (propertyName != null && !propertyName.equals(getPropertyName()))) {
				String oldValue = getPropertyName();
				action.setPropertyName(propertyName);
				getPropertyChangeSupport().firePropertyChange("propertyName", oldValue, propertyName);
				checkValidity();
			}
		}

		public PrimitiveType getPrimitiveType() {
			return action.getPrimitiveType();
		}

		public void setPrimitiveType(PrimitiveType primitiveType) {
			if (primitiveType != getPrimitiveType()) {
				PrimitiveType oldValue = getPrimitiveType();
				action.setPrimitiveType(primitiveType);
				getPropertyChangeSupport().firePropertyChange("primitiveType", null, primitiveType);
				checkValidity();
			}
		}

		public String getDescription() {
			return action.getDescription();
		}

		public void setDescription(String description) {
			if ((description == null && getDescription() != null) || (description != null && !description.equals(getDescription()))) {
				String oldValue = getDescription();
				action.setDescription(description);
				getPropertyChangeSupport().firePropertyChange("description", oldValue, description);
				checkValidity();
			}
		}

	}

}
