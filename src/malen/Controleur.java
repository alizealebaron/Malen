package malen;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;

import javax.swing.SwingUtilities;

import malen.vue.MalenMainFrame;
import malen.vue.MalenSubFrame;
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
	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/
	/* Attributs */
	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/

	public static final String PIPETTE = "pipette";
	public static final String SOURIS = "souris";
	public static final String POT_DE_PEINTURE = "pot";
	public static final String SELECTION_RECTANGLE = "rectangle";
	public static final String SELECTION_OVALE = "cercle";
	public static final String EFFACE_FOND = "fond";
	public static final String TEXT = "text";
	public static final String LUMINOSITE = "lumi";
	public static final String CONTRASTE = "cont";

	private static final String REPERTOIRE = "./data/images/";

	private MalenMainFrame mainFrame;
	private MalenSubFrame subFrame;

	private boolean onMainFrame;

	private Color currentColor = Color.BLACK; // La couleur actuelle, par défaut noire
	private String curseur;

	private Point point1;
	private Point point2;
	private BufferedImage subImage;

	private int densite;

	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/
	/* Controleurs */
	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/

	public Controleur() {
		this.mainFrame = new MalenMainFrame(this); // Crée la fenêtre principale
		this.curseur = "souris";

		this.point1 = null;
		this.point2 = null;
		this.subImage = null;
		this.onMainFrame = true;
	}

	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/
	/* Accesseurs */
	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/

	public String getCurseur() {
		return this.curseur;
	}

	public Point getPoint1() {
		return this.point1;
	}

	public Point getPoint2() {
		return this.point2;
	}

	public BufferedImage getSubImage() {
		return this.subImage;
	}

	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/
	/* Modificateurs */
	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/

	public void setPoint1(Point point1) {
		this.point1 = point1;
	}

	public void setPoint2(Point point2) {
		this.point2 = point2;
	}

	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/
	/* Méthodes */
	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/

	// Méthode pour démarrer l'application
	public void startApplication() {
		SwingUtilities.invokeLater(() -> {
			mainFrame.setVisible(true);
		});
	}

	public void nouvelleFenetre() {
		subFrame = new MalenSubFrame(this.mainFrame, this);
		subFrame.setVisible(true);
	}

	public void setCurseur(String curseur) {
		this.curseur = curseur;
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		switch (this.curseur) {
			case Controleur.SELECTION_RECTANGLE:
				this.mainFrame.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
				if (this.subFrame != null) {
					this.subFrame.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
				}
				break;

			case Controleur.SELECTION_OVALE:
				this.mainFrame.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
				if (this.subFrame != null) {
					this.subFrame.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
				}
				break;

			case Controleur.PIPETTE:
				this.mainFrame.setCursor(toolkit.createCustomCursor(
						toolkit.getImage(REPERTOIRE + "pipette-retournee.png"), new java.awt.Point(0, 0), "Pipette"));
				if (this.subFrame != null) {
					this.subFrame.setCursor(
							toolkit.createCustomCursor(toolkit.getImage(REPERTOIRE + "pipette-retournee.png"),
									new java.awt.Point(0, 0), "Pipette"));
				}
				break;

			case Controleur.POT_DE_PEINTURE:
				this.mainFrame.setCursor(toolkit.createCustomCursor(toolkit.getImage(REPERTOIRE + "pot.png"),
						new java.awt.Point(0, 15), "Pot"));
				if (this.subFrame != null) {
					this.subFrame.setCursor(toolkit.createCustomCursor(toolkit.getImage(REPERTOIRE + "pot.png"),
							new java.awt.Point(0, 15), "Pot"));
				}
				break;

			case Controleur.EFFACE_FOND:
				this.mainFrame.setCursor(toolkit.createCustomCursor(toolkit.getImage(REPERTOIRE + "transparence.png"),
						new java.awt.Point(0, 0), "Transparence"));
				if (this.subFrame != null) {
					this.subFrame
							.setCursor(toolkit.createCustomCursor(toolkit.getImage(REPERTOIRE + "transparence.png"),
									new java.awt.Point(0, 0), "Transparence"));
				}
				break;

			case Controleur.TEXT:
				this.mainFrame.setCursor(new Cursor(Cursor.TEXT_CURSOR));
				if (this.subFrame != null) {
					this.subFrame.setCursor(new Cursor(Cursor.TEXT_CURSOR));
				}
				break;

			default:
				this.mainFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				if (this.subFrame != null) {
					this.subFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
				break;
		}
		if (!curseur.equals(SELECTION_RECTANGLE)) {
			resetSelection(); // Réinitialiser la sélection quand on sort du mode sélection rectangle
		}
	}

	public void resetSelection() {
		point1 = null;
		point2 = null;
		subImage = null;
	}

	// Méthode pour obtenir la couleur actuelle

	public void setSubImage(BufferedImage image) {
		this.subImage = image;
	}

	public void setOnMainFrame() {
		System.out.println("main frame");
		this.onMainFrame = true;
	}

	public void setOnSecondFrame() {
		System.out.println("second frame");
		this.onMainFrame = false;
	}

	public boolean isOnMainFrame() {
		return this.onMainFrame;
	}

	public boolean isOnSecondFrame() {
		return !this.onMainFrame;
	}

	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/
	/*---------------------------------------------------------Action avec le click---------------------------------------------------------------------*/
	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/

	/**
	 * Méthode permettant de faire une action pendant un click selon l'état du
	 * curseur
	 * 
	 */
	public void onClickLeft(BufferedImage biImage, Color coulPixel, int x, int y) {
		switch (this.curseur) {
			case Controleur.SOURIS:
				break;

			case Controleur.PIPETTE:

				System.out.println("Souris en mode : " + this.curseur);
				System.out.println(coulPixel.toString());
				if (this.isOnMainFrame()) {
					System.out.println("mainframe");
					this.mainFrame.setColor(coulPixel);
				}
				if (this.isOnSecondFrame()) {
					System.out.println("subFrame");
					this.subFrame.setColor(coulPixel);
				}
				break;

			case Controleur.POT_DE_PEINTURE:
				fill(biImage, coulPixel, this.currentColor, x, y);

				break;

			case Controleur.EFFACE_FOND:
				fondTransparent(biImage, coulPixel, x, y);

				break;

			case Controleur.SELECTION_RECTANGLE:
				if (this.subImage != null) { // peut poser des problemes, mettre verif sur point1 et point2
					this.mainFrame.pasteSubImage();
				}

				break;

			case Controleur.SELECTION_OVALE:
				if (this.subImage != null) { // peut poser des problemes, mettre verif sur point1 et point2
					this.mainFrame.pasteSubImage();
				}

				break;

			default:
				System.out.println("Choix incorrect");
				break;

		}
	}

	public void createRectangleSubImage(BufferedImage image) {
		if (image == null) {
			this.subImage = null;
		} else {
			if (point1 != null && point2 != null && point1.x() != point2.x() && point1.y() != point2.y()) {
				int x1 = Math.min(point1.x(), point2.x());
				int y1 = Math.min(point1.y(), point2.y());
				int width = Math.abs(point1.x() - point2.x());
				int height = Math.abs(point1.y() - point2.y());

				System.out.println("subimage rect : " + x1 + " " + y1 + " " + width + " " + height + " ");

				subImage = image.getSubimage(x1, y1, width, height);
			}
		}
	}

	public void createOvalSubImage(BufferedImage image) {

		if (image == null) {
			this.subImage = null;
		} else {
			if (point1 != null && point2 != null && point1.x() != point2.x() && point1.y() != point2.y()) {
				int x1 = Math.min(point1.x(), point2.x());
				int y1 = Math.min(point1.y(), point2.y());
				int width = Math.abs(point1.x() - point2.x());
				int height = Math.abs(point1.y() - point2.y());

				System.out.println("subimage oval : " + x1 + " " + y1 + " " + width + " " + height + " ");

				// Crée une image vide avec un canal alpha (transparence)
				BufferedImage ovalImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

				// Crée un Graphics2D pour dessiner sur l'image ovale
				Graphics2D g2d = ovalImage.createGraphics();

				// Activer l'anti-aliasing pour des bords plus lisses
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

				// Créer une forme ovale
				Shape oval = new java.awt.geom.Ellipse2D.Double(0, 0, width, height);

				// Dessiner l'image source à l'intérieur de l'ovale
				g2d.setClip(oval); // Limite le dessin à la région ovale
				g2d.drawImage(image, -x1, -y1, null); // Ajuste pour positionner la bonne partie de l'image source

				g2d.dispose();
				this.subImage = ovalImage;
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
