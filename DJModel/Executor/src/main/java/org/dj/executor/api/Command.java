/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.executor.api;

import java.io.File;
import org.dj.remote.api.HostInfo;

/**
 *
 * @author djabry
 */
public interface Command {
    
    String getCommandString();
    String[] getEnvP();
    File getDir();
    

}
