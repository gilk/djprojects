/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter.api;

import org.dj.domainmodel.api.DJNodeObject;

/**
 *
 * @author djabry
 */
public interface Field extends DJNodeObject{
    
    public static final String PROP_VALUE = "value";
    public static final String PROP_FORMAT = "format";
    public static final String PROP_SPACES = "spaces";
    public static final String PROP_WIDTH = "width";
    public static final String PROP_TEXT = "text";
    public static final String PROP_DEFAULT = "default";
    public static final String PROP_PARENT_RECORD = "parentRecord";
    public static final String PROP_FIELD_ORDER = "fieldOrder";
    
    Object getValue();
    int getLevel();
    Object getValueFor(Object obj);
    void setValue(Object value);
    String getValueString();
    int getSpaces();
    int getWidth();
    Format getFormat();
    String getText();
    boolean isDefault();
    Record getParentRecord();
    int getFieldOrder();
    Field duplicate(Record parentRecord);
    
    
}
