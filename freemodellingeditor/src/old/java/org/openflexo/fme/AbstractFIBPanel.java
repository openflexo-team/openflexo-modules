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

package org.openflexo.fme;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.openflexo.fib.FIBLibrary;
import org.openflexo.fib.controller.FIBController;
import org.openflexo.fib.model.FIBComponent;
import org.openflexo.fib.model.listener.FIBMouseClickListener;
import org.openflexo.fib.view.FIBView;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.rm.Resource;
import org.openflexo.toolbox.HasPropertyChangeSupport;
import org.openflexo.toolbox.PropertyChangeListenerRegistrationManager;

/**
 * Please comment this class
 * 
 * @author sguerin
 * 
 */
// TODO: move this to FIB library
@SuppressWarnings("serial")
public class AbstractFIBPanel extends JPanel implements PropertyChangeListener, Observer {
	static final Logger logger = Logger.getLogger(AbstractFIBPanel.class.getPackage().getName());

	private Object dataObject;

	private FIBView<?, ?, ?> fibView;
	private FMEFIBController fibController;
	private FIBComponent fibComponent;

	protected PropertyChangeListenerRegistrationManager manager = new PropertyChangeListenerRegistrationManager();

	public AbstractFIBPanel(Object representedObject, File fibFile, boolean addScrollBar) {
		this(representedObject, FIBLibrary.instance().retrieveFIBComponent(fibFile), addScrollBar);
	}
	
	public AbstractFIBPanel(Object representedObject, Resource fibResource, boolean addScrollBar) {
		this(representedObject, FIBLibrary.instance().retrieveFIBComponent(fibResource), addScrollBar);
	}

	protected AbstractFIBPanel(Object dataObject, FIBComponent fibComponent, boolean addScrollBar) {
		super(new BorderLayout());
		this.dataObject = dataObject;
		this.fibComponent = fibComponent;

		if (dataObject instanceof HasPropertyChangeSupport) {
			manager.addListener(this, (HasPropertyChangeSupport) dataObject);
		} else if (dataObject instanceof Observable) {
			((Observable) dataObject).addObserver(this);
		}

		initializeFIBComponent();

		fibController = createFibController(fibComponent);

		fibView = fibController.buildView(fibComponent);

		fibController.setDataObject(dataObject);

		if (this instanceof FIBMouseClickListener) {
			fibView.getController().addMouseClickListener((FIBMouseClickListener) this);
		}

		if (addScrollBar) {
			add(new JScrollPane(fibView.getJComponent()), BorderLayout.CENTER);
		} else {
			add(fibView.getJComponent(), BorderLayout.CENTER);
		}

		validate();
		revalidate();

	}

	/**
	 * Create the Fib Controller to be used for this view. Can be overrided to add functionalities to this Fib View.
	 * 
	 * @param fibComponent
	 * @param controller
	 * @return the newly created FlexoFIBController
	 */
	protected FMEFIBController createFibController(FIBComponent fibComponent) {
		return (FMEFIBController) FIBController.instanciateController(fibComponent, FlexoLocalization.getMainLocalizer());
		/*if (returned instanceof FlexoFIBController) {
			((FlexoFIBController) returned).setFlexoController(controller);
			return (FlexoFIBController) returned;
		} else if (fibComponent.getControllerClass() != null) {
			logger.warning("Controller for component " + fibComponent + " is not an instanceof FlexoFIBController");
		}*/
		// return fibController = new FIBController(fibComponent);
	}

	@Override
	public void update(Observable observable, Object dataModification) {
		System.out.println("Tiens faudrait mettre a jour, non ?");
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println("Tiens faudrait mettre a jour ce truc, non ?");
	}

	public Object getDataObject() {
		return dataObject;
	}

	public void setDataObject(Object object) {
		// logger.info(">>>>>>> setDataObject with " + object);
		if (this.dataObject instanceof HasPropertyChangeSupport) {
			manager.removeListener(this, (HasPropertyChangeSupport) this.dataObject);
		} else if (this.dataObject instanceof Observable) {
			((Observable) this.dataObject).deleteObserver(this);
		}
		dataObject = object;
		if (dataObject instanceof HasPropertyChangeSupport) {
			manager.addListener(this, (HasPropertyChangeSupport) this.dataObject);
		} else if (dataObject instanceof Observable) {
			((Observable) dataObject).addObserver(this);
		}
		fibController.setDataObject(object, true);
	}

	public FIBComponent getFIBComponent() {
		return fibComponent;
	}

	public FIBView<?, ?, ?> getFIBView() {
		return fibView;
	}

	public FMEFIBController getFIBController() {
		return fibController;
	}

	public FIBView<?, ?, ?> getFIBView(String componentName) {
		return fibController.viewForComponent(componentName);
	}

	public void deleteView() {
		if (this instanceof FIBMouseClickListener && fibView.getController() != null) {
			fibView.getController().removeMouseClickListener((FIBMouseClickListener) this);
		}
		fibView.delete();
		if (dataObject instanceof Observable) {
			((Observable) dataObject).deleteObserver(this);
		}
		if (dataObject instanceof HasPropertyChangeSupport) {
			manager.removeListener(this, (HasPropertyChangeSupport) dataObject);
		}
		manager.delete();
	}

	/**
	 * Returns flag indicating if this view is itself responsible for scroll management When not, Flexo will manage it's own scrollbar for
	 * you
	 * 
	 * @return
	 */
	public boolean isAutoscrolled() {
		return false;
	}

	/**
	 * This method is a hook which is called just before to initialize FIBView and FIBController, and allow to programmatically define,
	 * check or redefine component
	 */
	protected void initializeFIBComponent() {
	}

	public void setDiagramEditor(DiagramEditor diagramEditor) {
		if (fibController.getDiagramEditor() != null) {
			fibController.removeSelectionListener(fibController.getDiagramEditor());
		}
		fibController.setDiagramEditor(diagramEditor);
		if (diagramEditor != null) {
			fibController.addSelectionListener(diagramEditor);
		}
	}

}
