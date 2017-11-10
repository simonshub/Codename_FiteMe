/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.main;

import java.io.File;
import org.simon.src.utils.Log;
import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.simon.src.game.states.cardcrafter.CardCrafterState;
import org.simon.src.game.states.cardgallery.CardGalleryState;
import org.simon.src.game.states.combat.CombatState;
import org.simon.src.game.states.creaturecrafter.CreatureCrafterState;
import org.simon.src.utils.Consts;
import org.simon.src.utils.ResourceMgr;
import org.simon.src.utils.Settings;

/**
 *
 * @author emil.simon
 */
public class Main extends StateBasedGame {
    
    public static void main (String[] args) {
        Log.log("Starting ...");
        File file = new File ("natives");
        if (file.exists()) {
            switch(LWJGLUtil.getPlatform()) {
                case LWJGLUtil.PLATFORM_WINDOWS:
                    file = new File("native/windows/");
                    break;
                case LWJGLUtil.PLATFORM_LINUX:
                    file = new File("native/linux/");
                    break;
                case LWJGLUtil.PLATFORM_MACOSX:
                    file = new File("native/macosx/");
                    break;
                default:
                    file = new File("native/windows/");
                    break;
            }
            
            Log.log("LWJGL Natives : '"+file.getAbsolutePath()+"'");
            System.setProperty("org.lwjgl.librarypath",file.getAbsolutePath());
        }

        try {
            AppGameContainer agc = new AppGameContainer (new Main (Consts.APP_TITLE));
            agc.setDisplayMode (Settings.screen_width, Settings.screen_height, false);
            agc.setTargetFrameRate(60);
            agc.setAlwaysRender(true);
            
            agc.start();
        } catch (SlickException ex) {
            Log.err("Couldn't start the game!");
            Log.err(ex);
            System.exit(-1);
        }
    }

    @Override
    public void initStatesList(GameContainer container) {
        try {
            ResourceMgr.init();
            AppGameContainer app = (AppGameContainer) this.getContainer();
            app.setDisplayMode (Settings.screen_width, Settings.screen_height, false);
        } catch (IllegalArgumentException | SlickException ex) {
            Log.err("Couldn't start the game!");
            Log.err(ex);
            System.exit(-1);
        }
            
        this.addState(new CombatState ());
        this.addState(new CardCrafterState ());
        this.addState(new CreatureCrafterState ());
        this.addState(new CardGalleryState ());
//        this.addState(new CardCrafterState ());
        
        this.enterState(CombatState.ID);
    }
    
    public Main (String title) {
        super(title);
    }
    
}
