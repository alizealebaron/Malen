package malen.vue;

import javax.swing.*;
import java.awt.event.*;

public class MalenMenuBar extends JMenuBar {

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
		JMenu textMenu = new JMenu("Texte");
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
		JMenuItem planeItem = new JMenuItem("Rotation Plane");
		rotationMenu.add(axialItem);
		rotationMenu.add(planeItem);

		// Ajouter l'ItemListener à chaque JMenuItem
		addItemListenerToMenu(axialItem);
		addItemListenerToMenu(planeItem);

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
				break;
			case "Fond":
				System.out.println("ouai, tu utilise : " + menuItem);
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
				System.out.println("ouai, tu utilise : " + menuItem);
				break;
			case "Rotation Plane":
				mainFrame.switchRotationPlane();
				break;
			case "Sélection Rectangle":
				System.out.println("ouai, tu utilise : " + menuItem);
				break;
			case "Sélection Ovale":
				System.out.println("ouai, tu utilise : " + menuItem);
				break;
			case "Choix de Couleur sur l'image":
				mainFrame.switchPipette();
				break;
			case "Choix de Couleur":
				mainFrame.chooseColor();
				break;
			default:
				System.out.println("Action non définie pour " + menuItem);
				break;
		}
	}
}
