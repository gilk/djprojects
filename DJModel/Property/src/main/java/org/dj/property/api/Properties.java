/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.property.api;

import java.util.Set;
import org.dj.listener.api.MultiListener;

/**
 *
 * @author djabry
 */
public interface Properties extends MultiListener{
    
    DJProperty getProperty(String propName);
    
    Object getPropertyValue(String propName);    
    void setPropertyValue(String propName, Object value);
    
    @Deprecated
    void setProperty(DJProperty prop);
    
    void putProperty(DJProperty prop);
    
    Set<DJProperty> getAllProperties();
}
