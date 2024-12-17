package malen;

import java.awt.Color;

import javax.swing.SwingUtilities;

import malen.vue.MalenMainFrame;
import malen.modele.Couleur;
import java.awt.image.BufferedImage;

/**
 * Classe Controleur
 * 
 * @author : Alizéa Lebaron
 * @author : Tom Goureau
 * @version : 1.0.0 - 16/12/2024
 * @date : 16/12/2024
 */

public class Controleur 
{
	/**
	 * 
	 */
	public static final String PIPETTE             =   "pipette";
	public static final String SOURIS              =    "souris";
	public static final String POT_DE_PEINTURE     =       "pot";
	public static final String SELECTION_RECTANGLE = "rectangle";
	public static final String SELECTION_OVALE     =    "cercle";
	public static final String EFFACE_FOND         =      "fond";

	private MalenMainFrame mainFrame;

	private Color currentColor = Color.GREEN; // La couleur actuelle, par défaut noire
	private String curseur;

	public Controleur() 
	{
		mainFrame = new MalenMainFrame(this); // Crée la fenêtre principale
		this.curseur = "souris";
	}

	// Méthode pour démarrer l'application
	public void startApplication() {
		SwingUtilities.invokeLater(() -> {
			mainFrame.setVisible(true);
		});
	}

	public void setCurseur(String curseur)
	{
		this.curseur = curseur;
	}

	public String getCurseur()
	{
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

	/* ------------------------------------------------------------------------------------------------------------------------------------------------ */
	/*                                                          Action avec le click                                                                    */
	/* ------------------------------------------------------------------------------------------------------------------------------------------------ */

	/** Méthode permettant de faire une action pendant un click selon l'état du curseur
	 * 
	 */
	public void onClick (BufferedImage biImage, Color coulPixel, int x, int y)
	{
		switch (this.curseur) 
		{
			case Controleur.SOURIS:

            	System.out.println("Souris en mode : " + this.curseur);
				break;

			case Controleur.PIPETTE:

				System.out.println("Souris en mode : " + this.curseur);
				break;
			
			case Controleur.POT_DE_PEINTURE:

				System.out.println("Souris en mode : " + this.curseur);

				fill(biImage, coulPixel, this.currentColor, x, y);

				break;

			case Controleur.EFFACE_FOND:

				System.out.println("Souris en mode : " + this.curseur);
				
				fondTransparent(biImage, coulPixel, x, y);

				break;

			default:
				System.out.println("Choix incorrect");
				break;
		}
	}

	/* ------------------------------------------------------------------------------------------------------------------------------------------------ */
	/*                                                         Liaison Modele Couleur                                                                   */
	/* ------------------------------------------------------------------------------------------------------------------------------------------------ */

	public BufferedImage fill(BufferedImage biOriginal, Color colOld, Color colNew, int x, int y)
	{
		return Couleur.fill(biOriginal, colOld, colNew, x, y);
	}

	public BufferedImage fondTransparent(BufferedImage biOriginal, Color colOld, int x, int y)
	{
		return Couleur.fondTransparent(biOriginal, colOld, x, y);
	}

	public BufferedImage changerContraste(BufferedImage biOriginal, int contraste)
	{
		return Couleur.changerContraste(biOriginal, contraste);
	}

	public BufferedImage changerLuminosite(BufferedImage biOriginal, int luminosite)
	{
		return Couleur.changerLuminosite(biOriginal, luminosite);
	}

	/* ------------------------------------------------------------------------------------------------------------------------------------------------ */
	/*                                                                    Main                                                                          */
	/* ------------------------------------------------------------------------------------------------------------------------------------------------ */

	public static void main(String[] args) {
		// Lancer l'application depuis le contrôleur
		Controleur controleur = new Controleur();
		controleur.startApplication();
	}
}