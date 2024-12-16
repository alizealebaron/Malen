package malen.modele;

/** Classe Controleur
  * @author : Alizéa Lebaron
  * @version : 1.0.0 - 16/12/2024
  * @date : 16/12/2024
  */

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.Buffer;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;

import java.awt.Color;

public class Couleur 
{
	/* -------------------------------------- */
	/*               Attributs                */
	/* -------------------------------------- */

	

	/* -------------------------------------- */
	/*              Constructeur              */
	/* -------------------------------------- */

	public Couleur()
	{
		
	}

	/* -------------------------------------- */
	/*               Accesseurs               */
	/* -------------------------------------- */



	/* -------------------------------------- */
	/*              Modificateurs             */
	/* -------------------------------------- */


 
	/* -------------------------------------- */
	/*                 Méthodes               */
	/* -------------------------------------- */

	/** Méthode de remplissage
	 * @param biOriginal L'image sans la modification
	 * @param colOld La couleur a remplacé par la nouvelle
	 * @param colNew La couleur qui remplacera l'ancienne
	 * @param x Coordonnées x du point initial
	 * @param y Coordonnées y du point initial
	 * @return L'image modifiée
	 */
	public static BufferedImage fill(BufferedImage biOriginal, Color colOld, Color colNew, int x, int y)
	{
		// Initialisation de la liste des points
		Queue<Point> file = new LinkedList<Point>();
		file.add ( new Point ( x, y ) );

		// Récupération des couleurs en int
		int intCoulOld = colOld.getRGB() & 0xFFFFFF;
		int intCoulNew = colNew.getRGB() & 0xFFFFFF;

		// Changement de tous les points présents dans la file
		while ( ! file.isEmpty() )
		{
			// On retire le point de la liste
			Point p = file.remove();

			// On vérifie que le point est dans l'image et que la couleur est ressemblante
			if ( p.x() >= 0 && p.x() < biOriginal.getWidth  () && p.y() >= 0 && p.y() < biOriginal.getHeight () && distance (intCoulOld, biOriginal.getRGB(p.x(), p.y() ) & 0xFFFFFF ) < 90)
			{
				// On change la couleur
				biOriginal.setRGB ( p.x(), p.y(), colNew.getRGB());

				// On ajoute tous les voisins à la file
				file.add ( new Point ( p.x()+1, p.y()   ) );
				file.add ( new Point ( p.x()-1, p.y()   ) );
				file.add ( new Point ( p.x()  , p.y()-1 ) );
				file.add ( new Point ( p.x()  , p.y()+1 ) );
			}
		}

		return biOriginal;
	}

	/** Permet de comparer la distance entre deux couleurs
	 * @param coul1 Première couleur à comparée
	 * @param coul2 Deuxième couleur à comparée
	 * @return La distance séparant les deux couleurs
	 */
	public static double distance (int coul1, int coul2)
	{
		Color color1 = new Color (coul1);
		Color color2 = new Color (coul2);

		return Math.sqrt(Math.pow((color1.getRed() - color2.getRed()), 2 ) + Math.pow((color1.getGreen() - color2.getGreen()), 2 ) + Math.pow((color1.getBlue() - color2.getBlue()), 2 ));
	}

	/** Méthode pour tester sans le contrôleur
	 * @param args Arguments donnés
	 */
	// public static void main(String[] args) 
	// {
	// 	BufferedImage biSource = null;

	// 	try 
	// 	{
	// 		biSource = ImageIO.read(new File("asterix_couleur.png"));
	// 	} 
	// 	catch (Exception e) 
	// 	{
	// 		e.printStackTrace();
	// 	}

	// 	Color coulOrig = new Color(248,248,248);
	// 	Color coulNew  = new Color(  0,255,  0);

	// 	fill(biSource, coulOrig, coulNew, 1, 1);

	// 	try 
	// 	{
	// 		ImageIO.write(biSource, "png", new File("Arceus2.png"));
	// 	} 
	// 	catch (Exception e) 
	// 	{
	// 		e.printStackTrace();
	// 	}
	// }
	
}
