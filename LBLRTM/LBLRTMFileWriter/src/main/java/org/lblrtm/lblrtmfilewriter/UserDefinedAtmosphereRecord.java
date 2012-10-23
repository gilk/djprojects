/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter;

import java.util.Iterator;
import java.util.List;
import javolution.util.FastList;
import org.lblrtm.lblrtmdata.api.Molecule;
import org.lblrtm.lblrtmfilewriter.api.*;
import org.lblrtm.profile.api.PhysicalProperty;

/**
 *
 * @author djabry
 */
public class UserDefinedAtmosphereRecord extends RepeatedRecordSet {

    //private Record rThreePointFive;
    public UserDefinedAtmosphereRecord(RecordContainer rC) {

        super("3.5-3.6", rC);

    }

    @Override
    public List<Record> getChildRecords() {

        List<Record> records = new FastList<Record>();

        Record rThreePointFive = new RecordAbstr("3.5") {

            @Override
            public boolean isValid() {
                if (getTAPE5().getField("3.1", "MODEL").getValue().equals(0)
                        && getTAPE5().getField("1.2", "IATM").getValue().equals(1)) {
                    return true;
                }
                return false;
            }

            @Override
            public String getName() {
                return "3.6." + (Integer) this.getPropertyValue(PROP_LEVEL) + "A";
            }
        };

        records.add(rThreePointFive);

        Field fZM = new FieldImpl("ZM", Format.FLOAT, 0, 10, 0.0, "", false, rThreePointFive) {

            @Override
            public Object getValue() {

                return getTAPE5().getProfile().getValueForVariable(PhysicalProperty.ALTITUDE.name(),
                        (Integer) this.getPropertyValue(PROP_LEVEL));
            }
        };

        Field fPM = new FieldImpl("PM", Format.FLOAT, 0, 10, 0.0, "", false, rThreePointFive) {

            @Override
            public Object getValue() {
                return getTAPE5().getProfile().getValueForVariable(PhysicalProperty.PRESSURE.name(),
                        (Integer) this.getPropertyValue(PROP_LEVEL));

            }
        };

        Field fTM = new FieldImpl("TM", Format.FLOAT, 0, 10, 0.0, "", false, rThreePointFive) {

            @Override
            public Object getValue() {
                return getTAPE5().getProfile().getValueForVariable(PhysicalProperty.TEMPERATURE.name(),
                        (Integer) this.getPropertyValue(PROP_LEVEL));


            }
        };

        Field fJCHARP = new FieldImpl("JCHARP", Format.STRING, 5, 1, "A", "", false, rThreePointFive);
        Field fJCHART = new FieldImpl("JCHART", Format.STRING, 0, 1, "A", "", false, rThreePointFive);
        Field fJLONG = new FieldImpl("JLONG", Format.STRING, 1, 1, "L", "", false, rThreePointFive);
        Field fSPACE = new FieldImpl("JLONG", Format.STRING, 0, 1, " ", "", false, rThreePointFive);

        //To do: Implement other 3.x records

        Iterator<Molecule> iterator = getTAPE5().getProfile().getMolecules().iterator();

        while (iterator.hasNext()) {

            Molecule m = iterator.next();

            Field fJCHAR = new FieldImpl("JCHAR", Format.STRING, 0, 1, "A", "", false, rThreePointFive) {

                @Override
                public Object getValue() {

                    return getTAPE5().getProfile().getUnitForVariable(
                            ((Molecule) this.getPropertyValue("molecule")).name(),
                            (Integer) this.getPropertyValue(PROP_LEVEL)).lBLRTMString;


                }

                @Override
                public String getName() {
                    return "JCHAR(" + ((Molecule) this.getPropertyValue("molecule")).getMoleculeNumber() + ")";
                }
            };

            fJCHAR.setPropertyValue("molecule", m);

        }
        Iterator<Molecule> iterator1 = getTAPE5().getProfile().getMolecules().iterator();

        int i = 8;
        int recordCounter = 0;
        Record r = null;

        while (iterator1.hasNext()) {

            if (i == 8) {
                recordCounter++;
                r = new RecordAbstr("3.6." + recordCounter) {

                    @Override
                    public boolean isValid() {
                        if (getTAPE5().getField("3.1", "MODEL").getValue().equals(0)
                                && getTAPE5().getField("1.2", "IATM").getValue().equals(1)) {
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public String getName() {
                        return "3.6." + (Integer) this.getPropertyValue(PROP_LEVEL) + "B." + (Integer) this.getPropertyValue("recordCounter");
                    }
                };

                r.setPropertyValue("recordCounter", recordCounter);
                records.add(r);
            }

            i--;
            Molecule m = iterator1.next();

            Field fVMOL = new FieldImpl("VMOL", Format.EXP, 0, 15, 0.0, "", false, r) {

                @Override
                public String getName() {
                    return "VMOL(" + ((Molecule) this.getPropertyValue("molecule")).getMoleculeNumber() + ")";
                }

                @Override
                public Object getValue() {

                    return getTAPE5().getProfile().getValueForVariable(((Molecule) this.getPropertyValue("molecule")).name(),
                            (Integer) this.getPropertyValue(PROP_LEVEL));

                }
            };

            //To do: Override getWidth method to return 15 if JLONG==L
            fVMOL.setPropertyValue("molecule", m);
            if (i == 0) {

                i = 8;
            }

        }

        return records;

    }

    @Override
    public int getNumberOfRepetitions() {
        return getTAPE5().getProfile().getNumberOfLevels();
    }

    @Override
    public boolean isValid() {
        if (getTAPE5().getField("3.1", "MODEL").getValue().equals(0)
                && getTAPE5().getField("1.2", "IATM").getValue().equals(1)) {
            return true;
        }
        return false;
    }

    public DJTAPE5 getTAPE5() {

        return getParentTAPE5();
    }

    @Override
    public void initRecords() {
    }
}
