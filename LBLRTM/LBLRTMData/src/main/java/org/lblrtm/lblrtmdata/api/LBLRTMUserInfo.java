/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmdata.api;



/**
 *
 * @author djabry
 */
public interface LBLRTMUserInfo {
    
    public static final String USER_NAME = "userName";
    public static final String PASSWORD = "password";
    
    String getUserName();
    String getPassword();
    
}
