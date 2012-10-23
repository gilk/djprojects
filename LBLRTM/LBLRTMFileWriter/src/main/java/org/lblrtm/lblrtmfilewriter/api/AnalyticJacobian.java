/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter.api;

import org.lblrtm.lblrtmdata.api.AJParameter;
import java.util.EnumSet;
import java.util.List;

/**
 *
 * @author djabry
 */
public interface AnalyticJacobian extends CalculationTemplate{
    
    public EnumSet<AJParameter> getAJParameters();
    public void setAJParameters(EnumSet<AJParameter> aP);
    
    public static final String PROP_AJPARAMETERS = "aJParameters";
    
}
