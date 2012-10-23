/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.fileutilities;

import com.jcraft.jsch.SftpProgressMonitor;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;

/**
 *
 * @author djabry
 */
public class FileTransferProgressMonitor implements SftpProgressMonitor {

    private ProgressHandle handle;
    private double scaleFactor;
    private int currentProg;
    private int limit;

    private int convertToInteger(long l) {

        if (scaleFactor * l > Integer.MAX_VALUE) {

            scaleFactor = (Integer.MAX_VALUE - 1000) / l;
        }

        Double d = scaleFactor * l;
        return d.intValue();

    }

    @Override
    public void init(int i, String string, String string1, long l) {

        scaleFactor = 1;


        handle = ProgressHandleFactory.createHandle("Transferring file: " + string);
        this.limit=convertToInteger(l);
        handle.start(limit);
        currentProg = 0;

    }

    @Override
    public boolean count(long l) {

        if (handle != null) {  
            
            currentProg+=convertToInteger(l);
            
            if(currentProg<=limit&&currentProg>=0){
                
                handle.progress(currentProg);
            }else{

                handle.switchToIndeterminate();
            }
            

        }

        return true;
    }

    @Override
    public void end() {

        if (handle != null) {
            handle.finish();
        }

    }
}
