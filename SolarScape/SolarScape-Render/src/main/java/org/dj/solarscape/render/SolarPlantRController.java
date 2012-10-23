/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.solarscape.render;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.markers.BasicMarker;
import gov.nasa.worldwind.render.markers.BasicMarkerAttributes;
import gov.nasa.worldwind.render.markers.BasicMarkerShape;
import gov.nasa.worldwind.render.markers.Marker;
import java.util.Set;
import javolution.util.FastSet;
import org.dj.solarscape.data.api.SolarPlant;
import org.dj.worldwindmodel.object.api.WWDJObject;
import org.dj.worldwindmodel.render.api.RenderableController;

/**
 *
 * @author djabry
 */
public class SolarPlantRController implements RenderableController{
    
    private final SolarPlant p;
    private final BasicMarker m;
    private final BasicMarkerAttributes bMA;
    private final Set<Marker> s;
    
    
    public SolarPlantRController(SolarPlant p){
        
        this.p=p;
        this.s=new FastSet<Marker>(); 
        bMA = new BasicMarkerAttributes();
        bMA.setShapeType(BasicMarkerShape.CUBE);
        bMA.setMaterial(Material.BLUE);
        Position pos = Position.fromDegrees(p.getPosition().getLatitude().degrees,p.getPosition().getLongitude().degrees);
        this.m=new BasicMarker(pos,bMA);
        
        if(!pos.equals(Position.ZERO)){
            s.add(m);
        }
        
        
    }

    @Override
    public void setVisible(boolean v) {
        double o = 0.0;
        
        if(v){
            o=1.0;
        }
            
        
        this.m.getAttributes().setOpacity(o);
    }

    @Override
    public boolean isVisible() {
        return m.getAttributes().getOpacity()!=0.0;
    }

    @Override
    public Set getRenderables() {
        return s;
    }

    @Override
    public WWDJObject getObject() {
       return p;
    }

    @Override
    public void setHighlighted(boolean tf) {
        
        if(tf){
            
            bMA.setMaterial(Material.RED);
        }else{
            bMA.setMaterial(Material.BLUE);
            
            
        }
        

    }

    @Override
    public boolean isHighlighted() {
        return bMA.getMaterial().equals(Material.RED);
    }
    
}
