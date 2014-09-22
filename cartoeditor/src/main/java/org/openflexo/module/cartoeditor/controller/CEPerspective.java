package org.openflexo.module.cartoeditor.controller;

import java.util.ArrayList;
import javax.swing.ImageIcon;

import org.openflexo.foundation.FlexoObject;
import org.openflexo.module.cartoeditor.model.CEModel;
import org.openflexo.module.cartoeditor.view.CEIconLibrary;
import org.openflexo.module.cartoeditor.view.CEModuleView;
import org.openflexo.module.cartoeditor.widget.FIBCEProjectBrowser;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

/**
 * Cartography Editor Perspective.
 * <p/>
 * Created by eloubout on 03/09/14.
 */
public class CEPerspective extends FlexoPerspective {

	private final FIBCEProjectBrowser projectBrowser;

	public CEPerspective(final FlexoController controller) {
		super("cartography_editor_perspective", controller);
		projectBrowser = new FIBCEProjectBrowser(controller);
		setTopLeftView(projectBrowser);
	}

	@Override
	public ImageIcon getActiveIcon() {
		return CEIconLibrary.CE_SMALL_ICON;
	}

	@Override
	public String getWindowTitleforObject(final FlexoObject object, final FlexoController controller) {
		return "";
	}

	@Override
	public ModuleView<?> createModuleViewForObject(final FlexoObject object) {
		if (object instanceof CEModel) {
			return new CEModuleView(getController(), this, (CEModel) object);
		}
		return super.createModuleViewForObject(object);
	}

	@Override
	public boolean hasModuleViewForObject(final FlexoObject object) {
		if (object instanceof CEModel){
			return true;
		}
		return super.hasModuleViewForObject(object);
	}
}
