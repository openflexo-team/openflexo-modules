/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Freemodellingeditor, a component of the software infrastructure 
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

package org.openflexo.fme.model;

import java.util.logging.Logger;

import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.foundation.fml.rt.rm.FMLRTVirtualModelInstanceResourceFactory;
import org.openflexo.foundation.nature.VirtualModelInstanceBasedNatureObject;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;

/**
 * Defines container of sample data discovered during free-modelling<br>
 * 
 * Rely on a {@link VirtualModelInstance}
 * 
 * Note that in a {@link FreeModellingProjectNature}, {@link FMESampleData} is conform to {@link FMEConceptualModel}
 * 
 * @author sylvain
 * 
 */
@ModelEntity
@XMLElement
@ImplementationClass(FMESampleData.FMESampleDataImpl.class)
public interface FMESampleData extends VirtualModelInstanceBasedNatureObject<FreeModellingProjectNature> {

	public String getName();

	public abstract class FMESampleDataImpl extends VirtualModelInstanceBasedNatureObjectImpl<FreeModellingProjectNature>
			implements FMESampleData {

		private static final Logger logger = FlexoLogger.getLogger(FMESampleData.class.getPackage().getName());

		@Override
		public String getName() {
			if (getAccessedVirtualModelInstance() != null) {
				return getAccessedVirtualModelInstance().getName() + FMLRTVirtualModelInstanceResourceFactory.FML_RT_SUFFIX;
			}
			return "<SampleData>";
		}

	}
}