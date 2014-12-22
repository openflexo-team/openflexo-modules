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
package org.openflexo.fme.model.action;

import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.fme.model.FreeModellingProjectNature;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.InvalidArgumentException;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.fml.action.CreateViewPoint;
import org.openflexo.foundation.fml.rm.ViewPointResource;
import org.openflexo.foundation.fmlrt.action.CreateView;
import org.openflexo.foundation.fmlrt.rm.ViewResource;
import org.openflexo.foundation.resource.InvalidFileNameException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.rm.DiagramSpecificationRepository;

/**
 * This action is called to gives a new FME nature to a project
 * 
 * @author vincent
 */
public class GivesFMENature extends FlexoAction<GivesFMENature, FlexoProject, FlexoObject> {

	private static final Logger logger = Logger.getLogger(GivesFMENature.class.getPackage().getName());

	public static FlexoActionType<GivesFMENature, FlexoProject, FlexoObject> actionType = new FlexoActionType<GivesFMENature, FlexoProject, FlexoObject>("gives_fme_nature") {

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
	protected void doAction(Object context) throws InvalidFileNameException, SaveResourceException, InvalidArgumentException {
		ViewPointResource freeModellingViewPointResource = getFocusedObject().getViewPointRepository().getResource(
				getFocusedObject().getURI() + FreeModellingProjectNature.FREE_MODELLING_VIEWPOINT_RELATIVE_URI);

		// Since CreateViewpoint/View are LongRunning actions, we call them as embedded actions, therefore we are ensure that Viewpoint and View are created after doAction() call.
		if (freeModellingViewPointResource == null) {
			CreateViewPoint action = CreateViewPoint.actionType.makeNewEmbeddedAction(getFocusedObject().getViewPointRepository().getRootFolder(), null,this);
			action.setNewViewPointName(FreeModellingProjectNature.FREE_MODELLING_VIEWPOINT_NAME);
			action.setNewViewPointURI(getFocusedObject().getURI() + FreeModellingProjectNature.FREE_MODELLING_VIEWPOINT_RELATIVE_URI);
			action.setNewViewPointDescription("This is the generic ViewPoint storing all FreeModelling meta-models");
			action.doAction();
			freeModellingViewPointResource = (ViewPointResource) action.getNewViewPoint().getResource();
		}

		ViewResource freeModellingViewResource = getFocusedObject().getViewLibrary().getResource(
				getFocusedObject().getURI() + FreeModellingProjectNature.FREE_MODELLING_VIEWPOINT_RELATIVE_URI);

		if (freeModellingViewResource == null) {
			CreateView action = CreateView.actionType.makeNewEmbeddedAction(getFocusedObject().getViewLibrary().getRootFolder(), null, this);
			action.setNewViewName(FreeModellingProjectNature.FREE_MODELLING_VIEW_NAME);
			action.setNewViewTitle(FreeModellingProjectNature.FREE_MODELLING_VIEW_NAME);
			action.setViewpointResource(freeModellingViewPointResource);
			action.doAction();
			freeModellingViewResource = (ViewResource) action.getNewView().getResource();
		}

		DiagramTechnologyAdapter diagramTechnologyAdapter = getFocusedObject().getServiceManager().getTechnologyAdapterService()
				.getTechnologyAdapter(DiagramTechnologyAdapter.class);
		DiagramSpecificationRepository dsRepository = getFocusedObject().getRepository(DiagramSpecificationRepository.class, diagramTechnologyAdapter);
		dsRepository.createNewFolder(FreeModellingProjectNature.DIAGRAM_SPECIFICATIONS_FOLDER);
	}

	@Override
	public boolean isValid() {
		return true;
	}

}