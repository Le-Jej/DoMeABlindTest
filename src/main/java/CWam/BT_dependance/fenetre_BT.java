/* DoMeABlindTest - Un logiciel en cours de construction
# Copyright (C) 2025 Jérôme MARTHE
#
# Ce programme est libre : vous pouvez le redistribuer et/ou le modifier
# selon les termes de la Licence Publique Générale GNU publiée par la
# Free Software Foundation (version 3 ou ultérieure).
#
# Ce programme est distribué dans l’espoir qu’il sera utile,
# mais SANS AUCUNE GARANTIE ; sans même la garantie implicite de
# QUALITÉ MARCHANDE ou D’ADÉQUATION À UN BUT PARTICULIER.
# Voir la Licence Publique Générale GNU pour plus de détails.
#
# Vous devriez avoir reçu une copie de la Licence Publique Générale GNU
# avec ce programme. Sinon, voir <https://www.gnu.org/licenses/>.
*/

package CWam.BT_dependance;
import javax.swing.JFrame;
 
public class fenetre_BT extends JFrame{
    
	
	private static final long serialVersionUID = 1L;

  //fenetre par defaut	
  public fenetre_BT(){

    super();          
    //Définit un titre pour notre fenêtre
    this.setTitle("Random ton doss");
    //Définit sa taille : 400 pixels de large et 100 pixels de haut
    this.setSize(1280, 720);
    //Nous demandons maintenant à notre objet de se positionner au centre
    this.setLocationRelativeTo(null);
    //Rend la fenetre non redimensionnable
    this.setResizable(false);
    //Termine le processus lorsqu'on clique sur la croix rouge
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    //on charge avec notre panneau personalisé par defaut
    this.setContentPane(new panneau_BT());
  }
  
  
}