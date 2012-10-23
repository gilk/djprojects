/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter.api;

/**
 *
 * @author djabry
 */
public interface FFTScanner extends ScannerTemplate{
    
    //JFNin
   public static final String PROP_SCANNING_FUNCTION = "scanningFunction";
   
   //MRATin
   public static final String PROP_HWHM_RATIO = "hWHMRatio";
   
   //IVX
   public static final String PROP_CALCULATION_TYPE = "calculationType";
   
   //NOFIX
   public static final String PROP_BOXCAR_DECONVOLUTION = "boxcarDeconvolution";
      
   //PARAM1
   public static final String PROP_PARAM1 ="param1";
   
   //PARAM2
   public static final String PROP_PARAM2 = "param2";

   //PARAM3
   public static final String PROP_PARAM3 = "param3";
   
   
   ScanningFunction getScanningFunction();
   void setScanningFunction(ScanningFunction sF);
   
   int getHWHMRatio();
   void setHWHMRatio(int ratio);
   
   CalculationType getCalculationType();
   void setCalculationType(CalculationType cT);
   
   boolean isBoxcarDeconvolution();
   void setBoxcarDeconvolution(boolean de);
   
   
   double getParam1();
   void setParam1(double val);
   
   double getParam2();
   void setParam2(double val);
   
   double getParam3();
   void setParam3(double val);
   
   
   
    
}
