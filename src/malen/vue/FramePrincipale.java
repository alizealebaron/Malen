package malen.vue;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.event.MouseEvent;

import malen.Controleur;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;

/** Frame principale de l'application
 * @author  : Alizéa Lebaron
 * @author  : Tom Goureau
 * @author  : Trystan Baillobay
 * @version : 1.0.0 - 19/12/2024
 * @since   : 19/12/2024
 */

public class FramePrincipale extends Frame 
{

	private PanelMenu    panelMenu;
	private String       nomImage = null;

	public FramePrincipale(Controleur controleur) {
		super(controleur);
		setTitle("Malen - Fenêtre Principale");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Ajouter le panneau de menu
		this.panelMenu = new PanelMenu(this);
		this.add(this.panelMenu, BorderLayout.NORTH);
	}

	public void saveImage() 
	{
		if (this.nomImage == null)
		{
			JFileChooser fileChooser = new JFileChooser();
			int userSelection = fileChooser.showSaveDialog(null);

			//TODO: Peut-être gérer le cas où l'utilisateur ne met rien
			if (userSelection == JFileChooser.APPROVE_OPTION) 
			{
				File fileToSave = fileChooser.getSelectedFile();
				String filePath = fileToSave.getAbsolutePath();

				if (!filePath.toLowerCase().endsWith(".png")) 
				{
					filePath += ".png";
				}

				this.nomImage = filePath;
				this.saveImage(filePath);
			}
		}
		else
		{
			this.saveImage(this.nomImage);
		}
	}

	public void saveSousImage ()
	{
		JFileChooser fileChooser = new JFileChooser();
		int userSelection = fileChooser.showSaveDialog(null);

		if (userSelection == JFileChooser.APPROVE_OPTION) 
		{
			File fileToSave = fileChooser.getSelectedFile();
			String filePath = fileToSave.getAbsolutePath();

			if (!filePath.toLowerCase().endsWith(".png")) 
			{
				filePath += ".png";
			}

			this.nomImage = filePath;
			this.saveImage(filePath);
		}
	}

	public void saveImage(String fileName) 
	{
		System.out.println(fileName);

		try 
		{
			File outputFile = new File(fileName);
			ImageIO.write(this.panelImage.getImage(), "png", outputFile);
		} 
		catch (IOException e) 
		{
			System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
		}
	}

	public void onClickRight(MouseEvent e) {
		if (this.controleur.isOnSecondFrame())
		{
			this.controleur.setOnMainFrame();
			System.out.println("passage en main frame");
		}
	}

	public void nouvelleFenetre() {
		super.controleur.nouvelleFenetre();
	}

	public void updateButton() {
		this.panelMenu.setCouleurButton();
	}

	public boolean isMainFrame() {
		return true;
	}
}