/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.sfx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.newdawn.slick.Graphics;
import org.simon.src.game.gui.GuiElement;
import org.simon.src.utils.Log;
import org.simon.src.utils.ResourceMgr;
import org.simon.src.utils.Settings;

/**
 *
 * @author XyRoN
 */
public class SpecialEffect {
    
    public static final String PARSE_SFX_PARTICLE_DELIMITER = ";";
    public static final String PARSE_SFX_ARG_DELIMITER = "_";
    
    public static final String PARSE_SFX_CASTPOINT_KEYWORD = "castpoint";
    public static final String PARSE_SFX_SOUNDPOINT_KEYWORD = "soundpoint";
    
    public static final String PARSE_PARTICLE_SRC_KEYWORD = "src";
    public static final String PARSE_PARTICLE_TAR_KEYWORD = "tar";
    
    public static final int PARSE_PARTICLE_ARG_LENGTH = 4;      // name from to duration
    public static final int PARSE_SOUNDPOINT_ARG_LENGTH = 2;    // soundpoint soundname
    
    
    
    private boolean start;
    
    private int castpoint_index;
    private int current_particle_index;
    private List<Soundpoint> soundpoint_list;
    
    private List<ParticleEffect> particle_list;
    private SpecialEffectCallback callback;
    
    
    
    public SpecialEffect () {
        start = false;
        current_particle_index = 0;
        soundpoint_list = new ArrayList<> ();
        particle_list = new ArrayList<> ();
        callback = null;
    }
    
    public SpecialEffect (String sfx_callstring, GuiElement src, GuiElement target) {
        this();
        
        parseSfx(sfx_callstring, src, target);
    }
    
    
    
    public final void parseSfx (String sfx_callstring, GuiElement src, GuiElement target) {
        List<String> particle_callstrings = new ArrayList <> (Arrays.asList(sfx_callstring.split(PARSE_SFX_PARTICLE_DELIMITER)));

        for (int i=0;i<particle_callstrings.size();i++) {
            String particle_callstring = particle_callstrings.get(i);

            if (particle_callstring.equalsIgnoreCase(PARSE_SFX_CASTPOINT_KEYWORD)) {
                // CASTPOINT ARG
                if (i+1 >= particle_callstrings.size()) {
                    Log.err("Castpoint set to index "+i+", but no particle callstring exists at next index ("+String.valueOf(i+1)+")!");
                } else {
                    this.castpoint_index = i;
                    particle_callstrings.remove(i);
                    if (Settings.debug_sfx) Log.log("Set castpoint to index "+i);
                    i--;
                }

            } else if (particle_callstring.toLowerCase().startsWith(PARSE_SFX_SOUNDPOINT_KEYWORD.toLowerCase())) {
                // SOUND ARG
                String[] args = particle_callstrings.get(i).trim().split(PARSE_SFX_ARG_DELIMITER);
                
                if (args.length!=PARSE_SOUNDPOINT_ARG_LENGTH) {
                    Log.err("Wrong number of arguments for soundpoint in sfx callstring; required "+PARSE_SOUNDPOINT_ARG_LENGTH+", found "+args.length+"!");
                } else {
                    soundpoint_list.add(new Soundpoint (i, args[1].trim()));
                    particle_callstrings.remove(i);
                    if (Settings.debug_sfx) Log.log("Added soundpoint '"+args[1].trim()+"' at index "+i);
                    i--;
                }

            } else {
                // PARTICLE ARG
                String[] args = particle_callstrings.get(i).trim().split(PARSE_SFX_ARG_DELIMITER);

                if (args.length!=PARSE_PARTICLE_ARG_LENGTH) {
                    Log.err("Wrong number of arguments for particle in sfx callstring; required "+PARSE_PARTICLE_ARG_LENGTH+", found "+args.length+"!");
                } else {
                    String name = args[0].trim();
                    String from_token = args[1].trim();
                    String to_token = args[2].trim();
                    float duration = 0f;
                    float from_x=0f, from_y=0f, to_x=0f, to_y=0f;
                    boolean failed = false;

                    try {
                        duration = Float.parseFloat(args[3].trim().replace(",","."));
                    } catch (NumberFormatException ex) {
                        Log.err("Unparseable duration value for particle in sfx callstring!");
                        failed = true;
                    }

                    if (from_token.equalsIgnoreCase(PARSE_PARTICLE_SRC_KEYWORD)) {
                        from_x = src.getCenterX();
                        from_y = src.getCenterY();
                    } else if (from_token.equalsIgnoreCase(PARSE_PARTICLE_TAR_KEYWORD)) {
                        from_x = target.getCenterX();
                        from_y = target.getCenterY();
                    } else {
                        Log.err("Unknown 'from' value '"+from_token+"' for particle in sfx callstring!");
                        failed = true;
                    }

                    if (to_token.equalsIgnoreCase(PARSE_PARTICLE_SRC_KEYWORD)) {
                        to_x = src.getCenterX();
                        to_y = src.getCenterY();
                    } else if (to_token.equalsIgnoreCase(PARSE_PARTICLE_TAR_KEYWORD)) {
                        to_x = target.getCenterX();
                        to_y = target.getCenterY();
                    } else {
                        Log.err("Unknown 'to' value '"+to_token+"' for particle in sfx callstring!");
                        failed = true;
                    }

                    if (!failed) this.addParticle(name, from_x, from_y, to_x, to_y, duration);
                    else Log.err("Unable to add particle from callstring '"+particle_callstrings.get(i).trim()+"'");
                }

            }
        }
    }
    
    
    
    public boolean isEmpty () {
        return particle_list.isEmpty();
    }
    
    private void addParticle (String name, float x, float y, float end_x, float end_y, float duration) {
        if (!ResourceMgr.hasParticle(name)) {
            Log.err("No particle definition of name '"+name+"' loaded!");
            return;
        }
        
        ParticleEffect effect = new ParticleEffect (ResourceMgr.getParticle(name), x, y);
        if (duration>0f) {
            effect.setMovement(end_x, end_y, duration);
        }
        particle_list.add(effect);
        if (Settings.debug_sfx) Log.log("Added effect '"+name+"' at ("+x+","+y+")");
    }
    
    
    
    public void start () {
        start = true;
    }
    
    public void stop () {
        start = false;
        particle_list = new ArrayList<> ();
    }
    
    public void setCallback (SpecialEffectCallback callback) {
        this.callback = callback;
    }
    
    public SpecialEffectCallback getCallback () {
        return callback;
    }
    
    public void invokeCallback () {
        if (callback!=null) {
            callback.call();
        }
    }
    
    public void playSounds (int index) {
        for (int i=0;i<soundpoint_list.size();i++) {
            Soundpoint soundpoint = soundpoint_list.get(i);
            
            if (soundpoint.getIndex() == index)
                soundpoint.playSound();
        }
    }
    
    private void moveUpSoundpoints () {
        for (int i=0;i<soundpoint_list.size();i++) {
            soundpoint_list.get(i).moveIndexUp();
        }
    }
    
    public void checkCastpoint (int index) {
        if (current_particle_index==castpoint_index) {
            invokeCallback();
            if (Settings.debug_sfx) Log.log("Castpoint reached at index "+current_particle_index);
        }
    }
    
    public void setCastpoint (int castpoint_index) {
        this.castpoint_index = castpoint_index;
    }
    
    public boolean isStarted () {
        return start;
    }
    
    
    
    public void render (Graphics g) {
        for (int i=0;i<particle_list.size();i++) {
            particle_list.get(i).render(g);
        }
    }
    
    public boolean update (int dt) {
        if (!start) return true;
        if (particle_list.isEmpty()) return false;
        
        if (current_particle_index<particle_list.size() && !particle_list.get(current_particle_index).isStarted()) {
            particle_list.get(current_particle_index).start();
            
            checkCastpoint(current_particle_index);
            playSounds(current_particle_index);
        }
            
        for (int i=0;i<particle_list.size();i++) {
            particle_list.get(i).update(dt);
            
            if (particle_list.get(i).isReadyForCleanup()) {
                if (Settings.debug_sfx) Log.log("Special effect '"+particle_list.get(i).parent.getKey()+"' has finished and was removed");
                current_particle_index = Math.max(current_particle_index-1, 0);
                
                particle_list.remove(i);
                
                i--;
                castpoint_index--;
                moveUpSoundpoints();
            }
        }
        
        if (current_particle_index < particle_list.size() && particle_list.get(current_particle_index).isFinished()) {
            current_particle_index++;
        } else if (current_particle_index >= particle_list.size()) {
            current_particle_index=0;
        }
        
        return true;
    }
    
}
