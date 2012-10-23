/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter;

import org.lblrtm.lblrtmfilewriter.api.Field;
import org.lblrtm.lblrtmfilewriter.api.FieldValueModifier;
import org.lblrtm.lblrtmfilewriter.api.ScannerTemplate;
import org.lblrtm.lblrtmfilewriter.api.ScannerTemplateAbstr;


/**
 *
 * @author djabry
 */
public class InterpolationScanner extends ScannerTemplateAbstr implements ScannerTemplate{
    
    private final IntepolationFVM iFVM = new IntepolationFVM();
    
    
    public InterpolationScanner(){
        
        super("Interpolation scanner");
        
    }

    
   
    @Override
    public ScannerTemplate duplicate() {
        InterpolationScanner iS = new InterpolationScanner();
        iS.importPropertiesFrom(this);
        return iS;
        
    }

    @Override
    public String getRecordName() {
       return "9.1";
    }
    
    
    
    private class IntepolationFVM implements FieldValueModifier {

        @Override
        public Object getValueForField(Field field) {
            
            if(field.getParentRecord().getName().equals("1.2")){
                
                if(field.getName().equals("ISCAN")){
                    
                    return 2;
                }
                
                return 0;
            }
            return null;
        }
    }

    @Override
    public FieldValueModifier getFieldValueModifier() {
        
        return iFVM;
    }


    
    
}
