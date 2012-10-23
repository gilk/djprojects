/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.profile.editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.Map;
import javax.swing.table.AbstractTableModel;
import javolution.util.FastMap;
import org.lblrtm.profile.api.DJProfile;
import org.lblrtm.profile.api.Level;
import org.lblrtm.profile.api.Profile;
import org.lblrtm.profile.api.ValueUnitCell;

/**
 *
 * @author djabry
 */
public class ProfileTableModel extends AbstractTableModel implements Profile.Provider{
    
    private final DJProfile p;
    private final Map<Integer,Level> levelByRow = new FastMap<Integer,Level>();
    private final Map<Level,Integer> rowByLevel = new FastMap<Level,Integer>();
    private final Map<String,Integer> columnByVar = new FastMap<String, Integer>();
    private final Map<Integer,String> varByColumn = new FastMap<Integer,String>();
    
    private PropertyChangeListener levelListener = new PropertyChangeListener(){

        @Override
        public void propertyChange(PropertyChangeEvent pce) {
            if(pce.getPropertyName().equals(Profile.MESSAGE_LEVELS_CHANGED)){
                updateLevels();
                fireTableDataChanged();
            }
        }
        
    };
    
    private void updateLevels(){
        
        this.rowByLevel.clear();
        this.levelByRow.clear();
        int i=0;
        Iterator<Level> iterator = this.p.getAllLevels().iterator();
        
        while(iterator.hasNext()){
            Level next = iterator.next();
            this.rowByLevel.put(next, i);
            this.levelByRow.put(i, next);
            i++;
            
        }
        
    }
    
    
    private PropertyChangeListener variableListener = new PropertyChangeListener(){

        @Override
        public void propertyChange(PropertyChangeEvent pce) {
            if(pce.getPropertyName().equals(Profile.MESSAGE_VARIABLES_CHANGED)){
                updateColumnNames();
                fireTableStructureChanged();
            }
        }

    };
    
    private PropertyChangeListener cellListener = new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent pce) {
           if(pce.getPropertyName().equals(Profile.MESSAGE_CELL_CHANGED)){
               
               String var = (String)pce.getNewValue();
               Level l = (Level)pce.getOldValue();
               
               int r = rowByLevel.get(l);
               int c = columnByVar.get(var);
               fireTableCellUpdated(r, c);
           }
        }
    };
    
    public ProfileTableModel(DJProfile p){
        this.p=p;
        
        updateColumnNames();
        updateLevels();
        this.p.addPropertyChangeListener(Profile.MESSAGE_LEVELS_CHANGED,levelListener);
        this.p.addPropertyChangeListener(Profile.MESSAGE_VARIABLES_CHANGED,variableListener);
        this.p.addPropertyChangeListener(Profile.MESSAGE_CELL_CHANGED,cellListener);
    }
    
    private void updateColumnNames(){
        
        this.columnByVar.clear();
        this.varByColumn.clear();
        
        int i = 0;
        Iterator<String> iterator = this.p.getVariableManager().getVariables().iterator();
        while(iterator.hasNext()){
            String var = iterator.next();
            this.columnByVar.put(var, i);
            this.varByColumn.put(i, var);
            i++;
        }
        
        
    }
    
    int getColumnIndexForVar(String var){
        return this.columnByVar.get(var);
    }
    
    int getRowIndexForLevel(Level l){

        return this.rowByLevel.get(l);
    }

    @Override
    public String getColumnName(int i) {
       return p.getVariableManager().getVariableFullName(this.varByColumn.get(i));
    }

    @Override
    public boolean isCellEditable(int i, int i1) {
        return true;
    }

    @Override
    public Class<?> getColumnClass(int i) {
        return ValueUnitCell.class;
    }


    @Override
    public int getRowCount() {
        return this.levelByRow.size();
    }

    @Override
    public int getColumnCount() {
       return p.getVariables().size();
    }

    @Override
    public Object getValueAt(int i, int i1) {

       Level l = this.levelByRow.get(i);
       return l.getCellForName(varByColumn.get(i1));

    }

    @Override
    public Profile getProfile() {
        return p;
    }
    
    
    
   
    
}
