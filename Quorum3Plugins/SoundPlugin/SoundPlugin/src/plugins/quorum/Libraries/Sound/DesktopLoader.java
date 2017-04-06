/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plugins.quorum.Libraries.Sound;

import java.io.File;
import quorum.Libraries.System.File_;
import quorum.Libraries.Sound.AudioBuffer_;

/**
 *
 * @author alleew
 */
public class DesktopLoader implements DataLoader
{

    @Override
    public Data Load(File_ quorumFile) 
    {
        File file = new File(quorumFile.GetAbsolutePath());
        String fileName = file.getName().toLowerCase();
        
        if (fileName.endsWith(".wav"))
            return new WavData(file);
        else if (fileName.endsWith(".ogg"))
            return new OggData(file);
        else 
            throw new RuntimeException("Can't load file " + file.getAbsolutePath() + " because the file extension is unsupported!");
    }

    @Override
    public Data LoadToStream(File_ quorumFile) 
    {
        File file = new File(quorumFile.GetAbsolutePath());
        String fileName = file.getName().toLowerCase();
        
        if (fileName.endsWith(".wav"))
            return new WavStreamingData(file);
        else if (fileName.endsWith(".ogg"))
            return new OggStreamingData(file);
        else 
            throw new RuntimeException("Can't load file " + file.getAbsolutePath() + " because the file extension is unsupported!");
    }
    
    @Override
    public Data Load(AudioBuffer_ buffer)
    {
        return new WavData(buffer);
    }
    
}
