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

import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;

/**
 * Constants used by the FreeModellingEditor
 * 
 * @author sylvain
 */
public class FMECst {

	public static final int PALETTE_DOC_SPLIT_LOCATION = 300;

	public static String FME_MODULE_VERSION = "0.0.1";

	public static String FME_MODULE_SHORT_NAME = "FME";

	public static String FME_MODULE_NAME = "FME_module_name";

	public static String FME_MODULE_DESCRIPTION = "FME_module_name_description";

	public static String DEFAULT_FME_BROWSER_WINDOW_TITLE = "Free Modelling Editor";

	public static int DEFAULT_FME_BROWSER_WINDOW_WIDTH = 300;

	public static int DEFAULT_FME_BROWSER_WINDOW_HEIGHT = 250;

	public static int DEFAULT_MAINFRAME_WIDTH = 850;

	public static int DEFAULT_MAINFRAME_HEIGHT = 600;

	public static Resource CREATE_FREE_MODEL_DIALOG_FIB = ResourceLocator.locateResource("Fib/Dialog/CreateFreeModelDialog.fib");
	public static Resource CREATE_NEW_CONCEPT_DIALOG_FIB = ResourceLocator.locateResource("Fib/Dialog/CreateNewConceptDialog.fib");

}
