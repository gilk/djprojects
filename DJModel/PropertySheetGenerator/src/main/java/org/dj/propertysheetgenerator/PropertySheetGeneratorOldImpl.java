/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.propertysheetgenerator;

import org.dj.domainmodel.api.DJObject;
import org.dj.property.api.DJProperty;
import org.dj.propertysheetgenerator.api.PropertySheetGenerator;
import org.openide.nodes.Node.Property;
import org.openide.nodes.Sheet;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider (service = PropertySheetGenerator.class)
public class PropertySheetGeneratorOldImpl implements PropertySheetGenerator {
    
    
    
    private Property createProperty(DJProperty prop) {

        return new RWProp(prop);

    }

    @Override
    public Sheet generatePropertySheet(DJObject obj) {
 
        Sheet sheet = Sheet.createDefault();
        
        Sheet.Set setProps = Sheet.createPropertiesSet();
        setProps.setDisplayName("Properties");
        sheet.put(setProps);
 
        
        for(DJProperty property:obj.getAllProperties()){
                
                Property rProp = this.createProperty(property);

            if (rProp != null) {

                if (property.getPropertyName().equals(DJObject.PROP_DESCRIPTION)&&property.isCanRead()) {

                    Sheet.Set setDescr = new Sheet.Set();
                    setDescr.setName("Description");
                    setDescr.setValue("tabName", " Description ");
                    setDescr.setShortDescription("Description");
                    setDescr.put(rProp);
                    sheet.put(setDescr);

                } else {

                    setProps.put(rProp);
                }
            } 
            
        }
        
        return sheet;
    }
    
}
