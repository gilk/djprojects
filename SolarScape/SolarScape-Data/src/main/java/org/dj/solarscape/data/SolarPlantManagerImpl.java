/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.solarscape.data;

import com.google.common.collect.Lists;
import gov.nasa.worldwind.geom.Position;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javolution.util.FastList;
import javolution.util.FastMap;
import javolution.util.FastSet;
import org.dj.db.api.DBObjectManagerAbstr;
import org.dj.domainmodel.api.DJNodeObject;
import org.dj.executor.api.DJRunnableAbstr;
import org.dj.executor.api.Executor;
import org.dj.fileutilities.api.FileUtilities;
import org.dj.solarscape.data.api.SolarPlant;
import org.dj.solarscape.data.api.SolarPlantManager;
import org.dj.worldwindmodel.locationservice.api.LocationService;
import org.dj.worldwindmodel.locationservice.api.LocationService.PositionNotFoundException;
import org.dj.worldwindmodel.object.api.WWDJObject;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider(service = SolarPlantManager.class)
public class SolarPlantManagerImpl extends DBObjectManagerAbstr<SolarPlant> implements SolarPlantManager {

    private static final LocationService lS = Lookup.getDefault().lookup(LocationService.class);
    private static final List<String> addressSearchParams;
    private static final String LATITUDE_HEADING = "latitude";
    private static final String LONGITUDE_HEADING = "longitude";
    private static final Executor ex = Lookup.getDefault().lookup(Executor.class);
    private static final FileUtilities fU = Lookup.getDefault().lookup(FileUtilities.class);

    public SolarPlantManagerImpl() {
    }

    static {

        addressSearchParams = new FastList<String>();

        addressSearchParams.add("city");
        addressSearchParams.add("state_province");
        addressSearchParams.add("country");
        addressSearchParams.add("zipcode");

    }

    @Override
    public void addFromFile(String file) throws FileNotFoundException, IOException {

        DataImporter dI = new DataImporter(new File(file));
        dI.run();
    }

    private class DataImporter extends DJRunnableAbstr {

        private final File f;
        private File tempDataFile;
        private BufferedWriter writer;
        private boolean containsPositions;
        private final boolean writeFileOnly;
        private Position pos;
        private Calendar comCalendar;
        private int latIndex;
        private int lonIndex;
        private List<String> headings;
        private List<String> vals;
        private String latString;
        private String lonString;
        private Map<String, Object> props;
        private final Set<SolarPlant> plants = new FastSet<SolarPlant>();

        public DataImporter(File f) {

            this(f, false);
        }

        public DataImporter(File f, boolean writeFileOnly) {
            this.f = f;
            this.writeFileOnly = writeFileOnly;
            this.pos = Position.ZERO;

            comCalendar = Calendar.getInstance();
            comCalendar.set(Calendar.HOUR, 0);
            comCalendar.set(Calendar.MINUTE, 0);
            comCalendar.set(Calendar.SECOND, 0);

        }

        private List<String> readValuesFromLine(String line) {
            List<String> values = Lists.newArrayList(Arrays.asList(line.split(",")));
            return values;

        }

        private void writeToFile(List<String> values) throws IOException {
            System.out.println("Writing values to file...");
            Iterator<String> iterator = values.iterator();

            try {
                while (iterator.hasNext()) {
                    String next = iterator.next();
                    this.writer.append(next);
                    if (iterator.hasNext()) {
                        writer.append(",");
                    } else {
                        writer.append("\n");
                    }
                }
            } finally {
                writer.flush();
            }

        }

        private void readValuesFromFile(String file) throws FileNotFoundException, IOException {
            BufferedReader bufRdr = new BufferedReader(new FileReader(file));
            String line = null;
            int length = 0;
            //Count number of lines;
            while (bufRdr.readLine() != null) {
                length++;
            }

            bufRdr.close();

            this.getProgressHandle().switchToDeterminate(length);
            headings = new FastList<String>();

            bufRdr = new BufferedReader(new FileReader(file));
            int progress = 0;

            if ((line = bufRdr.readLine()) != null) {
                headings = readValuesFromLine(line);
                if (headings.contains(LATITUDE_HEADING) && headings.contains(LONGITUDE_HEADING)) {
                    //Read positions from headings
                    this.containsPositions = true;

                } else {
                    //Lookup positions from location service and
                    this.containsPositions = false;
                    tempDataFile = File.createTempFile("temp_solar_plant_data_", "", fU.getTempDir());
                    writer = new BufferedWriter(new FileWriter(tempDataFile));
                    headings.add(LATITUDE_HEADING);
                    headings.add(LONGITUDE_HEADING);
                    this.writeToFile(headings);
                }
            } else {

                return;
            }

            latIndex = headings.indexOf(LATITUDE_HEADING);
            lonIndex = headings.indexOf(LONGITUDE_HEADING);
            progress++;
            this.getProgressHandle().progress(progress);
            while ((line = bufRdr.readLine()) != null) {
                pos = Position.ZERO;
                vals = readValuesFromLine(line);

                int offset = 0;
                if (!this.containsPositions) {
                    offset = 2;
                }

                if (headings.size() - offset == vals.size() + 1) {
                    //if zip code is missing, pad with empty string
                    vals.add("");
                }

                if (headings.size() - offset == vals.size()) {

                    props = this.interpret(headings, vals);

                    if (!this.containsPositions) {


                        vals.add(latString);
                        vals.add(lonString);

                    }

                    if (!this.writeFileOnly) {
                    SolarPlantImpl sP = new SolarPlantImpl(props);
                    //plants.add(sP);
                    SolarPlantManagerImpl.this.addChild(sP);
                    }


                    if (!this.containsPositions) {

                        try {
                            this.writeToFile(vals);
                        } finally {
                        }


                    }

                } else {
                    if (!vals.isEmpty()) {

                        System.out.println("Invalid entry in file for plant " + vals.get(0));
                    }

                }

                progress++;
                this.getProgressHandle().progress(progress);


            }

            //close the file
            bufRdr.close();

            if (!this.containsPositions) {
                this.writer.close();
                String newFilePath = f.getPath().replace(".csv", "");
                newFilePath = newFilePath + "_with_lat_lon.csv";
                fU.copyFile(fU.fromFile(this.tempDataFile), fU.fromFile(new File(newFilePath)));

            }

        }

        private String generateAddressString(List<String> searchResults) {
            Iterator<String> iterator2 = searchResults.iterator();
            String addressString = "";
            while (iterator2.hasNext()) {
                String valString = iterator2.next();
                if (valString != null) {
                    addressString += valString;
                    addressString += " ";
                }

            }

            return addressString;

        }

        private Map<String, Object> interpret(List<String> headings, List<String> vals) {

            Map<String, Object> props = new FastMap<String, Object>();
            Iterator<String> iterator = headings.iterator();
            Iterator<String> iterator1 = vals.iterator();
            List<String> addSearchRes = new FastList<String>(addressSearchParams.size());


            for (int i = 0; i < addressSearchParams.size(); i++) {
                addSearchRes.add("");
            }

            while (iterator.hasNext() && iterator1.hasNext()) {

                String heading = iterator.next();
                String val = iterator1.next();

                Iterator<String> iterator2 = addressSearchParams.iterator();
                int ix = 0;
                while (iterator2.hasNext()) {
                    String searchParam = iterator2.next();
                    if (heading.toLowerCase().contains(searchParam)) {
                        addSearchRes.set(ix, val);
                    }

                    ix++;
                }

                props.put(heading, val);

                if (heading.toLowerCase().contains("commissioning") && !val.isEmpty()) {
                    try{
                    String hVal = heading.toLowerCase();
                    if (hVal.contains("year")) {
                        this.comCalendar.set(Calendar.YEAR, Integer.valueOf(val));
                    } else if (hVal.contains("month")) {
                        this.comCalendar.set(Calendar.MONTH, Integer.valueOf(val));
                    } else if (hVal.contains("day")) {
                        this.comCalendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(val));
                    }
                    props.remove(heading);
                    }catch(NumberFormatException nFE){
                        
                        
                    }
                    
                    
                }
            }



            //Get lat-lon coordinates

            latString = "" + pos.getLatitude().degrees;
            lonString = "" + pos.getLongitude().degrees;


            if (this.containsPositions) {

                latString = vals.get(latIndex);
                lonString = vals.get(lonIndex);
                if (!latString.isEmpty() && !lonString.isEmpty()) {
                    pos = Position.fromDegrees(Double.valueOf(latString), Double.valueOf(lonString));
                }

            } else {



                List<String> searchTerms = Lists.newArrayList(addSearchRes);
                searchTerms.add("");
                while (pos.equals(Position.ZERO) && searchTerms.size() > 1) {
                    searchTerms.remove(searchTerms.size()-1);
                    String addressString = this.generateAddressString(searchTerms);
                    try {
                        pos = lS.getPositionForAddress(addressString);
                    } catch (PositionNotFoundException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }

                latString = "" + pos.getLatitude().degrees;
                lonString = "" + pos.getLongitude().degrees;



            }



            props.put(WWDJObject.PROP_POSITION, pos);
            props.put(SolarPlant.PROP_COMISSION_DATE, comCalendar.getTime());
            Object obj = props.get("plant");

            if (obj != null && obj instanceof String) {
                this.setDescription("Processing " + obj);
                props.put(PROP_NAME, obj);

            }

            return props;

        }

        @Override
        public void doRun() {
            try {
                this.setName("Importing data from " + f.getPath());
                this.readValuesFromFile(this.f.getPath());
            } catch (FileNotFoundException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        @Override
        public boolean cancel() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    @Override
    public DJNodeObject createObject() {
        File f = new FileChooserBuilder(DEFAULT_BROWSE_DIR).setFileFilter(SolarPlantManager.CSV_FILTER).setTitle("Open data file").showOpenDialog();
        if (f != null) {

            DataImporter dI = new DataImporter(f);
            ex.execute(dI);

        }

        return null;

    }
}
