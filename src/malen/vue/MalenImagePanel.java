package malen.vue;

import javax.swing.*;

import malen.modele.Point;
import malen.modele.Rotation;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
    private JSlider        rotationSlider;
    private JPanel         sliderPanel;
    private boolean        flipHorizontal = false;
    private boolean        flipVertical = false;

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

    public void rotateImage(double angle)
    {
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
        return rotation.getImage(this.image);
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

            g2d.drawImage(transformedImage, 0, 0, null);
            g2d.dispose();
        }
    }

    public void saveImageToFile(String filePath)
    {
        if (!imageLoaded)
        {
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