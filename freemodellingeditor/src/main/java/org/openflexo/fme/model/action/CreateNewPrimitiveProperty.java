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

import org.openflexo.connie.DataBinding;
import org.openflexo.connie.type.PrimitiveType;
import org.openflexo.fme.model.FMEFreeModel;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.FlexoConceptInstanceRole;
import org.openflexo.foundation.fml.FlexoProperty;
import org.openflexo.foundation.fml.action.CreateInspectorEntry;
import org.openflexo.foundation.fml.action.CreatePrimitiveRole;
import org.openflexo.toolbox.StringUtils;

/**
 * This action is used to create a new primitive property for a concept<br>
 * Focused object is the GR concept
 * 
 * @author sylvain
 * 
 */
public class CreateNewPrimitiveProperty extends FMEAction<CreateNewPrimitiveProperty, FlexoConcept, FlexoObject> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CreateNewPrimitiveProperty.class.getPackage().getName());

	public static FlexoActionFactory<CreateNewPrimitiveProperty, FlexoConcept, FlexoObject> actionType = new FlexoActionFactory<CreateNewPrimitiveProperty, FlexoConcept, FlexoObject>(
			"create_new_property", FlexoActionFactory.defaultGroup, FlexoActionFactory.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateNewPrimitiveProperty makeNewAction(FlexoConcept focusedObject, Vector<FlexoObject> globalSelection,
				FlexoEditor editor) {
			return new CreateNewPrimitiveProperty(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(FlexoConcept object, Vector<FlexoObject> globalSelection) {
			return object.getAccessibleProperty(FMEFreeModel.CONCEPT_ROLE_NAME) != null;
		}

		@Override
		public boolean isEnabledForSelection(FlexoConcept object, Vector<FlexoObject> globalSelection) {
			return isVisibleForSelection(object, globalSelection);
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(CreateNewPrimitiveProperty.actionType, FlexoConcept.class);
	}

	private String propertyName;
	private String description;
	private PrimitiveType primitiveType = PrimitiveType.String;

	// private FlexoConceptInstance newFlexoConceptInstance;

	CreateNewPrimitiveProperty(FlexoConcept focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	protected void doAction(Object context) throws FlexoException {

		System.out.println("OK on cree la propriete dans " + getConcept());
		System.out.println("Pour " + getGRConcept());
		System.out.println("type " + getPrimitiveType());

		if (getConcept() != null) {
			CreatePrimitiveRole createPropertyAction = CreatePrimitiveRole.actionType.makeNewEmbeddedAction(getConcept(), null, this);
			createPropertyAction.setRoleName(getPropertyName());
			createPropertyAction.setPrimitiveType(getPrimitiveType());
			createPropertyAction.setDescription(getDescription());
			createPropertyAction.doAction();
		}

		if (getGRConcept() != null) {
			CreateInspectorEntry createInspectorEntry = CreateInspectorEntry.actionType.makeNewEmbeddedAction(getGRConcept().getInspector(),
					null, this);
			createInspectorEntry.setEntryName(getPropertyName());
			createInspectorEntry.setEntryType(getPrimitiveType().getType());
			createInspectorEntry.setData(new DataBinding<Object>(FMEFreeModel.CONCEPT_ROLE_NAME + "." + propertyName));
			createInspectorEntry.doAction();
		}

	}

	/**
	 * Return graphical representation concept
	 * 
	 * @return
	 */
	public FlexoConcept getGRConcept() {
		return getFocusedObject();
	}

	/**
	 * Return concept (conceptual model level) where to create the property<br>
	 * 
	 * @return
	 */
	public FlexoConcept getConcept() {
		if (getGRConcept() != null) {
			FlexoProperty<?> p = getGRConcept().getAccessibleProperty(FMEFreeModel.CONCEPT_ROLE_NAME);
			if (p instanceof FlexoConceptInstanceRole) {
				FlexoConceptInstanceRole fciRole = (FlexoConceptInstanceRole) p;
				return fciRole.getFlexoConceptType();
			}
		}
		return null;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		if ((propertyName == null && getPropertyName() != null) || (propertyName != null && !propertyName.equals(getPropertyName()))) {
			String oldValue = getPropertyName();
			this.propertyName = propertyName;
			getPropertyChangeSupport().firePropertyChange("propertyName", oldValue, propertyName);
		}
	}

	public PrimitiveType getPrimitiveType() {
		return primitiveType;
	}

	public void setPrimitiveType(PrimitiveType primitiveType) {
		this.primitiveType = primitiveType;
		getPropertyChangeSupport().firePropertyChange("primitiveType", null, primitiveType);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		if ((description == null && this.description != null) || (description != null && !description.equals(this.description))) {
			String oldValue = this.description;
			this.description = description;
			getPropertyChangeSupport().firePropertyChange("description", oldValue, description);
		}
	}

	@Override
	public boolean isValid() {

		if (StringUtils.isEmpty(getPropertyName())) {
			return false;
		}

		if (getPrimitiveType() == null) {
			return false;
		}

		return true;
	}

}
