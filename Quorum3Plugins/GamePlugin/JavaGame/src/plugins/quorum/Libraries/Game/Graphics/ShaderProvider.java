/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plugins.quorum.Libraries.Game.Graphics;

import quorum.Libraries.Game.Graphics.Renderable_;
import quorum.Libraries.Game.Graphics.Renderable;
import plugins.quorum.Libraries.Game.libGDX.Array;

public abstract class ShaderProvider 
{
    protected Array<Shader> shaders = new Array<Shader>();
    
    public Shader GetShader(Renderable_ renderable)
    {
        Shader suggestedShader = ((Renderable)renderable).plugin_.shader;
        if (suggestedShader != null && suggestedShader.CanRender(renderable)) return suggestedShader;
        for (Shader shader : shaders) {
                if (shader.CanRender(renderable)) return shader;
        }
        final Shader shader = CreateShader(renderable);
        shader.Initialize();
        shaders.add(shader);
        return shader;
    }
    
    protected abstract Shader CreateShader(final Renderable_ renderable);
    
    public void Dispose()
    {
        for (Shader shader: shaders)
        {
            shader.dispose();
        }
        shaders.clear();
    }
}
