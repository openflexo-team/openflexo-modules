/*
 * (c) Copyright 2010-2011 AgileBirds
 * (c) Copyright 2012-2013 Openflexo
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

import javax.swing.ImageIcon;

import org.openflexo.rm.ResourceLocator;
import org.openflexo.toolbox.ImageIconResource;

public class FMEIconLibrary {

	// Module icons
	public static final ImageIcon FME_SMALL_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/module-fme-16.png"));
	public static final ImageIcon FME_MEDIUM_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/module-fme-32.png"));
	public static final ImageIcon FME_MEDIUM_ICON_WITH_HOVER = new ImageIconResource(
			ResourceLocator.locateResource("Icons/module-fme-hover-32.png"));
	public static final ImageIcon FME_BIG_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/module-fme-hover-64.png"));

	// Perspective icons
	public static final ImageIcon VPM_VPE_ACTIVE_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/VPM/viewpoint-perspective.png"));

	public static final ImageIcon COPY_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/Copy.png"));
	public static final ImageIcon PASTE_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/Paste.png"));
	public static final ImageIcon CUT_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/Cut.png"));
	public static final ImageIcon DELETE_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/Delete.png"));

	public static final ImageIconResource FREE_MODEL_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/FreeModel.png"));
	public static final ImageIconResource FREE_MODEL_MEDIUM_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/FreeModel_32x32.png"));
	public static final ImageIconResource FREE_MODEL_BIG_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/FreeModel_64x64.png"));

	public static final ImageIcon CONCEPT_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/EClass.gif"));
	public static final ImageIcon INSTANCE_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/EObject.gif"));

	public static final ImageIcon DIAGRAM_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/Diagram.png"));
}
