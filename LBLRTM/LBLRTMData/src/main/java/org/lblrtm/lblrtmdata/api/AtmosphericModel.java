/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmdata.api;

/**
 *
 * @author djabry
 */
public enum AtmosphericModel {
    
    USER_DEFINED(0,"User-defined"),
    TROPICAL(1,"Tropical"),
    MIDLATITUDE_SUMMER(2,"Mid-latitude summer"),
    MIDLATITUDE_WINTER(3,"Mid-latitude winter"),
    SUBARCTIC_SUMMER(4,"Sub-arctic summer"),
    SUBARCTIC_WINTER(5,"Sub-arctic winter"),
    US_STANDARD(6,"US standard 1976");
    
    private final int index;
    private final String name;
    
    AtmosphericModel(int index,String name){
        
        this.index=index;
        this.name=name;
    }

    @Override
    public String toString() {
        return getName();
    }
    
    public int getIndex(){
        
        return index;
    }
    
    public String getName(){
        
        return name;
    }
}

