/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.task.api;

import com.google.common.collect.Range;
import com.google.common.collect.Ranges;
import java.io.*;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javolution.util.FastSet;
import org.dj.executor.api.AssignedCommand;
import org.dj.executor.api.DJRunnableAbstr;
import org.dj.executor.api.Executor;
import org.dj.fileutilities.api.DJFile;
import org.dj.fileutilities.api.FileUtilities;
import org.dj.parallel.api.GSplittableTask;
import org.lblrtm.file.api.TAPE6;
import org.lblrtm.file.api.TAPE6Manager;
import org.lblrtm.host.api.LBLRTMHostInfo;
import org.lblrtm.lblrtmdata.api.LBLRTMOutputFile;
import org.lblrtm.lblrtmfilereader.api.FileReaderProperties;
import org.lblrtm.lblrtmfilereader.api.LBLRTMFileReader;
import org.lblrtm.lblrtmfilewriter.api.CalculationTemplate;
import org.lblrtm.lblrtmfilewriter.api.DJTAPE5;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author djabry
 */
public class LBLRTMTaskAbstr extends DJRunnableAbstr implements LBLRTMTask {

    private final DJTAPE5 t5;
    //private TAPE6 t6;
    private LBLRTMHostInfo hI;
    private boolean lockedByCurrentProc;
    //private static final FileMerger fM = Lookup.getDefault().lookup(FileMerger.class);
    private static final FileUtilities fU = Lookup.getDefault().lookup(FileUtilities.class);
    private static final Executor ex = Lookup.getDefault().lookup(Executor.class);
    private File localDir;
    private Set<File> taskOutputFiles;
    private static final LBLRTMFileReader reader = Lookup.getDefault().lookup(LBLRTMFileReader.class);
    private static final FileReaderProperties fRP = Lookup.getDefault().lookup(FileReaderProperties.class);
    private boolean convertFiles;
    private static final TAPE6Manager mgr = Lookup.getDefault().lookup(TAPE6Manager.class);

    public LBLRTMTaskAbstr(DJTAPE5 t5, LBLRTMHostInfo hI, File localDir, boolean convert) {

        this.t5 = t5;
        this.hI = hI;

        convertFiles = convert;



        this.localDir = localDir;
    }

    @Override
    public DJTAPE5 getTAPE5() {

        return this.t5;
    }

    @Override
    public File getLocalDir() {

        if (!localDir.exists()) {

            localDir.mkdirs();
        }

        return localDir;
    }

    public File getLocalTempDir() {

        File locTempDir = new File(fU.getTempDir() + File.separator + this.getId() + File.separator + this.getTempDirName());

        if (!locTempDir.exists()) {

            locTempDir.mkdirs();
        }

        return locTempDir;
    }

    @Override
    public void setLocalDir(File dir) {

        localDir = dir;
    }

    @Override
    public Set<File> getOutputFiles() {

        return taskOutputFiles;
    }

    @Override
    public void setHostInfo(LBLRTMHostInfo hI) {
        this.hI = hI;
    }

    private DJFile createLockFile() throws IOException {

        File lock = new File(fU.getTempDir().getPath() + File.separator + LOCK_FILE_NAME);
        if (!lock.exists()) {

            lock.createNewFile();
            BufferedWriter w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(lock)));
            w.write("Locked on " + Calendar.getInstance().getTime().toString());
            w.flush();
            w.close();

        }

        return fU.fromFile(lock);

    }

    private boolean isLocked() {

        DJFile f = this.remoteFileFromName(LOCK_FILE_NAME);

        if (fU.exists(f)) {

            return true;
        }

        return false;

    }

    private void writeLock() throws IOException {

        DJFile hostWDir = fU.fromFile(hI.getWorkingDir(), hI);

        if (!fU.exists(hostWDir)) {

            fU.mkdir(hostWDir);
        }


        this.setName("Waiting for previous process to finish on " + this.getHostInfo().getName() + "...");

        while (this.isLocked()) {

            try {

                delay(1000);
            } finally {
            }
        }
        this.lockedByCurrentProc = true;

        this.setName("Writing lock");

        DJFile fOut = this.remoteFileFromName(LOCK_FILE_NAME);
        DJFile fIn = this.createLockFile();

        fU.copyFile(fIn, fOut);


    }

    public static void delay(long n) {
        long t0, t1;
        t0 = System.currentTimeMillis();
        do {
            t1 = System.currentTimeMillis();
        } while (t1 - t0 < n);
    }

    public void removeLock() {
        if (this.lockedByCurrentProc) {
            DJFile f = this.remoteFileFromName(LOCK_FILE_NAME);
            fU.delete(f);
            this.lockedByCurrentProc = false;

        }

    }

    private void putCalculationTemplate() {

        this.t5.setRunTemplate(t5.getCalculationTemplate());
    }

    private void putScannerTemplate() {

        this.t5.setRunTemplate(t5.getScannerTemplate());
    }

    @Override
    public void doRun() {
        try {

            //Write lock file
            writeLock();

            putCalculationTemplate();

            //Copy TAPE5 to host;
            copyFilesToHost();

            //Link files in working directory on host
            makeLinks();

            //Call LBLRTM
            runLBLRTM();

            //Scan files 
            scanFiles();

            //
            moveFilesToLocalDir();

            //Convert transferred files
            convertOutputFilesToNetCDF();

            //Write TAPE5 and TAPE6 files to local dir, write layer profile
            writeDiagnosticsToLocalDir();

            //deleteRemoteFiles();
            removeLock();

        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

    }



    private void writeDiagnosticsToLocalDir() throws IOException {

        this.setName("Writing diagnostics files...");

        DJFile t5Out = fU.fromFile(new File(getLocalDir().getPath() + File.separator + "TAPE5"));

        if (!fU.exists(t5Out)) {

            this.getTAPE5().writeToFile(t5Out.getFile());

        }

    }

    private String getTempDirName() {


        return t5.getV1() + "-" + t5.getV2();
    }

    private DJFile localFileFromName(String name) {

        return fU.fromFile(new File(getLocalDir().getPath() + File.separator + name));

    }

    private DJFile remoteFileFromName(String name) {

        return fU.fromFile(new File(hI.getWorkingDir().getPath() + this.getSeperatorForHost() + name), hI);
    }

    private void moveFilesToLocalDir() throws IOException {

        String oldDescr = this.getDescription();

        this.setName("Moving files to local directory from " + this.getHostInfo().getName() + "...");

        this.putCalculationTemplate();
        //Copy results files to Local direcory
        List<LBLRTMOutputFile> oFiles = ((CalculationTemplate) t5.getRunTemplate()).getOutputFiles(getTAPE6());
        Iterator<LBLRTMOutputFile> iterator2 = oFiles.iterator();


        while (iterator2.hasNext()) {
            String next = iterator2.next().getName();

            this.setDescription("Moving file " + next);
            DJFile fIn = remoteFileFromName(next);

            File fO = new File(this.getLocalTempDir().getPath() + File.separator + next);
            if (!fO.getParentFile().exists()) {

                fO.getParentFile().mkdirs();
            }
            
            DJFile fOut = fU.fromFile(fO);


            try {
                fU.copyFile(fIn, fOut);
            } finally {

                if (!fIn.getFile().equals(fOut.getFile()) || (!fIn.getHostInfo().equals(fOut.getHostInfo()))) {
                    fU.delete(fIn);
                }
            }

        }

        //Delete other output files
        this.setName("Cleaning up remote directory");
        this.setDescription("");
        Iterator<File> outputFiles = getTAPE6().getOutputFiles();
        while (outputFiles.hasNext()) {
            File next1 = outputFiles.next();
            DJFile rF = this.remoteFileFromName(next1.getName());
            if (fU.exists(rF)) {
                if (!rF.getFile().getParentFile().equals(this.getLocalDir()) || !rF.getHostInfo().isLocal()) {
                    this.setDescription("Deleting remote file " + rF.getFile().getName()+ " on "+this.hI.getName());
                    fU.delete(rF);
                }

            }
        }

        this.setDescription(oldDescr);

    }

    private TAPE6 getTAPE6() throws IOException {


        //Copy TAPE6 file to local dir

        
        DJFile t6Out = this.localFileFromName(TAPE6.TAPE6_NAME);
        DJFile t6In = this.remoteFileFromName(TAPE6.TAPE6_NAME);


        try {
            if (!fU.exists(t6Out)) {

                if (fU.isBeingTransferred(t6Out)) {
                    System.out.println("Waiting for transfer to finish");
                    fU.waitForTransferToFinish(t6Out);

                } else {

                    fU.copyFile(t6In, t6Out);
                }

            }

        } finally {

            TAPE6 t6 = mgr.getTAPE6ForFile(t6Out.getFile());
            return t6;
        }

    }

    private void convertOutputFilesToNetCDF() throws IOException {

        String oldDescr = this.getDescription();

        this.setName("Converting files to NetCDF format...");

        if (this.convertFiles) {

            Iterator<LBLRTMOutputFile> iterator = t5.getCalculationTemplate().getOutputFiles(getTAPE6()).iterator();
            this.taskOutputFiles = new FastSet<File>();

            while (iterator.hasNext()) {
                LBLRTMOutputFile next = iterator.next();
                File fIn = new File(this.getLocalTempDir() + File.separator + next.getName());
                File fOut = new File(fIn.getPath() + ".nc");

                fRP.setRunString(getTAPE5().getRunString());

                if (next.getName().equals("TAPE12")) {
                    //Change to be pressure corresponding to observer
                    fRP.setPressure(t5.getObserver().getAltitude());
                } else {

                    //Change to be pressure corresponding to layer/level
                    fRP.setPressure(0.0);

                }

                //fRP.setPressure()
                if (reader.canRead(fIn)) {

                    this.setDescription("Converting file " + fOut.getName());

                    Runnable fileConverter = reader.getFileConverter(fIn, fOut);
                    fRP.setVariables(next.getVariables());
                    fileConverter.run();
                    fIn.delete();


                } else {
                    fOut = fIn;
                }

                taskOutputFiles.add(fOut);
            }

        }
        this.setDescription(oldDescr);

    }

    private void scanFiles() throws IOException {

        String oldDescr = this.getDescription();

        this.setName("Scanning files on " + this.getHostInfo().getName() + "...");


        TAPE6 t6 = getTAPE6();


        Map<String, String> filesToScan = ((CalculationTemplate) t5.getRunTemplate()).getFilesToScan(t6);

        //Write scanning TAPE5 file
        putScannerTemplate();
        fU.copyFile(t5.getInputStream(), fU.fromFile(new File(hI.getWorkingDir().getPath() + getSeperatorForHost() + "TAPE5"), hI));

        Iterator<String> iterator = filesToScan.keySet().iterator();
        Iterator<String> iterator1 = filesToScan.values().iterator();

        File fLinkIn = new File(hI.getWorkingDir().getPath() + getSeperatorForHost() + "TAPE24");
        File fLinkOut = new File(hI.getWorkingDir().getPath() + getSeperatorForHost() + "TAPE25");

        while (iterator.hasNext() && iterator1.hasNext()) {
            File fIn = new File(hI.getWorkingDir().getPath() + getSeperatorForHost() + iterator.next());
            File fOut = new File(hI.getWorkingDir().getPath() + getSeperatorForHost() + iterator1.next());
            this.setDescription("Scanning " + fOut.getName());

            DJFile fLIn = fU.fromFile(fLinkIn, hI);

            if (fU.exists(fLIn)) {
                fU.delete(fLIn);
            }

            //Rename file to be scanned to TAPE24
            fU.rename(fU.fromFile(fIn, hI), fLIn);

            //Run LBLRTM
            runLBLRTM(this.getName(), this.getDescription());

            //Destructive scanning procedure
            fU.delete(fLIn);
            fU.rename(fU.fromFile(fLinkOut, hI), fU.fromFile(fOut, hI));


        }

        this.setDescription(oldDescr);

    }

    String getSeperatorForHost() {

        if (hI.isLocal()) {

            return File.separator;
        } else {

            return "/";
        }
    }

    private String getRunPrefix() {

        String runPrefix = "";

        if (System.getProperty("os.name").toLowerCase().contains("win") && (hI.isLocal())) {

            runPrefix = "";
        } else {
            runPrefix = "./";
        }

        return runPrefix;

    }

    private void runLBLRTM() {
        String name = "Running LBLRTM on " + hI.getName() + "...";
        String descr = "<html>Performing " + t5.getRunTemplate().getName() + " calculation for range " + this.getTempDirName() + " (cm<sup>-1</sup>)</html>";

        this.runLBLRTM(name, descr);

    }

    private void runLBLRTM(String name, String description) {
        String oldDesc = this.getDescription();
        String oldName = this.getName();
        this.setName(name);
        this.setDescription(description);

        AssignedCommand c = ex.createCommand(getRunPrefix() + hI.getExecutable().getName(), new String[]{}, hI.getWorkingDir(), hI, "EXIT");

        c.run();

        this.setName(oldName);
        this.setDescription(oldDesc);
    }

    private void copyFilesToHost() throws IOException {

        this.setName("Copying files to " + hI.getName() + "...");

        fU.copyFile(t5.duplicate().getInputStream(), fU.fromFile(new File(hI.getWorkingDir().getPath() + getSeperatorForHost() + "TAPE5"), hI));
    }

    private void makeLinks() throws IOException {

        //this.setName("Copying files to host...");

        Set<String> inputFiles = ((CalculationTemplate) t5.getRunTemplate()).getInputFiles();

        //Also make a link to the executable
        inputFiles.add(hI.getExecutable().getName());

        String exDir = hI.getExecutable().getParent();
        String workDir = hI.getWorkingDir().getPath();

        for (String fileName : inputFiles) {
            DJFile source = fU.fromFile(new File(exDir + getSeperatorForHost() + fileName), hI);
            DJFile destination = fU.fromFile(new File(workDir + getSeperatorForHost() + fileName), hI);
            if (fU.exists(source) && !fU.exists(destination)) {
                fU.makeSymbolicLink(source, destination);
            }
        }

        DJFile aJDir = fU.fromFile(new File(hI.getWorkingDir().getPath() + this.getSeperatorForHost() + "AJ"), hI);

        //Create the "AJ" firectory for AJ calculations if it doesn't exist
        if (!fU.exists(aJDir)) {

            fU.mkdir(aJDir);
        }
    }

    @Override
    public GSplittableTask duplicate() {

        return this.duplicate(true);
    }

    @Override
    public LBLRTMHostInfo getHostInfo() {
        return this.hI;
    }

    @Override
    public void setConvertFiles(boolean tf) {
        this.convertFiles = tf;
    }

    @Override
    public boolean isConvertFiles() {
        return this.convertFiles;
    }

    @Override
    public boolean cancel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void moveOutputFileToDir(File outputFile, File dir, boolean overwrite) throws IOException {

        if (this.getOutputFiles().contains(outputFile)) {

            if (!outputFile.getParent().equals(dir.getPath())) {

                File out = new File(dir + File.separator + outputFile.getName());

                if (out.exists()) {

                    if (!outputFile.exists()) {
                        //If the file has already been moved then update the link
                        this.getOutputFiles().remove(outputFile);
                        this.getOutputFiles().add(out);
                        return;

                    }

                    if (!overwrite) {

                        return;
                    }

                    out.delete();
                }

                fU.copyFile(fU.fromFile(outputFile), fU.fromFile(out));
                this.getOutputFiles().remove(outputFile);
                this.getOutputFiles().add(out);
                outputFile.delete();

            }

        }
    }

    @Override
    public void cleanup() {


        fU.delete(fU.fromFile(this.getLocalTempDir()));

    }

    @Override
    public LBLRTMTask duplicate(boolean linkedProfile) {

        LBLRTMTaskAbstr t = new LBLRTMTaskAbstr(((DJTAPE5) getTAPE5()).copy(linkedProfile), getHostInfo(), getLocalDir(), isConvertFiles());

        return t;
    }

    @Override
    public Range<Integer> getRange() {

        DJTAPE5 t5 = this.getTAPE5();
        double v1 = t5.getV1();
        double v2 = t5.getV2();
        double dV = t5.getDV();
        Integer start = ((Double) (v1 / dV)).intValue();
        Integer end = ((Double) (v2 / dV)).intValue() - 1;

        return Ranges.closed(start, end);

    }

    @Override
    public void setRange(Range<Integer> r) {

        Integer start = r.lowerEndpoint();
        Integer end = r.upperEndpoint();

        DJTAPE5 t5 = this.getTAPE5();
        double dv = t5.getDV();

        double v1 = roundThreeDecimals(start * dv);
        double v2 = roundThreeDecimals((end + 1) * dv);


        t5.setV1(v1);
        t5.setV2(v2);


    }

    double roundThreeDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.###");
        return Double.valueOf(twoDForm.format(d));
    }

//    @Override
//    public TaskResult getResult() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
    @Override
    public void close() throws IOException {
        this.removeLock();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.close();
    }
}
