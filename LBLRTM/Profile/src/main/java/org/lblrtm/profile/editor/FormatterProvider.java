/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.profile.editor;

import java.util.Formatter;
import java.util.Map;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javolution.util.FastMap;
import org.lblrtm.lblrtmdata.api.LBLRTMUnit;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider (service = FormatterProvider.class)
public class FormatterProvider {
    
    private static final Map<LBLRTMUnit, AbstractFormatter> formatters = new FastMap<LBLRTMUnit, AbstractFormatter>();
    
    public FormatterProvider(){
        
        populateFormatters();
    }
    
    public AbstractFormatter getFormatter(LBLRTMUnit u){
        
        return formatters.get(u);
    }
    
    private void populateFormatters() {

        if (formatters.isEmpty()) {
            for (LBLRTMUnit u : LBLRTMUnit.values()) {
                formatters.put(u, new NumberUnitFormatter(u.symbol,u.min,u.max));
            }
        }
    }
}
