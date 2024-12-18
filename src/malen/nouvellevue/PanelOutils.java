package malen.nouvellevue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

/** Panel de gestion des outils
 * @author  : Alizéa Lebaron
 * @author  : Tom Goureau
 * @author  : Trystan Baillobay
 * @version : 1.0.0 - 19/12/2024
 * @since   : 19/12/2024
 */

public class PanelOutils extends JPanel
{
	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/
	/*                                                                 Attributs                                                                        */
	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/

	private FramePrincipale framePrincipale;

	/* Gestion générale */
	private JSlider outilSlider;
	private char outil = 'D'; // L = Luminosité / C = Contraste / R = Rotation / D = Default

	/* Gestion de la rotation */
	private double  rotate_angle = 0;
	private JPanel  sliderPanel;
	private boolean flipHorizontal = false;
	private boolean flipVertical = false;

	/* Gestion du texte */
	private JPanel     panelGestionText;
	private JButton    btnCouleurText;

	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/
	/*                                                                Controleurs                                                                       */
	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/

	public PanelOutils (FramePrincipale frame)
	{
		this.framePrincipale = frame;

		//Génération du slider de rotation
		outilSlider = new JSlider(0, 0, 0);
		outilSlider.setVisible(false);
		this.add(outilSlider);

		// Mise sur écoute du slider
		outilSlider.addChangeListener(e -> 
		{
			int value = outilSlider.getValue();

			switch (this.outil) 
			{
				case 'R':
					this.rotateImage(value % 360);
					break;
				case 'L':
					if (!outilSlider.getValueIsAdjusting() && this.framePrincipale.isImage()) 
					{
						this.framePrincipale.changerLuminosite(value);
						repaint();
					}
					break;
				case 'C':
					if (!outilSlider.getValueIsAdjusting() && this.framePrincipale.isImage()) 
					{
						this.framePrincipale.changerContraste(value);
						repaint();
					}
					break;
				default:
					this.rotateImage(0);
					break;
			}
		});

		//Génération du champ de texte
		initialisationPanelText();

		this.setPreferredSize(new Dimension(0, 50));

	}

	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/
	/*                                                                Accesseurs                                                                        */
	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/

	public JPanel  getPanelGestionText ( ) { return this.panelGestionText; }
	public JSlider getOutilSlider      ( ) { return this.outilSlider     ; }
	public char    getOutil            ( ) { return this.outil           ; }

	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/
	/*                                                               Modificateurs                                                                      */
	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/



	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/
	/*                                                                 Méthodes                                                                         */
	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/

	/* ------------------------------------------------------------ */
	/*              Initialisation de la zone de text               */
	/* ------------------------------------------------------------ */

	/**
	 * Initialise le panel de texte et tout ses composants
	 * 
	 */
	public void initialisationPanelText() 
	{
		// Initialisation d'un panel
		this.panelGestionText = new JPanel();

		// Récupération de toutes les polices d'écriture du pc
		String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

		// Initialisation des différents champs de modification
		JComboBox<String> fontBox = new JComboBox<>(fonts);
		JSpinner sizeSpinner      = new JSpinner (new SpinnerNumberModel(20, 8, 72, 1));
		JCheckBox boldCheck       = new JCheckBox("Gras");
		JCheckBox italicCheck     = new JCheckBox("Italique");

		this.btnCouleurText = new JButton("      ");
        this.btnCouleurText.addActionListener(e -> chooseColor());
		this.btnCouleurText.setBackground(this.framePrincipale.getCurrentColor());
		this.btnCouleurText.setFocusPainted(false);
        this.btnCouleurText.setBackground(Color.BLACK); // Couleur de fond
		this.btnCouleurText.setForeground(Color.BLACK);
        this.btnCouleurText.setOpaque(true); 
        this.btnCouleurText.setBorderPainted(false); 
        this.btnCouleurText.setToolTipText("Couleur actuelle");

		// Ajout des différents composants au panel
		this.panelGestionText.add(new JLabel("Police:"));
		this.panelGestionText.add(fontBox);
		this.panelGestionText.add(new JLabel("Taille:"));
		this.panelGestionText.add(sizeSpinner);
		this.panelGestionText.add(boldCheck);
		this.panelGestionText.add(italicCheck);
		this.panelGestionText.add(btnCouleurText);

		// Ajout de l'évenement pour écouter chacun des champs
		fontBox    .addActionListener (e -> updateTextFont(fontBox, sizeSpinner, boldCheck, italicCheck) );
		sizeSpinner.addChangeListener (e -> updateTextFont(fontBox, sizeSpinner, boldCheck, italicCheck) );
		boldCheck  .addActionListener (e -> updateTextFont(fontBox, sizeSpinner, boldCheck, italicCheck) );
		italicCheck.addActionListener (e -> updateTextFont(fontBox, sizeSpinner, boldCheck, italicCheck) );

		// Ajout du panelTexte au panelImage puis on le rend invisible
		this.panelGestionText.setVisible(false);
		this.add(this.panelGestionText);
	}

	private void chooseColor() 
	{
        Color newColor = JColorChooser.showDialog(this, "Choisir la couleur du texte", this.framePrincipale.getColorText());

        if (newColor != null)
		{
			this.framePrincipale.updateTextColor(newColor);
			this.btnCouleurText .setBackground  (newColor);
			this.btnCouleurText .setForeground  (newColor);
    	}
	}

	public void updateTextFont (JComboBox<String> fontBox, JSpinner sizeSpinner, JCheckBox boldCheck, JCheckBox italicCheck) { this.framePrincipale.updateTextFont(fontBox, sizeSpinner, boldCheck, italicCheck);}

	/* ------------------------------------------------------------ */
	/*                   Transformation d'image                     */
	/* ------------------------------------------------------------ */

	public void rotateImage(double angle) 
	{
		this.rotate_angle = angle;
		this.framePrincipale.repaintImage();
	}

	/* ------------------------------------------------------------ */
	/*                    Gestion de l'échelle                      */
	/* ------------------------------------------------------------ */

	/**
	 * Permet d'aficher ou non la barre d'outil (Rotation, Luminosité, Contraste)
	 * 
	 * @param outil L = Luminosité / C = Contraste / R = Rotation / D = Default
	 */
	public void showOutilSlider(char outil) 
	{

		if (outilSlider.isVisible() && outil == this.outil || outil == 'D') 
		{
			outilSlider.setVisible(false);
			outilSlider.setEnabled(false);
			outil = 'D';
		} 
		else 
		{
			outilSlider.setVisible(true);
			outilSlider.setEnabled(true);
		}

		this.outil = outil;

		switch (this.outil) 
		{
			case 'R':
				outilSlider.setValue(0);
				outilSlider.setMinimum(0);
				outilSlider.setMaximum(360);
				break;

			case 'L':
				outilSlider.setValue(0);
				outilSlider.setMinimum(-255);
				outilSlider.setMaximum(255);
				break;

			case 'C':
				outilSlider.setValue(0);
				outilSlider.setMinimum(-100);
				outilSlider.setMaximum(100);
				break;

			default:
				outilSlider.setValue(0);
				outilSlider.setMinimum(0);
				outilSlider.setMaximum(0);
				break;
		}
	}
}
