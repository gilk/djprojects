/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmdata.api;

/**
 *
 * @author djabry
 */
public class WNRange {
    
    private double v1;
    private double v2;
    
    public WNRange(double v1, double v2){
        
        this.v1=v1;
        this.v2=v2;
        
        
    }

    
    public double getLength(){
        return getV2()-getV1();
    }
    

    /**
     * @return the v1
     */
    public double getV1() {
        return v1;
    }

    /**
     * @return the v2
     */
    public double getV2() {
        return v2;
    }

    /**
     * @param v1 the v1 to set
     */
    public void setV1(double v1) {
        this.v1 = v1;
    }

    /**
     * @param v2 the v2 to set
     */
    public void setV2(double v2) {
        this.v2 = v2;
        
    }
    
}
