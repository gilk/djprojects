/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.solarscape.data;

import gov.nasa.worldwind.geom.Position;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.dj.db.api.DBObjectAbstr;
import org.dj.domainmodel.api.DJNodeObjectAbstr;
import org.dj.property.api.DJProperty;
import org.dj.solarscape.data.api.SolarPlant;

/**
 *
 * @author djabry
 */

@Entity
public class SolarPlantImpl extends DBObjectAbstr implements SolarPlant{

    public SolarPlantImpl() {
        
        DJProperty<Position> posProp = getPropertyFactory().createProperty(PROP_POSITION, Position.ZERO);
        posProp.setCanRead(false);
        putProperty(posProp);
        
        DJProperty<Date> cDateProp = getPropertyFactory().createProperty(PROP_COMISSION_DATE, Calendar.getInstance().getTime());
        cDateProp.setDisplayName("Comission date");
        cDateProp.setCanWrite(false);
        putProperty(cDateProp);
    }
    
    public SolarPlantImpl(Map<String,Object> props){
        
        this();
        putProps(props);
        
        
    }

    @Override
    public String getHtmlDisplayName() {
        
        String dispName = super.getHtmlDisplayName();
        
        if(this.getPosition().equals(Position.ZERO)){
            
            dispName="<font color='#0000FF'>"+dispName+"</font>";
        }
        return dispName;
    }
    
    
    
    private void putProps(Map props){
        
        Iterator<Entry> iterator = props.entrySet().iterator();
        
        while(iterator.hasNext()){
            Entry next = iterator.next();
            
            String propName = (String)next.getKey();
            Object vl = next.getValue();
            
            String displayName = propName.replace("_", " ");
            displayName = displayName.substring(0, 1).toUpperCase()+displayName.substring(1).toLowerCase();
            
            
            DJProperty<Object> prop = this.getProperty(propName);
            
            if(prop==null){
                
                prop = getPropertyFactory().createProperty(propName, vl);
                prop.setDisplayName(displayName);
                prop.setCanWrite(false);
                this.putProperty(prop); 
                
            }else{
                
                prop.setValue(vl);
            }
            
            
        }
        
        
    }

//    @Override
//    public String getName() {
//        
//        if(this.getPosition().equals(Position.ZERO)){
//            return "<font color='0000FF'>"+super.getName()+"</font>";
//        }
//        return super.getName();
//    }
    
    
    
    public HashMap<String,String> getSolarPlantProps(){
        HashMap<String,String> props = new HashMap<String,String>();
        Iterator<DJProperty> iterator = this.getAllProperties().iterator();
        while(iterator.hasNext()){
            DJProperty next = iterator.next();
            Object propVal = next.getValue();
            
            if(propVal instanceof String){
                String propName = next.getPropertyName();
                props.put(propName, (String)propVal);
            } 
        }
        
        return props;
    }
    
    public void setSolarPlantProps(HashMap<String,String> props){
        putProps(props);
    }
    
    public List<Double> getDBPosition(){
        List<Double> posList = new ArrayList<Double>();
        Position pos = this.getPosition();
        posList.add(pos.getLatitude().degrees);
        posList.add(pos.getLongitude().degrees);
        posList.add(pos.getElevation());
        return posList;
    }
    
    public void setDBPosition(List<Double> posList){
        Position p = Position.fromDegrees(posList.get(0), 
                posList.get(1), 
                posList.get(2));
        
        this.setPosition(p);
        
    }

    @Override
    @Transient
    public Position getPosition() {
        return (Position) this.getPropertyValue(PROP_POSITION);
    }

    @Override
    public void setPosition(Position p) {
        this.setPropertyValue(PROP_POSITION, p);
    }

    @Override
    public boolean filter(Date obj) {
       if(obj.after(getComissionDate())){
           
           return true;
       }
       
       return false;
    }

    @Override
    @Temporal(TemporalType.DATE)
    public Date getComissionDate() {
        return (Date) this.getPropertyValue(PROP_COMISSION_DATE);
    }
    
    public void setComissionDate(Date d){
        this.setPropertyValue(PROP_COMISSION_DATE, d);
    }
    
}
