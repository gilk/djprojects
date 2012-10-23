/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.filter.api;

/**
 *
 * @author djabry
 */
public interface DynamicFilter extends Filter{
    
    void setFilter(Filter filter);
    
    public static final String PROP_FILTER = "filter";
    
}
