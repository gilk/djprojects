/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter;

import java.util.Arrays;
import java.util.List;
import javolution.util.FastList;
import org.dj.property.api.DJProperty;
import org.dj.property.api.PropertyAbstr;
import org.lblrtm.lblrtmfilewriter.api.*;

/**
 *
 * @author djabry
 */
public class FFTScannerImpl extends ScannerTemplateAbstr implements FFTScanner {


    private final ScannerFVM iFVM = new ScannerFVM();

    private static final List<String> extraPropNames = Arrays.asList(new String[]{
       
        PROP_SCANNING_FUNCTION,
        PROP_HWHM_RATIO,
        PROP_CALCULATION_TYPE,
        PROP_BOXCAR_DECONVOLUTION,
        PROP_PARAM1,
        PROP_PARAM2,
        PROP_PARAM3
        
    });
    

    public FFTScannerImpl() {

        super("FFT scanner");
        
        DJProperty sFProp = getPropertyFactory().createProperty(PROP_SCANNING_FUNCTION, ScanningFunction.BOXCAR);
        sFProp.setDisplayName("Scanning function");
        putProperty(sFProp);
        
        DJProperty hWProp = getPropertyFactory().createProperty(PROP_HWHM_RATIO, -1);
        hWProp.setDisplayName("HWHM ratio");
        putProperty(hWProp);
        
        
        DJProperty<CalculationType> cTProp = getPropertyFactory().createProperty(PROP_CALCULATION_TYPE, CalculationType.AUTO);
        cTProp.setDisplayName("Calculation type");
        putProperty(cTProp);
        
        
        DJProperty<Boolean> bDProp = getPropertyFactory().createProperty(PROP_BOXCAR_DECONVOLUTION, false);
        bDProp.setDisplayName("Boxcar deconvolution");
        putProperty(bDProp);

        DJProperty<Double> p1Prop = new PropertyAbstr<Double>(PROP_PARAM1, 0.0){

            @Override
            public boolean isCanWrite() {
                return Math.abs(getScanningFunction().index)>10;
            }

            @Override
            public String getDisplayName() {
                
                String name = "Parameter 1";
                ScanningFunction sF = getScanningFunction();
                
                if(sF.equals(ScanningFunction.BRAULT)||sF.equals(ScanningFunction.KAISER_BESSEL)){
                    
                    name = "p";
                }else if(sF.equals(ScanningFunction.KIRUNA)){
                    
                    name = "v offset";
                }
                
                return name;
            }

        };

        
        putProperty(p1Prop);
        
        DJProperty<Double> p2Prop = new PropertyAbstr<Double>(PROP_PARAM2, 0.0){
            @Override
            public boolean isCanWrite() {
                return Math.abs(getScanningFunction().index)==13;
            }
            
            @Override
            public String getDisplayName() {
                
                String name = "Parameter 2";
                ScanningFunction sF = getScanningFunction();
                
                if(sF.equals(ScanningFunction.KIRUNA)){
                    
                    name = "C1";
                }
                
                return name;
            }
        };
                
        putProperty(p2Prop);
        
        DJProperty<Double> p3Prop = new PropertyAbstr<Double>(PROP_PARAM3, 0.0){
             @Override
            public boolean isCanWrite() {
                return Math.abs(getScanningFunction().index)==13;
            }
             
             @Override
            public String getDisplayName() {
                
                String name = "Parameter 3";
                ScanningFunction sF = getScanningFunction();
                
                if(sF.equals(ScanningFunction.KIRUNA)){
                    
                    name = "C2";
                }
                
                return name;
            }
            
        };

        putProperty(p3Prop);

        
        
    }


    

    @Override
    public ScanningFunction getScanningFunction() {
        return (ScanningFunction) this.getPropertyValue(PROP_SCANNING_FUNCTION);
    }

    @Override
    public void setScanningFunction(ScanningFunction sF) {
       this.setPropertyValue(PROP_SCANNING_FUNCTION, sF);
    }

    @Override
    public int getHWHMRatio() {
        return (Integer)this.getPropertyValue(PROP_HWHM_RATIO);
    }

    @Override
    public void setHWHMRatio(int ratio) {
        this.setPropertyValue(PROP_HWHM_RATIO, ratio);
    }

    @Override
    public CalculationType getCalculationType() {
        return (CalculationType) this.getPropertyValue(PROP_CALCULATION_TYPE);
    }

    @Override
    public void setCalculationType(CalculationType cT) {
        this.setPropertyValue(PROP_CALCULATION_TYPE, cT);
    }

    @Override
    public boolean isBoxcarDeconvolution() {
        return (Boolean)this.getPropertyValue(PROP_BOXCAR_DECONVOLUTION);
    }

    @Override
    public void setBoxcarDeconvolution(boolean de) {
        this.setPropertyValue(PROP_BOXCAR_DECONVOLUTION, de);
    }

    @Override
    public double getParam1() {
       return (Double)this.getPropertyValue(PROP_PARAM1);
       
    }

    @Override
    public void setParam1(double val) {
        this.setPropertyValue(PROP_PARAM1, val);
    }

    @Override
    public double getParam2() {
        return (Double)this.getPropertyValue(PROP_PARAM2);
    }

    @Override
    public void setParam2(double val) {
        this.setPropertyValue(PROP_PARAM2, val);
    }

    @Override
    public double getParam3() {
        return (Double)this.getPropertyValue(PROP_PARAM3);
    }

    @Override
    public void setParam3(double val) {
       this.setPropertyValue(PROP_PARAM3, val);
    }

    @Override
    public ScannerTemplate duplicate() {
        FFTScanner fS = new FFTScannerImpl();
        fS.importPropertiesFrom(this);
        return fS;
        
    }

    @Override
    public String getRecordName() {
       return "10.1";
    }

   

    private class ScannerFVM implements FieldValueModifier {

        @Override
        public Object getValueForField(Field field) {

            String rName = field.getParentRecord().getName();
            String fName = field.getName();

            if (rName.equals("1.2")) {

                if (fName.equals("ISCAN")) {

                    //FFT Scan 
                    return 3;
                }

                return 0;
            }
            
            if (rName.equals("10.1")) {

                if (fName.equals("HWHM")) {

                    //Max OPD
                    return getHWHM();
                }
                
                 if (fName.equals("JEMIT")) {

                    //Max OPD
                    return getConvolutionType().index;
                }

           

                if (fName.equals("JFNin")) {

                    int fac = -1;
                    return fac*getScanningFunction().index;
                    
                }

           

                if (fName.equals("MRATin")) {

                    //Ratio of halfwidth of scanning function to boxcar
                    return getHWHMRatio();
                }

            

                if (fName.equals("IVX")) {

                    
                    return getCalculationType().index;
                }

            

                if (fName.equals("NOFIX")) {

                    if(isBoxcarDeconvolution()){
                        
                        return 0;
                    }
                    
                    return 1;
                    
                }

            }

            if (rName.equals("10.2")) {

                if (fName.equals("PARAM1")) {

                    //Kaiser 'p' parameter
                    return getParam1();
                }
           

                if (fName.equals("PARAM2")) {

                    //Kaiser 'p' parameter
                    return getParam2();
                }
            

                if (fName.equals("PARAM3")) {

                    //Kaiser 'p' parameter
                    return getParam3();
                }
            }


            return null;
        }
    }

    @Override
    public FieldValueModifier getFieldValueModifier() {

        return iFVM;
    }

    @Override
    public List<String> getPropNames() {
        
        List<String> propNames = new FastList<String>();
        propNames.addAll(super.getPropNames());
        propNames.addAll( extraPropNames);
        return propNames;
    }
    
    

   
}
