package malen.vue;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;


import malen.Controleur;

public class FrameSecondaire extends Frame
{
	private PanelMenu panelMenu;

	private static final String[][] MODELE_SUB_BAR = 
	{
		{	"M", 				         "Fichier",			   "fichier.png",		"F"				    },
		{		"I", 			          "Ouvrir",			    "ouvrir.png",		"O", "CTRL+O"	    },
		{	"M", 			             "Couleur",			   "couleur.png",		"C"	             	},
		{		"I", 			     "Remplissage",		   "remplissage.png",	    "I"			    	},
		{		"I", 			     "Transparent",		  "transparence.png",	    "T"			    	},
		{		"I", 			      "Luminosité",	        "luminosite.png",	    "L"			     	},
		{		"I", 			      "Constraste",	         "contraste.png",	    "O"			     	},
		{		"S"													     	                     		},
		{		"I", 			         "Pipette",	           "pipette.png",	    "P"			     	},
		{		"I", 			         "Palette",	           "couleur.png",	    "L"			     	},
		{	"M", 				           "Texte",		        "police.png",		"P"				    },
		{		"I", 			   "Ajouter Texte",		        "police.png",		"T"				    },			
		{	"M", 				        "Rotation",		      "rotation.png",		"R"				    },
		{		"I", 			  "Rotation Plane",	          "rotation.png",	    "O"			     	},
		{		"I", 	   "Retournement Vertical",	         "verticale.png",	    "V"			     	},
		{		"I",     "Retournement Horizontal",	       "horizontale.png",	    "H"			     	},
		{	"M", 				       "Sélection",		     "selection.png",		"S"				    },
		{		"I", 	     "Sélection Rectangle",	             "carre.png",	    "R"			     	},
		{		"I",             "Sélection Ovale",	            "cercle.png",	    "O"			     	}
	};

	public FrameSecondaire(FramePrincipale mainFrame, Controleur controleur)
	{
		super(controleur);
		this.setTitle("Malen - Fenêtre Secondaire");
		this.setLocation(500, 250);

		this.panelMenu = new PanelMenu(this, MODELE_SUB_BAR);
		this.add(this.panelMenu, BorderLayout.NORTH);
	}

	public void onClickRight(MouseEvent e)
	{
		if (this.controleur.isOnMainFrame())
		{
			this.controleur.setOnSecondFrame();
			System.out.println("passage en seconde frame");
		}
	}

	public void updateButton() { this.panelMenu.setCouleurButton(); }

	public boolean isMainFrame() { return false; }
}