package Settings;

import java.util.EventListener;


public interface SettingsChangesListener extends EventListener {

    public void chatLoggingChanged(boolean enabled);
    public void topicVisibilityChanged(boolean visible);
    public void timestampFormatChanged(String format);

}
