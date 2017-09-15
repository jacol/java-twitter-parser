import rabbit.RabbitPoster;
import redis.RedisRepository;
import twitter.TwitterHarvester;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Application {
    public static void main(String [] args) throws IOException, TimeoutException {

        RedisRepository redisRepository = new RedisRepository();
        RabbitPoster rabbitPoster = new RabbitPoster();

        try {
            new TwitterHarvester(redisRepository, rabbitPoster).run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            redisRepository.close();
            rabbitPoster.close();
        }
    }
}
