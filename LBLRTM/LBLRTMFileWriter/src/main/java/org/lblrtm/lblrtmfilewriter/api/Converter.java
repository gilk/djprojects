/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter.api;

import org.dj.matrix.api.DJMatrix;
import org.dj.matrix.api.DJMatrixIterator;
import org.dj.matrix.api.MatrixFactory;
import org.openide.util.Lookup;



/**
 *
 * @author djabry
 */
public class Converter {
    
    public static final double H = 6.626068e-34;
    public static final double C = 2.99792458e8;
    public static final double KB =  8.314/6.022e23;
    public static final double ALPHA1 = 2*H*Math.pow(C, 2);
    public static final double ALPHA2 = H*C/KB;
    private static final MatrixFactory mF = Lookup.getDefault().lookup(MatrixFactory.class);
    
    
    public static double convertToBT(double radiance, double v){
        
        
        
        //Convert frequency to /m
        double vOut = 1.0E2*v;
        double r = radiance*1.0E2;
        
        return ALPHA2*vOut/Math.log(1+(ALPHA1*Math.pow(vOut, 3)/r));
        
    }
    
    
    
    
    public static double convertToR(double t, double v){
        
        double vOut = 1.0E2*v;

        return (ALPHA1*Math.pow(vOut,3)/(Math.exp(ALPHA2*vOut/t)-1))/1.0E2;
        
    }
    
    public static DJMatrix convertToBT(DJMatrix rad,DJMatrix wn){
        
        DJMatrix bT = mF.createMatrix(rad.getNumberOfRows(), rad.getNumberOfColumns());
        DJMatrixIterator bTIterator = bT.iterator(true);
        DJMatrixIterator radIterator = rad.iterator(true);
        DJMatrixIterator wnIterator = wn.iterator(true);
        
        while(bTIterator.hasNext()&&radIterator.hasNext()&&wnIterator.hasNext()){
            
            bTIterator.next();
            Double radVal = radIterator.next();
            Double wnVal = wnIterator.next();
            bTIterator.set(convertToBT(radVal,wnVal));
        }
        
        return bT;
        
    }
    
    public static DJMatrix convertToR(DJMatrix bT, DJMatrix wn){
        
        DJMatrix rad = mF.createMatrix(bT.getNumberOfRows(), bT.getNumberOfColumns());
        DJMatrixIterator bTIterator = bT.iterator(true);
        DJMatrixIterator radIterator = rad.iterator(true);
        DJMatrixIterator wnIterator = wn.iterator(true);
        
        while(bTIterator.hasNext()&&radIterator.hasNext()&&wnIterator.hasNext()){
            
            radIterator.next();
            Double bTVal = bTIterator.next();
            Double wnVal = wnIterator.next();
            bTIterator.set(convertToR(bTVal,wnVal));
        }
        
        return bT;
        
    }
    
    public static DJMatrix convertJacobianMatrix(DJMatrix jacobian, DJMatrix vec, VectorConverterFunction fn){
        
        boolean dim = false;
        if(vec.getNumElements()==jacobian.getNumberOfRows()){
            
            dim = true;
        }
        
        DJMatrix out = jacobian.copy();
        DJMatrixIterator iterator = vec.iterator(true);
        int i = 0; 
        while(iterator.hasNext()){
            
            Double next = iterator.next();
            double multiple = fn.convertValueAt(i, next);
            int lim = jacobian.getNumberOfRows();
            if(dim){
                lim = jacobian.getNumberOfColumns(); 
            }
   
            for(int j = 0;j<lim;j++){
                
                int r = j;
                int c = i;
                
                if(dim){
                    
                    r= i;
                    c= j;
                }
                
                out.setValueAt(r, c, out.getValueAt(r, c)*multiple);
  
            }

            i++;  
        }
        return out;

        
    }
    
    public static VectorConverterFunction LN_CONVERTER =  new VectorConverterFunction(){

        @Override
        public double convertValueAt(int i, double val) {
            return Math.log(val);
        }
    };
    
    public static VectorConverterFunction EXP_CONVERTER = new VectorConverterFunction(){

        @Override
        public double convertValueAt(int i, double val) {
            return Math.exp(val);
        }

    };
    
    public static DJMatrix contertJacobianMatrix(DJMatrix jac, DJMatrix wn, DJMatrix vec1, DJMatrix vec2, AJUnits inputUnits, AJUnits outputUnits){
        
        //Vec1 = state
        //Vec2 = spectrum
        
        
        DJMatrix out = jac;
        
        if(inputUnits.equals(AJUnits.D_RAD_D_LN_MR)||inputUnits.equals(AJUnits.D_RAD_D_MR)){
            
            if(outputUnits.equals(AJUnits.D_BT_D_LN_MR)||outputUnits.equals(AJUnits.D_BT_D_MR)){
                //Convert to BT
                out = convertJacobianMatrix(out, vec2, new JacobianBTConverter(vec2,wn));
                
            }
            
        }
        
        
        if(inputUnits.equals(AJUnits.D_BT_D_LN_MR)||inputUnits.equals(AJUnits.D_BT_D_MR)){
            if(outputUnits.equals(AJUnits.D_RAD_D_LN_MR)||outputUnits.equals(AJUnits.D_RAD_D_MR)){
                //Convert to rad
                out  = convertJacobianMatrix(out, vec2,new JacobianRadConverter(vec2,wn));
  
            } 
        }
        
        
        if(inputUnits.equals(AJUnits.D_RAD_D_LN_MR)||inputUnits.equals(AJUnits.D_BT_D_LN_MR)){
            
            if(outputUnits.equals(AJUnits.D_BT_D_MR)||outputUnits.equals(AJUnits.D_RAD_D_MR)){
                //Convert from ../ln(MR) to ../MR
                out = convertJacobianMatrix(out,vec1,EXP_CONVERTER);
                
            }
        }
        
        
        if(inputUnits.equals(AJUnits.D_BT_D_MR)||inputUnits.equals(AJUnits.D_RAD_D_MR)){
            
            if(outputUnits.equals(AJUnits.D_RAD_D_LN_MR)||outputUnits.equals(AJUnits.D_BT_D_LN_MR)){
                //convert from ../MR to ../ln(MR)
                
                out = convertJacobianMatrix(out,vec1,LN_CONVERTER);
            } 
        }
        
        return out;
        
        
    }
    
    public static DJMatrix convertCovarianceMatrix(DJMatrix covariance, DJMatrix vec, VectorConverterFunction fn){
        
        int side = vec.getLength();
        DJMatrix mu = mF.createMatrix(side, 1);
        DJMatrix out = covariance.copy();
        //First find mu values;
        for(int i = 0;i<side;i++){
        
            double diag = covariance.getValueAt(i, i);
            
            double vi = vec.getValueAt(i, 1);
            double mui = vi-Math.sqrt(diag);
            
            
            
            mu.setValueAt(i, 1, mui);
            out.setValueAt(i, i, 
                    (fn.convertValueAt(i,vi)-fn.convertValueAt(i,mui))*            
                    (fn.convertValueAt(i, vi)-fn.convertValueAt(i, mui)));
        }
        
        
        
        for(int i = 0;i<side;i++){
            
            for(int j=0;j<side;j++){
                
                //Only work on values in upper triangle
                if(j>=i){
                    
                    double val = out.getValueAt(i, j);
                    
                    if(val!=0.0){
                        
                        double vi = vec.getValueAt(i, 1);
                        double vj = vec.getValueAt(j,1);
                        double mui = mu.getValueAt(i, 1);
                        double muj = mu.getValueAt(j, 1);
                        
                        
                        double e = val/((vi-mui)*(vj-muj));
                        
                        double outputVal = e*(fn.convertValueAt(i,vi)-fn.convertValueAt(i,mui))*
                                (fn.convertValueAt(j, vj)-fn.convertValueAt(j, muj));

                        out.setValueAt(i, j, outputVal);
                        out.setValueAt(j, i, outputVal);

                    }
                }
            }
        }
        
        return out;
        
        
    }
}
    

