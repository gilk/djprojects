/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.listener.api;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Map;
import javolution.util.FastMap;

/**
 *
 * @author djabry
 */
public class ListenerAbstr implements MultiListener {

    private PropertyChangeSupport pcs;
    private Map<Object,PropertyChangeSupport> parameteredPCSs;

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        //Remove all listeners from source upon receiving this message
        if(pce.getPropertyName().equals(MESSAGE_DELETED)){
            
            Object source = pce.getSource();
            
            if(source instanceof Listener){
                Listener listener = (Listener) source;
                listener.removePropertyChangeListener(this);
                
            }
        }
    }

    
    @Override
    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {

        if (pcs == null) {

            pcs = new PropertyChangeSupport(this);
        }

        pcs.addPropertyChangeListener(listener);


    }
    
    @Override
     public synchronized void addPropertyChangeListener(PropertyChangeListener listener, Object parameter){
        
        initPCSs();
        
        PropertyChangeSupport p = parameteredPCSs.get(parameter);
        
        
        if(p==null){
            p = new PropertyChangeSupport(this);
            this.parameteredPCSs.put(parameter, p);
        }
        
        p.addPropertyChangeListener(listener);
        
        
        
    }
    
    private void initPCSs(){
        
        if(parameteredPCSs==null){
            this.parameteredPCSs  = new FastMap<Object,PropertyChangeSupport>();
        }
    }
    
    
    
    
   

    @Override
    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {

        if (pcs != null) {
            pcs.removePropertyChangeListener(listener); 
        }
        
        
    }
    
    @Override
    public synchronized void removePropertyChangeListener(PropertyChangeListener listener, Object parameter){
        
        initPCSs();
        PropertyChangeSupport c = this.parameteredPCSs.get(parameter);
        if(c!=null){ 
            c.removePropertyChangeListener(listener);
        }
  
    }

    @Override
    public void firePropertyChange(String prop, Object o1, Object o2) {

        if (pcs != null) {

            pcs.firePropertyChange(prop, o1, o2);  
        }
    }
    
    @Override
    public void firePropertyChange(String prop, Object o1, Object o2, Object parameterIndex){
        
        initPCSs();
        PropertyChangeSupport c = this.parameteredPCSs.get(parameterIndex);
        
        if(c!=null){ 
            c.firePropertyChange(prop, o1, o2);
        }
        
    }

    
    @Override
    public void firePropertyChange(PropertyChangeEvent pCE,Object parameterIndex) {

       initPCSs();
        PropertyChangeSupport c = this.parameteredPCSs.get(parameterIndex);
        
        if(c!=null){ 
            c.firePropertyChange(pCE);
        }
    }

    @Override
    public PropertyChangeListener[] getPropertyChangeListeners() {

        if (pcs != null) {
            return pcs.getPropertyChangeListeners();
        }

        return null;

    }

    @Override
    public PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
        if (pcs != null) {
            return pcs.getPropertyChangeListeners(propertyName);
        }

        return null;
    }

    @Override
    public void addPropertyChangeListener(String propName, PropertyChangeListener listener) {
        
        if(pcs==null){
            
            pcs = new PropertyChangeSupport(this);
        }
        
        pcs.addPropertyChangeListener(propName,listener);
    }

    @Override
    public void removePropertyChangeListener(String propName, PropertyChangeListener listener) {
        
        if(pcs!=null){
            
            pcs.removePropertyChangeListener(propName,listener);
        }
         
    }
    
    

    @Override
    public PropertyChangeSupport getPropertyChangeSupport() {
        return pcs;
    }
    
    public PropertyChangeSupport getPropertyChangeSupport(Object parameter){
        
        initPCSs();
        return this.parameteredPCSs.get(parameter);
    }
    


    @Override
    public void firePropertyChange(PropertyChangeEvent pCE) {
        if(pcs!=null){
            
            this.pcs.firePropertyChange(pCE);
        }
    }
}
