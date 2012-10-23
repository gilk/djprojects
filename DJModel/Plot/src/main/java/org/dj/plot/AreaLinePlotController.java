/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.plot;

import java.util.HashSet;
import java.util.Set;
import javax.swing.JComponent;
import org.dj.plot.api.PlotController;
import org.dj.plot.api.Plottable;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.StandardTickUnitSource;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.GrayPaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.data.Range;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author djabry
 */
public class AreaLinePlotController implements PlotController {

    private final XYDataset d;
    private final JFreeChart c;
    private final Plottable p;
    private DJChartPanel comp;

    public AreaLinePlotController(Plottable p) {
        this.p = p;
        

        if (p.getDimensions().length == 1) {
            this.d = new LineDataSet(p);

            this.c = ChartFactory.createXYLineChart(p.getChartTitle(), p.getAxisTitle(0), p.getAxisTitle(1), (LineDataSet) d, PlotOrientation.VERTICAL, true, true, false);
        } else {
            this.d = new AreaDataSet(p);
            NumberAxis xAxis = new NumberAxis(p.getAxisTitle(0));
            xAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
            NumberAxis yAxis = new NumberAxis(p.getAxisTitle(1));
            yAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
            XYBlockRenderer renderer = new XYBlockRenderer();
            com.google.common.collect.Range<Double> r = p.getRangeOfValues(0);
            renderer.setPaintScale(new GrayPaintScale(r.lowerEndpoint(), r.upperEndpoint()));
            
            XYPlot plot = new XYPlot();
            plot.setDataset(d);
            plot.setRenderer(renderer);
            plot.setDomainAxis(xAxis);
            plot.setRangeAxis(yAxis);

            this.c = new JFreeChart(p.getChartTitle(),plot);
            c.removeLegend();

        }

        Set<ValueAxis> axes = new HashSet<ValueAxis>();
        
        
        axes.add(c.getXYPlot().getDomainAxis());
        axes.add(c.getXYPlot().getRangeAxis());

        for (ValueAxis vA : axes) {
            double s = vA.getAutoRangeMinimumSize();
            Range range = vA.getRange();
            double cent = range.getCentralValue();
            if (cent < 0.01 || cent > 1.0E3) {

                vA.setStandardTickUnits(new StandardTickUnitSource());
            }
            
            
            ((NumberAxis)vA).setAutoRange(true);
            

        }


    }

    @Override
    public JComponent getPlot() {

        return this.getChartPanel();

    }

    private DJChartPanel getChartPanel() {

        if (comp == null) {
            comp = new DJChartPanel(c,p);

        }
        return comp;
    }

    @Override
    public void setTitle(String title) {
        this.p.setChartTitle(title);
    }

    @Override
    public String getTitle() {
        return this.p.getChartTitle();
    }

    @Override
    public Plottable getPlottable() {
        return p;
    }
}
