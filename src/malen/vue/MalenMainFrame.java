package malen.vue;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.event.MouseEvent;

import malen.Controleur;
import malen.modele.MalenFrame;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;

public class MalenMainFrame extends MalenFrame {

	private MalenImagePanel imagePanel; // Référence au panneau d'image
	private MalenMenuBar menuPanel;

	protected Controleur controleur;

	public MalenMainFrame(Controleur controleur) {
		super(controleur);
		setTitle("Malen - Fenêtre Principale");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Ajouter le panneau de menu
		this.menuPanel = new MalenMenuBar(this);
		add(this.menuPanel, BorderLayout.NORTH);
	}

	public void saveImage() {
		JFileChooser fileChooser = new JFileChooser();
		int userSelection = fileChooser.showSaveDialog(null);

		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File fileToSave = fileChooser.getSelectedFile();
			this.saveImage(fileToSave.getAbsolutePath() + ".png");
		}
	}

	public void saveImage(String fileName) {
		try {
			File outputFile = new File(fileName);
			ImageIO.write(this.imagePanel.getImage(), "png", outputFile);
		} catch (IOException e) {
			System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
		}
	}

	public void onClickRight(MouseEvent e) {
		if (this.controleur.isOnSecondFrame()) {
			this.controleur.setOnMainFrame();
			System.out.println("passage en seconde frame");
		} else {

			JPopupMenu contextMenu = new JPopupMenu();

			JMenuItem copyItem = new JMenuItem("Copier");
			copyItem.addActionListener(actionEvent -> {
				System.out.println("Option 'Copier' sélectionnée.");
				// if (isSelected)
			});
			contextMenu.add(copyItem);

			JMenuItem pasteItem = new JMenuItem("Coller");
			pasteItem.addActionListener(actionEvent -> {
				System.out.println("Option 'Coller' sélectionnée.");
			});
			contextMenu.add(pasteItem);

			contextMenu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	public void nouvelleFenetre() {
		super.controleur.nouvelleFenetre();
	}

	public void updateButton() {
		this.menuPanel.setCouleurButton();
	}
}