/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.idgenerator.api;

import org.dj.filter.api.Filter;



/**
 *
 * @author djabry
 */
public interface ObjectIDGenerator extends Filter{
    
     Object generateID(Object obj);
    
}
