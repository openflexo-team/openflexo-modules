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
package org.openflexo.ve.controller;

import java.awt.Dimension;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.openflexo.components.widget.FIBViewLibraryBrowser;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.FlexoProjectObject;
import org.openflexo.foundation.view.View;
import org.openflexo.foundation.view.ViewLibrary;
import org.openflexo.foundation.view.VirtualModelInstance;
import org.openflexo.icon.VEIconLibrary;
import org.openflexo.inspector.FIBInspectorPanel;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.ve.view.VirtualModelInstanceView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

public class ViewLibraryPerspective extends FlexoPerspective {

	protected static final Logger logger = Logger.getLogger(ViewLibraryPerspective.class.getPackage().getName());

	private final FIBViewLibraryBrowser viewLibraryBrowser;

	private final JPanel EMPTY_RIGHT_VIEW = new JPanel();

	private final FIBInspectorPanel inspectorPanel;

	private final JComponent bottomRightDummy;

	/**
	 * @param controller
	 *            TODO
	 * @param name
	 */
	public ViewLibraryPerspective(VEController controller) {
		super("view_library_perspective", controller);

		viewLibraryBrowser = new FIBViewLibraryBrowser(controller.getProject() != null ? controller.getProject().getViewLibrary() : null,
				controller);

		setTopLeftView(viewLibraryBrowser);

		EMPTY_RIGHT_VIEW.setPreferredSize(new Dimension(300, 300));
		bottomRightDummy = new JPanel();

		// Initialized inspector panel
		inspectorPanel = new FIBInspectorPanel(controller.getModuleInspectorController());
	}

	/**
	 * Overrides getIcon
	 * 
	 * @see org.openflexo.view.controller.model.FlexoPerspective#getActiveIcon()
	 */
	@Override
	public ImageIcon getActiveIcon() {
		return VEIconLibrary.VIEW_ICON;
	}

	@Override
	public FlexoObject getDefaultObject(FlexoObject proposedObject) {
		if (proposedObject instanceof View) {
			return proposedObject;
		}
		if (proposedObject instanceof FlexoProjectObject) {
			return ((FlexoProjectObject) proposedObject).getProject().getViewLibrary();
		} else {
			return null;
		}
	}

	@Override
	public boolean hasModuleViewForObject(FlexoObject object) {
		if (super.hasModuleViewForObject(object)) {
			return true;
		}
		return false;
	}

	public VirtualModelInstanceView getCurrentVirtualModelInstanceView() {
		if (getController() != null && getController().getCurrentModuleView() instanceof VirtualModelInstanceView) {
			return (VirtualModelInstanceView) getController().getCurrentModuleView();
		}
		return null;
	}

	@Override
	public String getWindowTitleforObject(FlexoObject object, FlexoController controller) {
		if (object == null) {
			return FlexoLocalization.localizedForKey("no_selection");
		}
		if (object instanceof ViewLibrary) {
			return FlexoLocalization.localizedForKey("view_library");
		}
		if (object instanceof VirtualModelInstance) {
			return ((VirtualModelInstance) object).getTitle();
		}
		if (object instanceof View) {
			return ((View) object).getName();
		}
		return object.toString();
	}

	public void setProject(FlexoProject project) {
		viewLibraryBrowser.setRootObject(project != null ? project.getViewLibrary() : null);
	}

}
