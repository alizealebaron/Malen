package malen.modele;

/** Classe Controleur
  * @author : Alizéa Lebaron
  * @version : 1.0.0 - 16/12/2024
  * @date : 16/12/2024
  */

import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;

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
	public BufferedImage fill(BufferedImage biOriginal, Color colOld, Color colNew, int x, int y)
	{
		Queue<Point> file = new LinkedList<Point>();

		file.add ( new Point ( x, y ) );

		while ( ! file.isEmpty() )
		{
			Point p = file.remove();

			if ( p.x() >= 0 && p.x() < bi.getWidth  () && p.y() >= 0 && p.y() < bi.getHeight () &&
			     distance (colorOrig, bi.getRGB(p.x(), p.y() ) & 0xFFFFFF) < 90
			   )
			{
				biOriginal.setRGB ( p.x(), p.y(), coul );

				file.add ( new Point ( p.x()+1, p.y()   ) );
				file.add ( new Point ( p.x()-1, p.y()   ) );
				file.add ( new Point ( p.x()  , p.y()-1 ) );
				file.add ( new Point ( p.x()  , p.y()+1 ) );
			}
		}

		return null;
	}

	public static double distance (int coul1, int coul2)
	{
		Color color1 = new Color (coul1);
		Color color2 = new Color (coul2);

		return Math.sqrt(Math.pow((color1.getRed() - color2.getRed()), 2 ) + Math.pow((color1.getGreen() - color2.getGreen()), 2 ) + Math.pow((color1.getBlue() - color2.getBlue()), 2 ));
	}



	
}
