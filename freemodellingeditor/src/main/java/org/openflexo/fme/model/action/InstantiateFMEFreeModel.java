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

package org.openflexo.fme.model.action;

import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.fme.model.FMEFreeModel;
import org.openflexo.fme.model.FMEFreeModelInstance;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.toolbox.StringUtils;

/**
 * Abstract implementation for the creation of a {@link FMEFreeModelInstance}
 * 
 * This action is used to instantiate a new {@link FMEFreeModelInstance} conform to a {@link FMEFreeModel}<br>
 * 
 * @author sylvain
 * 
 */
public abstract class InstantiateFMEFreeModel<A extends InstantiateFMEFreeModel<A, FM>, FM extends FMEFreeModel>
		extends FMEAction<A, FM, FlexoObject> {

	private static final Logger logger = Logger.getLogger(InstantiateFMEFreeModel.class.getPackage().getName());

	private FMEFreeModelInstance freeModelInstance;
	private String freeModelInstanceName;
	private String freeModelInstanceDescription;

	InstantiateFMEFreeModel(FlexoActionFactory<A, FM, FlexoObject> actionType, FM focusedObject, Vector<FlexoObject> globalSelection,
			FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	protected void doAction(Object context) throws SaveResourceException {

		logger.info("Instantiate free model " + getFocusedObject());

		// Create FreeMetaModel when not existant
		// Use the same name
		freeModelInstance = instantiateFreeModel(getFreeModelInstanceName());

		getFocusedObject().getPropertyChangeSupport().firePropertyChange("freeModelInstances", null, null);

	}

	protected abstract FMEFreeModelInstance instantiateFreeModel(String freeModelInstanceName);

	@Override
	public boolean isValid() {

		if (StringUtils.isEmpty(freeModelInstanceName)) {
			return false;
		}

		if (getFocusedObject() != null && getFocusedObject().getFreeModelInstance(freeModelInstanceName) != null) {
			return false;
		}

		return true;
	}

	/**
	 * Return newly created FreeModelInstance
	 * 
	 * @return
	 */
	public FMEFreeModelInstance getNewFreeModelInstance() {
		return freeModelInstance;
	}

	public String getFreeModelInstanceName() {
		return freeModelInstanceName;
	}

	public void setFreeModelInstanceName(String freeModelInstanceName) {
		boolean wasValid = isValid();
		this.freeModelInstanceName = freeModelInstanceName;
		getPropertyChangeSupport().firePropertyChange("freeModelInstanceName", null, freeModelInstanceName);
		getPropertyChangeSupport().firePropertyChange("isValid", wasValid, isValid());
	}

	public String getFreeModelInstanceDescription() {
		return freeModelInstanceDescription;
	}

	public void setFreeModelInstanceDescription(String freeModelInstanceDescription) {
		boolean wasValid = isValid();
		this.freeModelInstanceDescription = freeModelInstanceDescription;
		getPropertyChangeSupport().firePropertyChange("freeModelInstanceDescription", null, freeModelInstanceDescription);
		getPropertyChangeSupport().firePropertyChange("isValid", wasValid, isValid());
	}

}
