package org.openflexo.vpm;

import org.openflexo.icon.VPMIconLibrary;
import org.openflexo.module.Module;

/**
 * ViewPointModeller module<br>
 * This class represents the static definition of the module<br>
 * You should never instantiate this class (never invoke constructor of this class): this will be done by the ModuleLoader during dynamic
 * service discovering
 * 
 * @author sylvain
 */
public class ViewPointModeller extends Module<VPMModule> {

	public static ViewPointModeller INSTANCE;

	public ViewPointModeller() {
		super(VPMModule.VPM_MODULE_NAME, VPMModule.VPM_MODULE_SHORT_NAME, VPMModule.class, VPMPreferences.class,
				"modules/flexoviewpointmodeller", "10401", "vpm", VPMIconLibrary.VPM_SMALL_ICON, VPMIconLibrary.VPM_MEDIUM_ICON,
				VPMIconLibrary.VPM_MEDIUM_ICON_WITH_HOVER, VPMIconLibrary.VPM_BIG_ICON);
		// WE set it now, because we are sure the ServiceManager did this call first
		// WE want to avoid twice defined objects
		INSTANCE = this;
	}
}