package redis;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;
import configuration.ConfigurationManager;
import twitter.TwitterEventHandler;

public class RedisRepository implements TwitterEventHandler {

    private RedisClient redisClient;
    private StatefulRedisConnection<String, String> connection;
    private RedisCommands<String, String> syncCommands;

    private boolean disposed = false;
    private ConfigurationManager configurationManager;

    public RedisRepository(ConfigurationManager configurationManager) {

        this.configurationManager = configurationManager;
    }

    public void connect(){
        redisClient = RedisClient.create(configurationManager.GetProperty("redis_addr"));
        connection = redisClient.connect();
        syncCommands = connection.sync();

        System.out.println("Connected to Redis");
    }

    public void close(){
        connection.close();
        redisClient.shutdown();
        disposed = true;
        System.out.println("Redis disposed");
    }

    @Override
    protected void finalize() throws Throwable {
        if(!disposed){
            close();
            super.finalize();
        }
    }

    public void handleEvent(String keyword, String twitterMsg) {
        syncCommands.lpush(keyword, twitterMsg);
    }
}

