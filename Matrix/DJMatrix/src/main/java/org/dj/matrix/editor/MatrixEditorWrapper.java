/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.matrix.editor;

import org.dj.matrix.api.DJMatrix;
import org.dj.property.api.PropertyAbstr;

/**
 *
 * @author djabry
 */
public class MatrixEditorWrapper extends PropertyAbstr {
    
    public MatrixEditorWrapper(DJMatrix mat){
        
        super("Matrix",mat);
    }
    
    
    
    
}
