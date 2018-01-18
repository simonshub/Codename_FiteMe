/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.data.gameplay.levels;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.simon.src.utils.Log;
import org.simon.src.utils.SlickUtils;

/**
 *
 * @author emil.simon
 */
public class LevelType {
    
    public static final String STARTING_LEVEL_TYPE = "forest";
    
    public static final String KEYWORD_SET_NAME = "name";
    public static final String KEYWORD_SET_BACKGROUND = "bkgr";
    public static final String KEYWORD_SET_ENCOUNTER_LIST = "elst";
    
    public static final String ENCOUNTER_DELIMITER = ";";
    public static final String ENCOUNTER_LISTING_DELIMITER = ",";
    
    public static final String PARSE_DELIMITER = ":";
    public static final String PARSE_COMMENT = "#";
    
    private String name;
    private String background;
    private List<Encounter> encounter_list;
    
    public LevelType (String file_path) {
        name = "";
        background = "";
        encounter_list = new ArrayList<> ();
        
        try {
            File file = new File (file_path);
            if (file.exists()) {
                String line;
                BufferedReader br = new BufferedReader (new FileReader (file));
                while ( (line = br.readLine())!=null ) {
                    if (line.trim().startsWith(LevelType.PARSE_COMMENT)) continue;
                    String[] args = line.split(LevelType.PARSE_DELIMITER);
                    if (args.length != 2) {
                        Log.err("Unable to parse line '"+line+"' while loading level type; wrong number of arguments");
                        continue;
                    }
                    String parse_action = args[0].trim();
                    String parse_value = args[1].trim();
                    switch (parse_action) {
                        case KEYWORD_SET_NAME :
                            this.name = parse_value;
                            break;
                        case KEYWORD_SET_BACKGROUND :
                            this.background = parse_value;
                            break;
                        case KEYWORD_SET_ENCOUNTER_LIST :
                            String[] encounters = parse_value.split(LevelType.ENCOUNTER_DELIMITER);
                            encounters = SlickUtils.Strings.trimAll(encounters);
                            for (int i=0;i<encounters.length;i++) {
                                encounter_list.add(new Encounter (encounters[i].trim()));
                            }
                            break;
                        default:
                            Log.err("Unknown parse action '"+parse_action+"' for level type '"+name+"'?");
                    }
                }
            }
        } catch (IOException ex) {
            Log.err("Error while loading player character class from file; '"+file_path+"'");
            Log.err(ex);
        }
    }
    
    public String getBackground () {
        return background;
    }
    
    public Wave makeWave () {
        return new Wave ((Encounter) SlickUtils.randListObject(encounter_list));
    }
    
}
