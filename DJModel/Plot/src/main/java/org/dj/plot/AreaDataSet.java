/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.plot;

import org.dj.plot.api.Plottable;
import org.jfree.data.DomainOrder;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.xy.XYZDataset;

/**
 *
 * @author djabry
 */
public class AreaDataSet implements XYZDataset {
    
    private final Plottable p;
    
    public AreaDataSet(Plottable p){
        this.p=p;
        
    }

    @Override
    public Number getZ(int series, int item) {
        
        return new Double(getZValue(series,item));
        
    }

    @Override
    public double getZValue(int series, int item) {
        int xLoc = this.getDimLoc(0, item);
        int yLoc = this.getDimLoc(1, item);
        return p.getValueAt(series, new int[]{xLoc,yLoc});
        
    }

    @Override
    public DomainOrder getDomainOrder() {
        return DomainOrder.ASCENDING;
    }

    @Override
    public int getItemCount(int series) {
        
        int l = 1;
        for(int i = 0;i<p.getDimensions().length;i++){
            l=p.getDimensions()[i]*l;
        }
        
        return l;
    }

    @Override
    public Number getX(int series, int item) {

        int dim = 0;
        int loc = getDimLoc(dim,item);
        
        return loc;
        
    }
    
    private int getDimLoc(int dim, int item){
        
        int lengthY = p.getDimensions()[1];
        
        int loc = 0;
        
        if(dim==0){
            
            loc = item / lengthY;
        }else{
            
            
            loc = item % lengthY;
        }
        
        return loc;
        
    }

    @Override
    public double getXValue(int series, int item) {
        Integer loc = (Integer)getX(series,item);
        double x = p.getScaleOf(series, 0, loc);
       
        return x;
    }

    @Override
    public Number getY(int series, int item) {
       
        int dim = 1;
        int loc = getDimLoc(dim,item);
        return loc;
    }

    @Override
    public double getYValue(int series, int item) {
         
        int dim = 1;
        double y = p.getScaleOf(series, dim, (Integer)getY(series, item));
        return y;
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
    public int indexOf(Comparable seriesKey) {
        int numberOfSeries = p.getNumberOfSeries();
        
        for(int i =0;i<numberOfSeries;i++){
            if(seriesKey.equals(p.getSeriesKey(i))){
                
                return i;
            }
        }
        
        return -1;
    }

    @Override
    public void addChangeListener(DatasetChangeListener listener) {
       
    }

    @Override
    public void removeChangeListener(DatasetChangeListener listener) {
        
    }

    @Override
    public DatasetGroup getGroup() {
        return null;
    }

    @Override
    public void setGroup(DatasetGroup group) {
        
    }

    
}
