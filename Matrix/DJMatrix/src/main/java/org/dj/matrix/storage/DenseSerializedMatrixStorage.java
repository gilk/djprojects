/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.matrix.storage;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javolution.util.FastList;
import org.dj.fileutilities.api.FileUtilities;
import org.dj.matrix.RangeIterator;
import org.dj.matrix.api.IndexPool;
import org.dj.matrix.api.MatrixFactory;
import org.dj.matrix.api.MatrixStorageAbstr;
import org.openide.util.Lookup;
import sun.misc.Cleaner;
import sun.nio.ch.DirectBuffer;

/**
 *
 * @author djabry
 */
public class DenseSerializedMatrixStorage extends MatrixStorageAbstr implements Serializable, Closeable {

    //private static final MatrixFactory mF = Lookup.getDefault().lookup(MatrixFactory.class);
    private static final FileUtilities fU = Lookup.getDefault().lookup(FileUtilities.class);
    private final IndexPool iP;
    private double defaultValue = 0.0;
    private boolean diagonal;
    transient private RandomAccessFile raf;
    transient private File f;
    private int minValueIndex = 0;
    private int maxValueIndex = 0;
    private double minValue = defaultValue;
    private double maxValue = defaultValue;
    private static final int MAPPING_SIZE = 1 << 30;
    private static final long serialVersionUID = 7526471155622776147L;
    
    private transient List<MappedByteBuffer> mappings;
    
    
    public File getFile(){
        
        return this.f;
    }
    
    private void initialiseRAF(){
        try {
            //String fileName = iDGen.generateID();
            mappings = new FastList<MappedByteBuffer>();
          
            f = File.createTempFile("Matrix", "Temp",fU.getTempDir());
            //System.out.println("Creating new matrix storage at: "+f.getPath());
            this.raf = new RandomAccessFile(f.getPath(), "rw");
            
            

            try {

                long size = 8L * getNumberOfElements();
                for (long offset = 0; offset < size; offset += MAPPING_SIZE) {
                    long size2 = Math.min(size - offset, MAPPING_SIZE);
                    mappings.add(raf.getChannel().map(FileChannel.MapMode.READ_WRITE, offset, size2));
                }
            } catch (IOException e) {
                try {
                    raf.close();
                    throw e;
                } catch (IOException ex) {
                    Logger.getLogger(DenseSerializedMatrixStorage.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(DenseSerializedMatrixStorage.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
    }

    public DenseSerializedMatrixStorage(int r, int c) {
        super(r, c);
        
        iP = new IndexPoolImpl(r,c);
        this.diagonal = true;

    }

    @Override
    public double getValueAt(int index) {

        if (!this.iP.contains(index)) {

            return this.defaultValue;
        }

        long p = index * 8;
        int mapN = (int) (p / MAPPING_SIZE);
        int offN = (int) (p % MAPPING_SIZE);
        return mappings.get(mapN).getDouble(offN);
    }

    @Override
    public void setValueAt(int index, double value) {
        if (value != defaultValue) {
            if (this.rowFromIndex(index) != this.columnFromIndex(index)) {

                this.diagonal = false;
            }
            
            if(value>maxValue){
                
                maxValue = value;
                maxValueIndex = index;
                
            }
            
            if(value<minValue){
                
                minValue = value;
                minValueIndex = index;
                
            }

            this.iP.put(index);
            long p = index * 8;
            int mapN = (int) (p / MAPPING_SIZE);
            int offN = (int) (p % MAPPING_SIZE);
            mappings.get(mapN).putDouble(offN, value);
        }
    }

    @Override
    public Iterator<Integer> getIndexIterator(boolean allElements) {

        if (allElements) {

            return new RangeIterator(0, this.getNumberOfElements()-1).iterator();
        }


        return iP.allItems();
    }
    
    private void readObject(ObjectInputStream oIS) throws ClassNotFoundException, IOException{
        
        oIS.defaultReadObject();
        initialiseStorage();
        Iterator<Integer> iterator = getIndexIterator(false);
        
        while(iterator.hasNext()){
            Integer index = iterator.next();
            setValueAt(index, oIS.readDouble()); 
        } 
        
    }
    
    private void writeObject(ObjectOutputStream oOS) throws IOException{
        
        oOS.defaultWriteObject();
        Iterator<Integer> iterator = getIndexIterator(false);
        while(iterator.hasNext()){
            oOS.writeDouble(getValueAt(iterator.next()));
        }
        
    }

    @Override
    public int getCardinality() {
        return this.iP.getCardinality();
    }

    @Override
    public boolean isDiagonal() {
        return this.diagonal;
    }

    private void clean(MappedByteBuffer mapping) {
        if (mapping == null) {
            return;
        }
        Cleaner cleaner = ((DirectBuffer) mapping).cleaner();
        if (cleaner != null) {
            cleaner.clean();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
        
        
    }
    


    

    @Override
    public void close() throws IOException {
        //System.out.println("Clearing temporary file "+f.getPath());
        for (MappedByteBuffer mapping : mappings) {
            clean(mapping);
        }
        
        try{
            raf.close();
        }finally{
            if(f.exists()){    
                this.f.delete();
            } 
        }

    }

    @Override
    public boolean isSerialized() {
        return true;
    }

    @Override
    public boolean isDense() {
        return true;
    }

    @Override
    public void initialiseStorage() {
        
        initialiseRAF();
    }


}
