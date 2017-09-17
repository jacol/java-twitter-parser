import configuration.ConfigurationManager;
import rabbit.RabbitConnectionFactory;
import rabbit.RabbitListener;
import redis.RedisRepository;
import twitter.TwitterHarvester;
import twitter.TwitterListener;
import twitter.TwitterParser;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Application {
    public static void main(String [] args) throws IOException, TimeoutException {

        ConfigurationManager configurationManager = new ConfigurationManager();

        RedisRepository redisRepository = new RedisRepository(configurationManager);
        RabbitConnectionFactory rabbitConnectionFactory = new RabbitConnectionFactory(configurationManager);
        RabbitListener rabbitListener = new RabbitListener(rabbitConnectionFactory);
        TwitterListener twitterListener = new TwitterListener(new TwitterParser(), configurationManager);

        try {

            new TwitterHarvester(redisRepository, rabbitListener, twitterListener).run();

            while (System.in.available() == 0) {
                // Do whatever you want
            }

        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            redisRepository.close();
            rabbitListener.close();
            rabbitConnectionFactory.close();
        }
    }
}
