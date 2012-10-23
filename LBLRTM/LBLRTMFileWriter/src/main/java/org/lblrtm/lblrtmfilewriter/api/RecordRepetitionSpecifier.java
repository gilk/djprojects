/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter.api;

/**
 *
 * @author djabry
 */
public interface RecordRepetitionSpecifier {
    
    Integer getNumberOfRepetitionsForRecord(Record record);
    
}
