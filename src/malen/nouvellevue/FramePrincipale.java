package malen.nouvellevue;

/** Frame principale de l'application
 * @author  : Alizéa Lebaron
 * @author  : Tom Goureau
 * @author  : Trystan Baillobay
 * @version : 1.0.0 - 19/12/2024
 * @since   : 19/12/2024
 */

import javax.imageio.ImageIO;
import javax.swing.*;

import malen.Controleur;
import malen.modele.Point;
import malen.vue.MalenImagePanel;
import malen.vue.MalenMenuBar;

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

public class FramePrincipale extends JFrame 
{
	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/
	/*                                                                 Attributs                                                                        */
	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/

	// Chemin vers le répertoire d'images
	private static final String REPERTOIRE = "./data/images/";

	// Lien avec les panels
	private PanelPrincipal   panelPrincipal; 
	private PanelMenu        panelMenu;
	private PanelOutils      panelOutils;
	private PanelImage       panelImage;

	private JScrollBar       scPanelPrincipal;

	// Lien avec le controleur
	private Controleur       controleur;

	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/
	/*                                                                Controleurs                                                                       */
	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/

	public FramePrincipale ( Controleur controleur ) 
	{
		// Récupération du controleur
		this.controleur = controleur;
		
		// Initialisation des paramètres de la frame principale
		this.setTitle("Malen"); // Titre de la frame
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Fermeture quand on clique sur la croix
		this.setSize(1650,1080); // Taille de référence
		this.setExtendedState(JFrame.MAXIMIZED_BOTH); // Plein écran
		this.setLayout(new BorderLayout());

		// Ajouter le panel menu
		this.panelMenu = new PanelMenu(this);
		this.add(this.panelMenu, BorderLayout.NORTH);

		// Initialisation du panel outil et du panel image
		this.panelImage  = new PanelImage (this);
		this.panelOutils = new PanelOutils(this);

		// Ajouter le panel principale
		this.panelPrincipal   = new PanelPrincipal (this, this.panelImage, this.panelOutils);
		this.add(this.panelPrincipal, BorderLayout.CENTER);

		// Afficher la fenêtre
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/
	/*                                                                Accesseurs                                                                        */
	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/

	public Color         getCurrentColor ( ) { return this.controleur.getCurrentColor ( ); }
	public Point         getPoint1       ( ) { return this.controleur.getPoint1       ( ); }
	public Point         getPoint2       ( ) { return this.controleur.getPoint2       ( ); }
	public BufferedImage getSubImage     ( ) { return this.controleur.getSubImage     ( ); }

	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/
	/*                                                               Modificateurs                                                                      */
	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/

	public void setPoint1   ( Point         point1 ) { this.controleur.setPoint1   ( point1 );}
	public void setPoint2   ( Point         point2 ) { this.controleur.setPoint2   ( point2 );}
	public void setSubimage ( BufferedImage image  ) { this.controleur.setSubImage ( image  );}

	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/
	/*                                                                 Méthodes                                                                         */
	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/

	/* ------------------------------------------------------------ */
	/*                      Gestion du curseur                      */
	/* ------------------------------------------------------------ */

	/** Changement du curseur dans le controleur
	 * @param curseur
	 */
	public void switchCurseur(String curseur) 
	{
		if (this.controleur.getCurseur().equals(curseur)) 
		{
			this.controleur .setCurseur(Controleur.SOURIS);
			scPanelPrincipal.setCursor (new Cursor(Cursor.DEFAULT_CURSOR));
		} 
		else 
		{
			this.controleur.setCurseur(curseur);
			this.changerApparenceCurseur(curseur);
		}
	}

	/** Permet de changer le curseur selon le mode actuel
	 * @param curseur Le mod activé
	 */
	private void changerApparenceCurseur (String curseur)
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
				scPanelPrincipal.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
				break;

			case Controleur.SELECTION_OVALE:
				scPanelPrincipal.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
				break;

			case Controleur.PIPETTE:
				scPanelPrincipal.setCursor(pipette);
				break;

			case Controleur.POT_DE_PEINTURE:
				scPanelPrincipal.setCursor(pot);
				break;

			case Controleur.EFFACE_FOND:
				scPanelPrincipal.setCursor(trans);
				break;

			case Controleur.TEXT:
				scPanelPrincipal.setCursor(new Cursor(Cursor.TEXT_CURSOR));
				break;
		
			default:
				scPanelPrincipal.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				break;
		}
	}

	/* ------------------------------------------------------------ */
	/*                   Passerelle Outils-Image                    */
	/* ------------------------------------------------------------ */
	
	public void          repaintImage ( ) {        this.panelImage.repaint  ( ) ; }
	public boolean       isImage      ( ) { return this.panelImage.isImage  ( ) ; }
	public BufferedImage getImage     ( ) { return this.panelImage.getImage ( ) ; }

	/* ------------------------------------------------------------ */
	/*                     Gestion des outils                       */
	/* ------------------------------------------------------------ */

	public void changerContraste  (int value) { this.controleur.changerContraste  (this.panelImage.getImage(), value); }
	public void changerLuminosite (int value) { this.controleur.changerLuminosite (this.panelImage.getImage(), value); }

}


