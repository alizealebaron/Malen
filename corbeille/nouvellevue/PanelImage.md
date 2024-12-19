package malen.nouvellevue;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;

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
	private File            fontImage = null;

	private double  rotate_angle   = 0;
	private boolean flipHorizontal = false;
	private boolean flipVertical   = false;

	private boolean isMovingSubImage;

	
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

	public boolean       isImage        ( ) { return this.biImage != null; }
	public BufferedImage getImage       ( ) { return this.biImage        ; }
	public Color         getColorText   ( ) { return this.textColor      ; }
	public JTextField    getTextField   ( ) { return this.textField      ; }
	public Double        getRotateAngle ( ) { return this.rotate_angle   ; }

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

	public void setTextColor   ( Color  c ) { this.textColor    = c; }
	public void setRotateAngle ( Double a ) { this.rotate_angle = a; }
	public void setFontImage   ( File   f ) { this.fontImage    = f; }

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
		// Création du graphique g2d pour ajouter le texte
		Graphics2D g2d = biImage.createGraphics();

		if (editingText) 
		{
			String text = textField.getText();

			// On ajout le texte à l'image s'il n'est pas vide
            if (!text.isEmpty())
			{
				if (this.fontImage != null)
				{	
					try 
					{
						BufferedImage backgroundImage = ImageIO.read(new File(this.fontImage.getAbsolutePath()));

						TexturePaint texturePaint = new TexturePaint
						(
							backgroundImage,
							new Rectangle2D.Double(0, 0, backgroundImage.getWidth(), backgroundImage.getHeight())
						);

						// Définir la texture comme peinture
						g2d.setPaint(texturePaint);

						// Définir une police pour le texte
						g2d.setFont(this.textFont);

						// Créer un objet Shape représentant le texte
						FontMetrics metrics = g2d.getFontMetrics(this.textFont);
						
						Shape textShape = this.textFont.createGlyphVector(g2d.getFontRenderContext(), text).getOutline(textBounds.x + 2, textBounds.y + textBounds.height - 10);

						// Remplir le texte avec la texture
						g2d.fill(textShape);

						// Optionnel : Ajouter un contour au texte
						g2d.draw(textShape);
					}
					catch (Exception e) 
					{
						System.err.println("Erreur lors du chargement de l'image de fond de texte : " + e);
					}
					
				}	
				else
				{
					g2d.setFont(textFont);
					g2d.setColor(this.textColor);
					g2d.drawString(text, textBounds.x + 2, textBounds.y + textBounds.height - 10);
				}
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

	public BufferedImage changeImage(BufferedImage rotaImage) 
	{
		Rotation rotation = new Rotation(rotate_angle, flipHorizontal, flipVertical);
		return rotation.getImage(rotaImage);
	}

	/* ------------------------------------------------------------ */
	/*                     ✨ Retournement ✨                      */
	/* ------------------------------------------------------------ */

	public void switchFlipHorizontal ()
	{
		this.flipHorizontal = !this.flipHorizontal;
		repaint();
	}

	public void switchFlipVertical ()
	{
		this.flipVertical = !this.flipVertical;
		repaint();
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
		if (this.framePrincipale.getOutil() == 'R') 
		{
			if (this.framePrincipale.getPoint1() != null && this.framePrincipale.getPoint2() != null) 
			{
				this.framePrincipale.setSubimage(this.changeImage(this.framePrincipale.getSubImage()));
			} 
			else 
			{
				this.biImage = this.changeImage(this.biImage);
			}

			this.framePrincipale.showOutilSlider('R');

			//outilSlider.setValue(0); //TODO: Peut-être pas besoin mais je le note au cas où

			this.repaint();
		}

		if (this.framePrincipale.isCurseurOn(Controleur.SELECTION_RECTANGLE) || this.framePrincipale.isCurseurOn(Controleur.SELECTION_OVALE)) 
		{
			if (null == this.framePrincipale.getSubImage()) 
			{
				this.framePrincipale.setPoint1(new Point((int) e.getPoint().getX(), (int) e.getPoint().getY()));
				this.framePrincipale.setPoint2(null); // Réinitialiser le deuxième point

				this.framePrincipale.createSubImage(null);
			} 
			else 
			{
				int x1 = Math.min(this.framePrincipale.getPoint1().x(), this.framePrincipale.getPoint2().x());
				int y1 = Math.min(this.framePrincipale.getPoint1().y(), this.framePrincipale.getPoint2().y());
				int x2 = Math.max(this.framePrincipale.getPoint1().x(), this.framePrincipale.getPoint2().x());
				int y2 = Math.max(this.framePrincipale.getPoint1().y(), this.framePrincipale.getPoint2().y());

				if (e.getX() >= x1 && e.getX() <= x2 && e.getY() >= y1 && e.getY() <= y2) 
				{
					// Si le clic est dans la zone de la subimage, on commence à la déplacer
					this.isMovingSubImage = true;
				} 
				else 
				{
					java.awt.Point awtPoint = e.getPoint();
					Point clickPoint = new Point((int) awtPoint.getX(), (int) awtPoint.getY());
					this.framePrincipale.onClick(this.biImage, clickPoint.x(), clickPoint.y(), getColorAtPoint(clickPoint));
					// Sinon, on commence une nouvelle sélection
					this.framePrincipale.setPoint1(new Point((int) e.getPoint().getX(), (int) e.getPoint().getY()));
					this.framePrincipale.setPoint2(null); // Réinitialiser le deuxième point
					this.framePrincipale.createSubImage(null);
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		if (isMovingSubImage) {
			// Fin du déplacement de la subimage
			isMovingSubImage = false;
		} 
		else if (this.framePrincipale.isCurseurOn(Controleur.SELECTION_RECTANGLE) || this.framePrincipale.isCurseurOn(Controleur.SELECTION_OVALE)) 
		{
			// Terminer la sélection et créer la subimage
			this.framePrincipale.setPoint2(new Point(e.getX(), e.getY()));
			this.framePrincipale.createSubImage(this.biImage);
		}

		this.repaint();
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
		if (isMovingSubImage) 
		{
			// Calcul du déplacement relatif
			int deltaX = e.getX() - this.framePrincipale.getPoint1().x();
			int deltaY = e.getY() - this.framePrincipale.getPoint1().y();

			// Mettre à jour les points pour "déplacer" la subimage
			this.framePrincipale.setPoint1(new Point(this.framePrincipale.getPoint1().x() + deltaX, this.framePrincipale.getPoint1().y() + deltaY));
			this.framePrincipale.setPoint2(new Point(this.framePrincipale.getPoint2().x() + deltaX, this.framePrincipale.getPoint2().y() + deltaY));

			this.repaint();
		} 
		else 
		{
			// Mettre à jour le deuxième point pour le rectangle de sélection
			if (this.framePrincipale.isCurseurOn(Controleur.SELECTION_RECTANGLE)
					|| this.framePrincipale.isCurseurOn(Controleur.SELECTION_OVALE)) {
				this.framePrincipale.setPoint2(new Point((int) e.getPoint().getX(), (int) e.getPoint().getY()));
				this.repaint();
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) 
	{
		
	}
}
