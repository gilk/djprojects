/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.parallel.api;

/**
 *
 * @author djabry
 */
public interface Result<R extends Result> {
    
    R mergeWith(R result);
    
}
