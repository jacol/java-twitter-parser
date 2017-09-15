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
import rabbit.RabbitPoster;
import redis.RedisRepository;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;

public class TwitterHarvester {

    private RedisRepository redisRepo;
    private RabbitPoster rabbitPoster;

    public TwitterHarvester(RedisRepository redisRepo, RabbitPoster rabbitPoster) throws IOException, TimeoutException {

        this.redisRepo = redisRepo;
        this.rabbitPoster = rabbitPoster;
        redisRepo.connect();

    }

    public void run() throws InterruptedException {
        String keyword = "putin";

        /** Set up your blocking queues: Be sure to size these properly based on expected TPS of your stream */
        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(100000);
        BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<Event>(1000);

        /** Declare the host you want to connect to, the endpoint, and authentication (basic auth or oauth) */
        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();

        // Optional: set up some followings and track terms
        List<Long> followings = Lists.newArrayList(1000L, 999999999L);
        List<String> terms = Lists.newArrayList(keyword);


        hosebirdEndpoint.followings(followings);
        hosebirdEndpoint.trackTerms(terms);

        // These secrets should be read from a config file
        Authentication hosebirdAuth = new OAuth1("zouRZe8Adl0bGYp8eoQvSfwjy", "GysVTr0g8A7NXbnAzKKeWa84tl5yDQHuI1aGxkoFWw7D7VhvP7"
                , "891399162834636800-592KFUlgFWbs6xhcz7rxhV7yGlz3DCz", "109zy35I6uccayVcCwFvbHjirzDyDfdK9HnDXbsmtACkS");

        ClientBuilder builder = new ClientBuilder()
                .name("Hosebird-Client-01")                              // optional: mainly for the logs
                .hosts(hosebirdHosts)
                .authentication(hosebirdAuth)
                .endpoint(hosebirdEndpoint)
                .processor(new StringDelimitedProcessor(msgQueue))
                .eventMessageQueue(eventQueue);                          // optional: use this if you want to process client events

        Client hosebirdClient = builder.build();
        // Attempts to establish a connection.
        hosebirdClient.connect();

        // on a different thread, or multiple different threads....
        while (!hosebirdClient.isDone()) {
            String msg = msgQueue.take();

            JSONObject obj = new JSONObject(msg);
            String text = obj.getString("text");

            redisRepo.addData(keyword + "_" + java.util.UUID.randomUUID().toString(), text);

            System.out.println(text);
        }

    }
}
