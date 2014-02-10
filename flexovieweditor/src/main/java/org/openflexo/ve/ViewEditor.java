package org.openflexo.ve;

import org.openflexo.icon.VEIconLibrary;
import org.openflexo.module.Module;

/**
 * ViewEditor module<br>
 * This class represents the static definition of the module<br>
 * You should never instantiate this class (never invoke constructor of this class): this will be done by the ModuleLoader during dynamic
 * service discovering
 * 
 * @author sylvain
 */
public class ViewEditor extends Module<VEModule> {
	public ViewEditor() {
		super(VEModule.VE_MODULE_NAME, VEModule.VE_MODULE_SHORT_NAME, VEModule.class, VEPreferences.class,
				"modules/flexoviewpointmodeller", "10008", "ve", VEIconLibrary.VE_SMALL_ICON, VEIconLibrary.VE_MEDIUM_ICON,
				VEIconLibrary.VE_MEDIUM_ICON_WITH_HOVER, VEIconLibrary.VE_BIG_ICON, true);
	}
}