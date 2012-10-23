/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmdata.api;

import java.util.List;

/**
 *
 * @author djabry
 */
public interface LBLRTMOutputFile {
    
    public static final LBLRTMOutputFile TAPE5 = new LBLRTMOutputFile(){

        @Override
        public String getName() {
            return "TAPE5";
        }

        @Override
        public List<VariableInfo> getVariables() {
            return null;
        }

    };
    
    public static final LBLRTMOutputFile TAPE7  = new LBLRTMOutputFile(){
        
         @Override
        public String getName() {
            return "TAPE7";
        }

        @Override
        public List<VariableInfo> getVariables() {
            return null;
        }
        
                
    };
    
    public static final LBLRTMOutputFile TAPE6 = new LBLRTMOutputFile(){

        @Override
        public String getName() {
            return "TAPE6";
        }

        @Override
        public List<VariableInfo> getVariables() {
            return null;
        }

    };
    
    String getName();
    List<VariableInfo> getVariables();
    
}
