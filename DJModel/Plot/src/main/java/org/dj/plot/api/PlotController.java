/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.plot.api;

import javax.swing.JComponent;

/**
 *
 * @author djabry
 */
public interface PlotController {
    
    JComponent getPlot();
    void setTitle(String title);
    String getTitle();
    Plottable getPlottable();
    
    
}
