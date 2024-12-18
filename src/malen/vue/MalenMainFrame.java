package malen.vue;

import javax.imageio.ImageIO;
import javax.swing.*;

import malen.Controleur;
import malen.modele.Point;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MalenMainFrame extends JFrame {

	private MalenImagePanel imagePanel; // Référence au panneau d'image
	private JScrollPane scrollPane; // JScrollPane pour gérer le défilement

	protected Controleur controleur;

	public MalenMainFrame(Controleur controleur) {
		this.controleur = controleur;
		setTitle("Malen - Fenêtre Principale");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 700);
		setLayout(new BorderLayout());
		

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
		if (this.isCurseurOn(curseur)) {
			this.controleur.setCurseur(Controleur.SOURIS);
		} else {
			this.controleur.setCurseur(curseur);
		}
	}

	public boolean isCurseurOn(String curseur) {
		return this.controleur.getCurseur().equals(curseur);
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
		if (this.isCurseurOn(Controleur.PIPETTE)) {
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

	public void afficherSlider(char outil) 
	{
		this.imagePanel.showOutilSlider(outil);
	}

	public void changerContraste(BufferedImage bi, int value)
	{
		this.controleur.changerContraste(bi, value);
	}

	public void changerLuminosite(BufferedImage bi, int value)
	{
		this.controleur.changerLuminosite(bi, value);
	}

	public void onClick(BufferedImage biImage, int x, int y, Color coulPixel) {
		this.controleur.onClick(biImage, coulPixel, x, y);
	}

	public Point getPoint1() {
		return controleur.getPoint1();
	}

	public Point getPoint2() {
		return controleur.getPoint2();
	}

	public void setPoint1(Point point1) {
		controleur.setPoint1(point1);
	}

	public void setPoint2(Point point2) {
		controleur.setPoint2(point2);
	}

	public void createSubImage(BufferedImage image) {
		this.controleur.createSubImage(image);
	}

	public BufferedImage getSubImage() {
		return this.controleur.getSubImage();
	}

	public void pasteSubImage(){
		System.out.println("oui");
		this.imagePanel.pasteSubImage();
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

	public void nouvelleFenetre () {
		this.controleur.nouvelleFenetre();
	}

	public void setMainFrameMenu() {
		MalenMenuBar menuPanel = new MalenMenuBar(this);
		add(menuPanel, BorderLayout.NORTH);
	}

	public void export() {
		this.controleur.export();
	}

	public void addImage(BufferedImage img) {
		if (img == null) {
			System.out.println("Aucune image à exporter.");
			return;
		}
	
		BufferedImage baseImage = imagePanel.getImage();
		if (baseImage == null || baseImage.getWidth() == 1 && baseImage.getHeight() == 1) {
			baseImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		}
	
		BufferedImage resultImage = fusionnerImages(baseImage, img);
		imagePanel.setImage(resultImage);
		repaint();
	}

	private BufferedImage fusionnerImages(BufferedImage base, BufferedImage ajout) {
		int largeur = Math.max(base.getWidth(), ajout.getWidth());
		int hauteur = Math.max(base.getHeight(), ajout.getHeight());
	
		BufferedImage resultat = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = resultat.createGraphics();
	
		g2d.drawImage(base, 0, 0, null);
		g2d.drawImage(ajout, 0, 0, null);
		g2d.dispose();
	
		return resultat;
	}
}
