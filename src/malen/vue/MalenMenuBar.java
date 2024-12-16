import javax.swing.*;
import java.awt.*;

public class MalenMenuBar extends JMenuBar {

    public MalenMenuBar() {
        // Configuration du panneau principal
        setLayout(new BorderLayout());

        // Menu Fichier
        JMenu fileMenu = new JMenu("Fichier");
        this.add(fileMenu);
        fileMenu.add(new JMenuItem("Sauvegarder"));
        fileMenu.add(new JMenuItem("Sauvegarder sous"));
        fileMenu.add(new JMenuItem("Importer"));

        // Menu Couleur
        JMenu colorMenu = new JMenu("Couleur");
        this.add(colorMenu);
        colorMenu.add(new JMenuItem("Pot de peinture"));
        colorMenu.add(new JMenuItem("Fonc"));
        colorMenu.add(new JMenuItem("Luminance"));
        colorMenu.add(new JMenuItem("Noir et Blanc"));

        // Menu Texte
        JMenu textMenu = new JMenu("Texte");
        this.add(textMenu);
        textMenu.add(new JMenuItem("Police"));
        textMenu.add(new JMenuItem("Couleur"));
        textMenu.add(new JMenuItem("Texture"));
        textMenu.add(new JMenuItem("Gras/Italique"));

        // Menu Rotation
        JMenu rotationMenu = new JMenu("Rotation");
        this.add(rotationMenu);
        rotationMenu.add(new JMenuItem("Rotation Axial"));
        rotationMenu.add(new JMenuItem("Rotation Plane"));

        // Menu Sélection
        JMenu selectionMenu = new JMenu("Sélection");
        this.add(selectionMenu);
        selectionMenu.add(new JMenuItem("Sélection Rectangle"));
        selectionMenu.add(new JMenuItem("Sélection Ovale"));

        // Menu Pipette
        JMenu pipetteMenu = new JMenu("Pipette");
        this.add(pipetteMenu);
        pipetteMenu.add(new JMenuItem("Choix de Couleur"));

        // Rendre visible
        this.setVisible(true);
    }

}

