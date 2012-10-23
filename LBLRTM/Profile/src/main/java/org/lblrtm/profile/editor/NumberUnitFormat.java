/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.profile.editor;

import java.math.RoundingMode;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

/**
 *
 * @author djabry
 */
public class NumberUnitFormat extends NumberFormat{
    
    private static final NumberFormat DEFAULT_FORMAT = NumberFormat.getInstance();
    private final String unitString;
    private final double min;
    private final double max;
    
    public NumberUnitFormat(String unitString,double min,double max ){
        
        this.min= min;
        this.max=max;
        this.unitString=" ("+unitString+")";

    }
    
   
    
    private StringBuffer doFormat(Number d,StringBuffer sb,FieldPosition fp){
        
        
       if(d.doubleValue()>max){
           
           d=max;
       }else if(d.doubleValue()<min){
           
           
           d=min;
       }
       
        StringBuffer f = DEFAULT_FORMAT.format(d, sb, fp);
        
        f.append(unitString);
        
        return f;
        
    }

    @Override
    public StringBuffer format(double d, StringBuffer sb, FieldPosition fp) {
       return this.doFormat(d, sb, fp); 
    }

    @Override
    public StringBuffer format(long l, StringBuffer sb, FieldPosition fp) {
        return this.doFormat(l, sb, fp); 
    }

    @Override
    public Number parse(String string, ParsePosition pp) {
        string.replace(unitString, "");
        return Double.valueOf(string);
    }
    
}
