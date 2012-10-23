/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.profile;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javolution.util.FastMap;
import javolution.util.FastSet;
import org.dj.domainmodel.api.DJNodeObject;
import org.dj.domainmodel.api.DJObjectManagerAbstr;
import org.lblrtm.lblrtmdata.api.LBLRTMUnit;
import org.lblrtm.lblrtmdata.api.Molecule;
import org.lblrtm.profile.api.*;

/**
 *
 * @author djabry
 */
public class LevelAbstr extends DJObjectManagerAbstr<ValueUnitCell> implements Level {

    //public static final String PROP_CONCENTRATION_MAP = "concentrationMap";
    //public static final String PROP_MOLECULE_MANAGER = "moleculeManager";
    
    private final Set<ValueUnitCell> cells = new FastSet<ValueUnitCell>();
    private final Map<String,ValueUnitCell> cellMap = new FastMap<String,ValueUnitCell>();

    @Override
    public void addChild(DJNodeObject obj, Integer dim, boolean notify) {
        
        
        
        if(obj instanceof ValueUnitCell){
            
            ValueUnitCell cell = (ValueUnitCell) obj;
            
            this.cellMap.put(cell.getVariableName(), cell);
            this.cells.add(cell);
           
        }
        
        super.addChild(obj, dim, notify);
    }

    @Override
    public void removeChild(DJNodeObject obj, Integer dim, boolean notify) {
        
        
        
        if(obj instanceof ValueUnitCell){
            
            ValueUnitCell cell = (ValueUnitCell) obj;
            this.cellMap.remove(cell.getVariableName());
            this.cells.remove(cell);
            
        }
        
        super.removeChild(obj, dim, notify);
    }
    
   

    
    public VariableManager getVariableManager(){
       
        return ((DJProfile)this.getParent()).getVariableManager();
    }

    public LevelAbstr() {

        this(1000.0);
    }
    
    public LevelAbstr(double pressure){
        
        this(new FastSet<ValueUnitCell>(),pressure);
        
    }



    public LevelAbstr(Set<ValueUnitCell> cells, double pressure) {
        
        super();

        for(ValueUnitCell c: cells){
            
            
            this.addChild(c);
           
        }
        
        ValueUnitCell c = new DJPhysicalPropertyAbstr(PhysicalProperty.PRESSURE);
        c.setValue(pressure);
        this.addChild(c);

    }


    @Override
    public double getValueForMolecule(Molecule m) {
        return this.getValueForVariable(m.name());
    }



    @Override
    public LBLRTMUnit getUnitForMolecule(Molecule m) {
        return this.getUnitForVariable(m.name());
    }

    @Override
    public Level duplicate() {

        Set<ValueUnitCell> l = new FastSet<ValueUnitCell>();

        for (String variable : this.getVariableManager().getVariables()) {

            l.add(this.getCellForName(variable).duplicate());
        }

        return new LevelAbstr(l,this.getValueForPhysicalProperty(PhysicalProperty.PRESSURE));

    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        
        if(pce.getPropertyName().equals(ValueUnitCell.PROP_VALUE )||
                pce.getPropertyName().equals(ValueUnitCell.PROP_UNIT)){
            
            firePropertyChange(pce.getPropertyName(), pce.getSource(), pce.getNewValue());
        }
        
        
        super.propertyChange(pce);
    }
    
    

    @Override
    public ValueUnitCell getCellForName(String name) {
        
        ValueUnitCell c = this.cellMap.get(name);
        if(c==null){
            
            c = createNewCellForName(name);
            this.addChild(c);
        }

        return c;
        
    }
    
    private ValueUnitCell createNewCellForName(String varName){
        
        VariableManager vM = getVariableManager();
        Enum e = vM.getVariableEnum(varName);
        ValueUnitCell c = null;
        
        if(e instanceof PhysicalProperty){
            PhysicalProperty p = (PhysicalProperty) e;
            
            c = new DJPhysicalPropertyAbstr(p);
        }else if(e instanceof Molecule) {
            Molecule molecule = (Molecule) e;
            
            c= new ConcentrationAbstr(molecule);
            
        }
        
        return c;
    }

    @Override
    public void setValueForMolecule(Molecule m, double val) {
        this.getCellForName(m.name()).setValue(val);
    }

    @Override
    public void setUnitForMolecule(Molecule m, LBLRTMUnit u) {
        this.getCellForName(m.name()).setUnit(u);
    }

    @Override
    public int compareTo(Level t) {
        return ((Double) t.getCellForName(PhysicalProperty.PRESSURE.name()).
                getValue()).compareTo((Double) this.getCellForName(
                PhysicalProperty.PRESSURE.name()).getValue());
    }

    @Override
    public double getValueForPhysicalProperty(PhysicalProperty p) {
        return this.getValueForVariable(p.name());
    }

    @Override
    public void setValueForPhysicalProperty(PhysicalProperty p, double value) {
        this.setValueForVariable(p.name(), value);
    }

    @Override
    public LBLRTMUnit getUnitForPhysicalProperty(PhysicalProperty p) {
        return this.getUnitForVariable(p.name());
    }

    @Override
    public void setUnitForPhysicalProperty(PhysicalProperty p, LBLRTMUnit u) {
       this.setUnitForVariable(p.name(), u);
    }

    @Override
    public double getValueForVariable(String varName) {
        return this.getCellForName(varName).getValue();
    }

    @Override
    public LBLRTMUnit getUnitForVariable(String varName) {
        return this.getCellForName(varName).getUnit();
    }

    @Override
    public void setValueForVariable(String varName, double val) {
        this.getCellForName(varName).setValue(val);
    }

    @Override
    public void setUnitForVariable(String varName, LBLRTMUnit u) {
        this.getCellForName(varName).setUnit(u);
    }

}
