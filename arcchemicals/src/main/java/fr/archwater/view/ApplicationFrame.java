package fr.archwater.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import fr.archwater.ExtractionProject;

public class ApplicationFrame extends JFrame {

	private ExtractionProject project;

	private ProjectBrowser projectBrowser;

	public ApplicationFrame(ExtractionProject project) {
		super();
		this.project = project;
		projectBrowser = new ProjectBrowser(project);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(projectBrowser, BorderLayout.WEST);

		validate();
		pack();
		setVisible(true);
	}

}
