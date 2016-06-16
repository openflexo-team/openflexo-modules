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

package org.openflexo.fme.controller.action;

import java.awt.Image;
import java.util.logging.Logger;

import org.openflexo.fme.model.action.CreateFreeModelDiagramFromPPT;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.technologyadapter.powerpoint.controller.ChoosePPTSlide;
import org.openflexo.view.controller.FlexoController;

public class CreateFreeModelDiagramFromPPTWizard extends AbstractCreateFreeModelDiagramWizard<CreateFreeModelDiagramFromPPT> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CreateFreeModelDiagramFromPPTWizard.class.getPackage().getName());

	private ChoosePPTSlide choosePPTSlide;

	public CreateFreeModelDiagramFromPPTWizard(CreateFreeModelDiagramFromPPT action, FlexoController controller) {
		super(action, controller);
		addStep(choosePPTSlide = new ChoosePPTSlide(controller) {
			@Override
			public void done() {
				super.done();
				getAction().setDiagramName(getDiagramName());
				getAction().setDiagramTitle(getDiagramTitle());
				getAction().setFile(getFile());
				getAction().setSlide(getSlide());
			}
		});
	}

	@Override
	protected void freeModelDiagramHasBeenDescribed() {
		super.freeModelDiagramHasBeenDescribed();
		choosePPTSlide.setDiagramName(getDescribeFreeModel().getDiagramName());
	}

	@Override
	public String getWizardTitle() {
		return getAction().getLocales().localizedForKey("create_free_model_diagram_based_on_a_powerpoint_slide");
	}

	@Override
	public Image getDefaultPageImage() {
		return IconFactory.getImageIcon(DiagramIconLibrary.DIAGRAM_BIG_ICON, IconLibrary.NEW_32_32).getImage();
	}

}
