package plugins.quorum.Libraries.Game;

import android.util.Log;
import android.view.MotionEvent;
import java.util.HashMap;
import quorum.Libraries.Containers.List_;
import quorum.Libraries.Interface.Events.TouchEvent;
import quorum.Libraries.Interface.Events.TouchEvent_;

/**
 *
 * @author alleew
 */
public class AndroidInput 
{
    public java.lang.Object me_ = null;
    
    private List_ touchEvents;
    private final HashMap<Integer, Coordinates> map = new HashMap<>();
    
    public void InitializePlugin(List_ list)
    {
        touchEvents = list;
    }
    
    public void AddEvent(TouchEvent_ event)
    {
        touchEvents.Add(event);
    }
    
    public void AddEvent(MotionEvent event)
    {
        /*
        public boolean onTouchEvent(MotionEvent event){
            if (this.mDetector.onTouchEvent(event)) {
              return true;
            }
            return super.onTouchEvent(event);
        } */
//        AndroidGestures gesture = new AndroidGestures();
//        gesture.PrintStuff("New gesture created in AddEvent");
//        if(event != null && gesture.onTouchEvent(event))
//            gesture.PrintStuff("Made it back to AddEvent call after sending event to Java");
        TouchEvent_[] events = ConvertToQuorumEvents(event);
        for (TouchEvent_ e : events)
            AddEvent(e);
    }
    
    public TouchEvent_[] ConvertToQuorumEvents(MotionEvent e)
    {
        /*
        TouchEvent_[] eventArray = new TouchEvent_[e.getPointerCount()];
        
        for (int i = 0; i < e.getPointerCount(); i++)
        {
            TouchEvent event = new TouchEvent();

            int id = e.getPointerId(i);
            
            int x = (int)e.getRawX();
            int y = (int)e.getRawY();

            event.x = x;
            event.y = y;
            event.fingerID = id;
            Coordinates coordinates;
            */
            TouchEvent_[] eventArray;
            TouchEvent event;
            int id;
            int x;
            int y;
            Coordinates coordinates;
            switch(e.getActionMasked())
            {
                    case (MotionEvent.ACTION_DOWN):
                    case (MotionEvent.ACTION_POINTER_DOWN):
                        eventArray = new TouchEvent_[1];
                        event = new TouchEvent();
                        id = e.getPointerId(e.getActionIndex());
            
                        x = (int)e.getRawX();
                        y = (int)e.getRawY();

                        event.x = x;
                        event.y = GameStateManager.display.GetHeight() - y;
                        event.fingerID = id;
                        event.eventType = event.BEGAN;
                        map.put(id, new Coordinates(x, y));
                        event.movementX = 0;
                        event.movementY = 0;
                        eventArray[0] = event;
                        break;
                    case (MotionEvent.ACTION_MOVE):
                    eventArray = new TouchEvent_[e.getPointerCount()];

                    for (int i = 0; i < e.getPointerCount(); i++)
                    {
                        event = new TouchEvent();

                        id = e.getPointerId(i);

                        x = (int)e.getRawX();
                        y = (int)e.getRawY();

                        event.x = x;
                        event.y = y;
                        event.fingerID = id;
                        event.eventType = event.MOVED;
                        Log.d("TESTING", "ACTION_MOVE " + id);
                        coordinates = map.get(id);
                        event.movementX = x - coordinates.x;
                        event.movementY = y - coordinates.y;
                        coordinates.x = x;
                        coordinates.y = GameStateManager.display.GetHeight() - y;
                        eventArray[i] = event;
                    }
                    break;
                    case (MotionEvent.ACTION_UP):
                    case (MotionEvent.ACTION_POINTER_UP):
                        eventArray = new TouchEvent_[1];
                        event = new TouchEvent();
                        id = e.getPointerId(e.getActionIndex());
            
                        x = (int)e.getRawX();
                        y = (int)e.getRawY();
                        event.eventType = event.ENDED;
                        Log.d("TESTING", "ON POINTER_UP " + id);
                        coordinates = map.get(id);
                        event.movementX = x - coordinates.x;
                        event.movementY = y - coordinates.y;
                        event.x = x;
                        event.y = GameStateManager.display.GetHeight() - y;
                        map.remove(id);
                        eventArray[0] = event;
                        break;
                    case (MotionEvent.ACTION_CANCEL):
                        eventArray = new TouchEvent_[e.getPointerCount()];

                    for (int i = 0; i < e.getPointerCount(); i++)
                    {
                        event = new TouchEvent();

                        id = e.getPointerId(i);

                        x = (int)e.getRawX();
                        y = (int)e.getRawY();

                        event.x = x;
                        event.y = y;
                        event.fingerID = id;
                        event.eventType = event.CANCELLED;
                        coordinates = map.get(id);
                        event.movementX = x - coordinates.x;
                        event.movementY = y - coordinates.y;
                        map.remove(id);
                        eventArray[i] = event;
                    }
                        break;
                    case (MotionEvent.ACTION_OUTSIDE):
                    default:
                        eventArray = new TouchEvent_[1];
                        event = new TouchEvent();
                        x = (int)e.getRawX();
                        y = (int)e.getRawY();
                        for(int i = 0; i < e.getPointerCount(); i++)
                        {
                        if (!map.containsKey(e.getPointerId(i)))
                            map.put(e.getPointerId(i), new Coordinates(x, y));
                        }
                        event.eventType = -1;
                        eventArray[0] = event;
            }
        
        return eventArray;
    }
    
    public List_ GetEvents()
    {
        return touchEvents;
    }
    
    private class Coordinates
    {
        public int x;
        public int y;
        
        public Coordinates(int x, int y)
        {
            this.x = x;
            this.y = y;
        }
    }
}
