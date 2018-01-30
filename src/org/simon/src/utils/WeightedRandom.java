/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.utils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author XyRoN
 */
public class WeightedRandom<T> {
    
    private class WeightedRandomEntry<C> {
        protected C object;
        protected float weight;
        
        protected WeightedRandomEntry (C object, float weight) {
            this.object = object;
            this.weight = weight;
        }
    }
    
    
    
    private final List<WeightedRandomEntry<T>> entries;
    
    
    
    public WeightedRandom () {
        entries = new ArrayList<> ();
    }
    
    public void add (T object, float weight) {
        entries.add(new WeightedRandomEntry<> (object, weight));
    }
    
    public T getRandom () {
        // use regular for-loops here because order is important!
        int total = 0;
        for (int i=0;i<entries.size();i++) {
            total += entries.get(i).weight;
        }
        
        float roll = SlickUtils.rand(0,total);
        float current = 0;
        
        for (int i=0;i<entries.size();i++) {
            current += entries.get(i).weight;
            if (roll < current) return entries.get(i).object;
        }
        return maxWeight();
    }
    
    public T maxWeight () {
        int max_index = 0;
        for (int i=1;i<entries.size();i++) {
            if (entries.get(max_index).weight <= entries.get(i).weight)
                max_index = i;
        }
        return entries.get(max_index).object;
    }
    
    public void clear () {
        entries.clear();
    }
    
}
