/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.worldwindmodel.object.api;

import gov.nasa.worldwind.geom.Position;
import org.dj.domainmodel.api.DJNodeObjectAbstr;
import org.dj.property.api.DJProperty;

/**
 *
 * @author djabry
 */
public class WWDJObjectAbstr extends DJNodeObjectAbstr implements WWDJObject{

    
    
    public WWDJObjectAbstr(){
        DJProperty<Position> positionProp = getPropertyFactory().createProperty(PROP_POSITION,Position.ZERO);
        positionProp.setDisplayName("Position");
        putProperty(positionProp);
    }

    @Override
    public Position getPosition() {
        return (Position) this.getPropertyValue(PROP_POSITION);
    }

    @Override
    public void setPosition(Position p) {
        this.setPropertyValue(PROP_POSITION, p);
    }

    /**
     * @return the elevation
     */

    public double getElevation() {
        return getPosition().getElevation();
    }

    /**
     * @param elevation the elevation to set
     */

    public void setElevation(double elevation) {
        Position oldPos = getPosition();
        this.setPosition(Position.fromDegrees(oldPos.latitude.degrees, oldPos.longitude.degrees,elevation));
    }

    /**
     * @return the latitude
     */

    public double getLatitude() {
        return getPosition().getLatitude().degrees;
    }

    /**
     * @param latitude the latitude to set
     */

    public void setLatitude(double latitude) {
        Position oldPos = this.getPosition();
        this.setPosition(Position.fromDegrees(latitude, oldPos.longitude.degrees, oldPos.elevation));
    }

    /**
     * @return the longitude
     */

    public double getLongitude() {
        return getPosition().longitude.degrees;
    }

    /**
     * @param longitude the longitude to set
     */

    public void setLongitude(double longitude) {
        Position oldPos = this.getPosition();
        this.setPosition(Position.fromDegrees(oldPos.latitude.degrees, longitude, oldPos.elevation));
    }


    public double getAltitude() {
        return getPosition().getAltitude();
    }
    

    
}
