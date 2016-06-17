/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package plugins.quorum.Libraries.Game.Graphics;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.ByteOrder;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import plugins.quorum.Libraries.Game.GameRuntimeError;
import plugins.quorum.Libraries.Game.GameState;

/**
 *
 * @author Taylor Bockman
 * 
 * This is a utility class utilized exclusively by the Java plugin side.
 */
public class DesktopGraphics implements GraphicsManager {
    public java.lang.Object me_ = null;

    // All GL20 constants are kept at the bottom of this class (as to make it
    // easier to find the functions while work is still undergoing).
    
    private ByteBuffer buffer = null;
    private FloatBuffer floatBuffer = null;
    private IntBuffer intBuffer = null;

    //Sets the OpenGL Clear Screen color when wiping the screen
    public void ClearScreenColor(float red, float green, float blue, float alpha)
    {
        GL11.glClearColor(red, green, blue, alpha);
    }

    public void glBindTexture (int target, int texture) 
    {
        GL11.glBindTexture(target, texture);
    }

    public void PixelStorageMode(int type, int parameter) {
        GL11.glPixelStorei(type, parameter);
    }
    
    public String glGetString (int name) 
    {
	return GL11.glGetString(name);
    }
    
    // This version of ClearScreenColor accepts the 64 bit values that the Quorum
    // "number" data type uses (i.e. doubles) and casts them down to 32 bit for
    // usage by GL11.
    public void ClearScreenColor(double red, double green, double blue, double alpha)
    {
        GL11.glClearColor((float)red, (float)green, (float)blue, (float)alpha);
    }

    //Actually performs the screen clearing
    public void ClearScreen(int mask){
        GL11.glClear(mask);
    }

    //Actually glViewport
    public void SetDrawingRegion(int x, int y, int width, int height){
        GL11.glViewport(x, y, width, height);
    }
    
    public void glActiveTexture(int texture)
    {
        GL13.glActiveTexture(texture);
    }

    public int glGenTexture()
    {
        return GL11.glGenTextures();
    }

    public void glGenTextures (int n, IntBuffer textures) {
        GL11.glGenTextures(textures);
    }

    public int glGenBuffer () 
    {
        return GL15.glGenBuffers();
    }

    public void glBindBuffer (int target, int buffer) 
    {
        GL15.glBindBuffer(target, buffer);
    }
    
    public void glBufferData (int target, int size, Buffer data, int usage) 
    {
	if (data == null)
            GL15.glBufferData(target, size, usage);
	else if (data instanceof ByteBuffer)
            GL15.glBufferData(target, (ByteBuffer)data, usage);
	else if (data instanceof IntBuffer)
            GL15.glBufferData(target, (IntBuffer)data, usage);
	else if (data instanceof FloatBuffer)
            GL15.glBufferData(target, (FloatBuffer)data, usage);
	else if (data instanceof DoubleBuffer)
            GL15.glBufferData(target, (DoubleBuffer)data, usage);
	else if (data instanceof ShortBuffer) //
            GL15.glBufferData(target, (ShortBuffer)data, usage);
    }
    
    public void glBufferSubData (int target, int offset, int size, Buffer data) 
    {
	if (data == null)
            throw new GameRuntimeError("Using null for the data not possible, blame LWJGL");
	else if (data instanceof ByteBuffer)
            GL15.glBufferSubData(target, offset, (ByteBuffer)data);
	else if (data instanceof IntBuffer)
            GL15.glBufferSubData(target, offset, (IntBuffer)data);
	else if (data instanceof FloatBuffer)
            GL15.glBufferSubData(target, offset, (FloatBuffer)data);
	else if (data instanceof DoubleBuffer)
            GL15.glBufferSubData(target, offset, (DoubleBuffer)data);
	else if (data instanceof ShortBuffer) //
            GL15.glBufferSubData(target, offset, (ShortBuffer)data);
    }
    
    public void glDeleteBuffer (int buffer) 
    {
	GL15.glDeleteBuffers(buffer);
    }
    
    public void glDeleteTexture (int texture) 
    {
	GL11.glDeleteTextures(texture);
    }
    
    public void glDepthMask (boolean flag) 
    {
	GL11.glDepthMask(flag);
    }
    
    public void glDisable (int cap) 
    {
	GL11.glDisable(cap);
    }
    
    public void glEnable (int cap) 
    {
	GL11.glEnable(cap);
    }
    
    public int glCreateShader (int type) 
    {
	return GL20.glCreateShader(type);
    }
    
    public void glShaderSource (int shader, String string) 
    {
	GL20.glShaderSource(shader, string);
    }
    
    public void glCompileShader (int shader) 
    {
	GL20.glCompileShader(shader);
    }
    
    public void glGetShaderiv (int shader, int pname, IntBuffer params) 
    {
		GL20.glGetShader(shader, pname, params);
    }
    
    public String glGetShaderInfoLog (int shader) 
    {
	ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 10);
	buffer.order(ByteOrder.nativeOrder());
	ByteBuffer tmp = ByteBuffer.allocateDirect(4);
	tmp.order(ByteOrder.nativeOrder());
	IntBuffer intBuffer = tmp.asIntBuffer();

	GL20.glGetShaderInfoLog(shader, intBuffer, buffer);
	int numBytes = intBuffer.get(0);
	byte[] bytes = new byte[numBytes];
	buffer.get(bytes);
	return new String(bytes);
    }
    
    public String glGetProgramInfoLog (int program) 
    {
	ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 10);
	buffer.order(ByteOrder.nativeOrder());
	ByteBuffer tmp = ByteBuffer.allocateDirect(4);
	tmp.order(ByteOrder.nativeOrder());
	IntBuffer intBuffer = tmp.asIntBuffer();

	GL20.glGetProgramInfoLog(program, intBuffer, buffer);
	int numBytes = intBuffer.get(0);
	byte[] bytes = new byte[numBytes];
	buffer.get(bytes);
	return new String(bytes);
    }
    
    public void glGetProgramiv (int program, int pname, IntBuffer params) 
    {
	GL20.glGetProgram(program, pname, params);
    }
    
    public int glGetAttribLocation (int program, String name) 
    {
	return GL20.glGetAttribLocation(program, name);
    }
    
    public int glCreateProgram () 
    {
	return GL20.glCreateProgram();
    }
    
    public void glAttachShader (int program, int shader) 
    {
	GL20.glAttachShader(program, shader);
    }
    
    public void glLinkProgram (int program) 
    {
	GL20.glLinkProgram(program);
    }
    
    public int glGetUniformLocation (int program, String name) 
    {
	return GL20.glGetUniformLocation(program, name);
    }
    
    public void glUniform1i (int location, int x) 
    {
	GL20.glUniform1i(location, x);
    }

    public void glUniform1iv (int location, int count, IntBuffer v) 
    {
	GL20.glUniform1(location, v);
    }

    public void glUniform1iv (int location, int count, int[] v, int offset) 
    {
	GL20.glUniform1(location, toIntBuffer(v, offset, count));
    }
    
    public void glUniform1f (int location, float x) 
    {
	GL20.glUniform1f(location, x);
    }
    
    public void glUniform1fv (int location, int count, FloatBuffer v) 
    {
	GL20.glUniform1(location, v);
    }

    public void glUniform1fv (int location, int count, float[] v, int offset) 
    {
	GL20.glUniform1(location, toFloatBuffer(v, offset, count));
    }
    
    public void glUniform2i (int location, int x, int y) 
    {
	GL20.glUniform2i(location, x, y);
    }

    public void glUniform2iv (int location, int count, IntBuffer v) 
    {
	GL20.glUniform2(location, v);
    }

    public void glUniform2iv (int location, int count, int[] v, int offset) 
    {
	GL20.glUniform2(location, toIntBuffer(v, offset, count << 1));
    }
    
    public void glUniform2f (int location, float x, float y) 
    {
	GL20.glUniform2f(location, x, y);
    }
    
    public void glUniform2fv (int location, int count, FloatBuffer v) 
    {
	GL20.glUniform2(location, v);
    }

    public void glUniform2fv (int location, int count, float[] v, int offset) 
    {
	GL20.glUniform2(location, toFloatBuffer(v, offset, count << 1));
    }
    
    public void glUniform3i (int location, int x, int y, int z) 
    {
	GL20.glUniform3i(location, x, y, z);
    }

    public void glUniform3iv (int location, int count, IntBuffer v) 
    {
	GL20.glUniform3(location, v);
    }

    public void glUniform3iv (int location, int count, int[] v, int offset) 
    {
	GL20.glUniform3(location, toIntBuffer(v, offset, count * 3));
    }
    
    public void glUniform3f (int location, float x, float y, float z) 
    {
	GL20.glUniform3f(location, x, y, z);
    }
    
    public void glUniform3fv (int location, int count, FloatBuffer v) 
    {
	GL20.glUniform3(location, v);
    }

    public void glUniform3fv (int location, int count, float[] v, int offset) 
    {
	GL20.glUniform3(location, toFloatBuffer(v, offset, count * 3));
    }
    
    public void glUniform4i (int location, int x, int y, int z, int w) 
    {
	GL20.glUniform4i(location, x, y, z, w);
    }

    public void glUniform4iv (int location, int count, IntBuffer v) 
    {
	GL20.glUniform4(location, v);
    }

    public void glUniform4iv (int location, int count, int[] v, int offset) 
    {
	GL20.glUniform4(location, toIntBuffer(v, offset, count << 2));
    }
    
    public void glUniform4f (int location, float x, float y, float z, float w) 
    {
	GL20.glUniform4f(location, x, y, z, w);
    }
    
    public void glUniform4fv (int location, int count, FloatBuffer v) 
    {
	GL20.glUniform4(location, v);
    }

    public void glUniform4fv (int location, int count, float[] v, int offset) 
    {
	GL20.glUniform4(location, toFloatBuffer(v, offset, count << 2));
    }

    public void glUniformMatrix2fv (int location, int count, boolean transpose, FloatBuffer value) 
    {
	GL20.glUniformMatrix2(location, transpose, value);
    }

    public void glUniformMatrix2fv (int location, int count, boolean transpose, float[] value, int offset) 
    {
        GL20.glUniformMatrix2(location, transpose, toFloatBuffer(value, offset, count << 2));
    }

    public void glUniformMatrix3fv (int location, int count, boolean transpose, FloatBuffer value) 
    {
	GL20.glUniformMatrix3(location, transpose, value);
    }

    public void glUniformMatrix3fv (int location, int count, boolean transpose, float[] value, int offset) 
    {
	GL20.glUniformMatrix3(location, transpose, toFloatBuffer(value, offset, count * 9));
    }

    public void glUniformMatrix4fv (int location, int count, boolean transpose, FloatBuffer value) 
    {
	GL20.glUniformMatrix4(location, transpose, value);
    }

    public void glUniformMatrix4fv (int location, int count, boolean transpose, float[] value, int offset) 
    {
	GL20.glUniformMatrix4(location, transpose, toFloatBuffer(value, offset, count << 4));
    }
    
    public void glVertexAttribPointer (int indx, int size, int type, boolean normalized, int stride, Buffer buffer) 
    {
	if (buffer instanceof ByteBuffer) 
        {
            if (type == GL_BYTE)
		GL20.glVertexAttribPointer(indx, size, false, normalized, stride, (ByteBuffer)buffer);
            else if (type == GL_UNSIGNED_BYTE)
		GL20.glVertexAttribPointer(indx, size, true, normalized, stride, (ByteBuffer)buffer);
            else if (type == GL_SHORT)
		GL20.glVertexAttribPointer(indx, size, false, normalized, stride, ((ByteBuffer)buffer).asShortBuffer());
            else if (type == GL_UNSIGNED_SHORT)
		GL20.glVertexAttribPointer(indx, size, true, normalized, stride, ((ByteBuffer)buffer).asShortBuffer());
            else if (type == GL_FLOAT)
		GL20.glVertexAttribPointer(indx, size, normalized, stride, ((ByteBuffer)buffer).asFloatBuffer());
            else
		throw new GameRuntimeError("Can't use " + buffer.getClass().getName() + " with type " + type
						+ " with this method. Use ByteBuffer and one of GL_BYTE, GL_UNSIGNED_BYTE, GL_SHORT, GL_UNSIGNED_SHORT or GL_FLOAT for type. Blame LWJGL");
	}
        else if (buffer instanceof FloatBuffer) 
        {
            if (type == GL_FLOAT)
		GL20.glVertexAttribPointer(indx, size, normalized, stride, (FloatBuffer)buffer);
            else
		throw new GameRuntimeError("Can't use " + buffer.getClass().getName() + " with type " + type + " with this method.");
	} 
        else
            throw new GameRuntimeError("Can't use " + buffer.getClass().getName() + " with this method. Use ByteBuffer instead. Blame LWJGL");
    }
    
    public void glVertexAttribPointer (int indx, int size, int type, boolean normalized, int stride, int ptr) 
    {
	GL20.glVertexAttribPointer(indx, size, type, normalized, stride, ptr);
    }
    
    public void glUseProgram (int program) 
    {
	GL20.glUseProgram(program);
    }
    
    public void glDeleteShader (int shader) 
    {
	GL20.glDeleteShader(shader);
    }
    
    public void glDeleteProgram (int program) 
    {
	GL20.glDeleteProgram(program);
    }
    
    public void glDisableVertexAttribArray (int index) 
    {
        GL20.glDisableVertexAttribArray(index);
    }
    
    public void glEnableVertexAttribArray (int index) 
    {
	GL20.glEnableVertexAttribArray(index);
    }
    
    public void glVertexAttrib4f (int indx, float x, float y, float z, float w) 
    {
	GL20.glVertexAttrib4f(indx, x, y, z, w);
    }

    public void glVertexAttrib4fv (int indx, FloatBuffer values) 
    {
	GL20.glVertexAttrib4f(indx, values.get(), values.get(), values.get(), values.get());
    }
    
    public String glGetActiveUniform (int program, int index, IntBuffer size, Buffer type) 
    {
	// FIXME this is less than ideal of course...
	IntBuffer typeTmp = BufferUtils.createIntBuffer(2);
	String name = GL20.glGetActiveUniform(program, index, 256, typeTmp);
	size.put(typeTmp.get(0));
	if (type instanceof IntBuffer) ((IntBuffer)type).put(typeTmp.get(1));
	return name;
    }
    
    public String glGetActiveAttrib (int program, int index, IntBuffer size, Buffer type) 
    {
	// FIXME this is less than ideal of course...
	IntBuffer typeTmp = BufferUtils.createIntBuffer(2);
	String name = GL20.glGetActiveAttrib(program, index, 256, typeTmp);
	size.put(typeTmp.get(0));
	if (type instanceof IntBuffer) ((IntBuffer)type).put(typeTmp.get(1));
	return name;
    }
    
    public void glDrawElements (int mode, int count, int type, int indices) 
    {
	GL11.glDrawElements(mode, count, type, indices);
    }
    
    public void glDrawElements (int mode, int count, int type, Buffer indices) 
    {
	if (indices instanceof ShortBuffer && type == GL_UNSIGNED_SHORT)
            GL11.glDrawElements(mode, (ShortBuffer)indices);
	else if (indices instanceof ByteBuffer && type == GL_UNSIGNED_SHORT)
            GL11.glDrawElements(mode, ((ByteBuffer)indices).asShortBuffer()); // FIXME yay...
        else if (indices instanceof ByteBuffer && type == GL_UNSIGNED_BYTE)
            GL11.glDrawElements(mode, (ByteBuffer)indices);
        else if (indices instanceof IntBuffer && type == GL_UNSIGNED_INT)
            GL11.glDrawElements(mode, (IntBuffer)indices);
	else
            throw new GameRuntimeError("Can't use " + indices.getClass().getName()
			+ " with this method. Use ShortBuffer or ByteBuffer instead. Blame LWJGL");
    }
    
    public void glDrawArrays (int mode, int first, int count) 
    {
	GL11.glDrawArrays(mode, first, count);
    }
    
    public void glBlendFunc (int sfactor, int dfactor) 
    {
	GL11.glBlendFunc(sfactor, dfactor);
    }
  
    public void SetTextureParameter(int targetTexture, int parameterType, int parameterValue)
    {
        GL11.glTexParameterf(targetTexture, parameterType, parameterValue);
    }

    //This method was pulled directly out of libgdx because it just works as a drop-in.
    public void glTexImage2D (int target, int level, int internalformat, int width, int height, int border, int format, int type,
                  Buffer pixels) 
    {
        if (pixels == null)
            GL11.glTexImage2D(target, level, internalformat, width, height, border, format, type, (ByteBuffer)null);
        else if (pixels instanceof ByteBuffer)
            GL11.glTexImage2D(target, level, internalformat, width, height, border, format, type, (ByteBuffer)pixels);
        else if (pixels instanceof ShortBuffer)
            GL11.glTexImage2D(target, level, internalformat, width, height, border, format, type, (ShortBuffer)pixels);
        else if (pixels instanceof IntBuffer)
            GL11.glTexImage2D(target, level, internalformat, width, height, border, format, type, (IntBuffer)pixels);
        else if (pixels instanceof FloatBuffer)
            GL11.glTexImage2D(target, level, internalformat, width, height, border, format, type, (FloatBuffer)pixels);
        else if (pixels instanceof DoubleBuffer)
            GL11.glTexImage2D(target, level, internalformat, width, height, border, format, type, (DoubleBuffer)pixels);
        else
            throw new GameRuntimeError("An error occurred while defining 2D image.");

    }
    
    public void glGetIntegerv(int pname, IntBuffer params)
    {
        GL11.glGetInteger(pname, params);
    }
    
    public void glDepthFunc (int func) 
    {
        GL11.glDepthFunc(func);
    }
    
    public void glDepthRangef (float zNear, float zFar) 
    {
        GL11.glDepthRange(zNear, zFar);
    }
    
    public void glCullFace (int mode) 
    {
        GL11.glCullFace(mode);
    }
  
    private IntBuffer toIntBuffer (int v[], int offset, int count) 
    {
	ensureBufferCapacity(count << 2);
	floatBuffer.clear();
	plugins.quorum.Libraries.Game.libGDX.BufferUtils.copy(v, count, offset, intBuffer);
	return intBuffer;
    }

	private void ensureBufferCapacity (int numBytes) 
        {
            if (buffer == null || buffer.capacity() < numBytes) {
		buffer = plugins.quorum.Libraries.Game.libGDX.BufferUtils.newByteBuffer(numBytes);
		floatBuffer = buffer.asFloatBuffer();
		intBuffer = buffer.asIntBuffer();
            }
	}

	private FloatBuffer toFloatBuffer (float v[], int offset, int count) {
		ensureBufferCapacity(count << 2);
		floatBuffer.clear();
		plugins.quorum.Libraries.Game.libGDX.BufferUtils.copy(v, floatBuffer, count, offset);
		return floatBuffer;
        }
}

