/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plugins.quorum.Libraries.System;

/**
 *
 * @author alleew
 */
public class SystemHelper
{
    public java.lang.Object me_ = null;
    
    public void Sleep(int milliseconds)
    {
        try
        {
            Thread.sleep(milliseconds);
        }
        catch (Exception e)
        {
            // Ignore the exception.
        }
    }
    
    public void RequestGarbageCollection()
    {
        System.gc();
    }
    
}
