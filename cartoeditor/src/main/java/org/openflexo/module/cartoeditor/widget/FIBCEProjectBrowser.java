package org.openflexo.module.cartoeditor.widget;

import org.openflexo.fib.model.FIBBrowser;
import org.openflexo.fib.model.FIBContainer;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.view.FIBBrowserView;
import org.openflexo.view.controller.FlexoController;

/**
 * Created by eloubout on 05/09/14.
 */
public class FIBCEProjectBrowser extends FIBBrowserView<FlexoProject> {

	private static final Resource BROWSER_FIB = ResourceLocator.locateResource("Fib/CEProjectBrowser.fib");

	public FIBCEProjectBrowser(final FlexoController controller) {
		super(controller.getProject(), controller, BROWSER_FIB);
	}

	@Override
	protected void initializeFIBComponent() {
		FIBBrowser projectBrowser = retrieveFIBBrowserNamed((FIBContainer) getFIBComponent(), "ProjectBrowser");
		if (projectBrowser != null) {
			bindFlexoActionsToBrowser(projectBrowser);
		}

		FIBBrowser ceProjectBrowser = retrieveFIBBrowserNamed((FIBContainer) getFIBComponent(), "CartographyEditorProjectBrowser");
		if (ceProjectBrowser != null) {
			bindFlexoActionsToBrowser(ceProjectBrowser);
		}
	}

}
