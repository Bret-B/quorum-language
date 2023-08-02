package plugins.quorum.Libraries.Interface.Accessibility.IOS;

import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.foundation.NSArray;
import org.robovm.apple.uikit.*;
import plugins.quorum.Libraries.Game.IOSApplication;
import quorum.Libraries.Game.Shapes.Rectangle_;
import quorum.Libraries.Interface.Item2D_;
import quorum.Libraries.Interface.Item3D_;
import quorum.Libraries.Interface.Item_;

import java.util.Set;

public class ItemIOS extends UIAccessibilityElement implements UIAccessibilityAction, UIAccessibilityFocus{
    @Override
    public boolean isAccessibilityElement() {
        return true;
    }

    public ItemIOS(UIAccessibilityContainer container)
    {
        super(container);
    }

    Item_ item;
    boolean focused = false;

    public void SetItem(Item_ item)
    {
        this.item = item;
    }

    public void Initialize(Item_ item) {
        this.item = item;
        UIAccessibilityTraits traits = UIAccessibilityTraits.Button;
        this.setAccessibilityTraits(traits);

    }

    @Override
    public CGRect getAccessibilityFrame() {
        int x = 0;
        int y = 0;
        int width = 0;
        int height = 0;

        if (item instanceof Item2D_)
        {
            double itemX = ((Item2D_)item).GetScreenX();
            double itemY = (((Item2D_)item).GetScreenY());
            if (Double.isNaN(itemX)) {
                itemX = 0;
            }

            if (Double.isNaN(itemY)) {
                itemY = 0;
            }

            width = (int) (((Item2D_) item).GetWidth() / IOSApplication.containerScaleFactorWidth);
            height = (int) (((Item2D_) item).GetHeight() / IOSApplication.containerScaleFactorHeight);
            x = (int)(itemX / IOSApplication.containerScaleFactorWidth);
            y = (int)(IOSApplication.accessibilityContainerBounds.getHeight() - ( height + (itemY / IOSApplication.containerScaleFactorHeight)));
        }
        else if (item instanceof Item3D_) {
            // This is only a place holder, to place a small box roughly at the
            // center of a 3D object in the screen. To calculate this correctly,
            // check how we calculate mouse input detection for 3D objects.
            Rectangle_ rectangle = ((Item3D_) item).GetScreenBounds();

            width = (int) (rectangle.GetWidth() / IOSApplication.containerScaleFactorWidth);
            height = (int) (rectangle.GetY() + rectangle.GetHeight() / IOSApplication.containerScaleFactorHeight);
            x = (int) (rectangle.GetX() / IOSApplication.containerScaleFactorWidth);
            y = (int) (IOSApplication.accessibilityContainerBounds.getHeight() - (height + (rectangle.GetY() / IOSApplication.containerScaleFactorHeight)));
        }

        return new CGRect(x, y, width, height);
    }

    @Override
    public NSArray<UIAccessibilityCustomAction> getAccessibilityCustomActions() {
        return null;
    }

    @Override
    public void setAccessibilityCustomActions(NSArray<UIAccessibilityCustomAction> v) {

    }

    @Override
    public boolean activate() {
        return false;
    }

    @Override
    public void increment() {

    }

    @Override
    public void decrement() {

    }

    @Override
    public boolean scroll(UIAccessibilityScrollDirection direction) {
        return false;
    }

    @Override
    public boolean performEscape() {
        return false;
    }

    @Override
    public boolean performMagicTap() {
        return false;
    }

    @Override
    public void didBecomeFocused() {
        System.out.println(item.GetName() + " gained focus");
        focused = true;
    }

    @Override
    public void didLoseFocus() {
        System.out.println(item.GetName() + " lost focus");
        focused = false;
    }

    @Override
    public boolean isFocused() {
        return focused;
    }

    @Override
    public Set<String> getAssistiveTechnologyFocusedIdentifiers() {
        return null;
    }
}

