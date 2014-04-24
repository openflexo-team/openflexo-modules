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

import org.openflexo.fme.controller.FMEController;
import org.openflexo.fme.model.FreeModel;
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
public class FIBRepresentedConceptBrowser extends FIBBrowserView<FreeModel> {
	static final Logger logger = Logger.getLogger(FIBRepresentedConceptBrowser.class.getPackage().getName());

	public static final Resource FIB_FILE = ResourceLocator.locateResource("Fib/Widget/FIBRepresentedConceptBrowser.fib");

	public FIBRepresentedConceptBrowser(FreeModel freeModel, FMEController controller) {
		super(freeModel, controller, FIB_FILE);
		// System.out.println("Showing browser with " + project);
	}

	public void setFreeModel(FreeModel freeModel) {
		setDataObject(freeModel);
	}
}