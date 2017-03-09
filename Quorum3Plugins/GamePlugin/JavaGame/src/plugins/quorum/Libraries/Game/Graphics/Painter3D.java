/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plugins.quorum.Libraries.Game.Graphics;

import plugins.quorum.Libraries.Game.GameRuntimeError;
import quorum.Libraries.Game.Graphics.Painter3D_;
import quorum.Libraries.Game.Graphics.Camera_;
import quorum.Libraries.Game.Graphics.Camera;
import quorum.Libraries.Game.Graphics.Renderable_;
import quorum.Libraries.Containers.Array_;
import quorum.Libraries.Game.Graphics.Environment_;
import quorum.Libraries.Game.Graphics.Skybox_;

/**
 *
 * @author alleew
 */
public class Painter3D 
{
    public java.lang.Object me_ = null;
    
    protected RenderContext context = new RenderContext();
    protected ShaderProvider shaderProvider = new ShaderProvider();
    protected Environment_ environment = null;
    protected Skybox_ skybox = null;
    protected SkyboxShader skyboxShader = new SkyboxShader();
    
    private boolean isRendering = false;
    
    // A convenient way to access the quorum side.
    private quorum.Libraries.Game.Graphics.Painter3D quorumBatch;
    
    private Array_ renderables;
    
    private Camera_ camera;
    
    public void Initialize(Painter3D_ batch, Array_ array)
    {
        quorumBatch = (quorum.Libraries.Game.Graphics.Painter3D)batch;
        renderables = array;
    }
    
    public boolean IsRendering()
    {
        return isRendering;
    }
    
    public void SetCamera(Camera_ cam)
    {
        if (isRendering && renderables.GetSize() > 0)
            Flush();
        
        camera = cam;
    }
    
    public Camera_ GetCamera()
    {
        return camera;
    }
    
    public void SetEnvironment(Environment_ environ)
    {
        environment = environ;
    }
    
    public Environment_ GetEnvironment()
    {
        return environment;
    }

    public void SetSkybox(Skybox_ box)
    {
        skybox = box;
    }
    
    public Skybox_ GetSkybox()
    {
        return skybox;
    }
    
    public void Begin()
    {
        if (isRendering)
            throw new GameRuntimeError("The Painter3D is already rendering! Call End() before calling Begin() again.");
        if (camera == null)
            throw new GameRuntimeError("The Painter3D must have a camera set before calling Begin().");
        
        context.Begin();
        isRendering = true;
    }
    
    public void End()
    {
        Flush();
        context.End();
        if (skybox != null)
            skyboxShader.Render(skybox, camera);
        isRendering = false;
    }
    
    public void Flush()
    {
        renderables.Sort();
        Shader currentShader = null;
        for (int i = 0; i < renderables.GetSize(); i++)
        {
            Renderable_ renderable = (Renderable_)renderables.Get(i);
            Renderable renderPlugin = ((quorum.Libraries.Game.Graphics.Renderable)renderable).plugin_;
            if (currentShader != renderPlugin.shader) 
            {
                if (currentShader != null)
                    currentShader.End();
                currentShader = renderPlugin.shader;
                currentShader.Begin(camera, context);
            }
            currentShader.Render(renderable);
        }
        if (currentShader != null)
            currentShader.End();
        
        renderables.Empty();
    }
    
    public void RenderNative(Renderable_ renderable)
    {
        renderable.Set_Libraries_Game_Graphics_Renderable__environment_(environment);
        Renderable renderPlugin = ((quorum.Libraries.Game.Graphics.Renderable)renderable).plugin_;
        renderPlugin.camera = camera;
        renderPlugin.shader = shaderProvider.GetShader(renderable);
    }
}
