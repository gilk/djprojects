/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.parallel.api;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javolution.context.ConcurrentContext;
import javolution.util.FastList;
import javolution.util.FastMap;
import javolution.util.FastSet;
import org.dj.executor.api.DJRunnableAbstr;
import org.dj.parallel.*;
import org.dj.remote.api.HostInfo;

/**
 *
 * @author djabry
 */
@Deprecated
public class ParallelTaskAbstr extends DJRunnableAbstr implements ParallelTask {

    //private double optimisingRatio;
    private double taskLength;
    //private List<Range> ranges;
    //private Set<HostInfo> hosts;
    private final Map<HostInfo, Long> hostTimes = new FastMap<HostInfo, Long>();
    private final SplittableTask task;
    private final Map<ContinuousRange, Set<SplittableTask>> completedTasks = new FastMap<ContinuousRange, Set<SplittableTask>>();
    private final Map<HostInfo, SplittableTaskQueue> queues = new FastMap<HostInfo, SplittableTaskQueue>();
    private Bins taskBins;
    public static final double TOLERANCE_RATIO = 1e-6;
    public static final double DEFAULT_OPTIMISING_RATIO = 0.01;

    private void rankHosts() {
        
        this.completedTasks.clear();
        for (ContinuousRange rng : this.getRanges()) {
            this.completedTasks.put(rng.duplicate(), new FastSet<SplittableTask>());
        }

        if (this.getHosts().size() == 1) {

            this.hostTimes.put(this.getHosts().iterator().next(), 1000L);
            return;
        }

        String oldName = this.getName();
        this.setName("Checking performance of hosts...");

        //Perform runs of the task split into fractions defined by the optimisingRatio
        double increment = taskLength * getOptimisingRatio();

        Iterator<ContinuousRange> iterator = getRanges().iterator();
        System.out.println("Number of ranges = " + getRanges().size());
        System.out.println("Task length = " + taskLength);
        System.out.println("Increment = " + increment);
        ContinuousRange r = iterator.next();

        getRanges().remove(r);
        //Adapt optimising ratio if it is too large in order to allow first range to be split into
        //nHosts number of pieces
        if (r.getSize() < (getHosts().size() * increment)) {

            increment = r.getSize() / getHosts().size();
            this.setOptimisingRatio(increment / taskLength);

        } else {

            //Add a new range excluding the ranges already calculated
            getRanges().add(new CRangeImpl(r.getStart() + (increment * getHosts().size()), r.getEnd()));
        }



        double start = r.getStart();
        double end = start + increment;

        Iterator<? extends HostInfo> hostIterator = getHosts().iterator();
        Set<SplittableTask> testTasks = new FastSet<SplittableTask>();
        System.out.println("Number of hosts = " + getHosts().size());
        while (hostIterator.hasNext()) {

            HostInfo hI = hostIterator.next();
            SplittableTask t = task.duplicate();
            t.setHostInfo(hI);
            t.setStart(start);
            t.setEnd(end);

            System.out.println("Added new task to test tasks, start = " + start + ", end = " + end);
            testTasks.add(t);
            start += increment;
            end += increment;


        }

        System.out.println("Test tasks length = " + testTasks.size());


        ConcurrentContext.enter();
        try {
            for (SplittableTask t : testTasks) {

                Runnable runnable = new HostRanker(t);
                ConcurrentContext.execute(runnable);
            }

        } finally {

            ConcurrentContext.exit();
            this.setName(oldName);
        }

    }

    @Override
    public Map<ContinuousRange, Set<SplittableTask>> getCompletedTasks() {
        return this.completedTasks;
    }



    private class HostRanker implements Runnable {

        private final SplittableTask t;

        public HostRanker(SplittableTask task) {

            this.t = task;
        }

        @Override
        public void run() {
            rankHost(this.t);
        }
    }

    public double getTaskLength() {
        updateTaskLength();
        return this.taskLength;
    }

    private void rankHost(SplittableTask t) {

        long startTime = System.currentTimeMillis();
        long endTime;
        try {
            t.run();
        } finally {
            endTime = System.currentTimeMillis();
        }

        long duration = endTime - startTime;
        this.hostTimes.put(t.getHostInfo(), duration);
        addToCompleteTasks(t);
        System.out.println("Host " + t.getHostInfo().getServerName() + " time = " + duration);

    }

    private void addToCompleteTasks(SplittableTask t) {
        Iterator<ContinuousRange> iterator = this.completedTasks.keySet().iterator();

        while (iterator.hasNext()) {
            ContinuousRange next = iterator.next();
            if (next.contains(t)) {
                completedTasks.get(next).add(t);
                return;
            }
        }
    }

    public ParallelTaskAbstr(SplittableTask task, Set<? extends HostInfo> hosts, Set<ContinuousRange> ranges, double optimisingRatio) {


        this.setPropertyValue(PROP_OPTIMISING_RATIO, optimisingRatio);
        this.setPropertyValue(PROP_HOSTS, hosts);

        this.task = task;

        this.setPropertyValue(PROP_RANGES, ranges);

        updateTaskLength();
    }

    public ParallelTaskAbstr(SplittableTask task, Set<? extends HostInfo> hosts, Set<ContinuousRange> ranges) {

        this(task, hosts, ranges, DEFAULT_OPTIMISING_RATIO);
    }

    public ParallelTaskAbstr(SplittableTask task, Set<? extends HostInfo> hosts) {

        this(task, hosts, rangesFromTask(task));

    }

    public ParallelTaskAbstr(SplittableTask task, Set<? extends HostInfo> hosts, double optimisingRatio) {

        this(task, hosts, rangesFromTask(task), optimisingRatio);
    }

    public ParallelTaskAbstr(SplittableTask task) {

        this(task, hostsFromTask(task));
    }

    private static Set<ContinuousRange> rangesFromTask(SplittableTask t) {

        Set<ContinuousRange> ranges = new FastSet<ContinuousRange>();
        ranges.add(new CRangeImpl(t.getStart(), t.getEnd()));
        return ranges;
    }

    private static Set<? extends HostInfo> hostsFromTask(SplittableTask t) {

        Set<HostInfo> hosts = new FastSet<HostInfo>();
        hosts.add(t.getHostInfo());
        return hosts;
    }

    private void updateTaskLength() {


        this.taskLength = 0.0;

        for (ContinuousRange r : this.getRanges()) {

            taskLength += r.getSize();
        }
    }

    private void createBins() {

        //Assign bin sizes according to host performance;
        Double totalSize = this.taskLength;
        this.updateTaskLength();
        Double remainingSize = this.taskLength;

        Double scaleLength = 0.0;

        for (HostInfo hI : hostTimes.keySet()) {
            scaleLength += (1.0 / hostTimes.get(hI));
        }

        List<SplittableTaskQueue> taskQueues = new FastList<SplittableTaskQueue>();

        for (HostInfo hI : hostTimes.keySet()) {

            double capacity = (remainingSize / (scaleLength * hostTimes.get(hI)));
            taskQueues.add(new SplittableTaskQueue(capacity, hI));
        }

        taskBins = new BinsImpl(taskQueues, TOLERANCE_RATIO * remainingSize);

    }

    private void generateTasks() {

        SplittablePool sP = new SplittablePoolImpl();

        //Split task into tasks with specified getRanges() and put into the splittable pool
        for (ContinuousRange r : getRanges()) {
            SplittableTask t = (SplittableTask) this.task.duplicate();
            t.setStart(r.getStart());
            t.setEnd(r.getEnd());
            sP.addSplittable(t);
        }

        //Put the splittable pool into the taskBins, the tasks are sorted accordingly
        this.taskBins.putSplittables(sP);

        System.out.println("Task bins size = " + taskBins.getBins().size());
        System.out.println("Task bins contents = ");
        Iterator<Bin> iterator = taskBins.getBins().iterator();

        while (iterator.hasNext()) {

            Bin next = iterator.next();
            Iterator<? extends Splittable> iterator1 = next.getSplittables().iterator();

            while (iterator1.hasNext()) {
                Splittable n = iterator1.next();
                System.out.println("Task start = " + n.getStart() + " end = " + n.getEnd());

            }
        }

    }

    @Override
    public void doRun() {


        updateTaskLength();


        rankHosts();
        createBins();
        generateTasks();
        ConcurrentContext.enter();

        //Execute tasks of each bin in sequence
        try {
            Iterator<Bin> iterator = taskBins.getBins().iterator();
            while (iterator.hasNext()) {
                Bin bin = iterator.next();
                Runnable r = new BinProcessor(bin);
                ConcurrentContext.execute(r);
            }
        } finally {

            ConcurrentContext.exit();
        }


        mergeResults();

        cleanup();
        
        restoreOriginalTaskRanges();
        
        

    }
    
    private void restoreOriginalTaskRanges(){
        
        this.setRanges(this.completedTasks.keySet());

    }

    private void cleanup() {
        
        String oldName = this.getName();
        
        this.setName("Cleaning up...");

        for (Set<SplittableTask> s : completedTasks.values()) {

            for (SplittableTask t : s) {

                t.cleanup();
            }

        }
        
        this.setName(oldName);
    }

    private void mergeResults() {

        ////This method merges tasks within their own initially specified ranges
        String oldName = this.getName();
        this.setName("Merging files...");
        
        SplittableTask t = null;

        Iterator<Set<SplittableTask>> iterator = completedTasks.values().iterator();
        while (iterator.hasNext()) {
            Iterator<SplittableTask> iterator1 = iterator.next().iterator();
            if (iterator1.hasNext()) {
                
                if(t==null){
                    
                    t=iterator1.next();
                }

                while (iterator1.hasNext()) {
                    
                   // t.mergeResults(iterator1.next());
                }
            }

        }
        
        this.setName(oldName);
    }

    private class BinProcessor implements Runnable {

        private final Bin b;

        public BinProcessor(Bin bin) {

            this.b = bin;
        }

        @Override
        public void run() {
            Iterator<? extends Splittable> iterator1 = b.getSplittables().iterator();
            while (iterator1.hasNext()) {
                Splittable next = iterator1.next();
                if (next instanceof SplittableTask) {
                    SplittableTask sT = (SplittableTask) next;
                    try {
                        sT.run();
                    } finally {

                        addToCompleteTasks(sT);
                    }
                }
            }
        }
    }

    @Override
    public boolean cancel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<? extends HostInfo> getHosts() {
        return (Set<? extends HostInfo>) getPropertyValue(PROP_HOSTS);
    }

    @Override
    public SplittableTask getTask() {
        return this.task;
    }

    @Override
    public Set<ContinuousRange> getRanges() {
        return (Set<ContinuousRange>) getPropertyValue(PROP_RANGES);
    }

    @Override
    public double getOptimisingRatio() {
        return (Double) getPropertyValue(PROP_OPTIMISING_RATIO);
    }

    public void setOptimisingRatio(double ratio) {

        this.setPropertyValue(PROP_OPTIMISING_RATIO, ratio);
    }
    
        
    @Override
    public void setRanges(Set<ContinuousRange> ranges) {
        
        Iterator<ContinuousRange> currentRanges = this.getRanges().iterator();
        Iterator<ContinuousRange> newRanges = ranges.iterator();
        
        Set<ContinuousRange> newExcessRanges = new FastSet<ContinuousRange>();
        Set<ContinuousRange> oldExcessRanges = new FastSet<ContinuousRange>();
        
        while(newRanges.hasNext()){
            
            ContinuousRange newRange = newRanges.next();
            
            if(currentRanges.hasNext()){
                ContinuousRange currentRange = currentRanges.next();
                currentRange.setStart(newRange.getStart());
                currentRange.setEnd(newRange.getEnd()); 
            }else{
                newExcessRanges.add(new CRangeImpl(newRange.getStart(),newRange.getEnd()));
                
            }
        }
        
        
        while(currentRanges.hasNext()){
            oldExcessRanges.add(currentRanges.next());
        }
        
        
        getRanges().removeAll(oldExcessRanges);
        getRanges().addAll(newExcessRanges);
        
    }
}
