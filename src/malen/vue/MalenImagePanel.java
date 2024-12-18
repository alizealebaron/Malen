package malen.vue;

import javax.swing.*;

import malen.Controleur;
import malen.modele.MalenFrame;
import malen.modele.Point;
import malen.modele.Rotation;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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

	private BufferedImage image;
	private boolean imageLoaded = false;
	private MalenFrame mainFrame;
	private double rotate_angle = 0;
	private JPanel sliderPanel;
	private boolean flipHorizontal = false;
	private boolean flipVertical = false;

	private JSlider outilSlider;
	private char outil = 'D'; // L = Luminosité / C = Contraste / R = Rotation / D = Default
	private boolean isMovingSubImage;

	/* Gestion du texte */
	private JPanel panelGestionText;
	private JTextField textField; // Zone de texte temporaire
	private Rectangle textBounds; // Bordure de la zone de texte
	private Font textFont = new Font("Arial", Font.PLAIN, 20); // Font par défaut
	private boolean editingText = false; // État de modification du texte
	private Color textColor = Color.BLACK; // Couleur du texte
	private JButton btnCouleurText;

	public MalenImagePanel(MalenFrame mainframe) {
		this.mainFrame = mainframe;

		// Désactiver le layout pour permettre un positionnement absolu
		// this.setLayout(null); // Remplace le BorderLayout par null
		setPreferredSize(new Dimension(800, 600)); // Taille initiale du panneau

		sliderPanel = new JPanel();
		sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS)); // Empile les composants verticalement
		sliderPanel.setBounds(0, 0, 200, 50); // Positionnement manuel si nécessaire
		this.add(sliderPanel);

		outilSlider = new JSlider(0, 0, 0); // Curseur de 0 à 360 degrés
		outilSlider.setVisible(false);
		sliderPanel.add(outilSlider);

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
					this.rotateImage(0);
					break;
			}
		});

		// Initialisation du panel de texte
		initialisationPanelText();

		// Gestion de la souris
		addMouseListener(this);
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

	public BufferedImage getImage() {
		return this.image;
	}

	public void pasteSubImage() {
		this.image = mainFrame.pasteSubImage();
		this.repaint();
	}

	/**
	 * Permet d'aficher ou non la barre d'outil (Rotation, Luminosité, Contraste)
	 * 
	 * @param outil L = Luminosité / C = Contraste / R = Rotation / D = Default
	 */
	public void showOutilSlider(char outil) {

		if (outilSlider.isVisible() && outil == this.outil || outil == 'D') {
			outilSlider.setVisible(false);
			outilSlider.setEnabled(false);
			outil = 'D';
		} else {
			outilSlider.setVisible(true);
			outilSlider.setEnabled(true);
		}

		this.outil = outil;

		switch (this.outil) {
			case 'R':
				outilSlider.setValue((int) this.rotate_angle);
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

	public void rotateImage(double angle) {
		this.rotate_angle = angle % 360;
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

	public BufferedImage changeImage(BufferedImage rotaImage) {
		Rotation rotation = new Rotation(rotate_angle, flipHorizontal, flipVertical);
		return rotation.getImage(rotaImage);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (!imageLoaded) {
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(Color.BLACK);
			g.drawString("Aucune image chargée", getWidth() / 2 - 80, getHeight() / 2);
		} else {
			Graphics2D g2d = (Graphics2D) g.create();
			Rotation rotation = new Rotation(rotate_angle, flipHorizontal, flipVertical);

			BufferedImage transformedImage;

			// Dessiner le rectangle de sélection
			if (mainFrame.getPoint1() != null && mainFrame.getPoint2() != null
					&& (mainFrame.isCurseurOn(Controleur.SELECTION_RECTANGLE)
							|| mainFrame.isCurseurOn(Controleur.SELECTION_OVALE))
					&& ((this.mainFrame.isOnMainFrame()
							&& this.mainFrame.isMainFrame())
							|| (this.mainFrame.isOnSecondFrame() && !this.mainFrame.isMainFrame()))) {
				this.setSize(new Dimension(image.getWidth(), image.getHeight()));

				g2d.drawImage(image, 0, 0, null);

				if (this.outil == 'R') {

					transformedImage = rotation.applyTransformations(mainFrame.getSubImage());

					this.mainFrame.setPoint1(new Point(transformedImage.getMinX() + mainFrame.getPoint1().x(),
							transformedImage.getMinY() + mainFrame.getPoint1().y()));
					this.mainFrame.setPoint2(new Point(
							transformedImage.getMinX() + transformedImage.getWidth() + mainFrame.getPoint1().x(),
							transformedImage.getMinY() + transformedImage.getHeight() + mainFrame.getPoint1().y()));

					g2d.drawImage(transformedImage, mainFrame.getPoint1().x(), mainFrame.getPoint1().y(), null);
				} else {

					g2d.drawImage(this.mainFrame.getSubImage(), mainFrame.getPoint1().x(),
							mainFrame.getPoint1().y(),
							null);

					g2d.setColor(Color.BLACK);
					g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10,
							new float[] { 10 }, 0)); // pointillé
					g2d.drawRect(Math.min(mainFrame.getPoint1().x(), mainFrame.getPoint2().x()),
							Math.min(mainFrame.getPoint1().y(), mainFrame.getPoint2().y()),
							Math.abs(mainFrame.getPoint1().x() - mainFrame.getPoint2().x()),
							Math.abs(mainFrame.getPoint1().y() - mainFrame.getPoint2().y()));
				}
			} else {

				transformedImage = rotation.applyTransformations(image);

				this.setSize(new Dimension(image.getWidth(), image.getHeight()));

				g2d.drawImage(transformedImage, 0, 0, null);
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

	/*
	 * -----------------------------------------------------------------------------
	 * -------------------------------------------------
	 */
	/* Gestion du texte */
	/*
	 * -----------------------------------------------------------------------------
	 * -------------------------------------------------
	 */

	/**
	 * Initialise le panel de texte et tout ses composants
	 * 
	 */
	public void initialisationPanelText() {
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
		this.btnCouleurText = new JButton("      ");
		this.btnCouleurText.addActionListener(e -> chooseColor());
		this.btnCouleurText.setBackground(this.mainFrame.getCurrentColor());
		this.btnCouleurText.setFocusPainted(false);
		this.btnCouleurText.setBackground(this.textColor); // Couleur de fond
		this.btnCouleurText.setForeground(this.textColor);
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

	/**
	 * Permet d'afficher le panel de modification du texte
	 * 
	 */
	public void afficherPanelText() {
		if (this.panelGestionText.isVisible()) {
			this.panelGestionText.setVisible(false);
		} else {
			if (this.outilSlider.isVisible())
				showOutilSlider('D');

			this.panelGestionText.setVisible(true);
		}
	}

	/**
	 * Récupération de toutes les données des champs pour modifier le champ de texte
	 * en direct
	 * 
	 * @param fontBox
	 * @param sizeSpinner
	 * @param boldCheck
	 * @param italicCheck
	 */
	private void updateTextFont(JComboBox<String> fontBox, JSpinner sizeSpinner, JCheckBox boldCheck,
			JCheckBox italicCheck) {
		int style = Font.PLAIN;
		if (boldCheck.isSelected())
			style |= Font.BOLD;
		if (italicCheck.isSelected())
			style |= Font.ITALIC;

		this.textFont = new Font((String) fontBox.getSelectedItem(), style, (Integer) sizeSpinner.getValue());
		this.textField.setFont(textFont);

		// Repositionner la zone de texte si nécessaire
		if (editingText && textBounds != null) {
			textField.setBounds(textBounds); // Réutiliser la position précédente
		}
	}

	/**
	 * Finalisation de l'ajout du texte à l'image
	 * 
	 */
	private void finalizeText() {
		// Création du graphique g2d pour jouter le texte
		Graphics2D g2d = image.createGraphics();

		if (editingText) {
			String text = textField.getText();

			// On ajout le texte à l'image s'il n'est pas vide
			if (!text.isEmpty()) {
				g2d.setFont(textFont);
				g2d.setColor(this.textColor);
				g2d.drawString(text, textBounds.x + 2, textBounds.y + textBounds.height - 10);
			}

			// On rend le text invisible
			textField.setVisible(false);
			editingText = false;
			textField.setText("");
			textBounds = null;

			// On repaint l'image
			this.repaint();
		}
	}

	private void startTextEditing(int x, int y) {
		System.out.println("" + x + "|" + y);
		this.textBounds = new Rectangle(x, y, 150, 30); // Taille initiale de la zone
		this.textField.setBounds(textBounds);
		this.textField.setFont(textFont);
		this.textField.setText("");
		this.textField.setVisible(true);
		this.textField.requestFocus();
		this.editingText = true;

		this.repaint();
	}

	/**
	 * Permet de changer la couleur du texte
	 * 
	 */
	private void chooseColor() {
		Color newColor = JColorChooser.showDialog(this, "Choisir la couleur du texte", textColor);

		if (newColor != null) {
			this.textColor = newColor;
			this.textField.setForeground(this.textColor);
			this.btnCouleurText.setBackground(this.textColor);
			this.btnCouleurText.setForeground(this.textColor);
		}

	}

	/*
	 * -----------------------------------------------------------------------------
	 * -------------------------------------------------
	 */
	/* Gestion de la souris */
	/*
	 * -----------------------------------------------------------------------------
	 * -------------------------------------------------
	 */

	@Override
	public void mouseClicked(MouseEvent e) {

		if (!SwingUtilities.isLeftMouseButton(e)) {
			mainFrame.onClickRight(e);
			return;
		} else {
			// Lorsqu'on fait un clique gauche sur l'image, obtenir la couleur sous le
			// curseur
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

			Component sourceComponent = e.getComponent();
			JFrame parentWindow = (JFrame) SwingUtilities.getWindowAncestor(sourceComponent);

			if (parentWindow instanceof MalenMainFrame) {
				System.out.println("Clic sur la main frame");
			} else {
				System.out.println("Clic sur la sub frame");
			}

			if (image != null && x >= 0 && y >= 0 && x < this.image.getWidth() && y < this.image.getHeight()) {
				System.out.println(color);
				mainFrame.onClickLeft(image, clickPoint.x(), clickPoint.y(), color);
				repaint();
			}

			if (this.mainFrame.isCurseurOn(Controleur.TEXT)) {

				if (this.editingText) {
					finalizeText(); // Terminer l'édition du texte en cours
				}

				startTextEditing(e.getX(), e.getY());
			}

			repaint();
		}
	}

	public void setImage(BufferedImage img) {
		this.image = img;
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println("pressed");

		if (this.outil == 'R') {
			if (mainFrame.getPoint1() != null && mainFrame.getPoint2() != null) {
				this.mainFrame.setSubimage(this.changeImage(this.mainFrame.getSubImage()));
			} else {
				this.image = this.changeImage(this.image);
			}

			this.showOutilSlider('R');
			outilSlider.setValue(0);
			this.repaint();
		}

		if ((mainFrame.isCurseurOn(Controleur.SELECTION_RECTANGLE)
				|| mainFrame.isCurseurOn(Controleur.SELECTION_OVALE))
				&& ((this.mainFrame.isOnMainFrame()
						&& this.mainFrame.isMainFrame())
						|| (this.mainFrame.isOnSecondFrame() && !this.mainFrame.isMainFrame()))) {
							System.out.println("oui 1");

			if (mainFrame.getSubImage() == null) {
				mainFrame.setPoint1(new Point((int) e.getPoint().getX(), (int) e.getPoint().getY()));
				mainFrame.setPoint2(null); // Réinitialiser le deuxième point
				mainFrame.createSubImage(null);
			} else {
				System.out.println("oui 2");
				int x1 = Math.min(mainFrame.getPoint1().x(), mainFrame.getPoint2().x());
				int y1 = Math.min(mainFrame.getPoint1().y(), mainFrame.getPoint2().y());
				int x2 = Math.max(mainFrame.getPoint1().x(), mainFrame.getPoint2().x());
				int y2 = Math.max(mainFrame.getPoint1().y(), mainFrame.getPoint2().y());

				if (e.getX() >= x1 && e.getX() <= x2 && e.getY() >= y1 && e.getY() <= y2) {
					System.out.println("oui 3");
					// Si le clic est dans la zone de la subimage, on commence à la déplacer
					this.isMovingSubImage = true;
				} else {
					java.awt.Point awtPoint = e.getPoint();
					Point clickPoint = new Point((int) awtPoint.getX(), (int) awtPoint.getY());
					mainFrame.onClickLeft(image, clickPoint.x(), clickPoint.y(), getColorAtPoint(clickPoint));
					// Sinon, on commence une nouvelle sélection
					mainFrame.setPoint1(new Point((int) e.getPoint().getX(), (int) e.getPoint().getY()));
					mainFrame.setPoint2(null); // Réinitialiser le deuxième point
					mainFrame.createSubImage(null);
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
			if ((mainFrame.isCurseurOn(Controleur.SELECTION_RECTANGLE)
					|| mainFrame.isCurseurOn(Controleur.SELECTION_OVALE))
					&& ((this.mainFrame.isOnMainFrame()
							&& this.mainFrame.isMainFrame())
							|| (this.mainFrame.isOnSecondFrame() && !this.mainFrame.isMainFrame()))) {
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
		} else if ((mainFrame.isCurseurOn(Controleur.SELECTION_RECTANGLE)
				|| mainFrame.isCurseurOn(Controleur.SELECTION_OVALE))
				&& ((this.mainFrame.isOnMainFrame()
						&& this.mainFrame.isMainFrame())
						|| (this.mainFrame.isOnSecondFrame() && !this.mainFrame.isMainFrame()))) {
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