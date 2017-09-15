package rabbit;

public interface RabbitEventHandler {
    public void handleEvent(String message) throws InterruptedException;
}
