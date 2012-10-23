/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.parameter.api;

import org.dj.filter.api.Filter;

/**
 *
 * @author djabry
 */
public interface FilteredParameters extends Parameters{
    
    void setFilter(String key,Filter filter);
    
    public static final String PROP_FILTER_CHANGE = "filter";
    
}
