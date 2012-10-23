/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter.api;

import org.dj.filter.api.Filter;

/**
 *
 * @author djabry
 */
public interface FieldValueSpecifier extends Filter<Field> {
    
    Object getValueForField(Field f);

    
}
