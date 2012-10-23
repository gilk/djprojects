/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter.api;


import java.util.List;
import org.dj.domainmodel.api.DJNodeObject;
import org.lblrtm.lblrtmfilewriter.RepeatedRecordSet;

/**
 *
 * @author djabry
 */
public interface Record extends DJNodeObject{
    
    
    public static final String PROP_PARENT_TAPE5 = "parentTAPE5";
    public static final String PROP_LEVEL = "level";
    
    
    
    List<Field> getFields();
    Field getField(String fieldName);
    boolean isValid();
    DJTAPE5 getParentTAPE5();
    Record duplicate(RecordContainer parent, boolean isValid);
    String getRecordString();
    int getLevel();

    
    
}
