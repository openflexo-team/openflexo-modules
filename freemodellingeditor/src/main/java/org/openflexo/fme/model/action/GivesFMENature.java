/**
 * 
 * Copyright (c) 2014-2015, Openflexo
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
import org.openflexo.fme.model.FreeModellingProjectNature;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.InvalidArgumentException;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.fml.action.CreateTopLevelVirtualModel;
import org.openflexo.foundation.fml.rm.VirtualModelResource;
import org.openflexo.foundation.fml.rt.action.CreateBasicVirtualModelInstance;
import org.openflexo.foundation.fml.rt.rm.FMLRTVirtualModelInstanceResource;
import org.openflexo.foundation.resource.InvalidFileNameException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.localization.LocalizedDelegate;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.rm.DiagramSpecificationRepository;

/**
 * This action is called to gives a new FME nature to a project
 * 
 * @author vincent
 */
public class GivesFMENature extends FlexoAction<GivesFMENature, FlexoProject, FlexoObject> {

	private static final Logger logger = Logger.getLogger(GivesFMENature.class.getPackage().getName());

	public static FlexoActionFactory<GivesFMENature, FlexoProject, FlexoObject> actionType = new FlexoActionFactory<GivesFMENature, FlexoProject, FlexoObject>(
			"gives_fme_nature") {

		/**
		 * Factory method
		 */
		@Override
		public GivesFMENature makeNewAction(FlexoProject focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
			return new GivesFMENature(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(FlexoProject object, Vector<FlexoObject> globalSelection) {
			return false;
		}

		@Override
		public boolean isEnabledForSelection(FlexoProject object, Vector<FlexoObject> globalSelection) {
			return object != null;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(GivesFMENature.actionType, FlexoProject.class);
	}

	GivesFMENature(FlexoProject focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
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
	protected void doAction(Object context) throws InvalidFileNameException, SaveResourceException, InvalidArgumentException {
		if (getFocusedObject().getVirtualModelRepository() == null) {
			logger.warning("Could not determine VirtualModelRepository. Aborting operation.");
			throw new InvalidArgumentException("Could not determine VirtualModelRepository. Aborting operation.");
		}

		VirtualModelResource freeModellingViewPointResource = getFocusedObject().getVirtualModelRepository()
				.getResource(getFocusedObject().getURI() + FreeModellingProjectNature.FREE_MODELLING_VIEWPOINT_RELATIVE_URI);

		// Since CreateViewpoint/View are LongRunning actions, we call them as embedded actions, therefore we are ensure that Viewpoint and
		// View are created after doAction() call.
		if (freeModellingViewPointResource == null) {
			CreateTopLevelVirtualModel action = CreateTopLevelVirtualModel.actionType
					.makeNewEmbeddedAction(getFocusedObject().getVirtualModelRepository().getRootFolder(), null, this);
			action.setNewVirtualModelName(FreeModellingProjectNature.FREE_MODELLING_VIEWPOINT_NAME);
			action.setNewVirtualModelURI(getFocusedObject().getURI() + FreeModellingProjectNature.FREE_MODELLING_VIEWPOINT_RELATIVE_URI);
			action.setNewVirtualModelDescription("This is the generic ViewPoint storing all FreeModelling meta-models");
			action.doAction();
			freeModellingViewPointResource = (VirtualModelResource) action.getNewVirtualModel().getResource();
		}

		FMLRTVirtualModelInstanceResource freeModellingViewResource = getFocusedObject().getVirtualModelInstanceRepository()
				.getResource(getFocusedObject().getURI() + FreeModellingProjectNature.FREE_MODELLING_VIEW_RELATIVE_URI);

		if (freeModellingViewResource == null) {
			CreateBasicVirtualModelInstance action = CreateBasicVirtualModelInstance.actionType
					.makeNewEmbeddedAction(getFocusedObject().getVirtualModelInstanceRepository().getRootFolder(), null, this);
			action.setNewVirtualModelInstanceName(FreeModellingProjectNature.FREE_MODELLING_VIEW_NAME);
			action.setNewVirtualModelInstanceTitle(FreeModellingProjectNature.FREE_MODELLING_VIEW_NAME);
			action.setVirtualModel(freeModellingViewPointResource.getVirtualModel());
			action.doAction();
			freeModellingViewResource = (FMLRTVirtualModelInstanceResource) action.getNewVirtualModelInstance().getResource();
		}

		DiagramTechnologyAdapter diagramTechnologyAdapter = getFocusedObject().getServiceManager().getTechnologyAdapterService()
				.getTechnologyAdapter(DiagramTechnologyAdapter.class);
		DiagramSpecificationRepository<?> dsRepository = diagramTechnologyAdapter.getDiagramSpecificationRepository(getFocusedObject());
		dsRepository.createNewFolder(FreeModellingProjectNature.DIAGRAM_SPECIFICATIONS_FOLDER);

		// We have now to notify project of nature modifications
		getFocusedObject().getPropertyChangeSupport().firePropertyChange("asNature(String)", false, true);
		getFocusedObject().getPropertyChangeSupport().firePropertyChange("hasNature(String)", false, true);
	}

	@Override
	public boolean isValid() {
		return true;
	}

}
