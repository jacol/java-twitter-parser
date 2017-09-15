package twitter;

public interface TwitterEventHandler {
    public void handleEvent(String keyword, String twitterMsg);
}
