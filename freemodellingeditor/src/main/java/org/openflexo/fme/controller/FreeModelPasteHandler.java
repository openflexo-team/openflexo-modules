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

package org.openflexo.fme.controller;

import java.util.logging.Logger;

import org.openflexo.fme.controller.editor.FreeModelDiagramEditor;
import org.openflexo.fme.model.FMEFreeModelInstance;
import org.openflexo.fme.model.FreeModellingProjectNature;
import org.openflexo.foundation.action.copypaste.FlexoClipboard;
import org.openflexo.foundation.action.copypaste.PastingContext;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.model.factory.Clipboard;
import org.openflexo.technologyadapter.diagram.controller.action.FMLControlledDiagramPasteHandler;
import org.openflexo.toolbox.StringUtils;

/**
 * Paste Handler suitable for pasting something into a FreeModel
 * 
 * @author sylvain
 * 
 */
public class FreeModelPasteHandler extends FMLControlledDiagramPasteHandler {

	private static final Logger logger = Logger.getLogger(FreeModelPasteHandler.class.getPackage().getName());

	public static final String COPY_SUFFIX = "-copy";

	private final FMEFreeModelInstance freeModel;

	public FreeModelPasteHandler(FMEFreeModelInstance freeModel, FreeModelDiagramEditor editor) {
		super(freeModel.getVirtualModelInstance(), editor);
		this.freeModel = freeModel;
	}

	public FMEFreeModelInstance getFreeModel() {
		return freeModel;
	}

	public FreeModellingProjectNature getProjectNature() {
		return freeModel.getProjectNature();
	}

	@Override
	public void prepareClipboardForPasting(FlexoClipboard clipboard, PastingContext<VirtualModelInstance<?, ?>> pastingContext) {

		super.prepareClipboardForPasting(clipboard, pastingContext);

		Clipboard leaderClipboard = clipboard.getLeaderClipboard();

		// Translating names
		if (leaderClipboard.isSingleObject()) {
			if (leaderClipboard.getSingleContents() instanceof FlexoConceptInstance) {
				translateName((FlexoConceptInstance) leaderClipboard.getSingleContents());
			}
		}
		else {
			for (Object o : leaderClipboard.getMultipleContents()) {
				if (o instanceof FlexoConceptInstance) {
					translateName((FlexoConceptInstance) o);
				}
			}
		}
	}

	private String translateName(FlexoConceptInstance object) {

		System.out.println("Tiens, si on changeait le nom de " + object);

		String oldName = getProjectNature().getInstanceName(object);
		System.out.println("Le nom, c'est " + oldName);
		if (StringUtils.isEmpty(oldName)) {
			return null;
		}
		String newName;
		if (oldName.endsWith(COPY_SUFFIX)) {
			newName = oldName + "2";
		}
		else if (oldName.contains(COPY_SUFFIX)) {
			try {
				int currentIndex = Integer.parseInt(oldName.substring(oldName.lastIndexOf(COPY_SUFFIX) + COPY_SUFFIX.length()));
				newName = oldName.substring(0, oldName.lastIndexOf(COPY_SUFFIX)) + COPY_SUFFIX + (currentIndex + 1);
			} catch (NumberFormatException e) {
				logger.warning("Could not parse as int " + oldName.substring(oldName.lastIndexOf(COPY_SUFFIX)));
				newName = oldName + COPY_SUFFIX;
			}
		}
		else {
			newName = oldName + COPY_SUFFIX;
		}
		System.out.println("translating name from " + oldName + " to " + newName);
		getProjectNature().setInstanceName(object, newName);
		return newName;
	}
}
