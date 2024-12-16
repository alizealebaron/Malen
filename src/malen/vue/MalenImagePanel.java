import javax.swing.*;

import org.w3c.dom.events.MouseEvent;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class MalenImagePanel extends JPanel
{
    private BufferedImage image;
    private boolean       imageLoaded = false;
    private double        rotate_angle = 0;
    private JSlider       rotationSlider;
    private JPanel        sliderPanel;

    public MalenImagePanel() {
        setPreferredSize(new Dimension(800, 600));
        setLayout(new BorderLayout());

        sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS)); // Empile les composants verticalement

        rotationSlider = new JSlider(0, 360, 0);  // Curseur de 0 à 360 degrés
        rotationSlider.setVisible(false);

        sliderPanel.add(rotationSlider);
        add(sliderPanel, BorderLayout.NORTH);

        rotationSlider.addChangeListener(e -> {
            int angle = rotationSlider.getValue() % 360;
            this.rotateImage(angle);
        });
    }

    // Méthode pour importer une image
    public void importImage(String imagePath) {
        try {
            this.image = ImageIO.read(new File(imagePath));
            this.imageLoaded = true;
            repaint();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement de l'image.", "Erreur", JOptionPane.ERROR_MESSAGE);
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
            g.fillRect(0, 0, getWidth(), getHeight());  // Zone vide
            g.setColor(Color.BLACK);
            g.drawString("Aucune image chargée", getWidth() / 2 - 80, getHeight() / 2);
        } else {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.translate(this.getSize().width/2, this.getSize().height/2);
            g2d.rotate(Math.toRadians(this.rotate_angle));  
            g2d.drawImage(image, -image.getWidth()/2, -image.getHeight()/2, null);
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
            JOptionPane.showMessageDialog(this, "Image sauvegardée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la sauvegarde.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
