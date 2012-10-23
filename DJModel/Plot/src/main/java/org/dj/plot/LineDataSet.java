/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.plot;

import org.dj.plot.api.Plottable;
import org.jfree.data.xy.AbstractIntervalXYDataset;

/**
 *
 * @author djabry
 */
public class LineDataSet extends AbstractIntervalXYDataset {

        private final Plottable p;
        
        //Number of dimensions == 1 since this is an x-y plot
        public Plottable getPlottable(){
            
            return this.p;
        }
        
        
        public LineDataSet(Plottable p){

            this.p=p;
        }

        @Override
        public int getSeriesCount() {
            return p.getNumberOfSeries();
        }

        @Override
        public Comparable getSeriesKey(int series) {
            return p.getSeriesKey(series);
        }

        @Override
        public int getItemCount(int series) {
            return p.getDimensions()[0];
        }

        @Override
        public Number getX(int series, int item) {
            return p.getScaleOf(series, 0,item); 
        }

        @Override
        public Number getY(int series, int item) {
            return p.getValueAt(series, new int[]{item});
        }

        @Override
        public Number getStartX(int series, int item) {
           return getX(series, item);
        }

        @Override
        public Number getEndX(int series, int item) {
            return getX(series, item);
        }

        @Override
        public Number getStartY(int series, int item) {
            return getY(series,item);
        }

        @Override
        public Number getEndY(int series, int item) {
            return getY(series, item);
        }
        
        
        
    }
