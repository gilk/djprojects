/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilereader.api;

import ucar.ma2.DataType;

/**
 *
 * @author djabry
 */
public enum NumberMode {

        DOUBLE_MODE(356, 8, DataType.DOUBLE),
        FLOAT_MODE(266, 4, DataType.FLOAT);
        private final int shift;
        private final int multiplier;
        private final DataType type;

        NumberMode(int shift, int multiplier, DataType dType) {

            this.multiplier = multiplier;
            this.shift = shift;
            this.type = dType;

        }

        public int getShift() {

            return this.shift;

        }

        public int getMultiplier() {

            return this.multiplier;
        }
        
        
        public DataType getDataType(){
            
            return this.type;
        }
    }
