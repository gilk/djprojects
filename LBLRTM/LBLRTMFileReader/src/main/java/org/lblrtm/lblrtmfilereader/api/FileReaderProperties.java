/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilereader.api;

import org.lblrtm.lblrtmdata.api.VariableInfo;
import java.util.List;
import org.dj.property.api.Properties;

/**
 *
 * @author djabry
 */
public interface FileReaderProperties extends Properties{
    
    
    public static final String PROP_VARIABLES = "variables";
    public static final String PROP_NUMBER_MODE = "numberMode";
    public static final String PROP_BIG_ENDIAN  = "bigEndian";
    public static final String PROP_RUN_STRING = "runString";
    public static final String PROP_PRESSURE = "pressure";
    
    List<VariableInfo> getVariables();
    void setVariables(List<VariableInfo> variables);
    
    NumberMode getNumberMode();
    void setNumberMode(NumberMode mode);
    
    boolean isBigEndian();
    void setBigEndian(boolean bE);
    
    String getRunString();
    void setRunString(String runString);
    
    double getPressure();
    void setPressure(double pressure);
    
}
