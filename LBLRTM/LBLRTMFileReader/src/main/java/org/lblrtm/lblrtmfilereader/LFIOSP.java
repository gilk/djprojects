/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilereader;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.*;
import javolution.util.FastList;
import javolution.util.FastMap;
import javolution.util.FastSet;
import org.lblrtm.lblrtmdata.api.LBLRTMDimension;
import org.lblrtm.lblrtmdata.api.VariableInfo;
import org.lblrtm.lblrtmfilereader.api.FileReaderProperties;
import org.lblrtm.lblrtmfilereader.api.NumberMode;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import ucar.ma2.Array;
import ucar.ma2.InvalidRangeException;
import ucar.ma2.Range;
import ucar.ma2.Section;
import ucar.nc2.Attribute;
import ucar.nc2.Dimension;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;
import ucar.nc2.iosp.AbstractIOServiceProvider;
import ucar.nc2.iosp.IOServiceProvider;
import ucar.nc2.util.CancelTask;
import ucar.unidata.io.RandomAccessFile;

/**
 *
 * @author djabry
 */
@ServiceProvider(service = IOServiceProvider.class)
public class LFIOSP extends AbstractIOServiceProvider {

    private NumberMode numberMode = NumberMode.DOUBLE_MODE;
    private final Map<HeaderVariable, Integer> headerVars= new EnumMap<HeaderVariable, Integer>(HeaderVariable.class);
    private final Map<Section, Panel> panels= new FastMap<Section, Panel>();
    private final List<VariableInfo> variables= new FastList<VariableInfo>();
    private static final FileReaderProperties fRP = Lookup.getDefault().lookup(FileReaderProperties.class);
    private boolean endFlag = false;
    private int vCount = 0;



    private int getLevelIndex() {

        return this.headerVars.get(HeaderVariable.LEVEL_INDEX);
    }

    private void readHeaderInfo(RandomAccessFile raf) throws IOException {
        for (HeaderVariable hV : HeaderVariable.values()) {
            raf.seek((hV.getPositionInHeader() - 1) * 4);
            this.headerVars.put(hV, raf.readInt());
        }

    }

    private boolean checkLength(RandomAccessFile raf, int nPts) throws IOException {

        int len = raf.readInt();
        if (len == numberMode.getMultiplier() * nPts) {
            return true;
        }

        return false;
    }

    private void readPanelInfo(RandomAccessFile raf) throws IOException, InvalidRangeException {

        //raf.readInt();

        double v1 = 0.0;
        double v2 = 0.0;
        double dv = 0.0;
        
        int nPts = 0;


        v1 = raf.readDouble();
        v2 = raf.readDouble();

        if (this.numberMode.equals(NumberMode.DOUBLE_MODE)) {

            dv = raf.readDouble();
            nPts = ((Long) raf.readLong()).intValue();


        } else {
            dv = raf.readFloat();
            nPts = raf.readInt();

        }

        if (nPts != 2400) {

            endFlag = true;
        }


        raf.readInt();

        Map<VariableInfo, Long> vars = new EnumMap<VariableInfo, Long>(VariableInfo.class);

        Iterator<VariableInfo> iterator = variables.iterator();

        while (true) {

            if (!checkLength(raf, nPts)) {

                break;
            }

            if (iterator.hasNext()) {

                VariableInfo vInfo = iterator.next();
                vars.put(vInfo, raf.getFilePointer());
            }

            raf.skipBytes(nPts * numberMode.getMultiplier());

            if (!checkLength(raf, nPts)) {

                break;
            }


        }
        
        int lastPoint = vCount+nPts-1;

        Range r = new Range(vCount, lastPoint);
        
        
        Section s = new Section(Arrays.asList(new Range[]{r}));

        vCount += nPts;

        panels.put(s, new PanelImpl(vars, nPts, v1, v2, dv, raf, numberMode));


    }

    private void readPanels(RandomAccessFile raf) throws IOException, InvalidRangeException {
        raf.seek(numberMode.getShift() * 4);
        raf.readInt();
        while (!endFlag) {
            readPanelInfo(raf);
        }

    }



    
    
    @Override
    public void open(RandomAccessFile raf, NetcdfFile ncfile, CancelTask cancelTask) throws IOException {
        try {

            this.openFile(raf);
            this.readHeaderInfo(raf);
            this.variables.addAll(fRP.getVariables());
            this.variables.remove(VariableInfo.WAVENUMBER);
            this.readPanels(raf);
            //super.open(raf, ncfile, cancelTask);

            List<Dimension> dims = Arrays.asList(new Dimension[]{
                        new Dimension(LBLRTMDimension.FREQUENCY.getFullName(), vCount, true, true, false)});

            for (VariableInfo v : this.getVariables()) {
                Variable var = this.generateVariable(ncfile, v);
                var.setDimensions(dims);
                ncfile.addVariable(null, var);
            }
            
            ncfile.addDimension(null, dims.get(0));

            ncfile.finish();

        } catch (InvalidRangeException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public boolean isValidFile(RandomAccessFile raf) throws IOException {

        if (this.openFile(raf)) {
            raf.seek(0);
            int startInt = raf.readInt();

            if (startInt == 1416) {

                return true;
            }

        }
        return false;
    }

    private boolean openFile(RandomAccessFile raf) throws IOException {

        try {

            raf.order(RandomAccessFile.LITTLE_ENDIAN);

            raf.seek(numberMode.getShift() * 4);
            int val = raf.readInt();

            if (val != 24 && val != 32) {
                //Change endian   
                raf.order(RandomAccessFile.BIG_ENDIAN);
            }

            raf.seek(numberMode.getShift() * 4);
            val = raf.readInt();

            if (val != 24 && val != 32) {

                return false;
            }


            if (val == 24) {

                this.numberMode = NumberMode.FLOAT_MODE;
            } else {

                this.numberMode = NumberMode.DOUBLE_MODE;
            }

            return true;


        } catch (Exception e) {

            return false;
        }



    }
    
    private VariableInfo forName(String name){
        
        String nameIn = name.toUpperCase().trim();
        
        return VariableInfo.valueOf(nameIn);
        
    }

    private void readSectionIntoArray(Panel p, Section s, String varName, int startPoint, Array arr) {

        VariableInfo vInfo = forName(varName);

        
        int index = startPoint;

        if (p.getVariables().contains(vInfo)) {

            Range.Iterator iterator = s.getRange(0).getIterator();

            while (iterator.hasNext()) {

                int next = iterator.next();
                Number val = p.getPoint(vInfo, index);
                arr.setObject(next, val);
                index++;
            }

        }

    }

    @Override
    public Array readData(Variable vrbl, Section sctn) throws IOException, InvalidRangeException {

        int[] shape = sctn.getShape();
        Array arr = Array.factory(numberMode.getDataType(), shape);

        Iterator<Entry<Section, Panel>> iterator = panels.entrySet().iterator();

        while (iterator.hasNext()) {

            Entry<Section, Panel> next = iterator.next();
            Panel p = next.getValue();
            Section s = next.getKey();

            if (sctn.intersects(s)) {


                Section intersect = sctn.intersect(s);
                int startPoint = intersect.getRange(0).first() - s.getRange(0).first();
                readSectionIntoArray(p, intersect, vrbl.getName(), startPoint, arr);

            }

        }

        return arr;
    }

    private Variable generateVariable(NetcdfFile nf, VariableInfo varInfo) {

        Variable var = new Variable(nf, null, null, varInfo.getLongName());

        var.addAttribute(new Attribute("units", varInfo.getUnit().toString()));
        var.addAttribute(new Attribute("long_name", varInfo.getLongName()));
        var.setDataType(numberMode.getDataType());

        return var;

    }

    private Set<VariableInfo> getVariables() {

        Set<VariableInfo> varsOut = new FastSet<VariableInfo>();
        varsOut.addAll(variables);
        varsOut.add(VariableInfo.WAVENUMBER);

        return varsOut;
    }

//    @Override
//    public Object sendIospMessage(Object message) {
//
//        if (message instanceof VariableInfo[]) {
//            VariableInfo[] variableInfo = (VariableInfo[]) message;
//            this.variables = Arrays.asList(variableInfo);
//
//        }
//
//        return super.sendIospMessage(message);
//    }

    @Override
    public String getFileTypeId() {
        return null;
    }

    @Override
    public String getFileTypeDescription() {
        return "LBLRTM line file";
    }
}
