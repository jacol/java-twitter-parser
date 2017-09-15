package rabbit;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitListener {

    private RabbitConnectionFactory rabbitConnectionFactory;
    private boolean disposed;

    public RabbitListener(RabbitConnectionFactory rabbitConnectionFactory){

        this.rabbitConnectionFactory = rabbitConnectionFactory;
    }



    public void close() throws IOException, TimeoutException {
        rabbitConnectionFactory.close();
        disposed = true;
    }

    @Override
    public void finalize() throws Throwable {
        if(!disposed) {
            close();
            super.finalize();
        }
    }
}
