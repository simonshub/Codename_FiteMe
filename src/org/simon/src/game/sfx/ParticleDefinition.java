/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.sfx;

import java.io.File;
import java.io.IOException;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;
import org.simon.src.utils.Log;
import org.simon.src.utils.ResourceManager;
import org.simon.src.utils.Settings;

/**
 *
 * @author XyRoN
 */
public class ParticleDefinition {
    
    private final String key;
    private final File config_xml;
    private final ParticleSystem particles;
    
    
    
    public ParticleDefinition (String key, String xml, String img) {
        this.key = key;
        
        if (!ResourceManager.hasGraphics(img)) Log.err("Missing graphics '"+img+"' for particle system '"+key+"'!");
        particles = new ParticleSystem (ResourceManager.getGraphics(img), Settings.max_particle_count);
        particles.setBlendingMode(ParticleSystem.BLEND_ADDITIVE);
        
        config_xml = new File (xml);
        if (!config_xml.exists()) {
            Log.err("Missing or malformed xml file at '"+xml+"' for particle system '"+key+"'!");
        }
    }
    
    
    
    public String getKey () {
        return key;
    }
    
    public ParticleSystem getParticles () {
        return particles;
    }
    
    public ConfigurableEmitter newEmitter () {
        ConfigurableEmitter emitter=null;
        try {
            emitter = ParticleIO.loadEmitter(config_xml);
        } catch (IOException ex) {
            Log.err(ex);
        }
        return emitter;
    }
    
}
