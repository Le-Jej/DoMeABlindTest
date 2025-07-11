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
import java.io.IOException;
import java.io.EOFException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.swing.*;

import org.apache.commons.io.FileUtils;

import com.mpatric.mp3agic.Mp3File;

import java.util.Map;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFileFormat;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


// extension de panneau
public class panneau_BT extends JPanel implements ISelection_Fichier{ 

	//chemin du doss
	private JPanel_Temps_Extrait P1;
	private JPanel_Debut_Extraction P2;
	private JPanel_Select_Dossier P3; //dossier d'entrée
	private JPanel_Select_Dossier P4; //dossier des réponse
	private JPanel_Select_Dossier P5; //dossier des extractions
	private JButton BPush;
	//private JLabel label_Path_BT;
	//private JLabel label_Path_Reponse;
	private JLabel etat_traitement;
	
	// un truc surement utile
	private static final long serialVersionUID = 1L;

	//Create par defaut	
  public panneau_BT(){
	this.setLayout(new GridLayout(10,1));
	
	P1 = new JPanel_Temps_Extrait();
	this.add(P1);    
	
	P2 = new JPanel_Debut_Extraction();
	this.add(P2);
	
	//les differents dossier
	P3 = new JPanel_Select_Dossier("Choisir le dossier avec les audio",this);
	P3.setName("P3");
	this.add(P3);
	
	P4 = new JPanel_Select_Dossier("Choisir le dossier avec les audio randomisés",this);
	P4.setName("P4");
	this.add(P4);
	
	P5 = new JPanel_Select_Dossier("Choisir le dossier avec les extraits audio anonymisés",this);
	P5.setName("P5");
	this.add(P5);
    
    //le bouton de recherche du dossier
    BPush = new my_JButton("Cliquez ici");
    BPush.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
    	if (e.getSource() == BPush ) {
    	  //JFileChooser dialogue = new JFileChooser(new File("."));
    	  //dialogue.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    	  File dossier = new File(P3.getText());
    	  		
    	  //if (dialogue.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
    	  //  dossier = dialogue.getSelectedFile();
    	  parcours_Du_Doss(dossier);
    	  //}
    	}
      }
    });
    this.add(BPush);
    
    //avancement du traitement
    etat_traitement = new JLabel();
    this.add(etat_traitement);
  }        
  
  //traitement principal
  public void parcours_Du_Doss(File dossier){
	int nb_Fichier = get_Nb_Audio(dossier);
	
	int[] int_Random = creer_Tableau_Random(nb_Fichier);
	
	//on creer le dossier BT (pour Blind Test)
	File path_Reponse = new File(P4.getText());
	File path_BT = new File(P5.getText());
	if (dossier_Existe(path_BT) || dossier_Existe(path_Reponse)) {
		//message de confirmation
		int choix = JOptionPane.showConfirmDialog(
				  null,
				  "Attention, les dossier " + path_BT.getPath() + " et " + path_Reponse.getPath() + " vont être supprimer dans le process." + 
				  "Voulez vous continuer ? " ,
				  "Confirmation",
				  JOptionPane.YES_NO_CANCEL_OPTION,
				  JOptionPane.QUESTION_MESSAGE
				);

		if (choix == JOptionPane.YES_OPTION) {
		  FileUtils.deleteQuietly(path_BT);
		  FileUtils.deleteQuietly(path_Reponse);
		} else {
		  return;
		}
	}
	
	
	//on creer le dossier réponse
	path_BT.mkdir();
	path_Reponse.mkdir();
	
	//lancement dans un thread en arrière plan
	long_Traitement_Principal(dossier,int_Random);
	
  }
  
  public int get_Nb_Audio(File dossier) {
	  int retour = 0;
	  for (File fichier : dossier.listFiles()) {
		if (!fichier.isDirectory() && isAudio(fichier)) {
		  retour++;
		}
	  }
	  return retour;
  }
  
  public static void extractRandomClip(File inFile, 
		                               File outFile, 
		                               int secDuration,
		                               int secMax) throws Exception {
	// 1. Obtenir un AudioInputStream MP3
    AudioInputStream mp3Stream = AudioSystem.getAudioInputStream(inFile);
    AudioFormat baseFormat = mp3Stream.getFormat();

    // 2. Créer un format PCM signé 16 bits, stereo, 44100 Hz (standard CD)
    AudioFormat pcmFormat = new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED,
            baseFormat.getSampleRate(),
            16,
            baseFormat.getChannels(),
            baseFormat.getChannels() * 2,
            baseFormat.getSampleRate(),
            false);
    
    // 3. Convertir le flux MP3 vers PCM
    AudioInputStream pcmStream = AudioSystem.getAudioInputStream(pcmFormat, mp3Stream);
  
    // 4. Calculer taille du flux en bytes et en frames
    AudioFileFormat aff = AudioSystem.getAudioFileFormat(inFile);
    Map<String,Object> props = aff.properties();
    
    int frameSize = pcmFormat.getFrameSize(); // channels*2
    long bytesPerSec = (long) ( pcmFormat.getSampleRate() * frameSize);
    
    
    long totalBytes;
    Long durUs = (Long) props.get("duration");
    if (durUs != null) {
        double durSec = durUs / 1_000_000.0;
        totalBytes = Math.round(durSec * bytesPerSec);

    } else {
        // 2. Fallback : mp3agic
        Mp3File mp3 = new Mp3File(inFile.getPath().toString());
        long durSec2 = mp3.getLengthInSeconds();
        totalBytes = durSec2 * bytesPerSec;
    }
    
 	// 5. Nombre de bytes à extraire pour secDuration secondes
    long clipBytes = secDuration* bytesPerSec;	
    
    
    // 6. Choisir une position random alignée sur frame
    long maxStartByte = secMax * bytesPerSec;
    //si le morceau plus court que le temps de debut d'extraction -> limite haute a temps total - temps d'extrait
    if (maxStartByte > totalBytes) maxStartByte = totalBytes-clipBytes;
    //si le morceau est plus court que le temps d'extrait
    if (maxStartByte < 0) maxStartByte = 0;
    
    long randomStart = (long)(Math.random() * maxStartByte);
    randomStart -= (randomStart % frameSize);

    // 7. Avancer le flux à la position random
    long skipped = 0;
    byte[] buffer = new byte[(int) bytesPerSec]; // Taille du tampon
    while (skipped < randomStart) {
        int bytesRead = pcmStream.read(buffer, 0, (int) Math.min(buffer.length, randomStart - skipped));
        if (bytesRead == -1) {
            throw new EOFException("Fin de flux atteinte avant la position souhaitée.");
        }
        skipped += bytesRead;
    }
    
    // 8. Lire clipBytes bytes dans un buffer
    try (AudioInputStream clipStream = new AudioInputStream(pcmStream, pcmFormat, clipBytes / frameSize)) {
    	AudioInputStream clipNorm = normaliser(clipStream);
        AudioSystem.write(clipNorm, AudioFileFormat.Type.WAVE, outFile);
    }    
    
  }
  
  public static AudioInputStream normaliser(AudioInputStream clipStream) throws IOException{
	AudioFormat fmt = clipStream.getFormat();
	int frameSize = fmt.getFrameSize();
	int channels = fmt.getChannels();
	float frameRate = fmt.getFrameRate();

    // Lecture totale du flux
    byte[] audioBytes = clipStream.readAllBytes();
    int totalFrames = audioBytes.length / frameSize;

    // Configuration des fenêtres de 500 ms
    int winMs = 500;
    int winFrames = Math.round(frameRate * winMs / 1000);
    int winBytes = winFrames * frameSize;

    List<Double> rmsList = new ArrayList<>();
    for (int pos = 0; pos + winBytes <= audioBytes.length; pos += winBytes) {
        double sumSq = 0;
        int samples = winBytes / 2;
        ByteBuffer bbWin = ByteBuffer.wrap(audioBytes, pos, winBytes)
                                      .order(ByteOrder.LITTLE_ENDIAN);
        for (int i = 0; i < samples; i++)
            sumSq += bbWin.getShort(i * 2) * (double) bbWin.getShort(i * 2);
        rmsList.add(Math.sqrt(sumSq / samples));
    }

    double targetRms = Short.MAX_VALUE * 0.6;
    double maxRms = Collections.max(rmsList);
    double globalGain = targetRms / maxRms;

    List<Double> smoothGains = new ArrayList<>();
    for (int i = 0; i < rmsList.size(); i++) {
        double sum = 0;
        int cnt = 0;
        for (int j = i - 1; j <= i + 1; j++)
            if (j >= 0 && j < rmsList.size()) {
                sum += targetRms / rmsList.get(j);
                cnt++;
            }
        smoothGains.add(sum / cnt);
    }

    // 4. Application du gain échantillon par échantillon, canal par canal
    ByteBuffer buf = ByteBuffer.wrap(audioBytes).order(ByteOrder.LITTLE_ENDIAN);
    for (int frame = 0; frame < totalFrames; frame++) {
        int winIdx = Math.min(frame / winFrames, smoothGains.size() - 1);
        double gain = Math.min(globalGain, smoothGains.get(winIdx));
        int base = frame * frameSize;
        for (int ch = 0; ch < channels; ch++) {
            int byteIndex = base + ch * 2;
            short s = buf.getShort(byteIndex);
            int mod = (int)Math.round(s * gain);
            mod = Math.max(Short.MIN_VALUE, Math.min(Short.MAX_VALUE, mod));
            buf.putShort(byteIndex, (short)mod);
        }
    }

    // Écriture du fichier WAV
    ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);
    AudioInputStream normStream = new AudioInputStream(bais, fmt, totalFrames);
	return normStream;    
  }
  
  //creer un tableau d'indice randomiser
  public int[] creer_Tableau_Random (int taille) {
	  int[] retour = new int[taille];
	  int ind1,ind2,swap;
	  //on rempli le tableau
	  for (int i=0; i < taille; i++) {
		retour[i] = i + 1;  
	  }
	  //on le mélange
	  for (int i=0; i < 10000; i++) {
		  ind1 = (int) (Math.random() * taille);
		  ind2 = (int) (Math.random() * taille);
		  swap = retour[ind1];
		  retour[ind1] = retour[ind2];
		  retour[ind2] = swap;
	  }
	  return retour;
  }
  
  public boolean isAudio(File fichier) {
	  boolean retour = false;
	  
	  //récupérer l'extension (merci chatGPT)
	  String chemin = fichier.getPath();
	  int dernier_Point = chemin.lastIndexOf(".");
	  String mon_extension = " ";
	  
	  //si trouvé
	  if (dernier_Point > 0 && dernier_Point < chemin.length() - 1) {
		  mon_extension = chemin.substring(dernier_Point + 1);
      }
	  
	  
	  switch (mon_extension){
	  case "mp3":
	  case "wav":
	  //case "ogg":
	  //case "flac":
			retour = true;	
		}
	  return retour;
  }
  
  public boolean dossier_Existe(final File f){
	if (f.exists() && f.isDirectory()){
  	  return true;
  	}else {
  	  return false;
  	}
  }
  
  
  public void long_Traitement_Principal(File dossier, int[] int_Random) {
      new extraction_Global(dossier, int_Random);
      
  }
  
  
  private class extraction_Global extends SwingWorker<Void, Integer> {
      private final File dossier;
      private final int[] int_Random; 

      public extraction_Global(File dossier, int[] int_Random) {
          this.dossier = dossier;
          this.int_Random = int_Random;
          execute();
      }

      @Override
      protected Void doInBackground() throws Exception {
    	File path_BT = new File(dossier.getPath() + "\\BT" );
        File path_Reponse = new File(dossier.getPath() + "\\Reponse" );
        int i = 0;
    	int nb_Audio = int_Random.length;
        
    	for (File fichier : dossier.listFiles()) {
    	  publish(i,nb_Audio);
    	  //Copie dans BT
    	  Path fichier_Entree = Paths.get(fichier.getPath());
    	  Path fichier_Sortie = Paths.get(path_BT.getPath() + "\\" + String.format("%02d",int_Random[i]) + ".wav" );

   		  // si un fichier audio	 
   		  if (!fichier.isDirectory() && isAudio(fichier)) {
    							
   			//Extraction de n seconde + enregistrement en .wav dan le dossier BT
   			File fichier_In = fichier;
   			File fichier_Out = new File(fichier_Sortie.toString());
   			try {
   			  extractRandomClip(fichier_In, fichier_Out, P1.getValue(),P2.getValue());
   			} catch (Exception e1) {
   			  e1.printStackTrace();
   			}				
   				
   			//Copie dans Réponse
   			fichier_Sortie = Paths.get(path_Reponse.getPath() + "\\" + String.format("%02d",int_Random[i]) + 
   						         " - " + fichier.getName() );
   			try {
   			  Files.copy(fichier_Entree, fichier_Sortie,StandardCopyOption.REPLACE_EXISTING);
   			} catch (IOException e) {
   			  e.printStackTrace();
   			}
   			i++;
   		  //fin traitement "si audio"	
   		  }
   		}
      return null;
      }

      @Override
      protected void process(java.util.List<Integer> chunks) {
          int processedFiles = chunks.get(0);
          int totalFile = chunks.get(1);
          etat_traitement.setText(processedFiles + "/" + totalFile +  " fichiers traités");
      }

      @Override
      protected void done() {
    	  etat_traitement.setText("Traitement terminé");
      }
     
  }  
  
  public void fichierSelectionne(File fichier, JPanel_Select_Dossier source) {
      String nom = source.getName();
      
      if ("P3".equals(nom)){
    	  P4.setText(fichier.getAbsolutePath() + "\\Reponse");
    	  P5.setText(fichier.getAbsolutePath() + "\\BT");
      }
  }
  
}