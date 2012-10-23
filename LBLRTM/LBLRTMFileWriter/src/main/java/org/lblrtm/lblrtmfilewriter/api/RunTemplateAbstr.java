/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter.api;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javolution.util.FastList;
import javolution.util.FastSet;
import org.dj.domainmodel.api.DJNodeObjectAbstr;
import org.dj.property.api.DJProperty;

/**
 *
 * @author djabry
 */
public abstract class RunTemplateAbstr<R extends RunTemplate> extends DJNodeObjectAbstr implements RunTemplate<R>{
    private final String n;
    private final Set<DefaultFieldValue> defaultFieldValues = new FastSet<DefaultFieldValue>();
    
    private final FieldValueModifier fVM = new DefaultFieldValueModifierAbstr(){

        @Override
        public Set<DefaultFieldValue> getDefaultFieldValues() {
            return getFieldValues();
        }
        
        
    };
    
    public Set<DefaultFieldValue> getFieldValues(){
        
        return this.defaultFieldValues;
    }
    
    
    public RunTemplateAbstr(String name) {
        
        super();
        this.setName(name);
        this.n=name;
        this.getProperty(PROP_NAME).setCanRead(false);
        this.getProperty(PROP_DESCRIPTION).setCanRead(false);

    }
    

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public String getName() {
        return this.n;
    }
    
    
    public List<String> getPropNames(){
        
        return new FastList<String>();
    }
    
   public DJTAPE5 getParentTAPE5(){
        
        return (DJTAPE5)this.getParent();
    }
    
    @Override
    public List<DJProperty> getPropertiesToEdit(){
        
        List<DJProperty> propsToEdit = new FastList<DJProperty>();
        Iterator<String> iterator = getPropNames().iterator();
        
        while(iterator.hasNext()){
            String next = iterator.next();
            propsToEdit.add(getProperty(next));
        }
        
        return propsToEdit;
        
        
        
        
    }

    @Override
    public void importPropertiesFrom(RunTemplate rT) {
        if (rT!=null) {
            Iterator<DJProperty> iterator = rT.getPropertiesToEdit().iterator();
            while (iterator.hasNext()) {
                DJProperty next = iterator.next();
                if (this.getProperty(next.getPropertyName()) != null) {
                    this.setPropertyValue(next.getPropertyName(), next.getValue());
                }
            }

        }
    }

    @Override
    public FieldValueModifier getFieldValueModifier() {
        return this.fVM;
    }
    
    
    
    
}
