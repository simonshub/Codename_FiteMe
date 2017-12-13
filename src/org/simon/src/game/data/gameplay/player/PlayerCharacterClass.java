/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.data.gameplay.player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.simon.src.game.data.gameplay.PointTypeEnum;
import org.simon.src.game.data.gameplay.cards.Card;
import org.simon.src.game.data.gameplay.cards.CardLibrary;
import org.simon.src.utils.CircularList;
import org.simon.src.utils.Log;

/**
 *
 * @author emil.simon
 */
public class PlayerCharacterClass {
    
    public static final String KEYWORD_NAME = "name";
    public static final String KEYWORD_GRFX = "grfx";
    public static final String KEYWORD_PNTS = "pnts";
    public static final String KEYWORD_CLST = "clst";
    
    private String name;
    private String graphics;
    private final List<Card> card_list;
    private final List<PointTypeEnum> point_leveling_list;
    
    
    
    public PlayerCharacterClass (String file_path) {
        String read_name = "";
        String read_grfx = "";
        this.card_list = new ArrayList<> ();
        this.point_leveling_list = new CircularList<> ();
        
        try {
            File file = new File (file_path);
            if (file.exists()) {
                String line;
                BufferedReader br = new BufferedReader (new FileReader (file));
                while ( (line = br.readLine())!=null ) {
                    if (line.trim().startsWith(CardLibrary.PARSE_COMMENT)) continue;
                    String[] args = line.split(CardLibrary.PARSE_DELIMITER);
                    if (args.length != 2) {
                        Log.err("Unable to parse line '"+line+"' while loading player character class; wrong number of arguments");
                        continue;
                    }
                    String parse_action = args[0].trim();
                    String parse_value = args[1].trim();
                    switch (parse_action) {
                        case KEYWORD_NAME :
                            this.name = parse_value;
                            break;
                        case KEYWORD_GRFX :
                            this.graphics = parse_value;
                            break;
                        case KEYWORD_PNTS :
                            String[] points = parse_value.split(" ");
                            for (String point : points) {
                                PointTypeEnum type = PointTypeEnum.getByCode(point.trim());
                                point_leveling_list.add(type);
                            }
                            break;
                        case KEYWORD_CLST :
                            String[] cards = parse_value.split(" ");
                            for (String card_name : cards) {
                                Card card = CardLibrary.getCard(card_name);
                                card_list.add(card);
                            }
                            break;
                    }
                }
            }
        } catch (IOException ex) {
            Log.err("Error while loading player character class from file; '"+file_path+"'");
            Log.err(ex);
        }
        
        this.name = read_name;
        this.graphics = read_grfx;
    }
    
    
    
    public Map<PointTypeEnum, Integer> getPointPoolForLevel (int level) {
        int index = 0;
        
        Map<PointTypeEnum, Integer> point_pool = new HashMap<> ();
        for (int i=0;i<PointTypeEnum.values().length;i++) {
            PointTypeEnum point_type = PointTypeEnum.values()[i];
            point_pool.put(point_type, 0);
        }
        
        for (index=0;index<PlayerCharacter.STARTING_POINTS;index++) {
            PointTypeEnum type = point_leveling_list.get(index);
            int value = point_pool.get(type);
            point_pool.put(type, value+1);
        }
        
        for (int i=0;i<(level-1);i++) {
            for (int j=0;j<PlayerCharacter.POINTS_PER_LEVEL;j++) {
                PointTypeEnum type = point_leveling_list.get(index);
                int value = point_pool.get(type);
                point_pool.put(type, value+1);
                index++;
            }
        }
        
        return point_pool;
    }
    
    public int getHealthForLevel (int level) {
        return 10;
    }
    
    public String getName () {
        return name;
    }
    
    public String getGraphics () {
        return graphics;
    }
    
}
