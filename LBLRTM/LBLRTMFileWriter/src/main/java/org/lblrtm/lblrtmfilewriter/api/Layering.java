/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter.api;

/**
 *
 * @author djabry
 */
public enum Layering {
    
    AUTO("Automatic"),
    PROFILE("Profile"),
    MANUAL("Manual");
    
    
    
    public final String fullName;
    Layering(String fullName){
        
        this.fullName=fullName;
    }

    @Override
    public String toString() {
        return fullName;
    }
    
    
    
}
