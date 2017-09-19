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

package org.openflexo.eamodule.view;

import org.openflexo.eamodule.controller.EAFIBController;
import org.openflexo.eamodule.controller.EAMController;
import org.openflexo.eamodule.model.EAProject;
import org.openflexo.eamodule.model.EAProjectNature;
import org.openflexo.foundation.fml.binding.FMLBindingFactory;
import org.openflexo.gina.model.FIBComponent;
import org.openflexo.gina.model.FIBContainer;
import org.openflexo.gina.model.widget.FIBBrowser;
import org.openflexo.gina.swing.view.widget.JFIBBrowserWidget;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.gina.model.FMLFIBBindingFactory;
import org.openflexo.view.FIBBrowserView;
import org.openflexo.view.controller.FlexoController;

/**
 * Browser
 * 
 * @author sylvain
 */
@SuppressWarnings("serial")
public class EAProjectBrowser extends FIBBrowserView<EAProject> {

	private static final Resource BROWSER_FIB = ResourceLocator.locateResource("Fib/EAProjectBrowser.fib");

	private FIBBrowser browser;

	public EAProjectBrowser(final FlexoController controller) {
		super(controller.getProject() != null ? (EAProject) controller.getProject().asNature(EAProjectNature.class.getName()) : null,
				controller, BROWSER_FIB, controller.getModuleLocales());
		getFIBController().setBrowser(this);
	}

	@Override
	public EAProjectBrowserFIBController getFIBController() {
		return (EAProjectBrowserFIBController) super.getFIBController();
	}

	@Override
	public EAMController getFlexoController() {
		return (EAMController) super.getFlexoController();
	}

	@Override
	public void initializeFIBComponent() {

		super.initializeFIBComponent();

		System.out.println("controller=" + getFlexoController());
		System.out.println("nature=" + getFlexoController().getEANature());

		getFIBComponent().setBindingFactory(new FMLFIBBindingFactory(
				getFlexoController().getEANature().getBPMNVirtualModel(getFlexoController().getApplicationContext())));

		browser = retrieveFIBBrowserNamed((FIBContainer) getFIBComponent(), "ProjectBrowser");
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

	@Override
	public void setDataObject(Object dataObject) {
		if (dataObject instanceof EAProject) {
			getFIBComponent().setBindingFactory(new FMLBindingFactory(((EAProject) dataObject).getBPMNVirtualModel()));
			if (getFIBController() != null) {
				getFIBController().setVariableValue("bpmnVMI", ((EAProject) dataObject).getBPMNVirtualModel());
			}
		}
		super.setDataObject(dataObject);
	}

	private Object selectedElement;

	public Object getSelectedElement() {
		return selectedElement;
	}

	public void setSelectedElement(Object selected) {
		selectedElement = selected;
	}

	public static class EAProjectBrowserFIBController extends EAFIBController {
		protected EAProjectBrowser browser;

		public EAProjectBrowserFIBController(FIBComponent component) {
			super(component);
		}

		protected void setBrowser(EAProjectBrowser browser) {
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
