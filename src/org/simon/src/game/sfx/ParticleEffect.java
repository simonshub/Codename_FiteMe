/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.sfx;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleSystem;
import org.simon.src.utils.Log;
import org.simon.src.utils.Settings;

/**
 *
 * @author XyRoN
 */
public class ParticleEffect {
    
    public static boolean START_ON_CREATION = false;
    
    private float x, y; // current location coordinates
    private float end_x, end_y; // targeted, end coordinates of movement
    private float start_x, start_y; // the coordinates of the starting location
    private float duration, remaining_duration; // the amount (total and remaining) of time (in seconds) it takes to get from A (start) to B (end)
    
    private boolean started, finished, cleanup;
    
    public ParticleSystem instance;
    public ConfigurableEmitter emitter;
    public final ParticleDefinition parent;
    
    public ParticleEffect (ParticleDefinition parent, float x, float y) {
        this.parent = parent;
        this.started = false;
        this.finished = false;
        this.cleanup = false;
        
        try {
            this.instance = parent.getParticles().duplicate();
        } catch (SlickException ex) {
            Log.err(ex);
        }
        
        this.x = x;
        this.y = y;
        this.end_x = x;
        this.end_y = y;
        this.start_x = x;
        this.start_y = y;
        this.duration = 0f;
        this.remaining_duration = 0f;
        this.emitter = null;
        
        if (START_ON_CREATION) start();
    }
    
    public final void start () {
        this.started = true;
        this.finished = false;
        this.cleanup = false;
        this.emitter = this.parent.newEmitter();
        this.emitter.setPosition(x,y,false);
        this.emitter.resetState();
        this.instance.addEmitter(this.emitter);
    }
    
    public boolean isStarted () {
        return started;
    }
    
    public boolean isFinished () {
        return finished;
    }
    
    public boolean isReadyForCleanup () {
        return cleanup;
    }
    
    public void setMovement (float target_x, float target_y, float duration) {
        this.end_x = target_x;
        this.end_y = target_y;
        this.start_x = this.x;
        this.start_y = this.y;
        this.duration = duration;
        this.remaining_duration = duration;
    }
    
    public void render (Graphics g) {
        instance.render();
        
        if (!started || finished || cleanup) return;
        
        if (Settings.debug_sfx) {
            g.setColor(Color.gray);
            g.fillRect(x-3, y-3, 6, 6);
        }
    }
    
    public void update (int dt) {
        instance.update(dt);
        
        if (started && finished && !emitter.completed()) {
            emitter.wrapUp();
            return;
        } else if (started && finished && emitter.completed()) {
            cleanup = true;
            return;
        } else if (!started || finished || cleanup) return;
        
        if ( remaining_duration>0f && emitter!=null ) {
            if (end_x != start_x || end_y != start_y) {
                float change_modifier = (dt/1000f) / duration;
                float total_dx = (end_x - start_x);
                float total_dy = (end_y - start_y);
                float dx = total_dx * change_modifier;
                float dy = total_dy * change_modifier;

                x += dx;
                y += dy;
                this.emitter.setPosition(x, y, false);
            }
            
            this.remaining_duration -= dt/1000f;
            if (remaining_duration <= 0f)
                this.finished = true;
        }
    }
    
}
