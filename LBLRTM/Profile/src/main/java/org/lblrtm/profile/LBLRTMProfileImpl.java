/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.profile;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javolution.util.FastList;
import javolution.util.FastMap;
import org.dj.domainmodel.api.DJNodeObjectAbstr;
import org.lblrtm.lblrtmdata.api.LBLRTMUnit;
import org.lblrtm.lblrtmdata.api.Molecule;
import ucar.ma2.*;

/**
 *
 * @author djabry
 */
public class LBLRTMProfileImpl extends DJNodeObjectAbstr implements LBLRTMProfile{
    
    private Map<String,List<Double>> profile;
    private Map<Molecule,List<LBLRTMUnit>> units;
    
    
    public LBLRTMProfileImpl(LBLRTMProfile p){
        
        this.profile= new FastMap<String,List<Double>>();
        this.units=new EnumMap<Molecule,List<LBLRTMUnit>>(Molecule.class);
        
        this.profile.put(PROP_ALTITUDE,new FastList<Double>(p.getAltitude()));
        this.profile.put(PROP_PRESSURE, new FastList<Double>(p.getPressureProfile()));
        this.profile.put(PROP_TEMPERATURE,new FastList<Double>(p.getTempProfile()));
        
        for(Molecule m:p.getMolecules()){

            this.profile.put(m.name(),new FastList<Double>(p.getProfileForMolecule(m)));
            this.units.put(m,new FastList<LBLRTMUnit>(p.getUnitsForMolecule(m)));
                   
        }
 
    }
    

    @Override
    public List<Double> getTempProfile() {
        return this.profile.get(PROP_TEMPERATURE);
    }

    @Override
    public List<Double> getAltitude() {
        return this.profile.get(PROP_ALTITUDE);
    }

    @Override
    public List<Double> getPressureProfile() {
       return this.profile.get(PROP_PRESSURE);
    }

    @Override
    public List<Double> getProfileForMolecule(Molecule molecule) {
        return this.profile.get(molecule.name());
    }

    @Override
    public Array getProfileForVariable(String name) {
        Section s = new Section();
        s.appendRange(this.getLimitsForVariable(name));
        return this.getArrayForVariable(name, s);
    }
    
    public double readValueForVariable(String name,int level){
        
        return this.profile.get(name).get(level);
    }
    
    

    @Override
    public Array getArrayForVariable(String name, Section section) {
        Array data = Array.factory(DataType.DOUBLE, section.getShape());

        Range r = section.getRanges().iterator().next();

        if (r.length() > 0) {

            Range.Iterator iterator = r.getIterator();

            while (iterator.hasNext()) {
                int next = iterator.next();
                data.setDouble(next, readValueForVariable(name, next));

            }


        }


        return data;
    }

    @Override
    public Range getLimitsForVariable(String name) {
        List<Double> get = this.profile.get(name);
        try {
            return new Range(0,get.size()-1);
        } catch (InvalidRangeException ex) {
            Logger.getLogger(LBLRTMProfileImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

    @Override
    public List<LBLRTMUnit> getUnitsForMolecule(Molecule molecule) {
        return this.units.get(molecule);
    }

    @Override
    public Set<Molecule> getMolecules() {
       return this.units.keySet();
    }

    @Override
    public LBLRTMProfile duplicate() {
        return new LBLRTMProfileImpl(this);
    }

    @Override
    public LBLRTMProfile section(double startAlt, double endAlt) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
   
    
}
