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
import java.util.Date;
import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.components.wizard.FlexoWizard;
import org.openflexo.components.wizard.WizardStep;
import org.openflexo.fme.model.FMEType;
import org.openflexo.fme.model.action.InstantiateNewFMEProperty;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.gina.annotation.FIBPanel;
import org.openflexo.icon.FMLIconLibrary;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.toolbox.StringUtils;
import org.openflexo.view.controller.FlexoController;

public class InstantiateNewFMEPropertyWizard extends FlexoWizard {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(InstantiateNewFMEPropertyWizard.class.getPackage().getName());

	private static final Dimension DIMENSIONS = new Dimension(600, 400);

	private final InstantiateNewFMEProperty action;

	private final ConfigureInstantiateNewFMEProperty configureNewFMEProperty;

	public InstantiateNewFMEPropertyWizard(InstantiateNewFMEProperty action, FlexoController controller) {
		super(controller);
		this.action = action;
		addStep(configureNewFMEProperty = new ConfigureInstantiateNewFMEProperty());
	}

	@Override
	public String getWizardTitle() {
		return action.getLocales().localizedForKey("define_new_property");
	}

	@Override
	public Dimension getPreferredSize() {
		return DIMENSIONS;
	}

	@Override
	public Image getDefaultPageImage() {
		return IconFactory.getImageIcon(FMLIconLibrary.FLEXO_ROLE_BIG_ICON, IconLibrary.NEW_32_32).getImage();
	}

	/**
	 * This step is used to set {@link VirtualModel} to be used, as well as name and title of the {@link FMLRTVirtualModelInstance}
	 * 
	 * @author sylvain
	 *
	 */
	@FIBPanel("Fib/Wizard/ConfigureInstantiateNewFMEProperty.fib")
	public class ConfigureInstantiateNewFMEProperty extends WizardStep {

		public ApplicationContext getServiceManager() {
			return getController().getApplicationContext();
		}

		public InstantiateNewFMEProperty getAction() {
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

			if (getFMEType() == null) {
				setIssueMessage(action.getLocales().localizedForKey("no_property_type_defined"), IssueMessageType.ERROR);
				return false;
			}

			if (getAction().getCreatePropertyAction().getConcept().getAccessibleProperty(getPropertyName()) != null) {
				setIssueMessage(action.getLocales().localizedForKey("a_property_with_that_name_already_exists"), IssueMessageType.ERROR);
				return false;
			}

			if (getFMEType() == FMEType.Enumeration && StringUtils.isEmpty(getEnumValues())) {
				setIssueMessage(action.getLocales().localizedForKey("no_enum_values_defined"), IssueMessageType.ERROR);
				return false;
			}

			if (getFMEType() == FMEType.Reference && getReferenceType() == null) {
				setIssueMessage(action.getLocales().localizedForKey("please_choose_a_concept_as_type_for_property"),
						IssueMessageType.ERROR);
				return false;
			}

			if (StringUtils.isEmpty(getDescription())) {
				setIssueMessage(action.getLocales().localizedForKey("it_is_recommanded_to_describe_the_new_property"),
						IssueMessageType.WARNING);
			}

			return true;

		}

		public String getPropertyName() {
			return action.getCreatePropertyAction().getPropertyName();
		}

		public void setPropertyName(String propertyName) {
			if ((propertyName == null && getPropertyName() != null) || (propertyName != null && !propertyName.equals(getPropertyName()))) {
				String oldValue = getPropertyName();
				action.getCreatePropertyAction().setPropertyName(propertyName);
				getPropertyChangeSupport().firePropertyChange("propertyName", oldValue, propertyName);
				checkValidity();
			}
		}

		public FMEType getFMEType() {
			return action.getCreatePropertyAction().getFMEType();
		}

		public void setFMEType(FMEType fmeType) {
			if (fmeType != getFMEType()) {
				FMEType oldValue = getFMEType();
				action.getCreatePropertyAction().setFMEType(fmeType);
				getPropertyChangeSupport().firePropertyChange("FMEType", oldValue, fmeType);
				checkValidity();
			}
		}

		public String getEnumValues() {
			return action.getCreatePropertyAction().getEnumValues();
		}

		public void setEnumValues(String enumValues) {
			if ((enumValues == null && getEnumValues() != null) || (enumValues != null && !enumValues.equals(getEnumValues()))) {
				String oldValue = getEnumValues();
				action.getCreatePropertyAction().setEnumValues(enumValues);
				getPropertyChangeSupport().firePropertyChange("enumValues", oldValue, enumValues);
				checkValidity();
			}
		}

		public FlexoConcept getReferenceType() {
			return action.getCreatePropertyAction().getReferenceType();
		}

		public void setReferenceType(FlexoConcept referenceType) {
			if (referenceType != getReferenceType()) {
				FlexoConcept oldValue = getReferenceType();
				action.getCreatePropertyAction().setReferenceType(referenceType);
				getPropertyChangeSupport().firePropertyChange("referenceType", oldValue, referenceType);
				checkValidity();
			}
		}

		public String getDescription() {
			return action.getCreatePropertyAction().getDescription();
		}

		public void setDescription(String description) {
			if ((description == null && getDescription() != null) || (description != null && !description.equals(getDescription()))) {
				String oldValue = getDescription();
				action.getCreatePropertyAction().setDescription(description);
				getPropertyChangeSupport().firePropertyChange("description", oldValue, description);
				checkValidity();
			}
		}

		public String getStringValue() {
			return action.getStringValue();
		}

		public void setStringValue(String stringValue) {
			if ((stringValue == null && getStringValue() != null) || (stringValue != null && !stringValue.equals(getStringValue()))) {
				String oldValue = getStringValue();
				action.setStringValue(stringValue);
				getPropertyChangeSupport().firePropertyChange("stringValue", oldValue, stringValue);
				checkValidity();
			}
		}

		public Boolean getBooleanValue() {
			return action.getBooleanValue();
		}

		public void setBooleanValue(Boolean booleanValue) {
			if ((booleanValue == null && getBooleanValue() != null) || (booleanValue != null && !booleanValue.equals(getBooleanValue()))) {
				Boolean oldValue = getBooleanValue();
				action.setBooleanValue(booleanValue);
				getPropertyChangeSupport().firePropertyChange("booleanValue", oldValue, booleanValue);
				checkValidity();
			}
		}

		public Long getIntegerValue() {
			return action.getIntegerValue();
		}

		public void setIntegerValue(Long integerValue) {
			if ((integerValue == null && getIntegerValue() != null) || (integerValue != null && !integerValue.equals(getIntegerValue()))) {
				Long oldValue = getIntegerValue();
				action.setIntegerValue(integerValue);
				getPropertyChangeSupport().firePropertyChange("integerValue", oldValue, integerValue);
				checkValidity();
			}
		}

		public Double getFloatValue() {
			return action.getFloatValue();
		}

		public void setFloatValue(Double floatValue) {
			if ((floatValue == null && getFloatValue() != null) || (floatValue != null && !floatValue.equals(getFloatValue()))) {
				Double oldValue = getFloatValue();
				action.setFloatValue(floatValue);
				getPropertyChangeSupport().firePropertyChange("floatValue", oldValue, floatValue);
				checkValidity();
			}
		}

		public Date getDateValue() {
			return action.getDateValue();
		}

		public void setDateValue(Date dateValue) {
			if ((dateValue == null && getDateValue() != null) || (dateValue != null && !dateValue.equals(getDateValue()))) {
				Date oldValue = getDateValue();
				action.setDateValue(dateValue);
				getPropertyChangeSupport().firePropertyChange("dateValue", oldValue, dateValue);
				checkValidity();
			}
		}

		public String getEnumerationValue() {
			return action.getEnumerationValue();
		}

		public void setEnumerationValue(String enumerationValue) {
			if ((enumerationValue == null && getEnumerationValue() != null)
					|| (enumerationValue != null && !enumerationValue.equals(getEnumerationValue()))) {
				String oldValue = getEnumerationValue();
				action.setEnumerationValue(enumerationValue);
				getPropertyChangeSupport().firePropertyChange("enumerationValue", oldValue, enumerationValue);
				checkValidity();
			}
		}

		public FlexoConceptInstance getReferenceValue() {
			return action.getReferenceValue();
		}

		public void setReferenceValue(FlexoConceptInstance referenceValue) {
			if ((referenceValue == null && getReferenceValue() != null)
					|| (referenceValue != null && !referenceValue.equals(getReferenceValue()))) {
				FlexoConceptInstance oldValue = getReferenceValue();
				action.setReferenceValue(referenceValue);
				getPropertyChangeSupport().firePropertyChange("referenceValue", oldValue, referenceValue);
				checkValidity();
			}
		}
	}

}
