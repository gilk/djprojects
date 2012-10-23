/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.filter.api;

import java.io.File;

/**
 *
 * @author djabry
 */
public interface  Filter<T> {
    
    boolean filter(T obj);
    
    
    public static final Filter STRING = new Filter(){

        @Override
        public boolean filter(Object obj) {
            if(obj instanceof String){
                
                return true;
            }
            
            return false;
        }
        
    };
    
    public static final Filter NUMBER = new Filter(){

        @Override
        public boolean filter(Object obj) {
            if (obj instanceof Number){
                
                return true;
            }
            
            return false;
        }
        
        
    };
    
    
    public static final Filter DOUBLE = new Filter(){

        @Override
        public boolean filter(Object obj) {
            if(obj instanceof Double){
                
                return true;
            }
            
            return false;
        }
        
        
    };
    
    public static final Filter LONG = new Filter(){

        @Override
        public boolean filter(Object obj) {
            if(obj instanceof Long){
                
                return true;
            }
            
            return false;
        }
        
        
        
    };
    
    public static final Filter FILE = new Filter(){

        @Override
        public boolean filter(Object obj) {
            if(obj instanceof File){
                
                return true;
            }
            
            return false;
        }
        
        
    };
    
    public static final Filter BOOLEAN = new Filter(){

        @Override
        public boolean filter(Object obj) {
            if(obj instanceof Boolean){
                
                return true;
            }
            
            return false;
        }
  
    };
    
    public static final Filter INTEGER = new Filter(){

        @Override
        public boolean filter(Object obj) {
            if(obj instanceof Integer){
                
                
                return true;
            }
            
            return false;
        }
        
        
        
    };

    
}
