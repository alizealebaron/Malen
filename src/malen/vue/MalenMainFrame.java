package malen.vue;

import javax.imageio.ImageIO;
import javax.swing.*;

import malen.Controleur;
import malen.modele.Point;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MalenMainFrame extends JFrame {

	private MalenImagePanel imagePanel; // Référence au panneau d'image
	private JScrollPane scrollPane; // JScrollPane pour gérer le défilement
	private static final String REPERTOIRE = "./data/images/";
	private MalenMenuBar menuPanel;

	private Controleur controleur;

	public MalenMainFrame(Controleur controleur) 
	{
		this.controleur = controleur;
		// Configuration de la fenêtre principale
		setTitle("Malen");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1650,1080);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		setLayout(new BorderLayout());

		// Ajouter le panneau de menu
		this.menuPanel = new MalenMenuBar(this);
		add(this.menuPanel, BorderLayout.NORTH);

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

	public void switchCurseur(String curseur) 
	{
		if (this.controleur.getCurseur().equals(curseur)) 
		{
			this.controleur.setCurseur(Controleur.SOURIS);
			scrollPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		} 
		else 
		{
			this.controleur.setCurseur(curseur);
			changerCurseur(curseur);
		}
	}

	/** Permet de changer le curseur selon le mode actuel
	 * @param curseur Le mod activé
	 */
	private void changerCurseur (String curseur)
	{
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image image = toolkit.getImage(REPERTOIRE + "pipette-retournee.png");
		Cursor pipette = toolkit.createCustomCursor(image , new java.awt.Point(0, 0), "Pipette");

		image = toolkit.getImage(REPERTOIRE + "pot.png");
		Cursor pot = toolkit.createCustomCursor(image , new java.awt.Point(0,15), "Pot");

		image = toolkit.getImage(REPERTOIRE + "transparence.png");
		Cursor trans = toolkit.createCustomCursor(image , new java.awt.Point(0, 0), "Transparence");

		switch (curseur) 
		{
			case Controleur.SELECTION_RECTANGLE:
				scrollPane.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
				break;

			case Controleur.SELECTION_OVALE:
				scrollPane.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
				break;

			case Controleur.PIPETTE:
				scrollPane.setCursor(pipette);
				break;

			case Controleur.POT_DE_PEINTURE:
				scrollPane.setCursor(pot);
				break;

			case Controleur.EFFACE_FOND:
				scrollPane.setCursor(trans);
				break;

			case Controleur.TEXT:
				scrollPane.setCursor(new Cursor(Cursor.TEXT_CURSOR));
				break;
		
			default:
				scrollPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				break;
		}
	}

	public boolean isCurseurOn(String curseur) 
	{
		return this.controleur.getCurseur().equals(curseur);
	}

	public Color getCurrentColor() {return this.controleur.getCurrentColor();}

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
		if (this.isCurseurOn(Controleur.SELECTION_RECTANGLE)) {
			this.controleur.createRectangleSubImage(image);
		}
		if (this.isCurseurOn(Controleur.SELECTION_OVALE)) {
			this.controleur.createOvalSubImage(image);
		}
	}

	public BufferedImage getSubImage() {
		return this.controleur.getSubImage();
	}

	public void setSubimage(BufferedImage image)
	{
		this.controleur.setSubImage(image);
	}

	public void pasteSubImage(){
		this.imagePanel.pasteSubImage();
	}

	public BufferedImage getImage() {
		return this.imagePanel.getImage();
	}

	public void saveImage() 
	{
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

	public void updateButton () {this.menuPanel.setCouleurButton ();}

	public void setCurrentColor (Color c) { this.controleur.setColor(c);}

	/* ------------------------------------------------------------------------------------------------------------------------------ */
	/*                                                     Gestion du texte                                                           */
	/* ------------------------------------------------------------------------------------------------------------------------------ */

	/** Permet d'afficher le panel de modification du texte
	 * 
	 */
	public void afficherPanelText()
	{
		this.imagePanel.afficherPanelText();
	}
}
