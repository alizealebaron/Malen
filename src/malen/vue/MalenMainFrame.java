import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MalenMainFrame extends JFrame {

    private MalenImagePanel imagePanel;  // Référence au panneau d'image

    public MalenMainFrame() {
        // Configuration de la fenêtre principale
        setTitle("Mini Paint Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLayout(new BorderLayout());

        // Ajouter le panneau de menu
        MalenMenuBar menuPanel = new MalenMenuBar();
        add(menuPanel, BorderLayout.NORTH);

        // Ajouter le panneau d'affichage d'image
        imagePanel = new MalenImagePanel();
        add(imagePanel, BorderLayout.CENTER);

        // Afficher la fenêtre
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Méthode pour ouvrir le dialogue d'importation d'image
    public void importImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Images", "png", "jpg", "jpeg", "gif"));
        int result = fileChooser.showOpenDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            // Appeler la méthode pour importer l'image dans le panneau
            imagePanel.importImage(selectedFile.getAbsolutePath());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MalenMainFrame mainFrame = new MalenMainFrame();
            // Exemple d'appel de la méthode d'importation d'image (pour test)
            mainFrame.importImage();
        });
    }
}
