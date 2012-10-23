/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.matrix.editor;

import java.util.IllegalFormatException;
import javax.swing.table.AbstractTableModel;
import org.dj.matrix.api.DJMatrix;

/**
 *
 * @author djabry
 */
public class MatrixTableModel extends AbstractTableModel{
    
    private final DJMatrix m;
    
    public MatrixTableModel(DJMatrix m){
        this.m=m;
    }

    @Override
    public int getRowCount() {
        return m.getNumberOfRows();
    }

    @Override
    public int getColumnCount() {
        
        return m.getNumberOfColumns();
    }

    @Override
    public String getColumnName(int i) {
        return ""+i;
    }

    @Override
    public Object getValueAt(int i, int i1) {
       return m.getValueAt(i, i1);
    }

    @Override
    public void setValueAt(Object o, int i, int i1) {
        
        try{
            Double v = Double.valueOf(o.toString());
            m.setValueAt(i, i, v);
            super.setValueAt(o, i, i1);
            
        }catch(IllegalFormatException iFE){
            
            
        }
        
        
    }
    
    
    
}
