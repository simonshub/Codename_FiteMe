/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.sfx;

import org.simon.src.utils.Log;
import org.simon.src.utils.ResourceManager;

/**
 *
 * @author XyRoN
 */
public class Soundpoint {
    private int index;
    private String sound;

    public Soundpoint (int index, String sound) {
        if (ResourceManager.hasSound(sound)) {
            this.index = index;
            this.sound = sound;
        } else {
            Log.err("Tried to create soundpoint at index "+index+", but sound resource '"+sound+"' was not loaded!");
        }
    }

    public int getIndex () {
        return index;
    }

    public void playSound () {
        ResourceManager.getSound(sound).play();
    }
    
    public void moveIndexUp () {
        index--;
    }
}
