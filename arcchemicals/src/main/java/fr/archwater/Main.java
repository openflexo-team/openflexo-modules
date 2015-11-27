package fr.archwater;

import java.io.File;

import org.openflexo.rm.FileResourceImpl;
import org.openflexo.rm.ResourceLocator;

public class Main {

	public static void main(String[] args) {

		final File rootDir = ((FileResourceImpl) ResourceLocator.locateResource("ArchChemicalFR")).getFile();

		ProductLabelDirectory rootDirectory = new ProductLabelDirectory(rootDir);

		// ExtractionProject project = new ExtractionProject(args[0]);

	}
}
