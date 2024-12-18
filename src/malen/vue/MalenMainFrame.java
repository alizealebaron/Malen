package malen.vue;

import javax.imageio.ImageIO;
import javax.swing.*;

import malen.Controleur;
import malen.modele.MalenFrame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

public class MalenMainFrame extends MalenFrame {

	private MalenImagePanel imagePanel; // Référence au panneau d'image
	private JScrollPane scrollPane; // JScrollPane pour gérer le défilement
	private static final String REPERTOIRE = "./data/images/";
	private MalenMenuBar menuPanel;

	protected Controleur controleur;

	public MalenMainFrame(Controleur controleur) {
		super(controleur);
		setTitle("Malen - Fenêtre Principale");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Ajouter le panneau de menu
		this.menuPanel = new MalenMenuBar(this);
		add(this.menuPanel, BorderLayout.NORTH);
	}

	public void saveImage() {
		JFileChooser fileChooser = new JFileChooser();
		int userSelection = fileChooser.showSaveDialog(null);

		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File fileToSave = fileChooser.getSelectedFile();
			this.saveImage(fileToSave.getAbsolutePath() + ".png");
		}
	}

	public void saveImage(String fileName) {
		try {
			File outputFile = new File(fileName);
			ImageIO.write(this.imagePanel.getImage(), "png", outputFile);
		} catch (IOException e) {
			System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
		}
	}

	public void nouvelleFenetre () {
		super.controleur.nouvelleFenetre();
	}

	public void updateButton () {this.menuPanel.setCouleurButton ();}
}