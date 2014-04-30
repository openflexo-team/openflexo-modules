/*
 * (c) Copyright 2013-2014 Openflexo
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openflexo.fme.controller;

import java.util.logging.Logger;

import org.openflexo.fme.controller.editor.FreeModelDiagramEditor;
import org.openflexo.fme.model.FreeModel;
import org.openflexo.fme.model.FreeModellingProjectNature;
import org.openflexo.foundation.action.FlexoClipboard;
import org.openflexo.foundation.action.PasteAction.PastingContext;
import org.openflexo.foundation.view.FlexoConceptInstance;
import org.openflexo.foundation.view.VirtualModelInstance;
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

	private final FreeModel freeModel;

	public FreeModelPasteHandler(FreeModel freeModel, FreeModelDiagramEditor editor) {
		super(freeModel.getVirtualModelInstance(), editor);
		this.freeModel = freeModel;
	}

	public FreeModel getFreeModel() {
		return freeModel;
	}

	public FreeModellingProjectNature getProjectNature() {
		return freeModel.getProjectNature();
	}

	@Override
	public void prepareClipboardForPasting(FlexoClipboard clipboard, PastingContext<VirtualModelInstance> pastingContext) {

		super.prepareClipboardForPasting(clipboard, pastingContext);

		Clipboard leaderClipboard = clipboard.getLeaderClipboard();

		// Translating names
		if (leaderClipboard.isSingleObject()) {
			if (leaderClipboard.getSingleContents() instanceof FlexoConceptInstance) {
				translateName((FlexoConceptInstance) leaderClipboard.getSingleContents());
			}
		} else {
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
		} else if (oldName.contains(COPY_SUFFIX)) {
			try {
				int currentIndex = Integer.parseInt(oldName.substring(oldName.lastIndexOf(COPY_SUFFIX) + COPY_SUFFIX.length()));
				newName = oldName.substring(0, oldName.lastIndexOf(COPY_SUFFIX)) + COPY_SUFFIX + (currentIndex + 1);
			} catch (NumberFormatException e) {
				logger.warning("Could not parse as int " + oldName.substring(oldName.lastIndexOf(COPY_SUFFIX)));
				newName = oldName + COPY_SUFFIX;
			}
		} else {
			newName = oldName + COPY_SUFFIX;
		}
		System.out.println("translating name from " + oldName + " to " + newName);
		getProjectNature().setInstanceName(object, newName);
		return newName;
	}
}
