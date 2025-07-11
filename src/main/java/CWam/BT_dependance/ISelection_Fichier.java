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
import java.io.File;


public interface ISelection_Fichier {
  void fichierSelectionne(File fichier, JPanel_Select_Dossier source);
}
