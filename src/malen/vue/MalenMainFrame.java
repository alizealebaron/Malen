package malen.vue;

import javax.imageio.ImageIO;
import javax.swing.*;

import malen.Controleur;
import malen.modele.Point;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
		imagePanel = new MalenImagePanel(this);
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
				new javax.swing.filechooser.FileNameExtensionFilter("png", "gif"));
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

	public void switchCurseur(String curseur) {
		if (this.controleur.getCurseur().equals(curseur)) {
			this.controleur.setCurseur(Controleur.SOURIS);
		} else {
			this.controleur.setCurseur(curseur);
		}
	}

	public void chooseColor() {
		// Afficher un sélecteur de couleur
		Color selectedColor = JColorChooser.showDialog(null, "Choisir une couleur", controleur.getCurrentColor());

		if (selectedColor != null) {
			controleur.setColor(selectedColor);
		}
	}

	/**
	 * @deprecated
	 */
	public void setPickedColor(Color color) {
		if (this.controleur.getCurseur().equals(Controleur.PIPETTE)) {
			// Afficher la couleur dans le label
			// colorLabel.setBackground(color);

			// Passer la couleur au contrôleur
			controleur.setColor(color);

			System.out.println(this.controleur.getCurrentColor().toString());

		}
	}

	public void saveImageToFile(String name) {
		this.imagePanel.saveImageToFile(name);
	}

	public void switchRetournementHorizontal() {
		imagePanel.switchFlipHorizontal();
	}

	public void switchRetournementVertical() {
		imagePanel.switchFlipVertical();
	}

	public void rotationAxiale() {
		this.imagePanel.showRotationSlider();
	}

	public void onClick(BufferedImage biImage, int x, int y, Color coulPixel) {
		this.controleur.onClick(biImage, coulPixel, x, y);
		if (controleur.getCurseur().equals(Controleur.SELECTION_RECTANGLE)) {
			Point point1 = controleur.getPoint1();
			Point point2 = controleur.getPoint2();

			// Si un point1 existe, afficher un rectangle en pointillé
			if (point1 != null) {
				Graphics2D g = (Graphics2D) biImage.getGraphics();
				g.setColor(Color.BLACK);
				g.setStroke(
						new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, new float[] { 10 }, 0)); // pointillé
				g.drawRect(Math.min(point1.x(), x), Math.min(point1.y(), y), Math.abs(x - point1.x()),
						Math.abs(y - point1.y()));
				repaint(); // Redessiner
			}

			// Si les deux points sont définis, créer la subimage
			if (point1 != null && point2 != null) {
				controleur.createSubImage(biImage);
				// Remettre les points à null après la sélection
				controleur.resetSelection();
			}
		}
	}

	public Point getPoint1() {
		return controleur.getPoint1();
	}

	public Point getPoint2() {
		return controleur.getPoint2();
	}

	public BufferedImage getImage() {
		return this.imagePanel.getImage();
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
			System.out.println("Image sauvegardée dans : " + fileName);
		} catch (IOException e) {
			System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
		}
	}
}
