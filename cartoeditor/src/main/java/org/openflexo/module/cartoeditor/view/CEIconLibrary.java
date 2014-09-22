/*
 * (c) Copyright 2014- Openflexo
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

package org.openflexo.module.cartoeditor.view;

import javax.swing.ImageIcon;

import org.openflexo.rm.ResourceLocator;
import org.openflexo.toolbox.ImageIconResource;

public class CEIconLibrary {

	public static final ImageIcon CE_SMALL_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/module-ce-16.png"));
	public static final ImageIcon CE_MEDIUM_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/bmo_32.jpg"));
	public static final ImageIcon CE_MEDIUM_ICON_HOVER = new ImageIconResource(
			ResourceLocator.locateResource("Icons/module-ce-hover-32.png"));
	public static final ImageIcon CE_BIG_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/module-ce-64.png"));

}
