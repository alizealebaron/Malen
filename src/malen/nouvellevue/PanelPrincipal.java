package malen.nouvellevue;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.border.Border;

/** Panel principale de l'application
 * @author  : Alizéa Lebaron
 * @author  : Tom Goureau
 * @author  : Trystan Baillobay
 * @version : 1.0.0 - 19/12/2024
 * @since   : 19/12/2024
 */



public class PanelPrincipal extends JPanel
{
	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/
	/*                                                                 Attributs                                                                        */
	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/

	private PanelOutils      panelOutils;
	private PanelImage       panelImage;
	private FramePrincipale  framePrincipale;

	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/
	/*                                                                Controleurs                                                                       */
	/*--------------------------------------------------------------------------------------------------------------------------------------------------*/

	public PanelPrincipal (FramePrincipale frame, PanelImage panelI, PanelOutils panelO)
	{
		// Initialisation des composants
		this.panelOutils = panelO;
		this.panelImage  = panelI;

		this.framePrincipale = frame;

		// Initialisation du panel
		this.setLayout(new BorderLayout());
		this.add(panelOutils, BorderLayout.NORTH );
		this.add(panelImage , BorderLayout.CENTER);
	}
}
