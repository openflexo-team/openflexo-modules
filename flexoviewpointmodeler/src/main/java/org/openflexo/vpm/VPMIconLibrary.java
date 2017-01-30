/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Flexoviewpointmodeler, a component of the software infrastructure 
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

package org.openflexo.vpm;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.icon.IconLibrary;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.icon.ImageIconResource;

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
