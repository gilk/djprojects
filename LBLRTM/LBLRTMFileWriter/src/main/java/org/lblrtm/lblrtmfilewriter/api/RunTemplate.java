/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter.api;

import java.util.List;
import org.dj.domainmodel.api.DJNodeObject;
import org.dj.property.api.DJProperty;

/**
 *
 * @author djabry
 */
public interface RunTemplate<T extends RunTemplate> extends DJNodeObject{
    
    FieldValueModifier getFieldValueModifier();
    RecordRepetitionSpecifier getRecordRepetitionSpecifier();
    List<DJProperty> getPropertiesToEdit();
    void importPropertiesFrom(RunTemplate rT);
    T duplicate();
    
}
