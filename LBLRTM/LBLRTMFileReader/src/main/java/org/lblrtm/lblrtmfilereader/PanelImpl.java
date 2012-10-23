/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilereader;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import javolution.util.FastSet;
import org.lblrtm.lblrtmdata.api.VariableInfo;
import org.lblrtm.lblrtmfilereader.api.NumberMode;
import org.openide.util.Exceptions;
import ucar.unidata.io.RandomAccessFile;

/**
 *
 * @author djabry
 */
public class PanelImpl implements Panel {

    private Map<VariableInfo, Long> variables;
    private int numberOfPoints;
    private RandomAccessFile raf;
    private double v1;
    private double v2;
    private double dv;
    private NumberMode nM;

    public PanelImpl(Map<VariableInfo, Long> vars, int numberOfPoints, double v1, double v2, double dv,
            RandomAccessFile raf, NumberMode nM) {

        this.variables = vars;
        this.numberOfPoints = numberOfPoints;
        this.v1 = v1;
        this.v2 = v2;
        this.dv = dv;
        this.raf = raf;
        this.nM = nM;
    }

    @Override
    public Number getPoint(VariableInfo varInfo, int point) {

        double out = Double.NaN;

        try {

            if (varInfo.equals(VariableInfo.WAVENUMBER)) {

                out = v1 + dv * point;
            } else {

                Long startPos = this.variables.get(varInfo);
                raf.seek(startPos);
                raf.skipBytes(point * nM.getMultiplier());

                if (nM.equals(NumberMode.DOUBLE_MODE)) {
                    out = raf.readDouble();
                } else {
                    out = raf.readFloat();
                }

            }

        } 
        catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        return out;
    }

    /**
     * @return the variables
     */
    @Override
    public Set<VariableInfo> getVariables() {
        
        Set<VariableInfo> varsOut = new FastSet<VariableInfo>();
        varsOut.addAll(this.variables.keySet());
        varsOut.add(VariableInfo.WAVENUMBER);

        return varsOut;
    }

    /**
     * @return the numberOfPoints
     */
    @Override
    public int getNumberOfPoints() {
        return numberOfPoints;
    }

    /**
     * @return the v1
     */
    @Override
    public double getV1() {
        return v1;
    }

    /**
     * @return the v2
     */
    @Override
    public double getV2() {
        return v2;
    }

    /**
     * @return the dv
     */
    @Override
    public double getDV() {
        return dv;
    }
}
