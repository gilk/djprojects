/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.remote.api;

/**
 *
 * @author djabry
 */
public enum ChannelType {
    
    SFTP("sftp"),
    SHELL("shell"),
    EXEC("exec");
    
    
    
    private final String channelString;
    
    ChannelType(String name){
        
        channelString = name;
        
    }
    
    public String getChannelString(){
        
        return channelString;
    }
    
}
