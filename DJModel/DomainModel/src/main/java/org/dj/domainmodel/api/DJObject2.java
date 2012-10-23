/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.domainmodel.api;

/**
 *
 * @author djabry
 */

public interface DJObject2 extends Unique {
    
    public static final String PROP_NAME = "name";
    public static final String PROP_ID = "id";
    public static final String PROP_DESCRIPTION = "description";

    String getName();
    void setName(String name);
    String getDescription();
    void setDescription(String description);
}
