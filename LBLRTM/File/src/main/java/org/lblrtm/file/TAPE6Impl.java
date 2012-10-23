/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.file;

import com.google.common.collect.Iterators;
import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import javolution.util.FastList;
import javolution.util.FastMap;
import javolution.util.FastSet;
import org.dj.filter.api.Filter;
import org.lblrtm.file.api.AJOutputFilePrefix;
import org.lblrtm.file.api.TAPE6;
import org.lblrtm.lblrtmdata.api.AJParameter;
import org.lblrtm.lblrtmdata.api.LBLRTMUnit;
import org.lblrtm.lblrtmdata.api.Molecule;
import org.lblrtm.profile.api.Level;
import org.lblrtm.profile.api.PhysicalProperty;
import org.lblrtm.profile.api.Profile;
import ucar.ma2.*;

/**
 *
 * @author djabry
 */
public class TAPE6Impl implements TAPE6 {

    //private File file;
    private Profile profile;
    private File t6;
    private LineNumberReader reader;
    private Range rowLimits;
    private int profileLineNumber;
    private final Map<String, Range> columnRanges = new FastMap<String, Range>();
    private final Map<String, Molecule> molecules = new FastMap<String, Molecule>();
    //private static final Set<String> AJ_PREF;
    //private final Set<String> aJOuptutFilePrefs = new FastSet<String>();
    //private String aJMol;
    private final EnumSet<AJParameter> aJParams = EnumSet.noneOf(AJParameter.class);
    private final Set<String> numberedPrefixes = new FastSet<String>();
    private final Set<String> constantFiles = new FastSet<String>();
    private static final Map<String, LBLRTMUnit> units;

    private boolean isAJ;
    private boolean failed;
    private double angle;

    static {

//        AJ_PREF = new FastSet<String>();
//        AJ_PREF.add("AJ/RDderivUPW_");
//        AJ_PREF.add("AJ/RDderivDNW_");
//        AJ_PREF.add("ODint_");
//        AJ_PREF.add("RDDNlayer_");
//        AJ_PREF.add("RDUPlayer_");

        units = new FastMap<String, LBLRTMUnit>();
        units.put(PhysicalProperty.ALTITUDE_FROM.name(), LBLRTMUnit.KILOMETRE);
        units.put(PhysicalProperty.ALTITUDE_TO.name(), LBLRTMUnit.KILOMETRE);
        units.put(PhysicalProperty.PRESSURE.name(), LBLRTMUnit.MILLIBAR);
        units.put(PhysicalProperty.TEMPERATURE.name(), LBLRTMUnit.KELVIN);
        

    }

    @Override
    protected void finalize() throws Throwable {


        close();

        super.finalize();
    }

    @Override
    public Iterator<File> getOutputFiles() {

       return Iterators.concat(new ConstantFileIterator(), new NumberedFileIterator());
    }

    @Override
    public boolean isAnalyticalJacobian() {
        return this.isAJ;
    }

    @Override
    public boolean failed() {
        return failed;
    }

    @Override
    public double getAngle() {
        return angle;
    }


    @Override
    public boolean getDirection() {
        return getAngle() > 90.0;
    }

    @Override
    public EnumSet<AJParameter> getAJParameters() {
        return EnumSet.copyOf(aJParams);
    }

    private class NumberedFileIterator implements Iterator<File> {

        private final String parentDir = t6.getParent();
        private final int limit = (numberedPrefixes.size() * profile.getNumberOfLevels()) + aJParams.size();
        private int cIndex = -1;
        private int currentLevel = -1;
        //private final Iterator<Integer> rangeIterator = Ranges.closed(0, profile.getNumberOfLevels()).asSet(DiscreteDomains.integers()).iterator();
        private final Iterator<String> prefixIterator = Iterators.cycle(numberedPrefixes);

        @Override
        public boolean hasNext() {
            return cIndex < limit-1;
        }

        @Override
        public File next() {

            cIndex++;

            if (cIndex % numberedPrefixes.size() == 0) {
                currentLevel++;
            }

            String pref = prefixIterator.next();

            while (currentLevel == profile.getNumberOfLevels() && !pref.startsWith("AJ/LEV")) {

                pref = prefixIterator.next();
            }

            File out = new File(parentDir + File.separator + pref + generateLevelString(currentLevel));
            //System.out.println("TAPE6 output file " + out.getPath());

            return out;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    private class ConstantFileIterator implements Iterator<File> {

        private final String parentDir = t6.getParent();
        private final Iterator<String> iterator = constantFiles.iterator();

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public File next() {
            return new File(parentDir + File.separator + iterator.next());
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }


    private String generateLevelString(int level) {

        String levelString = String.format("%03d", level + 1);
        return levelString;

    }







    @Override
    public File getFile() {
        return this.t6;
    }

    private String findLineContaining(String val) throws FileNotFoundException, IOException {
        String line = "";
        BufferedReader rdr = new BufferedReader(new InputStreamReader(new FileInputStream(t6)));
        while (rdr.ready() && line != null) {
            line = rdr.readLine();
            if (line.contains(val)) {

                return line;
            }

        }

        return null;

    }

    private double findAngle() throws FileNotFoundException, IOException {

        String angHead = "ANGLE   =";
        String l = findLineContaining(angHead);
        String[] split = l.split(angHead);
        String angVal = split[split.length - 1];
        angVal = angVal.trim().replaceAll("DEG", "").trim();
        return Double.valueOf(angVal);

    }

    private void scanForOutputFiles() throws FileNotFoundException, IOException {
        List<String> prefs = AJOutputFilePrefix.getAJFilePrefixes();
        BufferedReader rdr = new BufferedReader(new InputStreamReader(new FileInputStream(t6)));

        String line = "";
        while (rdr.ready() && line != null && !prefs.isEmpty()) {
            line = rdr.readLine();
            Iterator<String> iterator = prefs.iterator();
            while (iterator.hasNext()) {
                String prefix = iterator.next();

                if (line.contains(prefix)) {

                    this.numberedPrefixes.add(prefix);
                    prefs.remove(prefix);

                    if (prefix.startsWith("AJ")) {
                        AJParameter p = this.findAJParameter(prefix);
                        this.aJParams.add(p);
                        //Add the level files
                        this.numberedPrefixes.add(prefix.replace("AJ/", "AJ/LEV_"));
                    }
                }

            }

        }

        if (this.numberedPrefixes.isEmpty()) {
            isAJ = false;
            this.constantFiles.add(TAPE6.RADIANCE_OUTPUT_FILE);
        } else {
            isAJ = true;
            this.constantFiles.add("RDDN_sfc");
        }

        rdr.close();
    }

    private AJParameter findParameterForIndex(Integer index) {

        for (AJParameter p : AJParameter.values()) {
            if (p.parameterIndex == index) {
                return p;
            }

        }

        return null;

    }

    private AJParameter findAJParameter(String prefix) {
        String aJIndex = findAJIndex(prefix);
        Integer index = Integer.parseInt(aJIndex);
        return findParameterForIndex(index);
    }

    private String findAJIndex(String prefix) {

        String indexString = prefix.replace("AJ/RDderiv", "").replace("_", "").replace("UPW", "").replace("DNW", "");
        return indexString;

    }

    private boolean scanForFailure() throws FileNotFoundException, IOException {
        String line = "";
        //boolean failed = true;
        BufferedReader rdr = new BufferedReader(new InputStreamReader(new FileInputStream(t6)));
        while (rdr.ready() && line != null) {
            line = rdr.readLine();
            if (line.contains("Modules and versions used in this calculation")) {

                rdr.close();
                return false;
            }
        }

        rdr.close();
        return true;



    }

    public TAPE6Impl(File t6) throws IOException, FileNotFoundException {


        try {
            this.t6 = t6;
            failed = scanForFailure();

            if (!failed) {
                scanForProfile();
                scanForOutputFiles();
                angle = findAngle();

                this.columnRanges.put(PhysicalProperty.PRESSURE.name(), new Range(24, 40));
                this.columnRanges.put(PhysicalProperty.TEMPERATURE.name(), new Range(40, 50));
                this.profile = new T6Profile();

                //this.columnRanges.put(LBLRTMProfile.PROP_ALTITUDE,new Range(5,12));
                this.columnRanges.put(PhysicalProperty.ALTITUDE_FROM.name(), new Range(5, 12));
                this.columnRanges.put(PhysicalProperty.ALTITUDE_TO.name(), new Range(15, 22));

            }


        } catch (InvalidRangeException ex) {
        }


    }

    @Override
    public void close() throws IOException {
        this.reader.close();
        
    }

    class T6Profile implements Profile {

        @Override
        public int getNumberOfLevels() {
            return rowLimits.length();
        }

        @Override
        public Set<Molecule> getMolecules() {
            return new HashSet(molecules.values());
        }

        @Override
        public Set<String> getVariables() {
            return columnRanges.keySet();
        }

        @Override
        public Array getProfileForVariable(String name) {
            Section s = new Section();
            s.appendRange(this.getLimitsForVariable(name));
            return this.getArrayForVariable(name, s);
        }

        @Override
        public Array getArrayForVariable(String name, Section section) {

            Array data = Array.factory(DataType.DOUBLE, section.getShape());

            Range r = section.getRanges().iterator().next();

            if (r.length() > 0) {

                Range.Iterator iterator = r.getIterator();
                while (iterator.hasNext()) {

                    int next = iterator.next();
                    data.setDouble(next, this.getValueForVariable(name, next));
                }

            }

            return data;
        }

        @Override
        public Range getLimitsForVariable(String name) {
            try {
                return new Range(0, this.getNumberOfLevels() - 1);
            } catch (InvalidRangeException ex) {
            }

            return Range.EMPTY;
        }

        @Override
        public double getValueForVariable(String name, int level) {
            try {
                Range c = columnRanges.get(name);
                return readValueForRC(level, c);
            } catch (IOException ex) {
                Logger.getLogger(TAPE6Impl.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }

            return 0.0;
        }

        @Override
        public void setValueForVariable(String name, int level, double value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public LBLRTMUnit getUnitForVariable(String name, int level) {

            LBLRTMUnit u = units.get(name);

            if (u == null) {
                u = LBLRTMUnit.VOLUME_MIXING_RATIO;
            }

            return u;


        }

        @Override
        public void setUnitForVariable(String name, int level, LBLRTMUnit unit) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Profile duplicate() {
            return this;
        }

        @Override
        public Set<PhysicalProperty> getPhysicalProperties() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getVariableFullName(String variable) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Profile section(Filter<Level> levelFilter) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    private void scanForProfile() throws FileNotFoundException, IOException, InvalidRangeException {

        String line = "";

        reader = new LineNumberReader(new InputStreamReader(new FileInputStream(t6)));
        while (!line.contains("  MIXING RATIOS BY LAYER")) {
            line = reader.readLine();
        }

        String columnHeaders = reader.readLine();
        readColumnHeader(columnHeaders);
        reader.mark(50000);

        String s = "";

        this.profileLineNumber = reader.getLineNumber() + 1;

        while (!s.contains("1")) {
            line = reader.readLine();
            s = line.substring(0, 1);
        }

        this.rowLimits = new Range(this.profileLineNumber, reader.getLineNumber() - 1);

    }

    private Molecule matchMolecule(String molString) {

        List<Molecule> mols = new FastList<Molecule>(Arrays.asList(Molecule.values()));
        return matchMolecule(molString, mols);
    }

    private Molecule matchMolecule(String molString, List<Molecule> mols) {

        for (Molecule m : Molecule.values()) {

            if (molString.contains(m.toString())) {

                mols.remove(m);
                return m;
            }
        }

        return null;

    }

    private void readColumnHeader(String h) throws InvalidRangeException {

        Range r = new Range(55, 70);

        List<Molecule> mols = new FastList<Molecule>(Arrays.asList(Molecule.values()));

        while (r.last() < h.length()) {
            String molString = h.substring(r.first(), r.last()).trim();

            Molecule m = matchMolecule(molString, mols);
            if (m != null) {
                this.columnRanges.put(m.name(), r);
                this.molecules.put(m.name(), m);
            }
            r = new Range(r.first() + 15, r.last() + 15);
        }
    }

    @Override
    public Profile getOutputProfile() {
        return profile;
    }

    double readValueForRC(int row, Range column) throws IOException {

        String line = "";

        int transformedRow = rowLimits.first() + row;

        if (reader.getLineNumber() >= transformedRow) {

            reader.reset();
            line = reader.readLine();

        }

        while (reader.getLineNumber() < transformedRow) {

            line = reader.readLine();
        }

        double val = transformValueForColumn(
                Double.valueOf(line.substring(column.first(), column.last())), column);

        return val;

    }

    private double transformValueForColumn(double in, Range column) {

        //Convert to PPMV if value is related to a molecule
        if (column.first() >= 55) {

            return 1.0e6 * in;
        }

        return in;

    }
}
