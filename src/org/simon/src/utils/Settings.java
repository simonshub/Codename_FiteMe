/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.utils;

import java.lang.reflect.Field;

/**
 *
 * @author emil.simon
 */
public class Settings {
    
    public static int screen_width = 1280;
    public static int screen_height = 960;
    
    public static int max_particle_count = 1000;
    
    public static boolean debug_cards = true;
    public static boolean debug_mode = true;
    public static boolean debug_sfx = true;
    public static boolean debug_gui = true;
    
    public static String grfx_path = "res/grfx/";
    public static String sounds_path = "res/sounds/";
    public static String fonts_path = "res/fonts/";
    public static String sfx_path = "res/data/emitters/";
    public static String card_pack_path = "res/data/card_packs/";
    public static String creature_pack_path = "res/data/creature_packs/";
    public static String character_class_path = "res/data/chars/";
    
    public static String turn_indicator_graphics = "ui/turn_indicator";
    
    
    
    public static void setProperty (String name, String value) {
        try {
            Field f = Settings.class.getDeclaredField(name);
            Class f_type = f.getType();
            
            if (f_type.isPrimitive()) {
                if (f_type.equals(int.class)) {
                    f.set(null, Integer.parseInt(value));
                } else if (f_type.equals(char.class)) {
                    f.set(null, value.charAt(0));
                } else if (f_type.equals(float.class)) {
                    f.set(null, Float.parseFloat(value));
                } else if (f_type.equals(double.class)) {
                    f.set(null, Double.parseDouble(value));
                } else if (f_type.equals(boolean.class)) {
                    f.set(null, Boolean.parseBoolean(value));
                } else if (f_type.equals(byte.class)) {
                    f.set(null, Byte.parseByte(value));
                } else if (f_type.equals(long.class)) {
                    f.set(null, Long.parseLong(value));
                } else if (f_type.equals(short.class)) {
                    f.set(null, Short.parseShort(value));
                }
            } else {
                if (f_type.equals(String.class)) {
                    f.set(null, value);
                } else if (f_type.equals(Integer.class)) {
                    f.set(null, Integer.parseInt(value));
                } else if (f_type.equals(Character.class)) {
                    f.set(null, value.charAt(0));
                } else if (f_type.equals(Float.class)) {
                    f.set(null, Float.parseFloat(value));
                } else if (f_type.equals(Double.class)) {
                    f.set(null, Double.parseDouble(value));
                } else if (f_type.equals(Boolean.class)) {
                    f.set(null, Boolean.parseBoolean(value));
                } else if (f_type.equals(Byte.class)) {
                    f.set(null, Byte.parseByte(value));
                } else if (f_type.equals(Long.class)) {
                    f.set(null, Long.parseLong(value));
                } else if (f_type.equals(Short.class)) {
                    f.set(null, Short.parseShort(value));
                }
            }
            
            Log.log("Read settings property '"+name+"' as '"+value+"'");
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            Log.log("Error while reading settings property '"+name+"' with value '"+value+"'!");
            Log.err(ex);
        }
    }
    
}
