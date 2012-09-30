package Connection;

import java.util.EventListener;


public interface MyNickChangeListener extends EventListener {

    public void myNickHasChanged(String newNickname);

}
