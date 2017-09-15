package rabbit;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitListener {

    private RabbitConnectionFactory rabbitConnectionFactory;
    private RabbitEventHandler rabbitEventHandler;
    private boolean disposed;

    public RabbitListener(RabbitConnectionFactory rabbitConnectionFactory){

        this.rabbitConnectionFactory = rabbitConnectionFactory;
    }

    public void startListening() throws IOException, TimeoutException {
        Channel channel = rabbitConnectionFactory.CreateConnection();

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");

                System.out.println(" [x] Received '" + message + "'");

                if(rabbitEventHandler != null){
                    try {
                        rabbitEventHandler.handleEvent(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        channel.basicConsume(RabbitConnectionFactory.KEYWORDS_QUEUE_NAME, true, consumer);
    }

    public void addEventHandler(RabbitEventHandler rabbitEventHandler){
        this.rabbitEventHandler = rabbitEventHandler;
    }

    public void removeEventHandler(){
        rabbitEventHandler = null;
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
