/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.executor;

import com.jcraft.jsch.Channel;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dj.executor.api.AssignedCommand;
import org.dj.executor.api.Command;
import org.dj.executor.api.Executor;
import org.dj.fileutilities.api.DJFile;
import org.dj.fileutilities.api.FileUtilities;
import org.dj.remote.api.ChannelType;
import org.dj.remote.api.DJSch;
import org.dj.remote.api.HostInfo;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider(service = Executor.class)
public class ExecutorImpl extends ThreadPoolExecutor implements Executor {

    public ExecutorImpl() {
        
        super(NUM_THREADS, NUM_THREADS, 0xc8, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
       

    }
    
    private static final DJSch dJSch= Lookup.getDefault().lookup(DJSch.class);;
    private static final FileUtilities fU = Lookup.getDefault().lookup(FileUtilities.class);

    @Override
    public void execute(Runnable r) {

        //SwingUtilities.invokeLater(r);
        super.execute(r);

    }
    
    private String generateRemoteCommandString(Command command){
        
        String commString = command.getCommandString();
        
        for(String s: command.getEnvP()){
            
            commString+=" "+s;
            
        }
        
        return commString;
    }
    
    private Command generateCDCommand(AssignedCommand c){
        
        if(c.getDir()!=null){
            File dir = c.getDir();
            
            DJFile f = fU.fromFile(dir,c.getHostInfo());
            
            Command cd = new CommandImpl("cd "+f.getPath(),new String[]{},null);
            return cd;
        }
        
        return null;
    }
    
    private void writeCommand(Command c, BufferedWriter writer) throws IOException{
        
        writer.write(generateRemoteCommandString(c));
                writer.newLine();
                writer.flush();
        
    }
    
    private InputStream writeRemoteCommand(String command, Channel ch) throws IOException{
        
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ch.getOutputStream()));
        writer.write(command);
        writer.newLine();
        writer.flush();
        return ch.getInputStream();
        
    }
    
   
    private void waitForRemoteCommandToFinish(InputStream iS, Channel channel) throws IOException{
        
      byte[] tmp=new byte[1024];
      while(true){
        while(iS.available()>0){
          int i=iS.read(tmp, 0, 1024);
          if(i<0)break;
          System.out.print(new String(tmp, 0, i));
        }
        if(channel.isClosed()){
          System.out.println("exit-status: "+channel.getExitStatus());
          break;
        }
        try{Thread.sleep(1000);}catch(Exception ee){}
      }
        
//        String line = "";
//        
//       BufferedReader reader = new BufferedReader(new InputStreamReader(iS));
//       
//       while(line!=null){
//           
//           
//           line =reader.readLine();
//       }
       
    }
    
    private void waitForLocalCommandToFinish(InputStream iS){
        
        String line = "";
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(iS));

        while (line!=null) {
            try {
                //handle.progress(line);
                line = reader.readLine();
            } catch (IOException ex1) {
                Logger.getLogger(AssignedCommandImpl.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }
    
    

    @Override
    public InputStream executeCommand(AssignedCommand command) {
        
        HostInfo hI = command.getHostInfo();
        
        try {
            if (!hI.isLocal()) {
                //Execute remote command
                Channel ch = dJSch.openChannel(hI, ChannelType.SHELL);

                Command cd = generateCDCommand(command); 
                //InputStream iS = ch.getInputStream();
                //BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ch.getOutputStream()));
                
//                if(cd!=null){
//                    writeCommand(cd,writer);
//                    waitForCommandToFinish(iS,ch);    
//                }
//                
//                writeCommand(command, writer);
                
                String cdCommandString = cd.getCommandString();
                InputStream iS = writeRemoteCommand(cdCommandString,ch);
                //waitForRemoteCommandToFinish(iS,ch);
                
                
                 
                 String remoteCommand = generateRemoteCommandString(command);
                 
                 iS = writeRemoteCommand(remoteCommand,ch);
                 //waitForRemoteCommandToFinish(iS,ch);
                 
                return iS;
                
                
            } else {

                //Execute Local command
               InputStream iS = Runtime.getRuntime().exec(command.getCommandString(), command.getEnvP(), command.getDir()).getInputStream();
                //waitForLocalCommandToFinish(iS);
                
                
                
               return iS;
            }

        } catch (IOException ex) {
            Logger.getLogger(ExecutorImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }    

    @Override
    public AssignedCommand createCommand(String command, String[] envP, File dir) {
        return createCommand(command,envP,dir,HostInfo.LOCAL_HOST);
    }

    @Override
    public AssignedCommand createCommand(String command, String[] envP, File dir, HostInfo hI) {
        return createCommand(command,envP,dir,hI,"");
    }

    @Override
    public AssignedCommand createCommand(String command, String[] envP, File dir, HostInfo hI, String finishString) {
        return new AssignedCommandImpl(command, envP,dir,hI,finishString);
    }

    @Override
    public void execute(final List<Runnable> runnables) {
        Runnable r = new Runnable() {

            @Override
            public void run() {
               for(Runnable r:runnables){
                   
                   r.run();
               }
            }
        };
    }

    
}
