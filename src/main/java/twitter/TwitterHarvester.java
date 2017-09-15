package twitter;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.json.JSONObject;
import rabbit.RabbitListener;
import redis.RedisRepository;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;

public class TwitterHarvester {

    private RedisRepository redisRepo;
    private RabbitListener rabbitListener;
    private TwitterListener twitterListener;

    public TwitterHarvester(RedisRepository redisRepo, RabbitListener rabbitListener, TwitterListener twitterListener) throws IOException, TimeoutException {

        this.redisRepo = redisRepo;
        this.rabbitListener = rabbitListener;
        this.twitterListener = twitterListener;

    }

    public void run() throws IOException, TimeoutException {
        redisRepo.connect();

        twitterListener.addEventHandler(redisRepo);

        rabbitListener.addEventHandler(twitterListener);
        rabbitListener.startListening();
    }

}
