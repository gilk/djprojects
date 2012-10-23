/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.profile.editor;

import java.text.ParseException;
import javax.swing.JFormattedTextField.AbstractFormatter;

/**
 *
 * @author djabry
 */
public class NumberUnitFormatter extends AbstractFormatter{
    
    private final String unitString;
    private final double min;
    private final double max;
    
    public NumberUnitFormatter(String unit, double min, double max){
        this.unitString=" ("+unit+")";
        this.min=min;
        this.max=max;
        
    }

    @Override
    public Object stringToValue(String string) throws ParseException {
        return Double.parseDouble(string.replace(unitString, ""));
    }

    @Override
    public String valueToString(Object o) throws ParseException {
        return o+unitString;
    }
    
}
