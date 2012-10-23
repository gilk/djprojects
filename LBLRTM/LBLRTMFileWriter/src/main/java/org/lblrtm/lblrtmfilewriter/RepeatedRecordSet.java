/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter;


import org.lblrtm.lblrtmfilewriter.api.RecordContainer;
import java.util.Iterator;
import java.util.List;
import javolution.util.FastComparator;
import javolution.util.FastList;
import javolution.util.FastMap;
import org.dj.domainmodel.api.DJNodeObject;
import org.lblrtm.lblrtmfilewriter.api.Field;
import org.lblrtm.lblrtmfilewriter.api.Record;

/**
 *
 * @author djabry
 */
public abstract class RepeatedRecordSet extends RecordAbstr implements RecordContainer {

    private FastMap<String, Record> recordCache;
    private boolean cacheComplete;


    public abstract void initRecords();

    public RepeatedRecordSet(String name, RecordContainer parent) {
        super(name);
        parent.addChild(this);
        this.recordCache = new FastMap<String, Record>();
        this.cacheComplete = false;
        initRecords();
        this.setCacheComplete(true);

    }
    
    public RepeatedRecordSet(String name){        
        super(name);
        this.recordCache = new FastMap<String, Record>();
        this.cacheComplete = false;
        initRecords();
        this.setCacheComplete(true);
    }

    public void setCacheComplete(boolean tf) {
        //fillCache();
        this.cacheComplete = tf;
    }

    @Override
    public void addChild(DJNodeObject obj) {
        super.addChild(obj);
        if (obj instanceof Record) {
            Record record = (Record) obj;
            if (!(record instanceof RepeatedRecordSet)) {
                recordCache.put(record.getName(), record);
            }
        }
    }

    //Recursive method to extract all records
    public void extractRecords(RepeatedRecordSet rSet, List<Record> allRecords) {
        Iterator<Record> iterator = rSet.getRecords().iterator();
        while (iterator.hasNext()) {
            Record next = iterator.next();
            if (next instanceof RepeatedRecordSet) {
                RepeatedRecordSet repeatedRecordSet = (RepeatedRecordSet) next;
                extractRecords(repeatedRecordSet, allRecords);
            } else {
                allRecords.add(next);
            }
        }
    }

    private void fillCache() {

        for (int i = 0; i < getNumberOfRepetitions(); i++) {
            for (Record r : getChildRecords()) {
                if (r instanceof RepeatedRecordSet) {
                    //RepeatedRecordSet repeatedRecordSet = (RepeatedRecordSet) r;
                    //extractRecords(repeatedRecordSet, allRecords);
                } else {

                    r.setPropertyValue(PROP_LEVEL, i);
                    Record rOut = null;
                    if (r.isValid()) {
                        rOut = new RecordAbstr(r.getName()) {

                            @Override
                            public boolean isValid() {
                                return true;
                            }
                        };

                    } else {

                        rOut = new RecordAbstr(r.getName()) {

                            @Override
                            public boolean isValid() {
                                return false;
                            }
                        };
                    }
                    
                    for (Field f : r.getFields()) {

                        f.setPropertyValue(PROP_LEVEL, i);
                        Field fOut = new FieldImpl(f.getName(),
                                f.getFormat(),
                                f.getSpaces(),
                                f.getWidth(),
                                f.getValue(),
                                f.getText(),
                                f.isDefault(),
                                rOut);
                    }
                    recordCache.put(rOut.getName(), rOut);
                }
            }
        }

    }

    @Override
    public List<Record> getRecords() {

        FastList<Record> allRecords = new FastList<Record>();
        allRecords.addAll(recordCache.values());
        //Only return child records if record cache is complete

        if (this.cacheComplete) {

            Iterator<DJNodeObject> iterator = getChildren().iterator();

            while (iterator.hasNext()) {
                DJNodeObject next = iterator.next();
                if (next instanceof Record) {
                    Record record = (Record) next;
                    if (record instanceof RepeatedRecordSet) {
                        RepeatedRecordSet repeatedRecordSet = (RepeatedRecordSet) record;
                        extractRecords(repeatedRecordSet, allRecords);
                    }
                }
            }
        }
        
        return allRecords;
    }
    
    public List<Record> getAllRecords(){
        
        FastList<Record> allRecords = new FastList<Record>();
        
        for (int i = 0; i < getNumberOfRepetitions(); i++) {
            for (Record r : getChildRecords()) {
                if (r instanceof RepeatedRecordSet) {
                    
                    RepeatedRecordSet repeatedRecordSet = (RepeatedRecordSet) r;
                    allRecords.addAll(repeatedRecordSet.getAllRecords());
                    
                    //extractRecords(repeatedRecordSet, allRecords);
                } else {

                    r.setPropertyValue(PROP_LEVEL, i);
                    Record rOut = null;
                    if (r.isValid()) {
                        rOut = new RecordAbstr(r.getName()) {

                            @Override
                            public boolean isValid() {
                                return true;
                            }
                        };

                    } else {

                        rOut = new RecordAbstr(r.getName()) {

                            @Override
                            public boolean isValid() {
                                return false;
                            }
                        };
                    }
                    
                    for (Field f : r.getFields()) {

                        f.setPropertyValue(PROP_LEVEL, i);
                        Field fOut = new FieldImpl(f.getName(),
                                f.getFormat(),
                                f.getSpaces(),
                                f.getWidth(),
                                f.getValue(),
                                f.getText(),
                                f.isDefault(),
                                rOut);
                    }
                    
                    allRecords.add(rOut);
                }
            }
        }
        
        return allRecords;
        
        
    }

    public int getNumberOfRepetitions(){
        Integer n = getParentTAPE5().getRunTemplate().getRecordRepetitionSpecifier().getNumberOfRepetitionsForRecord(this);
        
        if(n!=null){
            
            return n;
        }
        
        return 1;
    }

    public Object getValue(String recordName, int level, String fieldName) {
        Field field = getRecord(recordName).getField(fieldName);
        field.setPropertyValue(PROP_LEVEL, level);
        return field.getValue();
    }

    public Record getRecord(String name) {

        Record r = recordCache.get(name);
        if (r == null) {
            Iterator<Record> iterator = getChildRecords().iterator();
            while (iterator.hasNext() && r == null) {
                Record childR = iterator.next();
                //Only search through constant records
                if (childR instanceof RepeatedRecordSet) {
                    RepeatedRecordSet repeatedRecordSet = (RepeatedRecordSet) childR;
                    r = repeatedRecordSet.getRecord(name);
                }
            }
        }

        return r;
    }

    List<Record> getChildRecords() {

        List<Record> records = new FastList<Record>();
        Iterator<DJNodeObject> iterator = getChildren().iterator();

        while (iterator.hasNext()) {
            DJNodeObject next = iterator.next();
            if (next instanceof Record) {
                Record record = (Record) next;
                records.add(record);
            }
        }

        return records;
    }

    @Override
    public String getRecordString() {

        String s = "";
        Iterator<Record> iterator = getRecords().iterator();

        while (iterator.hasNext()) {

            Record r = iterator.next();
            s += r.getRecordString();

            //Only add a new line character when there is a record after the current one
            if (iterator.hasNext()) {
                s += "\n";
            }
        }

        return s;

    }
}
