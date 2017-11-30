/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.data.gameplay.creatures;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.simon.src.game.data.gameplay.PointTypeEnum;
import org.simon.src.utils.Consts;
import org.simon.src.utils.Log;
import org.simon.src.utils.ResourceManager;
import org.simon.src.utils.Settings;
import org.simon.src.utils.SlickUtils;

/**
 *
 * @author XyRoN
 */
public class CreatureLibrary {
    
    public static final String PARSE_KEYWORD_SET_NEW_CREATURE = "crid";
    public static final String PARSE_KEYWORD_SET_NAME = "name";
    public static final String PARSE_KEYWORD_SET_ICON = "icon";
    public static final String PARSE_KEYWORD_SET_POINTS = "pnts";
    public static final String PARSE_KEYWORD_SET_HEALTH = "hlth";
    public static final String PARSE_KEYWORD_SET_ARMOR = "armr";
    public static final String PARSE_KEYWORD_SET_ATK_MOD = "amod";
    public static final String PARSE_KEYWORD_SET_ICON_SCALE = "icsz";
    
    public static final String PARSE_DELIMITER = ":";
    public static final String PARSE_COMMENT = "#";
    
    private static List<String> loaded_creature_packs;
    private static Map<String, Creature> creature_lib;
    
    
    
    public static final void init () {
        loadCreaturePacks();
    }
    
    
    
    private static void loadCreaturePacks () {
        creature_lib = new HashMap<> ();
        loaded_creature_packs = new ArrayList<> ();
        File dump = new File (Settings.creature_pack_path);
        
        if (!dump.exists()) {
            Log.err("Missing creature pack resource dump folder at '"+Settings.creature_pack_path+"'...");
        } else {
            File[] all_creature_packs = ResourceManager.getAllFilesOfExtensionInSubdirs(dump, Consts.CREATURE_PACK_FILE_EXTENSION);
            for (File creature_pack : all_creature_packs) {
                try {
                    String path = creature_pack.getCanonicalPath().replace(System.getProperty("file.separator"), "/");
                    String pack_name = path.substring(path.indexOf(Settings.creature_pack_path)+Settings.creature_pack_path.length(), path.lastIndexOf("."));
                    
                    String contents = new Scanner(creature_pack).useDelimiter("\\Z").next();
                    parseCreaturePackFile(pack_name, contents);
                    
                    loaded_creature_packs.add(pack_name);
                    Log.log("Loaded creature pack '"+pack_name+"' at path '"+path+"'");
                } catch (IOException ex) {
                    Log.err(ex);
                }
            }
        }
    }
    
    private static void parseCreaturePackFile (String pack_name, String contents) {
        String[] lines = contents.split("\n");
        
        String current_creature_id="";
        String current_creature_name="";
        String current_creature_icon="";
        int current_creature_hp=0;
        int current_creature_armor=0;
        int current_creature_atk_mod=0;
        float current_creature_icon_scale=1f;
        HashMap<PointTypeEnum, Integer> current_creature_points = new HashMap<> ();
        
        for (String line : lines) {
            // ignore empty and comment lines
            line = line.trim();
            if (line.isEmpty() || line.startsWith(PARSE_COMMENT)) continue;
            
            // split and remove "empty" keywords
            LinkedList<String> keywords = new LinkedList<> (Arrays.asList(line.trim().split(PARSE_DELIMITER)));
            for (int i=0;i<keywords.size();i++) {
                if (keywords.get(i).trim().isEmpty())
                    keywords.remove(i);
                else
                    keywords.set(i, keywords.get(i).trim());
            }
            
            if (keywords.size()>2) {
                Log.err("Found "+keywords.size()+" parameters while reading creature, instead of 2");
                Log.err("for line '"+line+"'");
                continue;
            }
            
            // determine parse action for this line
            String parse_action = keywords.size()==2 ? keywords.get(0) : "";
            String parse_value = keywords.size()==2 ? keywords.get(1) : "";
            
            // if all creature fields have been set, save the creature automatically
            if (!current_creature_id.isEmpty() && !current_creature_name.isEmpty() && !current_creature_icon.isEmpty()
                    && !current_creature_points.isEmpty() && current_creature_hp!=0) {
                if (!current_creature_id.isEmpty() &&
                    ( current_creature_name.isEmpty() || current_creature_points.isEmpty() || current_creature_icon.isEmpty() || current_creature_hp==0 ))
                    Log.err("Auto saving creature '"+current_creature_id+"' but still some properties missing?; missing properties - " +
                        (current_creature_name.isEmpty() ? "name " : "") +
                        (current_creature_icon.isEmpty() ? "icon " : "") +
                        (current_creature_points.isEmpty() ? "pnts " : "") +
                        (current_creature_hp==0 ? "hlth " : "") );
                
                // if all creature fields are set for this creature, finish loading it
                saveCreature(current_creature_id, pack_name, current_creature_name, current_creature_icon, current_creature_icon_scale, current_creature_hp, current_creature_armor, current_creature_atk_mod, current_creature_points);
                current_creature_id = "";
                current_creature_name = "";
                current_creature_icon = "";
                current_creature_hp = 0;
                current_creature_armor = 0;
                current_creature_atk_mod = 0;
                current_creature_icon_scale=1f;
                current_creature_points = new HashMap<> ();
            }
            
            switch (parse_action) {
                case PARSE_KEYWORD_SET_NEW_CREATURE :
                    if (!current_creature_id.isEmpty() &&
                        ( current_creature_name.isEmpty() || current_creature_icon.isEmpty() || current_creature_points.isEmpty() || current_creature_hp==0 ))
                        Log.err("Not finished parsing creature '"+current_creature_id+"', but new creature '"+keywords.get(1)+"' already started; missing properties - " +
                            (current_creature_name.isEmpty() ? "name " : "") +
                            (current_creature_icon.isEmpty() ? "icon " : "") +
                            (current_creature_points.isEmpty() ? "pnts " : "") +
                            (current_creature_hp==0 ? "hlth " : "") );
                    
                    if (!current_creature_id.isEmpty()) {
                        saveCreature(current_creature_id, pack_name, current_creature_name, current_creature_icon, current_creature_icon_scale, current_creature_hp, current_creature_armor, current_creature_atk_mod, current_creature_points);
                    }
                    
                    current_creature_id = parse_value;
                    current_creature_name = "";
                    current_creature_icon = "";
                    current_creature_hp = 0;
                    current_creature_armor = 0;
                    current_creature_atk_mod = 0;
                    current_creature_icon_scale=1f;
                    current_creature_points = new HashMap<> ();
                    break;
                case PARSE_KEYWORD_SET_NAME :
                    current_creature_name = parse_value;
                    break;
                case PARSE_KEYWORD_SET_ICON :
                    current_creature_icon = parse_value;
                    break;
                case PARSE_KEYWORD_SET_ICON_SCALE :
                    try {
                        current_creature_icon_scale = Float.parseFloat(parse_value.trim().replace(",","."));
                    } catch (NumberFormatException ex) {
                        Log.err("Error while parsing icon scale for creature '"+current_creature_id+"'");
                    }
                    break;
                case PARSE_KEYWORD_SET_POINTS :
                    LinkedList<String> point_tokens = new LinkedList<> (Arrays.asList(keywords.get(1).split(" ")));
                    for (int i=0;i<point_tokens.size();i++) {
                        if (point_tokens.get(i).trim().isEmpty())
                            point_tokens.remove(i);
                        else
                            point_tokens.set(i, point_tokens.get(i).trim());
                    }
                    
                    if (point_tokens.size()%2 != 0) {
                        Log.err("Odd number of point arguments");
                        break;
                    }
                    
                    int amount;
                    String which_type_str="";
                    PointTypeEnum which_type = PointTypeEnum.values()[0];
                    for (int i=0;i<point_tokens.size();i++) {
                        try {
                            if (which_type_str.isEmpty()) {
                                which_type_str = point_tokens.get(i);
                                which_type = PointTypeEnum.getByCode(which_type_str);
                            } else {
                                amount = Integer.parseInt(point_tokens.get(i));

                                current_creature_points.put(which_type, amount);

                                which_type_str = "";
                                which_type = PointTypeEnum.values()[0];
                            }
                        } catch (NumberFormatException ex) {
                            Log.err("Unreadable token '"+point_tokens.get(i)+"'!");
                        }
                    }
                    break;
                case PARSE_KEYWORD_SET_HEALTH :
                    try {
                        int health = Integer.parseInt(parse_value);
                        current_creature_hp = Math.abs(health);
                    } catch (NumberFormatException ex) {
                        Log.err("Error while parsing health value for creature '"+current_creature_id+"'");
                    }
                    break;
                case PARSE_KEYWORD_SET_ARMOR :
                    try {
                        int armor = Integer.parseInt(parse_value);
                        current_creature_armor = Math.abs(armor);
                    } catch (NumberFormatException ex) {
                        Log.err("Error while parsing armor value for creature '"+current_creature_id+"'");
                    }
                    break;
                case PARSE_KEYWORD_SET_ATK_MOD :
                    try {
                        int atk_mod = Integer.parseInt(parse_value);
                        current_creature_atk_mod = Math.abs(atk_mod);
                    } catch (NumberFormatException ex) {
                        Log.err("Error while parsing attack modifier value for creature '"+current_creature_id+"'");
                    }
                    break;
                default:
                    break;
            }
        }
            
        // if the last creature has not been saved, save it
        if (!current_creature_id.isEmpty() && !creature_lib.containsKey(current_creature_id)) {
            if (!current_creature_id.isEmpty() &&
                ( current_creature_name.isEmpty() || current_creature_icon.isEmpty() || current_creature_points.isEmpty() || current_creature_hp==0 ))
                Log.err("Not finished parsing creature '"+current_creature_id+"', but end of file reached; missing properties - " +
                    (current_creature_name.isEmpty() ? "name " : "") +
                    (current_creature_icon.isEmpty() ? "icon " : "") +
                    (current_creature_points.isEmpty() ? "pnts " : "") +
                    (current_creature_hp==0 ? "hlth " : "") );
            
            saveCreature(current_creature_id, pack_name, current_creature_name, current_creature_icon, current_creature_icon_scale, current_creature_hp, current_creature_armor, current_creature_atk_mod, current_creature_points);
        }
    }
    
    public static void saveCreature (String creature_id, String pack_name, String creature_name, String creature_icon, float creature_icon_scale, int creature_hp, int creature_armor, int creature_atk_mod, HashMap<PointTypeEnum,Integer> points) {
        Creature creature = new Creature (creature_id, pack_name, creature_name, creature_icon, creature_icon_scale, creature_hp, creature_armor, creature_atk_mod);
        creature.setPoints(points);
        creature_lib.put(creature_id, creature);
        Log.log("Loaded creature with ID '"+creature_id+"' and name '"+creature_name+"' to creature pack '"+pack_name+"'");
    }
    
    
    
    public static Creature getCreature (String name) {
        name = name.toLowerCase();
        if (!creature_lib.containsKey(name)) return null;
        return new Creature (creature_lib.get(name));
    }
    
    public static Creature getRandomCreature () {
        Creature[] creatures = new Creature [creature_lib.size()];
        creature_lib.values().toArray(creatures);
        return creatures[SlickUtils.randIndex(creature_lib.size())];
    }
    
    public static void saveCreatureToPack (Creature which_creature, String creature_pack_path) {
        try {
            if (creature_lib.containsKey(which_creature.getId())) {
                Log.err("Error while saving creature '"+which_creature.getName()+"' (id="+which_creature.getId()+"): a creature with that id already exists!");
                return;
            }
            
            Files.write(Paths.get(creature_pack_path), which_creature.toString().getBytes(), StandardOpenOption.APPEND);
            Log.log("Creature '"+which_creature.getName()+"' (id="+which_creature.getId()+") successfully saved to creature pack at '"+creature_pack_path+"'");
            creature_lib.put(which_creature.getId(), which_creature);
            Log.log("Creature '"+which_creature.getId()+"' added to creature library");
        } catch (IOException ex) {
            Log.err("Error while saving creature '"+which_creature.getName()+"' (id="+which_creature.getId()+"): error while appending to file!");
            Log.err(ex);
        }
    }
    
    
    
    public static String getCreaturePackFilePath (String pack_name) {
        return Settings.creature_pack_path + pack_name + Consts.CREATURE_PACK_FILE_EXTENSION;
    }
    
    public static boolean createNewPack (String pack_name) {
        String file_path = getCreaturePackFilePath(pack_name);
        
        File file = new File (file_path);
        if (file.exists() || loaded_creature_packs.contains(pack_name)) return false;
        
        try {
            boolean success = file.createNewFile();
            
            if (success) {
                loaded_creature_packs.add(pack_name);
                Log.log("Successfully created new creature pack '"+pack_name+"' at path '"+file_path+"'");
            }
            
            return success;
        } catch (IOException ex) {
            Log.err("Error while creating new creature pack");
            Log.err(ex);
            return false;
        }
    }
    
    public static String[] getLoadedCreaturePacks () {
        String[] result = new String [loaded_creature_packs.size()];
        loaded_creature_packs.toArray(result);
        return result;
    }
    
}
