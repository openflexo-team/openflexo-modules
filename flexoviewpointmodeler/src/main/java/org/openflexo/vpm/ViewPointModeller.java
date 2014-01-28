package org.openflexo.vpm;

import org.openflexo.icon.VPMIconLibrary;
import org.openflexo.module.Module;

/**
 * ViewPointModeller module
 * 
 * @author sylvain
 */
public class ViewPointModeller extends Module<VPMModule> {

	public static ViewPointModeller INSTANCE;

	public ViewPointModeller() {
		super(VPMModule.VPM_MODULE_NAME, VPMModule.VPM_MODULE_SHORT_NAME, VPMModule.class, VPMPreferences.class,
				"modules/flexoviewpointmodeller", "10009", "vpm", VPMIconLibrary.VPM_SMALL_ICON, VPMIconLibrary.VPM_MEDIUM_ICON,
				VPMIconLibrary.VPM_MEDIUM_ICON_WITH_HOVER, VPMIconLibrary.VPM_BIG_ICON, false);
		// WE set it now, because we are sure the ServiceManager did this call first
		// WE want to avoid twice defined objects
		INSTANCE = this;
	}
}