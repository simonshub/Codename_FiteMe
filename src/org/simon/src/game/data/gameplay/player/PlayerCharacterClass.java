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
import org.simon.src.game.data.gameplay.PointTypeEnum;
import org.simon.src.game.data.gameplay.cards.Card;
import org.simon.src.game.data.gameplay.cards.CardLibrary;
import org.simon.src.game.data.gameplay.creatures.Creature;
import org.simon.src.utils.CycleList;
import org.simon.src.utils.Log;
import org.simon.src.utils.ResourceManager;

/**
 *
 * @author emil.simon
 */
public class PlayerCharacterClass {
    
    public static final String KEYWORD_NAME = "name";
    public static final String KEYWORD_GRFX = "grfx";
    public static final String KEYWORD_PORT = "port";
    public static final String KEYWORD_PNTS = "pnts";
    public static final String KEYWORD_CLST = "clst";
    public static final String KEYWORD_HPBS = "hpbs";
    public static final String KEYWORD_HPPL = "hppl";
    
    private String name;
    private String graphics;
    private String portrait;
    private final List<Card> card_list;
    private final List<PointTypeEnum> point_leveling_list;
    
    private int base_hp;
    private int hp_per_level;
    
    
    
    public PlayerCharacterClass (File file) {
        this(file.getAbsolutePath());
    }
    
    public PlayerCharacterClass (String file_path) {
        this.card_list = new ArrayList<> ();
        this.point_leveling_list = new CycleList<> ();
        
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
                            if (ResourceManager.hasGraphics(parse_value)) this.graphics = parse_value;
                            break;
                        case KEYWORD_PORT :
                            this.portrait = parse_value;
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
                        case KEYWORD_HPBS :
                            this.base_hp = Integer.parseInt(parse_value);
                            break;
                        case KEYWORD_HPPL :
                            this.hp_per_level = Integer.parseInt(parse_value);
                            break;
                        default:
                            Log.err("Unknown parse action '"+parse_action+"' for character class '"+name+"'?");
                    }
                }
            }
        } catch (IOException ex) {
            Log.err("Error while loading player character class from file; '"+file_path+"'");
            Log.err(ex);
        }
    }
    
    
    
    public HashMap<PointTypeEnum, Integer> getPointPoolForLevel (int level) {
        int index;
        
        HashMap<PointTypeEnum, Integer> point_pool = new HashMap<> ();
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
        return base_hp + ( (level-1)*hp_per_level );
    }
    
    public String getName () {
        return name;
    }
    
    public String getGraphics () {
        return graphics;
    }
    
    public String getPortrait () {
        return portrait;
    }
    
    public Creature getCreatureParent (int level) {
        Creature c = new Creature ();
        
        c.setIcon(getGraphics());
        c.setId(PlayerCharacter.PLAYER_CHARACTER_CREATURE_ID_PREFIX + getName().toLowerCase().replace(" ", "_") + ("_"+level));
        c.setPoints(getPointPoolForLevel(level));
        c.setName(getName());
        c.setHealth(getHealthForLevel(level));
        c.setParent(null);
        c.setIsPlayerCharacter(true);
        
        return c;
    }
    
    public List<Card> getCardList () {
        return card_list;
    }
    
    public List<Card> getCardListForLevel (int level) {
        List<Card> leveled_list = new ArrayList<> ();
        card_list.stream().filter((c) -> (c.getUnlockLevel() <= level)).forEachOrdered((c) -> {
            leveled_list.add(c);
        });
        return leveled_list;
    }
    
}
