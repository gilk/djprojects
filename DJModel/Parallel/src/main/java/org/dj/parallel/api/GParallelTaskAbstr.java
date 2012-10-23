/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.parallel.api;

import com.google.common.collect.Iterables;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javolution.context.ConcurrentContext;
import javolution.util.FastList;
import javolution.util.FastMap;
import javolution.util.FastSet;
import org.dj.executor.api.DJRunnableAbstr;
import org.dj.index.api.IntegerIndex;
import org.dj.index.api.IntegerIndices;
import org.dj.parallel.*;
import org.dj.remote.api.HostInfo;
import org.openide.util.Lookup;

/**
 *
 * @author djabry
 */
public abstract class GParallelTaskAbstr<T extends GSplittableTask<H>, H extends HostInfo, R extends Result<R>> extends DJRunnableAbstr implements GParallelTask<T, H, R> {

    private final T tsk;
    private boolean mergeResults;
    private Set<H> hosts;
    private GBins bins;
    private List<Integer> testRanges = new FastList<Integer>();
    //private Set<Range<Integer>> taskRanges;
    private static final IntegerIndices iI= Lookup.getDefault().lookup(IntegerIndices.class);
    private IntegerIndex ranges;
    //private static final RangeOperations rO = Lookup.getDefault().lookup(RangeOperations.class);
    private final Set<GCompletedTask<T, H, R>> completedTasks = new FastSet<GCompletedTask<T, H, R>>();
    private GCompletedTask result;
    private final Map<H, Integer> hostTimes = new FastMap<H, Integer>();

    public GParallelTaskAbstr(T tsk, IntegerIndex ranges, Set<H> hosts) {

        this.tsk = tsk;
        this.ranges = ranges;
        this.hosts = hosts;
        this.mergeResults = true;

    }

    public GParallelTaskAbstr(T tsk, Set<H> hIs) {

        this(tsk, iI.fromRange(tsk.getRange()), hIs);

    }



    protected GParallelTaskAbstr(T tsk, IntegerIndex ranges, Map<H, Integer> hostTimes, boolean mergeResults) {

        this(tsk, ranges, hostTimes.keySet());
        Iterator<Entry<H, Integer>> iterator = hostTimes.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<H, Integer> next = iterator.next();
            this.hostTimes.put(next.getKey(), next.getValue());
        }
        this.mergeResults = mergeResults;
    }
    
    private void restoreOriginalRanges(){
        Iterator<Integer> iterator = this.testRanges.iterator();
        while(iterator.hasNext()){
            
            this.ranges.put(iterator.next());
        }
    }

    @Override
    public void doRun() {
        rankHosts();
        createBins();
        runTasks();
        mergeResults();
        cleanup();
        restoreOriginalRanges();
       
    }

    private void runTasks() {


        Iterator<GBin> iterator = this.bins.getBins().iterator();
        Set<QueueRunner> queues = new FastSet<QueueRunner>();
        
        while (iterator.hasNext()) {
            GBin bin = iterator.next();
            queues.add(new QueueRunner(new TaskQueue(bin, this.getTask())));
        }

        Iterator<QueueRunner> iterator1 = queues.iterator();
        
        try {
            ConcurrentContext.enter();
            
            while (iterator1.hasNext()) {
                QueueRunner r = iterator1.next();
                ConcurrentContext.execute(r);

            }
        } finally {

            ConcurrentContext.exit();
        }

    }


    private void mergeResults() {
        if (mergeResults) {
            GCompletedTask<T, H, R> completedTask = this.getCompletedTask();
        }


    }

    private void cleanup() {
        if (mergeResults) {
            Iterator<GCompletedTask<T, H, R>> iterator = this.getCompletedTasks().iterator();
            while (iterator.hasNext()) {
                iterator.next().getOriginalTask().cleanup();
            }

        }

    }

    @Override
    public GCompletedTask<T, H, R> getCompletedTask() {
        if (this.result == null) {

            result = new ProxyCompletedTask(this.completedTasks);
        }

        return result;
    }

    private class QueueRunner implements Runnable {

        private final TaskQueue q;

        public QueueRunner(TaskQueue<T> q) {

            this.q = q;
        }

        @Override
        public void run() {
            Iterator<T> iterator = q.iterator();

            while (iterator.hasNext()) {
                T t = iterator.next();
                Long tStart = 0L;
                Long tEnd = 0L;
                Integer duration = 0;

                try {
                    tStart = System.currentTimeMillis();
                    t.run();


                } finally {

                    tEnd = System.currentTimeMillis();
                    
                    Long d = tEnd-tStart;
                    
                    if(d>Integer.MAX_VALUE){
                        
                        throw new IllegalArgumentException("Task is too long");
                    }
                    
                    duration = d.intValue();
                    completedTasks.add(generateCompletedTaskFrom(t, duration));
                }

               
            }

        }
    }

    public abstract GCompletedTask<T, H, R> generateCompletedTaskFrom(T tsk, Integer duration);

    private void createBins() {

        Integer totalTaskLength = getRanges().getCardinality();

        double ssb = 0.0;
        Iterator<Integer> iterator = this.hostTimes.values().iterator();
        while (iterator.hasNext()) {
            Integer t = iterator.next();
            ssb += 1.0 / t;
        }

        Map<H, Integer> binSpec = new FastMap<H, Integer>();

        Iterator<H> iterator1 = this.getHosts().iterator();
        while (iterator1.hasNext()) {
            H hI = iterator1.next();
            Integer t = hostTimes.get(hI);
            Double lB = totalTaskLength / (t * ssb);
            binSpec.put(hI, lB.intValue());
        }

        updateBinSpec(binSpec, totalTaskLength);
        this.bins = new GBinsImpl(binSpec);
        this.bins.putRanges(ranges);

    }

    //Make sure number of task portions is conserved
    private void updateBinSpec(Map<H, Integer> binSpec, Integer totalLength) {

        Iterator<Integer> iterator = binSpec.values().iterator();
        Integer sum = 0;
        while (iterator.hasNext()) {
            sum += iterator.next();
        }

        Integer diff = totalLength - sum;

        if (diff != 0L) {

            //Increment is either + or -1
            Integer inc = diff / Math.abs(diff);
            Iterator<Entry<H, Integer>> iterator1 = Iterables.cycle(binSpec.entrySet()).iterator();

            for (long i = 0L; i < Math.abs(diff); i++) {
                Entry<H, Integer> next = iterator1.next();
                binSpec.put(next.getKey(), next.getValue() + inc);
            }
        }
    }

    private void rankHosts() {
        
        //Remove previously completed tasks
        this.completedTasks.clear();
        this.testRanges.clear();
        this.result = null;

        if (!hostTimes.isEmpty()) {
            if(hostTimes.keySet().containsAll(hosts)){
                
                return;
            }
        } else if(this.getHosts().size()==1){
            hostTimes.put(getHosts().iterator().next(), 1000);
            return;
        }
        
        
        Iterator<H> hIterator = getHosts().iterator();
        Iterator<Integer> allItems = this.getRanges().allItems();
        
        while(hIterator.hasNext()&&allItems.hasNext()){
            hIterator.next();
            testRanges.add(allItems.next());
            allItems.remove();
        }
        
        IntegerIndex testR = iI.empty(testRanges.get(0), testRanges.get(testRanges.size()-1));
        Iterator<Integer> tIt = testRanges.iterator();
        while(tIt.hasNext()){
            
            testR.put(tIt.next());
        }

        GParallelTaskAbstr t = new HostRanker(this,testR);
        t.setName("Testing performance of hosts...");
        try {

            t.run();
        } finally {
            Iterator<GCompletedTask<T, H, R>> iterator3 = t.getCompletedTasks().iterator();

            while (iterator3.hasNext()) {
                GCompletedTask<T, H, R> next = iterator3.next();
                this.getCompletedTasks().add(next);
                int timeTaken = next.getTimeTaken();
                H hI = next.getOriginalTask().getHostInfo();
                this.hostTimes.put(hI, timeTaken);
            }

        }

    }

    @Override
    public boolean cancel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<H> getHosts() {
        return this.hosts;
    }

    @Override
    public T getTask() {
        return this.tsk;
    }

    @Override
    public IntegerIndex getRanges() {
        return ranges;
    }

    //@Override
    public Set<GCompletedTask<T, H, R>> getCompletedTasks() {
        return this.completedTasks;
    }

    @Override
    public void setRanges(IntegerIndex ranges) {
        this.ranges=ranges;
    }


    


}
