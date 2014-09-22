package org.openflexo.module.cartoeditor.view;

import java.awt.BorderLayout;
import javax.swing.JPanel;

import org.openflexo.module.cartoeditor.model.CEModel;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

/**
 * Created by eloubout on 04/09/14.
 */
public class CEModuleView extends JPanel implements ModuleView<CEModel> {

	private final FlexoController controller;
	private final FlexoPerspective perspective;
	private final CEModel representedObject;

	public CEModuleView(FlexoController controller, FlexoPerspective perspective, CEModel toRpz){
		this.controller = controller;
		this.perspective = perspective;
		this.representedObject = toRpz;

		final JPanel bottomPanel = new JPanel(new BorderLayout());

		bottomPanel.add(getFlexoController().makeInfoLabel(), BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
	}


	@Override
	public CEModel getRepresentedObject() {
		return this.representedObject;
	}

	@Override
	public void deleteModuleView() {
		getFlexoController().removeModuleView(this);
	}

	@Override
	public FlexoPerspective getPerspective() {
		return this.perspective;
	}

	@Override
	public void willShow() {

	}

	@Override
	public void willHide() {

	}

	@Override
	public void show(final FlexoController controller, final FlexoPerspective perspective) {

	}

	@Override
	public boolean isAutoscrolled() {
		return false;
	}

	// ====================
	// GETTERS AND SETTERS
	// ====================

	public FlexoController getFlexoController() {
		return controller;
	}

}
