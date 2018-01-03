/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.utils;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author emil.simon
 */
public class CycleList<T> extends ArrayList {
    
    public CycleList () {
        super();
    }
    
    public CycleList (Collection<? extends T> c) {
        super(c);
    }
    
    @Override
    public T get (int index) {
        while (index < 0) {
            index += size();
        }
        while (index >= size()) {
            index -= size();
        }
        return (T) super.get(index);
    }
}
