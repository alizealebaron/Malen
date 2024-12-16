package malen;

import java.awt.Color;

import javax.swing.JColorChooser;
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
	private Color currentColor = Color.BLACK; // La couleur actuelle, par défaut noire
	private MalenMainFrame mainFrame;

	public Controleur() {
		mainFrame = new MalenMainFrame(this); // Crée la fenêtre principale
	}

	// Méthode pour démarrer l'application
	public void startApplication() {
		SwingUtilities.invokeLater(() -> {
			mainFrame.setVisible(true);
		});
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