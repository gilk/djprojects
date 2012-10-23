/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter;


import java.util.Iterator;
import org.lblrtm.lblrtmdata.api.Molecule;
import org.lblrtm.lblrtmfilewriter.api.*;
import org.lblrtm.profile.api.PhysicalProperty;
import org.lblrtm.profile.api.Profile;

/**
 *
 * @author djabry
 */
public class RecordTwoPointOneSet extends RepeatedRecordSet {

    private Profile profile;

    public Profile getProfile() {

        return profile;
    }

    public RecordTwoPointOneSet(Profile profile, RecordContainer parent) {

        super("2.1.1-2.1.3", parent);
        this.profile = profile;

        

    }

    @Override
    public int getNumberOfRepetitions() {
        return this.profile.getNumberOfLevels();
    }


    @Override
    public boolean isValid() {
        if (this.getParentTAPE5().getField("1.2", "IATM").getValue().equals(0)) {
            return true;
        }
        return false;
    }

    @Override
    public void initRecords() {
        
        DJTAPE5 parent = getParentTAPE5();
        Record rTwoPointOnePointOne = new RecordAbstr("2.1", this.getParentTAPE5()) {

            @Override
            public boolean isValid() {

                if (getParentTAPE5().getField("1.2", "IATM").getValue().equals(0)) {

                    return true;
                }

                return false;

            }
        };
        
        addChild(rTwoPointOnePointOne);


        Field fPAVE = new FieldImpl("PAVE", Format.FLOAT, 0, 10, 1000.0, "", false, rTwoPointOnePointOne) {

            @Override
            public String getName() {
                return "PAVE("+getPropertyValue(PROP_LEVEL)+")";
            }
            
            

            @Override
            public Object getValue() {
                
                return getProfile().getValueForVariable(PhysicalProperty.PRESSURE.name(), (Integer)getPropertyValue(PROP_LEVEL));
                
            }

            @Override
            public int getWidth() {
                if (getParentTAPE5().getField("2.1", "IFORM").getValue().equals(1)) {

                    return 15;
                }

                return 10;
            }
        };


        Field fTAVE = new FieldImpl("TAVE", Format.FLOAT, 0, 10, 300, "", false, rTwoPointOnePointOne) {
             @Override
            public String getName() {
                return "TAVE("+getPropertyValue(PROP_LEVEL)+")";
            }

           @Override
            public Object getValue() {
                return getProfile().getValueForVariable(PhysicalProperty.TEMPERATURE.name(), (Integer)getPropertyValue(PROP_LEVEL));
            }
        };

        Field fSECNTK = new FieldImpl("SECNTK", Format.FLOAT, 0, 10, 0.0, "", true, rTwoPointOnePointOne){
            
            @Override
            public String getName() {
                return "SECNTK("+getPropertyValue(PROP_LEVEL)+")";
            }
        };

        Field fITYL = new FieldImpl("ITYL", Format.STRING, 0, 3, "", "", true, rTwoPointOnePointOne){
        
            @Override
            public String getName() {
                return "ITYL("+getPropertyValue(PROP_LEVEL)+")";
            }
            
        };
        

        Field fIPATH = new FieldImpl("IPATH", Format.INTEGER, 0, 2, 0, "", true, rTwoPointOnePointOne){
            @Override
            public String getName() {
                return "IPATH("+getPropertyValue(PROP_LEVEL)+")";
            }
            
        };

        Field fALTZL_1 = new FieldImpl("ALTZ", Format.FLOAT, 1, 7, 0.0, "", true, rTwoPointOnePointOne){
            
            @Override
            public String getName() {
                
                int level = (Integer)getPropertyValue(PROP_LEVEL)-1;
                return "ALTZ("+level+")";
            }
            
        };

        Field fPZL_1 = new FieldImpl("PZ(L-1)", Format.FLOAT, 0, 8, 0.0, "", true, rTwoPointOnePointOne){
            @Override
            public String getName() {
                
                int level = (Integer)getPropertyValue(PROP_LEVEL)-1;
                return "PZ("+level+")";
            }
            
        };

        Field fTZL_1 = new FieldImpl("TZ(L-1)", Format.FLOAT, 0, 7, 0.0, "", true, rTwoPointOnePointOne){
            
            @Override
            public String getName() {
                
                int level = (Integer)getPropertyValue(PROP_LEVEL)-1;
                return "TZ("+level+")";
            }
            
        };

        Field fALTZL = new FieldImpl("ALTZ(L)", Format.FLOAT, 0, 7, 0.0, "", false, rTwoPointOnePointOne) {
            
            @Override
            public String getName() {
                
                int level = (Integer)getPropertyValue(PROP_LEVEL);
                return "ALTZ("+level+")";
            }

            @Override
            public Object getValue() {
                return getProfile().getValueForVariable(PhysicalProperty.TEMPERATURE.name(), (Integer)getPropertyValue(PROP_LEVEL));
            }
            
            
        };


        Field fPZL = new FieldImpl("PZ(L)", Format.FLOAT, 0, 8, 0.0, "", false, rTwoPointOnePointOne) {
            
            @Override
            public String getName() {
                
                int level = (Integer)getPropertyValue(PROP_LEVEL);
                return "PZ("+level+")";
            }

            @Override
            public Object getValue() {
                
                return getProfile().getValueForVariable(PhysicalProperty.PRESSURE.name(), (Integer)getPropertyValue(PROP_LEVEL));
                
            }
            
            
        };

        Field fTZL = new FieldImpl("TZ(L)", Format.FLOAT, 0, 7, 0.0, "", false, rTwoPointOnePointOne) {
             @Override
            public String getName() {
                
                int level = (Integer)getPropertyValue(PROP_LEVEL);
                return "TZ("+level+")";
            }

            @Override
            public Object getValueFor(Object arg) {
                if (arg instanceof Integer) {
                    Integer level = (Integer) arg;
                    return getProfile().getValueForVariable(PhysicalProperty.TEMPERATURE.name(), level);
                }
                return null;
            }
        };

        Record rTwoPointOnePointTwo = new RecordAbstr("2.1.2", parent) {

            @Override
            public boolean isValid() {
                if (getParentTAPE5().getField("1.2", "IATM").getValue().equals(0)) {

                    return true;
                }

                return false;
            }
        };
        
        addChild(rTwoPointOnePointTwo);

        Record rTwoPointOnePointThree = new RecordAbstr("2.1.3", parent) {

            @Override
            public boolean isValid() {
                if (getParentTAPE5().getField("1.2", "IATM").getValue().equals(0)
                        && (Integer) getParentTAPE5().getField("2.1", "NMOL").getValue() > 7) {

                    return true;
                }

                return false;
            }
        };
        
        addChild(rTwoPointOnePointThree);

        Iterator<Molecule> iterator = profile.getMolecules().iterator();

        int i = 0;

        Record r = rTwoPointOnePointTwo;

        while (iterator.hasNext()) {


            i++;

            Molecule m = iterator.next();
            if (i > 7) {

                r = rTwoPointOnePointThree;
            }

            Field fWKL = new FieldImpl("WKL(" + m + ",L)", Format.FLOAT, 0, 10, 0.0, "", false, r) {
                
                 @Override
                public String getName() {
                
                    int level = (Integer)getPropertyValue(PROP_LEVEL);
                    Molecule m = (Molecule)getPropertyValue("molecule");
                    return "WKL("+m+","+level+")";
                }

                @Override
                public Object getValue() {
                    Molecule m = (Molecule) getPropertyValue("molecule");
                    int l = (Integer) getPropertyValue(PROP_LEVEL);
                    return getProfile().getValueForVariable(m.name(), l);
                }

                @Override
                public int getWidth() {
                    if (getParentTAPE5().getField("2.1", "IFORM").getValue().equals(1)) {

                        return 15;
                    }

                    return 10;
                }
            };

            fWKL.setPropertyValue("molecule", m);

            if (r.equals(rTwoPointOnePointTwo)) {

                Field fWBROAD = new FieldImpl("WBROAD", Format.FLOAT, 0, 10, 0.0, "", true, r) {

                    @Override
                    public String getName() {
                        int level = (Integer)getPropertyValue(PROP_LEVEL);
                        return "WBROAD("+level+")";
                    }
                    
                    

                    @Override
                    public int getWidth() {


                        if (getParentTAPE5().getField("2.1", "IFORM").getValue().equals(1)) {

                            return 15;
                        }

                        return 10;

                    }
                };

            }
        }
    }
}
