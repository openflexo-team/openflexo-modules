/**
 * 
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2011-2012, AgileBirds
 * 
 * This file is part of Flexovieweditor, a component of the software infrastructure 
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

package org.openflexo.eamodule;

import java.util.logging.Logger;

import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLAttribute;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.prefs.ModulePreferences;
import org.openflexo.prefs.Preferences;

/**
 * Contains preferences for {@link EnterpriseArchitectureModule} module
 * 
 * @author sylvain
 * 
 */
@ModelEntity
@ImplementationClass(EAMPreferences.EAMPreferencesImpl.class)
@XMLElement(xmlTag = "EAMPreferences")
@Preferences(
		shortName = "Enterprise Architecture Editor",
		longName = "Enterprise Architecture Editor Preferences",
		FIBPanel = "Fib/Prefs/EAMPreferences.fib",
		smallIcon = "Icons/EAM/module-eam-16.png",
		bigIcon = "Icons/EAM/module-eam-64.png")
public interface EAMPreferences extends ModulePreferences<EAModule> {

	public static final String SCREENSHOT_QUALITY_KEY = "screenshotQuality";

	/*public static final String PROPERTY_KEY = "property";
	
	@Getter(value = PROPERTY_KEY)
	@XMLAttribute
	public Object getProperty();
	
	@Setter(PROPERTY_KEY)
	public void setProperty(Object object);*/

	@Getter(value = SCREENSHOT_QUALITY_KEY, defaultValue = "100")
	@XMLAttribute
	public int getScreenshotQuality();

	@Setter(SCREENSHOT_QUALITY_KEY)
	public void setScreenshotQuality(int limit);

	public abstract class EAMPreferencesImpl extends PreferencesContainerImpl implements EAMPreferences {

		@SuppressWarnings("unused")
		private static final Logger logger = Logger.getLogger(EAMPreferences.class.getPackage().getName());

		/*@Override
		public Object getProperty() {
			return null;
		}
		
		@Override
		public void setProperty(Object property) {
			if ((property == null && getProperty() != null) || (property != null && !property.equals(getProperty()))) {
				Object oldValue = getProperty();
				// this.property = property;
				getPropertyChangeSupport().firePropertyChange("property", oldValue, property);
			}
		}*/

	}

}
