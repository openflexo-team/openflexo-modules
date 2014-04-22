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

import java.io.File;
import java.util.logging.Logger;

import org.openflexo.fme.model.DataModel;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.rm.Resource;

public class ConceptBrowser extends AbstractFIBPanel {
	static final Logger logger = Logger.getLogger(ConceptBrowser.class.getPackage().getName());

	private static final Resource CONCEPT_BROWSER_FIB = ResourceLocator.locateResource("Fib/ConceptBrowser.fib");

	public ConceptBrowser(DataModel dataModel) {
		super(dataModel, CONCEPT_BROWSER_FIB, false);
	}
}