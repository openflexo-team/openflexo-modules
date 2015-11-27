package fr.archwater;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A directory containing multiple {@link ProductLabel}
 * 
 * @author sylvain
 *
 */
public class ProductLabelDirectory {

	private File directory;
	private ProductLabelTemplate template;
	private List<ProductLabelDirectory> subDirectories;
	private List<ProductLabel> labels;

	public ProductLabelDirectory(File directory) {
		this.directory = directory;

		// TODO: load template

		subDirectories = new ArrayList<ProductLabelDirectory>();
		labels = new ArrayList<ProductLabel>();
		for (File f : directory.listFiles()) {
			if (f.isFile() && f.getName().endsWith(".pdf")) {
				ProductLabel label = new ProductLabel(f);
				labels.add(label);
			}
			if (f.isDirectory()) {
				ProductLabelDirectory subDirectory = new ProductLabelDirectory(f);
				subDirectories.add(subDirectory);
			}
		}
	}

	public List<ProductLabel> getLabels() {
		return labels;
	}

	public List<ProductLabelDirectory> getSubDirectories() {
		return subDirectories;
	}
}
