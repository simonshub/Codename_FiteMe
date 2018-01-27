/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.data.save;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.simon.src.utils.Log;

/**
 *
 * @author XyRoN
 */
public class SavedStateFactory {
    
    public final static String SAVED_STATE_FILE_PATH = "save.snapshot";
    
    private final static SavedState SAVE_STATE = new SavedState ();
    
    private SavedStateFactory () { }
    
    
    
    public static void save () {
        Log.log("Saving game...");
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream (new File (SAVED_STATE_FILE_PATH));
            oos = new ObjectOutputStream (fos);
            
            SAVE_STATE.snapshot();
            oos.writeObject(SAVE_STATE);
            
            oos.flush();
            fos.flush();
            Log.log("Game saved successfully!");
        } catch (Exception ex) {
            Log.err("An exception occured while saving game...");
            Log.err(ex);
        } finally {
            try {
                if (fos!=null) fos.close();
                if (oos!=null) oos.close();
            } catch (IOException ex) {
                Log.err("An exception occured while saving game...");
                Log.err(ex);
            }
        }
    }
    
    
    
    public static void load () {
        Log.log("Loading game...");
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream (new File (SAVED_STATE_FILE_PATH));
            ois = new ObjectInputStream (fis);
            
            SavedState load = (SavedState) ois.readObject();
            load.apply();
            Log.log("Game progress loaded!");
        } catch (Exception ex) {
            Log.err("An exception occured while loading game...");
            Log.err(ex);
        } finally {
            try {
                if (fis!=null) fis.close();
                if (ois!=null) ois.close();
            } catch (IOException ex) {
                Log.err("An exception occured while loading game...");
                Log.err(ex);
            }
        }
    }
    
    
    
    public static void delete () {
        File f = new File (SAVED_STATE_FILE_PATH);
        if (f.exists()) f.delete();
    }
    
}
