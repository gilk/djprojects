/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.remote;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javolution.util.FastMap;
import org.dj.remote.api.ChannelType;
import org.dj.remote.api.DJSch;
import org.dj.remote.api.HostInfo;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider(service = DJSch.class)
public class DJSchImpl implements DJSch {
    
    

    private static final JSch jSch = new JSch();
    private static final Map<HostInfo, Session> sessions = new FastMap<HostInfo, Session>();;
    private static final Map<HostInfo, Map<ChannelType,Channel>> channels = new FastMap<HostInfo, Map<ChannelType,Channel>>();

    private Session createSession(HostInfo hI) throws JSchException {

        Session se = sessions.get(hI);
        
        if(se!=null){
            
            if(se.isConnected()){
                
                return se;
            }
        }
        
        se = jSch.getSession(hI.getUserName(), hI.getServerName(), 22);
        se.setUserInfo(new StandardUserInfo(hI.getPassword()));
        Properties config = new Properties();
        config.setProperty("StrictHostKeyChecking", "no");
        se.setConfig(config);
        sessions.put(hI, se);
        se.connect(); 
        return se;
    }

    @Override
    public Channel openChannel(HostInfo hI, ChannelType cT) {

        try {

            Channel ch = createChannel(hI, cT);

            return ch;

        } catch (JSchException ex) {
            Logger.getLogger(DJSchImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private Channel createChannel(HostInfo hI, ChannelType channelType) throws JSchException {
        
        Map<ChannelType, Channel> chMap = channels.get(hI);
        
        if(chMap==null){
            
            chMap = new EnumMap<ChannelType, Channel>(ChannelType.class);
            channels.put(hI, chMap);
        }

        Channel ch = chMap.get(channelType);

        if (ch != null) {

            if (ch.isConnected()) {

                return ch;
            }
        }

        Session se = createSession(hI);
        ch = se.openChannel(channelType.getChannelString());
        chMap.put(channelType, ch);
        ch.connect();

        return ch;



    }
}
