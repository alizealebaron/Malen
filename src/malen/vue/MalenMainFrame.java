package malen.vue;

import javax.imageio.ImageIO;
import javax.swing.*;

import malen.Controleur;
import malen.modele.MalenFrame;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class MalenMainFrame extends MalenFrame {

	private MalenImagePanel imagePanel;
	protected Controleur controleur;

	public MalenMainFrame(Controleur controleur) {
		super(controleur);
		setTitle("Malen - Fenêtre Principale");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		MalenMenuBar menuPanel = new MalenMenuBar(this);
		add(menuPanel, BorderLayout.NORTH);
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
			System.out.println("Image sauvegardée dans : " + fileName);
		} catch (IOException e) {
			System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
		}
	}

	public void nouvelleFenetre () {
		super.controleur.nouvelleFenetre();
	}

	public void onClickRight(MouseEvent e)
	{
		super.onClickRight(e);
		if (this.controleur.isOnSecondFrame()){
			this.controleur.setOnMainFrame();
		}
	}

	public boolean isMainFrame()
	{
		return true;
	}
}
