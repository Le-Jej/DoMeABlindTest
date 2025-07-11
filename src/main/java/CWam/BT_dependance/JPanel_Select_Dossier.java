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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
 

//premier panel pour la sélection du nombre de seconde des extrait
public class JPanel_Select_Dossier extends JPanel{
    
	private JButton selection;
	private JTextField Label;
	
	private static final long serialVersionUID = 1L;

	public JPanel_Select_Dossier(String text, ISelection_Fichier listener) {
		setLayout(new GridLayout(1,2));
		Label = new JTextField();
		selection = new my_JButton(text);
		selection.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	    	if (e.getSource() == selection ) {
	    	  JFileChooser dialogue = new JFileChooser(new File("."));
	    	  dialogue.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    	  	    	  		
	    	  if (dialogue.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
	    	    Label.setText(dialogue.getSelectedFile().getPath());
	    	    listener.fichierSelectionne(dialogue.getSelectedFile(), JPanel_Select_Dossier.this);
	    	  }
	    	}
	      }
	    });
		add(selection);
		add(Label);
	}
	
	public void setText(String text) {
		Label.setText(text);
	}
	
	public String getText() {
		return Label.getText();
	}
		
}