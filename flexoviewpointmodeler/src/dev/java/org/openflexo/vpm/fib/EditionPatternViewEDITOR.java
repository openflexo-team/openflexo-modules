/**
 * 
 * Copyright (c) 2013-2015, Openflexo
 * Copyright (c) 2011-2012, AgileBirds
 * 
 * This file is part of Flexoviewpointmodeler, a component of the software infrastructure 
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

package org.openflexo.vpm.fib;

import java.io.File;

import org.openflexo.TestApplicationContext;
import org.openflexo.fib.editor.FIBAbstractEditor;
import org.openflexo.foundation.fml.ViewPoint;
import org.openflexo.foundation.fml.ViewPointLibrary;
import org.openflexo.toolbox.FileResource;
import org.openflexo.vpm.VPMCst;

public class FlexoConceptViewEDITOR extends FIBAbstractEditor {

	@Override
	public Object[] getData() {

		TestApplicationContext testApplicationContext = new TestApplicationContext(
				new FileResource("src/test/resources/TestResourceCenter"));
		ViewPointLibrary viewPointLibrary = testApplicationContext.getViewPointLibrary();

		Object[] returned = new Object[11];

		/*ViewPoint viewPoint1 = viewPointLibrary
				.getViewPoint("http://www.agilebirds.com/openflexo/ViewPoints/Tests/BasicOrganizationTreeEditor.owl");
		returned[0] = viewPoint1.getFlexoConcept("Employee");
		returned[1] = viewPoint1.getFlexoConcept("BOTDepartment");*/

		System.out.println("viewPointLibrary=" + viewPointLibrary);
		System.out.println("vps=" + viewPointLibrary.getViewPoints());

		ViewPoint viewPoint2 = viewPointLibrary
				.getViewPoint("http://www.agilebirds.com/openflexo/ViewPoints/ScopeDefinition/OrganizationalUnitDefinition.owl");
		returned[0] = viewPoint2.getDefaultDiagramSpecification().getFlexoConcept("OrganizationalUnit");
		returned[1] = viewPoint2.getDefaultDiagramSpecification().getFlexoConcept("OrganizationalUnitPosition");
		returned[2] = viewPoint2.getDefaultDiagramSpecification().getFlexoConcept("PositionTask");

		ViewPoint viewPoint3 = viewPointLibrary.getViewPoint("http://www.agilebirds.com/openflexo/ViewPoints/Basic/BasicOntology.owl");
		returned[3] = viewPoint3.getDefaultDiagramSpecification().getFlexoConcept("Concept");
		returned[4] = viewPoint3.getDefaultDiagramSpecification().getFlexoConcept("IsARelationship");
		returned[5] = viewPoint3.getDefaultDiagramSpecification().getFlexoConcept("HasRelationship");

		ViewPoint viewPoint4 = viewPointLibrary.getViewPoint("http://www.agilebirds.com/openflexo/ViewPoints/SKOS/SKOSThesaurusEditor.owl");
		returned[6] = viewPoint4.getDefaultDiagramSpecification().getFlexoConcept("Concept");

		ViewPoint viewPoint5 = viewPointLibrary.getViewPoint("http://www.agilebirds.com/openflexo/ViewPoints/UML/UseCaseDiagram.owl");
		returned[7] = viewPoint5.getDefaultDiagramSpecification().getFlexoConcept("Actor");

		ViewPoint viewPoint6 = viewPointLibrary
				.getViewPoint("http://www.agilebirds.com/openflexo/ViewPoints/ScopeDefinition/OrganizationalMap.owl");
		returned[8] = viewPoint6.getDefaultDiagramSpecification().getFlexoConcept("ContainsPositionLink");
		returned[9] = viewPoint6.getDefaultDiagramSpecification().getFlexoConcept("SubOrganizationUnitLink");

		ViewPoint viewPoint7 = viewPointLibrary.getViewPoint("http://www.agilebirds.com/openflexo/ViewPoints/UML/PackageDiagram.owl");
		returned[10] = viewPoint7.getDefaultDiagramSpecification().getFlexoConcept("ImportPackage");

		// ViewPoint viewPoint9 = viewPointLibrary.getOntologyCalc("http://www.thalesgroup.com/ViewPoints/sepel-ng/MappingCapture.owl");
		// viewPoint9.loadWhenUnloaded();
		// returned[13] = viewPoint9.getFlexoConcept("ConceptMapping");

		return returned;
	}

	@Override
	public File getFIBFile() {
		return VPMCst.DIAGRAM_FLEXO_CONCEPT_VIEW_FIB;
	}

	public static void main(String[] args) {
		main(FlexoConceptViewEDITOR.class);
	}

}
