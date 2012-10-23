/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.profile.api;

import com.google.common.collect.Iterables;
import java.beans.PropertyChangeEvent;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import javolution.util.FastSet;
import org.dj.domainmodel.api.DJNodeObject;
import org.dj.domainmodel.api.DJObjectManagerAbstr;
import org.dj.filter.api.Filter;
import org.dj.matrix.api.DJMatrix;
import org.dj.matrix.api.DJMatrixIterator;
import org.dj.matrix.api.MatrixFactory;
import org.dj.property.api.DJProperty;
import org.lblrtm.lblrtmdata.api.LBLRTMUnit;
import org.lblrtm.lblrtmdata.api.Molecule;
import org.lblrtm.profile.LevelAbstr;
import org.lblrtm.profile.VariableManagerAbstr;
import org.lblrtm.profile.editor.ProfileTableModel;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import ucar.ma2.*;

/**
 *
 * @author djabry
 */
public class ProfileAbstr extends DJObjectManagerAbstr<Level> implements DJProfile {

    private final Set<Level> levels = new FastSet<Level>();
    private static final MatrixFactory mF = Lookup.getDefault().lookup(MatrixFactory.class);

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        super.propertyChange(pce);

        if (pce.getPropertyName().equals(PROP_CHILDREN)) {

            if (pce.getSource().equals(getVariableManager())) {

                firePropertyChange(MESSAGE_VARIABLES_CHANGED, pce.getOldValue(), pce.getNewValue());

            }

        } else if (pce.getPropertyName().equals(ValueUnitCell.PROP_VALUE)
                || pce.getPropertyName().equals(ValueUnitCell.PROP_UNIT)) {

            Level l = (Level) pce.getSource();
            ValueUnitCell c = (ValueUnitCell) pce.getOldValue();

            Enum var = c.getEnum();

            if (var.equals(PhysicalProperty.PRESSURE)) {

                this.firePropertyChange(MESSAGE_LEVELS_CHANGED, l, var.name());


            } else {

                this.firePropertyChange(MESSAGE_CELL_CHANGED, l, var.name());
            }
        }
    }

    public ProfileAbstr(Set<Level> levels, VariableManager mM) {

        super();
        this.setVariableManager(mM);

        for (Level l : levels) {
            this.addChild(l);
        }
    }

    public void setVariableManager(VariableManager vM) {

        DJProperty VariableManagerProp = this.getProperty(PROP_VARIABLE_MANAGER);

        if (VariableManagerProp != null) {

            this.getVariableManager().removePropertyChangeListener(this);
        }

        this.setPropertyValue(PROP_VARIABLE_MANAGER, vM);

        vM.addPropertyChangeListener(PROP_CHILDREN, this);
    }

    @Override
    public void addChild(DJNodeObject obj, Integer dim, boolean notify) {


        if (obj instanceof Level) {
            Level level = (Level) obj;
            this.levels.add(level);


        }

        super.addChild(obj, dim, notify);
        firePropertyChange(MESSAGE_LEVELS_CHANGED, null, obj);
    }

    @Override
    public void removeChild(DJNodeObject obj, Integer dim, boolean notify) {


        if (obj instanceof Level) {
            Level level = (Level) obj;
            this.levels.remove(level);


        }

        super.removeChild(obj, dim, notify);
        firePropertyChange(MESSAGE_LEVELS_CHANGED, null, obj);
    }

    public ProfileAbstr() {

        this(new FastSet<Level>(), new VariableManagerAbstr());

        VariableManager vM = this.getVariableManager();
        //Level l = (Level) this.createObject();

        vM.addMolecule(Molecule.H2O);
        vM.addMolecule(Molecule.CO2);
        vM.addMolecule(Molecule.O3);
        vM.addMolecule(Molecule.N2O);
        vM.addMolecule(Molecule.CO);
        vM.addMolecule(Molecule.CH4);

    }

    @Override
    public int getNumberOfLevels() {
        return levels.size();
    }

    @Override
    public Level getLevel(int levelIndex) {

        Iterator<Level> iterator = getAllLevels().iterator();
        int i = -1;
        Level l = null;

        while (iterator.hasNext() && i < levelIndex) {
            l = iterator.next();
            i++;
        }

        return l;

    }

    @Override
    public Profile duplicate() {
        Set<Level> levels = new FastSet<Level>(this.levels.size());

        for (Level l : this.levels) {

            levels.add(l.duplicate());
        }

        return new ProfileAbstr(levels, this.getVariableManager().duplicate());
    }

    @Override
    public DJNodeObject createObject() {

        double pressure = 1000.0;
        double alt = 0.0;
        double temp = 300.0;

        if (this.getNumberOfLevels() > 0) {
            Level topLevel = this.getLevel(this.getNumberOfLevels() - 1);
            alt = topLevel.getValueForPhysicalProperty(PhysicalProperty.ALTITUDE) + 1.0;
            temp = topLevel.getValueForPhysicalProperty(PhysicalProperty.TEMPERATURE) * 0.8;
            pressure = topLevel.getValueForPhysicalProperty(PhysicalProperty.PRESSURE) * 0.9;

        }

        LevelAbstr level = new LevelAbstr(pressure);
        this.addChild(level);
        level.setValueForPhysicalProperty(PhysicalProperty.ALTITUDE, alt);
        level.setValueForPhysicalProperty(PhysicalProperty.TEMPERATURE, temp);

        return level;


    }

    @Override
    public Array getProfileForVariable(String name) {
        Section s = new Section();
        s.appendRange(this.getLimitsForVariable(name));
        return this.getArrayForVariable(name, s);
    }

    @Override
    public Array getArrayForVariable(String name, Section section) {

        Array data = Array.factory(DataType.DOUBLE, section.getShape());

        Range r = section.getRanges().iterator().next();

        if (r.length() > 0) {

            Range.Iterator iterator = r.getIterator();
            while (iterator.hasNext()) {

                int next = iterator.next();
                data.setDouble(next, this.getValueForVariable(name, next));
            }

        }

        return data;
    }

    @Override
    public Range getLimitsForVariable(String name) {
        try {
            return new Range(0, this.getNumberOfLevels() - 1);
        } catch (InvalidRangeException ex) {
            Exceptions.printStackTrace(ex);
        }

        return Range.EMPTY;
    }

    @Override
    public double getValueForVariable(String name, int level) {
        return this.getLevel(level).getCellForName(name).getValue();
    }

    @Override
    public void setValueForVariable(String name, int level, double value) {
        this.getLevel(level).getCellForName(name).setValue(value);
    }

    @Override
    public LBLRTMUnit getUnitForVariable(String name, int level) {
        return this.getLevel(level).getCellForName(name).getUnit();
    }

    @Override
    public void setUnitForVariable(String name, int level, LBLRTMUnit unit) {
        this.getLevel(level).getCellForName(name).setUnit(unit);
    }

    @Override
    public Set<Molecule> getMolecules() {
        return this.getVariableManager().getMolecules();
    }

    @Override
    public Set<String> getVariables() {

        return this.getVariableManager().getVariables();
    }

    @Override
    public VariableManager getVariableManager() {
        return (VariableManager) this.getPropertyValue(PROP_VARIABLE_MANAGER);
    }

    @Override
    public Set<PhysicalProperty> getPhysicalProperties() {
        return getVariableManager().getPhysicalProperties();
    }

    @Override
    public String getVariableFullName(String variable) {
        return this.getVariableManager().getVariableFullName(variable);
    }

    @Override
    public Iterable<Level> getAllLevels() {

        return new TreeSet<Level>(levels);

    }

    @Override
    public Profile section(Filter<Level> levelFilter) {
        Set<Level> levels = new FastSet<Level>();

        for (Level l : this.levels) {

            if (levelFilter.filter(l)) {

                levels.add(l.duplicate());
            }
        }

        return new ProfileAbstr(levels, this.getVariableManager().duplicate());
    }

    @Override
    public DJMatrix exportToMatrix() {
        int rows = this.getNumberOfLevels();
        int cols = this.getVariableManager().getVariables().size();

        DJMatrix mat = null;
        if (rows > 0 && cols > 0) {
            mat = mF.createMatrix(rows, cols);

            for (int r = 0; r < rows; r++) {

                for (int c = 0; c < cols; c++) {
                    mat.setValueAt(r, c, this.getValueAt(r, c));
                }

            }
        }

        return mat;

    }

    private void trimToSize(int nRows, int nCols) {

        int diff = this.getNumberOfLevels() - nRows;

        while (diff > 0) {

            this.removeChild(this.getLevel(this.getNumberOfLevels() - 1));
            diff--;
        }

        VariableManager vM = this.getVariableManager();
        while (vM.getVariables().size() > nCols) {
            vM.removeVariable(Iterables.getLast(vM.getVariables()));
        }
    }

    @Override
    public void importMatrix(DJMatrix mat) {

        if (mat.getNumberOfColumns() >= this.getVariableManager().getPhysicalProperties().size()) {
            this.trimToSize(mat.getNumberOfRows(), mat.getNumberOfColumns());
            DJMatrixIterator iterator = mat.iterator(true);
            while (iterator.hasNext()) {
                Double next = iterator.next();
                this.setValueAt(iterator.getRow(), iterator.getColumn(), next);
            }

        }

    }

    @Override
    public double getValueAt(int r, int c) {
        Set<String> variables = this.getVariableManager().getVariables();
        String var = Iterables.get(variables, c);
        return this.getValueForVariable(var, r);
    }

    @Override
    public void setValueAt(int row, int col, double val) {

        while (row >= this.getNumberOfLevels()) {

            this.createObject();
        }

        VariableManager vM = this.getVariableManager();
        Iterator<Molecule> iterator = Arrays.asList(Molecule.values()).iterator();


        while (col >= vM.getVariables().size() && iterator.hasNext()) {
            Molecule m = iterator.next();
            if (!vM.getMolecules().contains(m)) {
                vM.addMolecule(m);

            }

        }


        if (row < getNumberOfLevels() && col < vM.getVariables().size()) {

            Set<String> variables = this.getVariableManager().getVariables();
            String var = Iterables.get(variables, col);
            this.setValueForVariable(var, row, val);

        }

    }
}
