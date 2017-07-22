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

import org.openflexo.fme.model.FreeMetaModel;
import org.openflexo.fme.model.FreeModel;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.InvalidArgumentException;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.action.ModelSlotInstanceConfiguration.DefaultModelSlotInstanceConfigurationOption;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.localization.LocalizedDelegate;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlotInstanceConfiguration;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelNature;
import org.openflexo.technologyadapter.diagram.fml.action.CreateFMLControlledDiagramVirtualModelInstance;
import org.openflexo.toolbox.StringUtils;

public class AbstractCreateFreeModelDiagram<A extends AbstractCreateFreeModelDiagram<A>>
		extends FlexoAction<A, FreeMetaModel, FlexoObject> {

	private static final Logger logger = Logger.getLogger(AbstractCreateFreeModelDiagram.class.getPackage().getName());

	private FreeModel freeModel;
	private String diagramName;
	private String description;

	AbstractCreateFreeModelDiagram(FlexoActionType<A, FreeMetaModel, FlexoObject> actionType, FreeMetaModel focusedObject,
			Vector<FlexoObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	public LocalizedDelegate getLocales() {
		if (getFocusedObject() != null) {
			return getFocusedObject().getFreeModellingProject().getLocales();
		}
		return super.getLocales();
	}

	@Override
	protected void doAction(Object context) throws SaveResourceException {

		logger.info("Create diagram free model");

		System.out.println("getDiagramName=" + getDiagramName());

		CreateFMLControlledDiagramVirtualModelInstance action = CreateFMLControlledDiagramVirtualModelInstance.actionType
				.makeNewEmbeddedAction(getFocusedObject().getFreeModellingProject().getFreeModellingView(), null, this);
		action.setNewVirtualModelInstanceName(getDiagramName());
		action.setNewVirtualModelInstanceTitle(getDiagramName());
		action.setVirtualModel(getFocusedObject().getVirtualModel());

		TypedDiagramModelSlot diagramModelSlot = FMLControlledDiagramVirtualModelNature
				.getTypedDiagramModelSlot(getFocusedObject().getVirtualModel());
		TypedDiagramModelSlotInstanceConfiguration diagramModelSlotInstanceConfiguration = (TypedDiagramModelSlotInstanceConfiguration) action
				.getModelSlotInstanceConfiguration(diagramModelSlot);
		diagramModelSlotInstanceConfiguration.setOption(DefaultModelSlotInstanceConfigurationOption.CreatePrivateNewModel);
		diagramModelSlotInstanceConfiguration.setFilename(getDiagramName() + ".diagram");
		diagramModelSlotInstanceConfiguration.setRelativePath("Diagrams/");
		diagramModelSlotInstanceConfiguration
				.setModelUri(FreeModel.getDiagramURI(getFocusedObject().getFreeModellingProject().getProject(), getDiagramName()));

		System.out.println("action=" + action);
		System.out.println("action.isValid()=" + action.isValid());

		action.doAction();

		FMLRTVirtualModelInstance newVirtualModelInstance = action.getNewVirtualModelInstance();

		// Wrap into FreeModel
		try {
			freeModel = getFocusedObject().getFreeModellingProject().getFreeModel(newVirtualModelInstance);
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
		}

		getFocusedObject().getFreeModellingProject().getPropertyChangeSupport().firePropertyChange("freeMetaModels", null, null);

	}

	@Override
	public boolean isValid() {

		if (StringUtils.isEmpty(diagramName)) {
			return false;
		}

		if (getFocusedObject().getFreeModellingProject().getFreeModel(diagramName) != null) {
			return false;
		}

		return true;
	}

	/**
	 * Return newly created FreeModel
	 * 
	 * @return
	 */
	public FreeModel getFreeModel() {
		return freeModel;
	}

	public String getDiagramName() {
		return diagramName;
	}

	public void setDiagramName(String diagramName) {
		boolean wasValid = isValid();
		this.diagramName = diagramName;
		getPropertyChangeSupport().firePropertyChange("diagramName", null, diagramName);
		getPropertyChangeSupport().firePropertyChange("isValid", wasValid, isValid());
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		boolean wasValid = isValid();
		this.description = description;
		getPropertyChangeSupport().firePropertyChange("description", null, description);
		getPropertyChangeSupport().firePropertyChange("isValid", wasValid, isValid());
	}

}
