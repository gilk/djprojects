/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.parallel.api;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dj.remote.api.HostInfo;
import org.openide.util.Lookup;

/**
 *
 * @author djabry
 */
@Deprecated
public abstract class SplittableTaskAbstr implements SplittableTask {

    private final SplittableTask task;
    private static final FileMerger fM= Lookup.getDefault().lookup(FileMerger.class);;

    public SplittableTaskAbstr(SplittableTask task) {
        this.task = task;

    }

    @Override
    public SplittableTask duplicate() {
        return (SplittableTask) task.duplicate();
    }

    //@Override
    public void mergeResults(SplittableTask t) {

        if (t instanceof SplittableTaskAbstr) {
            SplittableTaskAbstr sTA = (SplittableTaskAbstr) t;

            Set<File> outputFiles = this.getOutputFiles();
            Set<File> outputFiles2 = sTA.getOutputFiles();

            for (File f1 : outputFiles) {

                for (File f2 : outputFiles2) {
                    try {
                        fM.mergeFiles(f1, f2);
                    } catch (IOException ex) {
                        Logger.getLogger(SplittableTaskAbstr.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }

        } else {

            //this.task.mergeResults(t);
        }


    }

    @Override
    public void setHostInfo(HostInfo hI) {
        this.task.setHostInfo(hI);
    }

    @Override
    public HostInfo getHostInfo() {
        return task.getHostInfo();
    }

    @Override
    public void run() {
        task.run();
    }

    @Override
    public void setStart(double start) {
        task.setStart(start);
    }

    @Override
    public void setEnd(double end) {
        task.setEnd(end);
    }

    @Override
    public double getStart() {
        return task.getStart();
    }

    @Override
    public double getEnd() {
        return task.getEnd();
    }

    @Override
    public double getSize() {
        return task.getEnd();
    }

    @Override
    public void cleanup() {
    }

    private boolean isInRange(double v) {

        if (v >= this.getStart() && v <= this.getEnd()) {

            return true;
        }

        return false;
    }

    @Override
    public boolean intersects(ContinuousRange r) {

        if (isInRange(r.getStart()) || isInRange(r.getEnd())) {

            return true;
        }

        return false;
    }

    @Override
    public boolean contains(ContinuousRange r) {
        if (isInRange(r.getStart()) && isInRange(r.getEnd())) {

            return true;
        }

        return false;
    }
}
