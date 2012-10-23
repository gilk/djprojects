/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.profile.editor;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import org.lblrtm.lblrtmdata.api.LBLRTMUnit;
import org.lblrtm.profile.api.ValueUnitCell;
import org.openide.util.Lookup;

/**
 *
 * @author djabry
 */
public class ValueUnitCellEditor extends DefaultCellEditor{
    
    private final JFormattedTextField fld;
    private static final FormatterProvider fP = Lookup.getDefault().lookup(FormatterProvider.class);
    private ValueUnitCell c;
    
    private Popup pu;
    private JComboBox b;
    private UnitSelectorModel m;
    private int row;
    private int col;
    private boolean selected;
    private JTable t;
    
    class UnitSelectorModel extends DefaultComboBoxModel {

        
        public void updateModel(){
            
            b.removeActionListener(unitListener);
            this.removeAllElements();
            Iterator<LBLRTMUnit> iterator = c.getAvailableUnits().iterator();
            
            while(iterator.hasNext()){
                this.addElement(iterator.next());
                
            }
            
            this.setSelectedItem(c.getUnit());
            
            b.addActionListener(unitListener);
            
        }

    }
    
    
    public ValueUnitCellEditor(){
        
        super(new JFormattedTextField());
        this.fld=(JFormattedTextField)this.getComponent();
        this.fld.addPropertyChangeListener("value",cellUpdater);
        this.addCellEditorListener(l);
        
 
    }
    
    private void updateComponent(){

        if(c!=null){
            
            if(fld!=null){
                
                fP.getFormatter(c.getUnit()).install(fld);
                fld.setValue(c.getValue());
                
            }
            
            if(b!=null){
                
                this.b.setSelectedItem(c.getUnit());
            }
        }
        
    }
    
    private final ActionListener unitListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent ae) {
            updateCell();
        }
    };
    
    JComboBox getUnitSelector(){
        
        if(this.b==null){
            
            this.b=new JComboBox();
            this.m=new UnitSelectorModel();
            this.b.setModel(m);
            
        }
        
        m.updateModel();
        return b;
    }
    
    
    void showPopup(){
        if(c.getAvailableUnits().size()>1){
            
            Rectangle cellRect = t.getCellRect(row, col, true);
            Point loc = t.getLocationOnScreen();
            int x = loc.x+cellRect.x+cellRect.width;
            int y = loc.y+cellRect.y;

             this.pu=PopupFactory.
                getSharedInstance().getPopup(t.getRootPane(), getUnitSelector(), x, y);
           
            pu.show();
            
        }
    }
    
    
    void hidePopup(){
        
        if(this.pu!=null){
            
            pu.hide();
        }
    }

    @Override
    public Component getTableCellEditorComponent(JTable jtable, Object o, boolean bln, int i, int i1) {
        Component comp = super.getTableCellEditorComponent(jtable, o, bln, i, i1);
        
        this.row=i;
        this.col=i1;
        this.t=jtable;
        this.selected=bln;
        
        this.c = (ValueUnitCell)o;
        showPopup();

        updateComponent(); 
        return comp;
    }
    
    
    
    private void updateCell(){
        
        if(c!=null){
            
            if(fld!=null){
                if(fld.getValue()!=null){
                    this.c.setValue(((Number)this.fld.getValue()).doubleValue()); 
                }  
            }
            if(b!=null){
                
                this.c.setUnit((LBLRTMUnit)this.b.getSelectedItem());
            }
 
        } 
    }
    
    private final PropertyChangeListener cellUpdater = new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent pce) {
            updateCell();
        }
    };
    
    
    
    
    
    private final CellEditorListener l = new CellEditorListener() {

        @Override
        public void editingStopped(ChangeEvent ce) {
           hidePopup();
        }

        @Override
        public void editingCanceled(ChangeEvent ce) {
            hidePopup();
        }
    };
    
    
    
    
    
    
    
    
    
}
