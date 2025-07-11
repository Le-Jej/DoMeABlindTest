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
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
 

//premier panel pour la sélection du nombre de seconde des extrait
public class JPanel_Debut_Extraction extends JPanel{
    
	private JSlider testSlider;
	private JLabel nbSeconde;
	
	private static final long serialVersionUID = 1L;

	public JPanel_Debut_Extraction() {
		setLayout(new GridLayout(1,2));
		//Slide des seondes
		nbSeconde = new JLabel();
		testSlider = new JSlider(0,180);
		testSlider.setValue(120);
		testSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
	            nbSeconde.setText("Extraction dans les  : " + testSlider.getValue() + " premières secondes");
	        }
		});
	    this.add(testSlider);
	    
	    nbSeconde.setText("Extraction dans les  : " + testSlider.getValue() + " premières secondes");
	    this.add(nbSeconde);
	}
	
	public int getValue() {
		return testSlider.getValue();
	}
  
  
}