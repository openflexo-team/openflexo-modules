package org.openflexo.module.cartoeditor.model;

import java.util.List;

import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.nature.ProjectWrapper;
import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.foundation.view.View;
import org.openflexo.foundation.viewpoint.ViewPoint;
import org.openflexo.model.annotations.*;
import org.openflexo.module.cartoeditor.model.impl.CEProjectImpl;

/**
 * Created by eloubout on 05/09/14.
 */
@ModelEntity
@ImplementationClass(value = CEProjectImpl.class)
@XMLElement
public interface CEContext extends FlexoObject, ProjectWrapper<CEProjectNature>, ResourceData<CEContext> {

	@PropertyIdentifier(type = FlexoProject.class)
	public static final String PROJECT_KEY = "project";
	public static final String VIEWPOINT_KEY = "viewpoint";
	public static final String VIEW_KEY = "view";
	@PropertyIdentifier(type = List.class, cardinality = Getter.Cardinality.LIST)
	public static final String CARTOGRAPHY_MODELS_KEY = "models";
	public static final String PROJECT_NATURE_KEY = "projectNature";

	@Override
	@Getter(value = PROJECT_KEY, ignoreType = true)
	public FlexoProject getProject();

	@Setter(PROJECT_KEY)
	public void setProject(FlexoProject project);

	@Getter(value = VIEWPOINT_KEY, ignoreType = true)
	public ViewPoint getViewPoint();

	@Setter(VIEWPOINT_KEY)
	public void setViewPoint(ViewPoint viewpoint);

	@Getter(value = VIEW_KEY, ignoreType = true)
	public View getView();

	@Setter(VIEW_KEY)
	public void setView(View view);

	@Override
	@Getter(value = PROJECT_NATURE_KEY, ignoreType = true)
	public CEProjectNature getProjectNature();

	@Setter(PROJECT_NATURE_KEY)
	public void setProjectNature(CEProjectNature projectNature);

	@Getter(value = CARTOGRAPHY_MODELS_KEY, cardinality = Getter.Cardinality.LIST)
	public List<CEModel> getModels();

	@Setter(CARTOGRAPHY_MODELS_KEY)
	public void setModels(List<CEModel> models);

	@Adder(value = CARTOGRAPHY_MODELS_KEY)
	public void addModel(CEModel model);

	@Remover(value = CARTOGRAPHY_MODELS_KEY)
	public void removeModel(CEModel model);

	public void init(FlexoProject project, CEProjectNature nature) throws Exception;

	public String getName();
}
