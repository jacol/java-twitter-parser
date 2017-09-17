package rabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import configuration.ConfigurationManager;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitConnectionFactory {

    public final static String KEYWORDS_QUEUE_NAME = "keywords";

    private Channel channel;
    private Connection connection;
    private boolean disposed;
    private ConfigurationManager configurationManager;

    public RabbitConnectionFactory(ConfigurationManager configurationManager) {

        this.configurationManager = configurationManager;
    }

    public Channel CreateConnection() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(configurationManager.GetProperty("rabbitmq_addr"));

        connection = factory.newConnection();
        channel = connection.createChannel();
        return channel;
    }

    public void close() throws IOException, TimeoutException {
        channel.close();
        connection.close();
        disposed = true;
    }

    @Override
    public void finalize() throws Throwable {
        if(!disposed){
            close();
            super.finalize();
        }
    }
}
