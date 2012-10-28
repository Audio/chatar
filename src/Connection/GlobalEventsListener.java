package Connection;

import java.util.EventListener;


public interface GlobalEventsListener extends EventListener {

    public void awayStatusChanged(boolean isAway);

}
