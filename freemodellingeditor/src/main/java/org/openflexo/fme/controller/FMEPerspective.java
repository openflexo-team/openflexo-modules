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

package org.openflexo.fme.controller;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.fme.FMEIconLibrary;
import org.openflexo.fme.model.FreeMetaModel;
import org.openflexo.fme.model.FreeModel;
import org.openflexo.fme.view.FreeModelModuleView;
import org.openflexo.fme.widget.FIBConceptBrowser;
import org.openflexo.fme.widget.FIBFreeModellingProjectBrowser;
import org.openflexo.fme.widget.FIBRepresentedConceptBrowser;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.model.undo.CompoundEdit;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

public class FMEPerspective extends FlexoPerspective {

	protected static final Logger logger = Logger.getLogger(FMEPerspective.class.getPackage().getName());

	private FIBFreeModellingProjectBrowser freeModellingProjectBrowser = null;
	private final FIBRepresentedConceptBrowser representedConceptBrowser;
	private final FIBConceptBrowser conceptBrowser;

	/**
	 * Default constructor taking controller as argument
	 */
	public FMEPerspective(FMEController controller) {
		super("free_modelling_perspective", controller);
		// _controller = controller;

		freeModellingProjectBrowser = new FIBFreeModellingProjectBrowser(controller.getProject(), controller);

		/*viewPointBrowser = new FIBViewPointBrowser(null, controller);
		virtualModelBrowser = new FIBVirtualModelBrowser(null, controller);
		exampleDiagramBrowser = new FIBExampleDiagramBrowser(null, controller);
		diagramPaletteBrowser = new FIBDiagramPaletteBrowser(null, controller);*/

		setTopLeftView(freeModellingProjectBrowser);

		/*MultiSplitLayoutFactory factory = new MultiSplitLayoutFactory.DefaultMultiSplitLayoutFactory();
		
		Leaf top = factory.makeLeaf("top");
		Leaf bottom = factory.makeLeaf("bottom");
		Split root = factory.makeRowSplit(top, factory.makeDivider(), bottom);
		final MultiSplitLayout layout = new MultiSplitLayout(factory);
		layout.setModel(root);
		layout.setLayoutByWeight(false);
		layout.setFloatingDividers(false);
		JXMultiSplitPane splitPane = new JXMultiSplitPane(layout);*/

		representedConceptBrowser = new FIBRepresentedConceptBrowser(null, controller);
		conceptBrowser = new FIBConceptBrowser(null, controller);

		// splitPane.add(representedConceptBrowser, "top");
		// splitPane.add(conceptBrowser, "bottom");

	}

	public ModuleView<?> getCurrentModuleView(FlexoController controller) {
		return controller.getCurrentModuleView();
	}

	/**
	 * Overrides getIcon
	 * 
	 * @see org.openflexo.view.controller.model.FlexoPerspective#getActiveIcon()
	 */
	@Override
	public ImageIcon getActiveIcon() {
		return FMEIconLibrary.FME_SMALL_ICON;
	}

	@Override
	public String getWindowTitleforObject(FlexoObject object, FlexoController controller) {
		if (object instanceof FreeModel) {
			return ((FreeModel) object).getName();
		}
		if (object instanceof FreeMetaModel) {
			return ((FreeMetaModel) object).getName();
		}
		if (object != null) {
			return object.toString();
		}
		return "null";
	}

	public void focusOnFreeModel(FreeModel freeModel) {
		logger.info("focusOnFreeModel " + freeModel);

		representedConceptBrowser.setFreeModel(freeModel);
		conceptBrowser.setFreeModel(freeModel);

		setMiddleLeftView(representedConceptBrowser);
		setBottomLeftView(conceptBrowser);

	}

	@Override
	public void focusOnObject(FlexoObject object) {
		if (object instanceof FreeModel) {
			focusOnFreeModel((FreeModel) object);
		}
	};

	@Override
	public void objectWasClicked(Object object, FlexoController controller) {
		// logger.info("ViewPointPerspective: object was clicked: " + object);
		if (object == null) {
			return;
		}
	}

	@Override
	public void objectWasRightClicked(Object object, FlexoController controller) {
		// logger.info("ViewPointPerspective: object was right-clicked: " + object);
	}

	@Override
	public void objectWasDoubleClicked(Object object, FlexoController controller) {
		// logger.info("ViewPointPerspective: object was double-clicked: " + object);
		if (object instanceof FreeModel) {
			controller.selectAndFocusObject((FreeModel) object);
		}
		else {
			super.objectWasDoubleClicked(object, controller);
		}
	}

	public void setProject(FlexoProject project) {
		freeModellingProjectBrowser.setRootObject(project);
	}

	@Override
	public boolean hasModuleViewForObject(FlexoObject object) {
		if (object instanceof FreeModel) {
			return true;
		}
		return super.hasModuleViewForObject(object);
	}

	public void closeFreeModelBrowsers() {
		setMiddleLeftView(null);
		setBottomLeftView(null);
	}

	@Override
	public ModuleView<?> createModuleViewForObject(FlexoObject object, boolean editable) {
		if (object instanceof FreeModel) {
			// Initialization of Diagram representation may rise PAMELA edits
			// The goal is here to embed all those edits in a special edit record
			// Which is to be discarded as undoable action at the end of this initialization
			CompoundEdit edit = null;
			if (getController().getEditor().getUndoManager() != null) {
				edit = getController().getEditor().getUndoManager().startRecording("Initialize free diagram");
			}
			if (edit != null) {
				getController().getEditor().getUndoManager().stopRecording(edit);
				// Make this edit not-undoable
				edit.die();
			}
			return new FreeModelModuleView(getController(), (FreeModel) object, this);
		}
		return super.createModuleViewForObject(object, editable);
	}
}
