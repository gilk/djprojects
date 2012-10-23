/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.matrix;

import org.dj.matrix.storage.SubMatrixStorage;
import org.dj.matrix.api.DJMatrix;



/**
 *
 * @author djabry
 */
public class SubMatrix extends DJMatrixImpl implements DJMatrix{

    public SubMatrix(DJMatrix mat, int startRow, int endRow, int startCol, int endCol){
        
        super(new SubMatrixStorage(mat.getMatrixStorage(),
                startRow,
                endRow,
                startCol,
                endCol
                ));
    }

   

  
}
