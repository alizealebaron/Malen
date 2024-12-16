package malen;

import java.awt.Color;

import javax.swing.SwingUtilities;

import malen.vue.MalenMainFrame;

/**
 * Classe Controleur
 * 
 * @author : Alizéa Lebaron
 * @author : Tom Goureau
 * @version : 1.0.0 - 16/12/2024
 * @date : 16/12/2024
 */

public class Controleur {
	public static final String PIPETTE = "pipette";
	public static final String SOURIS = "souris";

	private MalenMainFrame mainFrame;

	private Color currentColor = Color.BLACK; // La couleur actuelle, par défaut noire
	private String curseur;

	public Controleur() {
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

	public static void main(String[] args) {
		// Lancer l'application depuis le contrôleur
		Controleur controleur = new Controleur();
		controleur.startApplication();
	}
}