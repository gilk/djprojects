/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.file.api;

import java.io.Closeable;
import java.io.File;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;
import org.lblrtm.lblrtmdata.api.AJParameter;
import org.lblrtm.profile.api.Profile;

/**
 *
 * @author djabry
 */
public interface TAPE6 extends Closeable{
    
    Profile getOutputProfile();
    
    //Profile getInputProfile();
    File getFile();
    Iterator<File> getOutputFiles();
    boolean isAnalyticalJacobian();
    boolean failed();
    double getAngle();
    //int getAJParameter();
    //String getAJParameterString();
    
    EnumSet<AJParameter> getAJParameters();
    boolean getDirection();
    
    public static final String RADIANCE_OUTPUT_FILE = "TAPE12";
    public static final String TAPE6_NAME = "TAPE6";
}
