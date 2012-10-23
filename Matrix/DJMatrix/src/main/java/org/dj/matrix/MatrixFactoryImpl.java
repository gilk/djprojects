/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.matrix;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import no.uib.cipr.matrix.LowerSymmBandMatrix;
import org.dj.matrix.api.*;
import org.dj.matrix.storage.BlockDiagMatrixStorage;
import org.dj.matrix.storage.MTJMatrixStorage;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import ucar.ma2.Array;
import ucar.ma2.IndexIterator;

/**
 *
 * @author djabry
 */
@ServiceProvider(service = MatrixFactory.class)
public class MatrixFactoryImpl implements MatrixFactory {

    //private File TEMP_DIR;
    private static final MatrixIO mIO= Lookup.getDefault().lookup(MatrixIO.class);
    //private static final String TEMP_DIR_NAME = "TempMatrixFiles";
    //private static final FileUtilities fU = Lookup.getDefault().lookup(FileUtilities.class);
    private static final MatrixStorageFactory mSF = Lookup.getDefault().lookup(MatrixStorageFactory.class);

    @Override
    public DJMatrix createVector(Double[] elements, boolean rowVector) {
        return this.createVector(Arrays.asList(elements), rowVector);
    }

    @Override
    public DJMatrix createVector(List<Double> elements, boolean rowVector) {

        DJMatrix vec = createVector(elements.size(), rowVector);
        DJMatrixIterator iterator = vec.iterator(true);
        Iterator<Double> iterator1 = elements.iterator();

        while (iterator.hasNext() && iterator1.hasNext()) {
            iterator.next();
            iterator.set(iterator1.next());
        }

        return vec;


    }

    @Override
    public DJMatrix createVector(int numElements, boolean rowVector) {

        int numCols = 1;
        int numRows = numElements;

        if (rowVector) {

            numCols = numElements;
            numRows = 1;
        }

        return createMatrix(numRows, numCols);
    }

    @Override
    public DJMatrix createDiagonalMatrix(Double[] diagonal) {

        return this.createDiagonalMatrix(Arrays.asList(diagonal));
    }
    
    

    @Override
    public DJMatrix createDiagonalMatrix(List<Double> elements) {
       
        int numRows = elements.size();
        DJMatrix mat = this.createDiagonalMatrix(numRows);

        Iterator<Double> iterator = elements.iterator();
        int i = 0;
        while (iterator.hasNext()) {

            double val = iterator.next();
            mat.setValueAt(i, i, val);
            i++;
        }
        
        return mat;

    }
    
    
    public MatrixStorage createStorage(int rows, int cols){
        
        //DenseMatrix dM = new DenseMatrix(rows,cols);
        //return new MTJMatrixStorage(dM);
        
        return mSF.createStorage(rows, cols);
        
    }

    @Override
    public DJMatrix createMatrix(int rows, int cols) {
        return createMatrixFromStorage(createStorage(rows,cols));
    }

    @Override
    public DJMatrix createIdentityMatrix(int size) {

        DJMatrix mat = this.createDiagonalMatrix(size);

        for (int i = 0; i < size; i++) {

            mat.setValueAt(i, i, 1.0);
        }

        return mat;


    }

    private void setRandom(DJMatrix m, double minVal, double maxVal) {
        DJMatrixIterator iterator = m.iterator(true);
        double multiplier = maxVal - minVal;
        while (iterator.hasNext()) {

            iterator.next();
            iterator.set(minVal + multiplier * Math.random());
        }
    }

    @Override
    public DJMatrix createFromFile(String fileName) throws IOException, FileNotFoundException {
        return mIO.loadFile(fileName);
        
//        try {
//            
//
//            FileInputStream fIS = new FileInputStream(fileName);
//            ObjectInputStream oIS = new ObjectInputStream(fIS);
//            
//            
//            Object o = oIS.readObject();
//            if(o instanceof MatrixStorage){
//                MatrixStorage mS = (MatrixStorage) o;
//                DJMatrix m = new DJMatrixImpl(mS);
//                oIS.close();
//                fIS.close();
//                return m;
//                
//            }
//            
//            
//
//        } catch (ClassNotFoundException ex) {
//            Exceptions.printStackTrace(ex);
//        } catch (IOException ex) {
//            Exceptions.printStackTrace(ex);
//        }
//        
//        return null;







    }

//    @Override
//    public File getTempDir() {
//
//        if (TEMP_DIR == null) {
//            try {
//                TEMP_DIR = new File(File.createTempFile("Matrix", "temp").getParent() + File.separator + TEMP_DIR_NAME);
//            } catch (IOException ex) {
//                Logger.getLogger(MatrixFactoryImpl.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//
//        if (!TEMP_DIR.exists()) {
//
//            TEMP_DIR.mkdirs();
//        }
//
//        return TEMP_DIR;
//    }

//    @Override
//    public void close() throws IOException {
//        fU.delete(fU.fromFile(this.getTempDir()));
//    }

    @Override
    public DJMatrix createRandomMatrix(int r, int c, double minVal, double maxVal) {
        DJMatrix m = this.createMatrix(r, c);
        this.setRandom(m, minVal, maxVal);
        return m;
    }

    @Override
    public DJMatrix createRandomVector(int numElements, boolean rowVec, double min, double max) {
        DJMatrix vec = this.createVector(numElements, rowVec);
        this.setRandom(vec, min, max);
        return vec;
    }

    @Override
    public DJMatrix createMatrixFromStorage(MatrixStorage mS) {
        return new DJMatrixImpl(mS);
    }

    @Override
    public DJMatrix createDiagonalMatrix(int diagLength) {
        return new DJMatrixImpl(new MTJMatrixStorage(new LowerSymmBandMatrix(diagLength, 0)));
    }

    @Override
    public DJMatrix createBlockDiagonalMatrix(List<DJMatrix> diags) {
        return this.createMatrixFromStorage(new BlockDiagMatrixStorage(diags));
    }
    
        private static int length(Array arr) {
        int l = 0;

        for (int i : arr.getShape()) {

            if (i > l) {

                l = i;
            }
        }

        return l;
    }

    private static boolean isVector(Array arr) {

        if (arr.getShape().length == 1) {

            return true;
        }

        for (int i : arr.getShape()) {

            if (i == 1) {

                return true;
            }
        }

        return false;
    }


    @Override
    public DJMatrix createFromArray(Array arr) {
            

        DJMatrix mat;

        if (isVector(arr)) {
            mat = this.createMatrix(length(arr), 1);
        } else {
            mat = this.createMatrix(arr.getShape()[0], arr.getShape()[1]);

        }

        IndexIterator iterator = arr.getIndexIterator();
        DJMatrixIterator iterator1 = mat.iterator(true);

        while (iterator1.hasNext()) {

            iterator1.next();
            iterator.next();
            iterator1.set(iterator.getDoubleCurrent());
        }

        return mat;


    }
}
