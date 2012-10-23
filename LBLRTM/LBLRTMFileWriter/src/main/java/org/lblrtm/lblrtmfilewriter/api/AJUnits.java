/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter.api;

/**
 *
 * @author djabry
 */
public enum AJUnits {
    
    D_RAD_D_LN_MR("dR/dln(MR)", 
            "Derivative of radiance with respect to natural logarithm of variable"),
    D_RAD_D_MR("dR/dMR",
            "Derivative of radiance with respect to variable"),
    D_BT_D_LN_MR("dBT/dln(MR)",
            "Derivative of brightness temperature with respect to natural logarithm of variable"),
    D_BT_D_MR("dBT/dMR",
            "Derivative of brightness temperature with respect to variable");
    
    public final String unitString;
    public final String unitDescr;
    
    AJUnits(String unitName,String unitDescr){
        this.unitString=unitName;
        this.unitDescr=unitDescr;
    }

    @Override
    public String toString() {
        return unitString;
    }
    
    
    
}
