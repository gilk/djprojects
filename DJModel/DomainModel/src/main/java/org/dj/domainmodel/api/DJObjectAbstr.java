/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.domainmodel.api;


import org.dj.filter.api.Filter;
import org.dj.idgenerator.api.IDGenerator;
import org.dj.property.api.DJProperty;
import org.dj.property.api.PropertiesAbstr;
import org.openide.util.Lookup;

/**
 *
 * @author djabry
 */
public abstract class DJObjectAbstr extends PropertiesAbstr implements DJObject{
   
    private static final IDGenerator idGen = Lookup.getDefault().lookup(IDGenerator.class);
    
    
    public DJObjectAbstr(){

        DJProperty nameProp = getPropertyFactory().createProperty(PROP_NAME,"");
        nameProp.setDisplayName("Name");
        nameProp.setFilter(Filter.STRING);
        nameProp.setNotify(true);
        putProperty(nameProp); 
        
        DJProperty descripProp = getPropertyFactory().createProperty(PROP_DESCRIPTION,"");
        descripProp.setDisplayName("Description");
        descripProp.setFilter(Filter.STRING);
        descripProp.setNotify(true);
        putProperty(descripProp);

        DJProperty idProp = getPropertyFactory().createProperty(PROP_ID, idGen.generateID());
        idProp.setDisplayName("Identification");
        idProp.setCanRead(false);
        idProp.setFilter(Filter.STRING);
        
        putProperty(idProp);

    }

  

    @Override
    public String getId() {
        
        return (String) getPropertyValue(PROP_ID);
    }
    
    public void setId(String id){
        
        this.setPropertyValue(PROP_ID, id);
    }

    @Override
    public String getName() {
        return (String) getPropertyValue(PROP_NAME);
    }
    
    @Override
    public void setName(String name){
        
        this.setPropertyValue(PROP_NAME, name);
        
    }

    @Override
    public String getDescription() {
        return (String) getPropertyValue(PROP_DESCRIPTION);
    }
    
    
    @Override
    public void setDescription(String description){
        this.setPropertyValue(PROP_DESCRIPTION, description);
    }
 
}
