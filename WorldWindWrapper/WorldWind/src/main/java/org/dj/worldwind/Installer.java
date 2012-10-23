/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.worldwind;

import gov.nasa.worldwind.Configuration;
import java.util.prefs.Preferences;
import org.openide.modules.ModuleInstall;
import org.openide.util.NbPreferences;

public class Installer extends ModuleInstall {
    
    private static final String PROXY_TYPE = "proxyType";
    private static final String DIRECT_CONNECTION = "0";


    @Override
    public void restored() {
        // TODO
                Preferences prefs = NbPreferences.root().node("org/netbeans/core");
        prefs.put(PROXY_TYPE, DIRECT_CONNECTION);
        
        System.out.println("WorldWind module started");

        if (Configuration.isMacOS()) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            //System.setProperty("com.apple.mrj.application.apple.menu.about.name", "World Wind Application");
            System.setProperty("com.apple.mrj.application.growbox.intrudes", "false");
            System.setProperty("apple.awt.brushMetalLook", "true");
        } else if (Configuration.isWindowsOS()) {
            System.setProperty("sun.awt.noerasebackground", "true"); // prevents flashing during window resizing
        }
    }
}
