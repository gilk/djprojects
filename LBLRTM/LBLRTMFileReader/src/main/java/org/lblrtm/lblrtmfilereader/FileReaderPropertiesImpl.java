/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilereader;

import java.util.List;
import javolution.util.FastList;
import org.dj.property.api.PropertiesAbstr;
import org.lblrtm.lblrtmdata.api.VariableInfo;
import org.lblrtm.lblrtmfilereader.api.FileReaderProperties;
import org.lblrtm.lblrtmfilereader.api.NumberMode;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */

@ServiceProvider(service = FileReaderProperties.class)
public class FileReaderPropertiesImpl extends PropertiesAbstr implements FileReaderProperties{
    
    public FileReaderPropertiesImpl(){
        
        setVariables(new FastList<VariableInfo>());
        
    }
    

    

    @Override
    public List<VariableInfo> getVariables() {
        
        return (List<VariableInfo>) getPropertyValue(PROP_VARIABLES);
                
    }

    @Override
    public void setVariables(List<VariableInfo> variables) {
       setPropertyValue(PROP_VARIABLES, variables);
    }

    @Override
    public NumberMode getNumberMode() {
        return (NumberMode) getPropertyValue(PROP_NUMBER_MODE);
    }

    @Override
    public void setNumberMode(NumberMode mode) {
        setPropertyValue(PROP_NUMBER_MODE, mode);
    }

    @Override
    public boolean isBigEndian() {
        return (Boolean) getPropertyValue(PROP_BIG_ENDIAN);
    }

    @Override
    public void setBigEndian(boolean bE) {
        setPropertyValue(PROP_BIG_ENDIAN,bE);
    }

    @Override
    public String getRunString() {
       return (String) getPropertyValue(PROP_RUN_STRING);
    }

    @Override
    public void setRunString(String runString) {
        setPropertyValue(PROP_RUN_STRING, runString);
    }

    @Override
    public double getPressure() {
        return (Double)getPropertyValue(PROP_PRESSURE);
    }

    @Override
    public void setPressure(double pressure) {
        setPropertyValue(PROP_PRESSURE, pressure);
    }
    
}
