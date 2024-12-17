package malen.vue;

/**
 * Classe MenuBar
 * 
 * @author : Alizéa Lebaron
 * @author : Tom Goureau
 * @version : 1.0.0 - 16/12/2024
 * @date : 16/12/2024
 */

import javax.swing.*;
import malen.Controleur;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.*;
import java.util.List;

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

	private MalenMainFrame mainFrame;

	/** Construteur de la navBar
	 * @param mainFrame
	 */
	public MalenMenuBar(MalenMainFrame mainFrame) 
	{
		//Initialisation
		super ( );

		this.mainFrame = mainFrame;

		this.initComposants ( );
	}

	/* ------------------------------------------------------------ */
	/*                 Initialisation des composants                */
	/* ------------------------------------------------------------ */

	private void initComposants ( )
	{
		JMenu  menuEnCreation     = null;
		JMenu  sousMenuEnCreation = null;
		String hotkey;

		// Format du MenuBar
		String[][] modeleBar = MalenMenuBar.getModeleBar ( );

		// Générer les composants
		for ( int cptLig = 0; cptLig < modeleBar.length; cptLig++ )
		{
			String[] ligne = modeleBar[cptLig];

			switch ( ligne[TYPE] )
			{
				case MENU:
					menuEnCreation = this.creerMenu ( ligne[NAME], ligne[ICON], ligne[CHAR] );
					this.add ( menuEnCreation );
					break;

				case SOUS_MENU:
					sousMenuEnCreation = this.creerMenu ( ligne[NAME], ligne[ICON], ligne[CHAR] );
					menuEnCreation.add ( sousMenuEnCreation );
					break;

				case ITEM:
					if ( ligne.length-1 == KEYS ) { hotkey = ligne[KEYS]; }
					else { hotkey = null; }
					menuEnCreation.add ( this.creerMenui ( ligne[NAME], ligne[ICON], ligne[CHAR], hotkey ) );
					break;

				case ITEM_SM:
					if ( ligne.length-1 == KEYS ) { hotkey = ligne[KEYS]; }
					else { hotkey = null; }
					sousMenuEnCreation.add ( this.creerMenui ( ligne[NAME], ligne[ICON], ligne[CHAR], hotkey ) );
					break;

				case SEPARATEUR:
					menuEnCreation.addSeparator ( );
					break;

				case SEPARATEUR_SM:
					sousMenuEnCreation.addSeparator ( );
					break;
			}
		}
	}

	/**
	 * Simplification de la création d'un élément Menu (correspond au premier niveau)
	 */
	private JMenu creerMenu ( String nom, String image, String mnemo )
	{
		JMenu menuTmp = new JMenu ( nom );

		if ( !image.equals ( "" ) )
		{
			menuTmp.setIcon ( genererIcone( image, 20 ) );
		}

		menuTmp.setMnemonic ( mnemo.charAt( 0 ) );
		return menuTmp;
	}

	private JMenuItem creerMenui ( String nom, String image, String mnemo, String hotkey )
	{
		JMenuItem menui = new JMenuItem ( nom );

		if ( !image.equals ( "" ) )
		{
			menui.setIcon ( genererIcone ( image, 20 ) );
		}

		if ( !mnemo.equals( "" ) )
		{
			menui.setMnemonic ( mnemo.charAt ( 0 ) );
		}

		if ( hotkey != null )
		{
			menui.setAccelerator( getEquivalentKeyStroke ( hotkey ) );
		}

		addItemListenerToMenu(menui);
		return menui;
	}

	public static KeyStroke getEquivalentKeyStroke ( String hotkey )
	{
		String[] setTmp = hotkey.split ( "\\+" );
		String sTmp="";

		for ( int cpt=0; cpt<setTmp.length-1; cpt++ )
		{
			sTmp += setTmp[cpt].toLowerCase ( ) + " ";
		}
		sTmp += setTmp[ setTmp.length-1 ];

		return KeyStroke.getKeyStroke ( sTmp );
	}

	/**
	 * Méthode utilitaire permettant de générer une image redimensionnée
	 */
	public static ImageIcon genererIcone ( String image, int taille )
	{
		ImageIcon originalIcon = new ImageIcon(REPERTOIRE + image); // Chemin vers l'icône
    	Image resizedImage = originalIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
    	ImageIcon resizedIcon = new ImageIcon(resizedImage);

		return resizedIcon;
	}

	/**
	 * Méthodes qui permet de récupérer toutes les options du MenuBar
	 */
	public static final String[] getOptionsBarre ( )
	{
		List<String> options = new ArrayList<> ( );

		for ( String[] ligne : MalenMenuBar.getModeleBar ( ) )
		{
			if ( ligne[TYPE].equals ( ITEM ) || ligne[TYPE].equals ( ITEM_SM ) )
			{
				options.add ( ligne[NAME] );
			}
		}

		return options.toArray ( new String[options.size ( )] );
	}


	// Méthode pour ajouter un ItemListener à chaque JMenuItem
	private void addItemListenerToMenu(JMenuItem menuItem) 
	{
		System.out.println(menuItem.getText());
		menuItem.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
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
			case "Ouvrir":
				mainFrame.importImage();
				break;
			case "Remplissage":
				System.out.println("ouai, tu utilise : " + menuItem);
				mainFrame.switchCurseur(Controleur.POT_DE_PEINTURE);
				break;
			case "Transparent":
				System.out.println("ouai, tu utilise : " + menuItem);
				mainFrame.switchCurseur(Controleur.EFFACE_FOND);
				break;
			case "Luminosité":
				mainFrame.afficherSlider('L');
				System.out.println("ouai, tu utilise : " + menuItem);
				break;
			case "Constraste":
				mainFrame.afficherSlider('C');
				System.out.println("ouai, tu utilise : " + menuItem);
				break;
			case "Police":
				System.out.println("ouai, tu utilise : " + menuItem);
				break;
			case "Rotation Axiale":
				mainFrame.afficherSlider('R');
				break;
			case "Retournement Vertical":
				mainFrame.switchRetournementVertical();
				break;
			case "Retournement Horizontal":
				mainFrame.switchRetournementHorizontal();
				break;
			case "Sélection Rectangle":
				mainFrame.switchCurseur(Controleur.SELECTION_RECTANGLE);
				break;
			case "Sélection Ovale":
				mainFrame.switchCurseur(Controleur.SELECTION_OVALE);
				break;
			case "Pipette":
				mainFrame.switchCurseur(Controleur.PIPETTE);
				break;
			case "Palette":
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
			{	MENU, 				         "Fichier",			   "fichier.png",		"F"				    },
			{		ITEM, 			          "Ouvrir",			    "ouvrir.png",		"O", "CTRL+O"	    },
			{		ITEM, 			     "Enregistrer",			"sauvegarde.png",		"S", "CTRL+S"	    },
			{		ITEM, 		   "Entregistrer Sous",			"sauvegarde.png",		"A", "CTRL+SHIFT+S"	},
			{	MENU, 			             "Couleur",			   "couleur.png",		"C"	             	},
			{		ITEM, 			     "Remplissage",		   "remplissage.png",	    "I"			    	},
			{		ITEM, 			     "Transparent",		  "transparence.png",	    "T"			    	},
			{		ITEM, 			      "Luminosité",	        "luminosite.png",	    "L"			     	},
			{		ITEM, 			      "Constraste",	         "contraste.png",	    "O"			     	},
			{		SEPARATEUR														                 		},
			{		ITEM, 			         "Pipette",	           "pipette.png",	    "P"			     	},
			{		ITEM, 			         "Palette",	           "couleur.png",	    "L"			     	},
			{	MENU, 				           "Texte",		        "police.png",		"P"				    },
			{	MENU, 				        "Rotation",		      "rotation.png",		"R"				    },
			{		ITEM, 			 "Rotation Axiale",	          "rotation.png",	    "O"			     	},
			{		ITEM, 	   "Retournement Vertical",	         "verticale.png",	    "V"			     	},
			{		ITEM,    "Retournement Horizontal",	       "horizontale.png",	    "H"			     	},
			{	MENU, 				       "Sélection",		     "selection.png",		"S"				    },
			{		ITEM, 	     "Sélection Rectangle",	             "carre.png",	    "R"			     	},
			{		ITEM,            "Sélection Ovale",	            "cercle.png",	    "O"			     	},
		};
	}
}
