package malen.vue;

import javax.swing.*;

import malen.Controleur;
import malen.modele.Point;
import malen.modele.Rotation;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class MalenImagePanel extends JPanel implements MouseListener, MouseMotionListener {

    private BufferedImage  image;
    private boolean        imageLoaded = false;
    private MalenMainFrame mainFrame;
    private double         rotate_angle = 0;
    private JPanel         sliderPanel;
    private boolean        flipHorizontal = false;
    private boolean        flipVertical = false;

	private JSlider outilSlider;
	private char outil = 'D'; // L = Luminosité / C = Contraste / R = Rotation / D = Default
	private boolean isMovingSubImage;

	/* Gestion du texte */
	private JPanel panelGestionText;
	private JTextField textField;      // Zone de texte temporaire
    private Rectangle textBounds;      // Bordure de la zone de texte
    private Font textFont = new Font("Arial", Font.PLAIN, 20); // Font par défaut
    private boolean editingText = false; // État de modification du texte


	public MalenImagePanel(MalenMainFrame mainframe) 
	{
		this.mainFrame = mainframe;
		setPreferredSize(new Dimension(800, 600)); // Taille initiale du panneau

		sliderPanel = new JPanel();
		sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS)); // Empile les composants verticalement

		outilSlider = new JSlider(0, 0, 0); // Curseur de 0 à 360 degrés
		outilSlider.setVisible(false);

		sliderPanel.add(outilSlider);
		add(sliderPanel, BorderLayout.NORTH);

		outilSlider.addChangeListener(e -> {
			int value = outilSlider.getValue();

			switch (this.outil) {
				case 'R':

					this.rotateImage(value % 360);
					break;

				case 'L':
					if (!outilSlider.getValueIsAdjusting() && image != null) {
						this.mainFrame.changerLuminosite(image, value);
						repaint();
					}
					break;

				case 'C':
					if (!outilSlider.getValueIsAdjusting() && image != null) {
						this.mainFrame.changerContraste(image, value);
						repaint();
					}
					break;

				default:
					break;
			}
		});

		/* Initialisation du panel de texte */
		initialisationPanelText();

		/* Gestion de la souris */

		addMouseListener(this); // Ajouter un écouteur de souris
		addMouseMotionListener(this);
	}

	// Méthode pour importer une image
	public void importImage(String imagePath) {
		try {
			this.image = ImageIO.read(new File(imagePath));
			this.imageLoaded = true;
			this.repaint(); // Redessiner après avoir chargé l'image
		} catch (IOException e) {
			e.printStackTrace();
			this.imageLoaded = false;
			JOptionPane.showMessageDialog(this, "Erreur lors du chargement de l'image.", "Erreur",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void pasteSubImage() 
	{
		System.out.println("oui");
		if (mainFrame.getSubImage() != null && this.image != null && mainFrame.getPoint1() != null
				&& mainFrame.getPoint2() != null) {
			// Déterminer les coordonnées où coller la subimage
			int x = Math.min(mainFrame.getPoint1().x(), mainFrame.getPoint2().x());
			int y = Math.min(mainFrame.getPoint1().y(), mainFrame.getPoint2().y());

			// Dessiner la subimage sur l'image principale
			Graphics2D g2d = image.createGraphics();
			System.out.println("c'est collé");
			g2d.drawImage(mainFrame.getSubImage(), x, y, null);
			g2d.dispose();

			// Réinitialiser la sélection et la subimage
			mainFrame.setPoint1(null);
			mainFrame.setPoint2(null); // Réinitialiser le deuxième point
			mainFrame.createSubImage(null);

			// Redessiner le panneau pour voir les changements
			repaint();
		}
	}

	/** Permet d'aficher ou non la barre d'outil (Rotation, Luminosité, Contraste)
	 * @param outil L = Luminosité / C = Contraste / R = Rotation / D = Default
	 */
	public void showOutilSlider(char outil) 
	{

		if (outilSlider.isVisible() && outil == this.outil || outil == 'D') 
		{
			outilSlider.setVisible(false);
			outilSlider.setEnabled(false);
		} 
		else
		{
			if (this.panelGestionText.isVisible())
				afficherPanelText();

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
				break;
		}
	}

	public void rotateImage(double angle) {
		this.rotate_angle = angle;
		repaint();
	}

	public void switchFlipHorizontal() {
		this.flipHorizontal = !this.flipHorizontal;
		repaint();
	}

	public void switchFlipVertical() {
		this.flipVertical = !this.flipVertical;
		repaint();
	}

    public BufferedImage getImage() {
        Rotation rotation = new Rotation(rotate_angle, flipHorizontal, flipVertical);
        return this.image = rotation.getImage(this.image);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        if (!imageLoaded)
        {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.BLACK);
            g.drawString("Aucune image chargée", getWidth() / 2 - 80, getHeight() / 2);
        }
        else
        {
            Graphics2D g2d = (Graphics2D) g.create();
            Rotation rotation = new Rotation(rotate_angle, flipHorizontal, flipVertical);
            BufferedImage transformedImage = rotation.applyTransformations(image);

            this.setSize(new Dimension(image.getWidth(), image.getHeight()));

			g2d.drawImage(transformedImage, 0, 0, null);

			// Dessiner le rectangle de sélection
			if (mainFrame.getPoint1() != null) {
				Point point1 = mainFrame.getPoint1();
				Point point2 = mainFrame.getPoint2();
				if (point2 != null) {
					g2d.setColor(Color.BLACK);
					g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10,
							new float[] { 10 }, 0)); // pointillé
					g2d.drawRect(Math.min(point1.x(), point2.x()), Math.min(point1.y(), point2.y()),
							Math.abs(point1.x() - point2.x()), Math.abs(point1.y() - point2.y()));
				}
			}

			g2d.dispose();
		}
	}

	public void saveImageToFile(String filePath) {
		if (!imageLoaded) {
			JOptionPane.showMessageDialog(this, "Aucune image à sauvegarder.", "Erreur", JOptionPane.ERROR_MESSAGE);
			return;
		}

		Rotation rotation = new Rotation(rotate_angle, flipHorizontal, flipVertical);
		rotation.saveImageToFile(image, filePath);
	}

	// Méthode pour obtenir la taille de l'image
	public Dimension getImageSize() {
		if (image != null) {
			return new Dimension(image.getWidth(), image.getHeight());
		}
		return new Dimension(0, 0);
	}

	// Méthode pour obtenir la couleur sous le curseur à la position donnée
	public Color getColorAtPoint(Point p) {
		if (image != null && p.x() >= 0 && p.x() < image.getWidth() && p.y() >= 0 && p.y() < image.getHeight()) {
			return new Color(image.getRGB(p.x(), p.y()));
		}
		return null; // Retourne null si la position est hors de l'image
	}

	/* ------------------------------------------------------------------------------------------------------------------------------ */
	/*                                                     Gestion du texte                                                           */
	/* ------------------------------------------------------------------------------------------------------------------------------ */

	/** Initialise le panel de texte et tout ses composants
	 * 
	 */
	public void initialisationPanelText()
	{
		// Initialisation d'un panel
		this.panelGestionText = new JPanel();

		// Initialiser la zone de texte qui s'affichera 
        this.textField = new JTextField();
        this.textField.setVisible(false);
        this.textField.setBorder(null);
        this.textField.addActionListener(e -> finalizeText()); 

		// Récupération de toutes les polices d'écriture du pc
		String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

		// Initialisation des différents champs de modification
		JComboBox<String> fontBox = new JComboBox<>(fonts);
		JSpinner sizeSpinner = new JSpinner(new SpinnerNumberModel(20, 8, 72, 1));
		JCheckBox boldCheck = new JCheckBox("Gras");
		JCheckBox italicCheck = new JCheckBox("Italique");

		// Ajout des différents composants au panel
		this.panelGestionText.add(new JLabel("Police:"));
		this.panelGestionText.add(fontBox);
		this.panelGestionText.add(new JLabel("Taille:"));
		this.panelGestionText.add(sizeSpinner);
		this.panelGestionText.add(boldCheck);
		this.panelGestionText.add(italicCheck);

		// Ajout de l'évenement pour écouter chacun des champs
		fontBox.addActionListener(e -> updateTextFont(fontBox, sizeSpinner, boldCheck, italicCheck));
		sizeSpinner.addChangeListener(e -> updateTextFont(fontBox, sizeSpinner, boldCheck, italicCheck));
		boldCheck.addActionListener(e -> updateTextFont(fontBox, sizeSpinner, boldCheck, italicCheck));
		italicCheck.addActionListener(e -> updateTextFont(fontBox, sizeSpinner, boldCheck, italicCheck));

		// Ajout du panelTexte au panelImage puis on le rend invisible
		this.add(this.panelGestionText, BorderLayout.SOUTH);
		this.panelGestionText.setVisible(false);

		// Ajout de la zone de texte invisible
		this.add(textField);
	}

	/** Permet d'afficher le panel de modification du texte
	 * 
	 */
	public void afficherPanelText()
	{
		if (this.panelGestionText.isVisible()) 
		{
			this.panelGestionText.setVisible(false);
		} 
		else
		{
			if (this.outilSlider.isVisible())
				showOutilSlider('D');
				
			this.panelGestionText.setVisible(true);
		}
	}

	
    /** Récupération de toutes les données des champs pour modifier le champ de texte en direct
     * @param fontBox
     * @param sizeSpinner
     * @param boldCheck
     * @param italicCheck
     */
    private void updateTextFont(JComboBox<String> fontBox, JSpinner sizeSpinner, JCheckBox boldCheck, JCheckBox italicCheck) 
	{
        int style = Font.PLAIN;
        if (boldCheck.isSelected()) style |= Font.BOLD;
        if (italicCheck.isSelected()) style |= Font.ITALIC;

        this.textFont = new Font((String) fontBox.getSelectedItem(), style, (Integer) sizeSpinner.getValue());
        this.textField.setFont(textFont);
    }

	/** Finalisation de l'ajout du texte à l'image
	 * 
	 */
	private void finalizeText() 
	{
		// Création du graphique g2d pour jouter le texte
		Graphics2D g2d = image.createGraphics();

        if (editingText)
		{
            String text = textField.getText();

			// On ajout le texte à l'image s'il n'est pas vide
            if (!text.isEmpty()) 
			{
                g2d.setFont(textFont);
                g2d.setColor(Color.BLACK);
                g2d.drawString(text, textBounds.x + 2, textBounds.y + textBounds.height - 10);
            }

			// On rend le text invisible
            textField.setVisible(false);
            editingText = false;
            textBounds = null;

			// On repaint l'image
            this.repaint();
        }
    }

	/* ------------------------------------------------------------------------------------------------------------------------------ */
	/*                                                  Gestion de la souris                                                          */
	/* ------------------------------------------------------------------------------------------------------------------------------ */

	@Override
	public void mouseClicked(MouseEvent e) 
	{
		// Lorsqu'on clique sur l'image, obtenir la couleur sous le curseur
		java.awt.Point awtPoint = e.getPoint();
		Point clickPoint = new Point((int) awtPoint.getX(), (int) awtPoint.getY());
		Color color = getColorAtPoint(clickPoint);

		int x = (int) clickPoint.x();
		int y = (int) clickPoint.y();

		// if (color != null)
		// {
		// // Informer la fenêtre principale (MalenMainFrame) pour traiter la couleur
		// mainFrame.setPickedColor(color);
		// }

		System.out.println("Coordonées : [" + clickPoint.x() + ";" + clickPoint.y() + "]");

		if (image != null && x >= 0 && y >= 0 && x < this.image.getWidth() && y < this.image.getHeight()) 
		{
			mainFrame.onClick(image, clickPoint.x(), clickPoint.y(), color);

			// Gestion de la zone de texte

			repaint();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {

		if (mainFrame.isCurseurOn(Controleur.SELECTION_RECTANGLE)
				|| mainFrame.isCurseurOn(Controleur.SELECTION_OVALE)) {
			if (null == mainFrame.getSubImage()) {
				mainFrame.setPoint1(new Point((int) e.getPoint().getX(), (int) e.getPoint().getY()));
				mainFrame.setPoint2(null); // Réinitialiser le deuxième point
				mainFrame.createSubImage(null);
				System.out.println("non");
			} else {
				int x1 = Math.min(mainFrame.getPoint1().x(), mainFrame.getPoint2().x());
				int y1 = Math.min(mainFrame.getPoint1().y(), mainFrame.getPoint2().y());
				int x2 = Math.max(mainFrame.getPoint1().x(), mainFrame.getPoint2().x());
				int y2 = Math.max(mainFrame.getPoint1().y(), mainFrame.getPoint2().y());

				if (e.getX() >= x1 && e.getX() <= x2 && e.getY() >= y1 && e.getY() <= y2) {
					// Si le clic est dans la zone de la subimage, on commence à la déplacer
					this.isMovingSubImage = true;
				} else {
					java.awt.Point awtPoint = e.getPoint();
					Point clickPoint = new Point((int) awtPoint.getX(), (int) awtPoint.getY());
					mainFrame.onClick(image, clickPoint.x(), clickPoint.y(), getColorAtPoint(clickPoint));
					// Sinon, on commence une nouvelle sélection
					mainFrame.setPoint1(new Point((int) e.getPoint().getX(), (int) e.getPoint().getY()));
					mainFrame.setPoint2(null); // Réinitialiser le deuxième point
					mainFrame.createSubImage(null);
					System.out.println("oui mais non");
				}
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (isMovingSubImage) {
			// Calcul du déplacement relatif
			int deltaX = e.getX() - mainFrame.getPoint1().x();
			int deltaY = e.getY() - mainFrame.getPoint1().y();

			// Mettre à jour les points pour "déplacer" la subimage
			mainFrame.setPoint1(new Point(mainFrame.getPoint1().x() + deltaX, mainFrame.getPoint1().y() + deltaY));
			mainFrame.setPoint2(new Point(mainFrame.getPoint2().x() + deltaX, mainFrame.getPoint2().y() + deltaY));

			this.repaint();
		} else {
			// Mettre à jour le deuxième point pour le rectangle de sélection
			if (mainFrame.isCurseurOn(Controleur.SELECTION_RECTANGLE)
					|| mainFrame.isCurseurOn(Controleur.SELECTION_OVALE)) {
				mainFrame.setPoint2(new Point((int) e.getPoint().getX(), (int) e.getPoint().getY()));
				this.repaint();
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (isMovingSubImage) {
			// Fin du déplacement de la subimage
			isMovingSubImage = false;
		} else if (mainFrame.isCurseurOn(Controleur.SELECTION_RECTANGLE)) {
			// Terminer la sélection et créer la subimage
			mainFrame.setPoint2(new Point(e.getX(), e.getY()));
			mainFrame.createSubImage(image);
		}

		this.repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

}