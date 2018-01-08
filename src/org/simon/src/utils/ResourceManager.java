package org.simon.src.utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.TrueTypeFont;
import org.simon.src.game.data.gameplay.GameplayManager;
import org.simon.src.game.data.gameplay.cards.CardLibrary;
import org.simon.src.game.data.gameplay.creatures.CreatureLibrary;
import org.simon.src.game.sfx.ParticleDefinition;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author emil.simon
 */
public class ResourceManager {
    
    private static final String SFX_PARSE_KEYWORD_KEY = "key";
    private static final String SFX_PARSE_KEYWORD_XML = "xml";
    private static final String SFX_PARSE_KEYWORD_IMG = "img";
    
    private static Map<String, Image> graphics_lib;
    private static Map<String, Sound> sound_lib;
    private static Map<String, Font> font_lib;
    
    private static Map<String, ParticleDefinition> particle_lib;
    
    private static Map<String, TrueTypeFont> preloaded_fonts;
    
    
    
    public static void init () {
        long start,end;
        long start_entire = System.currentTimeMillis();
        Log.log("Initializing resources ...");
        
        Log.log("Reading settings...");
        start = System.currentTimeMillis();
        loadSettings();
        end = System.currentTimeMillis();
        Log.log("Settings read in "+String.format("%.2f", (end-start)/1000f)+" sec");
        
        Log.log("Loading graphics...");
        start = System.currentTimeMillis();
        loadGraphics();
        end = System.currentTimeMillis();
        Log.log("Graphics loaded in "+String.format("%.2f", (end-start)/1000f)+" sec");
        
        
        Log.log("Loading sounds...");
        start = System.currentTimeMillis();
        loadSounds();
        end = System.currentTimeMillis();
        Log.log("Sounds loaded in "+String.format("%.2f", (end-start)/1000f)+" sec");
        
        Log.log("Loading fonts...");
        start = System.currentTimeMillis();
        loadFonts();
        end = System.currentTimeMillis();
        Log.log("Fonts loaded in "+String.format("%.2f", (end-start)/1000f)+" sec");
        
        Log.log("Loading particles...");
        start = System.currentTimeMillis();
        loadParticles();
        end = System.currentTimeMillis();
        Log.log("Particles loaded in "+String.format("%.2f", (end-start)/1000f)+" sec");
        
        Log.log("Loading cards...");
        start = System.currentTimeMillis();
        CardLibrary.init();
        end = System.currentTimeMillis();
        Log.log("Cards loaded in "+String.format("%.2f", (end-start)/1000f)+" sec");
        
        Log.log("Loading creatures...");
        start = System.currentTimeMillis();
        CreatureLibrary.init();
        end = System.currentTimeMillis();
        Log.log("Creatures loaded in "+String.format("%.2f", (end-start)/1000f)+" sec");
        
        Log.log("Loading player character classes...");
        start = System.currentTimeMillis();
        GameplayManager.init();
        end = System.currentTimeMillis();
        Log.log("Player character classes loaded in "+String.format("%.2f", (end-start)/1000f)+" sec");
        
        long end_entire = System.currentTimeMillis();
        Log.log("Finished loading in "+String.format("%.2f",(end_entire-start_entire)/1000f)+" sec");
    }
    
    private static void loadSettings () {
        File file = new File (Consts.APP_SETTINGS_FILE_PATH);
        
        if (file.exists()) {
            try {
                BufferedReader br = new BufferedReader (new FileReader (file));
                String line;
                
                while ((line=br.readLine()) != null) {
                    if (line.trim().isEmpty() || line.startsWith(Consts.APP_COMMENT_DELIMITER)) continue;
                    
                    String[] args = line.split(Consts.APP_PROPERTY_DELIMITER);
                    
                    if (args.length == 2) {
                        String name=args[0].trim();
                        String value=args[1].trim();
                        Settings.setProperty(name,value);
                    } else {
                        Log.err("Too many, or too few arguments found in line; \""+line+"\", number of arguments; "+args.length+", while parsing settings file!");
                    }
                }
                
                br.close();
            } catch (FileNotFoundException ex) {
                Log.err(ex);
            } catch (IOException ex) {
                Log.err(ex);
            }
        } else {
            try {
                BufferedWriter bw = new BufferedWriter (new FileWriter (file));
                bw.write(Settings.getFileContents());
                bw.flush();
                bw.close();
            } catch (IOException ex) {
                Log.err("Error while trying to create default settings file!");
                Log.err(ex);
            }
        }
    }
    
    private static void loadGraphics () {
        graphics_lib = new HashMap<> ();
        File dump = new File (Settings.grfx_path);
        
        if (!dump.exists()) {
            Log.err("Missing graphics resource dump folder at '"+Settings.grfx_path+"'...");
        } else {
            File[] all_images = SlickUtils.files.getFileArrayOfExtensionInSubdirs(dump, Consts.GRFX_FILE_EXTENSION);
            for (File img : all_images) {
                try {
                    String path = img.getCanonicalPath().replace(System.getProperty("file.separator"), "/");
                    String key = path.toLowerCase()
                            .substring(path.indexOf(Settings.grfx_path) + Settings.grfx_path.length())
                            .replaceAll(Consts.GRFX_FILE_EXTENSION, "");
                    
                    Image value = new Image (path, false, Image.FILTER_LINEAR);
                    value.setFilter(Image.FILTER_NEAREST);
                    graphics_lib.put(key, value);
                    
                    Log.log("Loaded graphics with name '"+key+"' at path '"+path+"'");
                } catch (SlickException | IOException ex) {
                    Log.err(ex);
                }
            }
        }
    }
    
    private static void loadSounds () {
        sound_lib = new HashMap<> ();
        File dump = new File (Settings.sounds_path);
        
        if (!dump.exists()) {
            Log.err("Missing sound resource dump folder at '"+Settings.sounds_path+"'...");
        } else {
            File[] all_sounds = SlickUtils.files.getFileArrayOfExtensionInSubdirs(dump, Consts.SOUND_FILE_EXTENSION);
            for (File snd : all_sounds) {
                try {
                    String path = snd.getCanonicalPath().replace(System.getProperty("file.separator"), "/");
                    String key = path.toLowerCase()
                            .substring(path.indexOf(Settings.sounds_path) + Settings.sounds_path.length())
                            .replaceAll(Consts.SOUND_FILE_EXTENSION, "");
                    Sound value = new Sound (path);
                    
                    Log.log("Loaded sound with name '"+key+"' at path '"+path+"'");
                    
                    sound_lib.put(key, value);
                } catch (SlickException | IOException ex) {
                    Log.err(ex);
                }
            }
        }
    }
    
    private static void loadFonts () {
        font_lib = new HashMap<> ();
        preloaded_fonts = new HashMap<> ();
        File dump = new File (Settings.fonts_path);
        
        if (!dump.exists()) {
            Log.err("Missing font resource dump folder at '"+Settings.fonts_path+"'...");
        } else {
            File[] all_fonts = SlickUtils.files.getFileArrayOfExtensionInSubdirs(dump, Consts.FONT_FILE_EXTENSION);
            for (File font : all_fonts) {
                try {
                    String path = font.getCanonicalPath().replace(System.getProperty("file.separator"), "/");
                    String key = path.substring(path.indexOf(Settings.fonts_path) + Settings.fonts_path.length());
                    key = key.substring(key.indexOf("/")+1);
                    key = key.toLowerCase().replaceAll(Consts.FONT_FILE_EXTENSION, "");
                    Font value = Font.createFont(Font.TRUETYPE_FONT, new File(path));
                    
                    Log.log("Loaded font with name '"+key+"' at path '"+path+"'");
                    
                    font_lib.put(key, value);
                } catch (IOException | FontFormatException ex) {
                    Log.err(ex);
                }
            }
        }
    }
    
    private static void loadParticles () {
        particle_lib = new HashMap<> ();
        File dump = new File (Settings.sfx_path);
        
        if (!dump.exists()) {
            Log.err("Missing particle resource dump folder at '"+Settings.sfx_path+"'...");
        } else {
            File[] all_sfx_files = SlickUtils.files.getFileArrayOfExtensionInSubdirs(dump, Consts.SFX_FILE_EXTENSION);
            for (File sfx_file : all_sfx_files) {
                try {
                    String path = sfx_file.getCanonicalPath().replace(System.getProperty("file.separator"), "/");
                    List<String> args = parseSfxFile(sfx_file);
                    String key = args.get(0);
                    String xml = Settings.sfx_path + args.get(1) + Consts.PARTICLE_FILE_EXTENSION;
                    String img = args.get(2);
                    ParticleDefinition value = new ParticleDefinition (key,xml,img);
                    
                    Log.log("Loaded particle effect with name '"+key+"' at path '"+path+"'");
                    
                    particle_lib.put(key, value);
                } catch (IOException ex) {
                    Log.err(ex);
                }
            }
        }
    }
    
    
    
    public static Image getGraphics (String name) {
        name = name.toLowerCase();
        if (!graphics_lib.containsKey(name)) {
            Log.err("no graphics of name '"+name+"'");
            return null;
        }
        return graphics_lib.get(name);
    }
    
    public static Sound getSound (String name) {
        name = name.toLowerCase();
        if (!sound_lib.containsKey(name)) {
            Log.err("no sound of name '"+name+"'");
            return null;
        }
        return sound_lib.get(name);
    }
    
    public static TrueTypeFont getFont (String name, int size) {
        name = name.toLowerCase();
        String preload_key = name + "_" + String.valueOf(size);
        
        if (preloaded_fonts.containsKey(preload_key))
            return preloaded_fonts.get(preload_key);
        
        TrueTypeFont result;
        if (!font_lib.containsKey(name)) {
            try {
                Font font = new Font (name, Font.PLAIN, size);
                font_lib.put(name, font);
                result = new TrueTypeFont (font, true);
            } catch (RuntimeException ex) {
                Log.err(ex);
                return null;
            }
        } else {
            Font resized_font = font_lib.get(name).deriveFont((float) size);
            result = new TrueTypeFont (resized_font, true);
        }
        
        preloaded_fonts.put(preload_key, result);
        return result;
    }
    
    public static ParticleDefinition getParticle (String name) {
        name = name.toLowerCase();
        if (!particle_lib.containsKey(name)) {
            Log.err("no effect of name '"+name+"'");
            return null;
        }
        return particle_lib.get(name);
    }
    
    public static boolean hasGraphics (String name) {
        if (name==null || name.isEmpty()) return false;
        return graphics_lib.containsKey(name);
    }
    
    public static boolean hasSound (String name) {
        if (name==null || name.isEmpty()) return false;
        return sound_lib.containsKey(name);
    }
    
    public static boolean hasFont (String name) {
        if (name==null || name.isEmpty()) return false;
        return font_lib.containsKey(name);
    }
    
    public static boolean hasParticle (String name) {
        if (name==null || name.isEmpty()) return false;
        return particle_lib.containsKey(name);
    }
    
    public static Set<String> getGraphicsKeySet () {
        return graphics_lib.keySet();
    }
    
    public static Set<String> getSoundKeySet () {
        return sound_lib.keySet();
    }
    
    public static Set<String> getFontKeySet () {
        return font_lib.keySet();
    }
    
    public static Set<String> getParticleKeySet () {
        return particle_lib.keySet();
    }
    
    
    
    public static List<String> getAllGraphicsStartingWith (String starts_with) {
        String[] all = new String [graphics_lib.size()];
        graphics_lib.keySet().toArray(all);
        List<String> result = new ArrayList<> ();
        
        for (int i=0;i<all.length;i++) {
            String key = all[i];
            if (key.startsWith(starts_with))
                result.add(key);
        }
        
        return result;
    }
    
    
    
    private static List<String> parseSfxFile (File f) throws IOException {
        List<String> results = new ArrayList<> ();
        results.add(""); // key
        results.add(""); // xml
        results.add(""); // img
        
        BufferedReader br = new BufferedReader (new FileReader (f));
        String line;
        while ((line=br.readLine()) != null) {
            String[] args = line.split(":");
            if (args.length!=2) throw new IOException ("Error while parsing sfx file; wrong number of arguments found in a line!");
            
            switch (args[0].toLowerCase().trim()) {
                case SFX_PARSE_KEYWORD_KEY :
                    results.set(0, args[1].trim());
                case SFX_PARSE_KEYWORD_XML :
                    results.set(1, args[1].trim());
                case SFX_PARSE_KEYWORD_IMG :
                    results.set(2, args[1].trim());
            }
        }
        
        return results;
    }
    
}
