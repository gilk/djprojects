/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilereader;

import java.util.Set;
import org.lblrtm.lblrtmdata.api.VariableInfo;

/**
 *
 * @author djabry
 */
public interface Panel {
    
    double getV1();
    double getV2();
    double getDV();
    Set<VariableInfo> getVariables();
    int getNumberOfPoints();
    Number getPoint(VariableInfo varInfo, int point);
    
    
    
}
