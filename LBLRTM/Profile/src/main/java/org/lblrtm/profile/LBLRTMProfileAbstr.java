/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.profile;

import java.util.List;
import javolution.util.FastList;
import org.dj.domainmodel.api.DJNodeObjectAbstr;
import org.lblrtm.lblrtmdata.api.Molecule;
import ucar.ma2.Array;
import ucar.ma2.DataType;
import ucar.ma2.Range;
import ucar.ma2.Range.Iterator;
import ucar.ma2.Section;

/**
 *
 * @author djabry
 */
public abstract class LBLRTMProfileAbstr extends DJNodeObjectAbstr implements LBLRTMProfile {

    public abstract double readValueForVariable(String vrbl, int layer);

    @Override
    public List<Double> getTempProfile() {
        return this.getProfileForVariable(PROP_TEMPERATURE, this.getLimitsForVariable(PROP_TEMPERATURE));
    }

    @Override
    public List<Double> getAltitude() {
        return this.getProfileForVariable(PROP_ALTITUDE, this.getLimitsForVariable(PROP_ALTITUDE));
    }

    @Override
    public List<Double> getPressureProfile() {
        return this.getProfileForVariable(PROP_PRESSURE, this.getLimitsForVariable(PROP_PRESSURE));
    }

    @Override
    public List<Double> getProfileForMolecule(Molecule molecule) {

        return this.getProfileForVariable(molecule.name(), this.getLimitsForVariable(molecule.name()));
    }

    @Override
    public Array getProfileForVariable(String name) {

        Section s = new Section();
        s.appendRange(this.getLimitsForVariable(name));
        return this.getArrayForVariable(name, s);
    }

    List<Double> getProfileForVariable(String name, Range r) {
        Section section = new Section();
        section.appendRange(r);
        Array arr = getArrayForVariable(name, section);
        List<Double> values = new FastList<Double>();
        r = section.getRanges().iterator().next();
        Iterator iterator = r.getIterator();

        while (iterator.hasNext()) {

            values.add(arr.getDouble(iterator.next()));
        }


        return values;
    }

    @Override
    public Array getArrayForVariable(String name, Section section) {

        Array data = Array.factory(DataType.DOUBLE, section.getShape());

        Range r = section.getRanges().iterator().next();

        if (r.length() > 0) {

            Iterator iterator = r.getIterator();

            while (iterator.hasNext()) {
                int next = iterator.next();
                data.setDouble(next, readValueForVariable(name, next));

            }


        }


        return data;
    }
    
    @Override
    public LBLRTMProfile duplicate(){
        
        return new LBLRTMProfileImpl(this);
    }
}
