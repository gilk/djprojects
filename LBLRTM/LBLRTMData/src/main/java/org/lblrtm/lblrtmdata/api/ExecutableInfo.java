/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmdata.api;

/**
 *
 * @author djabry
 */
public interface ExecutableInfo {
    
    public static final String EXECUTABLE_NAME = "executableName";
    public static final String EXECUTABLE_PATH = "executablePath";

    String getExecutableName();
    String getExecutablePath();
    
}
