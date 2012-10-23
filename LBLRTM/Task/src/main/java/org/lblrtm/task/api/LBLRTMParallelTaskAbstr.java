/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.task.api;

import com.google.common.collect.Range;
import com.google.common.collect.Ranges;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javolution.util.FastList;
import javolution.util.FastSet;
import org.dj.fileutilities.api.DJFile;
import org.dj.fileutilities.api.FileUtilities;
import org.dj.index.api.IntegerIndex;
import org.dj.index.api.IntegerIndices;
import org.dj.parallel.api.CRangeImpl;
import org.dj.parallel.api.ContinuousRange;
import org.dj.parallel.api.GCompletedTask;
import org.dj.parallel.api.GParallelTaskAbstr;
import org.lblrtm.file.api.TAPE6;
import org.lblrtm.file.api.TAPE6Manager;
import org.lblrtm.host.api.LBLRTMHostInfo;
import org.lblrtm.lblrtmfilereader.api.LBLRTMFileReader;
import org.lblrtm.task.CompletedTaskImpl;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author djabry
 */
public class LBLRTMParallelTaskAbstr extends GParallelTaskAbstr<LBLRTMTask, LBLRTMHostInfo, TaskResult> implements LBLRTMParallelTask {

    private static final FileUtilities fU = Lookup.getDefault().lookup(FileUtilities.class);
    private static final List<String> OLD_FILE_NAMES = Arrays.asList(new String[]{"TAPE6", "TAPE5", RESULT_FILE_NAME});
    private GCompletedTask<LBLRTMTask, LBLRTMHostInfo, TaskResult> res;
    private final Set<ContinuousRange> taskRanges = new TreeSet<ContinuousRange>(rComp);
    private final IntegerIndices iI = Lookup.getDefault().lookup(IntegerIndices.class);
    private static final LBLRTMFileReader reader = Lookup.getDefault().lookup(LBLRTMFileReader.class);
    private static final TAPE6Manager mgr = Lookup.getDefault().lookup(TAPE6Manager.class);
    private static final Comparator<ContinuousRange> rComp = new Comparator<ContinuousRange>() {
        @Override
        public int compare(ContinuousRange t, ContinuousRange t1) {
            return ((Double) t.getStart()).compareTo((Double) t1.getStart());
        }
    };

    public LBLRTMParallelTaskAbstr(LBLRTMTask task, Set<LBLRTMHostInfo> hIs) {
        super(task, hIs);


    }

    //Move files to specified directory
    @Override
    public GCompletedTask<LBLRTMTask, LBLRTMHostInfo, TaskResult> getCompletedTask() {
        
        if (res == null) {

            //Move all files in temp directories to local directory
            GCompletedTask<LBLRTMTask, LBLRTMHostInfo, TaskResult> completedTask = super.getCompletedTask();
            Iterator<File> iterator = completedTask.getResult().getFiles().iterator();
            String localDirPath = this.getTask().getLocalDir().getPath();
            Set<File> outputFiles = new FastSet<File>();
            while (iterator.hasNext()) {
                File f = iterator.next();
                DJFile fIn = fU.fromFile(f);

                String ds = "";

                if (f.getParentFile().getName().equals("AJ")) {
                    ds = "AJ" + File.separator;
                }

                DJFile fOut = fU.fromFile(new File(localDirPath + File.separator + ds + f.getName()));


                if (!fOut.getFile().getParentFile().exists()) {
                    fOut.getFile().getParentFile().mkdirs();
                }
                try {
                    fU.copyFile(fIn, fOut);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
                outputFiles.add(fOut.getFile());
            }

            Set<File> files = completedTask.getResult().getFiles();
            files.clear();
            files.addAll(outputFiles);
            res = completedTask;
        }

        return res;
    }

    public LBLRTMParallelTaskAbstr(LBLRTMTask task, IntegerIndex ranges, Set<LBLRTMHostInfo> hI) {

        super(task, ranges, hI);
        pullRanges();


    }

    @Override
    public void doRun() {

        pushRanges();
        removeOldFiles();
        super.doRun();


        DJFile t6 = this.localFileFromName("TAPE6");
        DJFile resultFile = this.localFileFromName(RESULT_FILE_NAME);
        Runnable fileConverter = reader.getFileConverter(t6.getFile(), resultFile.getFile());
        fileConverter.run();



    }

    private void removeOldFiles() {

        res = null;
        for (String oldFileName : OLD_FILE_NAMES) {
            DJFile oF = this.localFileFromName(oldFileName);
            if (fU.exists(oF)) {
                if (oF.getFile().getName().equals(TAPE6.TAPE6_NAME)) {

                    mgr.closeFile(oF.getFile());
                }

                fU.delete(oF);
            }



        }
    }

    private DJFile localFileFromName(String name) {

        return fU.fromFile(new File(this.getTask().getLocalDir().getPath() + File.separator + name));

    }

    @Override
    public LBLRTMParallelTask duplicate() {
        return duplicate(false);
    }

    @Override
    public Set<File> getOutputFiles() {
        return getCompletedTask().getResult().getFiles();

    }

    @Override
    public LBLRTMParallelTask duplicate(boolean linkedProfile) {
        this.pushRanges();
        LBLRTMParallelTask t = new LBLRTMParallelTaskAbstr(this.getTask().duplicate(linkedProfile), getRanges().copy(), new FastSet<LBLRTMHostInfo>(this.getHosts()));
        return t;
    }

    @Override
    public GCompletedTask<LBLRTMTask, LBLRTMHostInfo, TaskResult> generateCompletedTaskFrom(LBLRTMTask tsk, Integer duration) {
        try {
            return new CompletedTaskImpl(tsk, duration);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        return null;
    }

    @Override
    public void close() throws IOException {
        if (this.getTask() != null) {

            this.getTask().close();
        }

        if (this.getCompletedTasks() != null) {
            Iterator<GCompletedTask<LBLRTMTask, LBLRTMHostInfo, TaskResult>> iterator = this.getCompletedTasks().iterator();
            while (iterator.hasNext()) {
                GCompletedTask<LBLRTMTask, LBLRTMHostInfo, TaskResult> next = iterator.next();
                LBLRTMTask originalTask = next.getOriginalTask();
                if (originalTask instanceof Closeable) {

                    originalTask.close();
                }

            }

        }
    }

    @Override
    public Set<ContinuousRange> getWavelengthRanges() {
        return this.taskRanges;
    }

    public void pullRanges() {
        this.taskRanges.clear();
        Iterator<Range<Integer>> allRanges = this.getRanges().allRanges();
        double dv = this.getTask().getTAPE5().getDV();
        while (allRanges.hasNext()) {
            Range<Integer> next = allRanges.next();
            double start = next.lowerEndpoint() * dv;
            double end = (next.upperEndpoint() + 1) * dv;
            this.taskRanges.add(new CRangeImpl(start, end));

        }
    }

    private void pushRanges() {
        Iterator<ContinuousRange> iterator = this.taskRanges.iterator();
        List<Range<Integer>> ranges = new FastList<Range<Integer>>();
        double dv = this.getTask().getTAPE5().getDV();
        while (iterator.hasNext()) {
            ContinuousRange next = iterator.next();
            int start = ((Double) (next.getStart() / dv)).intValue();
            int end = ((Double) (next.getEnd() / dv)).intValue() - 1;
            ranges.add(Ranges.closed(start, end));

        }

        this.setRanges(iI.fromRanges(ranges));


    }
}
