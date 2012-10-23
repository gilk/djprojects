/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dj.listener.api;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * A wrapper interface of {@link PropertyChangeListener}
 * @author djabry
 */
public interface MultiListener extends Listener {
    
    //void addPropertyChangeListener(PropertyChangeListener listener);
    
    void addPropertyChangeListener(PropertyChangeListener listener, Object parameterIndex);
    
    //void removePropertyChangeListener(PropertyChangeListener listener);
    void removePropertyChangeListener(PropertyChangeListener listener, Object parameterIndex);
            
    //void firePropertyChange(String prop, Object o1, Object o2);
    void firePropertyChange(String prop, Object o1, Object o2, Object parameterIndex);
    
    
    //void firePropertyChange(PropertyChangeEvent pCE);
    void firePropertyChange(PropertyChangeEvent pCE, Object ParameterIndex);
    
    
    //PropertyChangeListener[] getPropertyChangeListeners();
    
    //PropertyChangeListener[] getPropertyChangeListeners(String propertyName);
    
    //void addPropertyChangeListener(String propName,PropertyChangeListener listener);
    //void removePropertyChangeListener(String propName,PropertyChangeListener listener);
    
    //PropertyChangeSupport getPropertyChangeSupport();

}
