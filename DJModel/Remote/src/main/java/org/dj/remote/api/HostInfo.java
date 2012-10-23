/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.remote.api;

/**
 *
 * @author djabry
 */
public interface HostInfo {
    
    String getServerName();
    String getPassword();
    String getUserName();
    boolean isLocal();
    
    public interface Provider<H extends HostInfo> {
        
        H getHostInfo();
    }
    
    
    public static final HostInfo LOCAL_HOST = new HostInfo() {

        @Override
        public String getServerName() {
            return "localhost";
        }

        @Override
        public String getPassword() {
            return "";
        }

        @Override
        public String getUserName() {
           return "";
        }

        @Override
        public boolean isLocal() {
            return true;
        }
    };
    
}
