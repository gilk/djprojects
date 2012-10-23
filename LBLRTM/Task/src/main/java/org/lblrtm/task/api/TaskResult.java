/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.task.api;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import org.dj.matrix.api.DJMatrix;
import org.dj.parallel.api.Result;
import org.dj.plot.api.Plottable;

/**
 *
 * @author djabry
 */
public interface TaskResult extends Plottable, Result<TaskResult>{
    
   Set<File> getFiles();
   
   public static final String UPWELLING_RADIANCE = "RDUP";
   public static final String DOWNWELLING_RADIANCE  = "RDDN";
   public static final String DOWNWELLING_LEVEL_JACOBIAN = "LEV_RDderivDNW";
   public static final String UPWELLING_LEVEL_JACOBIAN = "LEV_RDderivUPW";
   public static final String DOWNWELLING_LAYER_JACOBIAN = "RDderivDNW";
   public static final String UPWELLING_LAYER_JACOBIAN = "RDderivDNW";

   
   
   DJMatrix getMatrixFor(String variable) throws IOException;
   
   //Get either downwelling or upwelling (depending on calculation) radiance at observer position
   DJMatrix getRadiance() throws IOException;
   
   DJMatrix getSpectrum() throws IOException;
   
   DJMatrix getWavenumbers() throws IOException;
   
   DJMatrix getDefaultResult() throws IOException;
   
   //Get either downwelling or upwelling (depending on calculation) Jacobian at observer position
   DJMatrix getJacobian() throws IOException;
    
}
