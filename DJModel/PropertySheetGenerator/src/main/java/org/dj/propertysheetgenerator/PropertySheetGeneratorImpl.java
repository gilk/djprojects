/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.propertysheetgenerator;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.dj.domainmodel.api.DJObject;
import org.dj.property.api.DJProp;
import org.dj.property.api.DJProperty;
import org.dj.propertysheetgenerator.api.PropertySheetGenerator;
import org.openide.nodes.Node.Property;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */

//@ServiceProvider (service = PropertySheetGenerator.class)
public class PropertySheetGeneratorImpl implements PropertySheetGenerator{
    
    private static final List<String> GETTER_PREFIXES  = Arrays.asList(new String[]{
    "get","is"});
    
    private String generatePropertyName(Method m, DJProp prop){
        
        String propertyName = "";
        
        if(!prop.propertyName().isEmpty()){
            
            propertyName= prop.propertyName();
        
        }else{
            
             //If it is a standard getter method
             String methodName = m.getName();
             
             for(String prefix:GETTER_PREFIXES){
                 
                 if(methodName.startsWith(prefix)){
                     
                     propertyName = methodName.substring(prefix.length());
                     String firstLetter = propertyName.substring(0, 1);

                     propertyName = propertyName.replaceFirst(firstLetter,firstLetter.toLowerCase());
                     
                     return propertyName;

                 }
                 
             }
             
            
        }
        
        return propertyName;  
    }
    
    private Property createProperty(Object obj, DJProp prop, Method m){

        Property rProp=null;

        try {

            if (prop.canRead()) {

                if (prop.canWrite()) {
                    
                    
                    rProp = new PropertySupport.Reflection(obj,m.getReturnType(),generatePropertyName(m,prop));


                } else {

                    rProp = new PropertySupport.Reflection(obj,m.getReturnType(),m,null);

                }
                
                rProp.setShortDescription(prop.description());
                rProp.setDisplayName(prop.displayName());

            }




        } catch (NoSuchMethodException ex) {
            //Exceptions.printStackTrace(ex);
        }

        return rProp;
  
    }

    @Override
    public Sheet generatePropertySheet(DJObject obj) {
        
        Sheet sheet = Sheet.createDefault();
        
        Sheet.Set setProps = Sheet.createPropertiesSet();
        setProps.setDisplayName("Properties");
        sheet.put(setProps);
        
        for(Method m: obj.getClass().getMethods()){
            DJProp prop = m.getAnnotation(DJProp.class);
            
            if(prop!=null){
                
                Property rProp = this.createProperty(obj, prop, m);

            if (rProp != null) {

                if (rProp.getName().equals(DJObject.PROP_DESCRIPTION)) {

                    Sheet.Set setDescr = new Sheet.Set();
                    setDescr.setName("description");
                    setDescr.setValue("tabName", " Description ");
                    setDescr.setShortDescription("Description");
                    setDescr.put(rProp);
                    sheet.put(setDescr);

                } else {

                    setProps.put(rProp);
                }
            } 
 
            }
        }
        
        return sheet;
    }
    
}
