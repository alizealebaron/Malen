package malen.vue;

import javax.swing.*;

import org.w3c.dom.events.MouseEvent;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class MalenImagePanel extends JPanel implements MouseListener {

    private BufferedImage image; // Image qui sera affichée
    private boolean imageLoaded = false; // Pour savoir si une image a été chargée
    private MalenMainFrame mainFrame; // Référence à la fenêtre principale (Vue)

    public MalenImagePanel(MalenMainFrame mainframe) {
        this.mainFrame = mainframe;
        setPreferredSize(new Dimension(800, 600)); // Taille initiale du panneau
        addMouseListener(this);  // Ajouter un écouteur de souris
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

    public void rotateImage (double angle)
    {
        this.rotate_angle = angle;
        repaint();
    }

    public void rotationPlane (double angle)
    {
        this.rotate_angle += angle % 360;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Si aucune image n'est chargée, afficher une zone vide (ou un message)
        if (!imageLoaded) {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight()); // Zone vide
            g.setColor(Color.BLACK);
            g.drawString("Aucune image chargée", getWidth() / 2 - 80, getHeight() / 2);
        } else {
            // Si une image est chargée, la dessiner
            g.drawImage(image, 0, 0, this);
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
        if (image != null && p.x >= 0 && p.x < image.getWidth() && p.y >= 0 && p.y < image.getHeight()) {
            return new Color(image.getRGB(p.x, p.y));
        }
        return null;  // Retourne null si la position est hors de l'image
    }

    /*------------------------------------------ Pipette ------------------------------------------*/

    @Override
    public void mousePressed(MouseEvent e) {
        // Lorsqu'on clique sur l'image, obtenir la couleur sous le curseur
        Point clickPoint = e.getPoint();
        Color color = getColorAtPoint(clickPoint);

        if (color != null) {
            // Informer la fenêtre principale (MalenMainFrame) pour traiter la couleur
            mainFrame.setPickedColor(color);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}

}
