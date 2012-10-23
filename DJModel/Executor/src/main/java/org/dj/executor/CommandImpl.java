/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.executor;

import java.io.File;
import org.dj.executor.api.Command;

/**
 *
 * @author djabry
 */
public class CommandImpl implements Command{
    
    public CommandImpl(String c,String[] eP, File dir){
        
        this.commandString=c;
        this.envP=eP;
        this.dir=dir;
    }

    
    private final String commandString;
    private final String[] envP;
    private final File dir;

    @Override
    public String getCommandString() {
        return this.commandString;
    }

    @Override
    public String[] getEnvP() {
        return this.envP;
    }

    @Override
    public File getDir() {
        return this.dir;
    }

    
    
}
