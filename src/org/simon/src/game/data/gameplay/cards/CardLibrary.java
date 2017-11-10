/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.data.gameplay.cards;

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
import org.newdawn.slick.Color;
import org.simon.src.game.data.gameplay.PointTypeEnum;
import org.simon.src.utils.Consts;
import org.simon.src.utils.Log;
import org.simon.src.utils.ResourceMgr;
import org.simon.src.utils.Settings;

/**
 *
 * @author XyRoN
 */
public class CardLibrary {
    
    public static final String PARSE_KEYWORD_SET_NEW_CARD = "card";
    public static final String PARSE_KEYWORD_SET_NAME = "name";
    public static final String PARSE_KEYWORD_SET_ICON = "icon";
    public static final String PARSE_KEYWORD_SET_POINTS = "cost";
    public static final String PARSE_KEYWORD_SET_TARGET_MODE = "targ";
    public static final String PARSE_KEYWORD_SET_ACTION = "func";
    public static final String PARSE_KEYWORD_SET_DESCRIPTION = "text";
    public static final String PARSE_KEYWORD_SET_COLOR = "colr";
    public static final String PARSE_KEYWORD_SET_SFX = "spef";
    
    public static final String PARSE_DELIMITER = ":";
    public static final String PARSE_COMMENT = "#";
    
    private static List<String> loaded_card_packs;
    private static Map<String, Card> card_lib;
    
    
    
    public static final void init () {
        loadCardPacks();
    }
    
    
    
    private static void loadCardPacks () {
        card_lib = new HashMap<> ();
        loaded_card_packs = new ArrayList<> ();
        File dump = new File (Settings.card_pack_path);
        
        if (!dump.exists()) {
            Log.err("Missing card pack resource dump folder at '"+Settings.card_pack_path+"'...");
        } else {
            File[] all_card_packs = ResourceMgr.getAllFilesOfExtensionInSubdirs(dump, Consts.CARD_PACK_FILE_EXTENSION);
            for (File card_pack : all_card_packs) {
                try {
                    String path = card_pack.getCanonicalPath().replace(System.getProperty("file.separator"), "/");
                    String pack_name = path.substring(path.indexOf(Settings.card_pack_path)+Settings.card_pack_path.length(), path.lastIndexOf("."));
                    
                    String contents = new Scanner(card_pack).useDelimiter("\\Z").next();
                    parseCardPackFile(pack_name, contents);
                    
                    loaded_card_packs.add(pack_name);
                    Log.log("Loaded card pack '"+pack_name+"' at path '"+path+"'");
                } catch (IOException ex) {
                    Log.err(ex);
                }
            }
        }
    }
    
    private static void parseCardPackFile (String pack_name, String contents) {
        String[] lines = contents.split("\n");
        
        String current_card_id="";
        String current_card_name="";
        String current_card_icon="";
        String current_card_target_mode = "";
        String current_card_action="";
        String current_card_description="";
        String current_card_sfx="";
        Color current_card_color = Color.white;
        HashMap<PointTypeEnum, Integer> current_card_points = new HashMap<> ();
        
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
                Log.err("Found "+keywords.size()+" parameters while reading card, instead of 2");
                Log.err("for line '"+line+"'");
                continue;
            }
            
            // determine parse action for this line
            String parse_action = keywords.size()==2 ? keywords.get(0) : "";
            String parse_value = keywords.size()==2 ? keywords.get(1) : "";
            
            // if all card fields have been set, save the card automatically
            if (!current_card_id.isEmpty() && !current_card_name.isEmpty() && !current_card_icon.isEmpty() && !current_card_points.isEmpty()
                    && !current_card_action.isEmpty() && !current_card_description.isEmpty()) {
                if (!current_card_id.isEmpty() &&
                    ( current_card_name.isEmpty() || current_card_target_mode.isEmpty() || current_card_action.isEmpty() || current_card_icon.isEmpty() || current_card_description.isEmpty() || current_card_points.isEmpty() ))
                    Log.err("Auto saving card '"+current_card_id+"' but still some properties missing?; missing properties - " +
                        (current_card_name.isEmpty() ? "name " : "") +
                        (current_card_target_mode.isEmpty() ? "targ " : "") +
                        (current_card_action.isEmpty() ? "func " : "") +
                        (current_card_icon.isEmpty() ? "icon " : "") +
                        (current_card_description.isEmpty() ? "text " : "") +
                        (current_card_sfx.isEmpty() ? "spef " : "") +
                        (current_card_points.isEmpty() ? "cost " : "") );
                
                // if all card fields are set for this card, finish loading it
                saveCard(current_card_id, pack_name, current_card_name, current_card_icon, current_card_target_mode, current_card_action, current_card_description, current_card_sfx, current_card_color, current_card_points);
                current_card_id = "";
                current_card_name = "";
                current_card_icon = "";
                current_card_target_mode = "";
                current_card_action = "";
                current_card_description = "";
                current_card_sfx = "";
                current_card_color = Color.white;
                current_card_points = new HashMap<> ();
            }
            
            switch (parse_action) {
                case PARSE_KEYWORD_SET_NEW_CARD :
                    if (!current_card_id.isEmpty() &&
                        ( current_card_name.isEmpty() || current_card_target_mode.isEmpty() || current_card_action.isEmpty() || current_card_icon.isEmpty() || current_card_description.isEmpty() || current_card_points.isEmpty() ))
                        Log.err("Not finished parsing card '"+current_card_id+"', but new card '"+keywords.get(1)+"' already started; missing properties - " +
                            (current_card_name.isEmpty() ? "name " : "") +
                            (current_card_target_mode.isEmpty() ? "targ " : "") +
                            (current_card_action.isEmpty() ? "func " : "") +
                            (current_card_icon.isEmpty() ? "icon " : "") +
                            (current_card_description.isEmpty() ? "text " : "") +
                            (current_card_sfx.isEmpty() ? "spef " : "") +
                            (current_card_points.isEmpty() ? "cost " : "") );
                    
                    if (!current_card_id.isEmpty()) {
                        saveCard(current_card_id, pack_name, current_card_name, current_card_icon, current_card_target_mode, current_card_action, current_card_description, current_card_sfx, current_card_color, current_card_points);
                    }
                    
                    current_card_id = parse_value;
                    current_card_name = "";
                    current_card_icon = "";
                    current_card_target_mode = "";
                    current_card_action = "";
                    current_card_description = "";
                    current_card_sfx = "";
                    current_card_color = Color.white;
                    current_card_points = new HashMap<> ();
                    break;
                case PARSE_KEYWORD_SET_NAME :
                    current_card_name = parse_value;
                    break;
                case PARSE_KEYWORD_SET_ICON :
                    current_card_icon = parse_value;
                    break;
                case PARSE_KEYWORD_SET_DESCRIPTION :
                    current_card_description = parse_value;
                    break;
                case PARSE_KEYWORD_SET_TARGET_MODE :
                    current_card_target_mode = parse_value;
                    break;
                case PARSE_KEYWORD_SET_ACTION :
                    current_card_action = parse_value;
                    break;
                case PARSE_KEYWORD_SET_SFX :
                    current_card_sfx = parse_value;
                    break;
                case PARSE_KEYWORD_SET_COLOR :
                    if (parse_value.isEmpty())
                        current_card_color = new Color (1f,1f,1f,1f);
                    
                    LinkedList<String> color_tokens = new LinkedList<> (Arrays.asList(parse_value.split(" ")));
                    for (int i=0;i<color_tokens.size();i++) {
                        if (color_tokens.get(i).trim().isEmpty())
                            color_tokens.remove(i);
                        else
                            color_tokens.set(i, color_tokens.get(i).trim().replace(",","."));
                    }
                    
                    if (color_tokens.size() != 4) {
                        Log.err("Wrong number of color arguments, expected 4 but found "+color_tokens.size());
                        break;
                    }
                    
                    float r,g,b,a;
                    try {
                        r = Float.parseFloat(color_tokens.get(0));
                        g = Float.parseFloat(color_tokens.get(1));
                        b = Float.parseFloat(color_tokens.get(2));
                        a = Float.parseFloat(color_tokens.get(3));
                        
                        current_card_color = new Color (r,g,b,a);
                    } catch (NumberFormatException ex) {
                        Log.err("Unparsable number while reading color of card ...");
                        Log.err(ex);
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

                                current_card_points.put(which_type, amount);

                                which_type_str = "";
                                which_type = PointTypeEnum.values()[0];
                            }
                        } catch (NumberFormatException ex) {
                            Log.err("Unreadable token '"+point_tokens.get(i)+"'!");
                        }
                    }
                    break;
                default:
                    break;
            }
        }
            
        // if the last card has not been saved, save it
        if (!current_card_id.isEmpty() && !card_lib.containsKey(current_card_id)) {
            if (!current_card_id.isEmpty() &&
                ( current_card_name.isEmpty() || current_card_target_mode.isEmpty() || current_card_action.isEmpty() || current_card_icon.isEmpty() || current_card_description.isEmpty() || current_card_points.isEmpty() ))
                Log.err("Not finished parsing card '"+current_card_id+"', but end of file reached; missing properties - " +
                    (current_card_name.isEmpty() ? "name " : "") +
                    (current_card_target_mode.isEmpty() ? "targ " : "") +
                    (current_card_action.isEmpty() ? "func " : "") +
                    (current_card_icon.isEmpty() ? "icon " : "") +
                    (current_card_description.isEmpty() ? "text " : "") +
                    (current_card_sfx.isEmpty() ? "spef " : "") +
                    (current_card_points.isEmpty() ? "cost " : "") );
            
            saveCard(current_card_id, pack_name, current_card_name, current_card_icon, current_card_target_mode, current_card_action, current_card_description, current_card_sfx, current_card_color, current_card_points);
        }
    }
    
    public static void saveCard (String card_id, String pack_name, String card_name, String card_icon, String card_target_mode, String card_action, String card_description, String sfx,
            Color card_color, HashMap<PointTypeEnum,Integer> point_cost) {
        Card card = new Card (card_id, pack_name, card_name, card_icon, card_target_mode, card_action, card_description, sfx);
        card.setIconBackground(card_color);
        card.setPointCostMap(point_cost);
        card_lib.put(card_id, card);
        Log.log("Loaded card with ID '"+card_id+"' and name '"+card_name+"' to card pack '"+pack_name+"'");
    }
    
    
    
    public static Card getCard (String name) {
        name = name.toLowerCase();
        if (!card_lib.containsKey(name)) return null;
        return card_lib.get(name);
    }
    
    public static void saveCardToPack (Card which_card, String card_pack_path) {
        try {
            if (card_lib.containsKey(which_card.getId())) {
                Log.err("Error while saving card '"+which_card.getName()+"' (id="+which_card.getId()+"): a card with that id already exists!");
                return;
            }
            
            Files.write(Paths.get(card_pack_path), which_card.toString().getBytes(), StandardOpenOption.APPEND);
            Log.log("Card '"+which_card.getName()+"' (id="+which_card.getId()+") successfully saved to card pack at '"+card_pack_path+"'");
            card_lib.put(which_card.getId(), which_card);
            Log.log("Card '"+which_card.getId()+"' added to card library");
        } catch (IOException ex) {
            Log.err("Error while saving card '"+which_card.getName()+"' (id="+which_card.getId()+"): error while appending to file!");
            Log.err(ex);
        }
    }
    
    public static List<Card> getAllCards () {
        List<Card> card_list = new ArrayList<> (card_lib.values());
        return card_list;
    }
    
    
    
    public static String getCardPackFilePath (String pack_name) {
        return Settings.card_pack_path + pack_name + Consts.CARD_PACK_FILE_EXTENSION;
    }
    
    public static boolean createNewPack (String pack_name) {
        String file_path = getCardPackFilePath(pack_name);
        
        File file = new File (file_path);
        if (file.exists() || loaded_card_packs.contains(pack_name)) return false;
        
        try {
            boolean success = file.createNewFile();
            
            if (success) {
                loaded_card_packs.add(pack_name);
                Log.log("Successfully created new card pack '"+pack_name+"' at path '"+file_path+"'");
            }
            
            return success;
        } catch (IOException ex) {
            Log.err("Error while creating new card pack");
            Log.err(ex);
            return false;
        }
    }
    
    public static String[] getLoadedCardPacks () {
        String[] result = new String [loaded_card_packs.size()];
        loaded_card_packs.toArray(result);
        return result;
    }
    
}
