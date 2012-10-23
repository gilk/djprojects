/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.solarscape.data.api;

import java.util.Date;
import org.dj.db.api.DBObject;
import org.dj.filter.api.Filter;
import org.dj.worldwindmodel.object.api.WWDJObject;

/**
 *
 * @author djabry
 */
public interface SolarPlant extends WWDJObject,DBObject, Filter<Date> {
    
    public static final String PROP_COMISSION_DATE = "comissionDate";
    Date getComissionDate();
    
}
