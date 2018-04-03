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

package org.openflexo.fme.model.action;

import java.lang.reflect.InvocationTargetException;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.connie.exception.InvalidBindingException;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.fme.model.FMEDiagramFreeModel;
import org.openflexo.fme.model.FMEFreeModel;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.InvalidArgumentException;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.fml.CreationScheme;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.FlexoConceptInstanceRole;
import org.openflexo.foundation.fml.FlexoProperty;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstance.ObjectLookupResult;
import org.openflexo.foundation.fml.rt.action.CreateFlexoConceptInstance;
import org.openflexo.technologyadapter.diagram.fml.ConnectorRole;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;

/**
 * This action is used to create a new FlexoConcept from a diagram element
 * 
 * @author vincent
 * 
 */
public class CreateNewConceptFromDiagramElement extends AbstractInstantiateConceptFromDiagramElement<CreateNewConceptFromDiagramElement> {

	private static final Logger logger = Logger.getLogger(CreateNewConceptFromDiagramElement.class.getPackage().getName());

	public static FlexoActionFactory<CreateNewConceptFromDiagramElement, DiagramElement<?>, FlexoObject> actionType = new FlexoActionFactory<CreateNewConceptFromDiagramElement, DiagramElement<?>, FlexoObject>(
			"create_new_concept", FlexoActionFactory.defaultGroup, FlexoActionFactory.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateNewConceptFromDiagramElement makeNewAction(DiagramElement<?> focusedObject, Vector<FlexoObject> globalSelection,
				FlexoEditor editor) {
			return new CreateNewConceptFromDiagramElement(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(DiagramElement<?> object, Vector<FlexoObject> globalSelection) {
			return object instanceof DiagramShape || object instanceof DiagramConnector;
		}

		@Override
		public boolean isEnabledForSelection(DiagramElement<?> object, Vector<FlexoObject> globalSelection) {
			return object instanceof DiagramShape || object instanceof DiagramConnector;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(CreateNewConceptFromDiagramElement.actionType, DiagramShape.class);
		FlexoObjectImpl.addActionForClass(CreateNewConceptFromDiagramElement.actionType, DiagramConnector.class);
	}

	private CreateNewConceptFromDiagramElement(DiagramElement<?> focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	private FlexoConceptInstance flexoConceptInstance;
	private FlexoConcept flexoConcept;

	@Override
	protected void doAction(Object context) throws InvalidArgumentException {

		// Unused FlexoConcept containerConcept = null;
		if (getFocusedObject().getParent() != null) {
			ObjectLookupResult lookup = getFreeModelInstance().getAccessedVirtualModelInstance().lookup(getFocusedObject().getParent());
			if (lookup != null) {
				// System.out.println("lookup: " + lookup.flexoConceptInstance + " pty=" + lookup.property);
				FlexoConcept containerConceptGR = lookup.flexoConceptInstance.getFlexoConcept();
				FlexoProperty<?> p = containerConceptGR.getAccessibleProperty(FMEFreeModel.CONCEPT_ROLE_NAME);
				if (p instanceof FlexoConceptInstanceRole) {
					FlexoConceptInstanceRole fciRole = (FlexoConceptInstanceRole) p;
					// Unused containerConcept = fciRole.getFlexoConceptType();
				}
			}
		}

		logger.info("Create new instance of created concept from diagram element ");
		getNoneFlexoConcept();

		if (getFocusedObject() instanceof DiagramShape) {
			logger.info("Create new concept from shape element ");
			flexoConceptInstance = createFlexoConceptInstanceFromDiagramShape((DiagramShape) getFocusedObject());
			CreateNewConceptFromNoneConcept actionCreateNewConcept = CreateNewConceptFromNoneConcept.actionType
					.makeNewEmbeddedAction(flexoConceptInstance, null, this);
			actionCreateNewConcept.doAction();
			flexoConcept = actionCreateNewConcept.getNewFlexoConcept();
		}
		else if (getFocusedObject() instanceof DiagramConnector) {
			logger.info("Create new concept from connector element ");

			DiagramShape fromShape = ((DiagramConnector) getFocusedObject()).getStartShape();
			DiagramShape toShape = ((DiagramConnector) getFocusedObject()).getEndShape();

			FlexoConcept fromConcept = null, toConcept = null;
			FlexoConcept fromConceptGR = null, toConceptGR = null;

			FlexoConceptInstance fromFCIGR = null;
			FlexoConceptInstance toFCIGR = null;

			ObjectLookupResult fromLookup = getFreeModelInstance().getAccessedVirtualModelInstance().lookup(fromShape);
			if (fromLookup != null) {
				fromFCIGR = fromLookup.flexoConceptInstance;
				fromConceptGR = fromFCIGR.getFlexoConcept();
				FlexoProperty<?> p = fromConceptGR.getAccessibleProperty(FMEFreeModel.CONCEPT_ROLE_NAME);
				if (p instanceof FlexoConceptInstanceRole) {
					FlexoConceptInstanceRole fciRole = (FlexoConceptInstanceRole) p;
					fromConcept = fciRole.getFlexoConceptType();
				}
			}
			ObjectLookupResult toLookup = getFreeModelInstance().getAccessedVirtualModelInstance().lookup(toShape);
			if (toLookup != null) {
				toFCIGR = toLookup.flexoConceptInstance;
				toConceptGR = toFCIGR.getFlexoConcept();
				FlexoProperty<?> p = toConceptGR.getAccessibleProperty(FMEFreeModel.CONCEPT_ROLE_NAME);
				if (p instanceof FlexoConceptInstanceRole) {
					FlexoConceptInstanceRole fciRole = (FlexoConceptInstanceRole) p;
					toConcept = fciRole.getFlexoConceptType();
				}
			}

			System.out.println("From " + fromConcept + " gr=" + fromConceptGR);
			System.out.println("To " + toConcept + " gr=" + toConceptGR);

			// Now we create the new conceptual concept
			CreateNewRelationalConcept createNewConcept = CreateNewRelationalConcept.actionType.makeNewEmbeddedAction(getFMEFreeModel(),
					null, this);
			createNewConcept.setFromConcept(fromConcept);
			createNewConcept.setToConcept(toConcept);
			createNewConcept.setFromGRConcept(fromConceptGR);
			createNewConcept.setToGRConcept(toConceptGR);
			createNewConcept.doAction();
			FlexoConcept newFlexoConcept = createNewConcept.getNewFlexoConcept();
			FlexoConcept newGRFlexoConcept = createNewConcept.getNewGRFlexoConcept();

			// Now we instantiate that concept
			try {
				CreateFlexoConceptInstance instantiateConcept = CreateFlexoConceptInstance.actionType
						.makeNewEmbeddedAction(getFMEFreeModel().getSampleData().getAccessedVirtualModelInstance(), null, this);
				instantiateConcept.setFlexoConcept(newFlexoConcept);
				CreationScheme cs = newFlexoConcept.getCreationSchemes().get(0);
				instantiateConcept.setCreationScheme(cs);
				String name = newFlexoConcept.getName();
				FlexoConceptInstance fromFCI = fromFCIGR.execute("fmeConcept");
				FlexoConceptInstance toFCI = toFCIGR.execute("fmeConcept");

				if (fromFCIGR != null && toFCIGR != null) {
					name = newFlexoConcept.getName() + " " + fromFCIGR.execute("fmeConcept.name") + "-"
							+ toFCIGR.execute("fmeConcept.name");
				}
				instantiateConcept.setParameterValue(cs.getParameters().get(0), name);
				instantiateConcept.setParameterValue(cs.getParameters().get(1), fromFCI);
				instantiateConcept.setParameterValue(cs.getParameters().get(2), toFCI);
				instantiateConcept.doAction();
				FlexoConceptInstance conceptInstance = instantiateConcept.getNewFlexoConceptInstance();

				FlexoConceptInstance newFlexoConceptInstanceGR = getFreeModelInstance().getAccessedVirtualModelInstance()
						.makeNewFlexoConceptInstance(newGRFlexoConcept);
				ConnectorRole geRole = (ConnectorRole) newGRFlexoConcept.getAccessibleProperty(FMEDiagramFreeModel.CONNECTOR_ROLE_NAME);
				newFlexoConceptInstanceGR.setFlexoActor((DiagramConnector) getFocusedObject(), geRole);
				FlexoConceptInstanceRole fciRole = (FlexoConceptInstanceRole) newGRFlexoConcept
						.getAccessibleProperty(FMEFreeModel.CONCEPT_ROLE_NAME);
				newFlexoConceptInstanceGR.setFlexoActor(conceptInstance, fciRole);
				// return newFlexoConceptInstance;

			} catch (TypeMismatchException e) {
				e.printStackTrace();
			} catch (NullReferenceException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InvalidBindingException e) {
				e.printStackTrace();
			}

			// We should notify the creation of a new FlexoConcept
			getFreeModelInstance().getPropertyChangeSupport().firePropertyChange("usedFlexoConcepts", null, newFlexoConcept);
			getFreeModelInstance().getPropertyChangeSupport().firePropertyChange("usedTopLevelFlexoConcepts", null, newFlexoConcept);

			// This is used to notify the adding of a new instance of a flexo concept
			getFreeModelInstance().getPropertyChangeSupport().firePropertyChange("getInstances(FlexoConcept)", null, getFocusedObject());
			getFreeModelInstance().getPropertyChangeSupport().firePropertyChange("getEmbeddedInstances(FlexoConceptInstance)", null,
					getFocusedObject());

			// The new shape has well be added to the diagram, and the drawing (which listen to the diagram) has well received the event
			// The drawing is now up-to-date... but there is something wrong if we are in FML-controlled mode.
			// Since the shape has been added BEFORE the FlexoConceptInstance has been set, the drawing only knows about the DiagamShape,
			// and not about an FMLControlledDiagramShape. That's why we need to notify again the new diagram element's parent, to be
			// sure that the Drawing can discover that the new shape is FML-controlled
			getFocusedObject().getParent().getPropertyChangeSupport().firePropertyChange(DiagramElement.INVALIDATE, null,
					getFocusedObject().getParent());

		}

	}

	public FlexoConcept getFlexoConcept() {
		return flexoConcept;
	}

	public void setFlexoConcept(FlexoConcept flexoConcept) {
		this.flexoConcept = flexoConcept;
	}

	public FlexoConceptInstance getFlexoConceptInstance() {
		return flexoConceptInstance;
	}
}
