/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.matrix.io;

import com.google.common.collect.Lists;
import com.jmatio.io.MatFileReader;
import com.jmatio.io.MatFileWriter;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLDouble;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javolution.util.FastList;
import org.dj.fileutilities.api.DJFile;
import org.dj.fileutilities.api.FileUtilities;
import org.dj.matrix.api.DJMatrix;
import org.dj.matrix.api.DJMatrixArrayWrapper;
import org.dj.matrix.api.DJMatrixIterator;
import org.dj.matrix.api.MatrixFactory;
import org.dj.matrix.api.MatrixFileType;
import org.dj.matrix.api.MatrixIO;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import ucar.ma2.DataType;
import ucar.ma2.Index;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.Attribute;
import ucar.nc2.Dimension;
import ucar.nc2.NetcdfFile;
import ucar.nc2.NetcdfFileWriteable;
import ucar.nc2.Variable;


/**
 *
 * @author djabry
 */
@ServiceProvider(service = MatrixIO.class)
public class MatrixIOImpl implements MatrixIO {

    private static final MatrixFactory mF = Lookup.getDefault().lookup(MatrixFactory.class);
    private static final FileUtilities fU = Lookup.getDefault().lookup(FileUtilities.class);
    @Override
    public void saveToFile(DJMatrix mat, String fileName, MatrixFileType mFT) throws IOException {

        File f = new File(fileName);
        if (!f.exists()) {
                f.createNewFile();
            }
        if (mFT.equals(MatrixFileType.CSV)) {
            
            this.writeCSVFile(f, mat);

        } else if(mFT.equals(MatrixFileType.NC)){
            try {
                this.writeNetCDFFile(f, mat);
            } catch (InvalidRangeException ex) {
                Exceptions.printStackTrace(ex);
            }

        }else if(mFT.equals(MatrixFileType.MAT)){

            MatFileWriter writer = new MatFileWriter();
            MLArray mdbl = new MLDouble(f.getName(),fromMatrix(mat));
            ArrayList<MLArray> dbllst = Lists.newArrayList(mdbl);
            
            try{
                writer.write(f,dbllst);
            }finally{
                
                
            }

        }
    }
    
    private static double[][] fromMatrix(DJMatrix mat){
        
        double[][] vals = new double[mat.getNumberOfRows()][mat.getNumberOfColumns()];
        DJMatrixIterator iterator = mat.iterator(true);
        while(iterator.hasNext()){
            double val = iterator.next();
            vals[iterator.getRow()][iterator.getColumn()]=val;
            
        }
        
        return vals;
    }

    @Override
    public DJMatrix loadFile(String fileName) throws IOException, FileNotFoundException {
        File f = new File(fileName);

        if (!f.exists() || f.isDirectory()) {
            throw new FileNotFoundException("File " + fileName + " does not exist");
        }
        
        String ext =null;

        int i = f.getName().lastIndexOf(".");
        
        
        if(i>0&&i<f.getName().length()-1){
            ext = f.getName().substring(i + 1);
        }
        

        if (ext == null) {
            ext = "";

        }


        MatrixFileType mFT = findFT(ext);

        if (mFT == null) {
            //Add code to try different file types
            mFT = MatrixFileType.CSV;
        }
        try {
            return this.openFile(f, mFT);
        } catch (InvalidRangeException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        return null;


    }

    private MatrixFileType findFT(String ext) {

        for (MatrixFileType mFT : MatrixFileType.values()) {
            if (ext.equals(mFT.fileExtension.toLowerCase())) {

                return mFT;
            }

        }
        return null;
    }

    private DJMatrix openFile(File f, MatrixFileType fT) throws IOException, InvalidRangeException {

        if (fT.equals(MatrixFileType.CSV)) {
            return readCSVFile(f);

        }else if(fT.equals(MatrixFileType.MAT)){
            
            return this.readMatlabFile(f);
        }else{
            return readNetCDFFile(f);
           
        }

        


    }

    private DJMatrix readCSVFile(File f) throws FileNotFoundException, IOException {

        FileInfo fI = readFileInfo(f, null);
        DJMatrix m = mF.createMatrix(fI.rows, fI.cols);

        try {
            readFileInfo(f, m);
        } finally {
            return m;
        }
    }
    
    private DJMatrix readMatlabFile(File f) throws IOException{
        
        MatFileReader rdr = new MatFileReader();
        Map<String, MLArray> fls = rdr.read(f, MatFileReader.MEMORY_MAPPED_FILE);
        Iterator<MLArray> iterator = fls.values().iterator();
        while(iterator.hasNext()){
            MLArray next = iterator.next();
            if(next.getNDimensions()<=2&&next.isDouble()){
                return fromArray(next);
                
            }
        }
        
        return null;
        
    }
    
    private static DJMatrix fromArray(MLArray arr){
        DJMatrix out = null;
        if(arr instanceof MLDouble){
            MLDouble mlDouble = (MLDouble) arr;
            double[][] array = mlDouble.getArray();
            int rows = array.length;
            int cols = array[0].length;
            out = mF.createMatrix(rows, cols);
            
            for(int i =0;i<rows;i++){
                for(int j = 0;j<cols;j++){
                    out.setValueAt(i, j, array[i][j]);
                }
            }
            mlDouble.dispose();
        }
        
        return out;
    }

    private DJMatrix readNetCDFFile(File f) throws IOException, InvalidRangeException {

        NetcdfFile nF = NetcdfFile.open(f.getPath());
        Variable v = nF.getVariables().iterator().next();
        List<Dimension> dimensions = v.getDimensions();

        if (dimensions.size() < 2) {
            throw new IOException("Wrong number of dimensions");
        }

        DJMatrix m = mF.createMatrix(dimensions.get(0).getLength(), dimensions.get(1).getLength());
        DJMatrixIterator iterator = m.iterator(true);
        int[] singleShape = new int[]{1, 1};
        while (iterator.hasNext()) {
            iterator.next();
            int[] origin = new int[]{iterator.getRow(), iterator.getColumn()};
            double val = v.read(origin, singleShape).getDouble(0);
            iterator.set(val);
        }

        return m;


    }

    private FileInfo readFileInfo(File f, DJMatrix m) throws FileNotFoundException, IOException {


        int r = 0;
        int c = 0;


        BufferedReader bufRdr = new BufferedReader(new FileReader(f));
        String line = null;
        int row = 0;
        int col = 0;

        //read each line of text file
        while ((line = bufRdr.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(line, ",");
            while (st.hasMoreTokens()) {
                //get next token and store it in the array
                String t = st.nextToken();

                if (m != null) {
                    m.setValueAt(row, col, Double.valueOf(t));
                }
                col++;
            }


            c = Math.max(c, col);
            col = 0;

            row++;
            r = row;
        }

        //close the file
        bufRdr.close();

        return new FileInfo(r, c);

    }

    private void writeNetCDFFile(File f, DJMatrix mat) throws IOException, InvalidRangeException {
        NetcdfFileWriteable nF = NetcdfFileWriteable.createNew(f.getPath());

        List<Dimension> dims = new FastList<Dimension>();
        for (int i = 0; i < 2; i++) {
            Dimension dim = new Dimension(mat.getDimensionName(i), mat.getDimensionLength(i), true);
            nF.addDimension(null, dim);
            dims.add(dim);
        }

        Variable v = new Variable(nF, null, null, MATRIX_VARIABLE_NAME, DataType.DOUBLE, Dimension.makeDimensionList(dims));
        v.addAttribute(new Attribute("_FillValue", 0.0));


        nF.addVariable(null, v);
        nF.create();

        DJMatrixArrayWrapper mArr = new DJMatrixArrayWrapper(mat);

        try{
            nF.write(v.getName(), mArr);
            
        }finally{

            nF.close();
            
        }
        

        

    }

    private void writeCSVFile(File f, DJMatrix mat) throws IOException {

        FileWriter writer = new FileWriter(f);
        for (int i = 0; i < mat.getNumberOfRows(); i++) {

            for (int j = 0; j < mat.getNumberOfColumns(); j++) {

                writer.append("" + mat.getValueAt(i, j));

                if (j < mat.getNumberOfColumns() - 1) {

                    writer.append(",");

                }

            }
            writer.append("\n");
        }

        writer.flush();
        writer.close();
    }

    @Override
    public boolean isValidFile(File f) {
        Iterator<MatrixFileType> iterator = Arrays.asList(MatrixFileType.values()).iterator();
        while(iterator.hasNext()){
            MatrixFileType next = iterator.next();
            if(f.getPath().toLowerCase().endsWith("."+next.fileExtension)){
                return true;
            }
        }
        
        return false;
    }

    @Override
    public void saveToFile(DJMatrix mat, String fileName) throws IOException {
        Iterator<MatrixFileType> iterator = Arrays.asList(MatrixFileType.values()).iterator();
        while(iterator.hasNext()){
            MatrixFileType next = iterator.next();
            if(fileName.toLowerCase().endsWith(next.fileExtension.toLowerCase())){
                this.saveToFile(mat, fileName, next);
                break;
            }
        }
    }
    
    private FileChooserBuilder createBuilder(){
        
        FileChooserBuilder bldr = new FileChooserBuilder("Matrix_dir");
        for(MatrixFileType mFT:MatrixFileType.values()){
            
            bldr.addFileFilter(mFT.fileFilter);
        }
        
        return bldr;
    }

    @Override
    public void spawnSaver(DJMatrix mat) throws IOException {
        File f = createBuilder().showSaveDialog();
        if(f!=null&&!f.isDirectory()){
            this.saveToFile(mat, f.getPath());
            
        }
        
        
    }

    @Override
    public DJMatrix spawnOpener() throws IOException {
        File f = createBuilder().showOpenDialog();
        if(f!=null&&!f.isDirectory()){
            return this.loadFile(f.getPath());
            
        }
        return null;
        
    }

    @Override
    public DJMatrix loadFromStream(InputStream str, MatrixFileType mFT) throws IOException {
        File tempFile = File.createTempFile("testMat","", fU.getTempDir());
        DJFile outputFile = fU.fromFile(tempFile);
        try{
            fU.copyFile(str, outputFile);
        }finally{
            return this.loadFile(outputFile.getPath());
        }
    }


    
    

    private class FileInfo {

        public final int rows;
        public final int cols;

        public FileInfo(int r, int c) {
            this.rows = r;
            this.cols = c;

        }
    }
}
