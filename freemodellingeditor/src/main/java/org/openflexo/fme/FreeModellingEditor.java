package org.openflexo.fme;

import org.openflexo.fme.model.FreeModellingProjectNature;
import org.openflexo.module.NatureSpecificModule;

/**
 * ViewPointModeller module
 * 
 * @author sylvain
 */
public class FreeModellingEditor extends NatureSpecificModule<FMEModule, FreeModellingProjectNature> {

	public static FreeModellingEditor INSTANCE;

	public FreeModellingEditor() {
		super(FMEModule.FME_MODULE_NAME, FMEModule.FME_MODULE_SHORT_NAME, FMEModule.class, FMEPreferences.class,
				"modules/freemodellingeditor", "10400", "fme", FMEIconLibrary.FME_SMALL_ICON, FMEIconLibrary.FME_MEDIUM_ICON,
				FMEIconLibrary.FME_MEDIUM_ICON_WITH_HOVER, FMEIconLibrary.FME_BIG_ICON, FreeModellingProjectNature.class);
		// WE set it now, because we are sure the ServiceManager did this call first
		// WE want to avoid twice defined objects
		INSTANCE = this;
	}

}