/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.data.gameplay;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.simon.src.game.data.gameplay.creatures.Creature;
import org.simon.src.game.data.gameplay.player.PlayerCharacterClass;
import org.simon.src.game.gui.GuiElement;
import org.simon.src.utils.Consts;
import org.simon.src.utils.CycleList;
import org.simon.src.utils.Log;
import org.simon.src.utils.ResourceManager;
import org.simon.src.utils.Settings;
import org.simon.src.utils.SlickUtils;

/**
 *
 * @author XyRoN
 */
public class GameplayManager {
    
    public enum Opponent {
        PLAYER ("Player"), ENEMY ("Enemy")
        ;
        public final String text;
        
        Opponent (String text) {
            this.text = text;
        }
        public Opponent opposite () {
            if (this.equals(PLAYER)) return ENEMY;
            return PLAYER;
        }
    }
    
    
    
    private static Opponent current_opponent;
    private static Creature current_casting_creature;
    
    private static List<Creature> enemy_board;
    private static List<Creature> ally_board;
    
    private static Map<String,PlayerCharacterClass> loaded_character_classes;
    
    
    
    public static final void init () {
        loadPlayerCharacterClasses();
    }
    
    
    
    public static void addEnemy (GuiElement element) {
        if (element.getCreatures().length == 1)
            enemy_board.add(current_casting_creature);
        else
            Log.err("Cannot add enemy to board since passed GUI element does not contain extactly one creature");
    }
    
    public static void addAlly (GuiElement element) {
        if (element.getCreatures().length == 1)
            ally_board.add(current_casting_creature);
        else
            Log.err("Cannot add ally to board since passed GUI element does not contain extactly one creature");
    }
    
    public static Opponent getCurrentOpponent () {
        return current_opponent;
    }
    
    public static String getCurrentOpponentText () {
        return current_opponent.text;
    }
    
    public static List<Creature> getEnemies () {
        return enemy_board;
    }
    
    public static List<Creature> getAllies () {
        return ally_board;
    }
    
    public static Creature getCurrentCastingCreature () {
        return current_casting_creature;
    }
    
    public static void setCurrentCastingCreature (Creature current_casting_creature) {
        GameplayManager.current_casting_creature = current_casting_creature;
    }
    
    public static List<PlayerCharacterClass> getAllPlayerCharacterClassesList () {
        return new ArrayList<> (loaded_character_classes.values());
    }
    
    public static Map<String, PlayerCharacterClass> getAllPlayerCharacterClasses () {
        return loaded_character_classes;
    }
    
    public static CycleList<PlayerCharacterClass> getAllPlayerCharacterClassesCycleList () {
        return new CycleList<> (loaded_character_classes.values());
    }
    
    
    
    public static void loadPlayerCharacterClasses () {
        current_opponent = Opponent.PLAYER;
        enemy_board = new ArrayList<> ();
        ally_board = new ArrayList<> ();
        loaded_character_classes = new HashMap<> ();
        File dump = new File (Settings.character_class_path);
        
        if (!dump.exists()) {
            Log.err("Missing player character class dump folder at '"+Settings.character_class_path+"'...");
        } else {
            File[] all_character_class_files = ResourceManager.getAllFilesOfExtensionInSubdirs(dump, Consts.CHARACTER_CLASS_FILE_EXTENSION);
            for (File character_class_file : all_character_class_files) {
                try {
                    String path = character_class_file.getCanonicalPath().replace(System.getProperty("file.separator"), "/");
                    String class_name = SlickUtils.getFileName(path);
                    
                    PlayerCharacterClass char_class = new PlayerCharacterClass(character_class_file);
                    loaded_character_classes.put(class_name, char_class);
                    Log.log("Loaded player character class '"+class_name+"' at path '"+path+"'");
                } catch (IOException ex) {
                    Log.err(ex);
                }
            }
        }
    }
}
