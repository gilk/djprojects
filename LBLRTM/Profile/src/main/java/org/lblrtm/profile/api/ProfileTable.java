/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.profile.api;

import com.google.common.collect.Sets;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javolution.util.FastSet;
import org.lblrtm.lblrtmdata.api.LBLRTMUnit;
import org.lblrtm.profile.editor.ExcelAdapter;
import org.lblrtm.profile.editor.ProfileTableModel;
import org.lblrtm.profile.editor.ValueUnitCellEditor;
import org.lblrtm.profile.editor.ValueUnitCellRenderer;

/**
 *
 * @author djabry
 */
public class ProfileTable extends JTable {
    
    //private final ExcelAdapter eA;

    public ProfileTable(DJProfile p){
 
        this.setModel(new ProfileTableModel(p));
        this.setDefaultRenderer(ValueUnitCell.class, new ValueUnitCellRenderer());
        this.setDefaultEditor(ValueUnitCell.class, new ValueUnitCellEditor());
        this.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        this.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        this.setRowSelectionAllowed(true);
        this.setColumnSelectionAllowed(true);

        //this.eA=new ExcelAdapter(this);
    }
    
    
    public Set<LBLRTMUnit> getSelectedUnits(){
        
        Iterator<ValueUnitCell> iterator = getAllSelectedCells().iterator();
        Set<LBLRTMUnit> selectedUnits = EnumSet.noneOf(LBLRTMUnit.class);
        int limit = LBLRTMUnit.values().length;
        while(iterator.hasNext()){
            ValueUnitCell next = iterator.next();
            selectedUnits.add(next.getUnit());
            if(selectedUnits.size()==limit){
                
                break;
            }
            
        }
        
        return selectedUnits;
 
    }
    
    public Set<ValueUnitCell> getAllSelectedCells(){
        
        ProfileTableModel pTM  = (ProfileTableModel) this.getModel();
        Set<ValueUnitCell> selectedCells = new FastSet<ValueUnitCell>();
        
        
        for (int c:this.getSelectedColumns()){
            for( int r:this.getSelectedRows()){
                
               selectedCells.add((ValueUnitCell)pTM.getValueAt(r, c));
            }
            
        }
        
        
        
        return selectedCells;

    }
    
    public Set<LBLRTMUnit> getAllAvailableUnits(){
        
        
        Iterator<ValueUnitCell> iterator = getAllSelectedCells().iterator();
        Set<LBLRTMUnit> availableUnits =EnumSet.noneOf(LBLRTMUnit.class);

        if(iterator.hasNext()){
            
            availableUnits = EnumSet.copyOf(iterator.next().getAvailableUnits());

             while(iterator.hasNext()){
                
                 availableUnits = Sets.intersection(availableUnits, iterator.next().getAvailableUnits()); 
            }

        }
        
        return availableUnits;
 
        
    }
    
}
