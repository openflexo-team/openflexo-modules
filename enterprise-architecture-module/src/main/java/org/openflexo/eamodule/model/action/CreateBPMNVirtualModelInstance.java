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

package org.openflexo.eamodule.model.action;

import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.eamodule.model.BPMNVirtualModelInstance;
import org.openflexo.eamodule.model.EAProjectNature;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.fml.CreationScheme;
import org.openflexo.foundation.fml.rm.CompilationUnitResource;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.action.CreateBasicVirtualModelInstance;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.modelers.ModelersConstants;
import org.openflexo.toolbox.StringUtils;

/**
 * Action used to instanciate new BPMN modelling
 * 
 * This action is used to create a new {@link FMLRTVirtualModelInstance} in a {@link EAProjectNature}<br>
 * 
 * @author sylvain
 * 
 */
public class CreateBPMNVirtualModelInstance extends EAMAction<CreateBPMNVirtualModelInstance, EAProjectNature, FlexoObject> {

	private static final Logger logger = Logger.getLogger(CreateBPMNVirtualModelInstance.class.getPackage().getName());

	public static FlexoActionFactory<CreateBPMNVirtualModelInstance, EAProjectNature, FlexoObject> actionType = new FlexoActionFactory<CreateBPMNVirtualModelInstance, EAProjectNature, FlexoObject>(
			"start_bpmn_process_modelling", FlexoActionFactory.defaultGroup, FlexoActionFactory.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateBPMNVirtualModelInstance makeNewAction(EAProjectNature focusedObject, Vector<FlexoObject> globalSelection,
				FlexoEditor editor) {
			return new CreateBPMNVirtualModelInstance(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(EAProjectNature object, Vector<FlexoObject> globalSelection) {
			return true;
		}

		@Override
		public boolean isEnabledForSelection(EAProjectNature object, Vector<FlexoObject> globalSelection) {
			return true;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(CreateBPMNVirtualModelInstance.actionType, EAProjectNature.class);
	}

	private CreateBPMNVirtualModelInstance(EAProjectNature focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	private BPMNVirtualModelInstance bpmnVirtualModelInstance;
	private String bpmnModelName;
	private String bpmnModelDescription;

	@Override
	protected void doAction(Object context) throws SaveResourceException {

		logger.info("Create BPMN model");

		System.out.println("BPMN VM = " + getBPMNVirtualModelResource());

		// Create FreeMetaModel when not existant
		// Use the same name
		// freeModel = createNewFreeModel(getFreeModelName());
		// getFocusedObject().addToFreeModels(freeModel);

		CreateBasicVirtualModelInstance action = CreateBasicVirtualModelInstance.actionType
				.makeNewEmbeddedAction(getFocusedObject().getOwner().getVirtualModelInstanceRepository().getRootFolder(), null, this);
		action.setNewVirtualModelInstanceName(getBPMNModelName());
		action.setNewVirtualModelInstanceTitle(getBPMNModelName());
		action.setVirtualModel(getBPMNVirtualModelResource().getCompilationUnit().getVirtualModel());
		CreationScheme creationScheme = getBPMNVirtualModelResource().getCompilationUnit().getVirtualModel().getCreationSchemes().get(0);
		action.setCreationScheme(creationScheme);
		action.doAction();

		FMLRTVirtualModelInstance newVMI = action.getNewVirtualModelInstance();

		bpmnVirtualModelInstance = getFocusedObject().getOwner().getModelFactory().newInstance(BPMNVirtualModelInstance.class);
		bpmnVirtualModelInstance.setAccessedVirtualModelInstance(newVMI);
		getFocusedObject().setBPMNVirtualModelInstance(bpmnVirtualModelInstance);

	}

	public CompilationUnitResource getBPMNVirtualModelResource() {
		return getServiceManager().getVirtualModelLibrary().getCompilationUnitResource(ModelersConstants.BPMN_EDITOR_URI);
	}

	@Override
	public boolean isValid() {

		if (StringUtils.isEmpty(getBPMNModelName())) {
			return false;
		}

		if (getFocusedObject() != null && getFocusedObject().getProject().getVirtualModelInstanceRepository()
				.getVirtualModelInstanceResourceNamed(getBPMNModelName()) != null) {
			return false;
		}

		return true;
	}

	private String defaultBPMNModelName = null;

	public String getBPMNModelName() {
		if (bpmnModelName == null) {
			if (defaultBPMNModelName == null) {
				String baseName = "BPMN";
				if (getFocusedObject().getProject().getVirtualModelInstanceRepository()
						.getVirtualModelInstanceResourceNamed(baseName) != null) {
					int i = 2;
					while (getFocusedObject().getProject().getVirtualModelInstanceRepository()
							.getVirtualModelInstanceResourceNamed(baseName + i) != null) {
						i++;
					}
					defaultBPMNModelName = baseName + i;
				}
				else {
					defaultBPMNModelName = baseName;
				}
			}
			return defaultBPMNModelName;
		}
		return bpmnModelName;
	}

	public void setBPMNModelName(String bpmnModelName) {
		boolean wasValid = isValid();
		this.bpmnModelName = bpmnModelName;
		getPropertyChangeSupport().firePropertyChange("BPMNModelName", null, bpmnModelName);
		getPropertyChangeSupport().firePropertyChange("isValid", wasValid, isValid());
	}

	public String getBPMNModelDescription() {
		return bpmnModelDescription;
	}

	public void setBPMNModelDescription(String bpmnModelDescription) {
		boolean wasValid = isValid();
		this.bpmnModelDescription = bpmnModelDescription;
		getPropertyChangeSupport().firePropertyChange("BPMNModelDescription", null, bpmnModelDescription);
		getPropertyChangeSupport().firePropertyChange("isValid", wasValid, isValid());
	}

	public BPMNVirtualModelInstance getBPMNVirtualModelInstance() {
		return bpmnVirtualModelInstance;
	}

}
