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
package org.openflexo.vpm;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.icon.IconLibrary;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.toolbox.ImageIconResource;

/**
 * Utility class containing all icons used in context of VPMModule
 * 
 * @author sylvain
 * 
 */
public class VPMIconLibrary extends IconLibrary {

	private static final Logger logger = Logger.getLogger(VPMIconLibrary.class.getPackage().getName());

	// Module icons
	public static final ImageIcon VPM_SMALL_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/VPM/module-vpm-16.png"));
	public static final ImageIcon VPM_MEDIUM_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/VPM/module-vpm-32.png"));
	public static final ImageIcon VPM_MEDIUM_ICON_WITH_HOVER = new ImageIconResource(
			ResourceLocator.locateResource("Icons/VPM/module-vpm-hover-32.png"));
	public static final ImageIcon VPM_BIG_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/VPM/module-vpm-hover-64.png"));

	// Perspective icons
	public static final ImageIcon VPM_VPE_ACTIVE_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/VPM/viewpoint-perspective.png"));
	// public static final ImageIcon VPM_VPE_SELECTED_ICON = new
	// ImageIconResource(ResourceLocator.locateResource("Icons/VPM/viewpoint-perspective-hover.png"));
	public static final ImageIcon VPM_OP_ACTIVE_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/VPM/ontology-perspective.png"));
	// public static final ImageIcon VPM_OP_SELECTED_ICON = new
	// ImageIconResource(ResourceLocator.locateResource("Icons/VPM/ontology-perspective-hover.png"));

	// Editor icons
	public static final ImageIcon NO_HIERARCHY_MODE_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/VPM/NoHierarchyViewMode.gif"));
	public static final ImageIcon PARTIAL_HIERARCHY_MODE_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/VPM/PartialHierarchyViewMode.gif"));
	public static final ImageIcon FULL_HIERARCHY_MODE_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/VPM/FullHierarchyViewMode.gif"));

}
