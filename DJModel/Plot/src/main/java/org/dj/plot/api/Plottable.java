/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.plot.api;


import com.google.common.collect.Range;
import java.io.IOException;
import org.dj.domainmodel.api.DJObject;

/**
 *
 * @author djabry
 */
public interface Plottable extends DJObject{
    
    int[] getDimensions();
    int getNumberOfSeries();
    Comparable getSeriesKey(int series);
    
    double getValueAt(int series, int[] loc);
    Range<Double> getRangeOfValues(int series);
    double getScaleOf(int series, int dim, int loc);
    
    
    String getScaleTitle(int dimension);
    void setScaleTitle(int dimension, String title);
    
    String getAxisTitle(int dimension);
    void setAxisTitle(int dimension, String title);
    
    String getChartTitle();
    void setChartTitle(String title);
    
    public void exportData() throws IOException;
    
    
    
    
}
