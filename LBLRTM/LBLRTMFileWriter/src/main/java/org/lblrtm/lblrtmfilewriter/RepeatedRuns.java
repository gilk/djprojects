/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter;

import java.util.List;
import org.lblrtm.lblrtmdata.api.Observer;
import org.lblrtm.lblrtmfilewriter.api.BoundaryUnits;
import org.lblrtm.lblrtmfilewriter.api.CalculationTemplate;
import org.lblrtm.lblrtmfilewriter.api.DJTAPE5;
import org.lblrtm.lblrtmfilewriter.api.Field;
import org.lblrtm.lblrtmfilewriter.api.Format;
import org.lblrtm.lblrtmfilewriter.api.Record;

/**
 *
 * @author djabry
 */
public class RepeatedRuns extends RepeatedRecordSet {

    public RepeatedRuns(DJTAPE5 t5) {

        super("All Records", t5);

        //t5.addChild(this);

    }

    private String generateHMOD() {

        return "Test atmospheric profile";
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void initRecords() {

        DJTAPE5 t5 = getParentTAPE5();
        Record rOnePointOne = new RecordAbstr("1.1") {

            @Override
            public boolean isValid() {
                return true;
            }
        };



        Field fCXID = new FieldImpl("CXID", Format.STRING, 1, 79, t5.getRunString(), "$", false, rOnePointOne) {

            @Override
            public Object getValue() {
                return this.getParentRecord().getParentTAPE5().getRunString();
            }

            @Override
            public int getWidth() {
                return getParentTAPE5().getRunString().length();
            }
        };

        addChild(rOnePointOne);

        //Record 1.2
        Record rOnePointTwo = new RecordAbstr("1.2") {

            @Override
            public boolean isValid() {
                return true;
            }
        };



        /*
         * (0,1,2,3,4,9) selects desired version of HIRAC
         *
         * = 0 HIRAC HIRAC not activated; line-by-line calculation is bypassed
         * (skips to selected function) = 1 HIRAC1 (Voigt profile) = 2 HIRACL
         * (Lorentz profile, not available in LBLRTM) = 3 HIRACD (Doppler
         * profile, not available in LBLRTM) = 4 NLTE Option (Non Local
         * Thermodynamic Equilibrium) -state populations as a function of
         * altitude required on TAPE4 = 9 central line contribution omitted
         * (functions 1-3)
         */
        Field iHIRAC = new FieldImpl("IHIRAC", Format.INTEGER, 4, 1, 1, "HI=", false, rOnePointTwo);

        /*
         * (0,1,2) flag for LBLF4 (Line-By-Line Function 4)
         *
         * (LBLF4 extends bound of line by line calculation beyond 64 halfwidths
         * from line center)
         *
         * = 0 LBLF4 not activated (line by line bound is 64 halfwidths) (Note:
         * if IHARAC>0 and line coupling is used in generating the input TAPE3
         * then ILBLF4 = 1) = 1 line by line bound is 25 cm-1 for all layer
         * pressures (altitudes) = 2 line by line bound is 25 cm-1 for layers
         * with pressures .GT. 0.5 mb and 5 cm-1 for layers with pressures .LE.
         * 0.5 mb (ILBLF4 = 2 saves computational time above 50 km with minimal
         * loss of accuracy for most cases)
         */
        Field iLBLF4 = new FieldImpl("ILBLF4", Format.INTEGER, 4, 1, 1, "F4=", false, rOnePointTwo);


        /*
         * (0,1,2,3,4,5,6) flag for continuum (CONTNM)
         *
         * = 0 no continuum calculated = 1 all continua calculated, including
         * Rayleigh extinction where applicable = 2 H2O self not calculated, all
         * other continua/Rayleigh extinction calculated = 3 H2O foreign not
         * calculated, all other continua/Rayleigh extinction calculated = 4 H2O
         * self and foreign not calculated, all other continua/Rayleigh
         * extinction calculated = 5 Rayleigh extinction not calculated, all
         * other continua calculated = 6 Individual continuum scale factors
         * input (Requires Record 1.2a)
         */

        //Continuum
        Field iCNTNM = new FieldImpl("ICNTNM", Format.INTEGER, 4, 1, 1, "CN=", false, rOnePointTwo);


        /*
         * (0,1,5,7,9) flag for aerosols (LOWTRN)
         *
         * = 0 no aerosols used = 1 internal LOWTRAN aerosol models = 5 spectral
         * optical depths by layer from file 'in_lblrtm_cld' = 7 user defined
         * aerosol models = 9 use precalculated aerosols (TAPE20 from a previous
         * aerosol run)
         */
        //Aerosol
        Field iAERSL = new FieldImpl("IAERSL", Format.INTEGER, 4, 1, 0, "AE=", false, rOnePointTwo);

        /*
         * (0,1,2,3)
         *
         * = 0 optical depth only = 1 radiance and transmittance (Radiance
         * Units: W / cm^2 sr^-1 cm^-1 = 2 solar radiance (requires previously
         * calculated optical depths or transmittances and binary solar
         * radiation file SOLAR.RAD) = 3 radiance analytic Jacobian/derivative -
         * requires IMRG=40,41,42 or 43 - requires a subdirectory named "AJ" to
         * contain the analytic Jacobian/derivative files Note: - requires
         * previously calculated optical depths files (created by setting
         * IEMIT=0, IMRG=1, IOD=3)
         *
         * - units (dR/dx): temperature: { W / (cm^2 sr cm^-1) ] / [K] }
         * molecules: { W / (cm^2 sr cm^-1) ] / [log(volume mixing ratio)] } sfc
         * temp: { W / (cm^2 sr cm^-1) ] / [K] } sfc emiss/refl: { W / (cm^2 sr
         * cm^-1) ] / [K] }
         */
        Field iEMIT = new FieldImpl("IEMIT", Format.INTEGER, 4, 1, 1, "EM=", false, rOnePointTwo);

        /*
         * (0,1,2,3) flag for SCANFN 1 = scanning function or INTRPL 2 =
         * interpolating procedure or FFTSCN 3 = Fast Fourier Transform scan
         */
        //Scanning prcedure
        Field iSCAN = new FieldImpl("ISCAN", Format.INTEGER, 4, 1, 0, "SC=", false, rOnePointTwo);

        //Filter function
        Field iFILTR = new FieldImpl("IFILTR", Format.INTEGER, 4, 1, 0, "FL=", false, rOnePointTwo);

        //Plot flag
        Field iPLOT = new FieldImpl("IPLOT", Format.INTEGER, 4, 1, 0, "PL=", false, rOnePointTwo);

        Field iTEST = new FieldImpl("ITEST", Format.INTEGER, 4, 1, 0, "TS=", false, rOnePointTwo);

        //Flag for LBL Atmosphere
        Field iATM = new FieldImpl("IATM", Format.INTEGER, 4, 1, 1, "AM=", false, rOnePointTwo);

        //Merge type
        Field iMRG = new FieldImpl("IMRG", Format.STRING, 3, 2, 1, "MG", false, rOnePointTwo);

        //Laser options
        Field iLAS = new FieldImpl("ILAS", Format.INTEGER, 4, 1, 0, "LA=", false, rOnePointTwo);

        //Layering control in optical depth calculations
        Field iOD = new FieldImpl("IOD", Format.INTEGER, 4, 1, 3, "OD=", false, rOnePointTwo);


        //Flag for cross-sections
        Field iXSECT = new FieldImpl("IXSECT", Format.INTEGER, 4, 1, 0, "XS=", false, rOnePointTwo);

        /*
         * Number of optical depth values printed for the beginning and ending
         * of each panel as a result of convolution for current layer (for laser
         * option ILAS > 1, MPTS should be set to 0) (for MPTS < O, output
         * printing is suppressed)
         */
        Field mPTS = new FieldImpl("MPTS", Format.INTEGER, 1, 4, 0, "", true, rOnePointTwo);

        /*
         * number of values printed for the beginning and ending of each panel
         * as result of merge of current layer with previous layers (optical
         * depth for IEMIT=0; radiance and transmission for IEMIT=1) (for laser
         * option ILAS > 1, NPTS should be set to 0)
         */
        Field nPTS = new FieldImpl("NPTS", Format.INTEGER, 1, 4, 0, "", true, rOnePointTwo);

        addChild(rOnePointTwo);


        //Record 1.2a - Continuum coefficients
        Record rOnePointTwoA = new RecordAbstr("1.2a") {

            @Override
            public boolean isValid() {


                if (getParentTAPE5().getField("1.2", "ICNTNM").getValue().equals(6)) {

                    return true;
                }

                return false;
            }
        };

        addChild(rOnePointTwoA);



        //Record 1.2.1

        Record rOnePointTwoPointOne = new RecordAbstr("1.2.1") {

            @Override
            public boolean isValid() {
                if (getParentTAPE5().getField("1.2", "IEMIT").getValue().equals(2)) {

                    return true;
                }

                return false;
            }
        };

        addChild(rOnePointTwoPointOne);

        Field fINFLAG = new FieldImpl("INFLAG", Format.INTEGER, 0, 5, 0, "", true, rOnePointTwoPointOne);

        Field fIOTFLG = new FieldImpl("IOTFLG", Format.INTEGER, 0, 5, 0, "", true, rOnePointTwoPointOne);

        Field fJULDAT = new FieldImpl("JULDAT", Format.INTEGER, 2, 3, 0, "", true, rOnePointTwoPointOne);

        //Record 1.3
        Record rOnePointThree = new RecordAbstr("1.3") {

            @Override
            public boolean isValid() {

                Record rOnePointTwo = getParentTAPE5().getRecord("1.2");
                if ((Integer) rOnePointTwo.getField("IHIRAC").getValue() > 0
                        || (Integer) rOnePointTwo.getField("IAERSL").getValue() > 0
                        || (Integer) rOnePointTwo.getField("IEMIT").getValue() == 1
                        || (Integer) rOnePointTwo.getField("IATM").getValue() == 1
                        || (Integer) rOnePointTwo.getField("ILAS").getValue() > 0) {

                    return true;
                }

                return false;
            }
        };

        addChild(rOnePointThree);


        Field fV1 = new FieldImpl("V1", Format.FLOAT, 0, 10, 0.0, rOnePointThree) {

            @Override
            public Object getValue() {
                
                DJTAPE5 t5 = getParentRecord().getParentTAPE5();
                
                double wnMargin = t5.getCalculationTemplate().getWavenumberMargin();
                double v1 = getParentRecord().getParentTAPE5().getV1();
                return Math.max(0.0, v1-wnMargin);
            }
        };

        Field fV2 = new FieldImpl("V2", Format.FLOAT, 0, 10, 0.0, rOnePointThree) {

            @Override
            public Object getValue() {
                
                DJTAPE5 t5 = getParentRecord().getParentTAPE5();
                
                double wnMargin = t5.getCalculationTemplate().getWavenumberMargin();
                double v2 = getParentRecord().getParentTAPE5().getV2();
                
                return v2+wnMargin;
            }
        };
        
        

        Field fSAMPLE = new FieldImpl("SAMPLE", Format.FLOAT, 0, 10, 4.0, "", true, rOnePointThree);
        Field fDVSET = new FieldImpl("DVSET", Format.FLOAT, 0, 10, 0.0, "", true, rOnePointThree);
        Field fALFAL0 = new FieldImpl("FALFAL0", Format.FLOAT, 0, 10, 0.04, "", true, rOnePointThree);
        Field fAVMASS = new FieldImpl("AVMASS", Format.FLOAT, 0, 10, 30.0, "", true, rOnePointThree);
        Field fDPTMIN = new FieldImpl("DPTMIN", Format.FLOAT, 0, 10, 0.0002, "", true, rOnePointThree);
        Field fDPTFAC = new FieldImpl("fDPTFAC", Format.FLOAT, 0, 10, 0.001, "", true, rOnePointThree);
        Field fILNFLG = new FieldImpl("ILNFLG", Format.INTEGER, 4, 1, 0, "", true, rOnePointThree);
        Field fDVOUT = new FieldImpl("DVOUT", Format.FLOAT, 5, 10, 0.0, "", true, rOnePointThree);
        Field fNMOL_SCAL = new FieldImpl("NMOL_SCAL", Format.INTEGER, 3, 2, 0, "", true, rOnePointThree);


        //Record 1.3.a
        Record rOnePointThreePointA = new RecordAbstr("1.3.a") {

            @Override
            public boolean isValid() {
                if ((Integer) this.getParentTAPE5().getField("1.3", "NMOL_SCAL").getValue() > 0) {

                    return true;
                }
                return false;
            }
        };

        addChild(rOnePointThreePointA);

        //Record 1.3.b
        Record rOnePointThreePointB = new RecordAbstr("1.3.b") {

            @Override
            public boolean isValid() {
                if ((Integer) this.getParentTAPE5().getField("1.3", "NMOL_SCAL").getValue() > 0) {

                    return true;
                }
                return false;
            }
        };

        addChild(rOnePointThreePointB);

        //Record 1.4
        Record rOnePointFour = new RecordAbstr("1.4") {

            @Override
            public boolean isValid() {

                Field iEMIT = getParentTAPE5().getField("1.2", "IEMIT");

                if (iEMIT.getValue().equals(1)
                        || (iEMIT.getValue().equals(2) && getParentTAPE5().getField("1.2.1", "IOTFLG").getValue().equals(2))
                        //LBLRTM instructions do not state the condition below but this seems necessary
                        || iEMIT.getValue().equals(3)) {

                    return true;
                }
                return false;
            }
        };

        addChild(rOnePointFour);

        Field fTBOUND = new FieldImpl("TBOUND", Format.FLOAT, 0, 10, 300.0, "", false, rOnePointFour) {

            @Override
            public Object getValue() {
                return getParentRecord().getParentTAPE5().getSurface().getSurfaceTemperature();
            }
        };


        Field fSREMIS1 = new FieldImpl("SREMIS(1)", Format.FLOAT, 0, 10, 0.0, "", false, rOnePointFour) {

            @Override
            public Object getValue() {
                return getParentRecord().getParentTAPE5().getSurface().getEmissionCoefficient(0);
            }
        };
        Field fSREMIS2 = new FieldImpl("SREMIS(2)", Format.FLOAT, 0, 10, 0.0, "", true, rOnePointFour) {

            @Override
            public Object getValue() {
                return getParentRecord().getParentTAPE5().getSurface().getEmissionCoefficient(1);
            }
        };
        Field fSREMIS3 = new FieldImpl("SREMIS(3)", Format.FLOAT, 0, 10, 0.0, "", true, rOnePointFour) {

            @Override
            public Object getValue() {
                return getParentRecord().getParentTAPE5().getSurface().getEmissionCoefficient(2);
            }
        };

        Field fSRREFL1 = new FieldImpl("SRREFL(1)", Format.FLOAT, 0, 10, 0.0, "", true, rOnePointFour) {

            @Override
            public Object getValue() {
                return getParentRecord().getParentTAPE5().getSurface().getReflectionCoefficient(0);
            }
        };


        Field fSRREFL2 = new FieldImpl("SRREFL(2)", Format.FLOAT, 0, 10, 0.0, "", true, rOnePointFour) {

            @Override
            public Object getValue() {
                return getParentRecord().getParentTAPE5().getSurface().getReflectionCoefficient(1);
            }
        };

        Field fSRREFL3 = new FieldImpl("SRREFL(3)", Format.FLOAT, 0, 10, 0.0, "", true, rOnePointFour) {

            @Override
            public Object getValue() {
                return getParentRecord().getParentTAPE5().getSurface().getReflectionCoefficient(2);
            }
        };

        Field fSurf_refl = new FieldImpl("surf_refl", Format.STRING, 4, 1, "s", "", true, rOnePointFour) {

            @Override
            public Object getValue() {
                return getParentRecord().getParentTAPE5().getSurface().getSurfaceType().getSymbol();
            }
        };

        //Record 1.5
        Record rOnePointFive = new RecordAbstr("1.5") {

            @Override
            public boolean isValid() {
                if (getParentTAPE5().getField("1.2", "IEMIT").getValue().equals(3)
                        && ((Integer) getParentTAPE5().getField("1.2", "IMRG").getValue() > 39
                        && (Integer) getParentTAPE5().getField("1.2", "IMRG").getValue() < 44)) {
                    return true;
                }

                return false;
            }
        };

        addChild(rOnePointFive);

        Field fNSPCRT = new FieldImpl("NSPCRT", Format.INTEGER, 0, 5, 1, "", false, rOnePointFive);

        //Record 1.6a
        Record rOnePointSixA = new RecordAbstr("1.6a") {

            @Override
            public boolean isValid() {

                int iMRG = (Integer) getParentTAPE5().getField("1.2", "IMRG").getValue();

                if (iMRG == 35
                        || iMRG == 36
                        || iMRG == 40
                        || iMRG == 41
                        || iMRG == 45
                        || iMRG == 46) {

                    return true;
                }

                return false;
            }
        };

        addChild(rOnePointSixA);

        Field fPTHODL = new FieldImpl("PTHODL", Format.STRING, 0, 55, "", "", false, rOnePointSixA) {

            @Override
            public int getWidth() {
                return ((String) getValue()).length();
            }

        };

        Field fLAYTOT = new FieldImpl("LAYTOT", Format.INTEGER, 1, 4, 0, "", true, rOnePointSixA);

        //Record 2.1
        Record rTwoPointOne = new RecordAbstr("2.1") {

            @Override
            public boolean isValid() {
                if (getParentTAPE5().getField("1.2", "IATM").getValue().equals(0)) {
                    //Not sure if this is the only requirement since 2.1 does not appear in many 
                    //example TAPE5 files with IATM = 0;
                    return false;
                }

                return false;
            }
        };

        addChild(rTwoPointOne);

        Field fIFORM = new FieldImpl("IFORM", Format.INTEGER, 1, 1, 0, "", true, rTwoPointOne);

        Field fNLAYERS = new FieldImpl("NLAYERS", Format.INTEGER, 0, 3, 0, "", false, rTwoPointOne);

        Field fNMOL = new FieldImpl("NMOL", Format.INTEGER, 0, 5, 7, "", true, rTwoPointOne);

        Field fSECNTO = new FieldImpl("SECNTO", Format.FLOAT, 0, 10, 1.0, "", true, rTwoPointOne);

        //Observer altitude
        Field fZH1 = new FieldImpl("ZH1", Format.FLOAT, 20, 8, 0.0, "", true, rTwoPointOne);

        Field fZH2 = new FieldImpl("ZH2", Format.FLOAT, 4, 8, 0.0, "", true, rTwoPointOne);

        Field fZANGLE = new FieldImpl("ZANGLE", Format.FLOAT, 5, 8, 0.0, "", true, rTwoPointOne);

        //To do: Add records 2.1.1-3
        Record rThreePointOne = new RecordAbstr("3.1") {

            @Override
            public boolean isValid() {
                if (getParentTAPE5().getField("1.2", "IATM").getValue().equals(1)) {
                    return true;
                }
                return false;
            }
        };

        addChild(rThreePointOne);

        Field fMODEL = new FieldImpl("MODEL", Format.INTEGER, 0, 5, 0, "", false, rThreePointOne) {

            @Override
            public Object getValue() {
                return getParentTAPE5().getAtmosphericModel().getIndex();
            }
        };

        Field fITYPE = new FieldImpl("ITYPE", Format.INTEGER, 0, 5, 2, "", false, rThreePointOne);

        Field fIBMAX = new FieldImpl("IBMAX", Format.INTEGER, 0, 5, 0, "", false, rThreePointOne) {

            @Override
            public Object getValue() {
                CalculationTemplate cT = getParentTAPE5().getCalculationTemplate();
                BoundaryUnits bU = cT.getBoundaryUnits();
                int m = 1;
                if(bU.equals(BoundaryUnits.PRESSURE)){
                    
                    m=-1;
                }
                
                return m*cT.getNumberOfBoundaries();
                    
            }
        };


        Field fZERO = new FieldImpl("ZERO", Format.INTEGER, 0, 5, 2, "", true, rThreePointOne);

        Field fNOPRNT = new FieldImpl("NOPRNT", Format.INTEGER, 0, 5, 0, "", false, rThreePointOne);

        Field fNMOL2 = new FieldImpl("NMOL", Format.INTEGER, 0, 5, 0, "", true, rThreePointOne);

        Field fIPUNCH = new FieldImpl("IPUNCH", Format.INTEGER, 0, 5, 0, "", false, rThreePointOne);

        Field fIFXTYP = new FieldImpl("IFXTYP", Format.INTEGER, 0, 2, 0, "", true, rThreePointOne);

        Field fMNUNITS = new FieldImpl("MNUNITS", Format.INTEGER, 1, 2, 0, "", true, rThreePointOne);

        Field fRE = new FieldImpl("RE", Format.FLOAT, 0, 10, 0.0, "", true, rThreePointOne);

        Field fHSPACE = new FieldImpl("HSPACE", Format.FLOAT, 0, 10, 0.0, "", true, rThreePointOne);

        Field fVBAR = new FieldImpl("VBAR", Format.FLOAT, 0, 10, 0.0, "", true, rThreePointOne);

        Field fREF_LAT = new FieldImpl("REF_LAT", Format.FLOAT, 10, 10, 0.0, "", true, rThreePointOne);


        Record rThreePointTwo = new RecordAbstr("3.2") {

            @Override
            public boolean isValid() {

                int model = (Integer) getParentTAPE5().getField("3.1", "MODEL").getValue();
                int iTYPE = (Integer) getParentTAPE5().getField("3.1", "ITYPE").getValue();
                if (this.getParentTAPE5().getField("1.2", "IATM").getValue().equals(1)
                        && (model >= 0 || model <= 6) && iTYPE == 2) {
                    return true;
                }
                return false;
            }
        };

        addChild(rThreePointTwo);

        Field fH1 = new FieldImpl("H1", Format.FLOAT, 0, 10, 0.0, "", false, rThreePointTwo) {

            @Override
            public Object getValue() {

                Observer observer = getParentTAPE5().getObserver();
                return observer.getAltitude();
            }
        };

        Field fH2 = new FieldImpl("H2", Format.FLOAT, 0, 10, 0.0, "", false, rThreePointTwo) {

            @Override
            public Object getValue() {
                return getParentTAPE5().getEndHeight();
            }
        };

        Field fANGLE = new FieldImpl("ANGLE", Format.FLOAT, 0, 10, 180.0, "", false, rThreePointTwo) {

            @Override
            public Object getValue() {
                return getParentRecord().getParentTAPE5().getAngle();
   
            }
        };

        Field fRANGE = new FieldImpl("RANGE", Format.INTEGER, 0, 10, 0, "", false, rThreePointTwo);

        Field fBETA = new FieldImpl("BETA", Format.FLOAT, 0, 10, 0.0, "", false, rThreePointTwo);

        Field fLEN = new FieldImpl("LEN", Format.INTEGER, 0, 5, 0, "", true, rThreePointTwo);

        Field fHOBS = new FieldImpl("HOBS", Format.FLOAT, 0, 10, 0.0, "", false, rThreePointTwo);

        Record rThreePointThreeA = new RecordAbstr("3.3A") {

            @Override
            public boolean isValid() {
                if (getParentTAPE5().getField("3.1", "IBMAX").getValue().equals(0)
                        && getParentTAPE5().getField("1.2", "IATM").getValue().equals(1)) {
                    return true;

                }

                return false;
            }
        ;

        };
        
        addChild(rThreePointThreeA);

        Field fAVTRAT = new FieldImpl("AVTRAT", Format.FLOAT, 0, 10, 0.0, "", false, rThreePointThreeA);

        Field fTDIFF1 = new FieldImpl("TDIFF1", Format.FLOAT, 0, 10, 0.0, "", false, rThreePointThreeA);

        Field fTDIFF2 = new FieldImpl("TDIFF2", Format.FLOAT, 0, 10, 0.0, "", false, rThreePointThreeA);

        Field fALTD1 = new FieldImpl("ALTD1", Format.FLOAT, 0, 10, 0.0, "", false, rThreePointThreeA);

        Field fALTD2 = new FieldImpl("ALTD2", Format.FLOAT, 0, 10, 0.0, "", false, rThreePointThreeA);

        Record rThreePointThreeB = new RecordThreePointThreeB(this);

        Record rThreePointFour = new RecordAbstr("3.4") {

            @Override
            public boolean isValid() {
                if (getParentTAPE5().getField("3.1", "MODEL").getValue().equals(0)
                        && getParentTAPE5().getField("1.2", "IATM").getValue().equals(1)) {
                    return true;
                }
                return false;
            }
        };

        addChild(rThreePointFour);

        Field fIMMAX = new FieldImpl("IMMAX", Format.INTEGER, 0, 5, 0, "", false, rThreePointFour) {

            @Override
            public Object getValue() {

                return getParentRecord().getParentTAPE5().getProfile().getNumberOfLevels();
            }
        };

        Field fHMOD = new FieldImpl("HMOD", Format.STRING, 0, 24, generateHMOD(), "", false, rThreePointFour);


        //User defined atmosphere
        Record rUDAP = new UserDefinedAtmosphereRecord(this);

        //addChild(rUDAP);

        //To do: Implement other records 3.x and 4.x


        //To do: Implement other scanning procedure records 7.1x - 
        
        
        //Scan results
        Record rEightPointOne = new RecordAbstr("8.1"){

            @Override
            public boolean isValid() {
                if (getParentTAPE5().getField("1.2", "ISCAN").getValue().equals(1)) {

                    return true;
                }

                return false;
            }
            
        };
        
        addChild(rEightPointOne);
        
        /* HWHM    (Half Width Half Maximum)
 
                    negative value terminates SCANFN option
 
                  Notes: 1. HWHM is first zero crossing of periodic functions for JFN < 0.
                            HWHM is redefined as  HWHM=(FIRST ZERO)/(PI/SCALE)
                       2. HWHM is instrument field of view half angle (in degrees) for JFN=5,6
                            (e.g., for FOV of 10.0 degrees, HWHM=5.0)
        */
        
        Field fHWHM = new FieldImpl("HWHM",Format.FLOAT,0,10,5.0,"",false,rEightPointOne);
        
        Field fscnV1 = new FieldImpl("V1",Format.FLOAT,0,10,100.0,"",false,rEightPointOne){
            
            @Override
            public Object getValue() {
                return getParentTAPE5().getV1();
            }
        };
        
        Field fscnV2 = new FieldImpl("V2",Format.FLOAT,0,10,200.0,"",false,rEightPointOne){
            
            @Override
            public Object getValue() {
                return getParentTAPE5().getV2();
            }
        };
        
        /*JEMIT     = -1  SCANFN convolved with absorption (1.0 - transmission)
 
                    =  0  SCANFN convolved with transmission
 
                    =  1  SCANFN convolved with radiance 
                    */
        
        Field fscnJEMIT = new FieldImpl("JEMIT",Format.INTEGER,3,2,1,"",false,rEightPointOne);
        
        
        /*JFN    selects choice of scanning function
 
                      JFN    Function                HWHM         Default HWHM Bound     Default Sample
                      ---    --------               ------        ------------------     --------------
                    =  0     rectangular                                1.0                  0.5
 
                    =  1     triangular                                 2.0                  2.0
 
                    =  2     gaussian               param               4.0                  4.0
 
                    =  3     sinc squared                              54.1826               4.0
 
                    =  4     sinc                                     119.332818             4.0
 
                    =  5     FOV correction                             1.0                  0.5
                             (uses center of box)
 
                    =  6     FOV correction                             1.0                  0.5
                             (uses left edge of box)
 

                          Notes: 1. For each function BOUND = HWHM*(Half-width Bound).  BOUND represents the overlap
                                    required before V1 and after V2 in the monochromatic calculation. (Record 1.3)
 
                                 2. If JFN < 0, HWHM contains the value for the first zero crossing of periodic function.
 
                                 3. JFN cannot hold the values of -5 or -6.
 
                                 4. JFN = 5,6 cannot be used with IMRG = 13-18,35,36 on Record 1.2. */
        
        
        Field fJFN = new FieldImpl("JFN",Format.INTEGER,3,2,4,"",false,rEightPointOne);
        
        /*JVAR    flag for variable HWHM
 
                    =  0  no variation
 
                    =  1  HWHM(vi) = HWHM(v1) * (vi / v1) */
        
        Field fJVAR = new FieldImpl("JVAR",Format.INTEGER,3,2,0,"",false,rEightPointOne);
        
        /*SAMPL    number of sample points output per half width
 
                    =  0  gives default value for each function
 
                    <  0  this variable specifies the output spectral spacing (DELVO cm-1)
                          The value of SAMPL is calculated internally as SAMPL = HWHM/DELVO
 
                         Note: SAMPL must be < 0 and carry the value of the output spectral spacing for JFN=5,6.*/
        
        Field fscnSAMPLE = new FieldImpl("SAMPL",Format.FLOAT,0,10,0.0,"",false,rEightPointOne);
        
        Field fscnIUNIT = new FieldImpl("IUNIT", Format.INTEGER,3,2,11,"",true,rEightPointOne);
        
        Field fscnIFILST = new FieldImpl("IFILST", Format.INTEGER,3,2,1,"",true,rEightPointOne);
        
        Field fscnNIFILS = new FieldImpl("NIFILS", Format.INTEGER,3,2,1,"",true,rEightPointOne);
        
        Field fscnJUNIT = new FieldImpl("JUINT", Format.INTEGER,3,2,11,"",true,rEightPointOne);

         /*NPTS    number of values to be printed for the beginning and ending of each panel for current scanned file */
        Field fscnNPTS = new FieldImpl("NPTS", Format.INTEGER,0,5,2400,"",true,rEightPointOne);
         
        
        /*param    additional parameter for selected functions, e.g. HWHM for JFN=2
 
         ***** Note: if the DV of the data to be scanned is larger than DVINT = HWHM/12., *****
         *****       the data is first interpolated to DVINT before it is scanned.        *****
         *****       Interpolated data is written to named file 'SCNINTF'.                *****
         *****       Four-point interpolation is used for this procedure.                 *****
        */
        
        
        Field fscnparam = new FieldImpl("param", Format.FLOAT,0,10,0.0,"",true,rEightPointOne);
        
        
        
        
        
        //Interpolate results
        Record rNinePointOne = new RecordAbstr("9.1") {

            @Override
            public boolean isValid() {
                if (getParentTAPE5().getField("1.2", "ISCAN").getValue().equals(2)) {

                    return true;
                }

                return false;
            }
        };

        addChild(rNinePointOne);


        Field fDV0 = new FieldImpl("DV0", Format.FLOAT, 0, 10, 1.0, "", false, rNinePointOne) {

            @Override
            public Object getValue() {

                return getParentTAPE5().getDV();
            }
        };

        Field fV1NinePointOne = new FieldImpl("V1", Format.FLOAT, 0, 10, 0.0, "", false, rNinePointOne) {

            @Override
            public Object getValue() {
                return getParentTAPE5().getV1();
            }
        };

        Field fV2NinePointOne = new FieldImpl("V2", Format.FLOAT, 0, 10, 0.0, "", false, rNinePointOne) {

            @Override
            public Object getValue() {
                return getParentTAPE5().getV2();
            }
        };

        /*
         * JEMIT = -1 interpolation of absorption (1 - transmission)
         *
         * = 0 interpolation of transmission
         *
         * = 1 interpolation of radiance
         */
        Field fJEMIT = new FieldImpl("JEMIT", Format.INTEGER, 0, 5, 1, "", false, rNinePointOne);

        Field fI4PT = new FieldImpl("I4PT", Format.INTEGER, 0, 5, 0, "", false, rNinePointOne);

        //IUNIT    unit designation of file to be interpolated (default is MFILE)
        Field fIUNIT = new FieldImpl("IUNIT", Format.INTEGER, 15, 5, 24, "", false, rNinePointOne);

        //IFILST    initial file from IUNIT to be interpolated
        Field fIFILST = new FieldImpl("IFILST", Format.INTEGER, 0, 5, 0, "", true, rNinePointOne);

        // NIFILS    number of files to be interpolated starting at IFILST
        Field fNIFILS = new FieldImpl("NIFILST", Format.INTEGER, 0, 5, 0, "", true, rNinePointOne);

        //JUNIT    file containing interpolated results (default is JUNIT, file 11)
        Field fJUNIT = new FieldImpl("JUNIT", Format.INTEGER, 0, 5, 25, "", false, rNinePointOne);

        //NPTS number of values to be printed for the beginning and ending of each panel for current interpolated file
        Field fNPTS = new FieldImpl("NPTS", Format.INTEGER, 0, 5, 0, "", true, rNinePointOne);

        
        
        
        
        //FFT scan of results
        Record rTenPointOne = new RecordAbstr("10.1") {

            @Override
            public boolean isValid() {
                if (getParentTAPE5().getField("1.2", "ISCAN").getValue().equals(3)) {

                    return true;
                }

                return false;
            }
        };
        
        addChild(rTenPointOne);
        
        
        /*HWHM    (Half Width Half Maximum)
 
                    negative value terminates FFTSCN option
 
                  Note: HWHM is the maximum optical path difference of an
                      equivalent interferometer for JFNin < 0.*/
        
        Field ffftHWHM = new FieldImpl("HWHM",Format.FLOAT,0,10,1.0,"",false,rTenPointOne);
        
        Field ffftV1 = new FieldImpl("V1",Format.FLOAT,0,10,100.0,"",false,rTenPointOne){
            
            @Override
            public Object getValue() {
                return getParentTAPE5().getV1();
            }
        };
        
        Field ffftV2 = new FieldImpl("V2",Format.FLOAT,0,10,200.0,"",false,rTenPointOne){
            
            @Override
            public Object getValue() {
                return getParentTAPE5().getV2();
            }
        };
        
        Field ffftJEMIT = new FieldImpl("JEMIT",Format.INTEGER,0,5,1,"",false,rTenPointOne);
        
        
        /* JFNin    selects choice of scanning function
 
 
 
                    JFNin    Scanning Function          Apodization Function       a/HWHM    a/FZ     CR       
                    -----    -----------------          --------------------       ------    ----     --
                    =  0     boxcar
 
                    =  1     1-v/a, |v|<a               (sin(pi*x*a)/pi*x*a))**2   2.0       1.0      40
                              0, |v|>a                 (sinc squared)
                               (triangle)
 
                    =  2     exp(-0.5*(v/a)**2)         exp(-2*pi*(a*x)**2)        0.849322  (NA)     10
                               (gauss)                    (gauss)
 
                    =  3     (sin(pi*x*a)/pi*x*a))**2   1-x*a, |x|=<1/a            2.257609  1.0      40
                               (sinc squared)               0, |x|>1/a
                                                          (triangle)
 
                    =  4     sin(u)/u                   1, |x|=<1/a                3.314800  2.0      160
                               (sinc)                   0, |x|>1/a
                                                          (rectangle)
 
                    =  5     J(5/2,u)/(u**(5/2))        (1-(x*a)**2)**2            2.100669  0.91728  20
                               (Beer)
 
                    =  6     sinc(u)+c1*(sinc(u+pi)+    (1+2*c1*cos(pi*x*a))/      2.195676  1.0      20
                               sinc(u-pi))                (1+2*c1)
                               (Hamming)
 
                    =  7     sinc(u)+0.5*(sinc(u+pi)+   (1+cos(pi*x*a))/2          2.0       1.0      20
                               sinc(u-pi))
                               (Hanning)
 
                      NORTON-BEER FUNCTIONS:
                      _____________________
                                                  sum{Ci*(1-(x*a)**2)**i}
                                                    from i=0 to 4, for
                                                    0 =< x =< 1/a
                                                          and
                                                  0 for x > 1/a
 
                  =  8      weak                  Ci = 0.384093,-0.087577,   2.57027        40
                                                      0.703484, 0., 0.
 
                  =  9      moderate              Ci = 0.152442,-0.136176,   2.36771        40
                                                      0.983734, 0., 0.
 
                  =  10     strong                Ci = 0.045335, 0.,       2.07176          20
                                                      0.554883,0.,0.399782
 
 
                            
                    JFNin    Scanning Function          Apodization Function       a/HWHM    a/FZ     CR       
                    -----    -----------------          --------------------       ------    ----     --
 
                      OTHER FUNCTIONS:
                      _______________
 
                  =  11     NA                    1   for x < p                variable            100
                             (Brault)                      and
                                                  (1+cos**2(u'))/2, for
                                                      p/a < x < 1/a
                                                  where (0 < p < 1)
 
                  =  12     NA                    Io(pi*p*sqrt(1-(x*a)**2)/      variable            10
                             (Kaiser-Bessel)                Io(pi*p), for
                                                      0 =< x =< 1/a
                                                          and
                                                  0   for x > 1/a
                                                  where (2 < p < 4)
 
                  =  13     c1*sinc(u)+             NA                             variable            160
                             c2*sinc(u-2*Pi*
                             v_offset/a)
                             (Kiruna, assymetric)   
 
    _______________________________________________________________________________________________________________________
 
                  LEGEND:
 
                         -  v  = frequency (cm-1)
                         -  x  = optical path difference (cm)
                         -  u  = (2*pi*v)/a
                         -  u' = pi*(x*a-p)/(1-p)
                         -  a  = 1/L, where L is the maximum optical path difference of an equivalent interferometer.
                                 "a" determines the resolution, or the width of the scanning function.
                         -  FZ = distance from the center of the scanning function to the first zero.
                         -  CR = critical value for the ratio of the extent of the spectrum (v2-v1) and the HWHM.
                                 When the ratio is less than CR, the apodization function is calculated as the FFT
                                 of the scanning function.  When the ratio is greater than CR, the apodization
                                 function is calculated analytically.
                         -  J(5/2,u)/(u**(5/2)) = ((3-u**2)*sin(u)-3*u*cos(u))/u**5, where J(n,u) is the Bessel
                                 function of order n.
                         -  Io = modified Bessel function of the first kind, order 0.
                         -  c1 = 0.428752
 
    _______________________________________________________________________________________________________________________
 
                    Notes:   1.  If JFNin < 0, HWHM contains the value for the maximum optical path difference of an
                                equivalent interferometer, apodized to give the scanning function given by |JFNin|.
 
                             2.  The Brault and Kaiser-Bessel functions depend upon the parameter p.  The values of
                                a/HWHM and a/FZ for these functions also depend upon p.*/
        
        

        Field fJFNin = new FieldImpl("JFNin",Format.INTEGER,0,5,4,"",false,rTenPointOne);
        
        /*MRATin    ratio of HWHM of the scanning function to the halfwidth of the boxcar; for prescanning with a
                    boxcar (default = 12.).  If MRATin < 0., no boxcaring is performed.*/
        Field fMRATin = new FieldImpl("MRATin",Format.INTEGER,0,5,12,"",false,rTenPointOne);
        
        Field ffftDVOUT  = new FieldImpl("DVOUT",Format.FLOAT,0,10,1.0,"",false,rTenPointOne){

            @Override
            public Object getValue() {
                return getParentTAPE5().getDV();
            }
            
            
        };
        
        //IUNIT    unit designation of file to be interpolated (default is MFILE)
        Field ffftIUNIT = new FieldImpl("IUNIT", Format.INTEGER, 0, 5, 24, "", false, rTenPointOne);

        //IFILST    initial file from IUNIT to be interpolated
        Field ffftIFILST = new FieldImpl("IFILST", Format.INTEGER, 0, 5, 0, "", true, rTenPointOne);

        // NIFILS    number of files to be interpolated starting at IFILST
        Field ffftNIFILS = new FieldImpl("NIFILST", Format.INTEGER, 0, 5, 0, "", true, rTenPointOne);

        //JUNIT    file containing interpolated results (default is JUNIT, file 11)
        Field ffftJUNIT = new FieldImpl("JUNIT", Format.INTEGER, 0, 5, 25, "", false, rTenPointOne);

        /*IVX    (-1,0,1) flag for calculation of scanning function
 
                     = -1 scanning function is calculated as the FFT of the apodization function
                     =  0 program determines how to calculate the scanning function, using CR
                     =  1 scanning function is calculated analytically*/
        
        Field fIVX = new FieldImpl("IVX", Format.INTEGER, 0, 3, 0, "", false, rTenPointOne);

        
        /*NOFIX    flag for deconvolution (used when prescanning with a boxcar)
 
                     = 0 deconvolve the scanned spectrum with the boxcar
                     = nonzero, do not deconvolve with the boxcar*/
        
        Field fNOFIX = new FieldImpl("NOFIX", Format.INTEGER, 0, 2, 0, "", false, rTenPointOne);
        
  
        Record rTenPointTwo = new RecordAbstr("10.2") {

            @Override
            public boolean isValid() {
                if (getParentTAPE5().getField("1.2", "ISCAN").getValue().equals(3)) {

                    if(Math.abs(((Integer)getParentTAPE5().getField("10.1","JFNin").getValue()))>10){

                        return true;
                    }
                }


                return false;
            }
        };
        
        addChild(rTenPointTwo);
        
        /*PARM1            For JFNin = 11, value of p, where (0 < p < 1)
                      For JFNin = 12, value of p, where (2 < p < 4)
                      For JFNin = 13, value of v_offset*/
        
        Field fPARAM1 = new FieldImpl("PARAM1",Format.FLOAT,0,10,0.0,"",false,rTenPointTwo);
        
        /*PARM2     For JFNin = 13, value of c1*/
        
        Field fPARAM2 = new FieldImpl("PARAM2",Format.FLOAT,0,10,0.0,"",false,rTenPointTwo);
        
        
        /*PARM3     For JFNin = 13, value of c2*/

        Field fPARAM3 = new FieldImpl("PARAM3",Format.FLOAT,0,10,0.0,"",false,rTenPointTwo);
        
    }

    @Override
    public List<Record> getAllRecords() {
        List<Record> records = super.getAllRecords();

        //Append "-1." and "%"
        RecordAbstr terminationR = new RecordAbstr("12.2B") {

            @Override
            public boolean isValid() {
                return true;
            }
        };

        records.add(terminationR);




        Field terminationField = new FieldImpl("12.2B", Format.STRING, 0, 3, "-1.", "", false, terminationR);

        RecordAbstr terminationR2 = new RecordAbstr("12.2C") {

            @Override
            public boolean isValid() {
                return true;
            }
        };

        records.add(terminationR2);

        Field terminationField2 = new FieldImpl("12.2C", Format.STRING, 0, 1, "%", "", false, terminationR2);

        return records;
    }
}
