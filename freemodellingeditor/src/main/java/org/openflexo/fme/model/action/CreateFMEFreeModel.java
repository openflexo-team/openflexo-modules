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
import org.openflexo.fme.model.FMEFreeModel;
import org.openflexo.fme.model.FreeModellingProjectNature;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.fml.CreationScheme;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.action.CreateEditionAction;
import org.openflexo.foundation.fml.action.CreateFlexoBehaviour;
import org.openflexo.foundation.fml.action.CreateGenericBehaviourParameter;
import org.openflexo.foundation.fml.action.CreateModelSlot;
import org.openflexo.foundation.fml.action.CreateTopLevelVirtualModel;
import org.openflexo.foundation.fml.editionaction.ExpressionAction;
import org.openflexo.foundation.fml.rm.VirtualModelResource;
import org.openflexo.foundation.fml.rt.FMLRTTechnologyAdapter;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstanceModelSlot;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.toolbox.StringUtils;

/**
 * Abstract implementation for the creation of a {@link FMEFreeModel}
 * 
 * This action is used to create a new {@link FMEFreeModel} in a {@link FreeModellingProjectNature}<br>
 * 
 * @author sylvain
 * 
 */
public abstract class CreateFMEFreeModel<A extends CreateFMEFreeModel<A>> extends FMEAction<A, FreeModellingProjectNature, FlexoObject> {

	private static final Logger logger = Logger.getLogger(CreateFMEFreeModel.class.getPackage().getName());

	private FMEFreeModel freeModel;
	private String freeModelName;
	private String freeModelDescription;

	CreateFMEFreeModel(FlexoActionFactory<A, FreeModellingProjectNature, FlexoObject> actionType, FreeModellingProjectNature focusedObject,
			Vector<FlexoObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	protected void doAction(Object context) throws SaveResourceException {

		logger.info("Create free model");

		// Create FreeMetaModel when not existant
		// Use the same name
		freeModel = createNewFreeModel(getFreeModelName());

		getFocusedObject().addToFreeModels(freeModel);

		getFocusedObject().getOwner().setIsModified();
	}

	protected VirtualModel createVirtualModel(String metaModelName) {
		// Now we create the VirtualModel
		System.out.println("Creating VirtualModel...");
		CreateTopLevelVirtualModel action = CreateTopLevelVirtualModel.actionType
				.makeNewEmbeddedAction(getFocusedObject().getOwner().getVirtualModelRepository().getRootFolder(), null, this);
		action.setNewVirtualModelName(metaModelName);
		action.doAction();
		VirtualModel newVirtualModel = action.getNewVirtualModel();
		System.out.println("VirtualModel has been created: " + newVirtualModel);

		// Now we create the sample data model slot
		CreateModelSlot createMS = CreateModelSlot.actionType.makeNewEmbeddedAction(newVirtualModel, null, this);
		createMS.setTechnologyAdapter(getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(FMLRTTechnologyAdapter.class));
		createMS.setModelSlotClass(FMLRTVirtualModelInstanceModelSlot.class);
		createMS.setModelSlotName(FMEFreeModel.SAMPLE_DATA_MODEL_SLOT_NAME);
		createMS.setVmRes(getConceptualVirtualModelResource());
		createMS.doAction();
		FMLRTVirtualModelInstanceModelSlot sampleDataModelSlot = (FMLRTVirtualModelInstanceModelSlot) createMS.getNewModelSlot();

		// CreationScheme
		CreateFlexoBehaviour createCreationScheme = CreateFlexoBehaviour.actionType.makeNewEmbeddedAction(newVirtualModel, null, this);
		createCreationScheme.setFlexoBehaviourClass(CreationScheme.class);
		createCreationScheme.doAction();
		CreationScheme creationScheme = (CreationScheme) createCreationScheme.getNewFlexoBehaviour();

		CreateGenericBehaviourParameter createSampleDataParameter = CreateGenericBehaviourParameter.actionType
				.makeNewEmbeddedAction(creationScheme, null, this);
		createSampleDataParameter.setParameterName("sampleData");
		createSampleDataParameter.setParameterType(getConceptualVirtualModelResource().getVirtualModel().getInstanceType());

		createSampleDataParameter.doAction();

		CreateEditionAction assignSampleDataAction = CreateEditionAction.actionType.makeNewEmbeddedAction(creationScheme.getControlGraph(),
				null, this);
		assignSampleDataAction.setEditionActionClass(ExpressionAction.class);
		assignSampleDataAction.setAssignation(new DataBinding<>("sampleData"));
		assignSampleDataAction.doAction();
		ExpressionAction<?> assignSampleData = (ExpressionAction<?>) assignSampleDataAction.getBaseEditionAction();
		assignSampleData.setExpression(new DataBinding<>("parameters.sampleData"));

		/*CreateGenericBehaviourParameter createDiagramParameter = CreateGenericBehaviourParameter.actionType
				.makeNewEmbeddedAction(creationScheme, null, this);
		createDiagramParameter.setParameterName("diagram");
		createDiagramParameter.setParameterType(getConceptualVirtualModelResource().getVirtualModel().getInstanceType());
		createDiagramParameter.doAction();*/

		return newVirtualModel;
	}

	protected abstract FMEFreeModel createNewFreeModel(String metaModelName);

	protected VirtualModelResource getConceptualVirtualModelResource() {
		return (VirtualModelResource) getFocusedObject().getConceptualModel().getAccessedVirtualModel().getResource();
	}

	@Override
	public boolean isValid() {

		if (StringUtils.isEmpty(getFreeModelName())) {
			return false;
		}

		if (getFocusedObject() != null && getFocusedObject().getFreeModel(getFreeModelName()) != null) {
			return false;
		}

		return true;
	}

	/**
	 * Return newly created FreeModel
	 * 
	 * @return
	 */
	public FMEFreeModel getNewFreeModel() {
		return freeModel;
	}

	private String defaultFreeModelName = null;

	public String getFreeModelName() {
		if (freeModelName == null) {
			if (defaultFreeModelName == null) {
				String baseName = "FreeModel";
				if (getFocusedObject().getFreeModel(baseName) != null) {
					int i = 2;
					while (getFocusedObject().getFreeModel(baseName + i) != null) {
						i++;
					}
					defaultFreeModelName = baseName + i;
				}
				else {
					defaultFreeModelName = baseName;
				}
			}
			return defaultFreeModelName;
		}
		return freeModelName;
	}

	public void setFreeModelName(String freeModelName) {
		boolean wasValid = isValid();
		this.freeModelName = freeModelName;
		getPropertyChangeSupport().firePropertyChange("freeModelName", null, freeModelName);
		getPropertyChangeSupport().firePropertyChange("isValid", wasValid, isValid());
	}

	public String getFreeModelDescription() {
		return freeModelDescription;
	}

	public void setFreeModelDescription(String freeModelDescription) {
		boolean wasValid = isValid();
		this.freeModelDescription = freeModelDescription;
		getPropertyChangeSupport().firePropertyChange("freeModelDescription", null, freeModelDescription);
		getPropertyChangeSupport().firePropertyChange("isValid", wasValid, isValid());
	}

}
