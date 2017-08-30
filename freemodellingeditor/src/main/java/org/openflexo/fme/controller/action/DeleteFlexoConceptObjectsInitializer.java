/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Fml-technologyadapter-ui, a component of the software infrastructure 
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

package org.openflexo.fme.controller.action;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.KeyStroke;

import org.openflexo.fme.model.FreeMetaModel;
import org.openflexo.fme.model.FreeModel;
import org.openflexo.fme.model.FreeModellingProject;
import org.openflexo.fme.model.FreeModellingProjectNature;
import org.openflexo.foundation.InvalidArgumentException;
import org.openflexo.foundation.action.FlexoActionFinalizer;
import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.FlexoConceptObject;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.action.DeleteFlexoConceptObjects;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.icon.IconLibrary;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;

public class DeleteFlexoConceptObjectsInitializer
		extends ActionInitializer<DeleteFlexoConceptObjects, FlexoConceptObject, FlexoConceptObject> {

	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	public DeleteFlexoConceptObjectsInitializer(ControllerActionInitializer actionInitializer) {
		super(DeleteFlexoConceptObjects.actionType, actionInitializer);
	}

	@Override
	protected FlexoActionInitializer<DeleteFlexoConceptObjects> getDefaultInitializer() {
		return new FlexoActionInitializer<DeleteFlexoConceptObjects>() {
			@Override
			public boolean run(EventObject e, DeleteFlexoConceptObjects action) {

				try {
					if (action.getGlobalSelection().size() >= 1) {
						if (action.getFocusedObject() instanceof FlexoConcept) {
							FlexoConcept concept = (FlexoConcept) action.getFocusedObject();
							VirtualModel vm = concept.getOwner();
							FreeModellingProjectNature fmeNature = action.getServiceManager().getProjectNatureService()
									.getProjectNature(FreeModellingProjectNature.class);
							FreeModellingProject fmeProject = getProject().asNature(fmeNature);
							FreeMetaModel freeMM = fmeProject.getFreeMetaModel((VirtualModel) vm);
							if (freeMM == null) {
								// this concept belongs to a VirtualModel which is not a FreeMetaModel
								// abort
								return false;
							}
							if (concept == freeMM.getNoneFlexoConcept(getEditor(), action)) {
								FlexoController.showError(getModuleLocales(action).localizedForKey("none_concept_cannot_be_deleted"));
								return false;
							}
							// Lets see if there are instances of this concept in the project
							List<FlexoConceptInstance> fciToBeDeleted = new ArrayList<>();
							for (FreeModel freeModel : fmeProject.getFreeModels()) {
								fciToBeDeleted.addAll(freeModel.getInstances(concept));
							}

							if (fciToBeDeleted.size() > 0) {
								if (FlexoController.confirm(getModuleLocales(action).localizedForKeyWithParams(
										"would_you_really_like_to_delete_this_concept_and_all_existing_instances_of_that_concept_(($0)_instances_found)_?",
										fciToBeDeleted.size()))) {
									for (FlexoConceptInstance fci : fciToBeDeleted) {
										fci.delete();
									}
									return true;
								}
								return false;
							}

							else {
								return FlexoController.confirm(
										getModuleLocales(action).localizedForKey("would_you_really_like_to_delete_this_concept_?"));
							}
						}
					}
				} catch (InvalidArgumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					return false;
				}

				return false;
			}
		};
	}

	@Override
	protected FlexoActionFinalizer<DeleteFlexoConceptObjects> getDefaultFinalizer() {
		return new FlexoActionFinalizer<DeleteFlexoConceptObjects>() {
			@Override
			public boolean run(EventObject e, DeleteFlexoConceptObjects action) {
				if (action.getGlobalSelection().size() >= 1) {
					if (action.getFocusedObject() instanceof FlexoConcept) {
						// FlexoConcept concept = (FlexoConcept) action.getFocusedObject();
						FreeModellingProjectNature fmeNature = action.getServiceManager().getProjectNatureService()
								.getProjectNature(FreeModellingProjectNature.class);
						FreeModellingProject fmeProject = getProject().asNature(fmeNature);
						// This concept has been deleted
						for (FreeModel freeModel : fmeProject.getFreeModels()) {
							freeModel.getPropertyChangeSupport().firePropertyChange("usedFlexoConcepts", null,
									freeModel.getUsedFlexoConcepts());
						}
						return true;
					}
				}
				return true;
			}
		};
	}

	@Override
	protected Icon getEnabledIcon(FlexoActionFactory actionType) {
		return IconLibrary.DELETE_ICON;
	}

	@Override
	protected KeyStroke getShortcut() {
		return KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
	}
}
