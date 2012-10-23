/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilereader;

/**
 *
 * @author djabry
 */
public enum HeaderVariable {
    
    LEVEL_INDEX(332),
    NUMBER_OF_MOLECULES(330),
    NUMBER_OF_LEVELS(326),
    DIRECTION(310);
    
    private final int positionInHeader;
    
    HeaderVariable(int positionInHeader){
        
        this.positionInHeader=positionInHeader;
        
    }
    
    public int getPositionInHeader(){
        
        return this.positionInHeader;
    }
    
}
