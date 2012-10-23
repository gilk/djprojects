/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter.api;

import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author djabry
 */
public abstract class DefaultFieldValueModifierAbstr implements FieldValueModifier{
    
    public abstract Set<DefaultFieldValue> getDefaultFieldValues();

    @Override
    public Object getValueForField(Field field) {
        
        Iterator<DefaultFieldValue> iterator = getDefaultFieldValues().iterator(); 
        while(iterator.hasNext()){
            
            DefaultFieldValue v = iterator.next();   
            if(v.filter(field)){
                
                return v.getValueForField(field);
            }
        }     
        return null; 
    }
    
}
