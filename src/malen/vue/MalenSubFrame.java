package malen.vue;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import malen.Controleur;

public class MalenSubFrame extends MalenMainFrame
{
	private static final String[][] MODELE_SUB_BAR = {
			{	 "M", 				         "Fichier",			   "fichier.png",		"F"				    },
			{		 "I", 			          "Ouvrir",			    "ouvrir.png",		"O", "CTRL+O"	    },
			{		 "I", 		   			"Exporter",			"sauvegarde.png",		"E",				},
			{	 "M", 			             "Couleur",			   "couleur.png",		"C"	             	},
			{		 "I", 			     "Remplissage",		   "remplissage.png",	    "I"			    	},
			{		 "I", 			     "Transparent",		  "transparence.png",	    "T"			    	},
			{		 "I", 			      "Luminosité",	        "luminosite.png",	    "L"			     	},
			{		 "I", 			      "Constraste",	         "contraste.png",	    "O"			     	},
			{		 "S"	 														                 		},
			{		 "I", 			         "Pipette",	           "pipette.png",	    "P"			     	},
			{		 "I", 			         "Palette",	           "couleur.png",	    "L"			     	},
			{	 "M", 				           "Texte",		        "police.png",		"P"				    },
			{	 "M", 				        "Rotation",		      "rotation.png",		"R"				    },
			{		 "I", 			 "Rotation Axiale",	          "rotation.png",	    "O"			     	},
			{		 "I", 	   "Retournement Vertical",	         "verticale.png",	    "V"			     	},
			{		 "I",    "Retournement Horizontal",	       "horizontale.png",	    "H"			     	},
			{	 "M", 				       "Sélection",		     "selection.png",		"S"				    },
			{		 "I", 	     "Sélection Rectangle",	             "carre.png",	    "R"			     	},
			{		 "I",            "Sélection Ovale",	            "cercle.png",	    "O"			     	},
	};


	public MalenSubFrame(Controleur controleur)
	{
		super(controleur);
		this.setTitle("Malen - Fenêtre Secondaire");
		this.setLocation(500, 250);

		this.setJMenuBar(null);

        MalenMenuBar subMenuBar = new MalenMenuBar(this, MODELE_SUB_BAR);
        add(subMenuBar, BorderLayout.NORTH);
	}
}
