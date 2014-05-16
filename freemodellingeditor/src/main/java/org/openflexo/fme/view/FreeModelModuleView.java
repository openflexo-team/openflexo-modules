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
package org.openflexo.fme.view;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.openflexo.fme.controller.FMEPerspective;
import org.openflexo.fme.controller.editor.FreeModelDiagramEditor;
import org.openflexo.fme.model.FreeModel;
import org.openflexo.technologyadapter.diagram.controller.DiagramTechnologyAdapterController;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.TechnologyAdapterControllerService;
import org.openflexo.view.controller.model.FlexoPerspective;

@SuppressWarnings("serial")
public class FreeModelModuleView extends JPanel implements ModuleView<FreeModel>, PropertyChangeListener {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(FreeModelModuleView.class.getPackage().getName());

	private final FreeModelDiagramEditor editor;
	private final FlexoPerspective perspective;

	private final JPanel bottomPanel;

	public FreeModelModuleView(FreeModelDiagramEditor editor, FMEPerspective perspective) {
		super();
		setLayout(new BorderLayout());
		this.editor = editor;
		this.perspective = perspective;
		add(editor.getToolsPanel(), BorderLayout.NORTH);
		add(new JScrollPane(editor.getDrawingView()), BorderLayout.CENTER);

		bottomPanel = new JPanel(new BorderLayout());

		bottomPanel.add(editor.getFlexoController().makeInfoLabel(), BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);

		editor.getFlexoController().setInfoMessage("Free Modelling Editor - CTRL-drag to draw edges", false);

		validate();

		getRepresentedObject().getPropertyChangeSupport().addPropertyChangeListener(getRepresentedObject().getDeletedProperty(), this);
	}

	public FreeModelDiagramEditor getEditor() {
		return editor;
	}

	@Override
	public FlexoPerspective getPerspective() {
		return perspective;
	}

	@Override
	public void deleteModuleView() {
		getRepresentedObject().getPropertyChangeSupport().removePropertyChangeListener(getRepresentedObject().getDeletedProperty(), this);
		getEditor().getFlexoController().removeModuleView(this);
		getEditor().delete();
	}

	@Override
	public FreeModel getRepresentedObject() {
		return editor.getFreeModel();
	}

	@Override
	public boolean isAutoscrolled() {
		return true;
	}

	@Override
	public void willHide() {

		System.out.println("FreeModelModuleView WILL HIDE !!!!!!");

		getEditor().getFlexoController().getEditingContext().unregisterPasteHandler(getEditor().getPasteHandler());

		bottomPanel.remove(getDiagramTechnologyAdapterController(getEditor().getFlexoController()).getScaleSelector().getComponent());
	}

	@Override
	public void willShow() {

		System.out.println("FreeModelModuleView WILL SHOW !!!!!!");

		getEditor().getFlexoController().getEditingContext().registerPasteHandler(getEditor().getPasteHandler());

		bottomPanel.add(getDiagramTechnologyAdapterController(getEditor().getFlexoController()).getScaleSelector().getComponent(),
				BorderLayout.EAST);

		getPerspective().focusOnObject(getRepresentedObject());
	}

	public DiagramTechnologyAdapterController getDiagramTechnologyAdapterController(FlexoController controller) {
		TechnologyAdapterControllerService tacService = controller.getApplicationContext().getTechnologyAdapterControllerService();
		return tacService.getTechnologyAdapterController(DiagramTechnologyAdapterController.class);
	}

	@Override
	public void show(final FlexoController controller, FlexoPerspective perspective) {

		// Sets palette view of editor to be the top right view
		perspective.setTopRightView(getEditor().getPaletteView());
		// perspective.setHeader(((FreeDiagramModuleView) moduleView).getEditor().getS());

		getDiagramTechnologyAdapterController(controller).getInspectors().attachToEditor(getEditor());
		getDiagramTechnologyAdapterController(controller).getDialogInspectors().attachToEditor(getEditor());
		getDiagramTechnologyAdapterController(controller).getScaleSelector().attachToEditor(getEditor());

		/*JScrollPane scrollPane = new JScrollPane(getDiagramTechnologyAdapterController(controller).getInspectors().getPanelGroup(),
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(null);*/

		perspective.setBottomRightView(getDiagramTechnologyAdapterController(controller).getInspectors().getPanelGroup());

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Force right view to be visible
				controller.getControllerModel().setRightViewVisible(true);
			}
		});

		controller.getControllerModel().setRightViewVisible(true);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == getRepresentedObject() && evt.getPropertyName().equals(getRepresentedObject().getDeletedProperty())) {
			deleteModuleView();
		}
	}
}
