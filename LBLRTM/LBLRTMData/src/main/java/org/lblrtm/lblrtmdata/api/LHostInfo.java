/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmdata.api;


/**
 *
 * @author djabry
 */
public interface LHostInfo {
    
   
    LBLRTMUserInfo getUserInfo();
    ExecutableInfo getExecutableInfo(); 
    WorkingDir getWorkingDir();
    String getServerName();
    boolean isLocal();

    
}
