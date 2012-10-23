/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.task.api;

import com.google.common.collect.Range;
import com.google.common.collect.Ranges;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.dj.domainmodel.api.DJNodeObjectAbstr;
import org.dj.listener.api.ListenerAbstr;
import org.dj.property.api.DJProperty;
import org.dj.property.api.PropertyAbstr;
import org.lblrtm.lblrtmfilewriter.api.DJTAPE5;

/**
 *
 * @author djabry
 */
public class TaskRange extends DJNodeObjectAbstr {

    private final LBLRTMParallelTask t;
    private Range<Integer> r;
    private boolean lock;
    public static final String PROP_START = "start";
    public static final String PROP_END = "end";

    public Range getRange() {
        return r;
    }
    private final PropertyChangeListener valListener = new ListenerAbstr() {

        @Override
        public void propertyChange(PropertyChangeEvent pce) {
            updateRange();
            super.propertyChange(pce);
            
        }
    };

    public double getStart() {
        return (Double) this.getPropertyValue(PROP_START);

    }

    public double getEnd() {
        return (Double) this.getPropertyValue(PROP_END);
    }

    private void updateRange(Double start, Double end) {

        if (!lock) {

            double dv = this.t.getTask().getTAPE5().getDV();

            if (end - dv < start) {

                end = start + dv;
            }

            Range<Long> rng = Ranges.closed(fromRangeVal(start), fromRangeVal(end - dv));
            //t.getRanges().remove(this.r);
            //this.r = rng;
            //t.getRanges().add(rng);


        }

    }

    private void updateRange() {

        this.updateRange(this.getStart(), this.getEnd());

    }

    private Long fromRangeVal(Double val) {

        Double dv = this.t.getTask().getTAPE5().getDV();
        Double out = val / dv;
        return out.longValue();
    }

    private Double valFromRangeVal(Long val) {
        Double dv = this.t.getTask().getTAPE5().getDV();
        return val * dv;
    }

    public TaskRange(LBLRTMParallelTask t, Range<Long> range) {

        //if (!t.getRanges().contains(range)) {
          //  t.getRanges().add(range);
        //}

        this.t = t;
//        this.r = range;
//        this.lock = true;

//        DJProperty<Double> startProp = new PropertyAbstr<Double>(PROP_START, valFromRangeVal(range.lowerEndpoint())) {
//
//            @Override
//            public boolean setValue(Double value) {
//                
//                boolean s = super.setValue(value);
//                
//                if(s&&!lock){
//                    
//                    updateRange(value, getEnd());
//                }
//                
//                return true;
//            }
//        };
//
//        startProp.setDisplayName("Start");
//
//        this.putProperty(startProp);
//       
//
//        DJProperty<Double> endProp = new PropertyAbstr<Double>(PROP_END, valFromRangeVal(range.upperEndpoint()+1L)) {
//
//            @Override
//            public boolean setValue(Double value) {
//                boolean s = super.setValue(value);
//                
//                if(s&&!lock){
//                    
//                    updateRange(getStart(), value);
//                }
//                
//                return true;
//            }
//        };
//
//        endProp.setDisplayName("End");
//
//        this.putProperty(endProp);
//
//        this.t.getTask().getTAPE5().addPropertyChangeListener(DJTAPE5.PROP_DV,valListener);
//        this.lock = false;
//        
//    }
//
////    @Override
////    public int hashCode() {
////        return r.hashCode();
////    }
////
////    @Override
////    public boolean equals(Object o) {
////        if (o instanceof TaskRange) {
////            TaskRange taskRange = (TaskRange) o;
////            return getRange().equals(taskRange.getRange());
////
////        }
////
////        return this.equals(o);
////    }

    }
}
