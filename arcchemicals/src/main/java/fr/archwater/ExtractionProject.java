package fr.archwater;

import org.openflexo.FlexoMainLocalizer;
import org.openflexo.localization.LocalizedDelegate;

public class ExtractionProject {

	// TODO: make it an ExcelResource
	private Object excelFile;

	private ProductLabelDirectory rootDirectory;

	private LocalizedDelegate localization = FlexoMainLocalizer.getInstance();

	public ExtractionProject(ProductLabelDirectory rootDirectory) {
		this.rootDirectory = rootDirectory;
	}

	public LocalizedDelegate getLocalization() {
		return localization;
	}
}
