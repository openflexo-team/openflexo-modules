/**
 * 
 * Copyright (c) 2013-2015, Openflexo
 * Copyright (c) 2011-2012, AgileBirds
 * 
 * This file is part of Flexovieweditor, a component of the software infrastructure 
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

package org.openflexo.ve.fib.dialogs;

import java.io.File;
import java.util.Vector;

import org.openflexo.fib.ProjectDialogEDITOR;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.rt.View;
import org.openflexo.foundation.fml.rt.diagram.action.DeleteDiagramElements;
import org.openflexo.foundation.fml.rt.diagram.model.Diagram;
import org.openflexo.foundation.fml.rt.diagram.model.DiagramElement;
import org.openflexo.foundation.fml.rt.diagram.model.DiagramShape;
import org.openflexo.toolbox.FileResource;
import org.openflexo.ve.VECst;

public class DeleteViewElementsDialogEDITOR extends ProjectDialogEDITOR {

	@Override
	public Object[] getData() {
		FlexoEditor editor = loadProject(new FileResource("Prj/TestVE.prj"));
		FlexoProject project = editor.getProject();
		View view = project.getViewLibrary().getViewResourceNamed("BasicOrganization").getView();
		Diagram diagram = (Diagram) view.getVirtualModelInstances().get(0);
		DiagramShape a = diagram.getRootPane().getShapeNamed("A");
		DiagramShape b = a.getShapeNamed("B");
		DiagramShape c = a.getShapeNamed("C");
		DiagramShape x = b.getShapeNamed("X");
		DiagramShape worker1 = b.getShapeNamed("Worker");
		DiagramShape worker2 = b.getShapeNamed("Worker2");
		DiagramShape y = c.getShapeNamed("Y");
		DiagramShape z = a.getShapeNamed("Z");
		Vector<DiagramElement> selection = new Vector<DiagramElement>();
		selection.add(b);
		selection.add(x);
		selection.add(c);
		DeleteDiagramElements action = DeleteDiagramElements.actionType.makeNewAction(null, selection, null);
		return makeArray(action);
	}

	@Override
	public File getFIBFile() {
		return VECst.DELETE_DIAGRAM_ELEMENTS_DIALOG_FIB;
	}

	public static void main(String[] args) {
		main(DeleteViewElementsDialogEDITOR.class);
	}

}