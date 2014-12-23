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
package org.openflexo.ve;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.icon.IconLibrary;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.toolbox.ImageIconResource;

/**
 * Utility class containing all icons used in context of VEModule
 * 
 * @author sylvain
 * 
 */
public class VEIconLibrary extends IconLibrary {

	private static final Logger logger = Logger.getLogger(VEIconLibrary.class.getPackage().getName());

	// Module icons
	public static final ImageIcon VE_SMALL_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/VE/module-ve-16.png"));
	public static final ImageIcon VE_MEDIUM_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/VE/module-ve-32.png"));
	public static final ImageIcon VE_MEDIUM_ICON_WITH_HOVER = new ImageIconResource(
			ResourceLocator.locateResource("Icons/VE/module-ve-hover-32.png"));
	public static final ImageIcon VE_BIG_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/VE/module-ve-hover-64.png"));

	// Perspective icons
	public static final ImageIcon VE_OP_ACTIVE_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/VE/ontology-perspective.png"));
	public static final ImageIcon VE_SP_ACTIVE_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/VE/diagram-perspective.png"));

}
