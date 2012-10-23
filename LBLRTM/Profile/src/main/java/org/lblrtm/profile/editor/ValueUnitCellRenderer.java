/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.profile.editor;

import java.text.ParseException;
import javax.swing.table.DefaultTableCellRenderer;
import org.lblrtm.profile.api.ValueUnitCell;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author djabry
 */
public class ValueUnitCellRenderer extends DefaultTableCellRenderer{

    private static final FormatterProvider fP = Lookup.getDefault().lookup(FormatterProvider.class);

    @Override
     public void setValue(Object value) {

        try {
           ValueUnitCell c = (ValueUnitCell)value;
           setText(fP.getFormatter(c.getUnit()).valueToString(c.getValue()));
        } 
        catch (ParseException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    
    
}
