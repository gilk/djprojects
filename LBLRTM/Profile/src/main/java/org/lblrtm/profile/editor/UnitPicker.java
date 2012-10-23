/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.profile.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JComboBox;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.lblrtm.lblrtmdata.api.LBLRTMUnit;
import org.lblrtm.profile.api.ProfileTable;
import org.lblrtm.profile.api.ValueUnitCell;

/**
 *
 * @author djabry
 */
public class UnitPicker extends JComboBox {

    private final ProfileTable tbl;
    private boolean updating = false;

    public synchronized void updateSelectedUnits() {
        updating = true;
        this.removeAllItems();
        
        
        Set<LBLRTMUnit> allAvailableUnits = tbl.getAllAvailableUnits();
        Iterator<LBLRTMUnit> iterator = allAvailableUnits.iterator();

        while (iterator.hasNext()) {
            LBLRTMUnit next = iterator.next();
            this.addItem(next);

        }

        Set<LBLRTMUnit> units = this.tbl.getSelectedUnits();

        if (units.size() == 1) {
            LBLRTMUnit u = units.iterator().next();
            if (allAvailableUnits.contains(u)) {
                
                this.setSelectedItem(units.iterator().next());
                
                this.setEnabled(this.getItemCount()>1);

                updating = false;
                
                
                return;

            }
        }


        String multItem = "";
        this.addItem(multItem);

        this.setSelectedItem(multItem);
        this.setEnabled(this.getItemCount()>1);
        
        updating = false;
        

    }

    public UnitPicker(ProfileTable tble) {

        this.tbl = tble;

        this.updateSelectedUnits();

        this.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent ae) {
                
                if(updating){
                    
                    return;
                }
                
                Object selectedItem = getSelectedItem();

                if (selectedItem instanceof LBLRTMUnit) {
                    LBLRTMUnit lblrtmUnit = (LBLRTMUnit) selectedItem;
                    Iterator<ValueUnitCell> iterator = tbl.getAllSelectedCells().iterator();

                    while (iterator.hasNext()) {
                        ValueUnitCell next = iterator.next();
                        next.setUnit(lblrtmUnit);
                        

                    }
                }

            }
        });

        ListSelectionListener lSL = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent lse) {
                updateSelectedUnits();
            }
        };



        this.tbl.getColumnModel().getSelectionModel().addListSelectionListener(lSL);
        this.tbl.getSelectionModel().addListSelectionListener(lSL);



    }
}
