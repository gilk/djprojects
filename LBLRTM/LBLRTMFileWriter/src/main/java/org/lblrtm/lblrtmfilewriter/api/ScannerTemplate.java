/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter.api;

/**
 *
 * @author djabry
 */
public interface ScannerTemplate extends RunTemplate<ScannerTemplate>{
    
    //HWHM
    public static final String PROP_HWHM = "hWHM";
    
    //JEMIT
    public static final String PROP_CONVOLUTION_TYPE = "convolutionType";
    
    
    public static final String INTERPOLATION_SCANNER  = "Interpolation scanner";
    public static final String FFT_SCANNER  = "FFT scanner";
    

    
    
    
    double getHWHM();
    void setHWHM(double hWHM);
    
    ConvolutionType getConvolutionType();
    void setConvolutionType(ConvolutionType cType);
}
