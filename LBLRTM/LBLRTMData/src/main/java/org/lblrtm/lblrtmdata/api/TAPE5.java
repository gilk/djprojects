/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmdata.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author djabry
 */
public interface TAPE5 {
    
    double getDV();
    void setDV(double dv);
    
    double getV1();
    double getV2();
    
    void setV1(double v1);
    void setV2(double v2);
    
    double getAngle();
    
    
    File writeToDir(File dir) throws IOException;
    InputStream getInputStream() throws IOException;
    
    //File writeScannerToDir(File dir);
    //FileInputStream getScannerInputStream();
            
    TAPE5 duplicate();
    String getRunString();
    
}
