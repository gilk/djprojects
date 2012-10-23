/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.parameter.api;

import java.util.Collection;
import org.dj.listener.api.MultiListener;

/**
 *
 * @author djabry
 */
public interface Parameters extends MultiListener{
    
    Object getValue(String key);
    boolean setValue(String key, Object value);
    Collection<Object> getAllValues();
    boolean isNotify();
    void setNotify(boolean notify);
    
}
