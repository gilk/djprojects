/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter.api;

import java.util.Collections;
import java.util.List;
import javolution.util.FastList;
import org.dj.property.api.DJProperty;
import org.lblrtm.profile.api.PhysicalProperty;
import org.lblrtm.profile.api.Profile;

/**
 *
 * @author djabry
 */
//@ServiceProvider (service = CalculationTemplate.class)
public abstract class CalculationTemplateAbstr extends RunTemplateAbstr<CalculationTemplate> implements CalculationTemplate {
    //private static final CalculationTemplates cTs = Lookup.getDefault().lookup(CalculationTemplates.class);
    
    private static final List<String> propNames;
    
    public CalculationTemplateAbstr(String name){
        
        super(name);
        
        DJProperty<BoundaryUnits> bUProp = getPropertyFactory().createProperty(CalculationTemplate.PROP_BOUNDARY_UNITS, BoundaryUnits.ALTITUDE);
        bUProp.setDisplayName("Boundary units");
        putProperty(bUProp);
        
        DJProperty<Layering> layeringProp = getPropertyFactory().createProperty(CalculationTemplate.PROP_LAYERING, Layering.AUTO);
        layeringProp.setDisplayName("Layering type");
        putProperty(layeringProp);
        
        
        DJProperty<FastList<Double>> lBoundProps = getPropertyFactory().createProperty(CalculationTemplate.PROP_CUSTOM_LAYER_BOUNDARIES, new FastList<Double>());
        lBoundProps.setDisplayName("Layer boundaries");
        lBoundProps.setCanRead(false);
        putProperty(lBoundProps);
        
        DJProperty<RadianceUnits> radUProp = getPropertyFactory().createProperty(PROP_RADIANCE_OUTPUT_UNITS, RadianceUnits.RADIANCE);
        radUProp.setDisplayName("Spectrum ouptut units");
        putProperty(radUProp);
        
        DJProperty<AJUnits> jacUProp = getPropertyFactory().createProperty(PROP_JACOBIAN_OUTPUT_UNITS,AJUnits.D_RAD_D_LN_MR);
        jacUProp.setDisplayName("Jacobian output units");
        putProperty(jacUProp);
        
        
        DJProperty<Double> marginProp = getPropertyFactory().createProperty(PROP_WAVENUMBER_MARGIN, 25.0);
        marginProp.setDisplayName("Wavenumber margin");
        putProperty(marginProp);

    }
    
    static{
        

        propNames = new FastList<String>();
        propNames.add(PROP_BOUNDARY_UNITS);
        propNames.add(PROP_LAYERING);
        propNames.add(PROP_CUSTOM_LAYER_BOUNDARIES);
        propNames.add(PROP_RADIANCE_OUTPUT_UNITS);
        propNames.add(PROP_JACOBIAN_OUTPUT_UNITS);
        propNames.add(PROP_WAVENUMBER_MARGIN);
    }

    @Override
    public double getWavenumberMargin() {
        return (Double)this.getPropertyValue(PROP_WAVENUMBER_MARGIN);
    }

    @Override
    public void setWavenumberMargin(double margin) {
        this.setPropertyValue(PROP_WAVENUMBER_MARGIN, margin);
    }


    @Override
    public RadianceUnits getRadianceOutputUnits() {
        return (RadianceUnits) this.getPropertyValue(PROP_RADIANCE_OUTPUT_UNITS);
    }

    @Override
    public AJUnits getJacobianOutputUnits() {
        return (AJUnits) this.getPropertyValue(PROP_JACOBIAN_OUTPUT_UNITS);
    }

    @Override
    public void setJacobianOutputUnits(AJUnits aU) {
        this.setPropertyValue(PROP_JACOBIAN_OUTPUT_UNITS, aU);
    }

    @Override
    public void setRadianceOutputUnits(RadianceUnits rU) {
        this.setPropertyValue(PROP_RADIANCE_OUTPUT_UNITS, rU);
    }

    @Override
    public List<String> getPropNames() {
        return Collections.unmodifiableList(propNames);
    }
    
    
    
    
    
    @Override
    public List<Double> getCustomLayerBoundaries(){
        return (List<Double>) this.getPropertyValue(PROP_CUSTOM_LAYER_BOUNDARIES);

    }

    @Override
    public int getNumberOfBoundaries() {
        Layering l = this.getLayering();
        int n = 0;
        if(l.equals(Layering.PROFILE)){
            
            n = getParentTAPE5().getProfile().getNumberOfLevels();
        }else if(l.equals(Layering.MANUAL)){
            
            n= this.getCustomLayerBoundaries().size();
        }
        
        return n;
    }
    
    
    
    @Override
    public void setCustomLayerBoundaries(List<Double> bndrs){
        
        this.setPropertyValue(PROP_CUSTOM_LAYER_BOUNDARIES, bndrs);
    }

    @Override
    public List<Double> getLayerBoundaries() {
        List<Double> boundaries = this.getCustomLayerBoundaries();
        Layering layering = this.getLayering();
                
        if(layering.equals(Layering.PROFILE)){
            
            Profile p = this.getParentTAPE5().getProfile();
            
            boundaries = new FastList<Double>();
            String varName = PhysicalProperty.ALTITUDE.name();
            
            if(this.getBoundaryUnits().equals(BoundaryUnits.PRESSURE)){
                
                varName = PhysicalProperty.PRESSURE.name();
                
            }
            
            for(int i  = 0;i<p.getNumberOfLevels();i++){
                
                boundaries.add(p.getValueForVariable(varName, i));
            }
        }

        
        return boundaries;
    }

//    @Override
//    public void setLayerBoundaries(List<Double> boundries) {
//       setPropertyValue(PROP_LAYER_BOUNDARIES,boundries);
//    }

    @Override
    public BoundaryUnits getBoundaryUnits() {
       return (BoundaryUnits) this.getPropertyValue(PROP_BOUNDARY_UNITS);
    }

    @Override
    public void setBoundaryUnits(BoundaryUnits bU) {
        this.setPropertyValue(PROP_BOUNDARY_UNITS, bU);
    }

    @Override
    public Layering getLayering() {

      return (Layering) this.getPropertyValue(PROP_LAYERING);
    }

    @Override
    public void setLayering(Layering l) {
        this.setPropertyValue(PROP_LAYERING, l);
    }
    
    
    
    
    


}
