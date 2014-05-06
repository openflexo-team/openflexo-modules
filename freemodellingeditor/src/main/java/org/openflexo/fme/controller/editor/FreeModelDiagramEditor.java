/*
 * (c) Copyright 2010-2011 AgileBirds
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
package org.openflexo.fme.controller.editor;

import java.util.logging.Logger;

import javax.swing.JTabbedPane;

import org.openflexo.fge.swing.control.SwingToolFactory;
import org.openflexo.fge.swing.control.tools.JDianaPalette;
import org.openflexo.fme.controller.FreeModelPasteHandler;
import org.openflexo.fme.model.FreeModel;
import org.openflexo.technologyadapter.diagram.controller.action.FMLControlledDiagramPasteHandler;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.AbstractDiagramPalette;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.ContextualPalette;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FMLControlledDiagramEditor;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.view.controller.FlexoController;

/**
 * Editor of a FreeModel diagram<br>
 * There is a FreeMetaModel who has a VirtualModel controlling the edition of the diagram in FML-controlled mode
 * 
 * @author sylvain
 * 
 */
public class FreeModelDiagramEditor extends FMLControlledDiagramEditor {

	private static final Logger logger = Logger.getLogger(FreeModelDiagramEditor.class.getPackage().getName());

	private final FreeModel freeModel;

	private String conceptFilter;

	private final FreeModelPasteHandler freeModelPasteHandler;

	private final DynamicPalette dynamicPalette;
	private final JDianaPalette dynamicPaletteComponent;

	public FreeModelDiagramEditor(FreeModel freeModel, boolean readOnly, FlexoController controller, SwingToolFactory swingToolFactory) {
		super(freeModel.getVirtualModelInstance(), readOnly, controller, swingToolFactory);
		this.freeModel = freeModel;
		dynamicPalette = new DynamicPalette(this);
		dynamicPaletteComponent = swingToolFactory.makeDianaPalette(dynamicPalette);
		dynamicPaletteComponent.attachToEditor(this);
		conceptFilter = "*";
		// We have to switch properly between those paste handlers
		// AND do not forget to destroy them
		freeModelPasteHandler = new FreeModelPasteHandler(freeModel, this);
	}

	@Override
	public FMLControlledDiagramPasteHandler getPasteHandler() {
		return freeModelPasteHandler;
	}

	@Override
	public void delete() {
		getFlexoController().getEditingContext().unregisterPasteHandler(freeModelPasteHandler);
		super.delete();
	}

	public FreeModel getFreeModel() {
		return freeModel;
	}

	@Override
	public String getCommonPaletteTitle() {
		return "Free shapes";
	}

	@Override
	public AbstractDiagramPalette makeCommonPalette() {
		return new FreeShapesPalette(this);
	}

	@Override
	public ContextualPalette makeContextualPalette(DiagramPalette palette) {
		return new ConceptsPalette(palette, this);
	}

	@Override
	protected JTabbedPane makePaletteView() {
		JTabbedPane returned = super.makePaletteView();
		returned.add(dynamicPaletteComponent.getPaletteViewInScrollPane(), "Used shapes", 0);
		return returned;
	}

	public String getConceptFilter() {
		return conceptFilter;
	}

	public void setConceptFilter(String conceptFilter) {
		this.conceptFilter = conceptFilter;
	}

}
