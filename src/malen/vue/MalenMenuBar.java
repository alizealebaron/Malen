package malen.vue;

import javax.swing.*;
import malen.Controleur;
import java.awt.event.*;
import java.awt.*;

public class MalenMenuBar extends JMenuBar 
{
	/* ------------------------------------------------------------ */
	/*             Constante pour navigation modulable              */
	/* ------------------------------------------------------------ */

	private static final String REPERTOIRE = "./data/images/";

	// Identifiants des éléments du menu
	private static final String MENU            = "M";
	private static final String ITEM            = "I";
	private static final String SEPARATEUR      = "S";
	private static final String SOUS_MENU       = "m";
	private static final String ITEM_SM         = "i";
	private static final String SEPARATEUR_SM   = "s";

	// Position des éléments du menu dans le "modeleBar"
	private static final int TYPE = 0;
	private static final int NAME = 1;
	private static final int ICON = 2;
	private static final int CHAR = 3;
	private static final int KEYS = 4;

	/* ------------------------------------------------------------ */
	/*                                                              */
	/* ------------------------------------------------------------ */

	private MalenMainFrame mainFrame; // Référence vers MainFrame
	

	public MalenMenuBar(MalenMainFrame mainFrame) {
		this.mainFrame = mainFrame;

		// Configuration du panneau principal
		JMenu fileMenu = new JMenu("Fichier");
		this.add(fileMenu);
		JMenuItem saveItem = new JMenuItem("Sauvegarder");
		JMenuItem saveAsItem = new JMenuItem("Sauvegarder sous");
		JMenuItem importItem = new JMenuItem("Importer");
		fileMenu.add(saveItem);
		fileMenu.add(saveAsItem);
		fileMenu.add(importItem);

		// Ajouter l'ItemListener à chaque JMenuItem
		addItemListenerToMenu(saveItem);
		addItemListenerToMenu(saveAsItem);
		addItemListenerToMenu(importItem);

		// Menu Couleur
		JMenu colorMenu = new JMenu("Couleur");
		this.add(colorMenu);
		JMenuItem paintItem = new JMenuItem("Pot de peinture");
		JMenuItem darkItem = new JMenuItem("Fond");
		JMenuItem luminanceItem = new JMenuItem("Luminance");
		JMenuItem bwItem = new JMenuItem("Noir et Blanc");
		colorMenu.add(paintItem);
		colorMenu.add(darkItem);
		colorMenu.add(luminanceItem);
		colorMenu.add(bwItem);

		// Ajouter l'ItemListener à chaque JMenuItem
		addItemListenerToMenu(paintItem);
		addItemListenerToMenu(darkItem);
		addItemListenerToMenu(luminanceItem);
		addItemListenerToMenu(bwItem);

		// Menu Texte

		ImageIcon originalIcon = new ImageIcon(REPERTOIRE + "police.png"); // Chemin vers l'icône
        Image resizedImage = originalIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

		JMenu textMenu = new JMenu("");
		textMenu.setIcon(resizedIcon);
		this.add(textMenu);
		JMenuItem fontItem = new JMenuItem("Police");
		JMenuItem colorItem = new JMenuItem("Couleur");
		JMenuItem textureItem = new JMenuItem("Texture");
		JMenuItem boldItalicItem = new JMenuItem("Gras/Italique");
		textMenu.add(fontItem);
		textMenu.add(colorItem);
		textMenu.add(textureItem);
		textMenu.add(boldItalicItem);

		// Ajouter l'ItemListener à chaque JMenuItem
		addItemListenerToMenu(fontItem);
		addItemListenerToMenu(colorItem);
		addItemListenerToMenu(textureItem);
		addItemListenerToMenu(boldItalicItem);

		// Menu Rotation
		JMenu rotationMenu = new JMenu("Rotation");
		this.add(rotationMenu);
		JMenuItem axialItem = new JMenuItem("Rotation Axial");
		JMenuItem mirrorVertiItem = new JMenuItem("Retournement vertical");
		JMenuItem mirrorHorizItem = new JMenuItem("Retournement horizontal");
		rotationMenu.add(axialItem);
		rotationMenu.add(mirrorVertiItem);
		rotationMenu.add(mirrorHorizItem);

		// Ajouter l'ItemListener à chaque JMenuItem
		addItemListenerToMenu(axialItem);
		addItemListenerToMenu(mirrorVertiItem);
		addItemListenerToMenu(mirrorHorizItem);

		// Menu Sélection
		JMenu selectionMenu = new JMenu("Sélection");
		this.add(selectionMenu);
		JMenuItem rectItem = new JMenuItem("Sélection Rectangle");
		JMenuItem ovalItem = new JMenuItem("Sélection Ovale");
		selectionMenu.add(rectItem);
		selectionMenu.add(ovalItem);

		// Ajouter l'ItemListener à chaque JMenuItem
		addItemListenerToMenu(rectItem);
		addItemListenerToMenu(ovalItem);

		// Menu Pipette
		JMenu pipetteMenu = new JMenu("Pipette");
		this.add(pipetteMenu);
		JMenuItem colorPickerImageItem = new JMenuItem("Choix de Couleur sur l'image");
		JMenuItem colorPickerItem = new JMenuItem("Choix de Couleur");
		pipetteMenu.add(colorPickerImageItem);
		pipetteMenu.add(colorPickerItem);

		// Ajouter l'ItemListener à chaque JMenuItem
		addItemListenerToMenu(colorPickerImageItem);
		addItemListenerToMenu(colorPickerItem);
	}

	// Méthode pour ajouter un ItemListener à chaque JMenuItem
	private void addItemListenerToMenu(JMenuItem menuItem) {
		System.out.println(menuItem.getText());
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Quand un élément de menu est sélectionné
				JMenuItem source = (JMenuItem) e.getSource();
				String itemName = source.getText();
				System.out.println("Action sur l'élément: " + itemName);

				handleMenuAction(itemName);
			}
		});
	}

	// Méthode pour gérer l'action de chaque élément du menu
	private void handleMenuAction(String menuItem) {
		switch (menuItem) {
			case "Sauvegarder":
				this.mainFrame.saveImageToFile("output.png");
				break;
			case "Sauvegarder sous":
				System.out.println("ouai, tu utilise : " + menuItem);
				break;
			case "Importer":
				mainFrame.importImage();
				break;
			case "Pot de peinture":
				System.out.println("ouai, tu utilise : " + menuItem);
				mainFrame.switchCurseur(Controleur.POT_DE_PEINTURE);
				break;
			case "Fond":
				System.out.println("ouai, tu utilise : " + menuItem);
				mainFrame.switchCurseur(Controleur.EFFACE_FOND);
				break;
			case "Luminance":
				System.out.println("ouai, tu utilise : " + menuItem);
				break;
			case "Noir et Blanc":
				System.out.println("ouai, tu utilise : " + menuItem);
				break;
			case "Police":
				System.out.println("ouai, tu utilise : " + menuItem);
				break;
			case "Couleur":
				System.out.println("ouai, tu utilise : " + menuItem);
				break;
			case "Texture":
				System.out.println("ouai, tu utilise : " + menuItem);
				break;
			case "Gras/Italique":
				System.out.println("ouai, tu utilise : " + menuItem);
				break;
			case "Rotation Axial":
				mainFrame.rotationAxiale();
				break;
			case "Retournement vertical":
				mainFrame.switchRetournementVertical();
				break;
			case "Retournement horizontal":
				mainFrame.switchRetournementHorizontal();
				break;
			case "Sélection Rectangle":
				mainFrame.switchCurseur(Controleur.SELECTION_RECTANGLE);
				break;
			case "Sélection Ovale":
				mainFrame.switchCurseur(Controleur.SELECTION_OVALE);
				break;
			case "Choix de Couleur sur l'image":
				mainFrame.switchCurseur(Controleur.PIPETTE);
				break;
			case "Choix de Couleur":
				mainFrame.chooseColor();
				break;
			default:
				System.out.println("Action non définie pour " + menuItem);
				break;
		}
	}

	public static String[][] getModeleBar ( )
	{
		return new String[][] {
			{	MENU, 				                "",			   "fichier.png",		"F"				    },
			{		ITEM, 			     "Sauvegarder",			"sauvegarde.png",		"S", "CTRL+S"	    },
			{		ITEM, 			"Sauvegarder Sous",			"sauvegarde.png",		"A", "CTRL+SHIFT+S"	},
			{		ITEM, 			          "Ouvrir",			    "ouvrir.png",		"O", "CTRL+O"	    },
			{	MENU, 			                	"",			   "couleur.png",		"C"	             	},
			{		ITEM, 			     "Remplissage",		   "remplissage.png",	    "I"			    	},
			{		ITEM, 			     "Transparent",		  "transparence.png",	    "T"			    	},
			{		ITEM, 			      "Luminosité",	        "luminosite.png",	    "L"			     	},
			{		ITEM, 			      "Constraste",	        "constraste.png",	    "C"			     	},
			{	MENU, 				                "",		        "police.png",		"P"				    },
			{	MENU, 				                "",		      "rotation.png",		"R"				    },
			{		ITEM, 			 "Rotation Axiale",	          "rotation.png",	    "O"			     	},
			{		ITEM, 	   "Retournement Vertical",	         "verticale.png",	    "V"			     	},
			{		ITEM,  "Retournement Horizontale",	       "horizontale.png",	    "H"			     	},
			{	MENU, 				                "",		     "selection.png",		"S"				    },
			{		ITEM, 	               "Rectangle",	         "verticale.png",	    "R"			     	},
			{		ITEM,                       "Oval",	       "horizontale.png",	    "O"			     	},
		};
	}
}
