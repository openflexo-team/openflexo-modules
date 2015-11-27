package fr.archwater;

import java.io.File;

/**
 * A product label represents the PDF document from which we try to extract data
 * 
 * @author sylvain
 *
 */
public class ProductLabel {

	private File pdfFile;

	public ProductLabel(File pdfFile) {
		this.pdfFile = pdfFile;
		System.out.println("load " + pdfFile);
	}
}
