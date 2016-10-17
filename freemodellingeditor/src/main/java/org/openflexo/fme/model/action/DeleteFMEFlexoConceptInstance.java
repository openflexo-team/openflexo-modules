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

import org.openflexo.ApplicationContext;
import org.openflexo.fme.FreeModellingEditor;
import org.openflexo.fme.model.FreeModel;
import org.openflexo.fme.model.FreeModellingProject;
import org.openflexo.fme.model.FreeModellingProjectNature;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.InvalidArgumentException;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.fml.DeletionScheme;
import org.openflexo.foundation.fml.rt.AbstractVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstanceObject;
import org.openflexo.foundation.fml.rt.action.DeletionSchemeAction;
import org.openflexo.foundation.fml.rt.action.DeletionSchemeActionType;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.localization.LocalizedDelegate;

/**
 * This action is used to delete a flexo concept instance within Free modeling editor<br>
 * 
 * @author vincent
 * 
 */
public class DeleteFMEFlexoConceptInstance
		extends FlexoAction<DeleteFMEFlexoConceptInstance, FlexoConceptInstance, VirtualModelInstanceObject> {

	private static final Logger logger = Logger.getLogger(DeleteFMEFlexoConceptInstance.class.getPackage().getName());

	public static FlexoActionType<DeleteFMEFlexoConceptInstance, FlexoConceptInstance, VirtualModelInstanceObject> actionType = new FlexoActionType<DeleteFMEFlexoConceptInstance, FlexoConceptInstance, VirtualModelInstanceObject>(
			"delete_flexo_concept_instance", FlexoActionType.editGroup, FlexoActionType.DELETE_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public DeleteFMEFlexoConceptInstance makeNewAction(FlexoConceptInstance focusedObject,
				Vector<VirtualModelInstanceObject> globalSelection, FlexoEditor editor) {
			return new DeleteFMEFlexoConceptInstance(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(FlexoConceptInstance object, Vector<VirtualModelInstanceObject> globalSelection) {
			return true;
		}

		@Override
		public boolean isEnabledForSelection(FlexoConceptInstance object, Vector<VirtualModelInstanceObject> globalSelection) {
			return true;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(DeleteFMEFlexoConceptInstance.actionType, FlexoConceptInstance.class);
	}

	DeleteFMEFlexoConceptInstance(FlexoConceptInstance focusedObject, Vector<VirtualModelInstanceObject> globalSelection,
			FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	public LocalizedDelegate getLocales() {
		if (getServiceManager() instanceof ApplicationContext) {
			return ((ApplicationContext) getServiceManager()).getModuleLoader().getModule(FreeModellingEditor.class)
					.getLoadedModuleInstance().getLocales();
		}
		return super.getLocales();
	}

	@Override
	protected void doAction(Object context) throws SaveResourceException {

		logger.info("Delete flexo concept instances");

		try {
			FreeModel freeModel = getFreeModel();
			Vector<FlexoObject> globalSelection = getGlobalSelectionAndFocusedObject();
			if (globalSelection != null) {
				for (FlexoObject selection : globalSelection) {
					if (selection instanceof FlexoConceptInstance) {
						AbstractVirtualModelInstance<?, ?> vmi = ((FlexoConceptInstance) selection).getVirtualModelInstance();
						DeletionScheme deletionScheme = ((FlexoConceptInstance) selection).getFlexoConcept().getDefaultDeletionScheme();
						if (deletionScheme != null) {
							DeletionSchemeActionType deletionSchemeActionType = new DeletionSchemeActionType(deletionScheme,
									(FlexoConceptInstance) selection);
							DeletionSchemeAction deletionSchemeAction = deletionSchemeActionType
									.makeNewEmbeddedAction((FlexoConceptInstance) selection, getGlobalSelection(), this);
							deletionSchemeAction.setVirtualModelInstance(vmi);
							deletionSchemeAction.doAction();
							vmi.removeFromFlexoConceptInstances(((FlexoConceptInstance) selection));
							selection.delete(context);
							// This is used to notify the deletion of an instance of a flexo concept
							freeModel.getPropertyChangeSupport().firePropertyChange("getInstances(FlexoConcept)", selection, null);
						}
					}
				}
			}
			// TODO Delete concept if no instances??

		} catch (InvalidArgumentException e) {
			e.printStackTrace();
		}

	}

	public FreeModellingProjectNature getFreeModellingProjectNature() {
		return getServiceManager().getProjectNatureService().getProjectNature(FreeModellingProjectNature.class);
	}

	public FreeModellingProject getFreeModellingProject() {
		return getFreeModellingProjectNature().getFreeModellingProject(getEditor().getProject());
	}

	public FreeModel getFreeModel() throws InvalidArgumentException {
		return getFreeModellingProject().getFreeModel((VirtualModelInstance) getFocusedObject().getVirtualModelInstance());
	}
}
