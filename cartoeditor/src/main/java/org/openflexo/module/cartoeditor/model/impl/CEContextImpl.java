package org.openflexo.module.cartoeditor.model.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.view.rm.ViewResource;
import org.openflexo.foundation.viewpoint.VirtualModel;
import org.openflexo.module.cartoeditor.model.CEContext;
import org.openflexo.module.cartoeditor.model.CEProjectNature;

/**
 * Created by eloubout on 05/09/14.
 */
public abstract class CEContextImpl extends FlexoObjectImpl implements CEContext {

	private static final Logger LOGGER = Logger.getLogger(CEContextImpl.class.getPackage().getName());

	/**
	 * Initialize project with all setter and resource loader with some protection. No exception are thrown.
	 *
	 * @param project FlexoProject
	 * @param nature  Nature of cartography editor project.hasNature(nature) has to be true
	 */
	@Override
	public void init(FlexoProject project, CEProjectNature nature) throws Exception {
		this.setProject(project);
		this.setProjectNature(nature);

		ViewResource ceViewResource = project.getViewLibrary().getResource(
				project.getURI() + CEProjectNature.CARTO_EDITOR_RELATIVE_VIEW_URI);

		List<FlexoResourceCenter> lst = project.getServiceManager().getResourceCenterService()
				.getResourceCenters();


		new VirtualModel().getURI()

		if (ceViewPointResource == null) {
			String errMsg = "Could not retrieve CartographyEditorViewPoint resource (searched uri=" + (project.getURI()
					+ CEProjectNature.CARTO_EDITOR_RELATIVE_VIEWPOINT_URI) + ")";
			throw new Exception(errMsg);
		}
		if (ceViewResource == null) {
			String errMsg = "Could not retrieve CartographyEditorView resource (searched uri=" + (project.getURI()
					+ CEProjectNature.CARTO_EDITOR_RELATIVE_VIEW_URI) + "";
			throw new Exception(errMsg);
		}

		try {
			setViewPoint(ceViewPointResource.getResourceData(null));
			setView(ceViewResource.getResourceData(null));
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error while retrieving resource data while initializing CEProject", e);
		}

		if (getView() == null || getViewPoint() == null) {
			LOGGER.log(Level.SEVERE, "Could not load Cartography Editor");
		}

	}

	@Override
	public String getName() {
		return getProject().getName();
	}
}
