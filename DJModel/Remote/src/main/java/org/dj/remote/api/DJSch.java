/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.remote.api;

import com.jcraft.jsch.Channel;

/**
 *
 * @author djabry
 */
public interface DJSch {
    
    
    Channel openChannel(HostInfo hI,ChannelType cT);
    
}
