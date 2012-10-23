/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.fileutilities;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import java.io.*;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javolution.util.FastMap;
import org.dj.fileutilities.api.DJFile;
import org.dj.fileutilities.api.FileUtilities;
import org.dj.remote.api.ChannelType;
import org.dj.remote.api.DJSch;
import org.dj.remote.api.HostInfo;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider(service = FileUtilities.class)
public class FileUtilitiesImpl implements FileUtilities {


    public FileUtilitiesImpl(){
        this.tempDir=null;
        try {
            File tF = File.createTempFile("FileUtilities", "");
            
            String pathToTempDir = tF.getParent();
            tF.delete();
            
            this.tempDir = new File(pathToTempDir+File.separator+"TempFiles");
            if(!tempDir.exists()){
                
                tempDir.mkdirs();
            }
        } catch (IOException ex) {
            Logger.getLogger(FileUtilitiesImpl.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }
    
    private static final DJSch dJSch=Lookup.getDefault().lookup(DJSch.class);
    private static final int BUFFER_SIZE = 1024;
    private static final int UPDATE_FREQ = BUFFER_SIZE*50;
    private File tempDir;
    private static final Map<DJFile,DJFile> COPY_OPERATIONS = new FastMap<DJFile,DJFile>();



    private InputStream generateInputStream(DJFile f) throws JSchException, SftpException, FileNotFoundException {

        if (!f.getHostInfo().isLocal()) {

            
            ChannelSftp ch = (ChannelSftp) dJSch.openChannel(f.getHostInfo(), ChannelType.SFTP);
            return ch.get(f.getPath());

        } else {
            return new FileInputStream(f.getFile());
        }
    }

    private OutputStream generateOutputStream(DJFile f) throws JSchException, SftpException, FileNotFoundException {

        if (!f.getHostInfo().isLocal()) {
            
            //Delete file if it already exists
            if(exists(f)){
                
                delete(f);
            }
            
            ChannelSftp ch = (ChannelSftp) dJSch.openChannel(f.getHostInfo(), ChannelType.SFTP);
            
            
            return ch.put(f.getPath());
        } else {
            return new FileOutputStream(f.getFile());
        }
    }


    private void transferFile(InputStream iS, OutputStream oS,
            String sourceName,
            String destinationName) throws IOException {


        int counter = UPDATE_FREQ;
        int transferred = 0;
        
        int av = iS.available();

        FileTransferProgressMonitor fT = new FileTransferProgressMonitor();
        
        fT.init(0, sourceName, destinationName, av);
        
        byte buf[]=new byte[BUFFER_SIZE];
  
        int len;
        
        try{
            while ((len=iS.read(buf))>0) {
            
            counter-=BUFFER_SIZE;
            
            oS.write(buf,0,len);
            
            if (counter == 0) {
                
                transferred += UPDATE_FREQ;
                counter = UPDATE_FREQ;   
                fT.count(transferred);
                oS.flush();

            }
        }
            
        }finally{
            fT.end();
            iS.close();
            oS.close();
            
        }
    }

    private String generateTransferText(DJFile f) {

        String s = f.getFile().getName();

        if (!f.getHostInfo().isLocal()) {
            
            return s += " on " + f.getHostInfo().getServerName();
        }

        return s;

    }

    @Override
    public void copyFile(DJFile source, DJFile destination) throws FileNotFoundException, IOException {
        try {

            InputStream iS = generateInputStream(source);
            OutputStream oS = generateOutputStream(destination);
            
            try{
                
                if(this.isBeingTransferred(destination)){
                    
                    if(COPY_OPERATIONS.get(destination).equals(source)){
                        
                        this.waitForTransferToFinish(destination);
                    }
                    
                    return;
                }
                
                COPY_OPERATIONS.put(destination, source);
                
                transferFile(iS, oS,
                    generateTransferText(source),
                    generateTransferText(destination));
                
            }finally{
                COPY_OPERATIONS.remove(destination);

            }

            

        } catch (JSchException ex) {
            Logger.getLogger(FileUtilitiesImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SftpException ex) {
            Logger.getLogger(FileUtilitiesImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @Override
    public void makeSymbolicLink(DJFile source, DJFile destination) throws IOException {
        
        if(!source.getHostInfo().isLocal()&&!destination.getHostInfo().isLocal()){
             if (source.getHostInfo().getServerName().equals(
                destination.getHostInfo().getServerName())) {

            try {
                
                ChannelSftp ch = (ChannelSftp) dJSch.openChannel(source.getHostInfo(), ChannelType.SFTP);

                if(exists(destination)){
                    
                    delete(destination);
                }
                
                ch.symlink(source.getPath(),
                        destination.getPath());
                
                

            } catch (SftpException ex) {
                Logger.getLogger(FileUtilitiesImpl.class.getName()).log(Level.SEVERE, null, ex);
            } 
        
        }
            
            
        }else if(source.getHostInfo().isLocal()&&destination.getHostInfo().isLocal()){
            
            makeSymbolicLink(source.getFile(),destination.getFile());
 
        }

    }

    @Override
    public void makeSymbolicLink(File source, File destination) throws IOException {
   
            
            String[] command = null;


            String osName = System.getProperty("os.name").toLowerCase();
            String link = destination.getPath();
            String target = source.getPath();

            if (osName.contains("win")) {

                command = new String[]{
                    "mklink", link, target
                };

            } else {

                command = new String[]{
                    "ln", "-s", target, link
                };
            }

            Runtime.getRuntime().exec(command);




    }

    @Override
    public DJFile fromFile(File file, HostInfo hI) {
        return new DJFileImpl(file,hI);
    }

    @Override
    public DJFile fromFile(File file) {
        return fromFile(file, HostInfo.LOCAL_HOST);
    }

    @Override
    public void rename(DJFile file, DJFile newFile) {
        
        if(file.getHostInfo().isLocal()){
            if(newFile.getHostInfo().isLocal()){
                
                file.getFile().renameTo(newFile.getFile());
                
            }
        }else{
            if(file.getHostInfo().getServerName().equals(newFile.getHostInfo().getServerName())){
                ChannelSftp ch = (ChannelSftp) dJSch.openChannel(file.getHostInfo(), ChannelType.SFTP);
                try {
                    
                    if(exists(newFile)){
                        delete(newFile);   
                    }   
                    ch.rename(file.getPath(), newFile.getPath());
                    
                } catch (SftpException ex) {
                    Logger.getLogger(FileUtilitiesImpl.class.getName()).log(Level.SEVERE, null, ex);
                    
                }       
                //ch.disconnect();  
            } 
        }   
    }
    
    
    

    @Override
    public void mkdir(DJFile file) {
        if(file.getHostInfo().isLocal()){
            
            file.getFile().mkdir();
        }else{
            try {
                ChannelSftp ch = (ChannelSftp) dJSch.openChannel(file.getHostInfo(), ChannelType.SFTP);
                //ch.cd(file.getFile().getParent());
                System.out.println("Making directory "+ file.getPath());
                ch.mkdir(file.getPath());
            } catch (SftpException ex) {
                Logger.getLogger(FileUtilitiesImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }
    }

    @Override
    public boolean exists(DJFile file) {
        if(file.getHostInfo().isLocal()){
            
            return file.getFile().exists();
        }else{
           
                ChannelSftp ch = (ChannelSftp) dJSch.openChannel(file.getHostInfo(), ChannelType.SFTP);
                
                try{
                    ch.lstat(file.getPath());
                    return true;
                    
                }catch(Exception ex){
                    
                    
                }
 
            } 
            return false;

        }
    

    private void deleteDirAndSubFiles(File dir) {

        for (File f : dir.listFiles()) {

            if (f.isDirectory()) {

                deleteDirAndSubFiles(f);
            } else {

                f.setWritable(true,false);
                f.delete();
            }
        }

        dir.setWritable(true, false);
        dir.delete();


    }
    
    @Override
    public void delete(DJFile file) {
        if(file.getHostInfo().isLocal()){
            
            
            if(file.getFile().isDirectory()){
                
                deleteDirAndSubFiles(file.getFile());
                
            }else{
                file.getFile().setWritable(true,false);
                file.getFile().delete();
            }
            
            
        }else{
            try {
                
                ChannelSftp ch = (ChannelSftp) dJSch.openChannel(file.getHostInfo(), ChannelType.SFTP);
               
                ch.rm(file.getPath());
            } catch (SftpException ex) {
                Logger.getLogger(FileUtilitiesImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
  
    }
    
    public void waitForTransferToFinish(DJFile fileBeingTransferred){
        //retry every second to see if file is still being transferred
        while(isBeingTransferred(fileBeingTransferred)){

            long t = System.currentTimeMillis();
            long tEnd = t+1000;

            while(t<tEnd){
                
                t = System.currentTimeMillis();
            }
        }
    }

    @Override
    public boolean isBeingTransferred(DJFile file) {
       if(COPY_OPERATIONS.containsKey(file)||
               COPY_OPERATIONS.containsValue(file)){
           
           return true;
       }
       
       return false;
    }

    @Override
    public File getTempDir() {
        return this.tempDir;
    }

    @Override
    public void copyFile(InputStream iS, DJFile destination) throws IOException, FileNotFoundException {
        try {
            transferFile(iS,generateOutputStream(destination),"",generateTransferText(destination));;
        
        
        } catch (JSchException ex) {
            Exceptions.printStackTrace(ex);
        } catch (SftpException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

}
