/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plugins.quorum.Libraries.Game;

import quorum.Libraries.Game.Game_;
import quorum.Libraries.Game.IOSConfiguration_;
import quorum.Libraries.Game.IOSConfiguration;
import quorum.Libraries.Game.IOSDisplay_;
import quorum.Libraries.Game.IOSDisplay;
import quorum.Libraries.Game.Graphics.IOSGraphics;
import quorum.Libraries.Game.Graphics.Painter;

import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.foundation.Foundation;
import org.robovm.apple.foundation.NSString;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIApplicationLaunchOptions;
import org.robovm.apple.uikit.UIWindow;
import org.robovm.apple.uikit.UIScreen;
import org.robovm.apple.uikit.UIDevice;
import org.robovm.apple.uikit.UIUserInterfaceIdiom;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.apple.uikit.UIInterfaceOrientation;

/**
 *
 * @author alleew
 */
public class IOSApplication 
{
    public java.lang.Object me_ = null;
    
    // Reference to the game so we can call its actions.
    public Game_ game;
    
    UIApplication uiApp;
    UIWindow uiWindow;
    //IOSViewControllerListener viewControllerListener;
    IOSConfiguration_ config;
    IOSDisplay_ display;
    //IOSFiles files;
    //IOSInput input;
    //IOSNet net;
    //int logLevel = Application.LOG_DEBUG;
    
    // The display scale factor (1.0f for normal, 2.0f to use retina coordinates/dimensions.
    float displayScaleFactor;
    
    // Reference to the delegate -- may not be necessary.
    IOSDelegate delegate;
    
    private CGRect lastScreenBounds = null;
    
    // Are these necessary?
    //Array<Runnable> runnables = new Array<Runnable>();
    //Array<Runnable> executedRunnables = new Array<Runnable>();
    //Array<LifecycleListener> lifecycleListeners = new Array<LifecycleListener>();
    
    public static void SetOperatingSystem()
    {
        quorum.Libraries.System.File qFile = new quorum.Libraries.System.File();
        /*
        Foundation.log("%@", new NSString("Default directory + path: " + qFile.GetWorkingDirectory() + " + " + qFile.GetPath()));
        Foundation.log("%@", new NSString("Version is " + System.getProperty("os.version")));
        Foundation.log("%@", new NSString("Device name is " + UIDevice.getCurrentDevice().getName()));
        */
        if (UIDevice.getCurrentDevice().getName().contains("Simulator"))
            GameState.SetOperatingSystem("iOS Simulator");
        else
            GameState.SetOperatingSystem("iOS Device");

        //Foundation.log("%@", new NSString("Set OS as " + GameState.GetOperatingSystem()));
    }
    
    public void SetupNative(Game_ game)
    {
        this.game = game;
        delegate = new IOSDelegate();
        delegate.Begin(this);
    }
    
    final boolean DidFinishLaunching(UIApplication uiApp, UIApplicationLaunchOptions options)
    {
        display = (IOSDisplay_)GameState.GetDisplay();
        config = (IOSConfiguration_)display.GetConfiguration();
        
        this.uiApp = uiApp;
        
        UIApplication.getSharedApplication().setIdleTimerDisabled(config.Get_Libraries_Game_IOSConfiguration__preventScreenDimming_());
        
        //Gdx.app.debug("IOSApplication", "iOS version: " + UIDevice.getCurrentDevice().getSystemVersion());
        
        // fix the scale factor if we have a retina device (NOTE: iOS screen sizes are in "points" not pixels by default!)
        //Gdx.app.debug("IOSApplication", "Running in " + (Bro.IS_64BIT ? "64-bit" : "32-bit") + " mode");
        
        float scale = (float)(GetIOSVersion() >= 8 ? UIScreen.getMainScreen().getNativeScale() : UIScreen.getMainScreen().getScale());
        
        /*
        Scaling values are used as divisors of 1 in order to "flip" the scaling
        value about 1. The "NOTE ABOUT SCALING" comment in
        IOSConfiguration.quorum has been copied here for convenience:
        
        NOTE ON SCALING:
        libGDX goes the opposite way with scale, where a smaller scale value will
        make objects larger on the screen (the logic being you are scaling down the
        screen size). In our code, we make a larger scaling value cause items to be
        drawn larger on the screen, thus reducing the effective screen size, in
        order to maintain consistency with how scaling works with Drawables.
        */
        
        if (scale >= 2.0f)
        {
            //Gdx.app.debug("IOSApplication", "scale: " + scale);
            if (UIDevice.getCurrentDevice().getUserInterfaceIdiom() == UIUserInterfaceIdiom.Pad) 
            {
                // it's an iPad!
                displayScaleFactor = (float)(1 / config.Get_Libraries_Game_IOSConfiguration__largeRetinaDisplayScale_()) * scale;
            }
            else
            {
                // it's an iPod or iPhone
                displayScaleFactor = (float)(1 / config.Get_Libraries_Game_IOSConfiguration__smallRetinaDisplayScale_()) * scale;
            }
        }
        else
        {
            // No retina screen, so no scaling.
            if (UIDevice.getCurrentDevice().getUserInterfaceIdiom() == UIUserInterfaceIdiom.Pad) 
            {
                // it's an iPad!
                displayScaleFactor = (float)(1 / config.Get_Libraries_Game_IOSConfiguration__largeNonRetinaDisplayScale_());
            }
            else 
            {
                // it's an iPod or iPhone
                displayScaleFactor = (float)(1 / config.Get_Libraries_Game_IOSConfiguration__smallNonRetinaDisplayScale_());
            }
        }
        
        plugins.quorum.Libraries.Game.Graphics.IOSGraphics.init();

        // setup libgdx
        //this.input = new IOSInput(this);
        
        IOSInput input = ((quorum.Libraries.Game.IOSInput)GameState.GetInput()).plugin_;
        input.Initialize(this);
        ((IOSDisplay)display).plugin_.Initialize(scale, this, config, input, ((IOSGraphics)GameState.GetGameGraphics()).plugin_);
        //this.files = new IOSFiles();
        //this.audio = new IOSAudio(config);
        //this.net = new IOSNet(this);

        //Gdx.files = this.files;
        //Gdx.graphics = this.graphics;
        //Gdx.audio = this.audio;
        //Gdx.input = this.input;
        //Gdx.net = this.net;

        //this.input.setupPeripherals();

        this.uiWindow = new UIWindow(UIScreen.getMainScreen().getBounds());
        this.uiWindow.setRootViewController(((IOSDisplay)display).plugin_.viewController);
        this.uiWindow.makeKeyAndVisible();
        //Gdx.app.debug("IOSApplication", "created");
        
        return true;
    }
    
    private int GetIOSVersion()
    {
        String systemVersion = UIDevice.getCurrentDevice().getSystemVersion();
        int version = Integer.parseInt(systemVersion.split("\\.")[0]);
        return version;
    }
    
    public UIViewController GetUIViewController()
    {
        //return graphics.viewController;
        // TEMP:
        return null;
    }
    
    public UIWindow GetUIWindow()
    {
        return uiWindow;
    }
    
    public CGRect GetBounds()
    {
        // or screen size (always portrait)
        final CGRect screenBounds = UIScreen.getMainScreen().getBounds();
        final CGRect statusBarFrame = uiApp.getStatusBarFrame();
        final UIInterfaceOrientation statusBarOrientation = uiApp.getStatusBarOrientation();
        
        double statusBarHeight = Math.min(statusBarFrame.getWidth(), statusBarFrame.getHeight());
        
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

        // Make sure that the orientation is consistent with ratios. Should be, but may not be on older iOS versions
        switch (statusBarOrientation) 
        {
            case LandscapeLeft:
            case LandscapeRight:
                if (screenHeight > screenWidth) 
                {
                    // Swap the reported screen height and screen width.
                    double tmp = screenHeight;
                    screenHeight = screenWidth;
                    screenWidth = tmp;
                }
        }

        // update width/height depending on display scaling selected
        screenWidth *= displayScaleFactor;
        screenHeight *= displayScaleFactor;

        if (statusBarHeight != 0.0) 
        {
            //debug("IOSApplication", "Status bar is visible (height = " + statusBarHeight + ")");
            statusBarHeight *= displayScaleFactor;
            screenHeight -= statusBarHeight;
        } 
        else 
        {
            //debug("IOSApplication", "Status bar is not visible");
        }

        //debug("IOSApplication", "Total computed bounds are w=" + screenWidth + " h=" + screenHeight);

        return lastScreenBounds = new CGRect(0.0, statusBarHeight, screenWidth, screenHeight);
    }
    
    public CGRect GetCachedBounds()
    {
        if(lastScreenBounds == null)
            return GetBounds();
        else
            return lastScreenBounds;
    }
    
    public void DidBecomeActive(UIApplication uiApp)
    {
        /*
        Gdx.app.debug("IOSApplication", "resumed");
        // workaround for ObjectAL crash problem
        // see: https://groups.google.com/forum/?fromgroups=#!topic/objectal-for-iphone/ubRWltp_i1Q
        OALAudioSession.sharedInstance().forceEndInterruption();
        if (config.allowIpod) {
                OALSimpleAudio.sharedInstance().setUseHardwareIfAvailable(false);
        }*/
        ((IOSDisplay)display).plugin_.MakeCurrent();
        ((IOSDisplay)display).plugin_.Resume();
    }
    
    public void WillEnterForeground (UIApplication uiApp) 
    {
        /*
        // workaround for ObjectAL crash problem
        // see: https://groups.google.com/forum/?fromgroups=#!topic/objectal-for-iphone/ubRWltp_i1Q
        OALAudioSession.sharedInstance().forceEndInterruption(); 
        */    
    }

    public void WillResignActive (UIApplication uiApp) 
    {
        /*
        Gdx.app.debug("IOSApplication", "paused");
        */
        ((IOSDisplay)display).plugin_.MakeCurrent();
        ((IOSDisplay)display).plugin_.Pause();
        ((IOSGraphics)GameState.GetGameGraphics()).plugin_.glFlush();
    }

    public void WillTerminate (UIApplication uiApp) 
    {
        /*
        Gdx.app.debug("IOSApplication", "disposed");
        */
        ((IOSDisplay)display).plugin_.MakeCurrent();
        /*
        Array<LifecycleListener> listeners = lifecycleListeners;
        synchronized (listeners) {
                for (LifecycleListener listener : listeners) {
                        listener.pause();
                }
        }
        listener.dispose();
        */
        ((IOSGraphics)GameState.GetGameGraphics()).plugin_.glFlush();
    }
}
