/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.matrix.editor;

import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import java.io.IOException;
import java.util.List;
import org.dj.domainmodel.api.DJObjectAbstr;
import org.dj.matrix.api.DJMatrix;
import org.dj.plot.api.Plottable;

/**
 *
 * @author djabry
 */
public class MatrixPlotWrapper extends DJObjectAbstr implements Plottable {
    
    
    private final List<DJMatrix> mats;
    public MatrixPlotWrapper(DJMatrix m){
        
        this(Lists.newArrayList(m));
    }
    
    public MatrixPlotWrapper(List<DJMatrix> mats){
        this.mats=mats;
        
    }

    @Override
    public int[] getDimensions() {
        
        DJMatrix m = mats.get(0);
       return new int[]{m.getNumberOfRows(),m.getNumberOfColumns()};
    }

    @Override
    public int getNumberOfSeries() {
       return mats.size();
    }

    @Override
    public Comparable getSeriesKey(int series) {
        return series;
    }

    @Override
    public double getValueAt(int series, int[] loc) {
        return mats.get(series).getValueAt(loc[0], loc[1]);
    }

    @Override
    public Range<Double> getRangeOfValues(int series) {
       return mats.get(series).getMatrixStorage().findValueRange();
    }

    @Override
    public double getScaleOf(int series, int dim, int loc) {
        return loc;
    }

    @Override
    public String getScaleTitle(int dimension) {
        return mats.get(0).getDimensionName(dimension);
    }

    @Override
    public void setScaleTitle(int dimension, String title) {
        mats.get(0).setDimensionName(dimension, title);
    }

    @Override
    public String getAxisTitle(int dimension) {
        return mats.get(0).getDimensionName(dimension);
    }

    @Override
    public void setAxisTitle(int dimension, String title) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getChartTitle() {
        return mats.get(0).getTitle();
    }

    @Override
    public void setChartTitle(String title) {
        mats.get(0).setTitle(title);
    }

    @Override
    public void exportData() throws IOException {
        mats.get(0).saveToFile();
    }
    
}
