package malen.vue;

import javax.swing.*;

import malen.Controleur;

import java.awt.*;
import java.io.File;

public class MalenMainFrame extends JFrame {

	private MalenImagePanel imagePanel; // Référence au panneau d'image
	private JScrollPane scrollPane; // JScrollPane pour gérer le défilement

	private Controleur controleur;

	public MalenMainFrame(Controleur controleur) {
		this.controleur = controleur;
		// Configuration de la fenêtre principale
		setTitle("Mini Paint Application");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 700);
		setLayout(new BorderLayout());

		// Ajouter le panneau de menu
		MalenMenuBar menuPanel = new MalenMenuBar(this);
		add(menuPanel, BorderLayout.NORTH);

		// Ajouter le panneau d'affichage d'image dans un JScrollPane
		imagePanel = new MalenImagePanel();
		scrollPane = new JScrollPane(imagePanel); // Envelopper l'image dans un JScrollPane
		scrollPane.setPreferredSize(new Dimension(800, 600)); // Taille du panneau d'affichage de l'image
		add(scrollPane, BorderLayout.CENTER);

		// Afficher la fenêtre
		setLocationRelativeTo(null);
		setVisible(true);
	}

	// Méthode pour ouvrir le dialogue d'importation d'image
	public void importImage() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(
				new javax.swing.filechooser.FileNameExtensionFilter("jpeg", "gif"));
		int result = fileChooser.showOpenDialog(this);

		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			String imagePath = selectedFile.getAbsolutePath();

			// Importer l'image dans le panneau
			imagePanel.importImage(imagePath);

			// Mettre à jour la taille du JScrollPane selon la taille de l'image
			Dimension imageSize = imagePanel.getImageSize();
			imagePanel.setPreferredSize(imageSize); // Mettre à jour la taille du panel
			scrollPane.revalidate(); // Revalider le JScrollPane pour appliquer les nouvelles dimensions
			scrollPane.repaint(); // Redessiner le JScrollPane
		}
	}

	public void chooseColor() {
		// Afficher un sélecteur de couleur
		Color selectedColor = JColorChooser.showDialog(null, "Choisir une couleur", controleur.getCurrentColor());

		if (selectedColor != null) {
			controleur.setColor(selectedColor);
		}
	}
}
