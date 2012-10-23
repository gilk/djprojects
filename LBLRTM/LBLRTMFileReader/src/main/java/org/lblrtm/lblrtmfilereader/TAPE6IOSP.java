/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilereader;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javolution.util.FastMap;
import org.lblrtm.file.api.TAPE6;
import org.lblrtm.file.api.TAPE6Manager;
import org.lblrtm.lblrtmdata.api.LBLRTMDimension;
import org.lblrtm.lblrtmdata.api.Molecule;
import org.lblrtm.lblrtmdata.api.VariableInfo;
import org.lblrtm.profile.api.PhysicalProperty;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import ucar.ma2.*;
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
public class TAPE6IOSP extends AbstractIOServiceProvider {

    private static final String TAPE6_LINE = "  TIME ENTERING LBLRTM";
    private TAPE6 t6;
    private static final TAPE6Manager t6Mgr = Lookup.getDefault().lookup(TAPE6Manager.class);
    private final Dimension levelDim = new Dimension(LBLRTMDimension.ALTITUDE.getFullName(), 1, true, false, false);
    private final Dimension layerDim = new Dimension(LBLRTMDimension.LAYER.getFullName(), 1, true, false, false);
    private final Dimension freqDim = new Dimension(LBLRTMDimension.FREQUENCY.getFullName(), 1, true, false, false);
    private boolean freqInfoUpdated = false;
    private Map<String, Map<Section, File>> varMap = new FastMap<String, Map<Section, File>>();
    private final Map<String, VariableInfo> variables = new FastMap<String, VariableInfo>();
    private static final double maxPossibleValue = 10000;

    public TAPE6IOSP() {
        variables.put(PhysicalProperty.ALTITUDE_FROM.name(), VariableInfo.ALTITUDE);
        variables.put(PhysicalProperty.ALTITUDE_TO.name(), VariableInfo.ALTITUDE);
        variables.put(PhysicalProperty.PRESSURE.name(), VariableInfo.PRESSURE);
        variables.put(PhysicalProperty.TEMPERATURE.name(), VariableInfo.TEMPERATURE);
    }

    @Override
    public boolean isValidFile(RandomAccessFile raf) throws IOException {

        //First character of line is cut off when using RandomAccessFile
        //Accept file if the fourth line contains the text "0  TIME ENTERING LBLRTM"

        this.t6 = t6Mgr.getTAPE6ForFile(new File(raf.getLocation()));
        if (this.t6.failed()) {

            return false;
        }

        String line = raf.readLine();
        if (line != null) {

            if (line.contains("LBLRTM")) {
                line = raf.readLine();

                if (line.contains(TAPE6_LINE)) {

                    if (!t6.failed()) {
                        return true;

                    }
                }

            }
        }
        return false;
    }

    @Override
    public void open(RandomAccessFile raf, NetcdfFile ncfile, CancelTask cancelTask) throws IOException {

        //Scan file for available molecules
        this.t6 = t6Mgr.getTAPE6ForFile(new File(raf.getLocation()));

        try {

            addAllVariables(ncfile);
        } catch (InvalidRangeException ex) {
            Exceptions.printStackTrace(ex);
        } finally {

            ncfile.finish();
            raf.close();
        }



    }

    private int extractLevel(String name) {

        if (t6.isAnalyticalJacobian()) {

            if (name.toLowerCase().contains("sfc")) {

                return 0;
            }

            if (name.toLowerCase().contains("rddn")) {

                return Integer.valueOf(name.substring(name.length() - 3));

            }

            //Take last three characters and convert to integer
            return Integer.valueOf(name.substring(name.length() - 3)) - 1;

        } else {

            if (t6.getDirection()) {
                //In the upwelling case return the highest level
                return t6.getOutputProfile().getNumberOfLevels() - 1;
            }

            //In the downwelling case return the bottom level
            return 0;
        }
    }

    private String extractVarName(String fName) {

        if (!t6.isAnalyticalJacobian()) {

            if (t6.getDirection()) {

                return "RDUP";

            } else {

                return "RDDN";
            }

        }

        if (fName.toLowerCase().contains("rddn")) {

            return "RDDN";
        }

        if (fName.toLowerCase().contains("rdup")) {

            return "RDUP";
        }


        return fName.substring(0, fName.length() - 4);
    }

    private void updateVariable(NetcdfFile ncfile, File f) throws IOException, InvalidRangeException {

        String name = f.getName();
        int level = extractLevel(name);
        String varName = this.extractVarName(name);
        Variable var = ncfile.findVariable(varName);

        if (!freqInfoUpdated) {

            String fPath = f.getPath();
            if (!f.exists()) {
                fPath = f.getPath() + ".nc";

            }
            NetcdfFile nf = NetcdfFile.open(fPath);
            Variable wVar = nf.findVariable(VariableInfo.WAVENUMBER.getLongName());

            if (wVar != null) {
                this.freqDim.setLength(wVar.getShape(0));
                ncfile.addDimension(null, freqDim);
                this.freqInfoUpdated = true;

                //Add frequencies
                updateVariable(null, VariableInfo.WAVENUMBER.getLongName(), f, ncfile, 0);
            }
        }

        updateVariable(var, varName, f, ncfile, level);


    }

    private void updateVariable(Variable var, String varName, File f, NetcdfFile ncfile, int level) throws InvalidRangeException {

        if (var == null) {

            Map fileMap = new FastMap<Section, File>();

            varMap.put(varName.toLowerCase(), fileMap);
            var = new Variable(ncfile, null, null, varName);

            if (varName.startsWith("LEV") || varName.toLowerCase().contains("rddn")) {
                var.setDimensions(Arrays.asList(new Dimension[]{levelDim, freqDim}));

            } else if (varName.toLowerCase().equals(VariableInfo.WAVENUMBER.getLongName().toLowerCase())) {

                var.setDimensions(Arrays.asList(new Dimension[]{freqDim}));

            } else {
                var.setDimensions(Arrays.asList(new Dimension[]{layerDim, freqDim}));
            }



            var.setDataType(DataType.DOUBLE);
            ncfile.addVariable(null, var);
        }

        Section s = new Section(new int[]{level, 0}, new int[]{1, freqDim.getLength()});

        if (varName.toLowerCase().equals(VariableInfo.WAVENUMBER.getLongName().toLowerCase())) {

            s = new Section(new int[]{0}, new int[]{freqDim.getLength()});
        }

        File o = f;

        if (!f.exists()) {

            o = new File(f.getPath() + ".nc");
        }

        this.varMap.get(varName.toLowerCase()).put(s, o);


    }

    private void addAllVariables(NetcdfFile ncfile) throws IOException, InvalidRangeException {

        addProfileVariables(ncfile);

        Iterator<File> iterator = t6.getOutputFiles();

        while (iterator.hasNext()) {

            File f = iterator.next();

            File fnc = new File(f.getPath() + ".nc");

            if (f.exists() || fnc.exists()) {

                updateVariable(ncfile, f);

            }
        }

    }

    private void addProfileVariables(NetcdfFile ncfile) {

        layerDim.setLength(t6.getOutputProfile().getNumberOfLevels());
        levelDim.setLength(t6.getOutputProfile().getNumberOfLevels() + 1);
        ncfile.addDimension(null, levelDim);
        ncfile.addDimension(null, layerDim);
        //ncfile.addDimension(null, freqDim);

        for (Molecule m : t6.getOutputProfile().getMolecules()) {

            variables.put(m.name(), VariableInfo.PPMV);
        }

        for (Entry<String, VariableInfo> varInfo : variables.entrySet()) {

            Variable var = new Variable(ncfile, null, null, varInfo.getKey());
            var.setDimensions(Arrays.asList(new Dimension[]{layerDim}));
            var.addAttribute(new Attribute("units", varInfo.getValue().getUnit().toString()));
            var.setDataType(DataType.DOUBLE);
            ncfile.addVariable(null, var);
        }
    }

    @Override
    public Array readData(Variable vrbl, Section sctn) throws IOException, InvalidRangeException {

        String varName = vrbl.getFullName().toLowerCase();
        Array out = Array.factory(DataType.DOUBLE, sctn.getShape());

        if (this.varMap.containsKey(varName)) {

            Iterator<Entry<Section, File>> iterator = this.varMap.get(varName).entrySet().iterator();

            while (iterator.hasNext()) {
                Entry<Section, File> fileSec = iterator.next();
                if (sctn.intersects(fileSec.getKey())) {

                    Section intersect = sctn.intersect(fileSec.getKey());
                    File f = fileSec.getValue();

                    NetcdfFile nf = NetcdfFile.open(f.getPath());

                    try {

                        String vName = VariableInfo.RADIANCE.getLongName();

                        if (vrbl.getFullName().toLowerCase().equals(VariableInfo.WAVENUMBER.getLongName().toLowerCase())) {

                            vName = VariableInfo.WAVENUMBER.getLongName();
                        }

                        Variable v = nf.findVariable(vName);
                        Section sec = new Section(Arrays.asList(new Range[]{intersect.getRange(intersect.getRank() - 1)}));


                        Array data = nf.getIosp().readData(v, sec);
                        IndexIterator in = data.getIndexIterator();

                        IndexIterator outIt = out.section(intersect.getOrigin(), intersect.getShape()).getIndexIterator();
                        double prev = Double.MIN_VALUE;



                        while (in.hasNext() && outIt.hasNext()) {
                            in.next();
                            outIt.next();

                            //Check for incorrect values - All values should be <<1
                            //Assign value of previous element 
                            Double val = in.getDoubleCurrent();

                            if (val > maxPossibleValue || val.isInfinite() || val.isNaN() || val < -1.0) {
                                val = prev;
                            }

                            outIt.setDoubleCurrent(val);
                            prev = val;
                        }
                    } finally {

                        nf.close();
                    }

                }
            }

        } else {

            out = this.t6.getOutputProfile().getArrayForVariable(varName.toUpperCase(), sctn);
        }

        return out;


    }

    @Override
    public String getFileTypeId() {
        return null;
    }

    @Override
    public String getFileTypeDescription() {
        String out = "LBLRTM output diagnostic file";

        return out;
    }
}
