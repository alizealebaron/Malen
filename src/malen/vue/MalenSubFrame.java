package malen.vue;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;

import malen.Controleur;
import malen.modele.MalenFrame;

public class MalenSubFrame extends MalenFrame {
    private   MalenMainFrame mainFrame;
	private   MalenMenuBar   subMenuBar;
	protected Controleur controleur;

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


	public MalenSubFrame(MalenMainFrame mainFrame, Controleur controleur) {
		super(controleur);
        this.mainFrame  = mainFrame;
		this.controleur = controleur;

		this.setTitle("Malen - Fenêtre Secondaire");
		this.setLocation(500, 250);

        MalenMenuBar subMenuBar = new MalenMenuBar(this, MODELE_SUB_BAR);
        add(subMenuBar, BorderLayout.NORTH);
	}

	public void subFrame() {
		this.mainFrame.setVisible(false);
		System.out.println("SALUT");
	}

	public void onClickRight(MouseEvent e)
	{
		super.onClickRight(e);
		if (this.controleur.isOnMainFrame()){
			this.controleur.setOnSecondFrame();
			System.out.println("passage en seconde frame");
		}
	}

	public boolean isMainFrame()
	{
		return false;
	}

		public void updateButton () {this.subMenuBar.setCouleurButton ();}
}

