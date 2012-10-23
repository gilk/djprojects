/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter.api;

import org.dj.filter.api.Filter;
import org.dj.index.api.IntegerIndex;
import org.dj.index.api.IntegerIndices;
import org.openide.util.Lookup;

/**
 *
 * @author djabry
 */
public class DefaultFieldValue implements FieldValueSpecifier {

    private String recordName;
    private String fieldName;
    private IntegerIndex levels;
    private Object value;
    private static final IntegerIndices iI = Lookup.getDefault().lookup(IntegerIndices.class);
    
    public DefaultFieldValue(String recordName,String fieldName,IntegerIndex levels,Object value){
        this.recordName = recordName;
        this.fieldName = fieldName;
        this.levels = levels;
        this.value = value;
        
    }

    public DefaultFieldValue(String recordName, String fieldName, int level, Object value) {

        this(recordName,fieldName,iI.singleton(level),value);

    }
    
    public boolean isLevelValid(int level){

        if(this.getValidLevels().contains(-1)){
            
            return true;
        }
        
        if(this.getValidLevels().contains(level)){
            
            return true;
        }
        
        return false;
    }

    public IntegerIndex getValidLevels(){
        
        return this.levels;
    }
    
    public String getFieldName(){
        
        return this.fieldName;
    }
    
    public boolean isFieldNameValid(String fName){
        
        return this.getFieldName().equals(fName);
    }
    
    public String getRecordName(){
        
        return this.recordName;
    }
    
    public boolean isRecordNameValid(String rName){
        
        return this.getRecordName().equals(rName);
    }

    @Override
    public Object getValueForField(Field field) {

        return value;

    }

    @Override
    public boolean filter(Field field) {
            if (this.isFieldNameValid(field.getName())&&
                    this.isLevelValid(field.getLevel())&&
                    this.isRecordNameValid(field.getParentRecord().getName())) {
                return true;


        }

        return false;

    }
}
