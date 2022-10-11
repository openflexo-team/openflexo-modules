/**
 * 
 * Copyright (c) 2014, Openflexo
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

import javax.swing.ImageIcon;

import org.openflexo.icon.IconLibrary;
import org.openflexo.icon.IconMarker;
import org.openflexo.icon.ImageIconResource;
import org.openflexo.rm.ResourceLocator;

/**
 * Utility class containing all icons used in context of EnterpriseArchitectureModule
 * 
 * @author sylvain
 * 
 */
public class EAMIconLibrary extends IconLibrary {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(EAMIconLibrary.class.getPackage().getName());

	// Module icons
	public static final ImageIcon EAM_SMALL_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/EAM/module-eam-16.png"));
	public static final ImageIcon EAM_MEDIUM_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/EAM/module-eam-32.png"));
	public static final ImageIcon EAM_MEDIUM_ICON_WITH_HOVER = new ImageIconResource(
			ResourceLocator.locateResource("Icons/EAM/module-eam-hover-32.png"));
	public static final ImageIcon EAM_BIG_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/EAM/module-eam-hover-64.png"));

	public static final ImageIcon BPMN_SMALL_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/EAM/BPMN/BPMN_16x16.png"));
	public static final ImageIcon BPMN_MEDIUM_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/EAM/BPMN/BPMN_32x32.png"));
	public static final ImageIcon BPMN_BIG_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/EAM/BPMN/BPMN_64x64.png"));

	public static final ImageIcon BPMN_PROCESS_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/EAM/BPMN/Process.png"));

	public static final IconMarker EAM_MARKER = new IconMarker(
			new ImageIconResource(ResourceLocator.locateResource("Icons/EAM/EAMarker.png")), 8, 0);
	public static final IconMarker EAM_BIG_MARKER = new IconMarker(EAM_MEDIUM_ICON, 32, 0);

}
