/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quorum.vm.implementation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import org.quorum.execution.Linker;
import org.quorum.steps.ClassExecution;
import org.quorum.steps.ContainerExecution;
import org.quorum.steps.IntermediateExecutionBuilder;
import org.quorum.vm.interfaces.CodeGenerator;

/**
 *
 * @author Andreas Stefik
 */
public class QuorumBytecodeGenerator implements CodeGenerator {
    
    private Linker linker;
    private IntermediateExecutionBuilder builder;
    private HashMap<String, QuorumBytecode> classHash = new HashMap<String, QuorumBytecode>();
    private File buildFolder;
            
    /**
     * This method generates java bytecode for all classes on the system.
     * 
     */
    @Override
    public void generate() {
        Iterator<ContainerExecution> containers = builder.getContainers();
        while (containers.hasNext()) {
            ContainerExecution exe = containers.next();
            generate(exe);
        }
    }
    
    private void generate(ContainerExecution container) {
        Iterator<ClassExecution> classes = container.getClasses();
        while (classes.hasNext()) {
            ClassExecution clazz = classes.next();
            QuorumBytecode code = generate(clazz);
            classHash.put(code.getStaticKey(), code);
        }
    }
    
    private QuorumBytecode generate(ClassExecution clazz) {
        QuorumBytecode code = new QuorumBytecode();
        code.setStaticKey(clazz.getStaticKey());
        QuorumJavaBytecodeStepVisitor visitor = new QuorumJavaBytecodeStepVisitor();
        visitor.visit(clazz);
        ClassWriter classWriter = visitor.getClassWriter();
        
        //get the final bytecode and hash it away.
        byte[] b = classWriter.toByteArray();
        code.setOutput(b);
        return code;
    }

    /**
     * This method flushes all of the bytecode arrays for the generated
     * bytecode to disk.
     */
    @Override
    public void writeToDisk() throws IOException {
        if(classHash == null) {
            return;
        }
        
        if(buildFolder == null || !buildFolder.isDirectory()) {
            throw new FileNotFoundException("Cannot write to a build folder that does not exist.");
        }
        
        Iterator<QuorumBytecode> iterator = classHash.values().iterator();
        while(iterator.hasNext()) {
            QuorumBytecode code = iterator.next();
            byte[] bites = code.getOutput();
            File location = prepareFolder(code);
            writeBytes(location, bites);
        }
    }
    
    private File prepareFolder(QuorumBytecode code) throws IOException {
        File file;
        String path = QuorumConverter.convertStaticKeyToBytecodePath(code.getStaticKey());
        String fullPath = buildFolder + "/" + path + ".class";
        String[] split = fullPath.split("/");
        
        String valueWithoutName = "";        
        for(int i = 0; i < split.length - 1; i++) {
            valueWithoutName += split[i] + "/";
        }
        valueWithoutName = valueWithoutName.substring(0, valueWithoutName.length() - 1);
        
        File dirs = new File(valueWithoutName);
        boolean mkdirs = dirs.mkdirs();
        if(mkdirs) {
            int a = 4;
        }
        
        file = new File(fullPath);
        if(file.isFile()) {
            //delete it and remake it
            file.delete();
            file.createNewFile();
        }
        return file;
    }
    
    
    
    private void writeBytes(File file, byte[] bites) throws FileNotFoundException, IOException {
        FileOutputStream stream = new FileOutputStream(file);
        stream.write(bites);
        stream.flush();
        stream.close();        
    }
    
    
    /**
     * This method cleans out the hash of generated bytecode.
     */
    public void clean() {
        linker = null;
        builder = null;
        classHash = new HashMap<String, QuorumBytecode>();
    }
    
    
    
    
    /**
     * @return the linker
     */
    public Linker getLinker() {
        return linker;
    }

    /**
     * @param linker the linker to set
     */
    public void setLinker(Linker linker) {
        this.linker = linker;
    }

    /**
     * @return the builder
     */
    public IntermediateExecutionBuilder getBuilder() {
        return builder;
    }

    /**
     * @param builder the builder to set
     */
    public void setBuilder(IntermediateExecutionBuilder builder) {
        this.builder = builder;
    }

    /**
     * @return the buildFolder
     */
    public File getBuildFolder() {
        return buildFolder;
    }

    /**
     * @param buildFolder the buildFolder to set
     */
    public void setBuildFolder(File buildFolder) {
        this.buildFolder = buildFolder;
    }    
}
