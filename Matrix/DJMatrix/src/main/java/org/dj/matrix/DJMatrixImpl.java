/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.matrix;

import com.google.common.collect.Range;
import org.dj.matrix.api.MatrixOperations;
import org.dj.matrix.api.MatrixStorageFactory;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.dj.index.api.IntegerIndex;
import org.dj.matrix.api.DJMatrix;
import org.dj.matrix.api.DJMatrixIterator;
import org.dj.matrix.api.MatrixIO;
import org.dj.matrix.api.MatrixFileType;
import org.dj.matrix.api.MatrixStorage;

import org.openide.util.Lookup;

/**
 *
 * @author djabry
 */
public class DJMatrixImpl implements DJMatrix {

    private static final MatrixStorageFactory mSF = Lookup.getDefault().lookup(MatrixStorageFactory.class);
    private final MatrixStorage mS;
    private static final MatrixOperations mO = Lookup.getDefault().lookup(MatrixOperations.class);
    private final List<String> dimensionNames = Arrays.asList(new String[]{"Rows", "Columns"});
    private String title = "Matrix";
    private static final MatrixIO mE = Lookup.getDefault().lookup(MatrixIO.class);

    public DJMatrixImpl(int r, int c) {
        this(mSF.createStorage(r, c));
    }

    public DJMatrixImpl(MatrixStorage mS) {
        this.mS = mS;

    }
    
    public String getTitle(){
        return this.title;
    }
    
    public void setTitle(String title){
        this.title=title;
    }


    

    @Override
    public int getNumberOfColumns() {
        return getMatrixStorage().getNumberOfColumns();
    }

    @Override
    public int getNumberOfRows() {
        return getMatrixStorage().getNumberOfRows();
    }

    @Override
    public double getValueAt(int r, int c) {
        return getMatrixStorage().getValueAt(r, c);
    }

    @Override
    public void setValueAt(int r, int c, double val) {
        getMatrixStorage().setValueAt(r, c, val);
    }

    @Override
    public boolean isVector() {
        return this.getNumberOfColumns() == 1 || this.getNumberOfRows() == 1;
    }

    @Override
    public boolean isDiagonal() {
        return getMatrixStorage().isDiagonal();
    }

    @Override
    public boolean isSquare() {
        return this.getNumberOfRows() == this.getNumberOfColumns();
    }

    @Override
    public DJMatrix transpose() {
        return mO.transpose(this);
    }

    @Override
    public DJMatrix multiply(DJMatrix mat) {
        return mO.multiply(this, mat);
    }

    @Override
    public DJMatrix solve(DJMatrix mat) {
        return mO.solve(this, mat);
    }

    @Override
    public DJMatrix plus(DJMatrix mat) {
        return mO.add(this, mat);
    }

    @Override
    public DJMatrix elementMultiply(DJMatrix mat) {

        return mO.elementMultiply(this, mat);
    }

    @Override
    public DJMatrix inverse() {
        return mO.inverse(this);
    }

    @Override
    public DJMatrix minus(DJMatrix mat) {
        return mO.minus(this, mat);
    }

    @Override
    public DJMatrix extractVector(boolean extractRow, int element) {

        DJMatrix s;
        if (extractRow) {

            s = mO.subMatrix(this, element, element, 0, this.getNumberOfColumns() - 1);
        } else {

            s = mO.subMatrix(this, 0, this.getNumberOfRows() - 1, element, element);
        }

        return s;
    }

    @Override
    public DJMatrixIterator iterator(boolean allElements) {
        return this.getIteratorFor(allElements, 0, getNumberOfRows() - 1, 0, getNumberOfColumns() - 1);
    }

    @Override
    public DJMatrixIterator getIteratorFor(boolean allElements, int startRow, int endRow, int startCol, int endCol) {
        return mO.iteratorFor(mO.subMatrix(this, startRow, endRow, startCol, endCol), allElements);
    }

    @Override
    public double elementSum() {
        return mO.elementSum(this);
    }

    @Override
    public double trace() {
        return mO.trace(this);
    }

    @Override
    public double determinant() {
        return mO.determinant(this);
    }

    @Override
    public int getNumElements() {
        return getMatrixStorage().getNumberOfElements();
    }

    @Override
    public int getCardinality() {
        return getMatrixStorage().getCardinality();
    }

    @Override
    public MatrixStorage getMatrixStorage() {
        return mS;
    }

    @Override
    public DJMatrix copy() {
        return mO.copy(this);
    }

    @Override
    public int getLength() {
        return Math.max(getNumberOfColumns(), getNumberOfRows());
    }

    @Override
    public DJMatrix multiply(double val) {
        return mO.multiply(this, val);
    }

    @Override
    public DJMatrix transMultiply(DJMatrix mat) {
        return mO.transMultiply(this, mat);
    }



    @Override
    public DJMatrix subMatrix(Set<Integer> rows, Set<Integer> cols) {
        return mO.subMatrix(this, rows, cols);
    }

    @Override
    public DJMatrix fromRanges(Set<Range<Integer>> rowRange, Set<Range<Integer>> colRange) {
        return mO.fromRanges(this, rowRange, colRange);
    }

    @Override
    public String getDimensionName(int dim) {
        return this.dimensionNames.get(dim);
    }

    @Override
    public void setDimensionName(int dim, String name) {
        this.dimensionNames.set(dim, name);
    }

    @Override
    public int getDimensionLength(int dim) {
        if (dim == 0) {

            return this.getNumberOfRows();
        }

        return this.getNumberOfColumns();
    }

    @Override
    public boolean allElementsEqual(DJMatrix m, double tol) {
        return mO.allElementsEqual(this, m, tol);
    }

    @Override
    public DJMatrix subMatrix(IntegerIndex rows, IntegerIndex cols) {
        return mO.subMatrix(this, rows, cols);
    }


    @Override
    public void saveToFile(String fileName, MatrixFileType mFT) throws IOException {
        mE.saveToFile(this,fileName, mFT);
    }

    @Override
    public void saveToFile(String fileName) throws IOException {
        mE.saveToFile(this, fileName);
    }

    @Override
    public void saveToFile() throws IOException {
        mE.spawnSaver(this);
    }
}
