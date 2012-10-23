/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter.api;


import java.util.List;
import java.util.Map;
import java.util.Set;
import org.lblrtm.file.api.TAPE6;
import org.lblrtm.lblrtmdata.api.LBLRTMOutputFile;

/**
 *
 * @author djabry
 */
public interface CalculationTemplate extends RunTemplate<CalculationTemplate>{
    
   
    public static final String RADIANCE_CALCULATION = "Radiance";
    public static final String AJ_CALCULATION = "Analytical Jacobian";
    
    public static final String PROP_LAYERING = "layering";
    //public static final String PROP_LAYER_BOUNDARIES = "layerBoundaries";
    public static final String PROP_CUSTOM_LAYER_BOUNDARIES  = "customLayerBoundaries";
    public static final String PROP_BOUNDARY_UNITS = "boundryUnits";
    
    public static final String PROP_RADIANCE_OUTPUT_UNITS = "radianceOutputUnits";
    public static final String PROP_JACOBIAN_OUTPUT_UNITS = "jacobianOutputUnits";
    
    public static final String PROP_WAVENUMBER_MARGIN = "wavenumberMargin";
    
    double getWavenumberMargin();
    void setWavenumberMargin(double margin);
    
    RadianceUnits getRadianceOutputUnits();
    void setRadianceOutputUnits(RadianceUnits rU);
    AJUnits getJacobianOutputUnits();
    void setJacobianOutputUnits(AJUnits aU);
    
    Layering getLayering();
    void setLayering(Layering l);
    
    int getNumberOfBoundaries();
    
    BoundaryUnits getBoundaryUnits();
    void setBoundaryUnits(BoundaryUnits bU);
    
    List<Double> getCustomLayerBoundaries();
    void setCustomLayerBoundaries(List<Double> bndrs);
    
    List<Double> getLayerBoundaries();
    //public void setLayerBoundaries(List<Double> boundries);
    
    List<LBLRTMOutputFile> getOutputFiles(TAPE6 t6);
    Set<String> getInputFiles();
    Map<String,String> getFilesToScan(TAPE6 t6);

}
