/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.parallel;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javolution.util.FastList;
import org.dj.parallel.api.Splittable;
import org.dj.parallel.api.SplittableTask;
import org.dj.remote.api.HostInfo;

/**
 *
 * @author djabry
 */
public class SplittableTaskQueue implements Bin {

    private final double capacity;
    private final HostInfo hostInfo;
    private List<SplittableTask> subSplittableTasks;
    private static final Comparator<SplittableTask> cmp = new Comparator<SplittableTask>() {

        @Override
        public int compare(SplittableTask t, SplittableTask t1) {
            return Double.compare(t.getStart(),t1.getStart());
        }
    };

    public SplittableTaskQueue(Double capacity, HostInfo hI) {

        this.capacity = capacity;
        this.hostInfo = hI;
        this.subSplittableTasks = new FastList<SplittableTask>();
    }
    

    @Override
    public List<SplittableTask> getSplittables() {
        Collections.sort(subSplittableTasks,cmp);
        return subSplittableTasks;
    }

    @Override
    public void putSplittable(Splittable block) {
        if (block instanceof SplittableTask) {
            SplittableTask subSplittableTask = (SplittableTask) block;
            subSplittableTask.setHostInfo(getHostInfo());
            this.subSplittableTasks.add(subSplittableTask);
        }
    }

    @Override
    public Double getCapacity() {
        return capacity;
    }

    private double getTotalSizeOfSplittables() {

        double size = 0.0;



        Iterator<SplittableTask> iterator = this.subSplittableTasks.iterator();
        while (iterator.hasNext()) {
            SplittableTask next = iterator.next();
            size += next.getEnd()-next.getStart();
        }



        return size;
    }

    @Override
    public Double getRemainingCapacity() {
        return capacity - getTotalSizeOfSplittables();
    }

    /**
     * @return the hostInfo
     */
    public HostInfo getHostInfo() {
        return hostInfo;
    }
}
