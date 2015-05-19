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

package org.openflexo.fme.dialog;

import org.openflexo.fme.model.DataModel;
import org.openflexo.toolbox.StringUtils;

public class CreateConceptDialog {

	private String conceptName;
	private DataModel dataModel;

	public CreateConceptDialog(DataModel dataModel, String name) {
		this.dataModel = dataModel;
		if(name==null||name.equals("")){
			name = "NewConcept";
		}
		conceptName = getCandidateConceptName(name);
	}

	public String getConceptName() {
		return conceptName;
	}

	public void setConceptName(String conceptName) {
		this.conceptName = conceptName;
	}

	public boolean isValidConceptName() {
		return isValidConceptName(conceptName);
	}

	private boolean isValidConceptName(String proposedName) {
		if (StringUtils.isEmpty(proposedName)) {
			return false;
		}
		return dataModel.getConceptNamed(proposedName) == null;
	}

	private String getCandidateConceptName(String proposedName) {
		if (isValidConceptName(proposedName)) {
			return proposedName;
		}
		int i = 1;
		String aName = proposedName + i;
		while (!isValidConceptName(aName)) {
			i++;
			aName = proposedName + i;
		}
		return aName;
	}
}
