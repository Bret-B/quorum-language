/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plugins.quorum.Libraries.Sound;

import plugins.quorum.Libraries.Sound.IOS.IOSLoader;
import quorum.Libraries.System.File_;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alleew
 */
public class Audio {
    
    public java.lang.Object me_ = null;
    
    Data data;
    
    public static final DataLoader loader;
    
    static
    {
        String os = System.getProperty("os.name");
        
        /*
        We only need to find LWJGL if we are on a Desktop. If we are on Mac, 
        OpenAL will be accessed via static library.
        */
        if (os.contains("Windows") || os.contains("Mac"))
        {
            try 
            {
                URI uri = Audio.class.getProtectionDomain().getCodeSource().getLocation().toURI();
                String uriPath = uri.getPath();
                
                if (uri.getAuthority() != null)
                    uriPath = "\\\\" + uri.getAuthority() + uriPath;

                java.io.File file = new java.io.File(uriPath);
                
                String runLocation = file.getParentFile().getAbsolutePath();
                String lwjgl = runLocation + "/jni";
                System.setProperty("org.lwjgl.librarypath", lwjgl);
            } 
            catch (URISyntaxException ex) 
            {
                Logger.getLogger(Audio.class.getName()).log(Level.SEVERE, null, ex);
            }
            loader = new DesktopLoader();
        }
        else
        {
            loader = new IOSLoader();
        }
    }
    
    public void Load(quorum.Libraries.System.File_ quorumFile)
    {
        if (data != null)
            throw new RuntimeException("This audio has already been loaded! To reuse this audio, call Dispose() before loading again.");
        
        data = loader.Load(quorumFile);
    }
    
    public void LoadToStream(quorum.Libraries.System.File_ quorumFile)
    {
        if (data != null)
            throw new RuntimeException("This audio has already been loaded! To reuse this audio, call Dispose() before loading again.");
        
        data = loader.LoadToStream(quorumFile);
    }
    
    public void Load(quorum.Libraries.Sound.AudioSamples_ buffer)
    {
        if (data != null)
            throw new RuntimeException("This audio has already been loaded! To reuse this audio, call Dispose() before loading again.");
        
        data = loader.Load(buffer);
    }
    
    public void Play()
    {
        if (data == null)
            throw new RuntimeException("Can't play audio before it's loaded -- use Load first.");
        data.Play();
    }
    
    public void EnableLooping()
    {
        if (data == null)
            throw new RuntimeException("Can't enable audio looping before it's loaded -- use Load first.");
        data.SetLooping(true);
    }
    
    public void DisableLooping()
    {
        if (data == null)
            throw new RuntimeException("Can't disable audio looping before it's loaded -- use Load first.");
        data.SetLooping(false);
    }
    
    public void Stop()
    {
        if (data == null)
            throw new RuntimeException("Can't stop audio before it's loaded -- use Load first.");
        data.Stop();
    }
    
    public void Dispose()
    {
        if (data == null)
            throw new RuntimeException("There was no data to dispose! Use Load first.");
        data.Dispose();
        data = null;
    }
    
    public void Pause()
    {
        if (data == null)
            throw new RuntimeException("Can't pause audio before it's loaded -- use Load first.");
        data.Pause();
    }
    
    public void Resume()
    {
        if (data == null)
            throw new RuntimeException("Can't resume audio before it's loaded -- use Load first.");
        data.Resume();
    }
    
    public void SetPitch(double pitch)
    {
        data.SetPitch((float)pitch);
    }
    
    public void SetVolume(double volume)
    {
        if (volume < 0)
            volume = 0;
        data.SetVolume((float)volume);
    }
    
    public void SetBalance(double position)
    {
        if (position < -1)
            position = - 1;
        else if (position > 1)
            position = 1;
        
        data.SetHorizontalPosition((float)position);
    }
    
    public void SetFade(double fade)
    {
        if (fade < -1)
            fade = - 1;
        else if (fade > 1)
            fade = 1;
        
        data.SetFade((float)fade);
    }
    
    public void SetX(double newX)
    {
        data.SetX((float)newX);
    }
    
    public void SetY(double newY)
    {
        data.SetY((float)newY);
    }
    
    public void SetZ(double newZ)
    {
        data.SetZ((float)newZ * -1);
    }
    
    public double GetX()
    {
        return data.GetX();
    }
    
    public double GetY()
    {
        return data.GetY();
    }
    
    public double GetZ()
    {
        return data.GetZ() * -1;
    }
    
    public void SetPosition(double newX, double newY, double newZ)
    {
        data.SetPosition((float)newX, (float)newY, (float)newZ * -1);
    }
    
    public void EnableDoppler()
    {
        data.EnableDoppler();
    }
    
    public void DisableDoppler()
    {
        data.DisableDoppler();
    }
    
    public boolean IsDopplerEnabled()
    {
        return data.IsDopplerEnabled();
    }
    
    public void SetVelocityX(double x)
    {
        data.SetVelocityX((float)x);
    }
    
    public void SetVelocityY(double y)
    {
        data.SetVelocityY((float)y);
    }
    
    public void SetVelocityZ(double z)
    {
        data.SetVelocityZ((float)z * -1);
    }
    
    public void SetVelocity(double x, double y, double z)
    {
        data.SetVelocity((float)x, (float)y, (float)z * -1);
    }
    
    public double GetVelocityX()
    {
        return data.velocityX;
    }
    
    public double GetVelocityY()
    {
        return data.velocityY;
    }

    public double GetVelocityZ()
    {
        return data.velocityZ * -1;
    }
    
    public void SetRotation(double rotation)
    {
        data.SetRotation(rotation);
    }
    
    public void Rotate(double rotation)
    {
        data.Rotate(rotation);
    }
    
    public boolean IsStreaming()
    {
        return data.IsStreaming();
    }
    
    public boolean IsPlaying()
    {
        return data.IsPlaying();
    }
    
    public boolean IsLoopingEnabled()
    {
        return data.IsLooping();
    }
    
    public double GetBalance()
    {
        return data.GetBalance();
    }
    
    public double GetVolume()
    {
        return data.GetVolume();
    }
    
    public double GetPitch()
    {
        return data.GetPitch();
    }
    
    public double GetRotation()
    {
        return data.GetRotation();
    }
    
    public double GetFade()
    {
        return data.GetFade();
    }
    
    public void Stream()
    {
        if (data == null)
            throw new RuntimeException("Can't stream audio before it's loaded -- call Load first.");
        data.Update();
    }
    
    public void SetListenerPosition(double x, double y, double z)
    {
        AudioManager.SetListenerPosition(x, y, z * -1);
    }
    
    public void SetListenerX(double x)
    {
        AudioManager.SetListenerPosition(x, AudioManager.GetListenerY(), AudioManager.GetListenerZ());
    }
    
    public void SetListenerY(double y)
    {
        AudioManager.SetListenerPosition(AudioManager.GetListenerX(), y, AudioManager.GetListenerZ());
    }
    
    public void SetListenerZ(double z)
    {
        AudioManager.SetListenerPosition(AudioManager.GetListenerX(), AudioManager.GetListenerY(), z * -1);
    }
    
    public double GetListenerX()
    {
        return AudioManager.GetListenerX();
    }
    
    public double GetListenerY()
    {
        return AudioManager.GetListenerY();
    }
    
    public double GetListenerZ()
    {
        return AudioManager.GetListenerZ() * -1;
    }
    
    public void SetListenerVelocity(double x, double y, double z)
    {
        AudioManager.SetListenerVelocity(x, y, z * -1);
    }
    
    public void SetListenerVelocityX(double x)
    {
        SetListenerVelocity(x, AudioManager.GetListenerVelocityY(), AudioManager.GetListenerVelocityZ());
    }
    
    public void SetListenerVelocityY(double y)
    {
        SetListenerVelocity(AudioManager.GetListenerVelocityX(), y, AudioManager.GetListenerVelocityZ());
    }
    
    public void SetListenerVelocityZ(double z)
    {
        SetListenerVelocity(AudioManager.GetListenerVelocityX(), AudioManager.GetListenerVelocityY(), z * -1);
    }
    
    public double GetListenerVelocityX()
    {
        return AudioManager.GetListenerVelocityX();
    }
    
    public double GetListenerVelocityY()
    {
        return AudioManager.GetListenerVelocityY();
    }
    
    public double GetListenerVelocityZ()
    {
        return AudioManager.GetListenerVelocityZ() * -1;
    }
    
    public void EnableListenerDoppler()
    {
        AudioManager.EnableListenerDoppler();
    }
    
    public void DisableListenerDoppler()
    {
        AudioManager.DisableListenerDoppler();
    }
    
    public boolean IsListenerDopplerEnabled()
    {
        return AudioManager.IsListenerDopplerEnabled();
    }
    
    public void SetListenerDirection(double x, double y, double z)
    {
        AudioManager.SetListenerDirection(x, y, z * -1);
    }
    
    public void SetListenerUp(double x, double y, double z)
    {
        AudioManager.SetListenerUp(x, y, z * -1);
    }
    
    public double GetListenerDirectionX()
    {
        return AudioManager.GetListenerDirectionX();
    }
    
    public double GetListenerDirectionY()
    {
        return AudioManager.GetListenerDirectionY();
    }
    
    public double GetListenerDirectionZ()
    {
        return AudioManager.GetListenerDirectionZ() * -1;
    }
    
    public double GetListenerUpX()
    {
        return AudioManager.GetListenerUpX();
    }
    
    public double GetListenerUpY()
    {
        return AudioManager.GetListenerUpY();
    }
    
    public double GetListenerUpZ()
    {
        return AudioManager.GetListenerUpZ() * -1;
    }
}
