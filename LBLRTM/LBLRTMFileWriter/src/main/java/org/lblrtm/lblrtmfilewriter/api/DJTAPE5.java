/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter.api;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import org.lblrtm.lblrtmdata.api.*;
import org.lblrtm.profile.api.CustomLevels;
import org.lblrtm.profile.api.Profile;

/**
 *
 * @author djabry
 */
public interface DJTAPE5 extends TAPE5, RecordContainer, Profile.Provider{
    
    Record getRecord(String name);
    Field getField(String recordName, String fieldName);
   
 
    CalculationTemplate getCalculationTemplate();
    ScannerTemplate getScannerTemplate();
    
    void setScannerTemplate(ScannerTemplate sT);
    void setCalculationTemplate(CalculationTemplate cT);
    
    void setRunTemplate(RunTemplate rT);
    RunTemplate getRunTemplate();
    
    Set<LBLRTMOutputFile> getAllOutputFiles();
    
    Surface getSurface();
    Observer getObserver();
    
    
    AtmosphericModel getAtmosphericModel();
    void setAtmosphericModel(AtmosphericModel m);
    
    
    //CustomLevels getCustomLevels();
    
    boolean getDirection();
    
    //@Deprecated
    //Use setAngle instead
    //void setDirection(boolean direction);
    
    
    void setAngle(double angle);
    
    DJTAPE5 copy(boolean linkedProfile);
    
    double getEndHeight();
    void setEndHeight(double endHeight);

    File writeToFile(File f) throws IOException;
    //DJTAPE5 createScannerTAPE5();
    
    
    public static final String SCANNER_FILENAME = "scanner.TAPE5";
    
    public static final String CALCULATOR_FILENAME = "test.TAPE5";
    
    public static final String PROP_SCANNER_TEMPLATE = "scannerTemplate";
    public static final String PROP_V1 = "v1";
    public static final String PROP_V2 = "v2";
    public static final String PROP_DV = "dv";
    public static final String PROP_END_HEIGHT ="endHeight";
    public static final String PROP_ATMOSPHERIC_MODEL = "atmopshericModel";
    public static final String PROP_CALCULATION_TEMPLATE  = "calculationTemplate";
    public static final String PROP_RUN_STRING = "runString";
    public static final String PROP_PROFILE = "profile";
    public static final String PROP_CUSTOM_LEVELS = "customLevels";
    public static final String PROP_SURFACE="surface";
    public static final String PROP_OBSERVER = "observer";
    public static final String PROP_ANGLE = "angle";
    public static final String PROP_RUN_TEMPLATE = "runTemplate";
    public static final String PROP_DIRECTION = "direction";
    
    
    
}
