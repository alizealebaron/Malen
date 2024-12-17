package malen.vue;

import javax.swing.*;

import malen.modele.Point;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class MalenImagePanel extends JPanel implements MouseListener, MouseMotionListener {

	private BufferedImage image; // Image qui sera affichée
	private boolean imageLoaded = false; // Pour savoir si une image a été chargée
	private MalenMainFrame mainFrame; // Référence à la fenêtre principale (Vue)
	private double rotate_angle = 0;
	private JSlider rotationSlider;
	private JPanel sliderPanel;
	private boolean flipHorizontal = false;
	private boolean flipVertical = false;

	public MalenImagePanel(MalenMainFrame mainframe) {
		this.mainFrame = mainframe;
		setPreferredSize(new Dimension(800, 600)); // Taille initiale du panneau

		sliderPanel = new JPanel();
		sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS)); // Empile les composants verticalement

		rotationSlider = new JSlider(0, 360, 0); // Curseur de 0 à 360 degrés
		rotationSlider.setVisible(false);

		sliderPanel.add(rotationSlider);
		add(sliderPanel, BorderLayout.NORTH);

		rotationSlider.addChangeListener(e -> {
			int angle = rotationSlider.getValue() % 360;
			this.rotateImage(angle);
		});

		addMouseListener(this); // Ajouter un écouteur de souris
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

	public void showRotationSlider() {
		rotationSlider.setVisible(true);
		rotationSlider.setEnabled(true);
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

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

        // Si aucune image n'est chargée, afficher une zone vide (ou un message)
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

			if (flipHorizontal) {
				g2d.scale(-1, 1);
			}

			if (flipVertical) {
				g2d.scale(1, -1);
			}

			g2d.rotate(Math.toRadians(this.rotate_angle));

            g2d.drawImage(image, 0, 0, null);

			

			// Dessiner le rectangle de sélection
            if (mainFrame.getPoint1() != null) {
                Point point1 = mainFrame.getPoint1();
                Point point2 = mainFrame.getPoint2();
                if (point2 != null) {
                    g2d.setColor(Color.BLACK);
                    g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, new float[]{10}, 0)); // pointillé
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

		// Calcul des dimensions du rectangle englobant après rotation
		double radians = Math.toRadians(rotate_angle);
		int newWidth = (int) Math.round(Math.abs(image.getWidth() * Math.cos(radians)) +
				Math.abs(image.getHeight() * Math.sin(radians)));
		int newHeight = (int) Math.round(Math.abs(image.getWidth() * Math.sin(radians)) +
				Math.abs(image.getHeight() * Math.cos(radians)));

		// Créer un BufferedImage avec la taille calculée
		BufferedImage outputImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = outputImage.createGraphics();

		// Activer le rendu haute qualité
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Appliquer la rotation autour du centre de la nouvelle image
		g2d.translate(newWidth / 2, newHeight / 2);
		g2d.rotate(radians);
		g2d.translate(-image.getWidth() / 2, -image.getHeight() / 2);

		// Dessiner l'image transformée
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();

		// Sauvegarder l'image transformée
		try {
			File outputFile = new File(filePath);
			ImageIO.write(outputImage, "png", outputFile);
			JOptionPane.showMessageDialog(this, "Image sauvegardée avec succès.", "Succès",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Erreur lors de la sauvegarde.", "Erreur", JOptionPane.ERROR_MESSAGE);
		}
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

	/*------------------------------------------ Pipette ------------------------------------------*/

    @Override
    public void mousePressed(MouseEvent e) 
    {
        
    }

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
        //     // Informer la fenêtre principale (MalenMainFrame) pour traiter la couleur
        //     mainFrame.setPickedColor(color);
        // }

        System.out.println("Coordonées : [" + clickPoint.x() + ";" + clickPoint.y() + "]");

        if (image != null && x >= 0 && y >= 0 && x < this.image.getWidth() && y < this.image.getHeight()  )
        {
            System.out.println(color);
			mainFrame.onClick(image, clickPoint.x(), clickPoint.y(), color);
            repaint();
        }
	}



	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

}