/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quorum.vm.implementation;

import java.util.HashMap;
import java.util.Stack;
import org.quorum.symbols.TypeDescriptor;

/**
 * A helper class to encapsulate the standard operations stacks must deal
 * with when pushing and popping from methods, like tracking the max 
 * stack size, handling constants and variables, or other issues.
 * 
 * @author Andreas Stefik
 */
public class BytecodeStack {
    private Stack<BytecodeStackValue> constants = new Stack<BytecodeStackValue>();
    private Stack<LabelStackValue> labels = new Stack<LabelStackValue>();
    private HashMap<Integer, BytecodeStackValue> variables = new HashMap<Integer, BytecodeStackValue>();
    private int maxSize = 0;
    private int currentSize = 0;
    
    
    /**
     * This method pushes constants on the stack.
     * 
     * @param value 
     */
    public void pushConstant(BytecodeStackValue value) {
        constants.push(value);
        currentSize += value.getSize();
        if(currentSize > maxSize) {
            maxSize = currentSize;
        }
    }
    
    
    /**
     * This method pops constants off the stack.
     * @return 
     */
    public BytecodeStackValue popConstant() {
        BytecodeStackValue pop = constants.pop();
        currentSize -= pop.getSize();
        return pop;
    }
    
    /**
     * This method returns a constant value, zero-indexed from the top
     * of the stack. So, for example, if there are three values on the 
     * top of the stack and you want them in the order they were pushed on, 
     * you would call getConstantFromTop(-2), getConstantFromTop(-1), and 
     * getConstantFromTop(0).
     * 
     * @param location
     * @return 
     */
    public BytecodeStackValue getConstantFromTop(int location) {
        return constants.get(constants.size() - 1 - location);
    }
    
    public void pushLabel(LabelStackValue value) {
        labels.push(value);
    }
    
    
    public LabelStackValue popLabel() {
        return labels.pop();
    }
    
    public LabelStackValue peekLabel() {
        return labels.peek();
    }
    
    /**
     * Pushes a value for a particular variable onto a hash map that can
     * later be queried.
     * 
     * @param location
     * @param value 
     */
    public void setVariable(int location, BytecodeStackValue value) {
        variables.put(location, value);
    }
    
    /**
     * Pops a value off of a hash map that stores the value
     * of variables.
     * 
     * @param location
     * @return 
     */
    public BytecodeStackValue removeVariable(int location) {
        return variables.remove(location);
    }
    
    
    /**
     * Returns a value from a hash map storing variable values.
     * 
     * @param location
     * @return 
     */
    public BytecodeStackValue getVariable(int location) {
        return variables.get(location);
    }
    
    /**
     * This method tells the stack to start a new method definition.
     * Specifically, this method clears out the internal constants/variables
     * stacks and resets the max size value for a new method.
     * 
     */
    public void startMethod() {
        maxSize = 0;
        currentSize = 0;
        constants.empty();
        variables.clear();
    }
    
    /**
     * This method allows you to temporarily increase and decrease the 
     * constant stack size, possibly increasing the max stack size, 
     * and decreasing it back again. This is useful if you need to push
     * something on the stack, but do not need to actually push the 
     * value onto the stack.
     * 
     * @param type 
     */
    public void implicitStackIncrease(TypeDescriptor type) {
        int size = BytecodeStackValue.getSize(type);
        this.currentSize += size;
        if(currentSize > maxSize) {
            maxSize = currentSize;
        }
        currentSize -= size;
    }
    
    
    /**
     * Returns the maximum size that this particular method has 
     * achieved while processing push and pop values.
     * 
     * @return 
     */
    public int getMaxSize() {
        return maxSize;
    }
    
    /**
     * Returns the current size of the stack.
     * 
     * @return 
     */
    public int getCurrentSize() {
        return currentSize;
    }
}
