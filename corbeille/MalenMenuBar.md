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

public class MalenMenuBar extends JMenuBar implements ActionListener {
	/* ------------------------------------------------------------ */
	/* Constante pour navigation modulable */
	/* ------------------------------------------------------------ */

	private static final String REPERTOIRE = "./data/images/";

	// Identifiants des éléments du menu
	private static final String MENU = "M";
	private static final String ITEM = "I";
	private static final String SEPARATEUR = "S";
	private static final String SOUS_MENU = "m";
	private static final String ITEM_SM = "i";
	private static final String SEPARATEUR_SM = "s";

	// Position des éléments du menu dans le "modeleBar"
	private static final int TYPE = 0;
	private static final int NAME = 1;
	private static final int ICON = 2;
	private static final int CHAR = 3;
	private static final int KEYS = 4;

	/* ------------------------------------------------------------ */
	/*                                                              */
	/* ------------------------------------------------------------ */

	private MalenFrame parentFrame;
	private JButton btnCouleurAct;
	private String[][] modeleBar;

	/**
	 * Construteur de la navBar
	 * 
	 * @param mainFrame
	 */

	public MalenMenuBar(MalenFrame parentFrame, String[][] modeleBar) {
		super();
		this.parentFrame = parentFrame;
		this.modeleBar = modeleBar;
		this.initComposants();
	}

	public MalenMenuBar(MalenFrame parentFrame) {
		this(parentFrame, MalenMenuBar.getModeleBar());
	}

	/* ------------------------------------------------------------ */
	/* Initialisation des composants */
	/* ------------------------------------------------------------ */

	private void initComposants() {
		JMenu menuEnCreation = null;
		JMenu sousMenuEnCreation = null;
		String hotkey;

		for (int cptLig = 0; cptLig < modeleBar.length; cptLig++) {
			String[] ligne = modeleBar[cptLig];

			switch (ligne[TYPE]) {
				case MENU:
					menuEnCreation = this.creerMenu(ligne[NAME], ligne[ICON], ligne[CHAR]);
					this.add(menuEnCreation);
					break;

				case SOUS_MENU:
					sousMenuEnCreation = this.creerMenu(ligne[NAME], ligne[ICON], ligne[CHAR]);
					menuEnCreation.add(sousMenuEnCreation);
					break;

				case ITEM:
					if (ligne.length - 1 == KEYS) {
						hotkey = ligne[KEYS];
					} else {
						hotkey = null;
					}
					menuEnCreation.add(this.creerMenui(ligne[NAME], ligne[ICON], ligne[CHAR], hotkey));
					break;

				case ITEM_SM:
					if (ligne.length - 1 == KEYS) {
						hotkey = ligne[KEYS];
					} else {
						hotkey = null;
					}
					sousMenuEnCreation.add(this.creerMenui(ligne[NAME], ligne[ICON], ligne[CHAR], hotkey));
					break;

				case SEPARATEUR:
					menuEnCreation.addSeparator();
					break;

				case SEPARATEUR_SM:
					sousMenuEnCreation.addSeparator();
					break;
			}
		}

		this.add(Box.createHorizontalGlue());

		// Ajout du bouton de couleur
		this.btnCouleurAct = new JButton("      ");
		this.btnCouleurAct.setBackground(this.parentFrame.getCurrentColor());
		this.btnCouleurAct.setFocusPainted(false);
		this.btnCouleurAct.setBackground(this.parentFrame.getCurrentColor()); // Couleur de fond
		this.btnCouleurAct.setForeground(this.parentFrame.getCurrentColor());
		this.btnCouleurAct.setOpaque(true);
		this.btnCouleurAct.setBorderPainted(false);
		this.btnCouleurAct.setToolTipText("Couleur actuelle");

		this.btnCouleurAct.addActionListener(this);

		this.add(btnCouleurAct);
	}

	/**
	 * Simplification de la création d'un élément Menu (correspond au premier
	 * niveau)
	 */
	private JMenu creerMenu(String nom, String image, String mnemo) {
		JMenu menuTmp = new JMenu(nom);

		if (!image.equals("")) {
			menuTmp.setIcon(genererIcone(image, 20));
		}

		menuTmp.setMnemonic(mnemo.charAt(0));
		return menuTmp;
	}

	private JMenuItem creerMenui(String nom, String image, String mnemo, String hotkey) {
		JMenuItem menui = new JMenuItem(nom);

		if (!image.equals("")) {
			menui.setIcon(genererIcone(image, 20));
		}

		if (!mnemo.equals("")) {
			menui.setMnemonic(mnemo.charAt(0));
		}

		if (hotkey != null) {
			menui.setAccelerator(getEquivalentKeyStroke(hotkey));
		}

		addItemListenerToMenu(menui);
		return menui;
	}

	public static KeyStroke getEquivalentKeyStroke(String hotkey) {
		String[] setTmp = hotkey.split("\\+");
		String sTmp = "";

		for (int cpt = 0; cpt < setTmp.length - 1; cpt++) {
			sTmp += setTmp[cpt].toLowerCase() + " ";
		}
		sTmp += setTmp[setTmp.length - 1];

		return KeyStroke.getKeyStroke(sTmp);
	}

	/**
	 * Méthode utilitaire permettant de générer une image redimensionnée
	 */
	public static ImageIcon genererIcone(String image, int taille) {
		ImageIcon originalIcon = new ImageIcon(REPERTOIRE + image); // Chemin vers l'icône
		Image resizedImage = originalIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		ImageIcon resizedIcon = new ImageIcon(resizedImage);

		return resizedIcon;
	}

	public void setCouleurButton() {
		this.btnCouleurAct.setBackground(this.parentFrame.getCurrentColor());
		this.btnCouleurAct.setForeground(this.parentFrame.getCurrentColor());
	}

	/**
	 * Méthodes qui permet de récupérer toutes les options du MenuBar
	 */
	public static final String[] getOptionsBarre() {
		List<String> options = new ArrayList<>();

		for (String[] ligne : MalenMenuBar.getModeleBar()) {
			if (ligne[TYPE].equals(ITEM) || ligne[TYPE].equals(ITEM_SM)) {
				options.add(ligne[NAME]);
			}
		}

		return options.toArray(new String[options.size()]);
	}

	// Méthode pour ajouter un ItemListener à chaque JMenuItem
	private void addItemListenerToMenu(JMenuItem menuItem) {
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Quand un élément de menu est sélectionné
				JMenuItem source = (JMenuItem) e.getSource();
				String itemName = source.getText();

				handleMenuAction(itemName);
			}
		});
	}

	// Méthode pour gérer l'action de chaque élément du menu
	private void handleMenuAction(String menuItem) {
		switch (menuItem) {
			case "Enregistrer":
				parentFrame.saveImage("image_malen.png");
				break;
			case "Enregistrer Sous":
				parentFrame.saveImage();
				break;
			case "Ouvrir":
				parentFrame.importImage();
				break;
			case "Remplissage":
				parentFrame.switchCurseur(Controleur.POT_DE_PEINTURE);
				break;
			case "Transparent":
				parentFrame.switchCurseur(Controleur.EFFACE_FOND);
				break;
			case "Luminosité":
				parentFrame.afficherSlider('L');
				parentFrame.switchCurseur(Controleur.LUMINOSITE);
				break;
			case "Constraste":
				parentFrame.afficherSlider('C');
				parentFrame.switchCurseur(Controleur.CONTRASTE);
				break;
			case "Ajouter Texte":
				parentFrame.afficherPanelText();
				parentFrame.switchCurseur(Controleur.TEXT);
				break;
			case "Rotation Axiale":
				parentFrame.afficherSlider('R');
				break;
			case "Retournement Vertical":
				parentFrame.switchRetournementVertical();
				break;
			case "Retournement Horizontal":
				parentFrame.switchRetournementHorizontal();
				break;
			case "Sélection Rectangle":
				parentFrame.switchCurseur(Controleur.SELECTION_RECTANGLE);
				break;
			case "Sélection Ovale":
				parentFrame.switchCurseur(Controleur.SELECTION_OVALE);
				break;
			case "Pipette":
				parentFrame.switchCurseur(Controleur.PIPETTE);
				break;
			case "Palette":
				parentFrame.chooseColor();
				break;
			case "Nouvelle Fenêtre":
				parentFrame.nouvelleFenetre();
				break;
			default:
				break;
		}
	}

	public static String[][] getModeleBar() {
		return new String[][] {
				{ MENU, "Fichier", "fichier.png", "F" },
				{ ITEM, "Ouvrir", "ouvrir.png", "O", "CTRL+O" },
				{ ITEM, "Enregistrer", "sauvegarde.png", "S", "CTRL+S" },
				{ ITEM, "Entregistrer Sous", "sauvegarde.png", "A", "CTRL+SHIFT+S" },
				{ MENU, "Couleur", "couleur.png", "C" },
				{ ITEM, "Remplissage", "remplissage.png", "I" },
				{ ITEM, "Transparent", "transparence.png", "T" },
				{ ITEM, "Luminosité", "luminosite.png", "L" },
				{ ITEM, "Constraste", "contraste.png", "O" },
				{ SEPARATEUR },
				{ ITEM, "Pipette", "pipette.png", "P" },
				{ ITEM, "Palette", "couleur.png", "L" },
				{ MENU, "Texte", "police.png", "P" },
				{ ITEM, "Ajouter Texte", "police.png", "T" },
				{ MENU, "Rotation", "rotation.png", "R" },
				{ ITEM, "Rotation Axiale", "rotation.png", "O" },
				{ ITEM, "Retournement Vertical", "verticale.png", "V" },
				{ ITEM, "Retournement Horizontal", "horizontale.png", "H" },
				{ MENU, "Sélection", "selection.png", "S" },
				{ ITEM, "Sélection Rectangle", "carre.png", "R" },
				{ ITEM, "Sélection Ovale", "cercle.png", "O" },
				{ MENU, "Nouvelle Fenêtre", "frame.png", "E" },
				{ ITEM, "Nouvelle Fenêtre", "frame.png", "E", "CTRL+E" },
		};
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{

        // Ouvrir le sélecteur de couleur
        Color selectedColor = JColorChooser.showDialog
		(
            null,
            "Sélectionnez une couleur",
            this.parentFrame.getCurrentColor()
         );

        // Si une couleur est sélectionnée, appliquer la couleur au label
        if (selectedColor != null) 
		{
            this.parentFrame.setCurrentColor(selectedColor);
        }
	}
}
