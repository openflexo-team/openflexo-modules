/*
 * (c) Copyright 2014- Openflexo
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


package org.openflexo.module.sample.controller;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JMenuItem;

import org.openflexo.foundation.FlexoObject;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.selection.FlexoClipboard;
import org.openflexo.selection.PastingGraphicalContext;
import org.openflexo.view.controller.FlexoController;

// TODO: Auto-generated Javadoc
/**
 * smpClipboard is intented to be the object working with the smpSelectionManager and storing copied, cutted and pasted objects. Handled
 * objects are instances implementing {@link org.openflexo.selection.SelectableView}.
 * 
 * @author yourname
 */
public class SMPClipboard extends FlexoClipboard {

	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(SMPClipboard.class.getPackage().getName());

	/** The _smp selection manager. */
	protected SMPSelectionManager _smpSelectionManager;

	/**
	 * Instantiates a new SMP clipboard.
	 * 
	 * @param aSelectionManager
	 *            the a selection manager
	 * @param copyMenuItem
	 *            the copy menu item
	 * @param pasteMenuItem
	 *            the paste menu item
	 * @param cutMenuItem
	 *            the cut menu item
	 */
	public SMPClipboard(SMPSelectionManager aSelectionManager, JMenuItem copyMenuItem, JMenuItem pasteMenuItem, JMenuItem cutMenuItem) {
		super(aSelectionManager, copyMenuItem, pasteMenuItem, cutMenuItem);
		_smpSelectionManager = aSelectionManager;
		resetClipboard();
	}

	/**
	 * Gets the selection manager.
	 * 
	 * @return the selection manager
	 */
	public SMPSelectionManager getSelectionManager() {
		return _smpSelectionManager;
	}

	/**
	 * Gets the CED controller.
	 * 
	 * @return the CED controller
	 */
	public SMPController getCEDController() {
		return getSelectionManager().getCEDController();
	}

	/* (non-Javadoc)
	 * @see org.openflexo.selection.FlexoClipboard#performSelectionPaste()
	 */
	@Override
	public boolean performSelectionPaste() {
		if (_isPasteEnabled) {
			return super.performSelectionPaste();
		} else {
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("Sorry, PASTE disabled");
			}
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.openflexo.selection.FlexoClipboard#performSelectionPaste(org.openflexo.foundation.FlexoObject, org.openflexo.selection.PastingGraphicalContext)
	 */
	@Override
	protected void performSelectionPaste(FlexoObject pastingContext, PastingGraphicalContext graphicalContext) {
		JComponent targetContainer = graphicalContext.targetContainer;
		if (isTargetValidForPasting(targetContainer)) {
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("Paste is legal");
				// Handle paste here
			}
		} else {
			FlexoController.notify(FlexoLocalization.localizedForKey("cannot_paste_at_this_place_wrong_level"));
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("Paste is NOT legal");
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.openflexo.selection.FlexoClipboard#isCurrentSelectionValidForCopy(java.util.Vector)
	 */
	@Override
	protected boolean isCurrentSelectionValidForCopy(Vector currentlySelectedObjects) {
		return getSelectionManager().getSelectionSize() > 0;
	}

	/**
	 * Reset clipboard.
	 */
	protected void resetClipboard() {
	}

	/**
	 * Selection procedure for copy.
	 * 
	 * @param currentlySelectedObjects
	 *            the currently selected objects
	 * @return true, if successful
	 */
	@Override
	protected boolean performCopyOfSelection(Vector currentlySelectedObjects) {
		resetClipboard();
		// Put some code here
		// _clipboardData = ....
		return true;
	}

	/**
	 * Checks if is target valid for pasting.
	 * 
	 * @param targetContainer
	 *            the target container
	 * @return true, if is target valid for pasting
	 */
	protected boolean isTargetValidForPasting(JComponent targetContainer) {
		// Put some code here
		return false;
	}
}
