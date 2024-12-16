package malen.vue;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class MalenImagePanel extends JPanel {

    private BufferedImage image; // Image qui sera affichée
    private boolean imageLoaded = false; // Pour savoir si une image a été chargée

    public MalenImagePanel() {
        setPreferredSize(new Dimension(800, 600)); // Taille initiale du panneau
    }

    // Méthode pour importer une image
    public void importImage(String imagePath) {
        try {
            this.image = ImageIO.read(new File(imagePath));
            this.imageLoaded = true;
            this.repaint();  // Redessiner après avoir chargé l'image
        } catch (IOException e) {
            e.printStackTrace();
            this.imageLoaded = false;
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement de l'image.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
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
}
