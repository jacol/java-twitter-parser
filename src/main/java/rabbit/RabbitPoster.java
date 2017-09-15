package rabbit;

import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitPoster {

    private RabbitConnectionFactory rabbitConnectionFactory;
    private boolean disposed;

    public RabbitPoster(RabbitConnectionFactory rabbitConnectionFactory){

        this.rabbitConnectionFactory = rabbitConnectionFactory;
    }

    public void publishKeywords(String [] keywords) throws IOException, TimeoutException {

        Channel channel = rabbitConnectionFactory.CreateConnection();
        channel.queueDeclare(RabbitConnectionFactory.KEYWORDS_QUEUE_NAME, false, false, false, null);

        for (int i=0;i<keywords.length;i++){
            String message = keywords[i];
            channel.basicPublish("", RabbitConnectionFactory.KEYWORDS_QUEUE_NAME, null, message.getBytes());
            System.out.println("[" + message + "] uploaded!");
        }
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
