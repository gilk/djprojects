/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter;

import java.util.Iterator;
import java.util.List;
import javolution.util.FastList;
import org.lblrtm.lblrtmfilewriter.api.*;

/**
 *
 * @author djabry
 */
public class RecordThreePointThreeB extends RepeatedRecordSet {

    public RecordThreePointThreeB(RecordContainer parent) {

        super("3.3B", parent);
    }

    @Override
    public void initRecords() {
    }

    @Override
    public List<Record> getAllRecords() {
        
        List<Record> allRecords = new FastList<Record>();
        Iterator<Record> iterator = getRecords().iterator();

        while (iterator.hasNext()) {
            Record r = iterator.next();
            if (r.isValid()) {
                Record rOut = new RecordAbstr(r.getName()) {

                    @Override
                    public boolean isValid() {
                        return true;
                    }
                };
                
                allRecords.add(rOut);

                Iterator<Field> iterator1 = r.getFields().iterator();
                while (iterator1.hasNext()) {
                    Field f = iterator1.next();

                    Field fOut = new FieldImpl(f.getName(),
                                f.getFormat(),
                                f.getSpaces(),
                                f.getWidth(),
                                f.getValue(),
                                f.getText(),
                                f.isDefault(),
                                rOut);

                }

            }

        }
        
        
        return allRecords;
    }

    @Override
    public List<Record> getRecords() {

        List<Record> records = new FastList<Record>();
        List<Double> values = getTAPE5().getCalculationTemplate().getLayerBoundaries();
        int iBMAX = (Integer) getTAPE5().getField("3.1", "IBMAX").getValue();
        
        Iterator<Double> iterator = values.iterator();

        int i = 8;
        int level = 0;
        int recordCounter = 0;
        Record r = null;

        while (iterator.hasNext()) {
            level++;
            if (i == 8) {

                recordCounter++;

                r = new RecordAbstr("3.3B") {

                    @Override
                    public boolean isValid() {
                        if (!getTAPE5().getField("3.1", "IBMAX").getValue().equals(0)
                            &&getTAPE5().getField("1.2","IATM").getValue().equals(1)) {
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public String getName() {
                        return "3.3B." + getPropertyValue("recordCounter");
                    }
                };
                r.setPropertyValue("recordCounter", recordCounter);
                records.add(r);
            }
            i--;

            String fName = "";

            if (iBMAX > 0) {

                fName = "ZBND(" + level + ")";

            } else {


                fName = "PBND(" + level + ")";
            }

            Field f = new FieldImpl(fName, Format.FLOAT, 0, 10, iterator.next(), "", false, r);

            if (i == 0) {

                i = 8;
            };
        }

        return records;
    }

    @Override
    public int getNumberOfRepetitions() {
        return 0;
    }

    public DJTAPE5 getTAPE5() {

        return this.getParentTAPE5();
    }
    
   

    @Override
    public boolean isValid() {
        if (!getParentTAPE5().getField("3.1", "IBMAX").getValue().equals(0)
                &&getTAPE5().getField("1.2","IATM").getValue().equals(1)){
            return true;
        }
        return false;
    }
}
