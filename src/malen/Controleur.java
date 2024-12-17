package malen;

import java.awt.Color;

import javax.swing.SwingUtilities;

import malen.vue.MalenMainFrame;
import malen.modele.Couleur;
import malen.modele.Point;

import java.awt.image.BufferedImage;

/**
 * Classe Controleur
 * 
 * @author : Alizéa Lebaron
 * @author : Tom Goureau
 * @version : 1.0.0 - 16/12/2024
 * @date : 16/12/2024
 */

public class Controleur {
	/**
	 * 
	 */
	public static final String PIPETTE = "pipette";
	public static final String SOURIS = "souris";
	public static final String POT_DE_PEINTURE = "pot";
	public static final String SELECTION_RECTANGLE = "rectangle";
	public static final String SELECTION_OVALE = "cercle";
	public static final String EFFACE_FOND = "fond";

	private MalenMainFrame mainFrame;

	private Color currentColor = Color.GREEN; // La couleur actuelle, par défaut noire
	private String curseur;

	private Point point1;
	private Point point2;
	private BufferedImage subImage;

	public Controleur() {
		mainFrame = new MalenMainFrame(this); // Crée la fenêtre principale
		this.curseur = "souris";

		this.point1 = null;
		this.point2 = null;
		this.subImage = null;
	}

	// Méthode pour démarrer l'application
	public void startApplication() {
		SwingUtilities.invokeLater(() -> {
			mainFrame.setVisible(true);
		});
	}

	public void setCurseur(String curseur) {
		this.curseur = curseur;
		if (!curseur.equals(SELECTION_RECTANGLE)) {
			resetSelection(); // Réinitialiser la sélection quand on sort du mode sélection rectangle
		}
	}

	public void resetSelection() {
		point1 = null;
		point2 = null;
		subImage = null;
	}

	public String getCurseur() {
		return this.curseur;
	}

	// Méthode pour afficher le sélecteur de couleur et stocker la couleur choisie
	public void setColor(Color selectedColor) {
		currentColor = selectedColor;
	}

	// Méthode pour obtenir la couleur actuelle
	public Color getCurrentColor() {
		return currentColor;
	}

	public Point getPoint1() {
		return point1;
	}

	public Point getPoint2() {
		return point2;
	}

	public void setPoint1(Point point1) {
		this.point1 = point1;
	}

	public void setPoint2(Point point2) {
		this.point2 = point2;
	}

	public BufferedImage getSubImage() {
		return this.subImage;
	}

	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/
	/*---------------------------------------------------------Action avec le click---------------------------------------------------------------------*/
	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/

	/**
	 * Méthode permettant de faire une action pendant un click selon l'état du
	 * curseur
	 * 
	 */
	public void onClick(BufferedImage biImage, Color coulPixel, int x, int y) {
		switch (this.curseur) {
			case Controleur.SOURIS:

				System.out.println("Souris en mode : " + this.curseur);
				break;

			case Controleur.PIPETTE:

				System.out.println("Souris en mode : " + this.curseur);
				this.currentColor = coulPixel;
				break;

			case Controleur.POT_DE_PEINTURE:

				System.out.println("Souris en mode : " + this.curseur);

				fill(biImage, coulPixel, this.currentColor, x, y);

				break;

			case Controleur.EFFACE_FOND:

				System.out.println("Souris en mode : " + this.curseur);

				fondTransparent(biImage, coulPixel, x, y);

				break;

				case Controleur.SELECTION_RECTANGLE:
	
					System.out.println("Souris en mode : " + this.curseur);
					System.out.println(this.subImage!=null);
	
					if (this.subImage!=null) { //peut poser des problemes, mettre verif sur point1 et point2
						System.out.println("oui");
						this.mainFrame.pasteSubImage();
					}
	
					break;
	

			default:
				System.out.println("Choix incorrect");
				break;

		}
	}

	public void createSubImage(BufferedImage image) {
		if (image == null) {
			this.subImage = null;
		} else {
			if (point1 != null && point2 != null && point1.x() != point2.x() && point1.y() != point2.y()) {
				int x1 = Math.min(point1.x(), point2.x());
				int y1 = Math.min(point1.y(), point2.y());
				int width = Math.abs(point1.x() - point2.x());
				int height = Math.abs(point1.y() - point2.y());

				System.out.println("subimage : " + x1 + " " + y1 + " " + width + " " + height + " ");

				subImage = image.getSubimage(x1, y1, width, height);
			}
		}
	}

	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/
	/*---------------------------------------------------------Liaison modele couleur-------------------------------------------------------------------*/
	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/

	public BufferedImage fill(BufferedImage biOriginal, Color colOld, Color colNew, int x, int y) {
		return Couleur.fill(biOriginal, colOld, colNew, x, y);
	}

	public BufferedImage fondTransparent(BufferedImage biOriginal, Color colOld, int x, int y) {
		return Couleur.fondTransparent(biOriginal, colOld, x, y);
	}

	public BufferedImage changerContraste(BufferedImage biOriginal, int contraste) {
		return Couleur.changerContraste(biOriginal, contraste);
	}

	public BufferedImage changerLuminosite(BufferedImage biOriginal, int luminosite) {
		return Couleur.changerLuminosite(biOriginal, luminosite);
	}

	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/
	/*------------------------------------------------------------------main----------------------------------------------------------------------------*/
	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/

	public static void main(String[] args) {
		// Lancer l'application depuis le contrôleur
		Controleur controleur = new Controleur();
		controleur.startApplication();
	}
}
