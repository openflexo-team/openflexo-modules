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

package org.openflexo.fme.controller.action;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.fme.model.action.CreateFMEDiagramFreeModel;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.gina.annotation.FIBPanel;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.view.controller.FlexoController;

public class CreateFMEDiagramFreeModelWizard extends AbstractCreateFMEFreeModelWizard<CreateFMEDiagramFreeModel> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CreateFMEDiagramFreeModelWizard.class.getPackage().getName());

	public CreateFMEDiagramFreeModelWizard(CreateFMEDiagramFreeModel action, FlexoController controller) {
		super(action, controller);
	}

	@Override
	public String getWizardTitle() {
		return getAction().getLocales().localizedForKey("create_diagram_free_model");
	}

	@Override
	public DescribeDiagramFreeModel makeDescribeFreeModel() {
		return new DescribeDiagramFreeModel();
	}

	/**
	 * This step is used to set name and title of the {@link FMLRTVirtualModelInstance}
	 * 
	 * @author sylvain
	 *
	 */
	@FIBPanel("Fib/Wizard/DescribeDiagramFreeModel.fib")
	public class DescribeDiagramFreeModel extends DescribeFreeModel {

		@Override
		public boolean isValid() {

			if (!super.isValid()) {
				return false;
			}

			if (getDiagramSpecificationFolder() == null) {
				setIssueMessage(getAction().getLocales().localizedForKey("please_define_diagram_specification_folder"),
						IssueMessageType.ERROR);
				return false;
			}

			return true;
		}

		public RepositoryFolder<?, ?> getDiagramSpecificationFolder() {
			return getAction().getDiagramSpecificationFolder();
		}

		public void setDiagramSpecificationFolder(RepositoryFolder<?, ?> diagramSpecificationFolder) {
			if (!diagramSpecificationFolder.equals(getDiagramSpecificationFolder())) {
				RepositoryFolder<?, ?> oldValue = getDiagramSpecificationFolder();
				getAction().setDiagramSpecificationFolder(diagramSpecificationFolder);
				getPropertyChangeSupport().firePropertyChange("diagramSpecificationFolder", oldValue, diagramSpecificationFolder);
				checkValidity();
			}
		}

		public Type getDiagramSpecificationType() {
			return DiagramSpecification.class;
		}

		public DiagramTechnologyAdapter getDiagramTechnologyAdapter() {
			return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(DiagramTechnologyAdapter.class);
		}

	}

}
