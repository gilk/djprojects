/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.matrix.operations;

import com.google.common.collect.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import no.uib.cipr.matrix.*;
import org.dj.index.api.IntegerIndex;
import org.dj.index.api.IntegerIndices;
import org.dj.matrix.DJMatrixImpl;
import org.dj.matrix.SubMatrix;
import org.dj.matrix.api.*;
import org.dj.matrix.storage.MTJMatrixStorage;
import org.ejml.simple.SimpleMatrix;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider(service = MatrixOperations.class)
public class MatrixOperationsImpl implements MatrixOperations {

    //private boolean useCache;
    //private final Map<Matrix, DJMatrix> representations = new FastMap<Matrix, DJMatrix>();
    private static final MatrixFactory mF = Lookup.getDefault().lookup(MatrixFactory.class);
    private static final IntegerIndices iI = Lookup.getDefault().lookup(IntegerIndices.class);
    
    public MatrixOperationsImpl() {
        //this.useCache = true;
    }

    private DJMatrix fromMatrix(Matrix m) {

        DJMatrix out = new DJMatrixImpl(new MTJMatrixStorage(m));
        return out;
    }

    private Matrix fromDJMatrix(DJMatrix m) {

        MatrixStorage mS = m.getMatrixStorage();


        if (mS instanceof MTJMatrixStorage) {
            MTJMatrixStorage mtMS = (MTJMatrixStorage) mS;
            return mtMS.getMatrix();

        }



        Matrix out;

        int numRows = m.getNumberOfRows();
        int numCols = m.getNumberOfColumns();

        if (m.isDiagonal()) {

            int diagLength = Math.min(numRows, numCols);

            out = new LowerSymmBandMatrix(diagLength, 0);

            for (int i = 0; i < diagLength; i++) {

                out.set(i, i, m.getValueAt(i, i));
            }

            return out;

        } else {

            out = new DenseMatrix(m.getNumberOfRows(), m.getNumberOfColumns());
            DJMatrixIterator iterator = m.iterator(false);

            while (iterator.hasNext()) {
                Double next = iterator.next();
                out.set(iterator.getRow(), iterator.getColumn(), next);
            }
        }


        return out;
    }

    private DJMatrix sparseAdd(DJMatrix m1, DJMatrix m2) {


        DJMatrix out = copy(m1);
        DJMatrixIterator iterator = m2.iterator(false);

        while (iterator.hasNext()) {
            double m2Val = iterator.next();
            int m2Row = iterator.getRow();
            int m2Col = iterator.getColumn();
            double outVal = out.getValueAt(m2Row, m2Col);

            out.setValueAt(m2Row, m2Col, outVal + m2Val);
        }

        return out;
    }

    private DJMatrix denseAdd(DJMatrix m1, DJMatrix m2) {

        DJMatrix out = copy(m1);
        DJMatrixIterator iterator = m2.iterator(false);
        while (iterator.hasNext()) {

            double val = iterator.next();
            int r = iterator.getRow();
            int c = iterator.getColumn();

            out.setValueAt(r, c, val + out.getValueAt(r, c));

        }

        return out;

    }

    private DJMatrix nativeAdd(DJMatrix m1, DJMatrix m2) {

        //if(m1.getCardinality()+m2.getCardinality()>m1.getNumElements()){

        return denseAdd(m1, m2);
        //}


        //There is currently a bug in sparse add
        //return sparseAdd(m1,m2);

    }

    private boolean isDiagonal(Matrix m) {
        
        
        if(m instanceof LowerSymmBandMatrix){
            LowerSymmBandMatrix lM = (LowerSymmBandMatrix) m;
            
            if(lM.numSubDiagonals()==0){
                return true;
            }
        }
        
        return false;

//        if (m.isSquare()) {
//            if (Matrices.cardinality(m) == m.numRows()) {
//
//                for (int rB : Matrices.rowBandwidth(m)) {
//
//                    if (rB != 1) {
//
//                        return false;
//                    }
//                }
//
//
//                for (int cB : Matrices.columnBandwidth(m)) {
//
//
//                    if (cB != 1) {
//
//                        return false;
//                    }
//                }
//
//                return true;
//
//            }
//        }
//
//        return false;
    }

    private Matrix add(Matrix a, Matrix b) {

        Matrix out;
        
        if(this.isDiagonal(a)&&this.isDiagonal(b)){
            
            
            out = new LowerSymmBandMatrix(a.numRows(),0);
            out.set(a);
        }else{
            
            out = new DenseMatrix(a.numRows(),b.numColumns());
            out.set(a);
            
        }

        return out.add(b);
    }

    private Matrix multiply(Matrix a, Matrix b) {

        Matrix c = new DenseMatrix(a.numRows(), b.numColumns());
        
        return a.mult(b, c);
    }

    private Matrix transposeMultiply(Matrix a, Matrix b) {

        Matrix c = new DenseMatrix(a.numColumns(), b.numColumns());

        return a.transAmult(b, c);
    }

    private Matrix multiply(Matrix a, double b) {

        Matrix out = a.copy();
        Iterator<MatrixEntry> iterator = out.iterator();
        while (iterator.hasNext()) {
            MatrixEntry next = iterator.next();
            next.set(next.get() * b);
        }
        return out;
    }

    private Matrix minus(Matrix a, Matrix b) {



        return a.copy().add(-1.0, b);
    }

    private Matrix solve(Matrix a, Matrix b) {

        Matrix c = new DenseMatrix(a.numRows(), b.numColumns());
        return a.solve(b, c);
    }

    private Matrix transposeSolve(Matrix a, Matrix b) {

        Matrix c = new DenseMatrix(a.numColumns(), b.numColumns());
        return a.transSolve(b, c);
    }

    @Override
    public DJMatrix add(DJMatrix m1, DJMatrix m2) {

        if (m1.isDiagonal() || m2.isDiagonal()) {

            //return nativeAdd(m1, m2);
        }

        Matrix m = add(this.fromDJMatrix(m1), this.fromDJMatrix(m2));
        return fromMatrix(m);
    }

    private DJMatrix nativeMinus(DJMatrix m1, DJMatrix m2) {

        DJMatrix out = this.copy(m1);
        DJMatrixIterator iterator = m2.iterator(false);

        while (iterator.hasNext()) {
            double m2Val = iterator.next();
            int m2Row = iterator.getRow();
            int m2Col = iterator.getColumn();
            double outVal = out.getValueAt(m2Row, m2Col);

            out.setValueAt(m2Row, m2Col, outVal - m2Val);

        }

        return out;
    }

    @Override
    public DJMatrix minus(DJMatrix m1, DJMatrix m2) {

        if (m1.isDiagonal() || m2.isDiagonal()) {

            return nativeMinus(m1, m2);
        }

        return this.fromMatrix(minus(this.fromDJMatrix(m1), this.fromDJMatrix(m2)));


    }

    private DJMatrix nativeDiagMultiply(DJMatrix mat1, DJMatrix mat2) {
        //mat2 is presumed diagonal or a vector

        DJMatrixIterator iterator = mat2.iterator(false);
        DJMatrix out = mF.createMatrix(mat1.getNumberOfRows(), mat2.getNumberOfColumns());

        while (iterator.hasNext()) {
            double diagVal = iterator.next();
            int r = iterator.getRow();
            DJMatrix rowVec = mat1.extractVector(true, r);
            DJMatrixIterator iterator1 = rowVec.iterator(false);
            while (iterator1.hasNext()) {
                Double val = iterator1.next();
                int c = iterator1.getColumn();
                out.setValueAt(r, c, val * diagVal);
            }

        }

        return out;



    }

    private DJMatrix nativeMultiply(DJMatrix mat1, DJMatrix mat2) {

        DJMatrix m1 = mat1;
        DJMatrix m2 = mat2;

        if (mat2.isDiagonal() || mat2.isDiagonal()) {

            return nativeDiagMultiply(mat1, mat2);
        }


        DJMatrix m3 = mF.createMatrix(mat1.getNumberOfRows(), mat2.getNumberOfColumns());

        DJMatrixIterator iterator = m1.iterator(false);
        int m1Row, m1Col, m2Row, m2Col, m3Row, m3Col;

        while (iterator.hasNext()) {
            double m1Val = iterator.next();
            m1Row = iterator.getRow();
            m1Col = iterator.getColumn();
            DJMatrixIterator iterator1 = m2.iterator(false);

            while (iterator1.hasNext()) {

                double m2Val = iterator1.next();

                m2Row = iterator1.getRow();

                if (m1Col == m2Row) {

                    m3Row = m1Row;
                    m3Col = iterator1.getColumn();
                    double m3Val = m3.getValueAt(m3Row, m3Col) + m1Val * m2Val;
                    m3.setValueAt(m3Row, m3Col, m3Val);

                }

            }
        }

        return m3;

    }

    @Override
    public DJMatrix multiply(DJMatrix mat1, DJMatrix mat2) {

        return this.fromMatrix(this.multiply(this.fromDJMatrix(mat1), this.fromDJMatrix(mat2)));

    }

    @Override
    public DJMatrix solve(DJMatrix m1, DJMatrix m2) {

        //c = inv(a)*b

        if (m1.isDiagonal()) {

            return multiply(inverse(m1), (m2));

        } else {

            return this.fromMatrix(solve(this.fromDJMatrix(m1), this.fromDJMatrix(m2)));

        }
    }

    @Override
    public DJMatrix copy(DJMatrix m) {

        DJMatrix out;
        if(m.isDiagonal()){
           out = mF.createDiagonalMatrix(m.getNumberOfRows()); 
        }else{
            out = mF.createMatrix(m.getNumberOfRows(), m.getNumberOfColumns());
        }
        
        DJMatrixIterator iterator = m.iterator(false);

        while (iterator.hasNext()) {
            Double next = iterator.next();
            out.setValueAt(iterator.getRow(), iterator.getColumn(), next);
        }
        
        UnmodifiableIterator<Integer> iterator1 = Ranges.closed(0, 1).asSet(DiscreteDomains.integers()).iterator();
        while(iterator1.hasNext()){
            Integer next = iterator1.next();
            out.setDimensionName(next, m.getDimensionName(next));
        }
        
        
        return out;
    }

    @Override
    public DJMatrix transpose(DJMatrix m1) {

        if (m1.isDiagonal() && m1.isSquare()) {

            return copy(m1);
        } else {

            return nativeTranspose(m1);
        }

    }

    private DJMatrix nativeTranspose(DJMatrix m) {
        DJMatrix out = mF.createMatrix(m.getNumberOfColumns(), m.getNumberOfRows());
        DJMatrixIterator iterator = m.iterator(false);
        while (iterator.hasNext()) {
            double val = iterator.next();
            out.setValueAt(iterator.getColumn(), iterator.getRow(), val);

        }

        return out;

    }

    private SimpleMatrix eJMLfromDJMatrix(DJMatrix m) {

        SimpleMatrix out = new SimpleMatrix(m.getNumberOfRows(), m.getNumberOfColumns());
        DJMatrixIterator iterator = m.iterator(true);

        while (iterator.hasNext()) {
            double next = iterator.next();
            out.set(iterator.getRow(), iterator.getColumn(), next);
        }

        return out;
    }

    private double cacheDeterminant(DJMatrix m) {

        SimpleMatrix out = this.eJMLfromDJMatrix(m);
        return out.determinant();

    }

    @Override
    public double determinant(DJMatrix m) {

        return this.cacheDeterminant(m);
    }

    @Override
    public double dot(DJMatrix m1, DJMatrix m2) {

        int c1 = m1.getCardinality();
        int c2 = m2.getCardinality();

        DJMatrix mat1;
        DJMatrix mat2;

        if (c1 < c2) {

            mat1 = m1;
            mat2 = m2;

        } else {

            mat2 = m1;
            mat1 = m2;
        }

        DJMatrixIterator iterator1 = mat1.iterator(false);
        double sum = 0;

        while (iterator1.hasNext()) {
            double val = iterator1.next();
            sum += val * mat2.getValueAt(iterator1.getRow(), iterator1.getColumn());
        }

        return sum;

    }

    @Override
    public double trace(DJMatrix m) {
        int diagLength = getDiagLength(m);
        double sum = 0.0;
        for (int i = 0; i < diagLength; i++) {
            sum += m.getValueAt(i, i);
        }
        return sum;
    }

    private int getDiagLength(DJMatrix m) {

        return Math.min(m.getNumberOfRows(), m.getNumberOfColumns());
    }
    
    

    private Matrix inverse(Matrix m) {

        Matrix a = m;
        Matrix i = Matrices.identity(m.numRows());
        Matrix ai = i.copy();
        return a.solve(i, ai);

    }

    @Override
    public DJMatrix inverse(DJMatrix m) {

        if (m.isDiagonal()) {

            return nativeDiagInverse(m);

        } else {

            return this.fromMatrix(inverse(this.fromDJMatrix(m)));

        }

    }

    private DJMatrix nativeDiagInverse(DJMatrix m) {

        int diagLength = this.getDiagLength(m);

        DJMatrix out = mF.createDiagonalMatrix(m.getNumberOfRows());

        for (int i = 0; i < diagLength; i++) {

            out.setValueAt(i, i, 1.0 / m.getValueAt(i, i));
        }

        return out;

    }

    @Override
    public DJMatrix transposeMultiply(DJMatrix m1, DJMatrix m2) {
        return this.fromMatrix(this.transposeMultiply(this.fromDJMatrix(m1), this.fromDJMatrix(m2)));
    }

    @Override
    public DJMatrix transposeSolve(DJMatrix m1, DJMatrix m2) {
        if (m1.isDiagonal()) {

            return this.solve(m1, m2);
        } else {

            return this.fromMatrix(this.transposeSolve(this.fromDJMatrix(m1), this.fromDJMatrix(m2)));
        }
    }

    @Override
    public double elementSum(DJMatrix m) {

        double sum = 0;
        DJMatrixIterator iterator = m.iterator(false);

        while (iterator.hasNext()) {

            sum += iterator.next();
        }

        return sum;
    }

    @Override
    public DJMatrix elementMultiply(DJMatrix m1, DJMatrix m2) {

        DJMatrix mat1 = m1;
        DJMatrix mat2 = m2;

        DJMatrix out = new DJMatrixImpl(m1.getNumberOfRows(), m1.getNumberOfRows());

        if (m1.getCardinality() > m2.getCardinality()) {

            mat2 = m1;
            mat1 = m2;

        }
        DJMatrixIterator iterator = mat1.iterator(false);

        while (iterator.hasNext()) {

            double val1 = iterator.next();
            int r = iterator.getRow();
            int c = iterator.getColumn();

            double val2 = mat2.getValueAt(r, c);
            out.setValueAt(r, c, val2 * val1);
        }

        return out;
    }

    @Override
    public DJMatrix multiply(DJMatrix m1, double d) {

        return this.fromMatrix(this.multiply(this.fromDJMatrix(m1), d));
    }

    @Override
    public DJMatrix subMatrix(DJMatrix m, int startRow, int endRow, int startCol, int endCol) {

        if (startRow == 0 && startCol == 0 && endRow == m.getNumberOfRows() - 1 && endCol == m.getNumberOfColumns() - 1) {

            return m;
        }


        return new SubMatrix(m, startRow, endRow, startCol, endCol);
    }

    @Override
    public DJMatrixIterator iteratorFor(DJMatrix m, boolean allElements) {
        return m.getMatrixStorage().iterator(allElements);
    }

    @Override
    public DJMatrix transMultiply(DJMatrix m1, DJMatrix m2) {
        return this.fromMatrix(this.transposeMultiply(this.fromDJMatrix(m1), this.fromDJMatrix(m2)));
    }

    private int[] fromIterable(Iterable<Integer> in) {

        int[] out = new int[Iterables.size(in)];
        Iterator<Integer> iterator = in.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            out[i] = iterator.next();
            i++;
        }

        return out;
    }
    
    private int[] fromSet(Set<Integer> in){
        int[] out = new int[in.size()];
        Iterator<Integer> iterator = in.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            out[i] = iterator.next();
            i++;
        }

        return out;
        
    }
    
    private Set<Integer> fromRange(int first, int last){
        
        return Ranges.closed(first, last).asSet(DiscreteDomains.integers());
        
    }

    @Override
    public DJMatrix subMatrix(DJMatrix m, Set<Integer> rows, Set<Integer> cols) {



        if (rows == null && cols == null) {

            return m;
        }

        if (rows == null) {

            rows = this.fromRange(0,m.getNumberOfRows()-1);
        }

        if (cols == null) {

            cols = this.fromRange(0,m.getNumberOfColumns()-1);
        }



        //return this.nativeSubMatrix(m, rows, cols);

//        //If resulting matrix is square and diagonal
//        if (rows.size() == cols.size() && m.isDiagonal()) {
//
//            return this.nativeSubMatrix(m, rows, cols);
//        }
//
        Matrix mat = this.fromDJMatrix(m);
        Matrix subMatrix = Matrices.getSubMatrix(mat, fromSet(rows), fromSet(cols));
        return this.fromMatrix(subMatrix);
    }

    private DJMatrix nativeSubMatrix(DJMatrix m, Set<Integer> rows, Set<Integer> cols) {
        
        if(rows.size()==m.getNumberOfRows()&&cols.size()==m.getNumberOfColumns()){
            
            return m;
        }

        //MatrixStorage mS = new SubSetMatrixStorage(m.getMatrixStorage(),rows,cols);
        //DJMatrix out = mF.createMatrixFromStorage(mS);
        
        DJMatrix out = mF.createMatrix(rows.size(), cols.size());
        Iterator<Integer> rI = rows.iterator();
        //Iterator<Integer> cI = Iterables.cycle(fromRange(cols)).iterator();
        DJMatrixIterator iterator = out.iterator(true);
        while (rI.hasNext()) {
            int r = rI.next();
            Iterator<Integer> cI = cols.iterator();
            while (cI.hasNext() && iterator.hasNext()) {
                iterator.next();
                double val = m.getValueAt(r, cI.next());
                if (val != 0.0) {
                    iterator.set(val);
                }
            }
        }

        return out;
    }

    private Set<Integer> fromRange(Set<Range<Integer>> in) {
        
        Iterator<Range<Integer>> iterator = in.iterator();
        Set<Integer> s = null;
        
        while(iterator.hasNext()){
            
            if(s==null){
                s=iterator.next().asSet(DiscreteDomains.integers());
            }
            
            if(iterator.hasNext()){
                
                s = Sets.union(s, iterator.next().asSet(DiscreteDomains.integers()));
            }
        }
        
        return s;
    }

    private Set<Range<Integer>> allValues(int start, int end) {

        Set<Range<Integer>> rs = new HashSet<Range<Integer>>();
        rs.add(Ranges.closed(start, end));
        return rs;
    }

    @Override
    public DJMatrix fromRanges(DJMatrix m, Set<Range<Integer>> rowRanges, Set<Range<Integer>> colsRanges) {

        Set<Range<Integer>> rR = rowRanges;
        Set<Range<Integer>> cR = colsRanges;
        
        if(rR==null&&cR==null){
            
            return m;
        }

        if (rR == null) {

            rR = allValues(0,m.getNumberOfRows()-1);
        }

        if (cR == null) {

            cR = allValues(0,m.getNumberOfColumns()-1);
        }

        //If all rows and cols are specified then return the original matrix
        if (cR.size() == 1 && rR.size() == 1) {
            Range<Integer> r = cR.iterator().next();
            Range<Integer> c = rR.iterator().next();
            if (r.lowerEndpoint() == 0 && r.upperEndpoint() == m.getNumberOfRows() - 1) {

                if (c.lowerEndpoint() == 0 && c.upperEndpoint() == m.getNumberOfColumns() - 1) {

                    return m;
                }
            }
        }



        Set<Integer> rowIndices = this.fromRange(rR);
        Set<Integer> colIndices = this.fromRange(cR);
        return subMatrix(m, rowIndices, colIndices);


    }
    
    int size(BiMap<Integer,Integer> m){
        int s = 0;
        Iterator<Entry<Integer, Integer>> iterator = m.entrySet().iterator();
        while(iterator.hasNext()){
            Entry<Integer, Integer> next = iterator.next();
            s+=next.getValue()-next.getKey()+1;
        }
        return s;
    }
    
    int[] fromMap(BiMap<Integer,Integer> m){
        int[] out = new int[size(m)];
        BiMapIterator<Integer> i = new BiMapIterator(m);
        int j = 0;
        while(i.hasNext()){
            out[j]=i.next();
            j++;
        }
        return out;
    }

    @Override
    public boolean allElementsEqual(DJMatrix m1, DJMatrix m2, double tol) {
        for(int i =0;i<2;i++){
            if(m1.getDimensionLength(i)!=m2.getDimensionLength(i)){
                return false;
            }
        }
        
        if(m1.getCardinality()!=m2.getCardinality()){
            return false;
        }
        
        DJMatrixIterator iterator = m1.iterator(false);
        DJMatrixIterator iterator1 = m2.iterator(false);
        
        while(iterator.hasNext()&&iterator1.hasNext()){
            Double m1Val = iterator.next();
            Double m2Val = iterator1.next();
            if(!m1Val.equals(m2Val)){
                return false;
            }
            
        }
        
        return true;
        
        
    }
    
    public DJMatrix subMatrix(DJMatrix m, int[] rows, int[] cols){
        Matrix out = this.fromDJMatrix(m);
        out = Matrices.getSubMatrix(out, rows, cols);
        return this.fromMatrix(out);
        
    }
    

    @Override
    public DJMatrix subMatrix(DJMatrix m, BiMap<Integer, Integer> rows, BiMap<Integer, Integer> cols) {
        
        if(rows==null&&cols==null){
            
            return m;
        }
        
        if(size(rows)==m.getNumberOfRows()&&size(cols)==m.getNumberOfColumns()){
            
            return m;
        }
        
        int[] r = this.fromMap(rows);
        int[] c = this.fromMap(cols);
        
        
       return subMatrix(m,r,c);
        
    }

    @Override
    public DJMatrix subMatrix(DJMatrix m, IntegerIndex rows, IntegerIndex cols) {
        if(rows==null&&cols==null){
            
            return m;
        }
        
        if(rows==null){
            
            rows = iI.fromRange(0, m.getNumberOfRows()-1);
        }
        
        if(cols ==null){
            
            cols = iI.fromRange(0, m.getNumberOfColumns()-1);
        }
        
        if(rows.getCardinality()==m.getNumberOfRows()&&cols.getCardinality()==m.getNumberOfColumns()){
            
            return m;
        }
        
        
        
       
        int[] r = this.fromIndex(rows);
        int[] c = this.fromIndex(cols);
        return subMatrix(m,r,c);

    }
    
    int[] fromIndex(IntegerIndex ix){
        
        int[] vals = new int[ix.getCardinality()];
        Iterator<Integer> allItems = ix.allItems();
        int i =0;
        while(allItems.hasNext()){
            vals[i]=allItems.next();
            i++;
        }
        
        return vals;
    }
}
