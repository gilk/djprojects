/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter.api;

/**
 *
 * @author djabry
 */
public enum Format {
    STRING("s"),
    INTEGER("d"),
    FLOAT("G"),
    EXP("E");
    
    private String s;
   
    Format(String s){
    
        this.s=s;
    }
    
    public String getFormatString(){
        
        return s;
    }
    
    
    
}
