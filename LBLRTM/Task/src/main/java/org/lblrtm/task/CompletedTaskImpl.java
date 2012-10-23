/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.task;

import com.google.common.collect.Range;
import com.google.common.collect.Ranges;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javolution.util.FastList;
import javolution.util.FastSet;
import org.dj.domainmodel.api.DJObjectAbstr;
import org.dj.index.api.IntegerIndices;
import org.dj.matrix.api.DJMatrix;
import org.dj.matrix.api.DJMatrixIterator;
import org.dj.matrix.api.MatrixFactory;
import org.dj.matrix.api.MatrixFileType;
import org.dj.parallel.api.FileMerger;
import org.dj.parallel.api.GCompletedTaskAbstr;
import org.lblrtm.file.api.TAPE6;
import org.lblrtm.file.api.TAPE6Manager;
import org.lblrtm.host.api.LBLRTMHostInfo;
import org.lblrtm.lblrtmdata.api.AJParameter;
import org.lblrtm.lblrtmdata.api.LBLRTMDimension;
import org.lblrtm.lblrtmdata.api.VariableInfo;
import org.lblrtm.lblrtmfilewriter.api.AJUnits;
import org.lblrtm.lblrtmfilewriter.api.CalculationTemplate;
import org.lblrtm.lblrtmfilewriter.api.Converter;
import org.lblrtm.lblrtmfilewriter.api.RadianceUnits;
import org.lblrtm.profile.api.PhysicalProperty;
import org.lblrtm.profile.api.Profile;
import org.lblrtm.task.api.LBLRTMParallelTask;
import org.lblrtm.task.api.LBLRTMTask;
import org.lblrtm.task.api.TaskResult;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import ucar.ma2.Array;
import ucar.ma2.IndexIterator;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.Dimension;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

/**
 *
 * @author djabry
 */
public class CompletedTaskImpl extends GCompletedTaskAbstr<LBLRTMTask, LBLRTMHostInfo, TaskResult> {

    private final TaskResult r;
    private final TAPE6 t6;
    private static final TAPE6Manager mgr = Lookup.getDefault().lookup(TAPE6Manager.class);
    private static final FileMerger fM = Lookup.getDefault().lookup(FileMerger.class);
    private static final MatrixFactory mF = Lookup.getDefault().lookup(MatrixFactory.class);
    private static final IntegerIndices iI = Lookup.getDefault().lookup(IntegerIndices.class);
    private static final List<LBLRTMDimension> dims = Arrays.asList(new LBLRTMDimension[]{LBLRTMDimension.FREQUENCY, LBLRTMDimension.ALTITUDE});
    //private final List<String> axesTitles = new FastList<String>();
    //private final List<String> chartTitles = new FastList<String>();
    private Range<Double> jacRange;
    private Range<Double> radRange;
    private Range<Double> wnRange;

    public CompletedTaskImpl(LBLRTMTask tsk, Integer t) throws IOException {

        super(tsk, t);
        this.r = new TaskResultImpl(tsk.getOutputFiles());
        this.t6 = mgr.getTAPE6ForFile(new File(tsk.getLocalDir().getPath() + File.separator + "TAPE6"));


    }
    
    
    private String getAxisUnits(int dim){
        
        String title = "(cm\u207B\u00B9)";

       
           if(dim ==1){
           
           
                if(t6.isAnalyticalJacobian()){
                
                    title = "(Km)";
               
               
                }else{
                    
                   title = "(W/[cm\u00b2 cm\u207B\u00B9 sr])";
                   CalculationTemplate cT = getOriginalTask().getTAPE5().getCalculationTemplate();
                   RadianceUnits radianceOutputUnits = cT.getRadianceOutputUnits();
                   
                   if(radianceOutputUnits.equals(RadianceUnits.BT)){
                       
                       title ="(K)";
                   }
                   
               
                    
                }
           
           
            }
           
           return title;

        
    }
    
    

    @Override
    public TaskResult getResult() {
        return r;
    }

    class TaskResultImpl extends DJObjectAbstr implements TaskResult {
        
        @Override
        public String getAxisTitle(int dim){
        
        String dimTitle = "Wavenumber ";
        
        
        if(dim==1){
            
            if(t6.isAnalyticalJacobian()){
                
                dimTitle = "Altitude ";
                
            }else{

                dimTitle = getOriginalTask().getTAPE5().getCalculationTemplate().getRadianceOutputUnits().fullName+" ";

            }
            
        }

        return dimTitle+getAxisUnits(dim);
        
        
    }
    
        @Override
    public String getChartTitle(){
        
        String chartTitle = "Radiance";
        
        CalculationTemplate cT = getOriginalTask().getTAPE5().getCalculationTemplate();
        
        if(t6.isAnalyticalJacobian()){
            
            chartTitle = "Jacobian ("+cT.getJacobianOutputUnits()+")";
            
        }else{
            RadianceUnits radianceOutputUnits = cT.getRadianceOutputUnits();
            if(radianceOutputUnits.equals(RadianceUnits.BT)){
                
                chartTitle = radianceOutputUnits.fullName;
            }
            
        }
        
        return chartTitle;

    }

        private DJMatrix resultMat;
        private DJMatrix wn;
        private double minTemp = 0.0;
        private double maxTemp = 0.0;
        private final Set<File> files;
        //private static final FileUtilities fU = Lookup.getDefault().lookup(FileUtilities.class);

        public TaskResultImpl(Set<File> files) {
            this.files = files;
        }

        private double getMinTemp() {

            if (minTemp < 1.0) {


                minTemp = 500.0;
                Profile profile = t6.getOutputProfile();
                for (int i = 0; i < profile.getNumberOfLevels(); i++) {

                    double tmp = profile.getValueForVariable(PhysicalProperty.TEMPERATURE.name(), i);

                    if (tmp < minTemp) {

                        minTemp = tmp;
                    }
                }

            }

            return minTemp;
        }

        private double getMaxTemp() {

            if (maxTemp < 1.0) {


                Profile profile = t6.getOutputProfile();

                for (int i = 0; i < profile.getNumberOfLevels(); i++) {

                    double temp = profile.getValueForVariable(PhysicalProperty.TEMPERATURE.name(), i);

                    if (temp > maxTemp) {

                        maxTemp = temp;
                    }
                }

                maxTemp = Math.max(maxTemp, getOriginalTask().getTAPE5().getSurface().getSurfaceTemperature());

            }

            return maxTemp;

        }

        private double getSpectrum(int series, int loc) throws IOException {

            double radiance = 0.0;

            if (series == 0) {

                radiance = getDefaultResult().getValueAt(loc, 0);

            } else {

                double temp = getMinTemp();
                double waveNumber = getWavenumbers().getValueAt(loc, 0);

                if (series == 2) {

                    temp = getMaxTemp();
                }

                //convert to /cm^2 from /m^2
                
                if(getOriginalTask().getTAPE5().getCalculationTemplate().getRadianceOutputUnits().equals(RadianceUnits.RADIANCE)){
                    radiance = Planck.getValue(waveNumber, temp) / 100.0;
                    
                }else{
                    
                    radiance = temp;
                }
                
            }

            return radiance;


        }

//    public TaskResultImpl(LBLRTMTask t){
//        Iterator<File> iterator = t.getOutputFiles().iterator();
//        File localDir = t.getLocalDir();
//        files = new FastSet<File>();
//        
//        while(iterator.hasNext()){
//            File f = iterator.next();
//            DJFile in = fU.fromFile(f);
//            DJFile out = fU.fromFile(new File(localDir.getPath()+File.separator+f.getName()));
//            fU.copyFile(in, out);
//            files.add(out.getFile());
//        }
//        
//    }
        @Override
        public TaskResult mergeWith(TaskResult result) {

            Iterator<File> iterator = result.getFiles().iterator();
            Iterator<File> iterator1 = files.iterator();
            Set<File> outputFiles = new FastSet<File>();

            while (iterator.hasNext() && iterator1.hasNext()) {
                File f1 = iterator.next();
                File f2 = iterator1.next();
                try {
                    try {
                        outputFiles.add(fM.mergeFiles(f1, f2));
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }

                } finally {

                    f2.delete();
                }
            }

            return new TaskResultImpl(outputFiles);

        }

        private Range<Double> findMinMaxVal(DJMatrix mat) {


            double minVal = Double.POSITIVE_INFINITY;
            double maxVal = Double.NEGATIVE_INFINITY;

            DJMatrixIterator iterator = mat.iterator(true);

            while (iterator.hasNext()) {
                Double next = iterator.next();

                if (next < minVal) {


                    minVal = next;
                }


                if (next > maxVal) {

                    maxVal = next;
                }


            }

            if (minVal == Double.POSITIVE_INFINITY) {

                minVal = 0;
            }

            if (maxVal == Double.NEGATIVE_INFINITY) {

                maxVal = 0;
            }

            return Ranges.closed(minVal, maxVal);

        }

        @Override
        public int[] getDimensions() {
            int[] dimensions = null;

            try {
                DJMatrix r = getDefaultResult();

                if (r.isVector()) {

                    return new int[]{r.getLength()};
                }

                return new int[]{r.getNumberOfRows(), r.getNumberOfColumns()};
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }

            return dimensions;


        }

        @Override
        public int getNumberOfSeries() {

            if (t6.isAnalyticalJacobian()) {

                return 1;
            }

            return 3;
        }

        @Override
        public double getValueAt(int series, int[] loc) {

            double out = 0.0;

            if (t6.isAnalyticalJacobian()) {
                try {
                    DJMatrix jac = getDefaultResult();

                    out = jac.getValueAt(loc[0], loc[1]);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }

            } else {
                try {
                    out = getSpectrum(series, loc[0]);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }


            return out;

        }

        @Override
        public double getScaleOf(int series, int dim, int loc) {

            if (dim == 0) {
                try {
                    return getWavenumbers().getValueAt(loc, 0);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }

            if (dim == 1) {

                if (t6.isAnalyticalJacobian()) {

                    if (t6.getAJParameters().size() > 1) {

                        return loc;
                    }


                    Profile p = t6.getOutputProfile();

                    if (loc < p.getNumberOfLevels()) {
                        return p.getValueForVariable(PhysicalProperty.ALTITUDE_FROM.name(), loc);
                    } else {

                        return p.getValueForVariable(PhysicalProperty.ALTITUDE_TO.name(), loc - 1);
                    }
                }
            }

            return 0.0;
        }

        @Override
        public String getScaleTitle(int dimension) {

         
            return this.getAxisTitle(dimension);
        }

        @Override
        public void setScaleTitle(int dimension, String title) {

            throw new UnsupportedOperationException();
        }

        

        @Override
        public void setAxisTitle(int dimension, String title) {


            throw new UnsupportedOperationException();

        }

        
        @Override
        public void setChartTitle(String title) {

            throw new UnsupportedOperationException();

        }

        @Override
        public Set<File> getFiles() {
            return files;
        }

//        private String getJacobianVarName() {
//
//            String varName = TaskResult.DOWNWELLING_LEVEL_JACOBIAN;
//            boolean direction = t6.getDirection();
//            if (direction) {
//
//                varName = TaskResult.UPWELLING_LEVEL_JACOBIAN;
//            }
//
//            return varName;
//
//        }
        private List<String> getJacobianVarNames() {

            List<String> jacVarNames = new FastList<String>();

            String prefix = TaskResult.DOWNWELLING_LEVEL_JACOBIAN;

            if (t6.getDirection()) {

                prefix = TaskResult.UPWELLING_LEVEL_JACOBIAN;
            }

            for (AJParameter p : t6.getAJParameters()) {

                String varName = prefix + "_" + p.paramString;
                jacVarNames.add(varName);

            }

            return jacVarNames;


        }

        private String getRadianceVarName() {
            boolean direction = t6.getDirection();

            String varName = DOWNWELLING_RADIANCE;

            //int layerIndex = 0;

            if (direction) {

                varName = UPWELLING_RADIANCE;
                //layerIndex = getOriginalTask().getTAPE5().getProfile().getNumberOfLevels()-2;
            }

            return varName;
        }

        private String getResultPath() {

            return getOriginalTask().getLocalDir()
                    + File.separator + LBLRTMParallelTask.RESULT_FILE_NAME;
        }

        private int getFrequencyLength() throws IOException {

            NetcdfFile f = NetcdfFile.open(getResultPath());
            int length = 0;

            try {

                length = f.findDimension(LBLRTMDimension.FREQUENCY.getFullName()).getLength();

            } finally {
                f.close();

                return length;

            }





        }

        private int getResultLayerIndex() {

            int layerIndex = 0;

            boolean dirn = t6.getDirection();


            if (dirn) {


                layerIndex = t6.getOutputProfile().getNumberOfLevels() - 1;
            }

            return layerIndex;

        }

        private Array readMatrixFor(String vName, int[] origin, int[] shape) throws InvalidRangeException, IOException {

            System.out.println("Reading matrix for: " + vName);
            String var = vName;

            NetcdfFile f = NetcdfFile.open(this.getResultPath());
            Array arr = null;
            try {
                Iterator<Variable> iterator = f.getVariables().iterator();

                int[] newShape = shape;
                while (iterator.hasNext()) {
                    Variable v = iterator.next();

                    if (v.getFullName().toLowerCase().equals(var.toLowerCase())) {

                        if (origin == null) {
                            List<Dimension> dimensions = v.getDimensions();

                            Iterator<Dimension> dimIt = dimensions.iterator();

                            origin = new int[dimensions.size()];
                            int i = 0;
                            while (dimIt.hasNext()) {
                                Dimension dim = dimIt.next();
                                origin[i] = 0;
                                i++;
                            }
                        }

                        if (shape == null) {

                            List<Dimension> dimensions = v.getDimensions();

                            Iterator<Dimension> dimIt = dimensions.iterator();

                            newShape = new int[dimensions.size()];
                            int i = 0;
                            while (dimIt.hasNext()) {
                                Dimension dim = dimIt.next();
                                newShape[i] = dim.getLength() - origin[i];
                                i++;
                            }
                        }

                        arr = v.read(origin, newShape);
                    }
                }

            } finally {
                f.close();
                return arr;
            }



        }

        @Override
        public DJMatrix getMatrixFor(String variable) throws IOException {
            try {

                Array arr = this.readMatrixFor(variable, null, null);
                return mF.createFromArray(arr);

            } catch (InvalidRangeException ex) {
                Exceptions.printStackTrace(ex);
            }

            return null;

        }

        public DJMatrix getSpectrum() throws IOException {
            DJMatrix spectrum = this.getRadiance();

            CalculationTemplate cT = getOriginalTask().getTAPE5().getCalculationTemplate();
            if (cT.getRadianceOutputUnits().equals(RadianceUnits.BT)) {

                spectrum = Converter.convertToBT(spectrum, this.getWavenumbers());
            }

            return spectrum;
        }

        @Override
        public DJMatrix getRadiance() throws IOException {
            try {

                String varName = this.getRadianceVarName();
                int layerIndex = getResultLayerIndex();
                int freqLength = this.getFrequencyLength();

                Array arr = this.readMatrixFor(varName, new int[]{layerIndex, 0}, new int[]{1, freqLength});
                DJMatrix data = mF.createFromArray(arr);




                return data;
            } catch (InvalidRangeException ex) {
                Exceptions.printStackTrace(ex);
            }

            return null;

        }

        DJMatrix getProfileMatrixForVariable(Profile p, String varName) {

            DJMatrix out = mF.createMatrix(p.getNumberOfLevels(), 1);

            for (int i = 0; i < p.getNumberOfLevels(); i++) {

                out.setValueAt(i, 1, p.getValueForVariable(varName, i));


            }
            return out;

        }

        @Override
        public DJMatrix getJacobian() throws IOException {
            //Stack jacobians
            List<String> varNames = this.getJacobianVarNames();
            Iterator<String> iterator = varNames.iterator();

            DJMatrix stackedJacs = null;
            int varIndex = 0;

            CalculationTemplate cT = getOriginalTask().getTAPE5().getCalculationTemplate();
            AJUnits jUnits = cT.getJacobianOutputUnits();

            while (iterator.hasNext()) {
                String next = iterator.next();
                DJMatrix jac = getMatrixFor(next);
                Profile prof = getOriginalTask().getTAPE5().getProfile();
                DJMatrix vec1 = this.getProfileMatrixForVariable(prof, next);
                DJMatrix vec2 = this.getRadiance();
                        jac = Converter.contertJacobianMatrix(jac, getWavenumbers(), vec1, vec2, AJUnits.D_RAD_D_LN_MR, jUnits);


                if (stackedJacs == null) {
                    stackedJacs = mF.createMatrix(jac.getNumberOfRows() * varNames.size(), jac.getNumberOfColumns());
                }
                DJMatrixIterator iterator1 = jac.iterator(true);
                int jacNRows = jac.getNumberOfRows();

                while (iterator1.hasNext()) {
                    Double val = iterator1.next();
                    int row = jacNRows * varIndex + iterator1.getRow();
                    stackedJacs.setValueAt(row, iterator1.getColumn(), val);
                }

                varIndex++;

            }



            return stackedJacs;
        }

        @Override
        public DJMatrix getWavenumbers() throws IOException {

            if (this.wn == null) {
                String var = VariableInfo.WAVENUMBER.getLongName();
                wn = getMatrixFor(var);
            }


            return wn;
        }

        @Override
        public DJMatrix getDefaultResult() throws IOException {

            if (this.resultMat == null) {

                String resultName = "Radiance.csv";
                if (t6.isAnalyticalJacobian()) {
                    resultMat = getJacobian().transpose();
                    jacRange = this.findMinMaxVal(resultMat);
                    resultName = "Jacobian.csv";


                } else {

                    resultMat = getSpectrum();
                    radRange = this.findMinMaxVal(resultMat);
                }

                File f = new File(getOriginalTask().getLocalDir().getPath() + File.separator + resultName);
                resultMat.saveToFile(f.getPath(), MatrixFileType.CSV);
            }

            return resultMat;


        }

        @Override
        public Comparable getSeriesKey(int series) {
            if (series == 0) {

                return "Radiance";

            } else if (series == 1) {

                return "" + getMinTemp() + "K";
            }

            return "" + getMaxTemp() + "K";

        }

        @Override
        public Range<Double> getRangeOfValues(int series) {

            if (t6.isAnalyticalJacobian()) {

                return jacRange;
            } else {

                if (series == 1) {
                    return radRange;

                }
            }

            return Ranges.closed(0.0, 0.0);



        }

        @Override
        public void exportData() throws IOException {

            this.getDefaultResult().saveToFile();

        }
    }


}
