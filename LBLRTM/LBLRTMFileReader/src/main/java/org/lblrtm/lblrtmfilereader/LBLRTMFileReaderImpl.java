/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilereader;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javolution.util.FastSet;
import org.dj.executor.api.Executor;
import org.lblrtm.lblrtmfilereader.api.FileReaderProperties;
import org.lblrtm.lblrtmfilereader.api.LBLRTMFileReader;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import ucar.nc2.FileWriter;
import ucar.nc2.NetcdfFile;
import ucar.nc2.iosp.IOServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider(service = LBLRTMFileReader.class)
public class LBLRTMFileReaderImpl implements LBLRTMFileReader {

    private Set<IOServiceProvider> iOSPs;
    private FileReaderProperties fRP;

    public LBLRTMFileReaderImpl() {

        //this.executor = Lookup.getDefault().lookup(Executor.class);
        this.iOSPs = new FastSet<IOServiceProvider>();
        this.fRP=Lookup.getDefault().lookup(FileReaderProperties.class);


    }
    //private Executor executor;

    @Override
    public NetcdfFile readFile(File file) {

        if (this.canRead(file)) {
            try {
                
                NetcdfFile open = NetcdfFile.open(file.getPath());
                
                //if(fRP.getVariables()!=null){
                    
                  // open.getIosp().sendIospMessage(fRP.getVariables().toArray()); 
                //}
                
                
                return open;
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        return null;
    }

    @Override
    public boolean canRead(File file) {

        try {

            Iterator<Class<? extends IOServiceProvider>> iterator = Lookup.getDefault().lookupResult(IOServiceProvider.class).allClasses().iterator();


            while (!NetcdfFile.canOpen(file.getPath()) && iterator.hasNext()) {

                NetcdfFile.registerIOProvider(iterator.next());
            }

            if (NetcdfFile.canOpen(file.getPath())) {

                return true;

            }


        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        } catch (InstantiationException ex) {
            Exceptions.printStackTrace(ex);
        }

        return false;
    }

    private class LBLRTMFileConverter implements Runnable {

        private final File file;
        private final File outputFile;

        public LBLRTMFileConverter(File inputFile, File outputFile) {

            this.file = inputFile;
            this.outputFile = outputFile;
        }

        @Override
        public void run() {
            try {

                ProgressHandle handle = ProgressHandleFactory.createHandle("Opening " + file.getAbsolutePath() + "...");
                handle.start();

                if (canRead(file)) {

                    NetcdfFile nf = readFile(file);
                    handle.setDisplayName("Writing file to " + outputFile.getPath() + "...");
                    NetcdfFile fOut=null;
                    try {

                        fOut = FileWriter.writeToFile(nf, outputFile.getPath());
                        
                    } finally {
                       fOut.close();
                       nf.close();
                       
                    }


                }
                handle.finish();
                

            } catch (IOException ex) {
                Logger.getLogger(LBLRTMFileReaderImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void convertFile(File inputFile, File outputFile) {

       getFileConverter(inputFile, outputFile).run();
    }

    @Override
    public Runnable getFileConverter(File inputFile, File outputFile) {

        return new LBLRTMFileConverter(inputFile, outputFile);
    }
}
