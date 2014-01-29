/*
 * (c) Copyright 2010-2011 AgileBirds
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openflexo.fme;

import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.prefs.ModulePreferences;

/**
 * Contains preferences for ViewPointModeller module
 * 
 * @author sylvain
 * 
 */
@ModelEntity
@XMLElement(xmlTag = "FMEPreferences")
public interface FMEPreferences extends ModulePreferences<FMEModule> {

	public static final String SCREENSHOT_QUALITY_KEY = "screenshotQuality";

	@Getter(value = SCREENSHOT_QUALITY_KEY, defaultValue = "100")
	@XMLAttribute
	public int getScreenshotQuality();

	@Setter(SCREENSHOT_QUALITY_KEY)
	public void setScreenshotQuality(int limit);

}
