/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.data.gameplay;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.newdawn.slick.Color;
import org.simon.src.game.data.gameplay.cards.Card;
import org.simon.src.game.data.gameplay.creatures.Creature;
import org.simon.src.game.data.gameplay.levels.LevelType;
import org.simon.src.game.data.gameplay.levels.Wave;
import org.simon.src.game.data.gameplay.player.Player;
import org.simon.src.game.data.gameplay.player.PlayerCharacterClass;
import org.simon.src.game.gui.GuiController;
import org.simon.src.game.gui.GuiElement;
import org.simon.src.game.sfx.SpecialEffectSystem;
import org.simon.src.game.states.combat.CombatState;
import org.simon.src.utils.Consts;
import org.simon.src.utils.CycleList;
import org.simon.src.utils.Log;
import org.simon.src.utils.Settings;
import org.simon.src.utils.SlickUtils;
import org.simon.src.utils.WeightedRandom;

/**
 *
 * @author XyRoN
 */
public class GameplayManager {
    
    public enum Opponent {
        PLAYER ("Player"), ENEMY ("Opponent")
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
    
    
    
    public static final String WAVE_CLEARED_TEXT = "WAVE CLEARED";
    public static final Color WAVE_CLEARED_COLOR = Color.green;
    
    public static final String NEW_WAVE_TEXT = "NEW WAVE";
    public static final Color NEW_WAVE_COLOR = Color.red;
    
    
    
    private static Opponent current_opponent;
    private static Creature current_casting_creature;
    
    private static LevelType level_type;
    
    private static List<Creature> enemy_board;
    private static List<Creature> ally_board;
    
    private static List<AiAction> ai_action_queue;
    
    private static Map<String,PlayerCharacterClass> loaded_character_classes;
    private static Map<String,LevelType> loaded_level_types;
    
    
    
    public static final void init () {
        loadPlayerCharacterClasses();
        loadLevelTypes();
        Player.init();
        ai_action_queue = new ArrayList<> ();
        level_type = loaded_level_types.get(LevelType.STARTING_LEVEL_TYPE);
    }
    
    
    
    public static void checkWaveSpawn () {
        boolean all_dead = true;
        for (Creature enemy : enemy_board) {
            if (!enemy.isDead()) {
                all_dead = false;
                break;
            }
        }
        if (all_dead) {
            if (!enemy_board.isEmpty())
                CombatState.gui.addFloatingText(NEW_WAVE_TEXT, NEW_WAVE_COLOR, Settings.screen_width/2f, Settings.screen_height*0.3f);
            
            spawnWave();
        }
    }
    
    public static void spawnWave () {
        Wave wave = level_type.makeWave();
        GuiController gui = CombatState.gui;
        
        List<GuiElement> enemy_elements = gui.getElements("_enemy_");
        List<Creature> creatures = wave.getWaveCreatures();
        enemy_board.clear();
        enemy_board.addAll(creatures);
        
        for (;creatures.size()<enemy_elements.size();) creatures.add(null);
        creatures = (List<Creature>) SlickUtils.shuffleList(creatures);
        
        for (int i=0;i<enemy_elements.size();i++) {
            GuiElement enemy_el = enemy_elements.get(i);
            enemy_el.setCreature(creatures.get(i));
            if (creatures.get(i) != null) {
                enemy_el.instantCall("fadein");
            }
        }
    }
    
    
    
    public static void addEnemy (final Creature creature) {
        enemy_board.add(creature);
    }
    
    public static void addAlly (final Creature creature) {
        ally_board.add(creature);
    }
    
    public static void addEnemy (final GuiElement element) {
        if (element.getCreature() != null) enemy_board.add(element.getCreature());
    }
    
    public static void addAlly (final GuiElement element) {
        if (element.getCreature() != null) ally_board.add(element.getCreature());
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
    
    public static LevelType getCurrentLevelType () {
        return level_type;
    }
    
    public static boolean isCreatureEnemy (final Creature c) {
        return !ally_board.contains(c);
    }
    
    public static boolean allEnemiesDead () {
        for (Creature enemy : enemy_board) {
            if (!enemy.isDead()) return false;
        }
        return true;
    }
    
    public static Creature getCurrentCastingCreature () {
        return current_casting_creature;
    }
    
    public static void setCurrentCastingCreature (final Creature current_casting_creature) {
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
    
    public static void clearEnemies () {
        enemy_board.clear();
    }
    
    public static void clearAllies () {
        ally_board.clear();
    }
    
    public static void turnTick (List<Creature> creatures, SpecialEffectSystem sfx) {
        for (Creature creature : creatures) {
            creature.turnTick(sfx);
        }
    }
    
    
    
    public static void aiUpdate (SpecialEffectSystem sfx) {
        if (Opponent.PLAYER.equals(current_opponent) || ai_action_queue.isEmpty()) return;
        
        ai_action_queue.get(0).update(sfx);
        if (ai_action_queue.get(0).isFinished()) ai_action_queue.remove(0);
        
        if (ai_action_queue.isEmpty()) CombatState.startTurn();
    }
    
    
    
    public static void loadPlayerCharacterClasses () {
        // for the first turn we want the player to play.
        // we set the current_opponent to 'PLAYER' because on entry, the combat state triggers a turn change.
        current_opponent = Opponent.PLAYER;
        enemy_board = new ArrayList<> ();
        ally_board = new ArrayList<> ();
        loaded_character_classes = new HashMap<> ();
        File dump = new File (Settings.character_class_path);
        
        if (!dump.exists()) {
            Log.err("Missing player character class dump folder at '"+Settings.character_class_path+"'...");
        } else {
            File[] all_character_class_files = SlickUtils.Files.getFileArrayOfExtensionInSubdirs(dump, Consts.CHARACTER_CLASS_FILE_EXTENSION);
            for (File character_class_file : all_character_class_files) {
                try {
                    String path = character_class_file.getCanonicalPath().replace(System.getProperty("file.separator"), "/");
                    String class_name = SlickUtils.Files.getFileName(path);
                    
                    PlayerCharacterClass char_class = new PlayerCharacterClass(character_class_file);
                    loaded_character_classes.put(class_name, char_class);
                    Log.log("Loaded player character class '"+class_name+"' at path '"+path+"'");
                } catch (IOException ex) {
                    Log.err(ex);
                }
            }
        }
    }
    
    public static void loadLevelTypes () {
        loaded_level_types = new HashMap<> ();
        File dump = new File (Settings.level_type_path);
        
        if (!dump.exists()) {
            Log.err("Missing level type dump folder at '"+Settings.level_type_path+"'...");
        } else {
            File[] all_level_type_files = SlickUtils.Files.getFileArrayOfExtensionInSubdirs(dump, Consts.LEVEL_TYPE_FILE_EXTENSION);
            for (File level_type_file : all_level_type_files) {
                try {
                    String path = level_type_file.getCanonicalPath().replace(System.getProperty("file.separator"), "/");
                    String key = SlickUtils.Files.getFileNameLC(path);
                    LevelType value = new LevelType (path);
                    
                    Log.log("Loaded level type with name '"+key+"' at path '"+path+"'");
                    
                    loaded_level_types.put(key, value);
                } catch (IOException ex) {
                    Log.err(ex);
                }
            }
        }
    }
    
    
    
    public static void turnTick (SpecialEffectSystem sfx) {
        if (Opponent.PLAYER.equals(current_opponent)) {
            // ending the player's turn
            turnTick(ally_board, sfx);
            aiTurn(sfx);
        } else {
            // ending the ai's turn
            turnTick(enemy_board, sfx);
        }
        current_opponent = current_opponent.opposite();
    }
    
    public static void aiTurn (SpecialEffectSystem sfx) {
        ai_action_queue.clear();
        
        for (int i=0;i<5;i++) {
            List<Creature> viable_casters = new ArrayList<> ();
            for (Creature enemy : enemy_board) {
                if (!enemy.getCastableCards().isEmpty()) viable_casters.add(enemy);
            }
            if (viable_casters.isEmpty()) return;
            Creature selected_creature = (Creature) SlickUtils.randListObject(viable_casters);
            List<Card> viable_cards = selected_creature.getCastableCards();
            Card selected_card = (Card) SlickUtils.randListObject(viable_cards);
            List<Creature> selected_targets = aiResolveTargets(selected_creature, selected_card);
            ai_action_queue.add(new AiAction (selected_card, selected_creature, selected_targets, sfx));
        }
    }
    
    public static List<Creature> aiResolveTargets (Creature source, Card card) {
        TargetEnum target_mode = card.getTargetMode();
        
        // resolve trivial target modes
        if (TargetEnum.ALL_ALLIES.equals(target_mode) || TargetEnum.ALL_ENEMIES.equals(target_mode) || TargetEnum.ALL.equals(target_mode) || TargetEnum.SELF.equals(target_mode)) {
            return TargetEnum.getTargetList(target_mode, source, null, ally_board, enemy_board);
        }
        
        Creature target = null;
        WeightedRandom<Creature> rand = new WeightedRandom<> ();
        // resolve single target
        if (TargetEnum.SINGLE_ALLY.equals(target_mode)) {
            for (Creature ally : enemy_board) {
                if (source.equals(ally)) continue;
                
                int weight = (int) ( ( ally.getMaxHealth() - ally.getCurrentHealth() ) + ally.getTotalPoints() + ally.getDifficulty() );
                rand.add(ally, weight);
            }
            target = rand.getRandom();
        } else if (TargetEnum.SINGLE_ENEMY.equals(target_mode)) {
            for (Creature enemy : ally_board) {
                int weight = enemy.getMaxHealth() + enemy.getCurrentHealth() + enemy.getTotalPoints() + enemy.getArmor() + enemy.getAttackModifier();
                rand.add(enemy, weight);
            }
            target = rand.getRandom();
        }
        
        return Arrays.asList(target);
    }
    
}
