/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.executor;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dj.executor.api.AssignedCommand;
import org.dj.executor.api.Executor;
import org.dj.remote.api.HostInfo;
import org.openide.util.Lookup;

/**
 *
 * @author djabry
 */
public class AssignedCommandImpl extends CommandImpl implements AssignedCommand{
    private HostInfo hI;
    private final String finishString;
    private static final Executor ex = Lookup.getDefault().lookup(Executor.class);
    
    

    
    public AssignedCommandImpl(String command, String[] envP, File dir, HostInfo hI,String finishString){
        
        super(command, envP,dir);
        this.hI=hI;
        this.finishString=finishString;
        
    }

    @Override
    public void setHostInfo(HostInfo hI) {
        this.hI=hI;
    }

    @Override
    public HostInfo getHostInfo() {
        return this.hI;
    }
    
    private boolean finishTest(String line){
        
        if(line!=null){
            
            if(line.contains(this.finishString)&&!finishString.isEmpty()){
                
                return true;
            }
            
            return false;
            
        }else{
            
            return true;
 
        }

    }

    @Override
    public void run() {
        
        String line = "";
        InputStream iS = ex.executeCommand(this);
        

            BufferedReader reader = new BufferedReader(new InputStreamReader(iS));

        while (!finishTest(line)) {
            try {
                //handle.progress(line);
                line = reader.readLine();
            } catch (IOException ex1) {
                Logger.getLogger(AssignedCommandImpl.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
            
        
        
        
    }
    
}
