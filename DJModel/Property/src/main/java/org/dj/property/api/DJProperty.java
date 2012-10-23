/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.property.api;

import java.beans.PropertyEditor;
import org.dj.filter.api.Filter;
import org.dj.listener.api.MultiListener;

/**
 *
 * @author djabry
 */
public interface DJProperty<O> extends MultiListener{
    
    String getPropertyName();
    String getDisplayName();
    String getShortDescription();
    
    public static final String PROP_PROPERTY_NAME = "propertyName";
    
    
    public static final String PROP_DESCRIPTION = "description";
    void setDescription(String description);
    
    public static final String PROP_SHORT_DESCRIPTION = "shortDescription";
    void setShortDescription(String shortDescription);
 
    public static final String PROP_DISPLAY_NAME = "displayName";
    void setDisplayName(String displayName);
    
    public static final String PROP_DISPLAY_VALUE = "displayValue";
    <T extends Object> T getDisplayValue();
    <T extends Object> void setDisplayValue(T value);
    
    public static final String PROP_PROPERTY_EDITOR_CLASS = "propertyEditorClass";
    PropertyEditor getPropertyEditor();
    <P extends PropertyEditor> Class<P> getPropertyEditorClass();
    <P extends PropertyEditor> void setPropertyEditorClass(Class<P> propEditorClass);

    String getDescription();
    
    O getValue();
    
    public static final String PROP_VALUE = "value";
    
    boolean setValue(O value);
    
    boolean isCanRead();
    boolean isCanWrite();
    
    public static final String PROP_CAN_READ = "canRead";
    void setCanRead(boolean tf);
    
    public static final String PROP_CAN_WRITE = "canWrite";
    void setCanWrite(boolean tf);
    
    public static final String PROP_FILTER = "filter";
    void setFilter(Filter<O> filter);
    
    boolean isNotify();
    void setNotify(boolean notify);
    
    
    
}
