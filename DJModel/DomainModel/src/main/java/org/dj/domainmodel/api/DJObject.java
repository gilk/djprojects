/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.domainmodel.api;

import org.dj.property.api.Properties;

/**
 *
 * @author djabry
 */
public interface DJObject extends Properties, Unique{
    
    
    public static final String PROP_ID = "id";
    
    public static final String PROP_NAME = "name";
    String getName();
    void setName(String name);
    
    public static final String PROP_DESCRIPTION = "description";
    String getDescription();
    void setDescription(String description);
    
    

}
