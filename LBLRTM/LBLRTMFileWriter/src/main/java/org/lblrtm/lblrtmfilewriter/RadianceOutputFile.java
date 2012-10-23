/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter;

import java.util.Arrays;
import java.util.List;
import org.lblrtm.lblrtmdata.api.LBLRTMOutputFile;
import org.lblrtm.lblrtmdata.api.VariableInfo;

/**
 *
 * @author djabry
 */
public class RadianceOutputFile implements LBLRTMOutputFile{
    private String name;
    private static final List<VariableInfo> VARS = 
            Arrays.asList(new VariableInfo[]{
                VariableInfo.WAVENUMBER,
                VariableInfo.RADIANCE
            });
    
    public RadianceOutputFile(String name){
        
        this.name=name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<VariableInfo> getVariables() {
        return VARS;
    }
    
}
