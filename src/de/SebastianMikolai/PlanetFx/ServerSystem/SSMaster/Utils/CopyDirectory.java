package de.SebastianMikolai.PlanetFx.ServerSystem.SSMaster.Utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CopyDirectory { 
     
    private BufferedInputStream in = null; 
    private BufferedOutputStream out = null; 
     
    public void copyDir(File quelle, File ziel) { 
    	File[] files = quelle.listFiles(); 
		ziel.mkdirs(); 
		for (File file : files) { 
		    if (file.isDirectory()) { 
		        copyDir(file, new File(ziel.getAbsolutePath() + System.getProperty("file.separator") + file.getName())); 
		    } else { 
		        copyFile(file, new File(ziel.getAbsolutePath() + System.getProperty("file.separator") + file.getName())); 
		    } 
		}
    }
     
    public void copyFile(File file, File ziel) { 
    	try {
			in = new BufferedInputStream(new FileInputStream(file)); 
			out = new BufferedOutputStream(new FileOutputStream(ziel, true)); 
			int bytes = 0; 
			while ((bytes = in.read()) != -1) { 
				out.write(bytes); 
			} 
			in.close(); 
			out.close(); 
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
} 