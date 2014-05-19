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
package org.openflexo.fme.widget;

import java.util.logging.Logger;

import org.openflexo.fib.model.FIBBrowser;
import org.openflexo.fib.model.FIBContainer;
import org.openflexo.fme.controller.FMEController;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.view.FIBBrowserView;

/**
 * Browser allowing to browse all resources of a {@link FlexoProject}<br>
 * 
 * @author sguerin
 * 
 */
@SuppressWarnings("serial")
public class FIBFreeModellingProjectBrowser extends FIBBrowserView<FlexoProject> {
	static final Logger logger = Logger.getLogger(FIBFreeModellingProjectBrowser.class.getPackage().getName());

	public static final Resource FIB_FILE = ResourceLocator.locateResource("Fib/Widget/FIBFreeModellingProjectBrowser.fib");

	public FIBFreeModellingProjectBrowser(FlexoProject project, FMEController controller) {
		super(project, controller, FIB_FILE);
		// System.out.println("Showing browser with " + project);
	}

	@Override
	protected void initializeFIBComponent() {

		FIBBrowser projectBrowser = retrieveFIBBrowserNamed((FIBContainer) getFIBComponent(), "ProjectBrowser");

		if (projectBrowser != null) {
			bindFlexoActionsToBrowser(projectBrowser);
		}

		FIBBrowser freeModellingProjectBrowser = retrieveFIBBrowserNamed((FIBContainer) getFIBComponent(), "FreeModellingProjectBrowser");

		if (freeModellingProjectBrowser != null) {
			bindFlexoActionsToBrowser(freeModellingProjectBrowser);
		}

	}

}