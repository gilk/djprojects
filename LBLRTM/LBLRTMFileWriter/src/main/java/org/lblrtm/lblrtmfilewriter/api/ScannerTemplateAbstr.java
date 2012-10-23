/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter.api;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javolution.util.FastList;
import org.dj.property.api.DJProperty;

/**
 *
 * @author djabry
 */
public abstract class ScannerTemplateAbstr extends RunTemplateAbstr<ScannerTemplate> implements ScannerTemplate{
    
    private static final RecordRepetitionSpecifier rR = new RecordM();
    private static final List<String> extraPropsToAdd  = Arrays.asList(new String[]{
        PROP_HWHM,PROP_CONVOLUTION_TYPE
    });
    

    @Override
     public double getHWHM(){
         
         return (Double)this.getPropertyValue(PROP_HWHM);
     }

    @Override
    public void setHWHM(double hWHM) {
       this.setPropertyValue(PROP_HWHM, hWHM);
    }
    
    

    @Override
    public void setConvolutionType(ConvolutionType cType) {
       this.setPropertyValue(PROP_CONVOLUTION_TYPE, cType);
    }
    
    
    
    @Override
    public ConvolutionType getConvolutionType(){
        
        return (ConvolutionType) this.getPropertyValue(PROP_CONVOLUTION_TYPE);
    }
    
    public abstract String getRecordName();
    
    
    public ScannerTemplateAbstr(String name){
        super(name);
        
        DJProperty hWHMProp = getPropertyFactory().createProperty(PROP_HWHM, 1.0);
        hWHMProp.setDisplayName("Half width half maximum");
        putProperty(hWHMProp);
        
        
        DJProperty<ConvolutionType> cTProp = getPropertyFactory().createProperty(PROP_CONVOLUTION_TYPE, ConvolutionType.RADIANCE);
        cTProp.setDisplayName("Convolution type");
        putProperty(cTProp);
        
        Set<DefaultFieldValue> fieldValues = this.getFieldValues();
        
        DefaultFieldValue hWHMVal = new DefaultFieldValue(getRecordName(), "HWHM", -1, null){

            @Override
            public Object getValueForField(Field field) {
                return getHWHM();
            }
        };
        
        fieldValues.add(hWHMVal);
        
        DefaultFieldValue cvType = new DefaultFieldValue(getRecordName(),"JEMIT",-1,null){
            
             @Override
            public Object getValueForField(Field field) {
                return getConvolutionType().index;
            }
            
        };
        
        fieldValues.add(cvType);
        
        
        
        
        

    }

   
    @Override
    public RecordRepetitionSpecifier getRecordRepetitionSpecifier() {
        return rR;
    }

    

    @Override
    public List<String> getPropNames() {
       
       List<String> names = new FastList<String>();
       names.addAll(super.getPropNames());
       names.addAll(extraPropsToAdd);
       
       return names;
    }

    
    
    
    private static class RecordM implements RecordRepetitionSpecifier{

        @Override
        public Integer getNumberOfRepetitionsForRecord(Record record) {
            if(record.getName().equals("All records")){
                
                return 1;
            }
            
            return null;
        }   
    }
    
}
