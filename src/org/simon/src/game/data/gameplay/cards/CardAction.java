/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.data.gameplay.cards;

import org.simon.src.game.data.gameplay.creatures.Creature;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.simon.src.utils.Log;
import org.simon.src.utils.Settings;

/**
 *
 * @author XyRoN
 */
public class CardAction {
    
    private final Method method;
    private final List<String> arg_list;
    
    public CardAction (Method method, List<String> arg_list) {
        this.method = method;
        this.arg_list = new ArrayList<> (arg_list);
    }
    
    @Override
    public String toString () {
        String method_name = method!=null ? method.getName() : "null";
        String arg_list_str = !arg_list.isEmpty() ? arg_list.get(0) : "null";
        for (int i=1;i<arg_list.size();i++)
            arg_list_str += Card.ACTION_TOKEN_DELIMITER + arg_list.get(i);
        
        return method_name + Card.ACTION_TOKEN_DELIMITER + arg_list_str;
    }
    
    public Method getMethod () {
        return method;
    }
    
    public List<String> getArgs () {
        return arg_list;
    }
    
    public void call (Creature source, Card parent, Creature... targets) {
        try {
            if (Settings.debug_cards) {
                String args = "";
                for (int i=0;i<arg_list.size();i++) {
                    String arg = arg_list.get(i);
                    args += arg + ( (i+1<arg_list.size()) ? ", " : "" );
                }
                Log.log("Invoking card effect '"+method.getName()+"' with args ["+args+"]");
            }
            
            List<Creature> target_list = new ArrayList<> (Arrays.asList(targets));
            this.method.invoke(null, source, parent, arg_list, target_list);
        } catch (NullPointerException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Log.err("Error while invoking card effect!");
            Log.err(ex);
        }
    }
    
}
