/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Formose prototype, a component of the software infrastructure 
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

package org.openflexo.xxxmodule.widget;

import java.io.FileNotFoundException;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.gina.model.FIBComponent;
import org.openflexo.gina.model.FIBContainer;
import org.openflexo.gina.model.widget.FIBBrowser;
import org.openflexo.gina.swing.view.widget.JFIBBrowserWidget;
import org.openflexo.rm.Resource;
import org.openflexo.technologyadapter.gina.model.FMLFIBBindingFactory;
import org.openflexo.view.FIBBrowserView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.xxxmodule.XXXCst;
import org.openflexo.xxxmodule.controller.XXXController;
import org.openflexo.xxxmodule.controller.XXXFIBController;
import org.openflexo.xxxmodule.model.XXXProjectNature;

/**
 * Browser
 * 
 * @author yourname
 */
@SuppressWarnings("serial")
public abstract class AbstractXXXProjectBrowser extends FIBBrowserView<XXXProjectNature> {

	private FIBBrowser browser;

	public AbstractXXXProjectBrowser(final FlexoController controller, Resource fibResource) {
		super(controller.getProject() != null ? controller.getProject().getNature(XXXProjectNature.class) : null, controller, fibResource,
				controller.getModuleLocales());
		getFIBController().setBrowser(this);
	}

	@Override
	public XXXProjectBrowserFIBController getFIBController() {
		return (XXXProjectBrowserFIBController) super.getFIBController();
	}

	@Override
	public XXXController getFlexoController() {
		return (XXXController) super.getFlexoController();
	}

	private VirtualModel xxxViewpoint;

	public VirtualModel getXXXViewPoint() {
		if (xxxViewpoint == null && getFlexoController() != null) {
			try {
				xxxViewpoint = getFlexoController().getApplicationContext().getVirtualModelLibrary()
						.getVirtualModel(XXXCst.XXX_VIEWPOINT_URI);
			} catch (FileNotFoundException | ResourceLoadingCancelledException | FlexoException e) {
				e.printStackTrace();
			}
		}
		return xxxViewpoint;
	}

	@Override
	public void initializeFIBComponent() {

		super.initializeFIBComponent();

		getFIBComponent().setBindingFactory(new FMLFIBBindingFactory(getXXXViewPoint()));

		browser = retrieveFIBBrowserNamed((FIBContainer) getFIBComponent(), "XXXProjectBrowser");
		if (browser != null) {
			bindFlexoActionsToBrowser(browser);
		}
	}

	public FIBBrowser getBrowser() {
		return browser;
	}

	public JFIBBrowserWidget<?> getFIBBrowserWidget() {
		return (JFIBBrowserWidget<?>) getFIBController().viewForComponent(retrieveFIBBrowser((FIBContainer) getFIBComponent()));
	}

	private Object selectedElement;

	public Object getSelectedElement() {
		return selectedElement;
	}

	public void setSelectedElement(Object selected) {
		selectedElement = selected;
	}

	public static class XXXProjectBrowserFIBController extends XXXFIBController {
		protected AbstractXXXProjectBrowser browser;

		public XXXProjectBrowserFIBController(FIBComponent component) {
			super(component);
		}

		protected void setBrowser(AbstractXXXProjectBrowser browser) {
			this.browser = browser;
		}

		public Object getSelectedElement() {
			if (browser != null) {
				return browser.getSelectedElement();
			}
			return null;
		}

		public void setSelectedElement(Object selected) {
			if (browser != null) {
				browser.setSelectedElement(selected);
			}
		}

	}

}
