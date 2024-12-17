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
	public static final String PIPETTE = "pipette";
	public static final String SOURIS = "souris";

  	public String etatCurseur;

	private MalenMainFrame mainFrame;
	private Couleur        couleur;

	private Color currentColor = Color.BLACK; // La couleur actuelle, par défaut noire
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

	public void onClick()
	{
		// switch (this.etatCurseur) 
		// {
		// 	case value->
				
		// 		break;
		
		// 	default:
		// 		break;
		// }
	}

	/* ------------------------------------------------------------------------------------------------------------------------------------------------ */
	/*                                                         Liaison Modele Couleur                                                                   */
	/* ------------------------------------------------------------------------------------------------------------------------------------------------ */

	public BufferedImage fill(BufferedImage biOriginal, Color colOld, Color colNew, int x, int y)
	{
		return this.couleur.fill(biOriginal, colOld, colNew, x, y);
	}

	public BufferedImage fondTransparent(BufferedImage biOriginal, Color colOld, int x, int y)
	{
		return this.couleur.fondTransparent(biOriginal, colOld, x, y);
	}

	public BufferedImage changerContraste(BufferedImage biOriginal, int contraste)
	{
		return this.couleur.changerContraste(biOriginal, contraste);
	}

	public BufferedImage changerLuminosite(BufferedImage biOriginal, int luminosite)
	{
		return this.couleur.changerLuminosite(biOriginal, luminosite);
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