package malen.nouvellevue;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.imageio.ImageIO;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import malen.Controleur;
import malen.modele.Point;
import malen.modele.Rotation;

/** Panel contenant l'image
 * @author  : Alizéa Lebaron
 * @author  : Tom Goureau
 * @author  : Trystan Baillobay
 * @version : 1.0.0 - 19/12/2024
 * @since   : 19/12/2024
 */

public class PanelImage extends JPanel implements MouseListener, MouseMotionListener
{
	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/
	/*                                                                 Attributs                                                                        */
	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/

	private FramePrincipale framePrincipale;
	private BufferedImage   biImage;
	private boolean         imageLoaded = false;

	private JTextField      textField; 
	private Rectangle       textBounds; 
	private boolean         editingText = false; // État de modification du texte
	private Font            textFont = new Font("Arial", Font.PLAIN, 20); // Font par défaut
	private Color           textColor = Color.BLACK; // Couleur du texte

	private double  rotate_angle   = 0;
	private boolean flipHorizontal = false;
	private boolean flipVertical   = false;

	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/
	/*                                                                Controleurs                                                                       */
	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/

	public PanelImage (FramePrincipale frame)
	{
		this.framePrincipale = frame;
		this.biImage         = null;

		this.setLayout(null);

		// Initialiser la zone de texte qui s'affichera
		this.textField = new JTextField();
		this.textField.setVisible(false);
		this.textField.setBorder(null);
		this.textField.addActionListener(e -> finalizeText());

		this.add(this.textField);

		// Mise en écoute de l'image
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/
	/*                                                                Accesseurs                                                                        */
	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/

	public boolean       isImage      ( ) { return this.biImage != null; }
	public BufferedImage getImage     ( ) { return this.biImage        ; }
	public Color         getColorText ( ) { return this.textColor      ; }
	public JTextField    getTextField ( ) { return this.textField      ; }

	public Dimension getImageSize() 
	{
		if (this.biImage != null) 
		{
			return new Dimension(this.biImage.getWidth(), this.biImage.getHeight());
		}

		return new Dimension(0, 0);
	}

	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/
	/*                                                               Modificateurs                                                                      */
	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/

	public void setTextColor ( Color c ) { this.textColor = c; }

	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/
	/*                                                                 Méthodes                                                                         */
	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/

	/* ------------------------------------------------------------ */
	/*                      Gestion des Fonts                       */
	/* ------------------------------------------------------------ */

	public void updateTextFont(JComboBox<String> fontBox, JSpinner sizeSpinner, JCheckBox boldCheck, JCheckBox italicCheck) 
	{
		int style = Font.PLAIN;

		if (boldCheck.isSelected  ( )) style |= Font.BOLD;
		if (italicCheck.isSelected( )) style |= Font.ITALIC;

		this.textFont = new Font((String) fontBox.getSelectedItem(), style, (Integer) sizeSpinner.getValue());
		this.textField.setFont(textFont);

		// Repositionner la zone de texte si nécessaire
		if (editingText && textBounds != null) 
		{
			textField.setBounds(textBounds); // Réutiliser la position précédente
		}
	}

	/**
	 * Finalisation de l'ajout du texte à l'image
	 * 
	 */
	private void finalizeText() 
	{
		// Création du graphique g2d pour jouter le texte
		Graphics2D g2d = biImage.createGraphics();

		if (editingText) {
			String text = textField.getText();

			// On ajout le texte à l'image s'il n'est pas vide
            if (!text.isEmpty()) 
			{
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

	private void startTextEditing(int x, int y) 
	{
		this.textBounds = new Rectangle(x, y, 150, 30); // Taille initiale de la zone
		this.textField.setBounds(textBounds);
		this.textField.setFont(textFont);
		this.textField.setText("");
		this.textField.setVisible(true);
		this.textField.requestFocus();
		this.editingText = true;

        this.repaint();
    }

	/* ------------------------------------------------------------ */
	/*                     Gestion de l'image                       */
	/* ------------------------------------------------------------ */

	public void importImage(String imagePath) 
	{
		try 
		{
			this.biImage = ImageIO.read(new File(imagePath));
			this.imageLoaded = true;
			this.repaint(); // Redessiner après avoir chargé l'image
		} 
		catch (IOException e)
		{
			e.printStackTrace();
			this.imageLoaded = false;
			JOptionPane.showMessageDialog(this, "Erreur lors du chargement de l'image.", "Erreur", JOptionPane.ERROR_MESSAGE);
		}
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

			BufferedImage transformedImage;

			// Dessiner le rectangle de sélection
			if (this.framePrincipale.getPoint1() != null && this.framePrincipale.getPoint2() != null) 
			{
				this.setSize(new Dimension(this.biImage.getWidth(), this.biImage.getHeight()));

				g2d.drawImage(this.biImage, 0, 0, null);

				if (this.framePrincipale.getOutil() == 'R') 
				{
					transformedImage = rotation.applyTransformations(this.framePrincipale.getSubImage());

					this.framePrincipale.setPoint1(new Point
					(
						transformedImage.getMinX() + this.framePrincipale.getPoint1().x(), 
						transformedImage.getMinY() + this.framePrincipale.getPoint1().y()
					));

					this.framePrincipale.setPoint2 ( new Point
					(
						transformedImage.getMinX() + transformedImage.getWidth () + this.framePrincipale.getPoint1().x(),
						transformedImage.getMinY() + transformedImage.getHeight() + this.framePrincipale.getPoint1().y()
					));

					g2d.drawImage(transformedImage, this.framePrincipale.getPoint1().x(), this.framePrincipale.getPoint1().y(), null);
				} 
				else 
				{

					g2d.drawImage(this.framePrincipale.getSubImage(), this.framePrincipale.getPoint1().x(), this.framePrincipale.getPoint1().y(), null);

					g2d.setColor(Color.BLACK);
					
					g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, new float[] { 10 }, 0)); // pointillé

					g2d.drawRect
					(
						Math.min(this.framePrincipale.getPoint1().x(),  this.framePrincipale.getPoint2().x()),
						Math.min(this.framePrincipale.getPoint1().y(),  this.framePrincipale.getPoint2().y()),
						Math.abs(this.framePrincipale.getPoint1().x() - this.framePrincipale.getPoint2().x()),
						Math.abs(this.framePrincipale.getPoint1().y() - this.framePrincipale.getPoint2().y())
					);
				}
			} 
			else 
			{

				transformedImage = rotation.applyTransformations(this.biImage);

				this.setSize(new Dimension(this.biImage.getWidth(), this.biImage.getHeight()));

				g2d.drawImage(transformedImage, 0, 0, null);
			}

			if (textBounds != null && editingText) 
			{
				g2d.setColor(Color.BLACK);
		
				// Définir un contour pointillé avec une épaisseur visible
				float[] dashPattern = {10, 6}; // Longueur des traits et espaces
				g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1, dashPattern, 0));
				g2d.drawRect(textBounds.x - 1, textBounds.y - 1, textBounds.width + 1, textBounds.height + 1);
			}

			g2d.dispose();
		}
	}

	/* ------------------------------------------------------------ */
	/*                 🐁 Gestion de la souris 🐁                  */
	/* ------------------------------------------------------------ */

	public Color getColorAtPoint (Point p) 
	{
		if (this.biImage != null && p.x() >= 0 && p.x() < this.biImage.getWidth() && p.y() >= 0 && p.y() < this.biImage.getHeight()) 
		{
			return new Color(this.biImage.getRGB(p.x(), p.y()));
		}
		return null; // Retourne null si la position est hors de l'image
	}

	@Override
	public void mouseClicked(MouseEvent e) 
	{
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

		if (this.biImage != null && x >= 0 && y >= 0 && x < this.biImage.getWidth() && y < this.biImage.getHeight()) 
		{
			this.framePrincipale.onClick(this.biImage, clickPoint.x(), clickPoint.y(), color);

			if (this.framePrincipale.isCurseurOn(Controleur.TEXT))
			{
				System.out.println("Il est passé par ici !");

				if (this.editingText) 
				{
					finalizeText(); // Terminer l'édition du texte en cours
				}

				startTextEditing(e.getX(), e.getY());
			}
		}

		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) 
	{
		
	}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		
	}

	@Override
	public void mouseEntered(MouseEvent e) 
	{
		
	}

	@Override
	public void mouseExited(MouseEvent e) 
	{
		
	}

	@Override
	public void mouseDragged(MouseEvent e) 
	{
		
	}

	@Override
	public void mouseMoved(MouseEvent e) 
	{
		
	}
}
