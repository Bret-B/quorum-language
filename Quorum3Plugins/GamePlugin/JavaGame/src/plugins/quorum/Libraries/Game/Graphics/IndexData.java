/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plugins.quorum.Libraries.Game.Graphics;

import plugins.quorum.Libraries.Game.GameRuntimeError;

/**
 * This class acts as a pseudo-interface. Each method here is meant to be 
 * overridden by the extending class. This class only exists such that the Java
 * plugin implementations of VertexData will respect the inheritance hierarchy
 * that is present in the Quorum implementations of the VertexData classes.
 * 
 * @author alleew
 */
public class IndexData 
{
    public java.lang.Object me_ = null;
    
    /** @return the number of indices currently stored in this buffer */
    public int GetSize()
    {
        throw new GameRuntimeError("This action hasn't been implemented for this class!");
    }

    /** @return the maximum number of indices this IndexData can store. */
    public int GetMaxSize()
    {
        throw new GameRuntimeError("This action hasn't been implemented for this class!");
    }

    /** Binds this IndexBufferObject for rendering with glDrawElements. */
    public void Bind()
    {
        throw new GameRuntimeError("This action hasn't been implemented for this class!");
    }

    /** Unbinds this IndexData. */
    public void Unbind()
    {
        throw new GameRuntimeError("This action hasn't been implemented for this class!");
    }

    /** Invalidates the IndexBufferObject so a new OpenGL buffer handle is created. Use this in case of a context loss. */
    public void Invalidate()
    {
        throw new GameRuntimeError("This action hasn't been implemented for this class!");
    }

    /** Disposes this IndexData and all its associated OpenGL resources. */
    public void Dispose()
    {
        throw new GameRuntimeError("This action hasn't been implemented for this class!");
    }
}