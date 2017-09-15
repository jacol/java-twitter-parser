import rabbit.RabbitConnectionFactory;
import rabbit.RabbitListener;
import redis.RedisRepository;
import twitter.TwitterHarvester;
import twitter.TwitterListener;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Application {
    public static void main(String [] args) throws IOException, TimeoutException {

        RedisRepository redisRepository = new RedisRepository();
        RabbitConnectionFactory rabbitConnectionFactory = new RabbitConnectionFactory();
        RabbitListener rabbitListener = new RabbitListener(rabbitConnectionFactory);
        TwitterListener twitterListener = new TwitterListener();

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
