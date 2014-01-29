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
package org.openflexo.vpm.controller;

import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

import org.openflexo.FlexoCst;
import org.openflexo.components.widget.FIBInformationSpaceBrowser;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.ontology.IFlexoOntologyObject;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.icon.VPMIconLibrary;
import org.openflexo.view.EmptyPanel;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.TechnologyAdapterController;
import org.openflexo.view.controller.model.FlexoPerspective;

public class InformationSpacePerspective extends FlexoPerspective {

	static final Logger logger = Logger.getLogger(InformationSpacePerspective.class.getPackage().getName());

	private final JLabel infoLabel;

	private final FIBInformationSpaceBrowser informationSpaceBrowser;

	/**
	 * @param controller
	 * @param name
	 */
	public InformationSpacePerspective(FlexoController controller/*VPMController controller*/) {
		super("information_space_perspective");

		informationSpaceBrowser = new FIBInformationSpaceBrowser(controller.getApplicationContext().getInformationSpace(), controller);

		setTopLeftView(informationSpaceBrowser);

		infoLabel = new JLabel("Information space perspective");
		infoLabel.setFont(FlexoCst.SMALL_FONT);
	}

	/**
	 * Overrides getIcon
	 * 
	 * @see org.openflexo.view.controller.model.FlexoPerspective#getActiveIcon()
	 */
	@Override
	public ImageIcon getActiveIcon() {
		return VPMIconLibrary.INFORMATION_SPACE_ICON;
	}

	@Override
	public FlexoObject getDefaultObject(FlexoObject proposedObject, FlexoController controller) {
		if (hasModuleViewForObject(proposedObject, controller)) {
			return proposedObject;
		}
		// return _controller.getBaseOntologyLibrary();
		return null;
	}

	@Override
	public boolean hasModuleViewForObject(FlexoObject object, FlexoController controller) {
		if (object instanceof TechnologyObject) {
			TechnologyAdapter ta = ((TechnologyObject) object).getTechnologyAdapter();
			TechnologyAdapterController<?> tac = controller.getApplicationContext().getTechnologyAdapterControllerService()
					.getTechnologyAdapterController(ta);
			return tac.hasModuleViewForObject((TechnologyObject) object, controller);
		}
		return false;
	}

	@Override
	public ModuleView<?> createModuleViewForObject(FlexoObject object, FlexoController controller) {
		if (object instanceof TechnologyObject) {
			return createModuleViewForTechnologyObject((TechnologyObject<?>) object, controller);
		}
		return new EmptyPanel<FlexoObject>(controller, this, object);
	}

	public <TA extends TechnologyAdapter> ModuleView<?> createModuleViewForTechnologyObject(TechnologyObject<TA> object,
			FlexoController controller) {
		TA ta = object.getTechnologyAdapter();
		TechnologyAdapterController<TA> tac = controller.getApplicationContext().getTechnologyAdapterControllerService()
				.getTechnologyAdapterController(ta);
		return tac.createModuleViewForObject(object, controller, this);
	}

	@Override
	public JComponent getFooter() {
		return infoLabel;
	}

	public String getWindowTitleforObject(FlexoObject object, FlexoController controller) {
		if (object instanceof TechnologyObject) {
			TechnologyAdapter ta = ((TechnologyObject) object).getTechnologyAdapter();
			TechnologyAdapterController<?> tac = controller.getApplicationContext().getTechnologyAdapterControllerService()
					.getTechnologyAdapterController(ta);
			return tac.getWindowTitleforObject((TechnologyObject) object, controller);
		}
		if (object instanceof IFlexoOntologyObject) {
			return ((IFlexoOntologyObject) object).getName();
		}
		if (object != null) {
			return object.toString();
		}
		logger.warning("Unexpected null object here");
		return null;
	}

}
