/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.host.api;

import java.io.File;
import org.dj.db.api.DBObject;
import org.dj.remote.api.HostInfo;

/**
 *
 * @author djabry
 */
public interface LBLRTMHostInfo extends HostInfo, DBObject{
    File getWorkingDir();
    void setWorkingDir(File dir);
    File getExecutable();
    void setExecutable(File ex);
    
    void setPassword(String password);
    void setUserName(String userName);
    void setServerName(String serverName);
    void setLocal(boolean local);
    
    
    public static final String PROP_WORKING_DIR = "workingDir";
    public static final String PROP_EXECUTABLE = "executable";
    public static final String PROP_PASSWORD = "password";
    public static final String PROP_USER_NAME = "userName";
    public static final String PROP_SERVER_NAME = "serverName";
    public static final String PROP_LOCAL = "local";
    
}
